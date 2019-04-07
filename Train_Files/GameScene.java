import java.util.concurrent.ThreadLocalRandom;

public class GameScene{
    boolean online;
    Locals locals;
    Grid gameGrid;

    public GameScene(Locals l){
        locals = l;
        resetAstroids(locals.level);
        locals.player = new Ship(locals);
        gameGrid = new Grid(50, l);
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
		gameGrid.show();
    }

    public void runNetwork(){
        float[] inputs = new float[gameGrid.getWidth() * gameGrid.getWidth() + 1];
        inputs[0] = (float) locals.player.getAngle() / (float) (2*3.14159265359);
        int c = 1;
        for (int x = 0; x < gameGrid.getGrid().length; x++){
            for (int y = 0; y < gameGrid.getGrid().length; y++){
                inputs[c] = (float) gameGrid.getGrid()[x][y];
				c++;
            }
        }

        locals.neat.runCurrent(inputs);

        float[] outputs = locals.neat.getCurOutput();

        if (outputs[0] > 0.0){
            locals.player.shoot();
        }
        if (outputs[1] > 0.0){
            locals.player.turnLeft();
        }
        if (outputs[2] > 0.0){
            locals.player.turnRight();
        }
        if (outputs[3] > 0.0){
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
