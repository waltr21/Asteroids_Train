import java.util.ArrayList;


public class Asteroids_Train{
    Ship player;
    ArrayList<Asteroid> asteroids;
    GameScene GS;
    int level;
    static int width = 900;
    static int height = 900;
    Locals locals;

    void setup(){
        locals = new Locals();
        //size(900, 900, OPENGL);
        //frameRate(60);
        locals.player = new Ship(locals);
        locals.asteroids = new ArrayList<Asteroid>();
        locals.GS = new GameScene(locals);
        locals.level = 0;
    }

    void draw(){
        GS.show();
        System.out.println(player.score + " - " + player.dead);
    }

    /**
     * Button released handle.
     */
    void keyReleased(){
        int code;
        if (keyCode > 40){
            code = int(Character.toLowerCase(key));
        }
        else{
            code = keyCode;
        }
        player.processButtonReleased(int(Character.toLowerCase(code)));
    }

    /**
     * Button pressed handle.
     */
    void keyPressed(){
        // char k = key;
        //System.out.println(keyCode);
        int code;
        if (keyCode > 40){
            code = int(Character.toLowerCase(key));
        }
        else{
            code = keyCode;
        }


        player.processButtonPress(code);
    }
}
