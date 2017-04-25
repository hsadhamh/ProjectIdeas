package com.parser.json;

public class DatabaseConnection {
	DatabaseDriver mDbDriver = null;
	public static enum DbEngine{
		MYSQL,
		NONE
	};
	
	public void LoadDatabaseDriver(DbEngine nCaseSql){
		switch(nCaseSql){
			case MYSQL:
				mDbDriver = MySqlDriver.getInstance();
				mDbDriver.LoadAndConnectEngine();
				break;
			
			default:
				Logger.getInstance().Debug("Cannot load driver for [%s].", nCaseSql.toString());
				
		}
	}
	
	public void close(){
		if(mDbDriver != null)
			mDbDriver.close();
	}
	
}
