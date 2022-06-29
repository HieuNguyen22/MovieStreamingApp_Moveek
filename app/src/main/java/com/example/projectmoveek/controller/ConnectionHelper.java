package com.example.projectmoveek.controller;

import android.content.Context;
import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;

public class ConnectionHelper {
    private Connection con;
    private Context mContext;
    String usename, password, ip, port, database;

    public Connection connectionClass() {
        ip = "127.0.0.1";
        database = "moveekdb";
        usename = "root";
        password = "songnhue31429";
        port = "3306";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:s";
        }
        catch (Exception ex) {
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return connection;
    }
}
