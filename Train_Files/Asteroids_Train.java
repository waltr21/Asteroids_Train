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
	int numAgents;
	double curAvgTime;
	long stamp, timeSum;

    public Asteroids_Train(){
        setup();
		numAgents = 80;
        neat = new Simple_NEAT(17, 4);
        locals.neat = neat;
        for (int i = 0; i < numAgents; i++){
            neat.addAgent();
        }
		stamp = System.currentTimeMillis();
		timeSum = 0;
		curAvgTime = 0;
    }

    private void setup(){
        locals = new Locals();
        //size(900, 900, OPENGL);
        //frameRate(60);
        locals.player = new Ship(locals);
        locals.asteroids = new ArrayList<Asteroid>();
        locals.GS = new GameScene(locals);
        locals.level = 8;
    }

    private void draw(){
        locals.GS.show();
        // System.out.println(locals.player.score + " - " + locals.player.dead);
    }

    private void runAll(){
        int totalFrames = 180 * 60;
        int c = 0;
        for (int i = 0; i < numAgents; i++){
            locals.GS.resetAstroids(4);
            neat.setCurrentAgent(i);
            int frameCount = 0;

            while(!locals.player.dead){
                locals.GS.show();
                frameCount++;
				if (frameCount > totalFrames)
					break;
            }

            double curFit = locals.player.getScore();
            //System.out.println("Accuracy: " + locals.player.getAccuracy() + "  Score: " + curFit);
            curFit = curFit * locals.player.getAccuracy();
            // if (totalFrames - frameCount < 5)
            //     curFit *= 2;
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

            printBest(i, count);

            ////////////////////////////////////////////////////
            //Save a .net file for every generation
            ///////////////////////////////////////////////////

            //Get The best fit here
            try {
            	neat.getBestFit().saveGenToFile("/home/sam/Documents/CIS_365/AiProject/newRepo/Asteroids_Train/Train_Files/visualizeGenerations/Gen" + i + "Best.net");
        		
        	} catch (Exception e) {
        		System.out.println(e);
        	}


            if(i != count)
                neat.breed();

            // if(i % 50 == 0)
            //     locals.level++;


        	//I was trying to do it here


        }
        neat.getBestFit().saveToFile("best.net");
    }

    public void printBest(int gen, int total){
		if (gen > 0){
        	removeLine(3);
		}
		int percent = (int) (gen / (total * 1.0) * 100);
        System.out.println(String.format("Generation: %d / best fit: %f", gen ,neat.getBestFit().getFitness()));
		System.out.println("Progress: " + Colors.GREEN + percent + "%" + Colors.RESET);	
		System.out.println("Time Remaining: " + Colors.YELLOW + timeRemain(gen , total) + Colors.RESET);
	}

	public void removeLine(int amt){
		for (int i = 0; i < amt; i++){
			System.out.print(String.format("\033[%dA",1)); // Move up
        	System.out.print("\033[2K");
		}
	}

	public String timeRemain(int gen, int total){		
		long tempStamp = System.currentTimeMillis();
		long passed = tempStamp - stamp;
		timeSum += passed;
		
		if (gen % 10 == 0){
			curAvgTime = (double) timeSum / 10.0;
			timeSum = 0;
			stamp = System.currentTimeMillis();
		}

		if (gen < 10)
			return "-";

		double remain = (total-gen) * (curAvgTime / 10.0);
		int totalSeconds = (int) remain / 1000;
		int totalMins = totalSeconds / 60;
		int finalSeconds = totalSeconds % 60;
		return totalMins + "m : " + finalSeconds + "s";
	}


    public static void main(String[] args){
        Asteroids_Train a = new Asteroids_Train();
        int gens = Integer.parseInt(args[0]);
        a.runGenerations(gens);
    }
}

class Colors {
    // Reset
    public static final String RESET = "\033[0m";  // Text Reset

    // Regular Colors
    public static final String BLACK = "\033[0;30m";   // BLACK
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String YELLOW = "\033[0;33m";  // YELLOW
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE
    public static final String CYAN = "\033[0;36m";    // CYAN
    public static final String WHITE = "\033[0;37m";   // WHITE

    // Bold
    public static final String BLACK_BOLD = "\033[1;30m";  // BLACK
    public static final String RED_BOLD = "\033[1;31m";    // RED
    public static final String GREEN_BOLD = "\033[1;32m";  // GREEN
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    public static final String BLUE_BOLD = "\033[1;34m";   // BLUE
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String WHITE_BOLD = "\033[1;37m";  // WHITE

    // Underline
    public static final String BLACK_UNDERLINED = "\033[4;30m";  // BLACK
    public static final String RED_UNDERLINED = "\033[4;31m";    // RED
    public static final String GREEN_UNDERLINED = "\033[4;32m";  // GREEN
    public static final String YELLOW_UNDERLINED = "\033[4;33m"; // YELLOW
    public static final String BLUE_UNDERLINED = "\033[4;34m";   // BLUE
    public static final String PURPLE_UNDERLINED = "\033[4;35m"; // PURPLE
    public static final String CYAN_UNDERLINED = "\033[4;36m";   // CYAN
    public static final String WHITE_UNDERLINED = "\033[4;37m";  // WHITE

    // Background
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED_BACKGROUND = "\033[41m";    // RED
    public static final String GREEN_BACKGROUND = "\033[42m";  // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m";   // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m";  // WHITE

    // High Intensity
    public static final String BLACK_BRIGHT = "\033[0;90m";  // BLACK
    public static final String RED_BRIGHT = "\033[0;91m";    // RED
    public static final String GREEN_BRIGHT = "\033[0;92m";  // GREEN
    public static final String YELLOW_BRIGHT = "\033[0;93m"; // YELLOW
    public static final String BLUE_BRIGHT = "\033[0;94m";   // BLUE
    public static final String PURPLE_BRIGHT = "\033[0;95m"; // PURPLE
    public static final String CYAN_BRIGHT = "\033[0;96m";   // CYAN
    public static final String WHITE_BRIGHT = "\033[0;97m";  // WHITE

    // Bold High Intensity
    public static final String BLACK_BOLD_BRIGHT = "\033[1;90m"; // BLACK
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String WHITE_BOLD_BRIGHT = "\033[1;97m"; // WHITE

    // High Intensity backgrounds
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String RED_BACKGROUND_BRIGHT = "\033[0;101m";// RED
    public static final String GREEN_BACKGROUND_BRIGHT = "\033[0;102m";// GREEN
    public static final String YELLOW_BACKGROUND_BRIGHT = "\033[0;103m";// YELLOW
    public static final String BLUE_BACKGROUND_BRIGHT = "\033[0;104m";// BLUE
    public static final String PURPLE_BACKGROUND_BRIGHT = "\033[0;105m"; // PURPLE
    public static final String CYAN_BACKGROUND_BRIGHT = "\033[0;106m";  // CYAN
    public static final String WHITE_BACKGROUND_BRIGHT = "\033[0;107m";   // WHITE
}
