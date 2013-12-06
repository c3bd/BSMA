package index;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import object.Tweet;


public interface IBtree {
	
	public void put(Tweet t) ;
	public void remove(Tweet t);
	public Tweet getTweet(Integer uid, int skipNum);
	public void close() throws IOException;
	public void flush() throws IOException;
}