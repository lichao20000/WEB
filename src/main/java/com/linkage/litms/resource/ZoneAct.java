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

public class ZoneAct {
    private PrepareSQL pSQL = null;

    /**
     * 获取所有局向信息
     */
    private String m_zoneAll_SQL = "select * FROM tab_zone";

    /**
     * 根据局向ID删除局向
     */
    private String m_zoneInfo_ByIdDel_SQL = "delete from tab_zone where zone_id=?";

    /**
     * 新增局向信息
     */
    private String m_zoneInfoAdd_SQL = "insert into tab_zone (zone_id,zone_name,staff_id,remark) values (?,?,?,?)";;

    /**
     * 更新小区信息
     */
    private String m_zoneInfoUpdate_SQL = "update tab_zone set zone_name=?,staff_id=?,remark=? ,zone_id=? where zone_id=?";
    /**
     * 根据指定小区编码获得小区信息
     */
    private String m_ZoneInfo_ByZoneId_SQL = "select * from tab_zone where zone_id=?";

    public ZoneAct() {
        pSQL = new PrepareSQL();
    }


    /**
     * 根据指定小区编码获得小区信息
     * 
     * @param m_ZoneId
     * @return Map
     */
    public Map getZoneInfo(String m_ZoneId) {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            m_ZoneInfo_ByZoneId_SQL = "select zone_name, zone_id, staff_id, remark from tab_zone where zone_id=?";
        }
        pSQL.setSQL(m_ZoneInfo_ByZoneId_SQL);
        pSQL.setString(1, m_ZoneId);
        return DataSetBean.getRecord(pSQL.getSQL());
    }
    /**
     * 分页查询，获得所有属地
     * 
     * @param request
     * @return ArrayList
     */
    public ArrayList getZoneList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.clear();
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            m_zoneAll_SQL = "select zone_id, zone_name, staff_id FROM tab_zone";
        }

        String stroffset = request.getParameter("offset");
        int pagelen = 15;
        int offset;
        if (stroffset == null)
            offset = 1;
        else
            offset = Integer.parseInt(stroffset);
        QueryPage qryp = new QueryPage();
        qryp.initPage(m_zoneAll_SQL, offset, pagelen);
        String strBar = qryp.getPageBar();
        list.add(strBar);
        PrepareSQL psql = new PrepareSQL(m_zoneAll_SQL);
		psql.getSQL();
        Cursor cursor = DataSetBean.getCursor(m_zoneAll_SQL, offset, pagelen);
        list.add(cursor);

        return list;
    }

    /**
     * 对小区资源进行操作（增、删、改）
     * 
     * @param request
     * @return String
     */
    public String zoneAct(HttpServletRequest request) {
        String strSQL;
        String strMsg = "";
        String strAction = request.getParameter("action");
        if (strAction.equals("delete")) { // 删除操作
            String str_zone_id = request.getParameter("zone_id");
            pSQL.setSQL(m_zoneInfo_ByIdDel_SQL);
            pSQL.setString(1, str_zone_id);
            strSQL = pSQL.getSQL();
        } else {
            String str_zone_id = request.getParameter("zone_id");
            String str_zone_id_old = request.getParameter("zone_id_old");
            String str_zone_name = request.getParameter("zone_name");
            String str_staff_id = request.getParameter("staff_id");
            String str_remark = request.getParameter("remark");

            if (strAction.equals("add")) { // 增加操作
                pSQL.setSQL(m_zoneInfoAdd_SQL);
                pSQL.setString(1, str_zone_id);
                pSQL.setString(2, str_zone_name);
                pSQL.setString(3, str_staff_id);
                pSQL.setString(4, str_remark);
                strSQL = pSQL.getSQL();
                strSQL = StringUtils.replace(strSQL, ",,", ",null,");
                strSQL = StringUtils.replace(strSQL, ",,", ",null,");
                strSQL = StringUtils.replace(strSQL, ",)", ",null)");
            } else { // 修改操作
            // strSQL = "update tab_zone set zone_name='" + str_zone_name +
            // "',staff_id='" + str_staff_id + "',remark='" + str_remark
            // + "' where zone_id='" + str_zone_id + "'";
                pSQL.setSQL(m_zoneInfoUpdate_SQL);
                pSQL.setString(1, str_zone_name);
                pSQL.setString(2, str_staff_id);
                pSQL.setString(3, str_remark);
                pSQL.setString(4, str_zone_id);
                pSQL.setString(5, str_zone_id_old);
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
                strMsg = "小区资源操作成功！";
            } else {
                strMsg = "小区资源操作失败，请返回重试或稍后再试！";
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
    	PrepareSQL psql = new PrepareSQL("select zone_id,zone_name from tab_zone");
		psql.getSQL();
    	Cursor cursor = DataSetBean.getCursor("select zone_id,zone_name from tab_zone");
    	Map fields = cursor.getNext();
    	Map map = new HashMap();
    	if(fields != null){
    		while(fields != null){
    			map.put(fields.get("zone_id"), fields.get("zone_name"));   			
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
