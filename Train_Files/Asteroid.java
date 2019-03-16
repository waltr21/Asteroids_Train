import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math;


public class Asteroid{
    double x, y, size, angle, maxLevel;
    int level;
    Vector velocity;
    static int width = 900;
    static int height = 900;
    final double PI = 3.14159265359;
    Locals locals;

    /**
     * Constructor for the Asteroid class.
     * @param x     X pos of the asteroid
     * @param y     Y pos of the asteroid
     * @param level Level of asteroid (1-3)
     */
    public Asteroid(double x, double y, int level, Locals l){
        locals = l;
        this.x = x;
        this.y = y;
        this.size = 30 *level;
        this.level = level;
        this.maxLevel = 3;
        this.angle = ThreadLocalRandom.current().nextDouble(-PI, PI);
        this.velocity = Vector.fromAngle(angle);
        this.velocity.mult( (float) ((maxLevel+1) - level) * 0.8);
    }

    public Asteroid(double x, double y, double a, int level, Locals l){
        locals = l;
        this.x = x;
        this.y = y;
        this.size = 40 * level;
        this.level = level;
        this.maxLevel = 3;
        this.angle = a;
        this.velocity = Vector.fromAngle(angle);
        this.velocity.mult(((maxLevel+1) - level) * 0.8);
    }


    /**
     * Bound the Asteroid to stay inside of the screen.
     */
    public void bound(){
        if (x + size < 0){
            x = width + size/2;
        }
        else if (x - size > width){
            x = 0 - size/2;
        }

        if (y + size < 0){
            y = height + size/2;
        }
        else if (y - size > height){
            y = 0 - size/2;
        }
    }

    public void travel(){
        x += velocity.x;
        y += velocity.y;
    }

    public void checkHit(){
        double distance = dist(x, y, locals.player.getX(), locals.player.getY());
        //System.out.println(distance);
        if (distance < size/2 + locals.player.getSize()){
            locals.player.setHit();
        }
    }

    public void explode(){
        locals.player.numHits = locals.player.numHits + 1;
        if (level > 1){

            for (int i = 0; i < 2; i++){
                Asteroid newAsteroid = new Asteroid(x, y, level - 1, locals);
                locals.asteroids.add(newAsteroid);
            }
            locals.asteroids.remove(this);
        }
        else{
            locals.asteroids.remove(this);
        }

    }

    public int getScore(){
        if (level == 1)
            return 100;
        else if (level == 2)
            return 50;
        else
            return 20;

    }


    public void show(){
        // pushMatrix();
        checkHit();
        travel();
        bound();
        // noFill();
        // stroke(255);
        // ellipseMode(CENTER);
        // translate(x, y);
        // ellipse(0, 0, size, size);
        // popMatrix();
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public int getLevel(){
        return level;
    }

    public double getAngle(){
        return angle;
    }

    public void setAngle(double a){
        angle = a;
    }

    public double getSize(){
        return size;
    }

    private double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }
}
