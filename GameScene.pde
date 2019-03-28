import java.util.concurrent.ThreadLocalRandom;

public class GameScene{
    boolean online;
    Locals locals;
    Simple_NEAT n;
    Grid gameGrid;

    public GameScene(Locals l){
        locals = l;
        locals.level = 3;
        resetAstroids(locals.level);
        locals.player = new Ship(locals);
        n = new Simple_NEAT(33,4);
        Network temp  = Network.loadFromFile("/Users/ryanwalt/Downloads/CODE/Java/Processing/Asteroids_Train/best.net");
        n.addAgent(temp);
        n.setCurrentAgent(0);
        gameGrid = new Grid(50, l);
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
        // runNetwork();
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

       if (outputs[0] > 0.5){
            locals.player.shoot();
        }
        if (outputs[1] >=0.5){
            locals.player.turnLeft();
        }
        if (outputs[2] >= 0.5){
            locals.player.turnRight();
        }
        if (outputs[3] <= 0.5){
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
