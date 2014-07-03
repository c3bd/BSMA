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

// Just in case you were wondering... yes. We support simple C comments too.

/**
 * Thrift files can namespace, package, or prefix their output in various
 * target languages.
 */
namespace java rpc

struct Query {
	1: byte qID,
	2: double frac
}

struct SubJob {
	1: i32 subJobID = -1,
	2: i32 opCount,
	3: i32 threadNum
}
struct Job {
  1: i32 jobID = -1,
  2: string name,
  3: byte dbImpl, //implementation of database interface
  4: optional string custDbImpl,
  5: list<Query> queries, //fractions of each query
  6: list<SubJob> subJobs,
  7: optional map<string, string> props
}

/**
 * Structs can also be exceptions, if they are nasty.
 */
exception InvalidJob {
  1: i32 what,
  2: string why
}

service BSMAService {
   Job submit(1:Job job) throws (1:InvalidJob ex),
   void cancelJob(1:i32 jobID),
   void cancelSubJob(1:i32 jobID, 2:i32 subID)
}
