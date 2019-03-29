import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.ArrayList; 
import java.util.ArrayList; 
import java.util.concurrent.ThreadLocalRandom; 
import java.lang.Math; 
import java.lang.Math; 
import java.util.concurrent.ThreadLocalRandom; 
import java.util.ArrayList; 
import java.lang.Math; 
import java.util.ArrayList; 
import java.util.concurrent.ThreadLocalRandom; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Asteroids_Train extends PApplet {



GameScene GS;
Locals locals;

public void setup(){
    locals = new Locals();
    
    frameRate(60);
    locals.player = new Ship(locals);
    locals.asteroids = new ArrayList<Asteroid>();
    GS = new GameScene(locals);
    // locals.level = 10;
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
    locals.player.processButtonReleased(PApplet.parseInt(Character.toLowerCase(code)));
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


    locals.player.processButtonPress(code);
}





public class Asteroid{
    double x, y, size, angle, maxLevel;
    int level;
    Vector velocity;
    final double PI = 3.14159265359f;
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
        this.size = 40 * level;
        this.level = level;
        this.maxLevel = 3;
        this.angle = ThreadLocalRandom.current().nextDouble(-PI, PI);
        this.velocity = Vector.fromAngle(angle);
        this.velocity.mult( (float) ((maxLevel+1) - level) * 0.8f);
    }

    public Asteroid(double x, double y, double a, int level, Locals l){
        locals = l;
        this.x = x;
        this.y = y;
        this.size = 30 * level;
        this.level = level;
        this.maxLevel = 3;
        this.angle = a;
        this.velocity = Vector.fromAngle(angle);
        this.velocity.mult(((maxLevel+1) - level) * 0.8f);
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
        pushMatrix();
        checkHit();
        travel();
        bound();
        noFill();
        stroke(255);
        ellipseMode(CENTER);
        translate((float) x, (float) y);
        ellipse(0, 0, (float) size, (float) size);
        popMatrix();
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



public class Bullet{
    double x, y, angle, size;
    int count;
    boolean owner;
    Vector velocity;
    final double PI = 3.14159265359f;
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


public class GameScene{
    boolean online;
    Locals locals;
    Simple_NEAT n;
    Grid gameGrid;

    public GameScene(Locals l){
        locals = l;
        locals.level = 3;
        resetAstroids(locals.level);
        gameGrid = new Grid(50, l);
        locals.player = new Ship(locals);
        n = new Simple_NEAT(gameGrid.getWidth() * gameGrid.getWidth() + 3, 4);
        Network temp  = Network.loadFromFile("/Users/ryanwalt/Downloads/CODE/Java/Processing/Asteroids_Train/best.net");
        n.addAgent(temp);
        n.setCurrentAgent(0);
    }

    /**
     * Show the text of the scene
     */
    private void showText(){
        textAlign(CENTER);
        textSize(30);
        String levelString = "Level " + locals.level + "\n" + locals.player.getScore();
        fill(255);
        textSize(30);
        text(levelString, width/2, 50);
        String liveString = "";
        for (int i = 0; i < locals.player.getLives(); i++){
            liveString += " | ";
        }
        text(liveString, width - 50, 50);
    }

    private void resetAstroids(int level){
        locals.asteroids.clear();
        int num = 2 + (level * 2);
        for (int i = 0; i < num; i++){
            float tempX = ThreadLocalRandom.current().nextInt(50, locals.width - 50);
            float tempY = ThreadLocalRandom.current().nextInt(50, locals.height/2 - 150) + ((locals.height/2 + 150) * ((ThreadLocalRandom.current().nextInt(0, 2))));
            locals.asteroids.add(new Asteroid(tempX, tempY, 3, locals));
            locals.player.resetPos();
            locals.player.clearBullets();
        }
    }

    /**
     * Display all of the asteroids to the screen.
     */
    private void showAsteroids(){
        try{
           for (Asteroid a : locals.asteroids){
               a.show();
           }
        }
        catch(Exception e){
            System.out.println("Exception in showAsteroids (gameScene): " + e);
        }
    }

    private boolean checkLevel(){
        if (locals.asteroids.size() < 1){
            locals.level++;
            resetAstroids(locals.level);
            return true;
        }
        return false;
    }

    public void show(){
        background(0);
        showText();
        // locals.player.showBullets();
        locals.player.show();
        runNetwork2();
        showAsteroids();
        checkLevel();
        gameGrid.show();
    }

    public void runNetwork(){
        float[] inputs = new float[33];
        int c = 0;
        for (Sensor s : locals.player.getSensors()){
            inputs[c] = (float) s.getWeightValue();
            c++;
        }
        for (Sensor s : locals.player.getSensors()){
            inputs[c] = (float) s.getLevelValue();
            c++;
        }

        inputs[32] = (float) locals.player.bullets.size() / 4;

        n.runCurrent(inputs);

        float[] outputs = n.getCurOutput();

       if (outputs[0] > 0.5f){
            locals.player.shoot();
        }
        if (outputs[1] >=0.5f){
            locals.player.turnLeft();
        }
        if (outputs[2] >= 0.5f){
            locals.player.turnRight();
        }
        if (outputs[3] <= 0.5f){
            locals.player.accelerate = true;
            locals.player.accelerate();
        }
        else{
            locals.player.accelerate = false;
        }

        String outs = "";
        for(float f : outputs){
            outs +=(f + ", ");
        }
        textSize(20);
        text(outs, width/2, height-50);

    }

    public void runNetwork2(){
        float[] inputs = new float[gameGrid.getWidth() * gameGrid.getWidth() + 3];
        inputs[0] = (float) locals.player.getX() / 900.0f;
        inputs[1] = (float) locals.player.getY() / 900.0f;
        inputs[2] = (float) locals.player.getAngle() / (2*3.14159265359f);
        int c = 3;
        for (int x = 0; x < gameGrid.getGrid().length; x++){
            for (int y = 0; y < gameGrid.getGrid().length; y++){
                inputs[c] = (float) gameGrid.getGrid()[x][y];
            }
        }

        n.runCurrent(inputs);

        float[] outputs = n.getCurOutput();

        if (outputs[0] > 0.5f){
            locals.player.shoot();
        }
        if (outputs[1] >=0.5f){
            locals.player.turnLeft();
        }
        if (outputs[2] >= 0.5f){
            locals.player.turnRight();
        }
        if (outputs[3] <= 0.5f){
            locals.player.accelerate = true;
            locals.player.accelerate();
        }
        else{
            locals.player.accelerate = false;
        }

        String outs = "";
        for(float f : outputs){
            outs +=(f + ", ");
        }
        textSize(20);
        text(outs, width/2, height-50);

    }
}
public class Grid{
    double[][] grid;
    Locals l;
    int cellWidth;


    //CellWidth should be an even multiple of the width and height of the screen.
    public Grid(int cellWidth, Locals l){
        this.l = l;
        this.cellWidth = cellWidth;
        int xSize = (int) l.width / cellWidth;
        int ySize = (int) l.height / cellWidth;
        grid = new double[xSize][ySize];
        resetGrid();
    }

    private void resetGrid(){
        for (int x = 0; x < grid.length; x++){
            for (int y = 0; y < grid.length; y++){
                grid[x][y] = 0.0f;
            }
        }
    }

    private void setAsteroids(){
        resetGrid();
        for (Asteroid a : l.asteroids){
            int xCell = (int) (a.getX() / cellWidth);
            int yCell = (int) (a.getY() / cellWidth);

            //Cap all coordinates
            if (xCell >= grid.length)
                xCell = grid.length-1;
            if (xCell < 0)
                xCell = 0;
            if (yCell >= grid[0].length)
                yCell = grid[0].length-1;
            if (yCell < 0)
                yCell = 0;

            grid[xCell][yCell] = a.getLevel() * 0.33f;
        }

        int xCell = (int) (l.player.getX() / cellWidth);
        int yCell = (int) (l.player.getY() / cellWidth);

        //Cap all coordinates
        if (xCell >= grid.length)
            xCell = grid.length-1;
        if (xCell < 0)
            xCell = 0;
        if (yCell >= grid[0].length)
            yCell = grid[0].length-1;
        if (yCell < 0)
            yCell = 0;

        grid[xCell][yCell] = -1;
    }

    public void show(){
        float squareSize = 200;

        stroke(255);
        fill(0, 0, 0);
        rect(10, 10, squareSize, squareSize);

        float subSize = squareSize / grid.length;

        setAsteroids();
        for (int x = 0; x < grid.length; x++){
            for (int y = 0; y < grid.length; y++){
                if (grid[x][y] > 0){
                    fill(255);
                    noStroke();
                    rectMode(CORNER);
                    rect(10 + subSize * x, 10 + subSize * y, subSize, subSize);
                }
                if (grid[x][y] < 0){
                    fill(255, 0, 0);
                    noStroke();
                    rectMode(CORNER);
                    rect(10 + subSize * x, 10 + subSize * y, subSize, subSize);
                }
            }
        }
    }

    public int getWidth(){
        return grid.length;
    }

    public double[][] getGrid(){
        return grid;
    }

}


public class Locals{
    Ship player;
    ArrayList<Asteroid> asteroids;
    GameScene GS;
    int level;
    int width;
    int height;

    public Locals(){
        width = 900;
        height = 900;
    }
}


public class Sensor{
    private double length, angle, weightValue, levelValue;
    private Locals locals;

    public Sensor(double angle, Locals l){
        this.length = 350;
        this.angle = angle;
        this.weightValue = 0.0f;
        this.levelValue = 0.0f;
        locals = l;
    }

    public void show(double x, double y, double angle){
        double x2 = x + (Math.cos(angle + this.angle) * length);
        double y2 = y + (Math.sin(angle + this.angle) * length);
        // System.out.println(angle);
        strokeWeight(2);
        stroke(255);
        line((float) x, (float) y, (float) x2, (float) y2);
        calculateIntersection(x,y,x2,y2);
    }

    public void calculateIntersection(double x0, double y0, double x1, double y1){
        double a = square(x1 - x0) + square(y1 - y0);

        double iX = 0.0f;
        double iY = 0.0f;
        weightValue = 0;
        levelValue = 0;

        for (Asteroid ast : locals.asteroids){
            double b = 2 * (x1-x0) * (x0 - ast.getX()) + 2 * (y1-y0) * (y0 - ast.getY());
            double c = square(x0-ast.getX()) + square(y0-ast.getY()) - square(ast.getSize()/2.0f);
            double t = (2*c) / ((-1*b) + Math.sqrt(square(b) - (4 * a * c)));

            if(t > 0 && t < 1 && square(b) - (4 * a * c) > 0){
                if (1 - t > weightValue){
                    weightValue = 1-t;
                    levelValue = ast.getLevel() * 0.333f;
                    iX = intersectX(x0, x1, t);
                    iY = intersectY(y0, y1, t);
                }
            }
        }
        if (weightValue > 0.0f){
            fill(255, 0, 0);
            noStroke();
            ellipse((float) iX, (float) iY, 15, 15);
        }
        // fill(255, 0, 0);
        // ellipse(iX, iY, 10, 10);
    }

    public double intersectX(double x0, double x1, double t){
        return (x1-x0) * t + x0;
    }

    public double intersectY(double y0, double y1, double t){
        return (y1-y0) * t + y0;
    }


    private double square(double x){
        return x * x;
    }

    public double getWeightValue(){
        return weightValue;
    }

    public double getLevelValue(){
        return levelValue;
    }
}



public class Ship{
    double x, y, size, angle, turnRadius, deRate;
    ArrayList<Integer> pressedChars;
    ArrayList<Bullet> bullets;
    ArrayList<Sensor> sensors;
    boolean turn, accelerate, dead, noHit;
    long timeStamp;
    int k;
    int lives, maxLives, score;
    Vector velocity;
    final double PI = 3.14159265359f;
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
        this.sensors = new ArrayList<Sensor>();
        sensors.add(new Sensor(0, locals));
        sensors.add(new Sensor(PI, locals));
        sensors.add(new Sensor(3*PI/2, locals));
        sensors.add(new Sensor(PI/2, locals));
        sensors.add(new Sensor(PI/4, locals));
        sensors.add(new Sensor(3*PI/4, locals));
        sensors.add(new Sensor(7*PI/4, locals));
        sensors.add(new Sensor(5*PI/4, locals));
        sensors.add(new Sensor(22.5f*(PI/180), locals));
        sensors.add(new Sensor(-22.5f*(PI/180), locals));
        sensors.add(new Sensor(-67.5f*(PI/180), locals));
        sensors.add(new Sensor(67.5f*(PI/180), locals));
        sensors.add(new Sensor(112.5f*(PI/180), locals));
        sensors.add(new Sensor(-112.5f*(PI/180), locals));
        sensors.add(new Sensor(-157.5f*(PI/180), locals));
        sensors.add(new Sensor(157.5f*(PI/180), locals));



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
        velocity.mult(0.99f);
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
    public void settings() {  size(900, 900, OPENGL); }
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "Asteroids_Train" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
