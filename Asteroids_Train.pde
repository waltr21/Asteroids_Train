import java.util.ArrayList;

GameScene GS;
Locals locals;

void setup(){
    locals = new Locals();
    size(900, 900, OPENGL);
    frameRate(5);
    locals.player = new Ship(locals);
    locals.asteroids = new ArrayList<Asteroid>();
    GS = new GameScene(locals);
    // locals.level = 10;
}

void draw(){
    GS.show();
    // System.out.println(player.score + " - " + player.dead);
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
    locals.player.processButtonReleased(int(Character.toLowerCase(code)));
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


    locals.player.processButtonPress(code);
}
