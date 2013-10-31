package object;

import java.util.Comparator;

public class ComparatorByTimeOrder implements Comparator<Task>{

	@Override
	public int compare(Task m1, Task m2) {
		// TODO Auto-generated method stub
		long comp = m1.getM().getTime()-m2.getM().getTime();
		if(comp == 0){
			return (int) (m1.getM().getMid().compareTo(m2.getM().getMid()));
		} else {
			return comp < 0? -1 : 1;
		}
	}
	
}