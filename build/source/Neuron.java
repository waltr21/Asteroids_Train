import java.util.Random;
import java.util.ArrayList;

public class Neuron{

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

    public float sum(){
        float sum = 0;
        for (Connection c : inputConnections){
            sum += c.neuron.getValue() * c.weight;
        }
        sum += bias * biasWeight;
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
        System.out.println("Setting input --> " + val);
    }

    public void activate(){
        float d = (float) Math.pow((double) Math.exp(1.0),(double) sum());
        value = (float) (1.0/(1+d));
        activated = true;
    }

    public void feed(){
        for (Connection c : inputConnections){
            if (!c.neuron.activated){
                System.out.println("Feeding: " + c.neuron.getID());
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

    public void removeConnection(Neuron n){
        for (Connection c : inputConnections){
            if (c.neuron == n){
                inputConnections.remove(c);
                return;
            }
        }
    }
}

class Connection{
    public Neuron neuron;
    public float weight;
    private Random r;

    public Connection(Neuron n){
        neuron = n;
    }

    public Connection(Neuron n, float w){
        neuron = n;
        weight = w;
    }

    public void randomizeWeight(){
        weight = r.nextFloat();
        //yea this sucks but I am lazy at the moment.
        if (r.nextFloat() > 0.5)
            weight = weight * -1;
    }
}