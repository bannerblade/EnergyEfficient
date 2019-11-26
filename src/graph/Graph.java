package graph;

import java.util.*;
import Main.EntityParser;

public class Graph implements EntityParser {
	Map<Integer,Node> Topo = new HashMap<Integer,Node>();
	Map<Integer,Link> LinkOnTopo = new HashMap<>();
	
	public Map<Integer,Node> getTopo(){
		return this.Topo;
	}
	
	public Set<Integer> GetAllNode(){
		return this.Topo.keySet();
	}
	
	public Iterable<Node> GetNodes(){
		return this.Topo.values();
	}
	
	public Map<Integer,Link> GetAllLink(){
		return this.LinkOnTopo;
	}
	
	public void clear(){
		Topo.clear();
		LinkOnTopo.clear();
	}

	@Override
	public void parse(String str) {
		// TODO Auto-generated method stub
		String[] parameters = str.split(",");
		Integer id = Integer.parseInt(parameters[0]);
		Integer start = Integer.parseInt(parameters[1]);
		Integer end = Integer.parseInt(parameters[2]);
		
		Node startnode;
		Node endnode;
		if(!Topo.containsKey(start)){
			startnode = new Node(start);
			Topo.put(start, startnode);
		}
		else{
			startnode = Topo.get(start);
		}
		if(!Topo.containsKey(end)){
			endnode = new Node(end);
			Topo.put(end, endnode);
		}
		else{
			endnode = Topo.get(end);
		}
		
		Link link = new Link(id,startnode,endnode);
		LinkOnTopo.put(id, link);
	
		startnode.AddLink(link);

		endnode.AddLink(link);
			
	}

}
