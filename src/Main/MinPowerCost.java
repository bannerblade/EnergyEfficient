package Main;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import SFC.Sfc;
import graph.Graph;

public class MinPowerCost {
	public static int NowValue=0;
	private static final int beta=1;
	private static final int tao=1;
	public static void minPowerCost(Graph topo1,Set<Sfc> Flows,ArrayList<ArrayList<Set<Integer>>> AllStateLink,
			ArrayList<ArrayList<List<Integer>>> AllStateNode){

		//????????????
		int scheme[]=new int[Flows.size()];	
		while(NowValue==0){	
			Iterator<Sfc> itor2 = Flows.iterator();
			while(itor2.hasNext()){
				Sfc flow=itor2.next();
				Random r=new Random();
				int i=r.nextInt(AllStateLink.get(flow.getid()).size());
				scheme[flow.getid()]=i;
			}
			
			NowValue=TargetValue.PowerCompute(topo1,Flows,scheme,AllStateLink,AllStateNode);
			
		}
		
		for(int i=0;i<50;i++){
			//??50???????
			NextValue(topo1, Flows, scheme,AllStateLink, AllStateNode);
		}
	}
	
	public static void NextValue(Graph topo1,Set<Sfc> flows,int scheme[],ArrayList<ArrayList<Set<Integer>>> AllStateLink,
			ArrayList<ArrayList<List<Integer>>> AllStateNode){
		List<Integer> promotion=new LinkedList<Integer>();
		List<int[]> tempScheme=new LinkedList<>();
		List<Integer> coreConsumption=new LinkedList<>();
		for(Sfc flow:flows){
			int NextValue=0;
			int[] temp=scheme.clone();
			while(NextValue==0){
				Random r= new Random();
				int i=r.nextInt(AllStateLink.get(flow.getid()).size());
				temp[flow.getid()]=i;
				NextValue=TargetValue.PowerCompute(topo1,flows,temp,AllStateLink,AllStateNode);
				int tempcore=TargetValue.getCores();
				if(NextValue!=0)
					coreConsumption.add(tempcore);
			}

			promotion.add(NextValue);
			tempScheme.add(temp);
		}
		
		int min=Collections.min(promotion);
		int position=promotion.indexOf(min);
		double lamda=10*Math.pow(Math.E, 0.5*beta*(NowValue-min)-tao);
		
		scheme=tempScheme.get(position).clone();
		int cores=coreConsumption.get(position);
		Double Duration=-(1 / lamda) * Math.log(Math.random());//???????????????????
		
		try {
			WriteFile(NowValue,Duration,cores);
			System.out.println("The state "+NowValue+" will stay for "+Duration+" and costs "+cores+" CPU cores");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NowValue=min;
	}
	
	public static void WriteFile(int NowValue,double Duration,int cores) throws IOException{
		FileOutputStream fs = new FileOutputStream("E:\\JAVA_workspace\\EnergyEfficient\\data.txt",true);
		PrintStream p = new PrintStream(fs);
		p.println(NowValue+"      "+Duration+"      "+cores);
		p.close();
	}
}
