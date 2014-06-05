package object;

import java.util.Comparator;

public class ComparatorByTimeReverseOrder implements Comparator<Tweet>{

	@Override
	public int compare(Tweet t1, Tweet t2) {
		// TODO Auto-generated method stub
		long comp = t1.getTime()-t2.getTime();
		if(comp == 0){
			return (int) (t1.getMid().compareTo(t2.getMid()));
		} else {
			return comp < 0? 1 : -1;
		}
	}
	
}