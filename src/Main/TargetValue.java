package Main;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

import SFC.Sfc;
import graph.Graph;
import graph.Link;
import graph.Node;

public class TargetValue {
	private static final double LinkPeak = 300;
	private static final double LinkOn = 100;
	private static final double ServerPeak = 330;
	private static final double ServerOn = 170;
		
		private static double SingleCore=ServerPeak/16;
		private static int tempcore=0;

		public static int PowerCompute(Graph topo1, Set<Sfc> flows, int[] scheme, 
				ArrayList<ArrayList<Set<Integer>>> allStateLink, ArrayList<ArrayList<List<Integer>>> allStateNode){
			double LinkPower=0;
			double ServerPower=0;
			int totalcores=0;
		
			
			int LinkLoad[]=new int[topo1.GetAllLink().size()];
			int ServerLoad[][]=new int[topo1.GetAllNode().size()][3];
			
			for(Sfc flow:flows){
				int id=flow.getid();
				int sequence[]=new int[flow.getSequence().size()];
				for(int i=0;i<flow.getSequence().size();i++){
					sequence[i]=flow.getSequence().get(i);
				}
				
				for(int j:allStateLink.get(id).get(scheme[id])){
					LinkLoad[j]+=flow.getbandwidth();
				}
				
				List<Integer> Nodes=allStateNode.get(id).get(scheme[id]);
				Random r=new Random();
				PriorityQueue<Integer> q=new PriorityQueue<Integer>();
				while(q.size()<sequence.length){
					int randomNum=r.nextInt(Nodes.size());
					if(q.contains(randomNum)) continue;
					q.add(randomNum);
				}
				
				for(int k=0;k<sequence.length;k++){
					ServerLoad[Nodes.get(q.poll())][sequence[k]]+=flow.getbandwidth();
				}				
			}
			
			//???????server??????????CPU???????
			for(Node n:topo1.getTopo().values()){
				int CoreNum=0;
				for(int m=0;m<3;m++){
					int Num=(int)Math.ceil((double)ServerLoad[n.getid()][m]/(double)n.getServiceCapacity(m));
					CoreNum+=Num*n.getCost(m);
					ServerPower+=Num*SingleCore*(double)ServerLoad[n.getid()][m]/(double)n.getServiceCapacity(m);
				}
				
				if(CoreNum>n.CoreCapacity) return 0;
				else if(CoreNum==0) continue;
				else ServerPower+=ServerOn;
				totalcores+=CoreNum;
			}
			
			for(Link l:topo1.GetAllLink().values()){
				if(l.linkcapacity<LinkLoad[l.getid()]) 
					return 0;
				else{
					//????Link????
					if(LinkLoad[l.getid()]!=0) 
						LinkPower+=(double)LinkLoad[l.getid()]/(double)l.linkcapacity*LinkPeak+LinkOn;
				}
			}
			
			tempcore=totalcores;
			return (int) (LinkPower+ServerPower);
		}
		
		public static int getCores(){
			int cores= tempcore;
			return cores;
		}
}
