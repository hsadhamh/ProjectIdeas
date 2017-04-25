package com.parser.json;

import com.parser.json.DatabaseConnection.DbEngine;

public class JsonGenerateMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DatabaseConnection mysql = new DatabaseConnection();
		mysql.LoadDatabaseDriver(DbEngine.MYSQL);
		mysql.close();
	}
}
