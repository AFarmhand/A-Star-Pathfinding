import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.lang.Math; 


public class pathFinding extends Frame {
  private char mat[][]; // 2d character array that stores the maze display (not in use currently)
  String map[] = new String[17]; //this is the actual map
  ArrayList<node> path = new ArrayList<node>(); //path taken to destination

  ArrayList<bot> randObs = new ArrayList<bot>();

  node spots[][] = new node[17][17]; //each spot on the map is a node
 
  String fileName;
  String stringAdded = "";
  GfxApp gfx = new GfxApp(); //gfx object
  
  Random RNG; 

  bot end = new bot(); //to store goal position
  bot clue = new bot(); //to store encrypted clue position
  bot key = new bot(); //to store key to clue position
  bot dKey = new bot(); //to store decoy key position
  bot AIPos = new bot(2,2); //to store bot position

  int timeR;
  int realTime;
  boolean goalReached = false;

  int totalStepsYay = 0;

  public void run(String a, int seed, int numToDo, int speed) throws IOException{

		BufferedWriter moves = new BufferedWriter(new FileWriter("output.txt"));

    moves.write("Steps for AI - Seed: " + seed);
    moves.newLine();
    moves.newLine();

    for(int k = 1; k <= numToDo; k++){
      System.out.print("Starting Map " + k);
      if(k == numToDo){
        System.out.println(", the final map!!");
      }
      else{
        System.out.println("!");
      }
      fileName = a + Integer.toString(k) + ".dat";
      pathFinding pathFinder = new pathFinding();
      pathFinder.setSpeed(speed);
      pathFinder.setFile(fileName);
      pathFinder.setSeed(seed);
      pathFinder.drawMap(k);
      pathFinder.redrawMap();
      System.out.println("Map " + k + " completed!");
      pathFinder.wait((10-speed) * 130 * 5);
      moves.write("AI in Map " + k +" ");
      moves.write(pathFinder.stringAdded);
      moves.newLine();
      moves.newLine();
      System.out.println();
    }
    moves.close();
  }

  public void addToString(String a){
    stringAdded += a;
  }

  public void setSpeed(int s){
    realTime = (10-s) * 130;

  }

  public void makeObstacles(){

    int numb =  6;
    while(numb != 0){
      int r = RNG.nextInt(15)+1;
      int c = RNG.nextInt(15)+1;
      if(
        ((((spots[r][c] != spots[clue.r][clue.c]) &&
      (spots[r][c] != spots[key.r][key.c])) &&
      ((spots[r][c] != spots[dKey.r][dKey.c])) &&
      (spots[r][c] != spots[AIPos.r][AIPos.c])) &&
      ((spots[r][c] != spots[end.r][end.c]) &&
      (spots[r][c].isPassable)))
      ){
        int repeated = 0;
        boolean test = false;
        for(int j = 0; j < randObs.size(); j++){
          test = ((randObs.get(j).r == r) && (randObs.get(j).c == c));
          if(test){
            repeated += 1;
          }
        }

        if(repeated == 0){
          randObs.add(new bot(r, c));
          //System.out.println("added " + r + " " + c);
          numb -= 1;
        }
        if(repeated > 0){
          //System.out.println("repeated " + r +" " + c);
        }
      }
    } 
    
    for(int j = 0; j < randObs.size(); j++){
      spots[randObs.get(j).r][randObs.get(j).c].isPassable = false;
    }

  }
 

  public void makeNodes(){ 
    for (int rr = 0; rr < 17; rr++){ //create all nodes[row][col]
      for(int cc = 0; cc < 17; cc++){
        if (map[rr].charAt(cc) == '-' || map[rr].charAt(cc) == '#'){  
        //map is in 1d array? //blocks false isPassable
          spots[rr][cc] = new node(rr, cc, false);                   
        }
        else if(map[rr].charAt(cc) == '>'){ //exit node
          spots[rr][cc] = new node(rr, cc, true);
          end.moveTo(rr, cc); //set end coords
        }
        else{
          spots[rr][cc] = new node(rr, cc, true);
        }
      }
    }
  }

  public void makeClues(){
    int max = 15;
    int min = 1;
    
    while(!clue.exists){
      //int r = (int)(Math.random() * (max - min + 1) + min);
      int r = (int)(RNG.nextInt(15)+1); 
      int c = (int)(RNG.nextInt(15)+1); 

      if ((spots[r][c] != spots[AIPos.r][AIPos.c] && spots[r][c] != spots[end.r][end.c]) && (spots[r][c].isPassable)){
        clue.moveTo(r,c);    
      }
    }

    while(!key.exists){
      int r = (int)(RNG.nextInt(15)+1); 
      int c = (int)(RNG.nextInt(15)+1);

      if ((spots[r][c] != spots[AIPos.r][AIPos.c] && spots[r][c] != spots[end.r][end.c]) && (spots[r][c].isPassable && spots[r][c] != spots[clue.r][clue.c])){
        key.moveTo(r,c);    
      }
    }

    while(!dKey.exists){
      int r = (int)(RNG.nextInt(15)+1); 
      int c = (int)(RNG.nextInt(15)+1); 

      if (((spots[r][c] != spots[AIPos.r][AIPos.c] && spots[r][c] != spots[key.r][key.c]) && spots[r][c] != spots[end.r][end.c]) && (spots[r][c].isPassable && spots[r][c] != spots[clue.r][clue.c])){
        dKey.moveTo(r,c);    
      }
    }


  }

  public void setSeed(int s){
    RNG = new Random(s);

  }

  public void setFile(String x){
    fileName = x;
  }

  public void drawMap(int mapNum) throws IOException{
    BufferedReader inStream = new BufferedReader(new FileReader(fileName));
    String inString;                                       
    int a = 0;        
    
    while((inString = inStream.readLine()) != null)  
    {
      map[a] = inString;
      a++;
    } 


    gfx.moveAI(AIPos.r,AIPos.c);
    makeNodes();
  
    if(mapNum > 0)
//      makeObstacles();

    makeClues();
    gfx.moveClue(clue.r,clue.c); //adding clue
    gfx.moveKey(key.r,key.c); //adding key
    gfx.moveDKey(dKey.r, dKey.c);
    gfx.setObstacles(randObs);


		gfx.setSize(1024,768);
		gfx.addWindowListener(new WindowAdapter() {public void
		windowClosing(WindowEvent e) {System.exit(0);}});
    gfx.setFile(fileName);
    gfx.readFile(); 
    wait(realTime * 3);
		gfx.show();
  }
         
  public void findPath(){
    int lowestG;
    int sR = AIPos.r;
    int sC = AIPos.c;
    int goalR;
    int goalC;

    int clueG;
    int keyG;
    int dKeyG;

    while(!(clue.taken && key.taken)){
      
      //System.out.println(clue.taken + " " + key.taken + " " + dKey.taken);
      lowestG = 100000;

      if(!clue.taken){
        superA(sR, sC, clue.r, clue.c, false);
        clueG = spots[clue.r][clue.c].gCost;
        lowestG = clueG;
      }
      else{
        clueG = 9999;
      }
      
      if(!key.taken){
        superA(sR, sC, key.r, key.c, false);
        keyG = spots[key.r][key.c].gCost;
      }
      else{
        keyG = 99999;
      }
      if(keyG < lowestG && !key.taken){
        lowestG = keyG;
      }

      if(!dKey.taken){
        superA(sR, sC, dKey.r, dKey.c, false);
        dKeyG = spots[dKey.r][dKey.c].gCost;
      }
      else{
        dKeyG = 99999;
      }

      if(dKeyG < lowestG && !dKey.taken){
        lowestG = dKeyG;
      }

      if(lowestG == clueG && !clue.taken){
        superA(sR, sC, clue.r, clue.c, true);
        clue.take();
        sR = clue.r;
        sC = clue.c;
      }
      else if(lowestG == keyG && !key.taken){
        superA(sR, sC, key.r, key.c, true);
        key.take();
        sR = key.r;
        sC = key.c;
      }
      else if(lowestG == dKeyG && !dKey.taken){
        superA(sR, sC, dKey.r, dKey.c, true);
        dKey.take();
        sR = dKey.r;
        sC = dKey.c;
      }
    }
    
    superA(sR, sC, end.r, end.c, true);
    
  }

  public void superA(int initialR, int initialC, int goalR, int goalC, boolean pathTo){

    ArrayList<node> tempPath = new ArrayList<node>(); //exists to die
    resetNodes();
    updateAround(spots[initialR][initialC], goalR, goalC);//first update
    goalReached = false;
    while(!goalReached){
        aStar(goalR, goalC);
    }

    if(pathTo){
      int rZ = goalR;
      int cZ = goalC;
      tempPath.add(spots[rZ][cZ]); //add final final spot
      while(spots[rZ][cZ].hasParent){ //parents to path
        int tempR = rZ;
        rZ = spots[rZ][cZ].parentR;
        cZ = spots[tempR][cZ].parentC;
        tempPath.add(spots[rZ][cZ]);
      }
      for(int p = tempPath.size() - 1; p >= 0; p--) //reversing to path
      {
          path.add(tempPath.get(p));
      }
      if(spots[clue.r][clue.c] == spots[goalR][goalC]){
        addToString(" > " + (tempPath.size()-1) + " steps to clue");
        totalStepsYay += tempPath.size()-1;
      }
      if(spots[end.r][end.c] == spots[goalR][goalC]){
        addToString(" > " + (tempPath.size()-1) + " steps to end");
        totalStepsYay += tempPath.size()-1;
      }
      if(spots[key.r][key.c] == spots[goalR][goalC]){
        addToString(" > " + (tempPath.size()-1) + " steps to key");
        totalStepsYay += tempPath.size()-1;
      }
      if(spots[dKey.r][dKey.c] == spots[goalR][goalC]){
        addToString(" > " + (tempPath.size()-1) + " steps to dummy key");
        totalStepsYay += tempPath.size()-1;
      }
    }
  }

  public void redrawMap() throws IOException{
    findPath();
    int c = 0;
    int k = 0;
    int d = 0;
    int e =0;
    Scanner input = new Scanner(System.in); //inputting pauses
    String side1;
    addToString(" == " + totalStepsYay + " total steps to solve!");
    wait(realTime * 3);

    for(int p = 0; p < path.size(); p++){
      if(e==1){
        for (int o = 0; o < 3; o++){
          System.out.println("Deciphering...");
          wait(realTime + (realTime / 2));
        }
        System.out.println("Deciphered! End is at (" + end.r + ", " + end.c + ")!!!!");
        wait(realTime * 2);
        gfx.findEnd();
        e = 2;
      }

      AIPos.moveTo(path.get(p).r, path.get(p).c);
      gfx.moveAI(AIPos.r,AIPos.c);
      gfx.repaint();


      if(AIPos.r == clue.r && AIPos.c == clue.c){//getting clue
        wait(realTime / 3);
        clue.moveTo(0,0);
        gfx.moveClue(0,0);
        c = 1;
        if(k==1){
          System.out.println("Key and Clue both found!! Now deciphering...");
          e = 1;
        }
        else if(d==1 && k == 0){
          System.out.println("Key and clue found... but the key is a dummy key and doesn't unlock the clue :(");
        }
        else{
          System.out.println("Clue found!!");
        }    
        wait(realTime * 2);    
      }      
      if(AIPos.r == key.r && AIPos.c == key.c){//getting key
        wait(realTime / 3);
        key.moveTo(0,0);
        gfx.moveKey(0,0);
        k = 1;
        if(c == 1){
          System.out.println("Key and Clue both found!! Now deciphering...");
          e = 1;
        }
        else{
          System.out.println("Key found!!");
        }
        wait(realTime * 2);
      }
      if(AIPos.r == dKey.r && AIPos.c == dKey.c){//getting dKey
        wait(realTime / 3);
        dKey.moveTo(0,0);
        gfx.moveDKey(0,0);
        d = 1;
        if(c==1 && k == 0){
          System.out.println("Key and clue found... but the key is a dummy key and doesn't unlock the clue :(");
        }
        else{
          System.out.println("Key found...?");
        }
        wait(realTime*2);
      }
      
      wait(realTime);
      //side1 = input.nextLine();


    }

          
  }

  public void writeFile(){
    
  }

  public void wait(int a){
    try{
        Thread.sleep(a);
    }
    catch(InterruptedException ex){
      Thread.currentThread().interrupt();
    }      
  }

  public void aStar(int goalR, int goalC){
    int lowestH = 10000; //finding lowest hCost of open nodes
    for (int r = 0; r < 17; r++){ //cycle through all hCosts
      for(int c = 0; c < 17; c++){
        if((spots[r][c].hCost != 0) && (spots[r][c].hCost < lowestH && spots[r][c].isOpen)){
          lowestH = spots[r][c].hCost;
        }
      }
    }
    for (int r = 0; r <17; r++){ 
      for(int c = 0; c < 17; c++){
        if(spots[r][c].hCost == lowestH && spots[r][c].isOpen){
          if(r == goalR && c == goalC){ //if lowest hCost is end
            goalReached = true;
            return;
          }
          else{ //update first open node with lowest hCost
            updateAround(spots[r][c], goalR, goalC);
            return;
          }
        }
      }
    }
  }
  
  public void updateAround(node a, int goalR, int goalC){
    spots[a.r][a.c].close(); //close node when using it
    for(int x = -1; x <= 1; x++){
      int r = a.r+x;
      int c = a.c;
      if(!spots[r][c].isClosed && spots[r][c].isPassable && x != 0){
        if((spots[r][c].gCost > findGCost(r, c, a.r, a.c)) || (spots[r][c].gCost == 0)){//if gCost is zero or is higher than new gCost then update
          spots[r][c].gCost = findGCost(r, c, a.r, a.c);
          spots[r][c].fCost = findFCost(r,c,goalR,goalC); 
          spots[r][c].findHCost();
          spots[r][c].open();
          spots[r][c].setParent(a.r, a.c); //set parent      
        }
      }
    }
    for(int y = -1; y <= 1; y++){
      int r = a.r;
      int c = a.c+y;
      if(!spots[r][c].isClosed && spots[r][c].isPassable && y != 0){
        if((spots[r][c].gCost > findGCost(r, c, a.r, a.c)) || (spots[r][c].gCost == 0)){//if gCost is zero or is higher than new gCost then update
          spots[r][c].gCost = findGCost(r, c, a.r, a.c);
          spots[r][c].fCost = findFCost(r,c,goalR,goalC); 
          spots[r][c].findHCost();
          spots[r][c].open();
          spots[r][c].setParent(a.r, a.c); //set parent
        }
      }
    }
  }

  public void resetNodes(){
    for (int r = 0; r < 17; r++){
      for (int c = 0; c < 17; c ++){
        spots[r][c].gCost = 0;
        spots[r][c].fCost = 0;
        spots[r][c].findHCost();
        spots[r][c].setParent(0,0);
        spots[r][c].isClosed = false;
        spots[r][c].isOpen = false;
        spots[r][c].hasStatus = false;
        spots[r][c].hasParent = false;
      }
    }
  }

  public int findGCost(int ra, int ca, int parentR, int parentC){//finds gCost value from iniital node's gCost + distance to new node
    double r = ra * 10;
    double c = ca * 10;
    double sR = parentR * 10;
    double cR = parentC * 10;
    int cost;
    double costt = (r-sR) * (r-sR) + (c-cR) * (c-cR);  
    costt = Math.sqrt(costt);
    cost = (int) costt;
    cost += spots[parentR][parentC].gCost;
    return cost;
  }

  public int findFCost(int ra, int ca, int goalR, int goalC){//finds distance to goal as fCost
    double r = ra * 10;
    double c = ca * 10;
    double sR = goalR * 10;
    double cR = goalC * 10;
    int cost;
    double costt = (r-sR) * (r-sR) + (c-cR) * (c-cR);  
    costt = Math.sqrt(costt);
    cost = (int) costt;
    return cost;
  }
}











