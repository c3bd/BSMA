package NetworkGenerator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class CopyingModelGenerator{
	int nodeNum;
	double beta;
	Map<Integer,List<Integer>> network = new TreeMap <Integer,List<Integer>>();
	TreeMap<Integer,Integer> outDegCPD = new TreeMap<Integer,Integer>();
	TreeMap<Integer,Integer> outDeg = new TreeMap<Integer,Integer>();
	BufferedWriter outfile ;
	public CopyingModelGenerator(int nodeNum,double beta){
		this.nodeNum = nodeNum;
		this.beta = beta;
		try{
        	BufferedReader infile = new BufferedReader(new FileReader("/home/yuchengcheng/workspace/TimeLineGenerator/data/outDegCPD"));
        	String line = null; 
        	while((line = infile.readLine())!=null ) 
            {
        		String[] linesItem = line.split("\t");
        		outDegCPD.put(Integer.valueOf(linesItem[0]),Integer.valueOf(linesItem[1]));
        		outDeg.put(Integer.valueOf(linesItem[1]),Integer.valueOf(linesItem[0]));
            }
        	infile.close();
		}catch(Exception e){System.out.println(e.toString());}
	}
	
	public void generator(String output){
		try {
			outfile =  new BufferedWriter(new FileWriter( output));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random r =  new Random();
		addEdge(0,0);
		for(int n=1;n<nodeNum;n++){
			
			int outDegnum = outDegCPD.ceilingEntry(r.nextInt(this.outDeg.floorEntry(n).getValue())).getValue();
			if(Math.random()<beta){
				Random rr = new Random();
				int rnd = rr.nextInt(n);
				List<Integer> ls = new ArrayList<Integer>();
				while(ls.size() < outDegnum){
					if(!ls.contains(rnd)){
						addEdge(n,rnd);
						ls.add(rnd);
					}
					rnd = rr.nextInt(n);
				}
			}else{
				int rnd = new Random().nextInt(n);
				List<Integer> ls = network.get(rnd);
				Set<Integer> index = new HashSet<Integer>();

				if(ls.size() >= outDegnum){
					Random rr =  new Random();
					while(index.size() < outDegnum){
						int rIndex = rr.nextInt(ls.size());
						if(!index.contains(rIndex)){
							index.add(rIndex);
							addEdge(n,ls.get(rIndex));
						}
					}
				}else{
					for(int i=0; i<ls.size();i++){
						addEdge(n,ls.get(i));
					}
					int irnd = new Random().nextInt(n);
					int num=0;
					while(  num < (outDegnum -ls.size())){
						if(!ls.contains(irnd)){
							num++;
							addEdge(n,irnd);
							ls.add(irnd);
						}
						irnd = new Random().nextInt(n);
					}
					
				}
			}
		}
		
		try {
			outfile.flush();
			outfile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	public void addEdge(int id1,int id2){
			if(network.containsKey(id1)){
				network.get(id1).add(id2);
			}else{
				List<Integer> ls = new ArrayList<Integer>();
				ls.add(id2);
				network.put(id1, ls);
			}
			System.out.println(id1+"\t"+id2);
			try {
				outfile.write(id1+"\t"+id2);
				outfile.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	
	
	public int getNodeNum() {
		return nodeNum;
	}
	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}
	public double getBeta() {
		return beta;
	}
	public void setBeta(double beta) {
		this.beta = beta;
	}
	
	 public static void main(String[] args) {
		 double rIN = 1.4667;
		 double p = (rIN-1)/rIN;
		 int num = 100;
		 CopyingModelGenerator cmg = new CopyingModelGenerator(num,p);
		 cmg.generator("/home/yuchengcheng/workspace/TimeLineGenerator/data/network"+num);
	 }
}