package com.example.new_login_page;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    String databaseURL = "jdbc:mysql://127.0.0.1:3306/valid_users";
    String databaseUsername = "root";
    String databasePassword = "1234";
    public Connection getDBConnection(){
        try{
            return DriverManager.getConnection(databaseURL , databaseUsername , databasePassword);
        }
        catch (java.sql.SQLException e) {
            System.out.println("59");
            System.out.println(e);
        }
        return null;
    }
}
