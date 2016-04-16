package com.etc;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCUtils {
	public static final String CLASSNAME = "com.mysql.jdbc.Driver";
	public static final String URL = "jdbc:mysql://localhost:3306/kf17";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "root";
	
	private JDBCUtils(){
		
	}
	
	static {
		try {
			Class.forName(CLASSNAME);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取连接
	 * @return
	 */
	public static Connection getconnnection(){
		Connection con = null;
		try {
			con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	/**
	 * 关闭连接
	 * @param rs
	 * @param st
	 * @param con
	 */
	public static void close(ResultSet rs,Statement st,Connection con){
		try {
			try{
				if(rs!=null){
				rs.close();
				}
			}finally{
				try{
					if(st!=null){
						st.close();
					}				
				}finally{
					if(con!=null)
						con.close();		
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 关闭连接
	 * @param st
	 * @param con
	 */
//	public static void close(Statement st,Connection con){
//		try {
//			try{
//				if(st!=null){
//					st.close();
//				}				
//			}finally{
//				if(con!=null)
//					con.close();		
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 新增、修改、删除
	 * @param sql
	 * @param args
	 * @return
	 */
	public static int update(String sql,Object ... args){
		int result = 0;
		Connection con = getconnnection();
		PreparedStatement ps = null; 
		try {
			ps = con.prepareStatement(sql);
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject((i+1), args[i]);
				}
			}
			result = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(null,ps,con);
		}
		
		return result;
	}
	/**
	 * 查询，返回ResultSet，需手动关闭资源  不推荐使用
	 * @param sql
	 * @param args
	 * @return
	 */
	@Deprecated
	public static ResultSet query(String sql,Object ... args){
		ResultSet result = null;
		Connection con = getconnnection();
		PreparedStatement ps = null; 
		try {
			ps = con.prepareStatement(sql);
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject((i+1), args[i]);
				}
			}
			result = ps.executeQuery();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
		}
		return result;
	}
	
	/**
	 * 查询单条记录，并且返回一个Map
	 * @param sql
	 * @param args
	 * @return
	 */
	public static Map<String,Object> queryForMap(String sql,Object ... args){
		Map<String,Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = queryForList(sql, args);
		if(list.size() > 0){
			result = list.get(0);
		}
		return result;
	}
	
	/**
	 * 查询单条记录，并且返回一个对象
	 * @param sql
	 * @param args
	 * @return
	 */
	public static <T> T queryForObject(String sql,Class<T> clz,Object ... args){
		T result = null;
		
		return result;
	}
	
	/**
	 * 查询单条记录，并且返回多个Map
	 * @param sql
	 * @param args
	 * @return
	 */
	public static List<Map<String,Object>> queryForList(String sql,Object ... args){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			con = getconnnection();
			ps = con.prepareStatement(sql);
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject((i+1), args[i]);
				}
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while(rs.next()){
				Map<String,Object> map = new HashMap<String,Object>();
				for (int i = 1; i <= columnCount; i++) {
					map.put(rsmd.getColumnLabel(i), rs.getObject(i));	
				}
				result.add(map);					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(rs, ps, con);
		}
		return result;
	}
	
	/**
	 * 查询单条记录，并且返回多个对象
	 * @param sql
	 * @param args
	 * @return
	 */
	public static <T> List<T> queryForList(String sql,Class<T> clz,Object ... args){
		List<T> result = null;
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			T obj = clz.newInstance();
			con = getconnnection();
			ps = con.prepareStatement(sql);
			if(args != null){
				for (int i = 0; i < args.length; i++) {
					ps.setObject((i+1), args[i]);
				}
			}
			rs = ps.executeQuery();
			
			clz.getDeclaredFields();
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
