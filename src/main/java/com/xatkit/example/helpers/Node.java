package com.xatkit.example.helpers;

import static com.xatkit.example.helpers.Utils.*;
import java.util.*;

// each Node will point to a list of node ( predertimned )
public class Node{

    public Node[] nodes;
    public String header;

    public Node ( ) { this.nodes = null; this.header = null;}
    public Node ( String header ){ this.header = header; }

    public Node fillNodes ( Node[] values )
    {
        this.nodes = new Node[values.length];
        for ( int i = 0 ; i < this.nodes.length ; i ++  ) { nodes[i] = values[i]; }

        return this;
    }


    public Node fillNodes ( String[] values )
    {
        this.nodes = new Node[values.length];
        for ( int i = 0 ; i < this.nodes.length; i ++ ) { nodes[i] = new Node ( values[i] ); }

        return this;
    }

    public Node[] getNodes () { return this.nodes; }
    

    @Override
    public String toString() { return this.header; }

}
