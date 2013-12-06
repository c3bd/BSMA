package index;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class IntegerBinding extends TupleBinding{

	@Override
	public Object entryToObject(TupleInput ti) {
		// TODO Auto-generated method stub
		Integer result = ti.readInt();
		
		
		return result;
	}

	@Override
	public void objectToEntry(Object object, TupleOutput to) {
		// TODO Auto-generated method stub
		Integer ti = (Integer)object;

		to.writeInt(ti);
		
	}
	
}