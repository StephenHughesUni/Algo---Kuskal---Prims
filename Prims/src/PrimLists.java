/**
 * ------------------------------------------
 * - Student Name    --   Student Code      -
 * - Stephen Hughes  --   D21126653         -
 * - Module          --   Algorithms & Data -
 * ------------------------------------------
 *
 * ----------------------------------------------------------
 * - Prims Algorithm  -- Traversing using Breadth and Depth -
 * ----------------------------------------------------------
 */

import java.io.*;
import java.util.*;

class Heap{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size

    // The heap constructor gets past from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos){
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    // Simplistic Boolean
    public boolean isEmpty(){
        return N == 0;
    }


    // SiftUp from LabTest + Lab work
    public void siftUp( int k){
        int v = a[k];


        a[0] = 0;  // Puts a default node at the top of the heap.
        dist[0] = 0;

        // while the distance of vertex is smaller than parents then
        while(dist[v] < dist[a[k/2]]){
            a[k] = a[k/2];  // moves up the tree, into position of child
            hPos[a[k]] = k; // hPos[] modified
            k = k/2;    // index changes
        }
        a[k] = v;       // resetting index added to heap
        hPos[v] = k;    // resetting added to hPos[]
    }


    // sorts the heap nodes into the right positions
    public void siftDown( int k){
        int v, j;

        v = a[k];

        while((2 * k) <= N){
            j = 2 * k;

            // Checks the heap to see if something is there  -- then finds the larger branch.
            if(j < N && dist[a[j]] > dist[a[j+1]]){
                j++;
            }

            // Comparing the position with the node being moved
            if (dist[v] < dist[a[j]]){
                break;
            }
            a[k] = a[j];
            hPos[a[k]] = k;
            k = j;      // update position
        }

        a[k] = v;       // reset
        hPos[v] = k;    // reset
    }


    //Adding a node, and sifts it up.
    public void insert( int x){
        a[++N] = x;     // attach vertex to end of heap
        siftUp( N);     // pass index for siftup
    }


    public int remove(){
        int v = a[1];

        hPos[v] = 0;    //v is not in the heap
        a[1] = a[N--];  //put empty node into empty spot


        siftDown(1); // index top of sift down
        a[N+1] = 0;     // null node to empty

        return v;       // return top vertex
    }
} // End of Heap

class Queue {

    private class Node {
        int data;
        Node next;
    }

    // Setups
    Node z;
    Node head;
    Node tail;

    public Queue() {
        z = new Node();     // sentinel node
        z.next = z;         // point to self
        head = z;           // head points to sentinel
        tail = null;        // tail is null
    }

    public void enQueue(int x) {
        Node temp;

        temp = new Node();
        temp.data = x;
        temp.next = z;  // new node is initialised - points sentinel

        if (head == z)  // if empty list case
            head = temp;
        else
            tail.next = temp; // if not empty then

        tail = temp;
    }


    public int deQueue() {  // point to next and delete
        int x = head.data;
        head = head.next;

        if (head == head.next) {
            tail = null;
        }
        return x;
    }

    public boolean isEmpty() {
        return head == head.next;
    }
}

// Graph code to support the Algo.
class GraphLists {
    class Node {
        public int vert;
        public int wgt;
        public Node next;

    Node(int vert, int wgt, Node n) {
        this.vert = vert;
        this.wgt = wgt;
        next = n;
    }
    Node() {}
}

    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    // mst[] is the value of parent[] from Prim to print
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;

    // used for traversing graph
    private int[] visited;
    private int id;

    private int count = 0;
    private int last = Integer.MIN_VALUE;

    public int getCount() {
        return (count);
    }
    public int getLast() {
        return (last);
    }

    // default constructor
    public GraphLists(String graphFile)  throws IOException{
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        String splits = " +";
        String line = reader.readLine();
        String[] parts = line.split(splits);
        System.out.println("Displaying Parts[] = " + parts[0] + " " + parts[1]);

        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        // sentinel node
        z = new Node();
        z.next = z;

        // adjacency list, starts to sentinel node z
        adj = new Node[V+1];
        for(v = 1; v <= V; ++v)
            adj[v] = z;

        // read the edges
        System.out.println("Reading in edges from text file:");
        System.out.println("\n");
        for(e = 1; e <= E; ++e){
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]);
            wgt = Integer.parseInt(parts[2]);

            System.out.println("Graph Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));


            if (u > last) last = u;
            if (v > last) last = v;

            adj[v] = new Node(u, wgt, adj[v]);
            adj[u] = new Node(v, wgt, adj[u]);

            count++;
        }
    }

    // convert vertex into char for pretty printing
    private char toChar(int u){
        return (char)(u + 64);
    }

    // method to display the graph representation
    public void display(){
        int v;
        Node n;

        System.out.println("\nDisplaying graph adjacancies:\n");
        for(v=1; v<=V; ++v){

            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next)
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");
        }
        System.out.println("\n");
    }



    public void MST_Prim(int s){
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        // Int arrays
        dist = new int[V + 1];
        parent = new int[V + 1];
        hPos = new int[V + 1];
        visited = new int[V + 1];

        //
        for (int i = 0; i <= V; i++){
            dist[i] = Integer.MAX_VALUE;
            parent[i] = 0;
            hPos[i] = 0;
        }

        dist[s] = 0; // starts distance

        Heap pq =  new Heap(V, dist, hPos); // Heap starts empty
        pq.insert(s);                       // root of MST is S

        while (!pq.isEmpty()){
            v = pq.remove();
            wgt_sum += dist[v];

            System.out.print("Adding to MST edge " + toChar(parent[v]) + "--(" + dist[v] + ")--" + toChar(v) + "\n");

            dist[v] = -dist[v];


            for(t = adj[v]; t != z; t = t.next){
                wgt = t.wgt;
                u = t.vert;


                if(wgt < dist[u]){
                    dist[u] = wgt;
                    parent[u] = v;


                    if(hPos[u] == 0){       // if not in heap then use insert
                        pq.insert(u);
                    }
                    else{
                        pq.siftUp(hPos[u]); // if value already within heap then siftup the node
                    }
                }
            }
        }
        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");

        mst = parent;
    } // end of MST

    // Print MST
    public void showMST(){

        System.out.print("\n\nMinimum Spanning tree parent array is:\n");
        for(int v = 1; v <= V; ++v)
            System.out.println(toChar(v) + " -> " + toChar(mst[v]));
        System.out.println("\n");
    }

    // method to initialise Depth First Traversal of Graph
    public void DF( int s) {
        id = 0;
        System.out.println("\nExecuting Depth First Search");
        for(int v = 1; v <= V; v++) {
            visited[v] = 0;
        }

        System.out.print("\nDepth First Graph Traversal\n");
        System.out.println("Starting with Vertex " + toChar(s));


        dfVisit( 0, s);
        System.out.print("\n");

    }

    // Recursive Depth First Traversal for adjacency linked lists
    private void dfVisit( int prev, int v){
        int u;

        visited[v] = id++;

        System.out.print("\nDF just visited vertex: " + toChar(v) + " along " +
                toChar(prev) + "--" + toChar(v) );

        for(Node t = adj[v]; t != z; t = t.next){
            u = t.vert;
            if(visited[u] == 0){
                dfVisit(v, u);
            }
        }
    }

    // Iterative Breadth First Traversal using a queue
    public void BF(int s) {
        visited = new int[V + 1];
        for (int v = 1; v <= V; v++) {
            visited[v] = 0;
        }
        id = 0;
        int u, v;
        Node t;

        visited[s] = ++id;
        System.out.print("\n  DF just visited vertex " + toChar(s));

        Queue Q = new Queue();
        Q.enQueue(s);

        while (!Q.isEmpty()) {
            v = Q.deQueue();
            for (t = adj[v]; t != z; t = t.next) {

                u = t.vert;
                if (visited[u] == 0) {
                    visited[u] = ++id;
                    System.out.print("\n  DF just visited vertex " + toChar(u));
                    Q.enQueue(u);
                }
            }   //end of for
        }   //end of while
    }   //end BF
}   // end of class


public class PrimLists{
    public static void main(String[] args) throws IOException {
        // error handling
        System.out.println(" ");
        Scanner sc= new Scanner(System.in);
        String fname = new String(getUserInput("Enter .txt file name to read in: "));
        System.out.println(" ");
        boolean fileError = true;
        GraphLists g = null;



        while (fileError) {
            String newfName = "";
            try {
                g = new GraphLists(fname);
                fileError = false; // won't run if an fileError is caught
            } catch (IOException e) { // to catch if the file is incorrect named

                try {
                    newfName = getUserInput("File not found, enter a valid file name: ");
                } catch (IOException f) {
                    System.out.println("Invalid input");

                }

                fname = newfName; //reassign the string f-name to this one.
            }
        }

        fileError = true;
        int getNum = g.getLast();
        while (fileError) {
            getNum = Integer.MIN_VALUE;
            try {
                System.out.println(" ");
                getNum = Math.abs(Integer.parseInt(getUserInput("Enter what vertex to start at : ")));


                fileError = false;
            } catch (IOException f) {
                System.out.println("Invalid input, must use numeric values");

            }
            if (!fileError && getNum > g.getLast()) {
                System.out.println("Number to high, must be below " + (g.getLast() + 1));
                fileError = true;
            }
        }
        int s = getNum;

        // updaters
        g.display();

        g.MST_Prim(s);

        g.showMST();

        g.DF(s);

        g.BF(s);
    }

    // get user input
    public static String getUserInput(String q) throws IOException {
        Console console = System.console();
        return console.readLine(q);
    }
}