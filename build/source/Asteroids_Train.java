import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.ArrayList; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Asteroids_Train extends PApplet {



Ship player;
ArrayList<Asteroid> asteroids;
GameScene GS;
int level;

public void setup(){
    
    frameRate(60);
    player = new Ship();
    asteroids = new ArrayList<Asteroid>();
    GS = new GameScene();
    level = 10;
}

public void draw(){
    GS.show();
    // System.out.println(player.score + " - " + player.dead);
}

/**
 * Button released handle.
 */
public void keyReleased(){
    int code;
    if (keyCode > 40){
        code = PApplet.parseInt(Character.toLowerCase(key));
    }
    else{
        code = keyCode;
    }
    player.processButtonReleased(PApplet.parseInt(Character.toLowerCase(code)));
}

/**
 * Button pressed handle.
 */
public void keyPressed(){
    // char k = key;
    //System.out.println(keyCode);
    int code;
    if (keyCode > 40){
        code = PApplet.parseInt(Character.toLowerCase(key));
    }
    else{
        code = keyCode;
    }


    player.processButtonPress(code);
}
public class Asteroid{
    float x, y, size, angle, maxLevel;
    int level;
    String aID;
    Vector velocity;

    /**
     * Constructor for the Asteroid class.
     * @param x     X pos of the asteroid
     * @param y     Y pos of the asteroid
     * @param level Level of asteroid (1-3)
     */
    public Asteroid(float x, float y, int level){
        this.x = x;
        this.y = y;
        this.size = 30 * level;
        this.level = level;
        this.maxLevel = 3;
        this.angle = random(-PI, PI);
        this.velocity = Vector.fromAngle(angle);
        this.velocity.mult( (float) ((maxLevel+1) - level) * 0.8f);
        while(true){
            boolean repeat = false;
            aID = getRandID();
            for(Asteroid a : asteroids){
                if(a.getID().equals(aID)){
                    repeat = true;
                }
            }

            if (!repeat)
                break;
        }
    }

    public Asteroid(float x, float y, float a, int level, String aID){
        this.x = x;
        this.y = y;
        this.size = 30 * level;
        this.level = level;
        this.maxLevel = 3;
        this.angle = a;
        this.velocity = Vector.fromAngle(angle);
        this.velocity.mult(((maxLevel+1) - level) * 0.8f);
        this.aID = aID;
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
        float distance = dist(x, y, player.getX(), player.getY());
        //System.out.println(distance);
        if (distance < size/2 + player.getSize()){
            player.setHit();
        }
    }

    public void explode(){
        if (level > 1){

            for (int i = 0; i < 2; i++){
                Asteroid newAsteroid = new Asteroid(x, y, level - 1);
                asteroids.add(newAsteroid);
            }
            asteroids.remove(this);
        }
        else{
            asteroids.remove(this);
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
        pushMatrix();
        checkHit();
        travel();
        bound();
        noFill();
        stroke(255);
        ellipseMode(CENTER);
        translate(x, y);
        ellipse(0, 0, size, size);
        popMatrix();
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public String getID(){
        return aID;
    }

    public int getLevel(){
        return level;
    }

    public float getAngle(){
        return angle;
    }

    public void setAngle(float a){
        angle = a;
    }

    public float getSize(){
        return size;
    }

    public String getRandID(){
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String word = "";
        for(int i = 0; i < 3; i++){
            word += chars.charAt(PApplet.parseInt(random(chars.length())));
        }
        return word;
    }
}
public class Bullet{
    float x, y, angle, size;
    int count;
    boolean owner;
    Vector velocity;

    public Bullet(float x, float y, float angle){
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
        for (Asteroid a : asteroids){
            float distance = dist(a.getX(), a.getY(), x, y);
            if (distance < a.getSize()/2 + size/2){
                player.addScore(a.getScore());
                a.explode();
                count = 1000;
                break;
            }
        }
    }

    public void show(){
        pushMatrix();
        ellipseMode(CENTER);
        translate(x, y);
        travel();
        noStroke();
        fill(255);
        ellipse(0, 0, size, size);
        popMatrix();
        if (owner)
            checkHit();
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

}
public class GameScene{
    boolean online;

    public GameScene(){
        level = 1;
        resetAstroids(level);
        player = new Ship();
    }

    /**
     * Show the text of the scene
     */
    private void showText(){
        textAlign(CENTER);
        textSize(30);
        String levelString = "Level " + level + "\n" + player.getScore();
        fill(255);
        textSize(30);
        text(levelString, width/2, 50);
        String liveString = "";
        for (int i = 0; i < player.getLives(); i++){
            liveString += " | ";
        }
        text(liveString, width - 50, 50);
    }

    private void resetAstroids(int level){
        asteroids.clear();
        int num = 2 + (level * 2);
        for (int i = 0; i < num; i++){
            float tempX = random(50, width - 50);
            float tempY = random(50, height/2 - 150) + ((height/2 + 150) * PApplet.parseInt(random(0, 2)));
            asteroids.add(new Asteroid(tempX, tempY, 3));
            player.resetPos();
            player.clearBullets();
        }
    }

    /**
     * Display all of the asteroids to the screen.
     */
    private void showAsteroids(){
        try{
           for (Asteroid a : asteroids){
               a.show();
           }
        }
        catch(Exception e){
            System.out.println("Exception in showAsteroids (gameScene): " + e);
        }
    }

    private boolean checkLevel(){
        if (asteroids.size() < 1){
            level++;
            resetAstroids(level);
            return true;
        }
        return false;
    }

    public void show(){
        background(0);
        showText();
        player.show();
        showAsteroids();
        checkLevel();
    }
}
public class Ship{
    float x, y, size, angle, turnRadius, deRate;
    ArrayList<Integer> pressedChars;
    ArrayList<Bullet> bullets;
    boolean turn, accelerate, dead, noHit;
    long timeStamp;
    int k;
    int lives, maxLives, score;
    Vector velocity;

    /**
     * Constuctor for the ship class.
     * @param a Asteroids in the game for the ship to reference.
     */
    public Ship(){
        //X and Y for the ship.
        this.x = width/2;
        this.y = height/2;
        //Size of the ship.
        this.size = 20;
        this.angle = 0;
        this.turnRadius = 0.1f;
        this.deRate = 0.05f;
        this.turn = false;
        this.accelerate = false;
        this.dead = false;
        this.noHit = false;
        this.maxLives = 1;
        this.lives = maxLives;
        this.score = 0;
        //ArrayList for the current pressed characters.
        //(Mainly used for making turning less janky.)
        this.pressedChars = new ArrayList<Integer>();
        this.bullets = new ArrayList<Bullet>();
        this.velocity = new Vector();
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
                angle -= turnRadius;
            }
            if (k == 'd' || k == 39){
                angle += turnRadius;
            }
        }
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
        velocity.mult(0.99f);
    }

    private void hyperDrive(){
        x = random(20, width - 20);
        y = random(20, height - 20);
    }

    /**
     * Accelerates the ship in the current faced direction.
     */
    private void accelerate(){
        if (accelerate){
            Vector force = Vector.fromAngle(angle - PI/2);
            //Limit how strong the force is.
            force.mult(0.08f);
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
            addBullet(new Bullet(x, y, angle));
        }
    }

    public void addScore(int s){
        score += s;
    }

    public void addBullet(Bullet b){
        bullets.add(b);
    }

    /**
     * Display the ship to the screen.
     */
    public boolean show(){
        if (!dead){
            checkNoHit();
            pushMatrix();
            // Display the bullets
            showBullets();

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
            translate(x, y);
            rotate(angle);

            triangle(-size, size, 0, -size - 5, size, size);

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

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getAngle(){
        return angle;
    }

    public float getSize(){
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
    public void settings() {  size(1000, 900, OPENGL); }
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Asteroids_Train" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
