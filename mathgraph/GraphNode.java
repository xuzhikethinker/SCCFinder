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

/**
 * Class representation of a vertex
 * 
 * @author Voulimeneas Alexios - avoulimeneas@gmail.com
 */
public class GraphNode implements Comparable<GraphNode> {
	
	private int name = 0;
	private int pre = 0;
	private int post = 0;
	// the strongly connected component this vertex belongs to 
	// each strongly connected component's name is a positive Integer
	private int scc = 0;
	private boolean visited = false;
	// contains the names of all the vertices v for which an edge (u,v) exists,and u is this vertex
	private ArrayList<Integer> connections = new ArrayList<Integer>();
	
	/**
	 * Default constructor
	 */
	public GraphNode() {
		
	}
	
	/**
	 * Another constructor
	 * 
	 * @param name
	 * 			the name of this vertex
	 */
	public GraphNode(int name) {
		this.name = name;
	}
	
	public void setName(int name) {
		this.name = name;
	}
	
	public void setPre(int pre) {
		this.pre = pre;
	}
	
	public void setPost(int post) {
		this.post = post;
	}
	
	/**
	 * Sets the strongly connected component this vertex belongs to
	 */
	public void setSCC(int scc) {
		this.scc = scc;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void setConnections(ArrayList<Integer> connections) {
		this.connections = connections;
	}
	
	public void addEdgeEndVertex(int Node) {
		this.connections.add(Node);
	}
	
	/**
	 * Previsits this vertex
	 */
	public void previsit(int pre) {
		this.visited = true;
		this.pre = pre;
	}
	
	/**
	 * Postvisits this vertex
	 */
	public void postvisit(int post) {
		this.post = post;
	}

	public int getName() {
		return this.name;
	}
	
	public int getPre() {
		return this.pre;
	}
	
	public int getPost() {
		return this.post;
	}
	
	public int getSCC() {
		return this.scc;
	}

	public boolean isVisited() {
		return this.visited;
	}
	
	/**
	 * Returns the connections of this vertex
	 */
	public ArrayList<Integer> getConnections() {
		return this.connections;
	}
	
	/**
	 * compareTo method for sorting vertices in discending numerical order according to their posts
	 */
	public int compareTo(GraphNode node) {
		int result = new Integer(this.getPost()).compareTo(node.getPost());
		return -result;
	}
}