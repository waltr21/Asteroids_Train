import java.util.ArrayList;


public class Asteroids_Train{
    Ship player;
    ArrayList<Asteroid> asteroids;
    GameScene GS;
    int level;
    static int width = 900;
    static int height = 900;
    Locals locals;
    Simple_NEAT neat;

    public Asteroids_Train(){
        setup();
        neat = new Simple_NEAT(34, 4);
        locals.neat = neat;
        for (int i = 0; i < 60; i++){
            neat.addAgent();
        }
    }

    private void setup(){
        locals = new Locals();
        //size(900, 900, OPENGL);
        //frameRate(60);
        locals.player = new Ship(locals);
        locals.asteroids = new ArrayList<Asteroid>();
        locals.GS = new GameScene(locals);
        locals.level = 7;
    }

    private void draw(){
        locals.GS.show();
        System.out.println(locals.player.score + " - " + locals.player.dead);
    }

    private void runAll(){
        int totalFrames = 30 * 60;
        int c = 0;
        for (int i = 0; i < 60; i++){
            locals.GS.resetAstroids(3);
            neat.setCurrentAgent(i);
            int frameCount = 0;

            while(frameCount < totalFrames && !locals.player.dead){
                locals.GS.show();
                frameCount++;
            }

            float curFit = (float) locals.player.getScore();
            // System.out.println(locals.player.getAccuracy());
            curFit = (float) curFit * (float) locals.player.getAccuracy();
            if (totalFrames - frameCount < 5)
                curFit *= 2;
            neat.setFitness(i, curFit);
            // System.out.println(c + " --> " + frameCount + " - " + locals.player.getScore());
            // System.out.print(String.format("\033[%dA",1)); // Move up
            // System.out.print("\033[2K"); // Erase line content
            c++;
            locals.player = new Ship(locals);
        }
    }

    public void runGenerations(int count){
        for (int i = 0; i <= count; i++){
            // System.out.println("Generation: " + i);
            runAll();
            // System.out.print(String.format("\033[2J"));
            printBest(i);
            if(i != count)
                neat.breed();
            // if(i % 50 == 0)
            //     locals.level++;
        }
        neat.getBestFit().saveToFile("best.net");
    }

    public void printBest(int gen){
        // System.out.print(String.format("\033[%dA",1)); // Move up
        // // System.out.print("\033[2K");
        System.out.println(String.format("Generation: %d / best fit: %f", gen ,neat.getBestFit().getFitness()));
    }


    public static void main(String[] args){
        Asteroids_Train a = new Asteroids_Train();
        int gens = Integer.parseInt(args[0]);
        a.runGenerations(gens);
    }
}
