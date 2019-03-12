import java.util.ArrayList;
import java.util.Random;


public class Network{
    ArrayList<Neuron> inputs, outputs, hidden;
    int idCount;
    float bias;
    Random r;

    public Network(int numInputs, int numOutputs){
        idCount = 0;
        bias = 1.0f;
        inputs = new ArrayList<Neuron>();
        outputs = new ArrayList<Neuron>();
        hidden = new ArrayList<Neuron>();
        r = new Random();

        //Add Neurons for inputs
        for (int x = 0; x < numInputs; x++){
            Neuron in = new Neuron(bias, idCount);
            inputs.add(in);
            idCount++;
        }

        for (int x = 0; x < numOutputs; x++){
            Neuron out = new Neuron(bias, idCount);
            // out.setLayer(0);
            outputs.add(out);
            idCount++;
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

    public void addRandHiddenNode(){
        //Create a new hidden node.
        Neuron tempHidden = new Neuron(bias, idCount);
        idCount++;

        //Combine all nodes that are valid for adding a connection (hidden and output)
        ArrayList<Neuron> validNodes = new ArrayList<Neuron>();
        validNodes.addAll(outputs);
        validNodes.addAll(hidden);

        //Grab a random node to manipulate connection.
        int index = r.nextInt(validNodes.size());
        Neuron randNode = validNodes.get(index);

        //Grab random connection from this node.
        index = r.nextInt(randNode.getConnections().size());
        Connection randConnection = randNode.getConnections().get(index);

        //Add from the new hidden to the old.
        tempHidden.addConnection(randConnection.neuron, 1);
        //Remove old connection.
        randNode.removeConnection(randConnection);
        //Create new connection with the new hidden node.
        randNode.addConnection(tempHidden, randConnection.weight);
        hidden.add(tempHidden);

        //Set the layer of this node to its output + 1
        // tempHidden.setLayer(randNode.getLayer() + 1);
    }

    public void addRandConnection(Neuron node){
        ArrayList<Neuron> validConnections = new ArrayList<Neuron>();
        validConnections.addAll(hidden);

        //Grab a hidden to make a new input.
        int index = r.nextInt(validConnections.size());
        Neuron randHidden = validConnections.get(index);

        //Grab random hidden to connect to.
        while(true){
            if  (!checkRepeat(node, randHidden)){
                node.addConnection(randHidden);
                if (!checkDeadlock(node, node)){
                    System.out.println("Setting connection: " + node.getID() + " --> " +  randHidden.getID());
                    break;
                }
                else{
                    node.removeConnection(randHidden);
                }
            }

            validConnections.remove(randHidden);
            if (validConnections.size() < 1){
                System.out.println("No valid connection to make for: " + node.getID());
                break;
            }

            index = r.nextInt(validConnections.size());
            randHidden = validConnections.get(index);
        }
    }

    public boolean checkDeadlock(Neuron node, Neuron cur){
        for (Connection c : cur.getConnections()){
            if (node == c.neuron){
                return true;
            }
            if (checkDeadlock(node, c.neuron)){
                return true;
            }
        }
        return false;
    }

    public boolean checkRepeat(Neuron mainNode, Neuron connection){
        for (Connection c : mainNode.getConnections()){
            if (c.neuron == connection){
                return true;
            }
        }
        return false;
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

        //Run network and get activated values.
        for (Neuron n : outputs){
            n.feed();
        }

        //Reset each hidden node to unactivated.
        for (Neuron n : hidden){
            n.setActivation(false);
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
        Network n = new Network(8, 3);
        float[] inputs = {0.4f, 0.9f, 0.33f, 0.5f, 0.01f, 0.67f, 0.13f, 0.28f};
        for (int i = 0; i < 5; i++){
            n.addRandHiddenNode();
        }
        Random r = new Random();
        for (int i = 0; i < 30; i++){
            n.addRandConnection(n.outputs.get(r.nextInt(n.outputs.size())));
        }
        n.runNetwork(inputs);
        for (float i : n.getSimpleOutput()){
            System.out.println(i);
        }
    }
}