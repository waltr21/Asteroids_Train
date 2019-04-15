import java.util.*;

public class Visual{
    int[][] grid;
    int  X;
    int Y;
    //Locals l;
    //int cellWidth;
    ArrayList genList;
    
    ArrayList<Integer>[][] nodes;
    ArrayList<Map<Integer, Integer>>[][] edges;
    

    public Visual(ArrayList<Network> genList){
        
      
        this.genList = genList;
        
        //System.out.println(genList.size());
        
        //Create 2d array of arraylist for nodes and edges
        nodes = new ArrayList[genList.size()][3];
        edges = new ArrayList[genList.size()][3];
        
        //Initialize the arraylists
        for (int i = 0; i < genList.size(); i++) {
           for (int j = 0; j < 3; j++) {
              nodes[i][j] = new ArrayList<Integer>();
           }
        }
        for (int i = 0; i < genList.size(); i++) {
           for (int j = 0; j < 3; j++) {
              edges[i][j] = new ArrayList<Map<Integer, Integer>>();
           }
        }
        int count = 0;
        int biggest = 0;
        
        
         for (Network g: genList) {
           //System.out.println("=================Generation " + count + "===================="); 
           // System.out.println("_____________Inputs Layer_____________");
           for (Neuron n: g.inputs) {
             //System.out.println(n.getConnections());
            // System.out.println(n.getID());
             nodes[count][0].add(n.getID());
             
           }
           //System.out.println("");
           //System.out.println("_____________hidden Layer_____________");
           for (Neuron n: g.hidden) {
            // System.out.println(n.getConnections());
              //System.out.print(n.getID() + " connected to --> ");
              nodes[count][1].add(n.getID());
             for (Connection c: n.getConnections()) {
                //System.out.println(c);
                //System.out.print(c.getNeuron().getID() + " , ");
                Map<Integer, Integer> temp = new HashMap<Integer, Integer>();
                temp.put(n.getID(), c.getNeuron().getID());
                edges[count][1].add(temp);
             }
            //System.out.println("");

           }
           // System.out.println("");
           //System.out.println("_____________Outputs Layer_____________");
           for (Neuron n: g.outputs) {
             //System.out.println(n.getConnections());
             //System.out.print( n.getID() + " connected to --> ");
             
             nodes[count][2].add(n.getID());
             for (Connection c: n.getConnections()) {
                //System.out.println(c);
                //System.out.print(c.getNeuron().getID() + " , ");
                Map<Integer, Integer> temp = new HashMap<Integer, Integer>();
                temp.put(n.getID(), c.getNeuron().getID());
                edges[count][2].add(temp);
             }
              //System.out.println("");

           }
           
           
           
           count++;
           
        }
        
        // System.out.print("Try to print this stuff");
        
        //for (int i = 0; i < genList.size(); i++) {
        //  System.out.println("\n=============Generation: " + i + " =====================\n");
        //   for (int j = 0; j < 3; j++) {
        //     if (j == 0) {
        //      System.out.print("\nInputs Nodes --> "); 
        //     } else if (j == 1) {
        //        System.out.print("\nHidden Nodes --> "); 
        //     } else if (j == 2) {
        //       System.out.print("\nOutput Nodes --> ");
        //     } else {
        //        System.out.print("\nSomething went terribly wrong"); 
        //     }
        //      for (int k = 0; k < nodes[i][j].size(); k++) {
        //         System.out.print(nodes[i][j].get(k) + " , "); 
        //      }
        //      System.out.println("");
        //   }
        //}
        
        //for (int i = 0; i < genList.size(); i++) {
        //  System.out.println("\n=============Generation: " + i + " =====================\n");
        //   for (int j = 0; j < 3; j++) {
        //     if (j == 0) {
        //      System.out.print("\nInputs connections --> "); 
        //     } else if (j == 1) {
        //        System.out.print("\nHidden connections --> "); 
        //     } else if (j == 2) {
        //       System.out.print("\nOutput connections --> ");
        //     } else {
        //        System.out.print("\nSomething went terribly wrong"); 
        //     }
        //      for (int k = 0; k < edges[i][j].size(); k++) {
        //         System.out.print(edges[i][j].get(k) + " , "); 
        //      }
        //      System.out.println("");
        //   }
        //}
        
        
        
        
        X = 100;
        Y = 17 * 3;
        
        grid = new int[X][Y];
        resetGrid();
        
        
        //start(genList.size());
        
    }
    
    
     private void showText(int genCount){
        textAlign(CENTER);
        textSize(30);
        String levelString = "Generation " + genCount + "\n";
        fill(255);
        textSize(30);
        text(levelString, width/6, 50);
        String liveString = "";
        for (int i = 0; i < locals.player.getLives(); i++){
            liveString += " | ";
        }
        text(liveString, width - 50, 50);
    }
    
    private void resetGrid(){
      for (int x = 0; x < X; x++){
            for (int y = 0; y < Y; y++){
                grid[x][y] = -1;
            }
        }
    }
    
    private void setNodes(int generation) {
        resetGrid();
        
        int count = 0;
        int nodeNum = 0;
        int hiddenCount = 0;
        
        for (int j = 0; j < 3; j++) {
              for (int k = 0; k < nodes[generation][j].size(); k++) {
                count++;
                 if (j == 0) {
                    grid[1][count] = nodeNum; 
                 } else if (j == 1) {
                    grid[getRandomNumberInRange(10, 40)][getRandomNumberInRange(2, 40)] = hiddenCount + 4; 
                 } else if (j == 2) {
                    grid[49][count] = nodeNum;
                 } else {
                    System.out.print("\nSomething went terribly wrong"); 
                 } 
                 count++;
                 if (j != 1) {
                   nodeNum++;
                 }
                 hiddenCount++;
                 
               
              }
              count = 0;
              //System.out.println("");
         }
    }
    
    private boolean shouldConnect(int generation, int node1, int node2) {
         for (int j = 1; j < 3; j++) {
            for (int k = 0; k < edges[generation][j].size(); k++) {
               for (Map.Entry me: edges[generation][j].get(k).entrySet()) {
                  //if (((int) me.getKey() == node1 || (int) me.getKey() == node2) && ((int) me.getValue() == node1 || (int)me.getValue() == node2)) {
                  //  return true; 
                  //}
                  if ((int)me.getKey() == node1 && (int)me.getValue() == node2) {
                    return true; 
                  }
               }
               
            }
         }
        
        
        return false;
    }
   
   private void setEdges(int generation){
    //Draw the connections
          for (int x = 0; x < X; x++){
              for (int y = 0; y < Y; y++){
                  //Go through once
                  for (int a = 0; a < X; a++){
                      for (int b = 0; b < Y; b++){
                          //Go through second
                          if(grid[x][y] >= 0 && grid[a][b] >= 0) {
                            if(shouldConnect(generation, grid[x][y], grid[a][b])) {
                              
                              grid[x][y] = grid[a][b];
                              
                            }
                          }
                      }
                  }
              }
          } 
   }
    
    
    public void show(int generation, boolean refresh){
      
        showText(generation);
      
        //float squareSize = 880;
        float squareSize = 300;
        
        stroke(255);
        fill(0, 0, 0);
        rect(10, 60, squareSize, squareSize);
        
        float subSize = squareSize / Y;
        
        if (refresh) {
          setNodes(generation);
          //setEdges(generation);
        }
        
        for (int x = 0; x < X; x++){
            for (int y = 0; y < Y; y++){
                if (grid[x][y] >= 0){
                    fill(255);
                    noStroke();
                    rectMode(CORNER);
                    rect(10 + subSize * x, 60 + subSize * y, subSize, subSize);
                    for (int a = 0; a < X; a++){
                      for (int b = 0; b < Y; b++){
                        if (grid[x][y] == grid[a][b]) {
                          stroke(255);
                          line(10 + subSize * x, 60 + subSize * y, 10 + subSize * a, 60 + subSize * b);
                          
                          if (grid[x][y] >= 0){
                                fill(255, 0, 0);
                                noStroke();
                                rectMode(CORNER);
                                rect(10 + subSize * x, 60 + subSize * y, subSize, subSize);
                            }
                            if (grid[a][b] >= 0){
                                fill(255, 0, 0);
                                noStroke();
                                rectMode(CORNER);
                                rect(10 + subSize * a, 60 + subSize * b, subSize, subSize);
                            }
                          
                        }
                      }
                    }
                }
                
                
                
                //if (grid[x][y] < 0){
                //    fill(255, 0, 0);
                //    noStroke();
                //    rectMode(CORNER);
                //    rect(10 + subSize * x, 60 + subSize * y, subSize, subSize);
                //}
            }
        }
        
        if (refresh) {
          ////Draw the connections
          //for (int x = 0; x < X; x++){
          //    for (int y = 0; y < Y; y++){
          //        //Go through once
          //        for (int a = 0; a < X; a++){
          //            for (int b = 0; b < Y; b++){
          //                //Go through second
          //                if(grid[x][y] >= 0 && grid[a][b] >= 0) {
          //                  if(shouldConnect(generation, grid[x][y], grid[a][b])) {
          //                    stroke(255);
          //                    line(10 + subSize * x, 60 + subSize * y, 10 + subSize * a, 60 + subSize * b);
                              
          //                    //try to color connecting nodes
          //                    if (grid[x][y] >= 0){
          //                        fill(255, 0, 0);
          //                        noStroke();
          //                        rectMode(CORNER);
          //                        rect(10 + subSize * x, 60 + subSize * y, subSize, subSize);
          //                    }
          //                    if (grid[a][b] >= 0){
          //                        fill(255, 0, 0);
          //                        noStroke();
          //                        rectMode(CORNER);
          //                        rect(10 + subSize * a, 60 + subSize * b, subSize, subSize);
          //                    }
                              
                              
          //                  }
          //                }
          //            }
          //        }
          //    }
          //}
        }
 
    }
    
    private  int getRandomNumberInRange(int min, int max) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

}
