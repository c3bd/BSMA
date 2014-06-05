package object;

import java.util.List;
import java.util.Random;

public class UserInfo{
	double proportyIden;
	Random randomPost;
	Random randomRetweet;
	
	public UserInfo(double proportyIden){
		this.proportyIden = proportyIden;
		this.randomPost = new Random();
		this.randomRetweet = new Random();
	}

	
	public double getProportyIden() {
		return proportyIden;
	}
	public void setProportyIden(double pRate) {
		this.proportyIden = pRate;
	}
	public Random getRandomPost() {
		return randomPost;
	}
	public void setRandomPost(Random randomPost) {
		this.randomPost = randomPost;
	}
	public Random getRandomRetweet() {
		return randomRetweet;
	}
	public void setRandomRetweet(Random randomRetweet) {
		this.randomRetweet = randomRetweet;
	}
}