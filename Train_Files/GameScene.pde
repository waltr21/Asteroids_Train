public class GameScene{
    boolean online;

    public GameScene(){
        level = 1;
        resetAstroids(level);
        player = new Ship();
    }

    /**
     * Show the text of the scene
     */
    private void showText(){
        textMode(CENTER);
        textSize(30);
        String levelString = "Level " + level + "\n" + player.getScore();
        fill(255);
        textSize(30);
        text(levelString, width/2, 50);
        String liveString = "";
        for (int i = 0; i < player.getLives(); i++){
            liveString += " | ";
        }
        text(liveString, width - 50, 50);
    }

    private void resetAstroids(int level){
        asteroids.clear();
        int num = 2 + (level * 2);
        for (int i = 0; i < num; i++){
            float tempX = random(50, width - 50);
            float tempY = random(50, height/2 - 150) + ((height/2 + 150) * int(random(0, 2)));
            asteroids.add(new Asteroid(tempX, tempY, 3));
            player.resetPos();
            player.clearBullets();
        }
    }

    /**
     * Display all of the asteroids to the screen.
     */
    private void showAsteroids(){
        try{
           for (Asteroid a : asteroids){
               a.show();
           }
        }
        catch(Exception e){
            System.out.println("Exception in showAsteroids (gameScene): " + e);
        }
    }

    private boolean checkLevel(){
        if (asteroids.size() < 1){
            level++;
            resetAstroids(level);
            return true;
        }
        return false;
    }

    public void show(){
        // background(0);
        // showText();
        player.showBullets();
        player.show();
        showAsteroids();
        checkLevel();
    }
}
