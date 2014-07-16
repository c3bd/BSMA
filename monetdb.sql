 monetdbd create /path/to/mydbfarm
monetdbd start /path/to/mydbfarm
 monetdb create bsma
 monetdb release bsma
 mclient -u monetdb -d bsma
password:<monetdb>
 
//创建mood表
CREATE TABLE mood(mood CHAR(10), moodID TINYINT);
copy into mood from '/home/xiafan/Documents/dataset/bsma/mood.txt' using delimiters '\t','\n';
 
//创建rel_mood_microblog表
CREATE TABLE rel_mood_microblog(mid CHAR(30),moodID TINYINT);
copy into rel_mood_microblog from '/home/xiafan/Documents/dataset/bsma/data/rel_mood_microblog/' using delimiters '\t','\n';

//创建事件
CREATE TABLE Event(eventID INT,tag VARCHAR(30),
 introduction TEXT);
CREATE TABLE Event(eventID INT,tag VARCHAR(30),
  startTime BIGINT, introduction TEXT, locationID INT);
copy into Event from '/home/xiafan/Documents/dataset/bsma/political_t.txt' using delimiters '\t','\n';

//创建rel_event_microblog
CREATE TABLE rel_event_microblog(eventID INT,mid CHAR(30));
copy into rel_event_microblog from '/home/xiafan/Documents/dataset/bsma/data/rel_event_microblog/000000_0' using delimiters '\t','\n';

//创建用户表 
CREATE TABLE bsma.MUser(uid VARCHAR(30), uname VARCHAR(30),
     followersCount INT, friendsCount INT,
     statusesCount INT , favouritesCount INT, verify BOOLEAN,
     vtype INT, city INT, time BIGINT, gender CHAR(1));
copy into bsma.MUser from '/home/xiafan/bsma/user/user.txt' using delimiters '\t','\n';

//社交网络表
CREATE TABLE FriendList(uid VARCHAR(30), followeeID VARCHAR(30));
copy into FriendList from '/home/xiafan/Documents/dataset/bsma/mood.txt' using delimiters '\t','\n';

//微博
CREATE TABLE Microblog(mid VARCHAR(30), uid STRING,time BIGINT, content VARCHAR(1000), client VARCHAR(200),repostCount INT, commentCount INT,rtmid VARCHAR(30));
copy into Microblog from '/home/xiafan/Documents/dataset/bsma/mood.txt' using delimiters '\t','\n';
//转发
CREATE TABLE Retweet(mid VARCHAR(30), RTMID VARCHAR(30));
copy into Retweet from '/home/xiafan/Documents/dataset/bsma/mood.txt' using delimiters '\t','\n';
//mension
create table Mension(mid VARCHAR(30),uid VARCHAR(30));
copy into Mension from '/home/xiafan/Documents/dataset/bsma/mood.txt' using delimiters '\t','\n';
 
 
 
 
 