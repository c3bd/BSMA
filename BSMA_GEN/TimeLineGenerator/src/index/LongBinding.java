package index;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class LongBinding extends TupleBinding{

	@Override
	public Object entryToObject(TupleInput ti) {
		// TODO Auto-generated method stub
		Long result = ti.readLong();
		
		
		return result;
	}

	@Override
	public void objectToEntry(Object object, TupleOutput to) {
		// TODO Auto-generated method stub
		Long ti = (Long)object;

		to.writeLong(ti);
		
	}
	
}