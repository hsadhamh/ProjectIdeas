package com.parser.json.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.parser.json.log.Logger;

public class MySqlDriver extends DatabaseDriver {
	Connection mSqlConn = null;
	boolean mbConnected = false;
	
	public static DatabaseDriver getInstance(){
		if(driver == null)
			driver = new MySqlDriver();
		return driver;
	}
	
	public Connection getConnection(){ return mSqlConn; }
	
	
	public boolean LoadAndConnectEngine(){
		try {
			if(mbConnected)
				return mbConnected;
			
            Class.forName("com.mysql.jdbc.Driver").newInstance();            
            mSqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/names_dev","root", "");
            if(mSqlConn == null)
            {
            	Logger.getInstance().Debug("MySQL Connection Failed.");
            	return false;
            }
            
            Logger.getInstance().Debug("MySQL Connection succeeded.");
            mbConnected = true;
            return mbConnected;
	    } catch (Exception ex) {
	    	Logger.getInstance().Debug("Exception caught [%s .. %s].", ex.toString(), ex.getStackTrace().toString());
	    	return false;
	    }
	}
	
	public ResultSet runQueryWithResults(String statement) throws SQLException{
		ResultSet result = null;
		Statement ps = null;
		try {
			ps = createStatement(statement);
			result = ps.executeQuery(statement);
		} catch (SQLException ex) {
			Logger.getInstance().Debug("Exception caught [%s .. %s].", ex.toString(), ex.getStackTrace().toString());
		}
		finally{
			if(ps != null) ps.close();
		}
		return result;
	}
	
	public Statement createStatement(String statement){
		Statement stmt = null;
		try {
			stmt = mSqlConn.createStatement();
		} catch (SQLException ex) {
			Logger.getInstance().Debug("Exception caught [%s .. %s].", ex.toString(), ex.getStackTrace().toString());
		}
		return stmt;
	}
	
	protected void close(){ 
		try {
			if(mSqlConn != null) 
				mSqlConn.close();
		} catch (SQLException ex) {
			Logger.getInstance().Debug("Exception caught [%s .. %s].", ex.toString(), ex.getStackTrace().toString());
		} 
	}
}
