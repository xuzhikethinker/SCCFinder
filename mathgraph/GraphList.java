/*
 * Created on June 14, 2010
 *
 * Copyright (c) 2010, Voulimeneas Alexios
 * All rights reserved.
 *
 * This software is open-source under the BSD license
 * see "license.txt" for more information 
 */
package mathgraph;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Class representation of a graph
 * 
 * @author Voulimeneas Alexios - avoulimeneas@gmail.com
 */
public class GraphList {
	
    private int clock = 0;
    // number of strongly connected components of this graph
    private int scc = 0;
    // GraphList's adjacency list in vector form
    private ArrayList<GraphNode> adjacencylist = new ArrayList<GraphNode>();
   
    /**
     * Default constructor
     */
    public GraphList() { 	
    	
    }
    
    /**
     * Another constructor
     */
    public GraphList(ArrayList<GraphNode> adjacencylist) {
    	this.adjacencylist.addAll(adjacencylist);
    }
    
    /**
     * Sets the adjacency list of the graph
     */
    public void setAdjacencylist(ArrayList<GraphNode> adjacencylist) {
    	this.adjacencylist = adjacencylist;
    }
    
    /**
     * Returns the adjacency list of the graph
     */
    public ArrayList<GraphNode> getAdjacencylist() {
    	return this.adjacencylist;
    }
    
    /**
     * Returns the number of strongly connected components of this graph
     */
    public int getSCCNumber() {
    	return this.scc;
    }
    
    /**
     * Returns a vertex by giving it's name 
     */
    public GraphNode getNodeWithGivenName(int name) {
    	for(GraphNode node : this.adjacencylist) {
        	if(node.getName() == name) return node;
        }
        return null;
    }
    
    /**
     * Explore algorithm
     */
    public void explore(GraphNode node) {
        node.previsit(this.clock++);
        for(int value : node.getConnections()) {
        	GraphNode tmp = this.getNodeWithGivenName(value);
            if(!tmp.isVisited()) this.explore(tmp);
        }
        node.postvisit(this.clock++);
    }
    
    /**
     * Modified explore algorithm for scc algorithm
     */
    public void explore2(GraphNode node) {
        node.previsit(this.clock++); 
        node.setSCC(this.scc); 
        for(int value : node.getConnections()) {
        	GraphNode tmp = this.getNodeWithGivenName(value);
            if(!tmp.isVisited()) this.explore2(tmp);
        }
        node.postvisit(this.clock++);
    }
    
    /**
     * Dfs algorithm for this graph 
     */
    public void dfs() {
    	this.clock = 1;
    	for(GraphNode tmp : this.adjacencylist) {
    		tmp.setVisited(false);
	    }
    	for(GraphNode tmp : this.adjacencylist) {
	    	if(!tmp.isVisited()) this.explore(tmp);
	    }
    }
    
    /**
     * Modified dfs for scc algorithm for this graph  
     */
    public void dfs2() {
    	this.clock = 1;
    	this.scc = 0;
    	for(GraphNode tmp : this.adjacencylist) {
        	tmp.setVisited(false);
        }
    	for(GraphNode tmp : this.adjacencylist) {
        	if(!tmp.isVisited()) {
        		this.scc++; 
        		this.explore2(tmp);
        	}
        }
    }
    
    /**
     * Makes and returns the reverse graph of this graph
     */
    public GraphList reverseGraph() {
    	ArrayList<GraphNode> tmpAdjacencylist = new ArrayList<GraphNode>();
    	for(GraphNode tmp : this.adjacencylist) {
    		tmpAdjacencylist.add(new GraphNode(tmp.getName())); 
    	}
    	GraphList reverse = new GraphList(tmpAdjacencylist);
    	for(GraphNode tmp : this.adjacencylist) {
    		for(int value : tmp.getConnections()) {
    			reverse.getNodeWithGivenName(value).addEdgeEndVertex(tmp.getName());
    		}
    	}	
    	return reverse;
    }
    
    /**
     * Strongly connected components algorithm
     */
    public void StronglyConnectedComponents() {
    	// find the reverse graph
    	GraphList reverse = this.reverseGraph();
    	// run dfs in reverse graph
    	reverse.dfs();
    	// we set the post number of each vertex according to the post number of the same vertex in the reverse graph
    	for(GraphNode node : reverse.getAdjacencylist()) {
    		GraphNode tmp = this.getNodeWithGivenName(node.getName());
    		tmp.setPost(node.getPost());
    	}
    	// sorting GraphNodes in descending numerical order according to their posts numbers
    	Collections.sort(this.adjacencylist);
    	this.dfs2();
    }
}