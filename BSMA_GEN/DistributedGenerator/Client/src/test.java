import java.io.IOException;


public class test{
	public static void main(String args[]) throws IOException {
		Util.PartitionNetwork("/home/yuchengcheng/data",
				"/home/yuchengcheng/distributionGen/resource",
				"network10000", 10000, 3);
	}
}