import java.util.Random;
import java.util.ArrayList;

public class Neuron{

    float bias, biasWeight;
    ArrayList<Connection> inputConnections;
    boolean activated;
    float value;
    Random r;

    public Neuron(float bias){
        this.bias = bias;
        r = new Random();
        this.biasWeight = r.nextFloat();
        inputConnections = new ArrayList<Connection>();
        activated = false;
        value = 0.0f;
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
        float weight = r.nextFloat();
        //yea this sucks but I am lazy at the moment.
        if (r.nextFloat() > 0.5)
            weight = weight * -1;
        inputConnections.add(new Connection(n, weight));
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
                System.out.println("Feeding: " + c.neuron);
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

    public static void main(String[] args){
        float[] x = new float[12];
        Random r = new Random();
        for (int i = 0; i < 12; i++){
            x[i] = r.nextFloat() * 10;
        }
        // Neuron n = new Neuron(x, -1, r.nextFloat());
        // System.out.println((double)n.activate());
    }

}

class Connection{
    public Neuron neuron;
    public float weight;

    public Connection(Neuron n, float w){
        neuron = n;
        weight = w;
    }
}
