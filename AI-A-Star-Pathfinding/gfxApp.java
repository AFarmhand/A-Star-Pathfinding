import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.lang.Math; 

class GfxApp extends Frame {
    // Dimensions of the grid
    int numRows = 17;  // 35 Rows are displayed. The top row (row 0) is hidden behind the title bar.
    int numCols = 17;
    
    // Array to store the background layout
    String background[] = new String[36];
    String fileName; // Filename for loading the grid data

    bot AI = new bot(); // AI Bot
    bot clue = new bot(); // To store encrypted clue position
    bot key = new bot(); // To store key to clue position
    bot dKey = new bot(); // To store decoy key position
    bot end = new bot(); // To store exit position

    // List to store random obstacles
    ArrayList<bot> randObs = new ArrayList<bot>();

    // Flags and inputs
    boolean toDraw = false;
    int inputR = 0;
    int inputC = 0;

    // Method to set the filename for the grid
    public void setFile(String a){
        fileName = a;
    }

    // Method to read the grid layout from a file
    public void readFile() throws IOException {
        BufferedReader inStream = new BufferedReader(new FileReader(fileName));
        String inString;                                       
        int a = 0;

        // Read each line from the file and store it in the background array
        while((inString = inStream.readLine()) != null) {
            background[a] = inString;
            a++;
        }
    }

    // Method to set the row and column for input
    void setValue(int x, int y) {
        inputR = x;
        inputC = y;
    }

    // Method to paint the grid and all game elements
    public void paint(Graphics g) {
        // Fill the background with light gray color
        g.setColor(java.awt.Color.lightGray);
        g.fillRect(0, 0, 30000, 30000);

        // Iterate through each cell in the grid
        for (int r = 0; r < numRows; r++)
            for (int c = 0; c < numCols; c++)
                // Draw based on the character in the background array
                switch(background[r].charAt(c)) {
                    case '.' : drawSpace(g, r, c); break;
                    case '_' : drawScaryBox(g, r, c); break;
                    case '-' : drawRedScaryBox(g, r, c); break;
                    case '#' : drawBlock(g, r, c); break;
                    case '>' : drawEnd(g, r, c); break;
                    default  : drawUnknown(g, r, c);
                }

        // Draw random obstacles
        for(int j = 0; j < randObs.size(); j++) {
            drawRNGBlock(g, randObs.get(j).r, randObs.get(j).c);
        }

        // Draw clue, key, and decoy key if they are present
        if (!(clue.r + clue.c == 0)) {
            drawClue(g, clue.r, clue.c);
        }

        if (!(key.r + key.c == 0)) {
            drawKey(g, key.r, key.c);
        }

        if (!(dKey.r + dKey.c == 0)) {
            drawDKey(g, dKey.r, dKey.c);
        }

        // Draw the AI bot
        drawAI(g, AI.r, AI.c);
    }

    // Method to set random obstacles
    public void setObstacles(ArrayList<bot> array) {
        // Clear existing obstacles
        for (int j = randObs.size() - 1; j >= 0; j--) {
            randObs.remove(j);
        }
        // Set new obstacles
        randObs = array;
    }

    // Method to draw the clue
    public void drawClue(Graphics g, int r, int c) {
        r = r * 30 + 10;
        c = c * 30 + 10;
        g.setColor(java.awt.Color.MAGENTA);
        g.drawOval(c + 5, r + 5, 15, 16);
        g.drawOval(c + 5, r + 5, 16, 16);
        g.drawOval(c + 5, r + 5, 17, 17);
        g.drawOval(c + 5, r + 5, 18, 18);
        g.setColor(java.awt.Color.black);
    }

    // Method to draw the key
    public void drawKey(Graphics g, int r, int c) {
        r = r * 30 + 10;
        c = c * 30 + 10;
        g.setColor(java.awt.Color.lightGray);
        g.fillOval(c + 6, r + 6, 18, 18);
    }

    // Method to draw the decoy key
    public void drawDKey(Graphics g, int r, int c) {
        r = r * 30 + 10;
        c = c * 30 + 10;
        g.setColor(java.awt.Color.gray);
        g.fillOval(c + 6, r + 6, 18, 18);
    }

    // Method to draw an empty space
    public void drawSpace(Graphics g, int r, int c) {
        g.setColor(java.awt.Color.blue);
        g.fillRect(c * 30 + 10, r * 30 + 10, 30, 30);
    }

    // Method to draw a block
    public void drawBlock(Graphics g, int r, int c) {
        int a = 11;
        g.setColor(java.awt.Color.black);
        g.fillRect(c * 30 + 10, r * 30 + 10, 30, 30);
        g.setColor(java.awt.Color.blue);
        // Uncomment below lines to draw block details
        /* g.fillRect(c * 30 + 10, r * 30 + 10, a, a);
           g.fillRect(c * 30 + 10, r * 30 + 30 - a + 10, a, a);
           g.fillRect(c * 30 + 10 + 30 - a, r * 30 + 10, a, a);
           g.fillRect(c * 30 + 10 + 30 - a, r * 30 + 10 + 30 - a, a, a); */
    }

    // Method to draw a random obstacle block
    public void drawRNGBlock(Graphics g, int r, int c) {
        int a = 11;
        g.setColor(java.awt.Color.purple);
        g.fillRect(c * 30 + 10, r * 30 + 10, 30, 30);
        // Uncomment below lines to draw block details
        /* g.fillRect(c * 30 + 10, r * 30 + 10, a, a);
           g.fillRect(c * 30 + 10, r * 30 + 30 - a + 10, a, a);
           g.fillRect(c * 30 + 10 + 30 - a, r * 30 + 10, a, a);
           g.fillRect(c * 30 + 10 + 30 - a, r * 30 + 10 + 30 - a, a, a); */
    }

    // Method to draw a red scary box
    public void drawRedScaryBox(Graphics g, int r, int c) {
        g.setColor(java.awt.Color.red);
        g.fillRect(c * 30 + 10, r * 30 + 10, 30, 30);
/*         g.setColor(java.awt.Color.red);
        g.fillRect(c, r, 30, 30);
        g.setColor(java.awt.Color.pink);
        g.fillRect(c + 3, r + 12, 14, 8);
        g.fillRect(c + 3, r, 14, 8);
        g.fillRect(c, r, 7, 30);
        g.fillRect(c + 13, r, 7, 30);*/
    }

    // Method to draw a scary box
    public void drawScaryBox(Graphics g, int r, int c) {
        r = r * 30 + 10;
        c = c * 30 + 10;
        g.setColor(java.awt.Color.green);
        g.fillRect(c, r, 30, 30);
        g.setColor(java.awt.Color.CYAN);
        g.fillRect(c + 3, r + 12, 14, 8);
        g.fillRect(c + 3, r, 14, 8);
        g.fillRect(c, r, 3, 30);
        g.fillRect(c + 17, r, 3, 30);
    }

    // Method to draw an unknown object
    public void drawUnknown(Graphics g, int r, int c) {
        r = r * 30 + 10;
        c = c * 30 + 10;
        g.fillOval(c + 12, r + 3, 5, 5);
        g.fillOval(c + 3, r + 3, 5, 5);
        g.drawArc(c + 6, r + 9, 8, 13, 0, 180);
    }

    // Method to draw the AI bot
    public void drawAI(Graphics g, int r, int c) {
        r = r * 30 + 10;
        c = c * 30 + 10;
        g.setColor(java.awt.Color.pink);
        g.fillRect(c + 3, r + 2, 26, 26);
        g.setColor(java.awt.Color.black);
        g.fillOval(c + 30 - 16, r + 3, 5, 5);
        g.fillOval(c + 30 - 7, r + 3, 5, 5);
        g.fillRect(c + 30 - 4 - 13, r + 14, 13, 4);
    }

    // Method to draw the exit
    public void drawEnd(Graphics g, int r, int c) {
        if (end.taken) {
            r = r * 30 + 10;
            c = c * 30 + 10;
            g.setColor(java.awt.Color.green);
            g.fillRect(c + 2, r + 2, 26, 26);
            g.setColor(java.awt.Color.red);
            g.fillRect(c + 4, r + 4, 22, 22);
            g.setColor(java.awt.Color.green);
            g.fillRect(c + 6, r + 6, 18, 18);
            g.setColor(java.awt.Color.red);
            g.fillRect(c + 8, r + 8, 12, 12);
            g.setColor(java.awt.Color.green);
            g.fillRect(c + 11, r + 11, 8, 8);
        } else {
            g.setColor(java.awt.Color.blue);
            g.fillRect(c * 30 + 10, r * 30 + 10, 30, 30);
        }
    }

    // Method to move the AI bot to a new position
    void moveAI(int a, int b) {
        AI.moveTo(a, b);
        // System.out.println("AI is at " + AI); // Uncomment to print AI's position
    }

    // Method to move the clue to a new position
    void moveClue(int a, int b) {
        clue.moveTo(a, b);
    }

    // Method to move the key to a new position
    void moveKey(int a, int b) {
        key.moveTo(a, b);
    }

    // Method to move the decoy key to a new position
    void moveDKey(int a, int b) {
        dKey.moveTo(a, b);
    }

    // Method to mark the end position as taken
    void findEnd() {
        end.take();
    }
}
