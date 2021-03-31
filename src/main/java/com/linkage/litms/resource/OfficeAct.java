/*
 * 
 * 创建日期 2006-2-13
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.util.StringUtils;
import com.linkage.module.gwms.Global;

public class OfficeAct {
    private PrepareSQL pSQL = null;

    /**
     * 获取所有局向信息
     */
    private String m_OfficeAll_SQL = "select * FROM tab_office";

    /**
     * 根据局向ID删除局向
     */
    private String m_OfficeInfo_ByIdDel_SQL = "delete from tab_office where office_id=?";

    /**
     * 新增局向信息
     */
    private String m_OfficeInfoAdd_SQL = "insert into tab_office (office_id,office_name,staff_id,remark) values (?,?,?,?)";;

    /**
     * 更新局向信息
     */
    private String m_OfficeInfoUpdate_SQL = "update tab_office set office_name=?,staff_id=?,remark=? ,office_id=?  where office_id = ?";
    /**
     * 根据指定局向编码获得局向信息
     */
    private String m_OfficeInfo_ByOfficeId_SQL = "select * from tab_office where office_id=?";

    public OfficeAct() {
        pSQL = new PrepareSQL();
    }

    /**
     * 根据指定局向编码获得局向信息
     * 
     * @param m_CityId
     * @return Map
     */
    public Map getOfficeInfo(String m_OfficeId) {
        pSQL.setSQL(m_OfficeInfo_ByOfficeId_SQL);
        pSQL.setString(1, m_OfficeId);
        return DataSetBean.getRecord(pSQL.getSQL());
    }
    
    /**
     * 分页查询，获得所有属地
     * 
     * @param request
     * @return ArrayList
     */
    public ArrayList getOfficeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.clear();

        String stroffset = request.getParameter("offset");
        int pagelen = 15;
        int offset;
        if (stroffset == null)
            offset = 1;
        else
            offset = Integer.parseInt(stroffset);
        QueryPage qryp = new QueryPage();

        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            m_OfficeAll_SQL = "select office_id, office_name, staff_id FROM tab_office";
        }
        qryp.initPage(m_OfficeAll_SQL, offset, pagelen);
        String strBar = qryp.getPageBar();
        list.add(strBar);
        PrepareSQL psql = new PrepareSQL(m_OfficeAll_SQL);
    	psql.getSQL();
        Cursor cursor = DataSetBean.getCursor(m_OfficeAll_SQL, offset, pagelen);
        list.add(cursor);

        return list;
    }

    /**
     * 对局向资源进行操作（增、删、改）
     * 
     * @param request
     * @return String
     */
    public String officeAct(HttpServletRequest request) {
        String strSQL;
        String strMsg = "";
        String strAction = request.getParameter("action");
        if (strAction.equals("delete")) { // 删除操作
            String str_office_id = request.getParameter("office_id");
            pSQL.setSQL(m_OfficeInfo_ByIdDel_SQL);
            pSQL.setString(1, str_office_id);
            strSQL = pSQL.getSQL();
        } else {
            String str_office_id = request.getParameter("office_id");
            String str_office_id_old = request.getParameter("office_id_old");
            String str_office_name = request.getParameter("office_name");
            String str_staff_id = request.getParameter("staff_id");
            String str_remark = request.getParameter("remark");

            if (strAction.equals("add")) { // 增加操作
                pSQL.setSQL(m_OfficeInfoAdd_SQL);
                pSQL.setString(1, str_office_id.trim());
                pSQL.setString(2, str_office_name);
                pSQL.setString(3, str_staff_id);
                pSQL.setString(4, str_remark);
                strSQL = pSQL.getSQL();
                strSQL = StringUtils.replace(strSQL, ",,", ",null,");
                strSQL = StringUtils.replace(strSQL, ",,", ",null,");
                strSQL = StringUtils.replace(strSQL, ",)", ",null)");
            } else { // 修改操作
            // strSQL = "update tab_office set office_name='" + str_office_name
            // + "',staff_id='" + str_staff_id + "',remark='" + str_remark
            // + "' where office_id='" + str_office_id + "'";
                pSQL.setSQL(m_OfficeInfoUpdate_SQL);
                pSQL.setString(1, str_office_name);
                pSQL.setString(2, str_staff_id);
                pSQL.setString(3, str_remark);
                pSQL.setString(4, str_office_id);
                pSQL.setString(5, str_office_id_old);
                strSQL = pSQL.getSQL();
                strSQL = StringUtils.replace(strSQL, "=,", "=null,");
                strSQL = StringUtils.replace(strSQL, "= where", "=null where");
            }
        }

        // out.println(strSQL);
        if (!strSQL.equals("")) {
        	PrepareSQL psql = new PrepareSQL(strSQL);
        	psql.getSQL();
            int iCode = DataSetBean.executeUpdate(strSQL);
            if (iCode > 0) {
                strMsg = "局向资源操作成功！";
            } else {
                strMsg = "局向资源操作失败，请返回重试或稍后再试！";
            }
        }

        return strMsg;
    }
    
    /**
     *局向对应的Map
     * @author lizj （5202）
     * @return
     */
    public static Map getOfficeMap(){
    	PrepareSQL psql = new PrepareSQL("select office_id,office_name from tab_office");
    	psql.getSQL();
    	Cursor cursor = DataSetBean.getCursor("select office_id,office_name from tab_office");
    	Map fields = cursor.getNext();
    	Map map = new HashMap();
    	if(fields != null){
    		while(fields != null){
    			map.put(fields.get("office_id"), fields.get("office_name"));   			
    			fields = cursor.getNext();
    		}
    	}
    	return map;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成方法存根

    }

}

