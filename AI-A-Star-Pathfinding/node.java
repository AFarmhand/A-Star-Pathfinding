import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.lang.Math; 

public class node {

    // Cost values for pathfinding
    int gCost; // Cost from the start node
    int fCost; // Total cost (gCost + hCost)
    int hCost; // Heuristic cost to the goal

    // Flags and attributes for node status
    boolean isPassable; // Whether the node is passable

    int parentR; // Row of the parent node
    int parentC; // Column of the parent node
    boolean untouched; // Indicates if the node has been touched
    boolean isOpen; // Whether the node is open for processing
    boolean isClosed; // Whether the node is closed for processing

    boolean hasParent = false; // Indicates if the node has a parent
    boolean hasStatus = false; // Indicates if the node has status info

    // Coordinates of the node
    int r; 
    int c;

    // Default constructor
    node() { }

    // Parameterized constructor to initialize node position and passability
    node(int x, int y, boolean p) {
        r = x;
        c = y;
        isPassable = p;
    }

    // Marks the node as closed
    void close() {
        isOpen = false;
        isClosed = true;
        hasStatus = true;
        untouched = false;
    }

    // Marks the node as open
    void open() {
        isOpen = true;
        isClosed = false;
        hasStatus = true;
        untouched = false;
    }

    // Sets the parent of the node
    void setParent(int e, int f) {
        parentR = e;
        parentC = f;
        hasParent = true;
    }

    // Gets the row of the parent node
    public int getParentR() {
        return parentR;
    }

    // Gets the column of the parent node
    public int getParentC() {
        return parentC;
    }

    // Calculates the heuristic cost (hCost)
    void findHCost() {
        hCost = gCost + fCost;
    }

    // Returns a string representation of the node with its details
    public String toString() {
        if (hasStatus) {
            return "(" + r + ", " + c + "), gCost: " + gCost + ", fCost: " + fCost + ", hCost: " + hCost + ", Parent: (" + (int) parentR + ", " + (int) parentC + "), isOpen?: " + isOpen + ", isClosed?: " + isClosed;
        } else if (hasParent) {
            return "(" + r + ", " + c + "), gCost: " + gCost + ", fCost: " + fCost + ", hCost: " + hCost + ", Parent: (" + (int) parentR + ", " + (int) parentC + ")";      
        } else if (isPassable) {
            return "(" + r + ", " + c + "), gCost: " + gCost + ", fCost: " + fCost + ", hCost: " + hCost;
        } else {
            return "(" + r + ", " + c + ") - (x)";
        }
    }
}
