import java.util.ArrayList;

Ship player;
ArrayList<Asteroid> asteroids;
GameScene GS;
int level;

void setup(){
    size(1000, 900, OPENGL);
    frameRate(60);
    player = new Ship();
    asteroids = new ArrayList<Asteroid>();
    GS = new GameScene();
    level = 0;
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
