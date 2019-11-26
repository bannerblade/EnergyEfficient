package SFC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import Main.NodeDist;
import graph.Graph;
import graph.Link;
import graph.Node;


public class ComputePath{
	final static int kpaths=5;
	public static void AllState(Graph topo1, Set<Sfc> flows,ArrayList<ArrayList<Set<Integer>>> AllStateLink,
			ArrayList<ArrayList<List<Integer>>> AllStateNode){
		
	Map<Integer, Node> topo=topo1.getTopo();
	Iterator<Sfc> itor = flows.iterator();
			
	while(itor.hasNext()){
		Sfc flow = itor.next();
		int src = flow.getsrc();
		int dst = flow.getdst();
		
		ArrayList<Set<Integer>> allPathLink=new ArrayList<Set<Integer>>();
		ArrayList<List<Integer>> allPathNode=new ArrayList<List<Integer>>();
		Set<Integer> linkDelete=new HashSet<Integer>();
		
		StretchPath(topo, flow, src, dst, linkDelete, allPathLink,allPathNode);

		AllStateLink.add(allPathLink);
		AllStateNode.add(allPathNode);
	}
  }
	
	private static void StretchPath(Map<Integer, Node> topo,Sfc flow, int start, int end,
			Set<Integer>linkseen,ArrayList<Set<Integer>> allworkpath,ArrayList<List<Integer>> allpathNode) {
		List<Integer> path=KPATH(topo, flow, start, end, linkseen);
		
		if(path!=null){
			if(path.size()>=flow.getSequence().size()){
				Set<Integer> work=new HashSet<Integer>();
				Iterator<Integer> p=path.iterator();
				int temp=p.next();
				while(p.hasNext()){	
					int nextNode=p.next();
					for(Link l: topo.get(temp).getlinks()){
						if(l.getpeer(temp)==nextNode)
						{
							work.add(l.getid());
							temp=nextNode;
						}	
					}
				}
			
				allworkpath.add(work);
				allpathNode.add(path);
			}				
				for(int link:path){
					linkseen.add(link);
					stretch(topo, flow, start, end, linkseen, allworkpath,allpathNode);
					if(allworkpath.size()==kpaths) return;
					linkseen.remove(link);
				}

		}
		return;
	}

	
	private static void stretch(Map<Integer, Node> topo,Sfc flow, int start, int end,
			Set<Integer>linkseen,ArrayList<Set<Integer>> allworkpath,ArrayList<List<Integer>> allpathNode) {
		List<Integer> path=KPATH(topo, flow, start, end, linkseen);
		if(path!=null){	
			if(path.size()>=flow.getSequence().size()){
				Set<Integer> work=new HashSet<Integer>();
				Iterator<Integer> p=path.iterator();
				int temp=p.next();
				while(p.hasNext()){	
					int nextNode=p.next();
					for(Link l: topo.get(temp).getlinks()){
						if(l.getpeer(temp)==nextNode)
						{
							work.add(l.getid());
							temp=nextNode;
						}	
					}
				}
				if(allworkpath.contains(work)) return;
				if(work.size()>5) return;
				allworkpath.add(work);
				allpathNode.add(path);
			}
			
			if(allworkpath.size()==kpaths) return;
			for(int link:path){
				linkseen.add(link);
				stretch(topo, flow, start, end, linkseen, allworkpath,allpathNode);
				if(allworkpath.size()==kpaths) return;
				linkseen.remove(link);
			}
		}

		return;
	}
	
	private static List<Integer> KPATH(Map<Integer, Node> topo,Sfc flow, int start, int end,Set<Integer>linkseen2) {
		// TODO Auto-generated method stub
		Set<Integer> seen = new HashSet<>();
		Set<Integer> linkseen = new HashSet<>();
		
        Integer toposize = topo.size();
        HashMap<Integer,Link> nexthoplinks = new HashMap<>();
        HashMap<Integer, Integer> cost = new HashMap<Integer, Integer>(toposize * 2, 0.5f);
        HashMap<Integer, Queue<Node>> PathSet = new HashMap<Integer, Queue<Node>>();
        
        PriorityQueue<NodeDist> nodeq = new PriorityQueue<NodeDist>(128);
        Queue<Node> StartNode=new LinkedList<Node>();
        StartNode.add(topo.get(start));
        PathSet.put(start, StartNode);
             
        nodeq.add(new NodeDist(start, 0));
        cost.put(start, 0);
  
        while (nodeq.peek() != null) {
            NodeDist n = nodeq.poll();
            Integer node = n.getNode();
            Integer cdist = n.getDist();
          
            if ( seen.contains(node) ) continue;
            
            seen.add(node);
           if (node == end) { 
        	   Queue<Node> FinalPath = PathSet.get(end);
               List<Integer> result=new LinkedList<Integer>();
               Iterator<Node> itor=FinalPath.iterator();
               while(itor.hasNext()){
           		  result.add(itor.next().getid());
               }
               return result;
            }
             
            Set<Link> links = topo.get(node).getlinks();
            Iterator<Link> iter = links.iterator();
            
            Link tempLink = null;
            int dstID = -1;
            
            while (iter.hasNext()) {
                Link link = iter.next();
                if(linkseen2.contains(link.getid())) continue;
                      
                Queue<Node> tempPath=new LinkedList<Node>();
                tempPath.addAll(PathSet.get(node));
                
                int dst = link.getpeer(node);

                Node dstnode = topo.get(dst);
                if (dstnode != null) {
                    if (seen.contains(dst)) continue;
                   
                    Integer ndist = cdist + link.getcost();

                    Integer dstcost = cost.get(dst);

                    if (dstcost == null) {
                        cost.put(dst, ndist);
                
                        nexthoplinks.put(dst, link);
                        NodeDist ndTemp = new NodeDist(dst, ndist);
                        // add the current object to the queue.
                        nodeq.add(ndTemp);
                        tempLink = link;
                        dstID = dst;
                        
                        tempPath.add(dstnode);
                        PathSet.put(dst, tempPath);

                    }  
                    if (ndist < cost.get(dst)) {
                    	
                    	nodeq.remove(new NodeDist(dst,dstcost));
                        cost.put(dst, ndist);
                        nexthoplinks.put(dst, link);
                        NodeDist ndTemp = new NodeDist(dst, ndist);
                        nodeq.add(ndTemp);                      
                        tempLink = link;
                        dstID = dst;
                        
                        tempPath.add(dstnode);
                        PathSet.remove(dst);
                        PathSet.put(dst, tempPath);
                   
                    }
                }
            }
            
            if(dstID != -1){
            	linkseen.add(tempLink.getid());
            	//seen.add(dstID);
            }
        }
        return null;
		
	}
}