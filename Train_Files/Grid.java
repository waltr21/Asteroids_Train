public class Grid{
    double[][] grid;
    Locals l;
    int cellWidth;


    //CellWidth should be an even multiple of the width and height of the screen.
    public Grid(int cellWidth, Locals l){
        this.l = l;
        this.cellWidth = cellWidth;
        int xSize = (int) l.width / cellWidth;
        int ySize = (int) l.height / cellWidth;
        grid = new double[xSize][ySize];
        resetGrid();
    }

    private void resetGrid(){
        for (int x = 0; x < grid.length; x++){
            for (int y = 0; y < grid.length; y++){
                grid[x][y] = 0.0;
            }
        }
    }

    private void setAsteroids(){
        resetGrid();
		//System.out.println(l.asteroids.size());
        for (Asteroid a : l.asteroids){
            int xCell = (int) (a.getX() / cellWidth);
            int yCell = (int) (a.getY() / cellWidth);

            //Cap all coordinates
            if (xCell >= grid.length)
                xCell = grid.length-1;
            if (xCell < 0)
                xCell = 0;
            if (yCell >= grid[0].length)
                yCell = grid[0].length-1;
            if (yCell < 0)
                yCell = 0;

            grid[xCell][yCell] = 1.0;
        }

        int xCell = (int) (l.player.getX() / cellWidth);
        int yCell = (int) (l.player.getY() / cellWidth);

        //Cap all coordinates
        if (xCell >= grid.length)
            xCell = grid.length-1;
        if (xCell < 0)
            xCell = 0;
        if (yCell >= grid[0].length)
            yCell = grid[0].length-1;
        if (yCell < 0)
            yCell = 0;

        grid[xCell][yCell] = -1;
    }

    public void show(){
        setAsteroids();
    }

    public int getWidth(){
        return grid.length;
    }

    public double[][] getGrid(){
        return grid;
    }

}
