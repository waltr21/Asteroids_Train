public class Vector{
    public double x,y;

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector(){
        this.x = 0;
        this.y = 0;
    }

    public static Vector fromAngle(double a){
        return new Vector((float) Math.cos(a), (float) Math.sin(a));
    }

    public void mult(double n){
        this.x *= n;
        this.y *= n;
    }

    public void add(Vector v){
        this.x += v.x;
        this.y += v.y;
    }
}
