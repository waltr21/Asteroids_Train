import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class Simple_NEAT{
    private ArrayList<Network> agents;
    private int numInputs, numOutputs, genNum;
    private Network curAgent;
    private boolean keepBest;
    private double mutationRate;

    public Simple_NEAT(int nI, int nO){
        numInputs = nI;
        numOutputs = nO;
        genNum = 0;
        keepBest = false;
        agents = new ArrayList<Network>();
        mutationRate = 0.60;
    }

    public void addAgent(){
        agents.add(new Network(numInputs, numOutputs));
    }
    
    public void addAgent(Network n){
        agents.add(n);
    }

    public void setCurrentAgent(int index){
        curAgent = agents.get(index);
    }

    /**
     * Run the entire generation with the same input(s)
     * @param inputs Network input(s)
     */
    public void runAll(float[] inputs){
        for (Network n : agents){
            n.runNetwork(inputs);
        }
    }

    /**
     * Run the currently set agent
     * @param inputs Network input(s)
     */
    public void runCurrent(float[] inputs){
        curAgent.runNetwork(inputs);
    }

    public float[] getCurOutput(){
        return curAgent.getSimpleOutput();
    }

    public Network getAgent(int i){
        return agents.get(i);
    }

    public float[] getAgentOutput(int i){
        return agents.get(i).getSimpleOutput();
    }

    public void setFitness(int i, double d){
        Network n = getAgent(i);
        n.setFitness(d);
    }

    public void breed(){
        int popSize = 0;
        ArrayList<Network> nextGen = new ArrayList<Network>();
        if (keepBest){
            popSize = agents.size() - 1;
        }
        else
            popSize = agents.size();

        double sum = 0.0;
        for (Network a : agents){
            sum += a.getFitness();
        }
        //Normalize the fitness values
        for (Network a : agents){
            a.setGenFitness(a.getFitness()/sum);
        }

        Collections.sort(agents);
        for (Network a : agents){
            // System.out.println(a + " - " + a.getGenFitness());
        }
        // System.out.println("------------------------------");

        //Accumulate normalized fitness
        sum = 0.0;
        for (Network a : agents){
            double temp = a.getGenFitness();
            a.setGenFitness(temp + sum);
            sum += temp;
        }

        for (int i = 0; i < popSize; i++){
            double num = ThreadLocalRandom.current().nextDouble(0,1);
            for (Network a : agents){
                if (a.getGenFitness() >= num){
                    nextGen.add(a.copy());
                    break;
                }
            }
        }

        genNum++;
        agents = nextGen;
        mutate();
    }

    public void mutate(){
        for (Network a : agents){
            double num = ThreadLocalRandom.current().nextDouble(0,1);
            //Should we mutate this agent?
            if (mutationRate >= num){
                num = ThreadLocalRandom.current().nextDouble(0,1);
                if (num <= 1/3){
                    a.addRandHiddenNode();
                }
                else if(num > 1/3 && num <= 2/3){
                    a.addRandConnection();
                }
                else{
                    a.mutateWeight();
                }
            }
        }
    }

    public Network getBestFit(){
        double max = 0;
        Network best  = null;

        for (Network n : agents){
            if (n.getFitness() > max){
                best = n;
                max = n.getFitness();
            }
        }
        return best;
    }

    public Network getNetwork(int i){
        return agents.get(i);
    }

    public static void main(String args[]){
        // Simple_NEAT n = new Simple_NEAT(3, 2);
        // Locals l = new Locals();
        // for (int i =0; i < 1; i++){
        //     Ship s = new Ship(l);
        //     n.addAgent(s);
        //     n.setCurrentAgent(s);
        //     // n.setFitness(s, ThreadLocalRandom.current().nextInt(0, 200));
        // }
        //
        // float[] inputs = {0.4f, 0.9f, 0.33f};
        // n.runCurrent(inputs);
        // for (float i : n.getCurOutput()){
        //     System.out.print(i + ", ");
        // }
        // System.out.println("\n------------------------------------------");
        //
        // float[] inputs1 = {0.8f, 0.1f, 0.73f};
        // n.runCurrent(inputs1);
        // for (float i : n.getCurOutput()){
        //     System.out.print(i + ", ");
        // }
        // System.out.println("\n------------------------------------------");
        // n.breed();
    }
}