import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Ship{
    double x, y, size, angle, turnRadius, deRate;
    ArrayList<Integer> pressedChars;
    ArrayList<Bullet> bullets;
    ArrayList<Sensor> sensors;
    boolean turn, accelerate, dead, noHit;
    long timeStamp;
    int k;
    int lives, maxLives, score, avoids;
    Vector velocity;
    final double PI = 3.14159265359;
    Locals locals;

    /**
     * Constuctor for the ship class.
     * @param a Asteroids in the game for the ship to reference.
     */
    public Ship(Locals l){
        locals = l;
        //X and Y for the ship.
        this.x = width/2;
        this.y = height/2;
        //Size of the ship.
        this.size = 20;
        this.angle = 0;
        this.turnRadius = 0.1;
        this.deRate = 0.05;
        this.turn = false;
        this.accelerate = false;
        this.dead = false;
        this.noHit = false;
        this.maxLives = 1;
        this.lives = maxLives;
        this.score = 0;
        this.avoids = 0;
        //ArrayList for the current pressed characters.
        //(Mainly used for making turning less janky.)
        this.pressedChars = new ArrayList<Integer>();
        this.bullets = new ArrayList<Bullet>();
        this.velocity = new Vector();
        this.sensors = new ArrayList<Sensor>();
        sensors.add(new Sensor(0, locals));
        sensors.add(new Sensor(PI, locals));
        sensors.add(new Sensor(3*PI/2, locals));
        sensors.add(new Sensor(PI/2, locals));
        sensors.add(new Sensor(PI/4, locals));
        sensors.add(new Sensor(3*PI/4, locals));
        sensors.add(new Sensor(7*PI/4, locals));
        sensors.add(new Sensor(5*PI/4, locals));
        sensors.add(new Sensor(22.5*(PI/180), locals));
        sensors.add(new Sensor(-22.5*(PI/180), locals));
        sensors.add(new Sensor(-67.5*(PI/180), locals));
        sensors.add(new Sensor(67.5*(PI/180), locals));
        sensors.add(new Sensor(112.5*(PI/180), locals));
        sensors.add(new Sensor(-112.5*(PI/180), locals));
        sensors.add(new Sensor(-157.5*(PI/180), locals));
        sensors.add(new Sensor(157.5*(PI/180), locals));



    }

    /**
     * Removes one of the current pressed characters from the Array.
     * If there are no characters left then we stop turning.
     * @param k key entered by the user.
     */
    private void freezeTurn(int code){

        //Loop through and find characters we should remove.
        for (int i = pressedChars.size() - 1; i >= 0; i--){
            if (pressedChars.get(i) == code){
                pressedChars.remove(i);
            }
        }

        //If there are no more charaters then we should stop turning.
        if(pressedChars.size() < 1)
            turn = false;
    }

    /**
     * Tell the ship to start to turn in the appropriate direction.
     * @param k key entered by the user.
     */
    private void setTurn(int code){
        //System.out.println("Turn");
        turn = true;
        this.k = code;
        pressedChars.add(this.k);
    }

    /**
     * Turn the ship X radians.
     */
    private void turn(){
        //Make sure we are in the scene and we should be turning.
        if (turn){
            if (k == 'a' || k == 37){
                turnLeft();
            }
            if (k == 'd' || k == 39){
                turnRight();
            }
        }

        if (angle > 2*PI){
            angle = angle - (2*PI);
        }
        if (angle < 0){
            angle = (2*PI) + angle;
        }
    }

    private void turnLeft(){
        angle -= turnRadius;
    }

    private void turnRight(){
        angle += turnRadius;
    }

    /**
     * Bound the player to stay inside of the screen.
     */
    private void bound(){
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
    }

    public void resetPos(){
        x = width/2;
        y = height/2;
        velocity.mult(0);
        angle = 0;
    }

    public void resetVars(){
        lives = maxLives;
        dead = false;
        score = 0;
        bullets.clear();
    }

    public void clearBullets(){
        bullets.clear();
    }

    /**
     * Move in the direction of the current velocity.
     */
    private void move(){
        x += velocity.x;
        y += velocity.y;

        //Adds "friction" to slow down the ship.
        velocity.mult(0.99);
    }

    private void hyperDrive(){
        x = ThreadLocalRandom.current().nextInt(20, width - 20);
        y = ThreadLocalRandom.current().nextInt(20, height - 20);
    }

    /**
     * Accelerates the ship in the current faced direction.
     */
    private void accelerate(){
        if (accelerate){
            Vector force = Vector.fromAngle(angle - PI/2);
            //Limit how strong the force is.
            force.mult(0.08);
            velocity.add(force);
        }
    }

    /**
     * Display the bullets and remove if out of the screen.
     */
    private void showBullets(){
        for (int i = bullets.size() - 1; i >= 0; i--){
            Bullet b = bullets.get(i);
            b.show();

            //If the bullet is out of the screen then we want to remove it.
            if (b.bound()){
                bullets.remove(i);
            }
        }
    }

    public void setHit(){
        if (!noHit){
            lives--;
            if (lives < 1){
                dead = true;
            }
            else{
                noHit = true;
                timeStamp = millis();
                resetPos();
            }
        }
    }

    public void setAlive(){
        dead = false;
    }

    private void checkNoHit(){
        if(noHit){
            if (millis() - timeStamp > 3000)
                noHit = false;
        }
    }

    private void shoot(){
        if (bullets.size() < 4 && !dead){
            addBullet(new Bullet(x, y, angle, locals));
        }
    }

    public void addScore(int s){
        score += s;
    }

    public void addBullet(Bullet b){
        bullets.add(b);
    }

    public long millis(){
        return System.currentTimeMillis() % 1000;
    }

    public void showSensors(){
        for (Sensor s : sensors){
            s.show(x, y,  angle);
        }
    }

    /**
     * Display the ship to the screen.
     */
    public boolean show(){
        if (!dead){
            checkNoHit();
            pushMatrix();
            //Display the bullets
            showBullets();
            //showSensors();

            //Edit pos of the ship.
            turn();
            move();
            accelerate();
            bound();

            noFill();
            stroke(255, 255, 102);
            if (noHit)
                stroke(56, 252, 159);
            strokeWeight(3);
            translate((float)x, (float)y);
            rotate((float)angle);

            triangle((float)-size, (float)size, 0, (float)-size - 5, (float)size, (float)size);

            popMatrix();
            return true;
        }
        // resetVars();
        resetPos();
        return false;
    }

    /**
     * Handle the button pressed by the user.
     * @param k key entered by the user.
     */
    public void processButtonPress(int code){
        if (code == 97 || code == 100 || code == 37 || code == 39){
            setTurn(code);
        }

        if (code == 119 || code == 38){
            accelerate = true;
        }

        if (code == 32){
            shoot();
        }

        if (code == 115 || code == 40){
            hyperDrive();
        }
    }

    /**
     * Handle the button released by the user.
     * @param k key entered by the user.
     */
    public void processButtonReleased(int code){
        if (code == 97 || code == 100 || code == 37 || code == 39){
            freezeTurn(code);
        }
        if (code == 119 || code == 38){
            accelerate = false;
        }
    }

    public void processClick(){
        shoot();
    }

    public ArrayList<Sensor> getSensors(){
        return sensors;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getAngle(){
        return angle;
    }

    public double getSize(){
        return size;
    }

    public int getLives(){
        return lives;
    }

    public void setLives(int l){
        lives = l;
        maxLives = l;
    }

    public int getScore(){
        return score;
    }
}
