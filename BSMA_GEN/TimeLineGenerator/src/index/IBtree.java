package index;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import object.Tweet;


public interface IBtree {
	public void put(Long key, TweetInfo tweet) throws Exception;
	public void remove(Long key, TweetInfo tweet) throws Exception;
	public Tweet getMid(Long key, Tweet m);
	public void close() throws IOException;
	public void flush() throws IOException;
}