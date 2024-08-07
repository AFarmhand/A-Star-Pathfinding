import java.util.*;

public class bot {

    // Coordinates of the bot
    int r;
    int c;

    // Flags to indicate existence and whether the bot has been taken
    boolean exists = false;
    boolean taken = false;

    // Default constructor
    bot() { }

    // Parameterized constructor to initialize bot's position
    bot(int x, int y) {
        r = x;
        c = y;
        exists = true;
    }

    // Method to move the bot to a new position
    public void moveTo(int a, int b) {
        r = a;
        c = b;
        exists = true;
    }

    // Method to mark the bot as taken
    public void take() {
        taken = true;
    }

    // Method to return the bot's position as a string
    public String toString() {
        return "(" + r + ", " + c + ")";    
    }
}

/* 
    This section contains commented-out code and placeholders for potential future use:

    // Example code to print the path in order
    for(int p = 0; p < path.size(); p++) {
        node tempN = path.get(p);
        System.out.println("Step " + (p + 1) + ": (" + tempN.r + ", " + tempN.c + ")");
    }

    // Example code to clear old paths
    int temp = path.size();
    for(int z = 0; z < temp; z++) {
        path.remove(0);
    }
    for(int z = 0; z < temp; z++) {
        tempPath.remove(0);
    }

    // Example code to print the map
    for (int r = 0; r < 17; r++) {
        System.out.println();
        for (int c = 0; c < 17; c++) {
            System.out.print(map[r].charAt(c));
        }
    }

    // Example code for A* pathfinding and updating nodes
    tempp = tempPath.size();
    for(int z = 0; z < tempp; z++) {
        tempPath.remove(0);
    } 
    goalR = end.r;
    goalC = end.c;
    resetNodes();
    goalReached = false;
    updateAround(spots[clue.r][clue.c], goalR, goalC);
    while(!goalReached) {
        aStar(goalR, goalC);
    }

    rZ = goalR;
    cZ = goalC;
    System.out.println("goal's distance from last goal: " + spots[rZ][cZ].gCost);
    tempPath.add(spots[rZ][cZ]);
    while(spots[rZ][cZ].hasParent) {
        int tempR = rZ;
        rZ = spots[rZ][cZ].parentR;
        cZ = spots[tempR][cZ].parentC;
        tempPath.add(spots[rZ][cZ]);
    }
    for(int p = tempPath.size() - 1; p >= 0; p--) {
        path.add(tempPath.get(p));
    }
*/
