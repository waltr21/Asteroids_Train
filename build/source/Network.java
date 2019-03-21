import java.util.ArrayList;
import java.util.Random;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Network implements Serializable, Comparable<Network>{
    ArrayList<Neuron> inputs, outputs, hidden;
    int idCount;
    float bias;
    Random r;
    double fitness, genFitness;

    public Network(int numInputs, int numOutputs){
        idCount = 0;
        bias = 1.0f;
        fitness = 0.0;
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

    public Network(ArrayList<Neuron> inputs, ArrayList<Neuron> hidden, ArrayList<Neuron> outputs, float bias, int idCount){
        this.inputs = inputs;
        this.outputs = outputs;
        this.hidden = hidden;

        this.bias = bias;
        this.idCount = idCount;
        fitness = 0.0;
        r = new Random();

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

        // System.out.println("add node: " + validNodes.size());

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

        // System.out.println("Adding node " + tempHidden.getID() +  " --> " + randConnection.neuron.getID() + " - " + randNode.getID());

        //Set the layer of this node to its output + 1
        // tempHidden.setLayer(randNode.getLayer() + 1);
    }

    public void addRandConnection(Neuron node){
        ArrayList<Neuron> validConnections = new ArrayList<Neuron>();
        validConnections.addAll(hidden);

        // System.out.println(validConnections);
        if (validConnections.size() < 1)
            return;

        //Grab a hidden to make a new input.
        int index = r.nextInt(validConnections.size());
        Neuron randHidden = validConnections.get(index);

        //Grab random hidden to connect to.
        while(true){
            if  (!checkRepeat(node, randHidden)){
                node.addConnection(randHidden);
                if (!checkDeadlock(node, node)){
                    // System.out.println("Setting connection: " + randHidden.getID() + " --> " +  node.getID());
                    break;
                }
                else{
                    node.removeConnection(randHidden);
                }
            }

            validConnections.remove(randHidden);
            if (validConnections.size() < 1){
                // System.out.println("No valid connection to make for: " + node.getID());
                break;
            }

            index = r.nextInt(validConnections.size());
            randHidden = validConnections.get(index);
        }
    }

    public void addRandConnection(){
        ArrayList<Neuron> validConnections = getConnectionNodes();
        Neuron n = validConnections.get(r.nextInt(validConnections.size()));
        addRandConnection(n);
    }

    public ArrayList<Neuron> getConnectionNodes(){
        ArrayList<Neuron> validConnections = new ArrayList<Neuron>();
        validConnections.addAll(hidden);
        validConnections.addAll(outputs);
        return validConnections;
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

    public void mutateWeight(){
        ArrayList<Neuron> validConnections = new ArrayList<Neuron>();
        validConnections.addAll(hidden);
        validConnections.addAll(outputs);

        Neuron n = validConnections.get(r.nextInt(validConnections.size()));
        Connection c = n.getConnections().get(r.nextInt(n.getConnections().size()));

        c.randomizeWeight();
    }

    public void remvoveRandConnection(){
        ArrayList<Neuron> validConnections = new ArrayList<Neuron>();
        validConnections.addAll(hidden);
        validConnections.addAll(outputs);
        Neuron n = validConnections.get(r.nextInt(validConnections.size()));

        Connection c = n.getConnections().get(r.nextInt(n.getConnections().size()));
        n.removeConnection(c);

    }

    public void runNetwork(float[] initInputs){
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

    public double getFitness(){
        return fitness;
    }

    public void setFitness(double d){
        fitness = d;
    }

    public Network copy(){
        ArrayList<Neuron> tempInputs = new ArrayList<Neuron>();
        ArrayList<Neuron> tempHidden = new ArrayList<Neuron>();
        ArrayList<Neuron> tempOutputs = new ArrayList<Neuron>();

        for (Neuron n : inputs){
            tempInputs.add(n.copy());
        }

        for (Neuron n : hidden){
            tempHidden.add(n.copy());
        }

        for (Neuron n : outputs){
            tempOutputs.add(n.copy());
        }

        ArrayList<Neuron> validNodes = new ArrayList<Neuron>();
        validNodes.addAll(tempInputs);
        validNodes.addAll(tempHidden);

        for (int i = 0; i < hidden.size(); i++){
            Neuron n = hidden.get(i);
            for (Connection c : n.getConnections()){
                int id = c.neuron.getID();
                Neuron connectionNeuron = null;
                for (Neuron x : validNodes){
                    if (x.getID() == id){
                        connectionNeuron = x;
                    }
                }
                tempHidden.get(i).addConnection(connectionNeuron, c.weight);
            }
        }

        for (int i = 0; i < outputs.size(); i++){
            Neuron n = outputs.get(i);
            for (Connection c : n.getConnections()){
                int id = c.neuron.getID();
                Neuron connectionNeuron = null;
                for (Neuron x : validNodes){
                    if (x.getID() == id){
                        connectionNeuron = x;
                    }
                }
                tempOutputs.get(i).addConnection(connectionNeuron, c.weight);
            }
        }

        return new Network(tempInputs, tempHidden, tempOutputs, this.bias, this.idCount);
    }

    public boolean saveToFile(String s){
        try{
            FileOutputStream fos = new FileOutputStream(s);
    		ObjectOutputStream oos = new ObjectOutputStream(fos);
    		oos.writeObject(this);
    		oos.close();
            return true;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }

    public static Network loadFromFile(String s){
        try{
            FileInputStream fis = new FileInputStream(s);
    		ObjectInputStream ois = new ObjectInputStream(fis);
    		Network n = (Network) ois.readObject();
    		ois.close();
            return n;
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }

    public void setGenFitness(double f){
        genFitness = f;
    }

    public double getGenFitness(){
        return genFitness;
    }

    @Override
    public int compareTo(Network o) {
        if (this.genFitness > o.getGenFitness()){
            return -1;
        }
        if (this.genFitness < o.getGenFitness()){
            return 1;
        }
        return 0;
    }

    public static void main(String[] args){
        // Locals l = new Locals();
        // Network n = new Network(3, 2, l);
        //
        //
        // float[] inputs = {0.4f, 0.9f, 0.33f};
        // n.runNetwork(inputs);
        // for (float i : n.getSimpleOutput()){
        //     System.out.print(i + ", ");
        // }
        // System.out.println("\n------------------------------------------");
        // n.saveToFile("test");
        //
        // Network nCopy = Network.loadFromFile("test");
        //
        // for (float i : nCopy.getSimpleOutput()){
        //     System.out.print(i + ", ");
        // }
        // System.out.println("");

        // Network n = new Network(3, 2);
        // float[] inputs = {0.4f, 0.9f, 0.33f};
        // for (int i = 0; i < 5; i++){
        //     n.addRandHiddenNode();
        // }
        // Random r = new Random();
        // for (int i = 0; i < 4; i++){
        //     n.addRandConnection(n.outputs.get(r.nextInt(n.outputs.size())));
        // }
        // n.runNetwork(inputs);
        // for (float i : n.getSimpleOutput()){
        //     System.out.println(i);
        // }
    }
}