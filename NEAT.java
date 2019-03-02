import java.util.ArrayList;

public class NEAT{

    ArrayList<ArrayList<Neuron>> nodes;

    public NEAT(int numInputs, int numOutputs){
         nodes = new ArrayList<ArrayList<Neuron>>();
         nodes.add(new ArrayList<Neuron>());
         nodes.add(new ArrayList<Neuron>());

         //Add Neurons for inputs and outputs...
         for (int x = 0; x < numInputs; x++){
             nodes.get(0).add(new Neuron(1, -1));
         }

         for (int x = 0; x < numOutputs; x++){
             nodes.get(1).add(new Neuron(numInputs, 1));
         }

         initConnections();
    }

    private void initConnections(){
        for (Neuron inputN : nodes.get(0)){
            for (Neuron outputN : nodes.get(1)){
                inputN.setConnection(outputN);
            }
        }
    }

    private void runNetwork(float[] inputs){
        if (inputs.length !=  nodes.get(0).size()){
            System.out.println("Error: Input mismatch...");
            return;
        }
        //Set inputs for the input layer.
        for (int i = 0; i < nodes.get(0).size(); i++){
            Neuron inputN = nodes.get(0).get(i);
            inputN.setInput(inputs[i]);
        }

        for (ArrayList<Neuron> layer : nodes){
            for (Neuron n : layer){
                n.feed();
            }
        }
    }

    public float[] getSimpleOutput(){
        float[] nums = new float[nodes.get(nodes.size()-1).size()];

        for (int i = 0; i < nodes.get(nodes.size()-1).size(); i++){
            nums[i] = nodes.get(nodes.size()-1).get(i).activate();
        }

        return nums;
    }

    public static void main(String[] args){
        NEAT n = new NEAT(3, 1);
        float[] inputs = {0.4f, 0.9f, 0.33f};
        n.runNetwork(inputs);
        for (float i : n.getSimpleOutput()){
            System.out.println(i);
        }
    }
}
