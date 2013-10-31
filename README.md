Benchmark for Social Media Analysis (BSMA)
=========
  Social Media Data Benchmark System
  --------------

  BSMA, benchmark for social media analysis, was used as the benchmark in WISE 2012 Challenge (http://www.wise2012.cs.ucy.ac.cy/challenge.html). 
  
  There are many systems been deployed or being developed for social media analysis. It is difficult to decide which system is better than others, partially because the features differ between systems, and partially because there is not an easy way to compare the performance of one system versus another. The goal of the BSMA project is to develop a framework and common set of workloads for evaluating the performance of different systems dealing with social media analysis. 
  
  In the case of WISE 2012 Challenge, the base of workloads is 19 typical queries, semantics of which can be found in the specification of queries. Each contestant is expected to build a data analysis system covering those 19 queries and complete the interface layer in BSMA (BSMA/db/idb/…/DBClient.java). Assume experimental data is prepared , query results and performance (latency vs. throughput) can be obtained via BSMA.
  
  Social Media Data Generator
  ----------
  
  Due to various reasons such as privacy, bandwith, API barrier imposed by social network service providers,et al, accessing sheer volume of social media data in real world is often infeasible to researchers. Hence, we develope a data generator that generates data conforming to properties existed in a real social media data, which we have collected from Sina Weibo. A local generator and a distributed data generator is currently provided under the fold BSMA_GEN.
  
  TimeLineGenerator is a single node generator. In order to generate massive dataset more efficiently, we also develop a distributed generator, that is DistributedGenerator under BSMA_GEN, utilizing the power of commodity machines to conduct some tasks in parallel.  
