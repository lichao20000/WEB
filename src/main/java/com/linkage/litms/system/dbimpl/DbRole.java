/*
 * Created on 2004-3-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.linkage.litms.system.dbimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DBAdapter;
import com.linkage.litms.system.Role;

/**
 * @author yuht
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbRole {
	private static final String LOAD_ROLE_BY_USERID="select role_id,role_name," +
		"role_desc from tab_role where role_id in " +
		"(select role_id from tab_acc_role where acc_oid=?)";
	private static final String LOAD_ALL_ROLE = "select role_id,role_name," +
		"role_desc from tab_role";
	
	public Vector getAllRole(){
		Vector list = null;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Role role = null;
		try{
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.createStatement();
			PrepareSQL psql = new PrepareSQL(LOAD_ALL_ROLE);
            psql.getSQL();
			rs   = stmt.executeQuery(LOAD_ALL_ROLE);
			list = new Vector();
			while(rs.next()){
//				role = new Role(rs.getInt(1),rs.getString(2),rs.getString(3));
                role = new RoleSyb();
                role.setRoleId(rs.getInt(1));
                role.setRoleName(rs.getString(2));
                role.setRoleDesc(rs.getString(3));
                
				list.add(role);
			}
		}
		catch( SQLException sqle ) {
			sqle.printStackTrace();
		}
		finally {
			try {
				rs.close();
				rs = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {  
				stmt.close();  
				stmt = null; 
			}
			catch (Exception e) { e.printStackTrace(); }
			try {  
				conn.close();   
				conn = null;
			}
			catch (Exception e) { e.printStackTrace(); }
		}		
		return list;			
	}
	
	public Vector getRoleByUserid(int userid){
		Vector list = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Role role = null;
		try{
			conn = DBAdapter.getJDBCConnection();
			PrepareSQL psql = new PrepareSQL(LOAD_ROLE_BY_USERID);
            psql.getSQL();
			pstmt = conn.prepareStatement(LOAD_ROLE_BY_USERID);
			pstmt.setInt(1,userid);
			rs = pstmt.executeQuery();
			list = new Vector();
			while(rs.next()){
//              role = new Role(rs.getInt(1),rs.getString(2),rs.getString(3));
                role = new RoleSyb();
                role.setRoleId(rs.getInt(1));
                role.setRoleName(rs.getString(2));
                role.setRoleDesc(rs.getString(3));
                
				list.add(role);
			}
		}
		catch( SQLException sqle ) {
			sqle.printStackTrace();
		}
		finally {
			try {
				rs.close();
				rs = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {  
				pstmt.close();  
				pstmt = null; 
			}
			catch (Exception e) { e.printStackTrace(); }
			try {  
				conn.close();   
				conn = null;
			}
			catch (Exception e) { e.printStackTrace(); }
		}		
		return list;					
	}
	
	public boolean chkHasRole(Vector list,String role_id){
		boolean result = false;
		Role role;
		for(int i=0;i<list.size();i++){
			role = (Role)list.get(i);
			
			if(role.getRoleId() == Integer.parseInt(role_id)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	public int[] EditPremession(String[] operators,int action,String role_id){
		int[] rowResult=null;
		Connection conn = null;
		Statement stmt=null;
		String a;
		try{
			conn = DBAdapter.getJDBCConnection();
			stmt = conn.createStatement();
			if(action == 0){//增加
				for(int i=0; i<operators.length; i++) {
					PrepareSQL psql = new PrepareSQL("insert into tab_role_operator (role_id,operator_id) values ("+role_id+","+operators[i]+")");
		            psql.getSQL();
					stmt.addBatch("insert into tab_role_operator (role_id,operator_id) values ("+role_id+","+operators[i]+")");
				}
				//logger.debug("add");
			}
			else if(action == 1){//修改
				PrepareSQL psql = new PrepareSQL("delete from tab_role_operator where role_id="+role_id);
	            psql.getSQL();
				stmt.addBatch("delete from tab_role_operator where role_id="+role_id);
				for(int i=0; i<operators.length; i++) {
					psql = new PrepareSQL("insert into tab_role_operator (role_id,operator_id) values ("+role_id+","+operators[i]+")");
		            psql.getSQL();
					stmt.addBatch("insert into tab_role_operator (role_id,operator_id) values ("+role_id+","+operators[i]+")");
				}
				//logger.debug("update");
			}
			else{
				//logger.debug("delete");
				PrepareSQL psql = new PrepareSQL("delete from tab_role_operator where role_id="+role_id);
	            psql.getSQL();
				stmt.addBatch("delete from tab_role_operator where role_id="+role_id);
			}
			rowResult=stmt.executeBatch();
		}
		catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		finally {
			try {  
				stmt.close();  
				stmt = null; 
			}
			catch (Exception e) { e.printStackTrace(); }
			try {  
				conn.close();   
				conn = null;
			}
			catch (Exception e) { e.printStackTrace(); }
		}
		return rowResult;		
	}	
	
	public static void main(String[] args) {
	}
}
