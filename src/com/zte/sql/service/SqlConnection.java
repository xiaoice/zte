package com.zte.sql.service;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SqlConnection {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	//获取本地ibatis数据库连接
	public SqlSession getSqlSession(){
		return sqlSessionFactory.openSession();
	}
	
	//关闭本地ibatis数据库连接
	public void closeSqlSession(SqlSession sqlSession){
		try {
			if (sqlSession != null) {
				sqlSession.close();
				sqlSession = null;
			}
		} 
		catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	//查看当前是否已经连接成功
	public boolean isConnected(Connection conn) {
		try {
			if(conn!=null){
				return !conn.isClosed();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	//获取JDBC连接
	public Connection getConnection(String driver,String url,String user,String password){
	    try {
			Class.forName(driver);
			return DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	//获取Ibatis连接
	public Connection getConnection(){
		return getSqlSession().getConnection();
	}
	//关闭JDBC连接
	public void closeConnection(Connection conn){
		if(isConnected(conn)){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
