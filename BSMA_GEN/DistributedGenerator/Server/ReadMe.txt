========================================================================
Server(Distributed version) 
========================================================================

The server can generate final timeline data.
The distributionGen is a folder containing file parameters and output file in server.
 
/////////////////////////////////////////////////////////////////////////////

Parameters:
	nodeNum: the number of clients
	userNum: the number of user
	startTime: the start time of generated timeline
	endTime: the end time of generated timeline
	serverPath: the path of the folder of distributionGen in server
	clientPath: the path of the folder of distributionGen in client

File Parameters:
	network{userNum}: the followship network corresponding to userNum, which can be generated using the NetworkGenerator in the singal node version of TimeLineGenerator.
	cluster: the result of partition followshio network,we partition the followship network based on "Fast unfolding of communities in large networks", the partition source code can be download.
	userProportyCPD: the cumulative join probability distribution of user post rate and retweet rate

	network{userNum} and cluster stored in "distributionGen/networkPartition/{userNum}/".
	userProportyCPD stored in "distributionGen/data/".
	

The timeline output keep in "distributionGen/out/", named as "data".

