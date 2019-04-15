import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;

public class GameScene{
    boolean online;
    Locals locals;
    Simple_NEAT n;
    Visual vis;
    int generations;
    int genCount = 0;

    public GameScene(Locals l){
        locals = l;
        locals.level = 4;
        resetAstroids(locals.level);
        locals.player = new Ship(locals);
        n = new Simple_NEAT(17,4);
        Network temp  = Network.loadFromFile("/home/sam/Documents/CIS_365/AiProject/newRepo/Asteroids_Train/Train_Files/best.net");
        
        //Load all generations
        ArrayList<Network> genList = Network.loadGenFiles("/home/sam/Documents/CIS_365/AiProject/newRepo/Asteroids_Train/Train_Files/visualizeGenerations/");
        generations = genList.size();
        vis = new Visual(genList);
        
       //System.out.println("before");

        //for (Network g: genList) {
        //  System.out.println("_____________Inputs Layer_____________");
        //   for (Neuron n: g.inputs) {
        //     //System.out.println(n.getConnections());
        //     System.out.println(n.getID());
             
        //   }
        //   System.out.println("");
        //   System.out.println("_____________hidden Layer_____________");
        //   for (Neuron n: g.hidden) {
        //    // System.out.println(n.getConnections());
        //      System.out.print(n.getID() + " connected to --> ");
        //     for (Connection c: n.getConnections()) {
        //        //System.out.println(c);
        //        System.out.print(c.getNeuron().getID() + " , "); 
        //     }
        //    System.out.println("");

        //   }
        //  System.out.println("");
        //   System.out.println("_____________Outputs Layer_____________");
        //   for (Neuron n: g.outputs) {
        //     //System.out.println(n.getConnections());
        //     System.out.print( n.getID() + " connected to --> ");
        //     for (Connection c: n.getConnections()) {
        //        //System.out.println(c);
        //        System.out.print(c.getNeuron().getID() + " , "); 
        //     }
        //      System.out.println("");

        //   }
        //}
        
        n.addAgent(temp);
        n.setCurrentAgent(0);
    }

    /**
     * Show the text of the scene
     */
    private void showText(){
        textAlign(CENTER);
        textSize(30);
        String levelString = "Generation " + genCount + "\n";
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
        //showText();
        // locals.player.showBullets();
        //locals.player.show();
        //runNetwork();
        //showAsteroids();
        //checkLevel();
        
        if (genCount < generations) {
          vis.show(genCount);
        } else {
          //vis.show(generations - 1); 
        }
        genCount++;
    }

    public void runNetwork(){
        float[] inputs = new float[17];
        int c = 0;

        //Value 0-1 for distance between asteroid and ship
        for (Sensor s : locals.player.getSensors()){
            inputs[c] = (float) s.getWeightValue();
            c++;
        }

        //Value 0-1 for number of active shots
        inputs[16] = (float) locals.player.bullets.size() / 4;

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
        if (outputs[3] >= 0.5){
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
