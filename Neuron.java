import java.util.Random;
import java.util.ArrayList;

public class Neuron{

    ArrayList<Float> weights, inputs;
    float bias, biasWeight;
    int curIndex;
    ArrayList<Neuron> connections;


    public Neuron(int numInputs, float bias){
        this.bias = bias;
        Random r = new Random();
        this.biasWeight = r.nextFloat();
        weights = new ArrayList<Float>();
        inputs = new ArrayList<Float>();
        connections = new ArrayList<Neuron>();

        curIndex = 0;

        for (int i = 0; i < numInputs; i++){
            weights.add(r.nextFloat());
            inputs.add((float)0.0);
            //yea this sucks but I am lazy at the moment.
            if (r.nextFloat() > 0.5)
                weights.set(i, weights.get(i) *-1);
        }
    }

    public float sum(){
        float sum = 0;
        for (int i = 0; i < inputs.size(); i++){
            sum += inputs.get(i) * weights.get(i);
        }
        sum += bias * biasWeight;
        return sum;
    }

    public void setInput(float val){
        inputs.set(curIndex, val);
        curIndex++;
        System.out.println("Setting input --> " + val);
        if (curIndex >= inputs.size())
            curIndex = 0;
    }

    public float activate(){
        float d = (float) Math.pow((double) Math.exp(1.0),(double) sum());
        return (float) (1.0/(1+d));
    }

    public void setConnection(Neuron n){
        connections.add(n);
    }

    public void feed(){
        for (Neuron n : connections){
            n.setInput(activate());
        }
    }

    public ArrayList<Neuron> getConnections(){
        return connections;
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
