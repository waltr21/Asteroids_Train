import java.util.Random;
import java.util.ArrayList;
import java.io.Serializable;


public class Neuron implements Serializable{

    private float bias, biasWeight;
    private ArrayList<Connection> inputConnections;
    private boolean activated;
    private float value;
    private Random r;
    private int id, layer;

    public Neuron(float bias, int id){
        this.bias = bias;
        r = new Random();
        this.biasWeight = r.nextFloat();
        inputConnections = new ArrayList<Connection>();
        activated = false;
        value = 0.0f;
        this.id = id;
        layer = -1;
    }

    public Neuron(float bias, int id, float biasWeight){
        this.bias = bias;
        r = new Random();
        this.biasWeight = biasWeight;
        inputConnections = new ArrayList<Connection>();
        activated = false;
        value = 0.0f;
        this.id = id;
        layer = -1;
    }

    public float sum(){
        float sum = 0;
        for (Connection c : inputConnections){
            // System.out.println("Val: " + c.neuron.getValue() + " - weight: " + c.weight);
            sum += c.neuron.getValue() * c.weight;
        }
        // System.out.println("BeforeBias: " + sum);
        sum += bias * biasWeight;
        // System.out.println("FinalSum: " + sum);
        return sum;
    }

    public void addConnection(Neuron n){
        inputConnections.add(new Connection(n));
    }

    public void addConnection(Neuron n, float w){
        inputConnections.add(new Connection(n, w));
    }

    //Use for input nodes.
    public void setInput(float val){
        activated = true;
        value = val;
        // System.out.println("Setting input --> " + value);
    }

    public void activate(){
        float d = (float) Math.pow((double) Math.exp(1.0),(double) sum());
        value = (float) (1.0/(1+d));
        activated = true;
    }

    public void feed(){
        for (Connection c : inputConnections){
            if (!c.neuron.activated){
                // System.out.println("Feeding: " + c.neuron + " / " + c.neuron.getID());
                c.neuron.feed();
            }
        }
        activate();
    }

    public ArrayList<Connection> getConnections(){
        return inputConnections;
    }

    public float getValue(){
        return value;
    }

    public void setActivation(boolean b){
        activated = b;
    }

    public int getID(){
        return id;
    }

    public void setLayer(int l){
        layer = l;
        for (Connection c : inputConnections){
            c.neuron.setLayer(layer + 1);
        }
    }

    public int getLayer(){
        return layer;
    }

    public void removeConnection(Connection c1){
        for (Connection c2 : inputConnections){
            if (c1 == c2){
                inputConnections.remove(c2);
                return;
            }
        }
    }

    public Neuron copy(){
        // Neuron copy = new Neuron(bias, id, biasWeight);
        // for (Connection c : inputConnections){
        //     copy.inputConnections.add(c.copy());
        // }
        return new Neuron(bias, id, biasWeight);
    }

    public void removeConnection(Neuron n){
        for (Connection c : inputConnections){
            if (c.neuron == n){
                inputConnections.remove(c);
                return;
            }
        }
    }

    public static void main(String[] args){
        Neuron n0 = new Neuron(1, 0);
        Neuron n1 = new Neuron(1, 1);
        Neuron n2 = new Neuron(1, 2);
        Neuron n3 = new Neuron(1, 3);

        n0.addConnection(n1);
        n0.addConnection(n2);
        Neuron copy = n0.copy();
        System.out.println(n0.bias  + " --> " + n0.biasWeight);
        System.out.println(copy.bias  + " --> " + copy.biasWeight);

    }
}

class Connection implements Serializable{
    public Neuron neuron;
    public float weight;
    private Random r;

    public Connection(Neuron n){
        neuron = n;
        r = new Random();
        randomizeWeight();
    }

    public Connection(Neuron n, float w){
        neuron = n;
        weight = w;
        r = new Random();
    }

    public void randomizeWeight(){
        weight = r.nextFloat();
        //yea this sucks but I am lazy at the moment.
        if (r.nextFloat() > 0.5)
            weight = weight * -1;
    }
}