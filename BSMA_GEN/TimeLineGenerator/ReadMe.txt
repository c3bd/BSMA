========================================================================
TimeLineGenerator 
========================================================================

TimeLineGenerator is a single node generator version of BSMA_Gen. 

/////////////////////////////////////////////////////////////////////////////

Parameters:
	startTime: the start time of generated timeline
	endTime: the end time of generated timeline
	userNum: the number of user
	outputPath: the path of generated timeline
	isIndex: whether use disk index (BerkeleyDB)
	btreePath: the path of disk index
	cacheSize: the cache size of disk index
	windowSize: the time length of caching the timeline in windows size

File Parameters:
	hour: the hour justment factors
	day: the day justment factors
	network_{userNum}: the followship network
	retweetIntervalCPD_hour: the cumulative probability distribution of retweet interval 
	userProportyCPD: the cumulative join probability distribution of user post rate and retweet rate


File Parameters stored in "TimelineGenerator/data".

