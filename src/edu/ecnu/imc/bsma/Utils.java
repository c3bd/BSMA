/**                                                                                                                                                                                
 * Copyright (c) 2012 Institute of Massive Computing, East China Normal University.
 * NOTICE: This file is based on the corresponding one in YCSB [Copyright (c) 2010 Yahoo! Inc.]                                                                                                                       
 *                                                                                                                                                                                 
 * Licensed under the Apache License, Version 2.0 (the "License"); you                                                                                                             
 * may not use this file except in compliance with the License. You                                                                                                                
 * may obtain a copy of the License at                                                                                                                                             
 *                                                                                                                                                                                 
 * http://www.apache.org/licenses/LICENSE-2.0                                                                                                                                      
 *                                                                                                                                                                                 
 * Unless required by applicable law or agreed to in writing, software                                                                                                             
 * distributed under the License is distributed on an "AS IS" BASIS,                                                                                                               
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or                                                                                                                 
 * implied. See the License for the specific language governing                                                                                                                    
 * permissions and limitations under the License. See accompanying                                                                                                                 
 * LICENSE file.                                                                                                                                                                   
 */

package edu.ecnu.imc.bsma;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility functions.
 * 
 * @author wjx
 */
public class Utils
{

	/**
	 * Return file path for a given file name. In this case, files are put in root dir of project.
	 * @param fn filename
	 * @return file path
	 */
	public static String getFilePath(String fn)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(System.getProperty("user.dir"));
		sb.append(System.getProperty("file.separator"));
		sb.append(fn);
		String fpath = sb.toString();
		sb.delete(0, sb.length());
		return fpath;
	}

	/**
	 * Return a concurrent hashmap for a file to deal with concurrent calls
	 * @param filepath  the path of file to be processed
	 * @return concurrent hashmap
	 */
	@SuppressWarnings({ "rawtypes" })
	public static ConcurrentHashMap getConcurrentHashMap(String filepath)
	{
		ConcurrentHashMap<Integer, String> chm = new ConcurrentHashMap<Integer, String>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String line = "";
			int i = 0;
			while ((line = br.readLine()) != null)
			{
				chm.put(i++, line);
			}
			br.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return chm;
	}
	
	/**
	 * Return a concurrent hashmap for a file to deal with concurrent calls
	 * @param filepath  the path of file to be processed
	 * @return concurrent hashmap
	 */
	@SuppressWarnings({ "rawtypes" })
	public static ConcurrentHashMap getConcurrentHashMap(String filepath, String separator, String subseparator, int subcount)
	{
		ConcurrentHashMap<String, List<String>> chm = new ConcurrentHashMap<String, List<String>>();
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filepath));
			String line = "";
			String prefix = "";
			String suffix = "";
			String tmp = "";
			int i,j;
			while ((line = br.readLine()) != null)
			{
				List<String> list = new ArrayList<String>(subcount);
				i = line.indexOf(separator);
				prefix = line.substring(0, i);
				suffix = line.substring(i+1);
				
				while(suffix.contains(subseparator))
				{
					j = suffix.indexOf(subseparator);
					tmp = suffix.substring(0,j);
					suffix = suffix.substring(j+1);
					list.add(tmp);
				}
				list.add(suffix);
				chm.put(prefix, list);
			}
			br.close();

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return chm;
	}
	
}
