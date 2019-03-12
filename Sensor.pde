public class Sensor{
    private float x1, y1, x2, y2;

    public Sensor(float x1, float y1, float x2, float y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setX1(float n){
        x1 = n;
    }

    public void setY1(float n){
        y1 = n;
    }

    public void show(){
        line(x1, y1, x2, y2);
    }
}
