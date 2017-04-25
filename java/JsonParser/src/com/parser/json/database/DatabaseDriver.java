package com.parser.json.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseDriver {
	protected static DatabaseDriver driver = null;
	public Connection getConnection(){ return null; }
	protected boolean LoadAndConnectEngine(){ return false; }
	protected ResultSet runQueryWithResults(String statement) throws SQLException { return null; }
	protected void close(){}
	public Statement createStatement(String statement) { return null; }
	
}
