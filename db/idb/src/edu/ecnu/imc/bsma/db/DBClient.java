/**                                                                                                                                                                                
 * Copyright (c) 2012 Institute of Massive Computing, East China Normal University.
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

package edu.ecnu.imc.bsma.db;

import edu.ecnu.imc.bsma.DB;
import edu.ecnu.imc.bsma.DBException;

/**
 * DB client for BSMA framework
 */
public class DBClient extends DB
{

	boolean _debug = false;

	/**
	 * Initialize any state for this DB. Called once per DB instance; there is
	 * one DB instance per client thread.
	 */
	public void init() throws DBException
	{
		_debug = Boolean.parseBoolean(getProperties().getProperty("debug", "false"));
		if (_debug)
		{
			System.out.println("Debuging...");
		}
		// TODO Initialize any state for this DB. Called once per DB instance; there is one DB instance per client thread
	}

	/**
	 * Cleanup any state for this DB. Called once per DB instance; there is one
	 * DB instance per client thread.
	 */
	public void cleanup() throws DBException
	{
		// TODO Cleanup any state for this DB. Called once per DB instance; there is one DB instance per client thread.
	}

	
	public String BSMAQuery1(String userID, int returncount)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery1:");
			sb.append("userID = ");
			sb.append(userID);
		}
		// TODO Implement BSMAQuery1. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery2(String userID, int returncount)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery2:");
			sb.append("userID = ");
			sb.append(userID);
		}
		// TODO Implement BSMAQuery2. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery3(String userID, int returncount)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery3:");
			sb.append("userID = ");
			sb.append(userID);
		}
		// TODO Implement BSMAQuery3. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery4(String userID1, String userID2)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery4:");
			sb.append("userID1 = ");
			sb.append(userID1);
			sb.append(" userID2 = ");
			sb.append(userID2);
		}
		// TODO Implement BSMAQuery4. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery5(String userID1, String userID2)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery5:");
			sb.append("userID1 = ");
			sb.append(userID1);
			sb.append(" userID2 = ");
			sb.append(userID2);
		}

		// TODO Implement BSMAQuery5. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery6(String userID, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery6:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery6. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery7(int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery7:");
			sb.append("datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery7. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery8(int returncount, String userID)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery8:");
			sb.append("userID = ");
			sb.append(userID);
		}
		// TODO Implement BSMAQuery8. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery9(String userID, String tag, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery9:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append(" tag = ");
			sb.append(tag);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery9. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery10(int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery10:");
			sb.append("datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery10. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery11(String userID, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery11:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery11. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery12(String userID, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery12:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery12. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery13(String userID, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery13:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery13. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery14(int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery14:");
			sb.append("datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery14. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery15(String tag, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery15:");
			sb.append("tag = ");
			sb.append(tag);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery15. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery16(String userID, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery16:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery16. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery17(String userID, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery17:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery17. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery18(String userID, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery18:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append(" datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery18. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

	
	public String BSMAQuery19(String userID, int returncount, String datetime, String timespan)
	{
		String result = new String();
		StringBuilder sb = new StringBuilder();
		if (_debug)
		{
			sb.append("Debug BSMAQuery19:");
			sb.append("userID = ");
			sb.append(userID);
			sb.append("datetime = ");
			sb.append(datetime);
		}
		// TODO Implement BSMAQuery19. Refer to the specification for the
		// semantics of this query and the required pattern of result string

		result = sb.toString();
		sb.delete(0, sb.length());
		return result;
	}

}
