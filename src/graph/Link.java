package graph;

import java.util.*;

import SFC.Sfc;


public class Link {
	private Integer id;
	private Node src;
	private Node dst;
	public int cost;
	public int linkcapacity = 500;
	private int loadonlink = 0;
	
	private Set<Sfc> flowonlink = new HashSet<>();
	
	public Link (int id,Node src , Node dst){
		this.id = id;
		this.src = src;
		this.dst = dst;
		this.cost = 1;
	}
	
	public int getid(){
		return this.id;
	}
	
	public int getload(){
		return this.loadonlink;
	}
		
	public int getpeer(int nodeid){
		if(this.src.getid() == nodeid){
			return this.dst.getid();
		}
			
		if(this.dst.getid() == nodeid){
			return this.src.getid();
		}
		return -1;	
	}
	
	public int getsrc(){
		return this.src.getid();
	}
	public int getdst(){
		return this.dst.getid();
	}
	public Node getsrcpoint(){	
			return this.src;
		}
	public Node getdstpoint(){	
		return this.dst;
	}
	
	public void addflow(Sfc SFC){
		this.loadonlink += SFC.getbandwidth();
	}
	
	public void removeflow(Sfc SFC){	
			this.loadonlink -= SFC.getbandwidth();
	}
	
	public Set<Sfc> getflows(){
		return this.flowonlink;
	}
	
	public int getflownum(){
		return this.flowonlink.size();
	}
	public int getcost(){
		return this.cost;
	}
	
	public void breakdown(){
		this.src.removelink(this);
		this.dst.removelink(this);
		flowonlink.clear();
		loadonlink = 0;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Link other = (Link) obj;
        if (this.id!=other.id)
            return false;
        return true;
    }

}
