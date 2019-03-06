import java.util.ArrayList;

public class NEAT{
    ArrayList<Neuron> inputs, outputs;

    public NEAT(int numInputs, int numOutputs){
         inputs = new ArrayList<Neuron>();
         outputs = new ArrayList<Neuron>();

         //Add Neurons for inputs
         for (int x = 0; x < numInputs; x++){
             inputs.add(new Neuron(1));
         }

         for (int x = 0; x < numOutputs; x++){
             outputs.add(new Neuron(1));
         }

         initConnections();
    }

    private void initConnections(){
        for (Neuron inputN : inputs){
            for (Neuron outputN : outputs){
                outputN.addConnection(inputN);
            }
        }
    }

    private void runNetwork(float[] initInputs){
        if (initInputs.length !=  inputs.size()){
            System.out.println("Error: Input mismatch...");
            return;
        }
        //Set inputs for the input layer.
        for (int i = 0; i < inputs.size(); i++){
            Neuron inputN = inputs.get(i);
            inputN.setInput(initInputs[i]);
        }

        //Change this...
        for (Neuron n : outputs){
            n.feed();
        }

    }

    public float[] getSimpleOutput(){
        float[] nums = new float[outputs.size()];

        for (int i = 0; i < outputs.size(); i++){
            nums[i] = outputs.get(i).getValue();
        }

        return nums;
    }

    public static void main(String[] args){
        NEAT n = new NEAT(3, 2);
        float[] inputs = {0.4f, 0.9f, 0.33f};
        n.runNetwork(inputs);
        for (float i : n.getSimpleOutput()){
            System.out.println(i);
        }
    }
}
