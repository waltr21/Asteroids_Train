import java.util.ArrayList;
import java.io.Serializable;

public class Locals implements Serializable{
    Ship player;
    ArrayList<Asteroid> asteroids;
    GameScene GS;
    int level;
    int width;
    int height;
    Simple_NEAT neat;

    public Locals(){
        width = 900;
        height = 900;
    }
}
