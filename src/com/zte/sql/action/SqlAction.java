package com.zte.sql.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zte.framework.util.AjaxAction;
import com.zte.sql.service.MySqlConnection;

@Controller("sqlAction")
public class SqlAction extends AjaxAction {
	@Autowired
	private MySqlConnection mySqlConnection;
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	private Connection conn;
	//参数
	Map<String,String> parameter;
	
	//获取数据库连接
	public SqlSession getSqlSession(){
		return sqlSessionFactory.openSession();
	}
	
	//关闭数据库连接
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
	
	//获取表的数据
	public JSONObject findTable(String schema,String sql){
		JSONObject result=new JSONObject();
		SqlSession sqlSession = getSqlSession();
		List<JSONObject> listData=new ArrayList<JSONObject>();
		try {
			Connection conn=sqlSession.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from "+schema+"."+sql);
			
			// 获取结果集元数据  
		    ResultSetMetaData rsmd = rs.getMetaData();
		    String[] columns=new String[rsmd.getColumnCount()];
		    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
		    	String column=rsmd.getColumnName(i),type=rsmd.getColumnTypeName(i);
		    	int size=rsmd.getColumnDisplaySize(i);
		    	result.accumulate(column, type+"_"+size);
		    	columns[i-1]=column;
		    }
			// 获取集中数据  
			while (rs.next()) {  
				JSONObject jo=new JSONObject();
				for (int i = 0; i < columns.length; i++) {
					String key=columns[i], value=rs.getString(columns[i]);
					jo.accumulate(key,value);
				}
				listData.add(jo);
			}
			
			result.accumulate("DATA", listData);
		    
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			closeSqlSession(sqlSession);
		}
		return result;
	}
	
	//封装表的数据
	public JSONObject findTableData(String[] tables){
		JSONObject result=new JSONObject();
		for(String table:tables){
			if(StringUtils.isNotBlank(table)){
				JSONObject tableData =findTable("test",table);
				result.accumulate(table, tableData);
			}
		}
		return result;
	}
	
	public String show(){
		//String[] tables=new String[]{"image"};
		//JSONObject result = findTableData(tables);
		//JSONArray jt = mySqlConnection.findTables();
		//JSONArray ja = mySqlConnection.findTableColumns("image");
		return ajaxUtil.setSuccess();
	}
	
	//连接数据库
	public String index(){
		return "input";
	}
	
	//测试连接数据库
	public String testConnection(){
		String driver=parameter.get("driver");
        String url="jdbc:mysql://"+parameter.get("ip")+":"+parameter.get("port")+"/"+parameter.get("database");
        String user=parameter.get("user");
        String password=parameter.get("password");
        conn = mySqlConnection.getConnection(driver, url, user, password);
        //conn = mySqlConnection.getConnection();
        return ajaxUtil.setResult(mySqlConnection.isConnected(conn));
	}
	
	//获取数据库中所有的表、表中所有的列
	public String getTable(){
		if(conn==null){
			return ajaxUtil.setFail("请重新连接数据库！");
		}
		return ajaxUtil.setSuccess(formatTableName(mySqlConnection.findTables(conn)));
	}
	
	//获取表中数据
	public String findTableData(){
		HttpServletRequest request=ServletActionContext.getRequest();
		String sql=parameter.get("sql");
		String sqlCount="select count(1) from "+sql+" as temp";
		Integer pageIndex=Integer.valueOf(request.getParameter("page"));
		Integer pageSize=Integer.valueOf(request.getParameter("rows"));
		sql+=" limit "+(pageIndex-1)*pageSize+","+pageSize;
		try{
			Integer total = mySqlConnection.findTableCount(conn, sqlCount);
			JSONArray rows = mySqlConnection.findTableDatas(conn, "select * from "+sql);
			JSONObject j=new JSONObject();
			j.accumulate("rows", rows);
			j.accumulate("total", total);
			return ajaxUtil.setSuccess(j);
		}catch (SQLException e) {
			e.printStackTrace();
			return ajaxUtil.setFail(e.getMessage());
		}
	}
	
	//获取表中数据
	public String selectQuerySql(){
		HttpServletRequest request=ServletActionContext.getRequest();
		String sql=parameter.get("sql");
		Integer pageIndex=Integer.valueOf(request.getParameter("page"));
		Integer pageSize=Integer.valueOf(request.getParameter("rows"));
		JSONObject result=new JSONObject();
		if(pageIndex>1){
			try {
				String sqlCount="select count(1) from ("+sql+") as temp";
				Integer total = mySqlConnection.findTableCount(conn, sqlCount);
				if(total>pageSize){
					sql="select * from ("+sql+") as temp limit "+(pageIndex-1)*pageSize+","+pageSize;
				}
				JSONArray rows = mySqlConnection.findTableDatas(conn, sql);
				result.accumulate("rows", rows);
				result.accumulate("total", total);
				return ajaxUtil.setSuccess(result);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		JSONObject resultSql = mySqlConnection.executeSql(conn, sql,pageIndex,pageSize);
		if("select".equals(resultSql.get("type"))){
			List<JSONObject> list = (List<JSONObject>) resultSql.get("data");
			if(list.size()>100){
				result.accumulate("rows", list.subList(0, 100));
			}else{
				result.accumulate("rows", list);
			}
			result.accumulate("total", list.size());
		}else{
			result.accumulate("count", resultSql.get("data"));
		}
		return ajaxUtil.setSuccess(result);
	}
	
	//格式化tables
	private JSONArray formatTableName(List<String> list){
		JSONArray tablesArray=new JSONArray();
		for(String value : list){
			JSONObject j=new JSONObject();
			JSONArray array=mySqlConnection.findTableColumnInfo(conn, value);
			j.accumulate("id", value);
			j.accumulate("text", value);
			j.accumulate("iconCls","icon-table");
			j.accumulate("state", "closed");
			j.accumulate("columns", formatTableColumnDatagrid(array));
			j.accumulate("children", formatTableColumn(array));
			tablesArray.add(j);
		}
		return tablesArray;
	}
	
	//格式化table的column成easyui格式
	private JSONArray formatTableColumnDatagrid(JSONArray array){
		JSONArray tablesArray=new JSONArray();
		for(int i=0;i<array.size();i++){
			JSONObject column=(JSONObject)array.get(i);
			JSONObject j=new JSONObject();
			String value=(String) column.get("Field");
			j.accumulate("field", value);
			j.accumulate("title", value);
			j.accumulate("width", "100");
			//j.accumulate("sortable", true);
			tablesArray.add(j);
		}
		return tablesArray;
	}
	
	//格式化table的column
	private JSONArray formatTableColumn(JSONArray array){
		JSONArray tablesArray=new JSONArray();
		for(int i=0;i<array.size();i++){
			JSONObject column=(JSONObject) array.get(i);
			JSONObject j=new JSONObject();
			String value=(String) column.get("Field");
			String text=value+", "+column.get("Type");
			if("YES".equalsIgnoreCase((String) column.get("Null"))){
				text+=", Nullable";
			}
			if("PRI".equalsIgnoreCase((String) column.get("Key"))){
				j.accumulate("iconCls","icon-key");
			}else{
				j.accumulate("iconCls","icon-th-list");
			}
			j.accumulate("id", value);
			j.accumulate("text", text);
			j.accumulate("attributes", column);
			tablesArray.add(j);
		}
		return tablesArray;
	}

	
	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}
}
