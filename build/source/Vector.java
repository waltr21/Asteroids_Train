public class Vector{
    public float x,y;

    public Vector(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector(){
        this.x = 0;
        this.y = 0;
    }

    public static Vector fromAngle(float a){
        return new Vector((float) Math.cos(a), (float) Math.sin(a));
    }

    public void mult(float n){
        this.x *= n;
        this.y *= n;
    }

    public void add(Vector v){
        this.x += v.x;
        this.y += v.y;
    }
}