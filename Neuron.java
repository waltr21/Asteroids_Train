import java.util.Random;
import java.util.ArrayList;

public class Neuron{

    private float bias, biasWeight;
    private ArrayList<Connection> inputConnections;
    private boolean activated;
    private float value;
    private Random r;
    private int id;

    public Neuron(float bias, int id){
        this.bias = bias;
        r = new Random();
        this.biasWeight = r.nextFloat();
        inputConnections = new ArrayList<Connection>();
        activated = false;
        value = 0.0f;
        this.id = id;
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

    //Use for input nodes.
    public void setInput(float val){
        activated = true;
        value = val;
        System.out.println("Setting input --> " + val);
    }

    public void activate(){
        float d = (float) Math.pow((double) Math.exp(1.0),(double) sum());
        value = (float) (1.0/(1+d));
    }

    public void feed(){
        for (Connection c : inputConnections){
            if (!c.neuron.activated){
                System.out.println("Feeding: " + c.neuron.getID());
                c.neuron.feed();
            }
        }
        activate();
        activated = true;
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

    public void removeConnection(Connection c1){
        for (Connection c2 : inputConnections){
            if (c1 == c2){
                inputConnections.remove(c2);
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

    public void randomizeWeight(){
        weight = r.nextFloat();
        //yea this sucks but I am lazy at the moment.
        if (r.nextFloat() > 0.5)
            weight = weight * -1;
    }
}
