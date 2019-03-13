import java.lang.Math;


public class Bullet{
    double x, y, angle, size;
    int count;
    boolean owner;
    Vector velocity;
    final double PI = 3.14159265359;
    Locals locals;

    public Bullet(double x, double y, double angle, Locals l){
        locals = l;
        this.x = x;
        this.y = y;
        this.angle = angle - PI/2;
        this.size = 5;
        this.count = 0;
        this.owner = true;
        this.velocity = Vector.fromAngle(this.angle);
        this.velocity.mult(8);
    }

    public void travel(){
        x += velocity.x;
        y += velocity.y;
    }

    public void setOwner(boolean b){
        owner = b;
    }

    public boolean isOwner(){
        return owner;
    }

    public boolean bound(){
        if (x + size < 0){
            x = width + size;
        }
        else if (x - size > width){
            x = 0 - size;
        }

        if (y + size < 0){
            y = height + size;
        }
        else if (y - size > height){
            y = 0 - size;
        }

        if(count > 900){
            return true;
        }
        else{
            count += 8;
            return false;
        }
    }

    public void checkHit(){
        for (Asteroid a : locals.asteroids){
            double distance = dist(a.getX(), a.getY(), x, y);
            if (distance < a.getSize()/2 + size/2){
                locals.player.addScore(a.getScore());
                a.explode();
                count = 1000;
                break;
            }
        }
    }

    public void show(){
        pushMatrix();
        ellipseMode(CENTER);
        translate((float) x, (float) y);
        travel();
        noStroke();
        fill(255);
        ellipse(0, 0, (float) size, (float) size);
        popMatrix();
        if (owner)
            checkHit();
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    private double dist(double x1, double y1, double x2, double y2){
        return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

}
