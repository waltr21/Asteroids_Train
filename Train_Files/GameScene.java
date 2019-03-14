import java.util.concurrent.ThreadLocalRandom;

public class GameScene{
    boolean online;
    Locals locals;

    public GameScene(Locals l){
        locals = l;
        resetAstroids(locals.level);
        locals.player = new Ship(locals);
    }

    /**
     * Show the text of the scene
     */
    // private void showText(){
    //     textMode(CENTER);
    //     textSize(30);
    //     String levelString = "Level " + level + "\n" + player.getScore();
    //     fill(255);
    //     textSize(30);
    //     text(levelString, width/2, 50);
    //     String liveString = "";
    //     for (int i = 0; i < player.getLives(); i++){
    //         liveString += " | ";
    //     }
    //     text(liveString, width - 50, 50);
    // }

    public void resetAstroids(int level){
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
        // background(0);
        // showText();
        // locals.player.showBullets();
        runNetwork();
        locals.player.show();
        showAsteroids();
        checkLevel();
    }

    public void runNetwork(){
        float[] inputs = new float[18];
        int c = 0;
        for (Sensor s : locals.player.getSensors()){
            inputs[c] = (float) s.getWeightValue();
            c++;
        }
        inputs[16] = (float) locals.player.getX() / locals.player.width;
        inputs[17] = (float) locals.player.getY() / locals.player.height;

        locals.neat.runCurrent(inputs);

        float[] outputs = locals.neat.getCurOutput();

        if (outputs[0] > 0.5){
            locals.player.shoot();
        }
        if (outputs[1] >=0.5){
            locals.player.turnLeft();
        }
        if (outputs[2] >= 0.5){
            locals.player.turnRight();
        }
        if (outputs[3] >= 0.5){
            locals.player.accelerate = true;
            locals.player.accelerate();
        }
        else{
            locals.player.accelerate = false;
        }

        // for(float f : outputs){
        //     System.out.print(f + ", ");
        // }
        // System.out.print( "\n");

    }

}
