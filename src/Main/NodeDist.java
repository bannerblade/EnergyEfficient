package Main;



public class NodeDist implements Comparable<NodeDist> {
	private final Integer node;
	private final Integer dist;
	
	public NodeDist(int node, int ndist){
		this.node = node;
		this.dist = ndist;
	}
	
	public int getNode(){
		return this.node;
	}
	
	public Integer getDist(){
		return this.dist;
	}
	
	@Override
    public int compareTo(NodeDist o) {
        if (o.dist == this.dist) {
            return (int)(this.node - o.node);
        }
        return (int) (this.dist - o.dist);
    }
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NodeDist other = (NodeDist) obj;
        if (this.node != other.node)
            return false;
        return true;
	}

}
