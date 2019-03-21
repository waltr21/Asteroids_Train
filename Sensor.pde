import java.lang.Math;

public class Sensor{
    private double length, angle, weightValue;
    private Locals locals;

    public Sensor(double angle, Locals l){
        this.length = 350;
        this.angle = angle;
        this.weightValue = 0.0;
        locals = l;
    }

    public void show(double x, double y, double angle){
        double x2 = x + (Math.cos(angle + this.angle) * length);
        double y2 = y + (Math.sin(angle + this.angle) * length);
        // System.out.println(angle);
        strokeWeight(2);
        stroke(255);
        line((float) x, (float) y, (float) x2, (float) y2);
        calculateIntersection(x,y,x2,y2);
    }

    public void calculateIntersection(double x0, double y0, double x1, double y1){
        double a = square(x1 - x0) + square(y1 - y0);

        double iX = 0.0;
        double iY = 0.0;
        weightValue = 0;

        for (Asteroid ast : locals.asteroids){
            double b = 2 * (x1-x0) * (x0 - ast.getX()) + 2 * (y1-y0) * (y0 - ast.getY());
            double c = square(x0-ast.getX()) + square(y0-ast.getY()) - square(ast.getSize()/2.0);
            double t = (2*c) / ((-1*b) + Math.sqrt(square(b) - (4 * a * c)));

            if(t > 0 && t < 1 && square(b) - (4 * a * c) > 0){
                if (1 - t > weightValue){
                    weightValue = 1-t;
                    iX = intersectX(x0, x1, t);
                    iY = intersectY(y0, y1, t);
                }
            }
        }
        if (weightValue > 0.0){
            fill(255, 0, 0);
            noStroke();
            ellipse((float) iX, (float) iY, 15, 15);
        }
        // fill(255, 0, 0);
        // ellipse(iX, iY, 10, 10);
    }

    public double intersectX(double x0, double x1, double t){
        return (x1-x0) * t + x0;
    }

    public double intersectY(double y0, double y1, double t){
        return (y1-y0) * t + y0;
    }


    private double square(double x){
        return x * x;
    }

    public double getWeightValue(){
        return weightValue;
    }
}
