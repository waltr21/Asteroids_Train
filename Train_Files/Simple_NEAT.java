import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class Simple_NEAT{
    private ArrayList<Network> agents;
    private int numInputs, numOutputs, genNum;
    private Network curAgent;
    private boolean keepBest;

    public Simple_NEAT(int nI, int nO){
        numInputs = nI;
        numOutputs = nO;
        genNum = 0;
        keepBest = true;
        agents = new ArrayList<Network>();
    }

    public void addAgent(Object o){
        agents.add(new Network(numInputs, numOutputs, o));
    }

    public boolean setCurrentAgent(Object o){
        for (Network n : agents){
            if (n.agent == o){
                curAgent = n;
                return true;
            }
        }
        System.out.println("Agent not found...");
        return false;
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

    public Network getAgent(Object o){
        for (Network n : agents){
            if (o == n.agent){
                return n;
            }
        }
        return null;
    }

    public float[] getAgentOutput(Object o){
        Network n = getAgent(o);
        if (n != null){
            return n.getSimpleOutput();
        }
        return null;
    }

    public void setFitness(Object o, double d){
        Network n = getAgent(o);
        if (n == null){
            System.out.println("Object does not exist");
            return;
        }
        n.setFitness(d);
    }

    public void breed(){
        int popSize = agents.size();
        double sum = 0.0;
        for (Network a : agents){
            sum += a.getFitness();
        }
        for (Network a : agents){
            a.setGenFitness(a.getFitness()/sum);
        }

        Collections.sort(agents);
        for (Network a : agents){
            System.out.println(a + " - " + a.getGenFitness());
        }
    }

    public static void main(String args[]){
        Simple_NEAT n = new Simple_NEAT(8, 3);
        Locals l = new Locals();
        for (int i =0; i < 10; i++){
            Ship s = new Ship(l);
            n.addAgent(s);
            n.setFitness(s, ThreadLocalRandom.current().nextInt(0, 200));
        }
        n.breed();
    }
}
