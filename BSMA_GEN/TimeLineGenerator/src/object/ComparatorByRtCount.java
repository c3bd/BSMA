package object;

import java.util.Comparator;

public class ComparatorByRtCount implements Comparator<Tweet>{

	@Override
	public int compare(Tweet m1, Tweet m2) {
		// TODO Auto-generated method stub
		long comp = m1.getRtCount()-m2.getRtCount();
		if(comp == 0){
			return (int) (m1.getMid()-m2.getMid());
		} else {
			return comp < 0? 1 : -1;
		}
	}
	
}