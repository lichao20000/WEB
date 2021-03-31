/*
 * Created on 2004-3-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.linkage.litms.system.dbimpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.linkage.commons.db.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.DBAdapter;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.litms.system.Operator;
import com.linkage.litms.system.User;
import com.linkage.litms.system.UserRes;
import com.linkage.module.gwms.Global;

/**
 * @author yuht
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbOperator {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(DbOperator.class);
    private PrepareSQL pSQL = null;

    /**
     * 载入所有菜单权限（operator_type = 1为web菜单）
     */
    private final String LOAD_ALL_OPERATOR = "select operator_id,operator_pid,operator_layer,operator_name,remark from tab_operator where operator_type = 1 order by operator_id";

    private final String LOAD_PERMESSION_OPERATOR = "select operator_id,operator_pid,operator_layer,operator_name,remark from tab_operator where operator_type = 1 and operator_id in (select operator_id from tab_role_operator where role_id=?) order by operator_id";

    public DbOperator() {
        if (pSQL == null) {
            pSQL = new PrepareSQL();
        }
    }

    /**
     * 获取所有web菜单权限项
     * 
     * @return Vector
     */
    public Vector getAllOperator() {
        Vector list = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Operator operator = null;

        try {
            conn = DBAdapter.getJDBCConnection();
            stmt = conn.createStatement();
//            stmt.execute("set rowcount 0");
            rs = stmt.executeQuery(LOAD_ALL_OPERATOR);

            list = new Vector();
            while (rs.next()) {
                operator = new Operator(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5));
                list.add(operator);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                rs.close();
                rs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                stmt.close();
                stmt = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                conn.close();
                conn = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.debug("All operator length is " + list.size());

        return list;
    }

    /**
     * 获取指定角色操作菜单权限
     * 
     * @param role_id
     * @return Vector
     */
    public Vector getPermessionByRoleid(int role_id) {
        Vector list = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Operator operator = null;
        // String sql = "select operator_id," +
        // "operator_pid,operator_layer,operator_name,remark from " +
        // "tab_operator where operator_id in ("
        // + "select operator_id from tab_role_operator where operator_type = 1
        // and role_id=" + role_id + ") " + " order by operator_id";
        try {
            pSQL.setSQL(LOAD_PERMESSION_OPERATOR);
            pSQL.setInt(1, role_id);

            conn = DBAdapter.getJDBCConnection();
            stmt = conn.createStatement();
			// stmt.execute("set rowcount 0");
            rs = stmt.executeQuery(pSQL.getSQL());
            pSQL = null;
            list = new Vector();
            while (rs.next()) {
                operator = new Operator(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5));
                list.add(operator);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                rs.close();
                rs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                stmt.close();
                stmt = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                conn.close();
                conn = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.debug(role_id + "'s operator length is " + list.size());
        return list;
    }
    /**
     * 获取指定角色操作菜单权限
     * 
     * @param role_id
     * @return Vector
     */
    public Vector getPermessionByRoleid_1(String role_id) {
        Vector list = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        Operator operator = null;
        String sql = "select operator_id,operator_pid,operator_layer,operator_name,remark from tab_operator where operator_type = 1 "
        			 +  "and operator_id in (select operator_id from tab_role_operator where role_id in "
        			 + role_id + ") order by operator_id";
        // "operator_pid,operator_layer,operator_name,remark from " +
        // "tab_operator where operator_id in ("
        // + "select operator_id from tab_role_operator where operator_type = 1
        // and role_id=" + role_id + ") " + " order by operator_id";
        try {
//            pSQL.setSQL(LOAD_PERMESSION_OPERATOR);
//            pSQL.setInt(1, role_id);

        	conn = DBAdapter.getJDBCConnection();
            stmt = conn.createStatement();
			// stmt.execute("set rowcount 0");
            PrepareSQL psql = new PrepareSQL(sql);
            psql.getSQL();
            rs = stmt.executeQuery(sql);
            pSQL = null;
            list = new Vector();
            while (rs.next()) {
                operator = new Operator(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getString(5));
                list.add(operator);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            try {
                rs.close();
                rs = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                stmt.close();
                stmt = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                conn.close();
                conn = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.debug(role_id + "'s operator length is " + list.size());
        return list;
    }

    /**
     * 根据上一界面提交参数处理角色权限
     * 
     * @param request
     * @return String
     */
    public String operatorDoAct(HttpServletRequest request) {
        String strSQL = null;
        String strMsg = null;
        String strAction = request.getParameter("action");
        String role_id = "";
        int action_id;
        String[] str_operators = null;

        HttpSession session = request.getSession();
        UserRes curUser = (UserRes) session.getAttribute("curUser");
        User user = curUser.getUser();
        long ACC_OID = user.getId();
        long role_pid = user.getRoleId();

        if (strAction.equals("delete")) { // 删除操作
            role_id = request.getParameter("role_id");
            strSQL = "delete from tab_role where role_id=" + role_id;
            strSQL += ";delete from tab_acc_role where role_id=" + role_id;
            action_id = 2;
        } else {
            String role_name = request.getParameter("role_name");
            String role_desc = request.getParameter("role_desc");      
            str_operators = request.getParameterValues("operator_id");
            if (strAction.equals("add")) { // 增加操作
                // 判断是否已经存在
                strSQL = "select * from tab_role where role_name='" + role_name + "'";
                // teledb
                if (DBUtil.GetDB() == Global.DB_MYSQL) {
                    strSQL = "select role_id, role_name, role_pid, acc_oid, role_desc from tab_role where role_name='" + role_name + "'";
                }
                PrepareSQL psql = new PrepareSQL(strSQL);
                psql.getSQL();
                if (DataSetBean.getRecord(strSQL) != null)
                    strMsg = "角色" + role_name + "已经存在，请换一个名称。";
                else {
                    long maxrole_id = DataSetBean.getMaxId("tab_role", "role_id");
                    strSQL = "insert into tab_role (role_id,role_name,role_pid,acc_oid,role_desc) values (" + maxrole_id + ",'" + role_name + "',"
                            + role_pid + "," + ACC_OID + ",'" + role_desc + "')";

                    strSQL = StringUtils.replace(strSQL, ",,", ",null,");
                    strSQL = StringUtils.replace(strSQL, ",,", ",null,");
                    strSQL = StringUtils.replace(strSQL, ",)", ",null)");
                    // role_id = Integer.toString(maxrole_id);
                    role_id = String.valueOf(maxrole_id);
                }
                action_id = 0;
            } else {
                role_id = request.getParameter("role_id");
                // 判断是否和其他角色同名
                strSQL = "select * from tab_role where role_name='" + role_name + "' and role_id<>" + role_id;
                // teledb
                if (DBUtil.GetDB() == Global.DB_MYSQL) {
                    strSQL = "select role_id, role_name, role_pid, acc_oid, role_desc from tab_role where role_name='" + role_name + "' and role_id<>" + role_id;
                }

                PrepareSQL psql = new PrepareSQL(strSQL);
                psql.getSQL();
                if (DataSetBean.getRecord(strSQL) != null)
                    strMsg = "角色" + role_name + "已经存在，请换一个名称。";
                else {
                    strSQL = "update tab_role set role_name='" + role_name + "',role_pid=" + role_pid + ",acc_oid=" + ACC_OID + ",role_desc='"
                            + role_desc + "' where role_id=" + role_id;

                    strSQL = StringUtils.replace(strSQL, "=,", "=null,");
                    strSQL = StringUtils.replace(strSQL, "= where", "=null where");
                }
                action_id = 1;
            }
        }
        
        if (strMsg == null && !strSQL.equals("")) {
            DbRole dbrole = new DbRole();
            int[] iCode = dbrole.EditPremession(str_operators, action_id, role_id);            
            if (iCode != null && iCode.length > 0) {
            	PrepareSQL psql = new PrepareSQL(strSQL);
                psql.getSQL();
                iCode = DataSetBean.doBatch(strSQL);
                
                if (iCode != null && iCode.length > 0) {
                    strMsg = "角色保存操作成功！";
                } else {
                    strMsg = "角色保存操作失败，请返回重试或稍后再试！";
                    logger.debug("=========");
                }
            } else {
                strMsg = "角色保存操作失败，请返回重试或稍后再试！";
                logger.debug("=========EditPremession");
            }
        }
        
        //logger.debug("operatorDoAct_result:"+strMsg);

        return strMsg;
    }

    public static void main(String[] args) {
        DbOperator dboperator = new DbOperator();
        Vector list = dboperator.getPermessionByRoleid(1);
        // Vector list = dboperator.getAllOperator();
        if (list != null) {
            //logger.debug(list.size());
        } else {
            logger.debug("list is null");
        }
    }
}
