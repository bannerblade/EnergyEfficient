package SFC;

import java.util.ArrayList;
import java.util.List;


import SFC.Pair;
import graph.Link;

public class Sfc {
	public Integer FlowId;
	private Pair<Integer,Integer> NodePair;
	private int Bandwidth;
	public int  EachCPUcost=0;
	private List<Link> Path_Link = new ArrayList<>();
	private ArrayList<Integer> sequence;
	
	public Sfc(int id, Pair<Integer,Integer> Pair, int Bandwidth, ArrayList<Integer> sequence){
		// TODO Auto-generated constructor stub
		this.FlowId = id;
		this.NodePair = Pair;
		this.Bandwidth = Bandwidth;
		this.sequence=sequence;
	}
	
	public int getid(){
		return this.FlowId;
	}
	

	public int getsrc(){
		return this.NodePair.first();
	}
	public int getdst(){
		return this.NodePair.second();
	}
	
	public int getbandwidth(){
		return this.Bandwidth;
	}
	
	public ArrayList<Integer> getSequence(){
		return this.sequence;
	}
	
	public void addpathonlink(Link link){
		this.Path_Link.add(link);
	}
	
	public List<Link> getpath(){
		return this.Path_Link;
	}
	public void removepath(){
		this.Path_Link.clear();
	}
	
	@Override
	public int hashCode() {
		return this.FlowId.hashCode();
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
       Sfc other = (Sfc) obj;
        if (this.FlowId!=other.FlowId)
            return false;
        return true;
    }

	
}
