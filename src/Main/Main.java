package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import SFC.Sfc;
import SFC.ComputePath;
import SFC.Pair;
import graph.Graph;

public class Main {
	public static void main(String[] args) {
	Graph topo1 = new Graph();
	Graph topo2 = new Graph();
	
	int number=40;

		
	//???????????
	String filePath1 = "E:\\JAVA_workspace\\EnergyEfficient\\NSFNET.csv";
	FileUtil.read(filePath1, null, topo1);
	
	String filePath2 = "E:\\JAVA_workspace\\EnergyEfficient\\USANET.csv";
	FileUtil.read(filePath2, null, topo2);
	
	Set<Sfc> sfc=null;
	/*sfc=CreateSFC(number,topo1);
	
	try {
		WriteFile(sfc);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}*/
	

	try {
			sfc = readFile("E:\\JAVA_workspace\\EnergyEfficient\\SFC\\NSFNET\\20.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	ArrayList<ArrayList<Set<Integer>>> AllStateLink=new ArrayList<ArrayList<Set<Integer>>>();
	ArrayList<ArrayList<List<Integer>>> AllStateNode=new ArrayList<ArrayList<List<Integer>>>();
	
	ComputePath.AllState(topo1, sfc, AllStateLink, AllStateNode);
	
	MinPowerCost.minPowerCost(topo1, sfc, AllStateLink, AllStateNode);
	
	}
	

	public static Set<Sfc> CreateSFC(int num,Graph topo){	
		int size = topo.GetAllNode().size();
		Random r = new Random();
		Set<Sfc> SFC = new HashSet<Sfc>();
		Set<Pair<Integer,Integer>> Created = new HashSet<>();
		
		while(Created.size()<num){
			int src = r.nextInt(size-1)+1;
			int dst = r.nextInt(size-1)+1;
			if(src==dst) continue;
			Pair<Integer,Integer> sfc = new Pair<Integer, Integer>(src,dst);
			if(Created.add(sfc)){
				int Bandwidth = r.nextInt(40)+10;
				//int n=r.nextInt(3)+1;
				int n=3;
				int Sequence[] =new int[n];
				
				ArrayList<Integer> sequence=new ArrayList<Integer>();
				Set<Integer> createdNumber=new HashSet<>();
				for(int i=0;i<n;i++){					
					Sequence[i]=r.nextInt(3);
					while(createdNumber.contains(Sequence[i])){
						Sequence[i]=r.nextInt(3);
					}
					sequence.add(Sequence[i]);
					createdNumber.add(Sequence[i]);
				}		 
 			
				Sfc temp = new Sfc(Created.size()-1, sfc, Bandwidth, sequence);
				SFC.add(temp);
			}
		}
		return SFC;
	}
	
	public static void WriteFile(Set<Sfc> SFC) throws IOException{
		FileOutputStream fs = new FileOutputStream(new File("E:\\JAVA_workspace\\NFV_1\\SFC\\USNET\\3.txt"));
		PrintStream p = new PrintStream(fs);
		Iterator<Sfc> flow=SFC.iterator();
		while(flow.hasNext()){
			Sfc sfc=flow.next();
			int id=sfc.getid();
			int src=sfc.getsrc();
			int dst=sfc.getdst();
			int bandwidth=sfc.getbandwidth();
			ArrayList<Integer> ServiceSequence=sfc.getSequence();
			String str= new String();
			for(int i :ServiceSequence){
				str+=i+" ";
			}
			p.println(id+" "+src+" "+dst+" "+bandwidth+" "+str);
		}
		p.close();
	}
	
	public static Set<Sfc> readFile(String Path) throws IOException{
		int src[]=new int[100];
		int dst[]=new int[100];
		int Bandwidth[]=new int[100];
	
		ArrayList<ArrayList<Integer>> Sequence= new ArrayList<ArrayList<Integer>>();
		Set<Sfc> sfc=new HashSet<Sfc>();
	
		BufferedReader br = new BufferedReader(new FileReader(Path));

		String line = br.readLine();
		int i=0;//??1???,?id
		while(line!=null){
		String [] numbers = line.split(" ");//????????????
		src[i]=Integer.valueOf(numbers[1]);
		dst[i]=Integer.valueOf(numbers[2]);
		Bandwidth[i]=Integer.valueOf(numbers[3]);
		
		ArrayList<Integer> SequenceSub=new ArrayList<Integer>();
		for(int m=0;m<=(numbers.length-5);m++){
			SequenceSub.add(Integer.valueOf(numbers[m+4]));
		}
		Sequence.add(SequenceSub);
		i++;
		line = br.readLine();
		}

		for(int j=0;j<i;j++){
			
		/*	System.out.print(src[j]+" ");
			System.out.print(dst[j]+" ");
			System.out.print(Bandwidth[j]+" ");*/

			ArrayList<Integer> sequence=new ArrayList<Integer>();
			for(int k=0;k<Sequence.get(j).size();k++){
				//System.out.print(Sequence.get(j).get(k)+" ");
				sequence.add(Sequence.get(j).get(k));
			}
			Pair<Integer,Integer> pair=new Pair<Integer,Integer>(src[j], dst[j]);
			Sfc flow=new Sfc(j,pair, Bandwidth[j],sequence);
			sfc.add(flow);
			//System.out.println();	
		}
		br.close();
		return sfc;
		
	}
 }
