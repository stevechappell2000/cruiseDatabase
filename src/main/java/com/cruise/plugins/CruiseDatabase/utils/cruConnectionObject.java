package com.cruise.plugins.CruiseDatabase.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import com.corecruise.cruise.SessionObject;
import com.corecruise.cruise.logging.Clog;
import com.zaxxer.hikari.HikariDataSource;

public class cruConnectionObject {
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Statement st = null;
    public cruConnectionObject() {

    }
    public Connection getConnection(SessionObject so, String poolName, HashMap<String,HikariDataSource> cruConnections) throws SQLException, Exception {
    	conn = null;
    	if(null == poolName || poolName.trim().length()<1) {
    		Clog.Error(so, "service", "80010", "(getConnection) PoolName is required and was not supplied.");
    	}else {
    		poolName = poolName.trim().toUpperCase();
    		try {
	    		if(cruConnections.containsKey(poolName)) {
	    			HikariDataSource ds = cruConnections.get(poolName);
		    		try {
		    			conn =  ds.getConnection();
		    		} catch (SQLException ex) {
		    			Clog.Error(so, "ser", "80020", "(getConnection) failed:"+ex.getMessage());
		    			throw ex;
		    		} 
	    		}
    		}catch(Exception e) {
    			Clog.Error(so, "ser", "80020", "(getConnection) failed:"+e.getMessage());
    			throw e;
    		}
    	}
    	return conn;
    }
    public boolean Close() {
    	boolean ret = false;
    	if(null != ps) {
    		try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(null != st) {
    		try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(null != rs) {
    		try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(null != conn) {
    		try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	return ret;
    }
    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
    	ps = null;
    	if(null != conn) {
    		ps = conn.prepareStatement(sql);
    	}
    	return ps;
    }
    public Statement Statement() throws SQLException {
    	st = null;
    	if(null != conn) {
    		st = conn.createStatement();
    	}
    	return st;
    }
    public ResultSet getResultSet() {
    	return rs;
    }
}
