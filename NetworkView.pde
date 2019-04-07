public class NetworkView{
    Network net;
    ArrayList<Node> nodes;

    public NetworkView(Network net){
        this.net = net;
    }

    private void initNodes(){
        // for (Neuron n : net.getInputs()){
        //     break;
        // }
    }

    public void show(){
        return;
    }
}


class Node{
    float x, y;

    public Node(float x, float y){
        this.x = x;
        this.y = y;
    }
}
