========================================================================
CopyingModelGenerator 
========================================================================

CopyingModelGenerator based on Copying model by Kleinberg et al. which generate followship network. 

/////////////////////////////////////////////////////////////////////////////

Parameters:
	rIN: the exponent of power-law in-degree
	num: the number of user
	outputPath: the path of generated followship network

File Parameters:
	outDegCPD: the cumulative probability out-degree distribution of realistic data 

File Parameters stored in "TimelineGenerator/data".

/////////////////////////////////////////////////////////////////////////////

Usage:

Generate followship graph on 1000 users and power-law in-degree exponent 1.4667, and stored in the path "E://workspace/TimeLineGenerator/data/":

1.4667 1000 E://workspace/TimeLineGenerator/data/