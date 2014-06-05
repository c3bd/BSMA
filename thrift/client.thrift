/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * The first thing to know about are types. The available types in Thrift are:
 *
 *  bool        Boolean, one byte
 *  byte        Signed byte
 *  i16         Signed 16-bit integer
 *  i32         Signed 32-bit integer
 *  i64         Signed 64-bit integer
 *  double      64-bit floating point value
 *  string      String
 *  binary      Blob (byte array)
 *  map<t1,t2>  Map from one type to another
 *  list<t1>    Ordered list of one type
 *  set<t1>     Set of unique elements of one type
 *
 * Did you also notice that Thrift supports C style comments?
 */
include "shared.thrift"
namespace java thrift.rpc.client


/**
* configurations that may change during the test
*
*/
struct RuntimeConfig {
	1: i32 opCount
	2: i32 targetThroughput
}

/**
* the distribution of generated arguments
*/
struct TestConfig {
	1:i32 jobID
	2: list<double> fractions //fraction of each query
	3: map<string, string> properties
	4: RuntimeConfig config
}

service ClientService {
	void setup(1:TestConfig config),
	void startTest(),
	void stopTest(),
	void setThroughput(1: i32 throughput),
	
	void exit()
}
