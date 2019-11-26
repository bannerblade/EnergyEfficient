package graph;

import java.util.HashSet;
import java.util.Set;

import SFC.Sfc;;

public class Node {
	private Integer id;
	public double CoreCapacity = 16;
	Set<Link> link = new HashSet<Link>();
	private int ServiceCapacity[]={100,80,50};
	private int ServiceCost[]={1,1,4};
	
	private Set<Sfc> flowonnode = new HashSet<>();
	
	public Node(int id){
		this.setId(id);
	}

	public boolean iscapable(Integer cost){
		if(cost<=this.CoreCapacity)
			return true;
		else
			return false;
	}	
	
	public void AddLink(Link link){
		this.link.add(link);
	}
	
	public void removelink(Link link){
		this.link.remove(link);
	}
	
	public Set<Link> getlinks(){
		return this.link;
	}
	
	public int getdegree(){
		return this.link.size();
	}
	
	public void addflow(Sfc SFC){
		this.flowonnode.add(SFC);
	}
	
	public int getflownum(){
		return this.flowonnode.size();
	}
	
	public Integer getid(){
		return this.getId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public int getCost(int number) {
		return ServiceCost[number];
	}

	public int getServiceCapacity(int number) {
		return ServiceCapacity[number];
	}

}
