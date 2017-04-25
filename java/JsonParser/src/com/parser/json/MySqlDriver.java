package com.parser.json;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlDriver extends DatabaseDriver {
	Connection mSqlConn = null;
	
	public static DatabaseDriver getInstance(){
		if(driver == null)
			driver = new MySqlDriver();
		return driver;
	}
	
	public Connection getConnection(){ return mSqlConn; }
	
	
	public boolean LoadAndConnectEngine(){
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();            
            mSqlConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/names_dev","root", "");
            if(mSqlConn == null)
            	return false;
            Logger.getInstance().Debug("MySQL Connection succeeded.");
            return true;
	    } catch (Exception ex) {
	    	Logger.getInstance().Debug("Exception caught [%s .. %s].", ex.toString(), ex.getStackTrace().toString());
	    	return false;
	    }
	}
	
	public ResultSet runQueryWithResults(String statement) throws SQLException{
		ResultSet result = null;
		PreparedStatement ps = null;
		try {
			ps = createStatement(statement);
			result = ps.executeQuery();
		} catch (SQLException ex) {
			Logger.getInstance().Debug("Exception caught [%s .. %s].", ex.toString(), ex.getStackTrace().toString());
		}
		finally{
			if(ps != null) ps.close();
		}
		return result;
	}
	
	public PreparedStatement createStatement(String statement){
		PreparedStatement stmt = null;
		try {
			stmt = mSqlConn.prepareStatement(statement);
		} catch (SQLException ex) {
			Logger.getInstance().Debug("Exception caught [%s .. %s].", ex.toString(), ex.getStackTrace().toString());
		}
		return stmt;
	}
	
	protected void close(){ 
		try {
			mSqlConn.close();
		} catch (SQLException ex) {
			Logger.getInstance().Debug("Exception caught [%s .. %s].", ex.toString(), ex.getStackTrace().toString());
		} 
	}
}
