package com.parser.json.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.parser.json.log.Logger;

public class DatabaseConnection {
	public static enum DbEngine{
		MYSQL,
		NONE
	};
	DatabaseDriver mDbDriver = null;
	DbEngine mType = DbEngine.NONE; 
	
	public boolean LoadDatabaseDriver(DbEngine nCaseSql){
		switch(nCaseSql){
			case MYSQL:
				if(mDbDriver == null)
				{
					mDbDriver = MySqlDriver.getInstance();
					mType = DbEngine.MYSQL;
				}
				return mDbDriver.LoadAndConnectEngine();
			
			default:
				Logger.getInstance().Debug("Cannot load driver for [%s].", nCaseSql.toString());
				return false;
		}
	}
	
	public ResultSet ExecuteQueryWithResults(String query) throws SQLException{
		switch(mType){
			case MYSQL:
				return ((MySqlDriver) mDbDriver).runQueryWithResults(query);
			default:
				break;
		}
		return null;
	}
	
	public void close(){
		if(mDbDriver != null)
			mDbDriver.close();
	}
	
	public DatabaseDriver getDriver(){ return mDbDriver; }
	
	public Statement getStatementConnection(){ return mDbDriver.createStatement(""); }
	
}
