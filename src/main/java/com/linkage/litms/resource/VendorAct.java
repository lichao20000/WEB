/*
 *
 * 创建日期 2006-2-20
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.linkage.commons.db.DBUtil;
import com.linkage.commons.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.chart.PieChartDemo;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;
import com.linkage.litms.common.filter.SelectCityFilter;
import com.linkage.module.gwms.Global;

public class VendorAct {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(VendorAct.class);
    /**
     * 获取所有厂商信息
     */
    private String m_VendorAll_SQL = "select * FROM tab_vendor";

    /**
     * 获取某个设备厂商的信息
     */
    private String m_VendorInfo_ByVendorId_SQL = "select * from tab_vendor where vendor_id= ?";

    /**
     * 增加设备厂商
     */
    private String m_VendorAdd_SQL="insert into tab_vendor(vendor_id,vendor_name,vendor_add,telephone,staff_id,remark) values(?,?,?,?,?,?)";

    /**
     * 更新设备厂商
     */
    private String m_VendorUpdate_SQL ="update tab_vendor set vendor_name=?,vendor_add=?,telephone=?,staff_id=?,remark=? where vendor_id=?";

    /**
     * 删除设备厂商
     */
    private String m_VendorDelete_SQL ="delete from tab_vendor where vendor_id =?";
    /**
     * 统计饼图用的厂商SQL
     */
    private String vendor_Sql = "select b.vendor_name, b.vendor_id, count(1) num from tab_deviceresource a, tab_vendor b where a.city_id=? and a.vendor_id=b.vendor_id group by b.vendor_name,b.vendor_id";
//    private String vendor_sql_bbms = "select b.vendor_name, b.vendor_id, count(1) num from tab_gw_device a, tab_vendor b where a.city_id in (?) and a.oui=b.vendor_id and a.gw_type=2 group by b.vendor_name,b.vendor_id";
    private String vendor_sql_bbms = "select b.vendor_name, b.vendor_id, count(1) num from tab_gw_device a, tab_vendor b where a.city_id in (?) and a.vendor_id=b.vendor_id and a.gw_type=2 group by b.vendor_name,b.vendor_id";

    private PrepareSQL pSQL = null;

    private String city_id = null;
    private String vendor_id = null;

    /**
     * request object
     */
    private HttpServletRequest request = null;
    private HttpServletResponse response = null;

    public VendorAct() {
        pSQL = new PrepareSQL();
    }

    /**
     * 根据设备厂商ID获得厂商信息
     *
     * @return Map
     */
    public HashMap getSingleVendorInfo(HttpServletRequest request) {

    	HashMap vendorInfo = null;
    	String vendor_id = request.getParameter("vendor_id");
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            m_VendorInfo_ByVendorId_SQL = "select vendor_name, vendor_add, telephone, staff_id, remark from tab_vendor where vendor_id= ?";
        }
    	if(null!=vendor_id)
    	{
    		pSQL.setSQL(m_VendorInfo_ByVendorId_SQL);
    		pSQL.setStringExt(1,vendor_id,true);
    		logger.debug("m_VendorInfo_ByVendorId_SQL:"+pSQL.getSQL());
    		vendorInfo = DataSetBean.getRecord(pSQL.getSQL());
    	}
    	return vendorInfo;
    }


    /**
     * 分页查询，获得所有厂商
     *
     * @param request
     * @return ArrayList
     */
    public ArrayList getVendorList(HttpServletRequest request) {
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
            m_VendorAll_SQL = "select vendor_id, vendor_name, vendor_add, telephone FROM tab_vendor";
        }
        qryp.initPage(m_VendorAll_SQL, offset, pagelen);
        String strBar = qryp.getPageBar();
        list.add(strBar);
        PrepareSQL psql = new PrepareSQL(m_VendorAll_SQL);
		psql.getSQL();
        Cursor cursor = DataSetBean.getCursor(m_VendorAll_SQL, offset, pagelen);
        list.add(cursor);

        return list;
    }

    /**
     * 更新设备厂商（添加、删除、编辑）
     * @param request
     * @return 操作成功：true，操作失败：false
     */
    public boolean updateVendor(HttpServletRequest request) {
        boolean result = true;
        String str_vendor_name = request.getParameter("vendor_name");
        String str_vendor_add = request.getParameter("vendor_add");
        String str_telephone = request.getParameter("telephone");
        String str_staff_id = request.getParameter("staff_id");
        String str_remark = request.getParameter("remark");
        String action = request.getParameter("action");
        //添加
        if ("add".equals(action)) {
            String getVendorIdSql = "select nvl(max(to_number(vendor_id)),0)+1 as vendorid from tab_vendor";
            // teledb
            if (DBUtil.GetDB() == Global.DB_MYSQL) {
                getVendorIdSql = "select ifnull(max(cast(vendor_id as signed)),0)+1 as vendorid from tab_vendor";
            }
            
            com.linkage.commons.db.PrepareSQL psql1 = new com.linkage.commons.db.PrepareSQL(getVendorIdSql);
            psql1.getSQL();
            String str_vendor_id = StringUtil.getStringValue(DataSetBean.getRecord(getVendorIdSql),"vendorid");
            String queryByName = "select vendor_id from tab_vendor where vendor_name ='" + str_vendor_name + "'";
            if (DataSetBean.getRecord(queryByName) == null) {
                pSQL.setSQL(m_VendorAdd_SQL);
                pSQL.setStringExt(1, str_vendor_id, true);
                pSQL.setStringExt(2, str_vendor_name, true);
                pSQL.setStringExt(3, str_vendor_add, true);
                pSQL.setStringExt(4, str_telephone, true);
                pSQL.setStringExt(5, str_staff_id, true);
                pSQL.setStringExt(6, str_remark, true);
                logger.debug("vendor_add_SQL:" + pSQL.getSQL());
                int row = DataSetBean.executeUpdate(pSQL.getSQL());
                if (row <= 0) {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        //编辑
        else if ("update".equals(action)) {
            String str_vendor_id = request.getParameter("vendor_id");
            if (null == str_vendor_id) {
                result = false;
            } else {
                pSQL.setSQL(m_VendorUpdate_SQL);
                pSQL.setStringExt(1, str_vendor_name, true);
                pSQL.setStringExt(2, str_vendor_add, true);
                pSQL.setStringExt(3, str_telephone, true);
                pSQL.setStringExt(4, str_staff_id, true);
                pSQL.setStringExt(5, str_remark, true);
                pSQL.setStringExt(6, str_vendor_id, true);
                logger.debug("VendorUpdate_SQL:" + pSQL.getSQL());
                int row = DataSetBean.executeUpdate(pSQL.getSQL());
                if (row < 0) {
                    result = false;
                }
            }

        }
        //删除
        else {
            String str_vendor_id = request.getParameter("vendor_id");
            if (null == str_vendor_id) {
                result = false;
            } else {
                pSQL.setSQL(m_VendorDelete_SQL);
                pSQL.setStringExt(1, str_vendor_id, true);
                logger.debug("VendorDelete_SQL:" + pSQL.getSQL());
                int row = DataSetBean.executeUpdate(pSQL.getSQL());
                if (row < 0) {
                    result = false;
                }
            }
        }
        return result;
    }
    public VendorAct(HttpServletRequest request,HttpServletResponse response){
    	this.request = request;
        this.response = response;
        city_id = request.getParameter("city_id");
    }


    public String getVendorPerfTable(String city_id,String city_name)
    {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            vendor_sql_bbms = "select b.vendor_name, b.vendor_id, count(*) num from tab_gw_device a, tab_vendor b where a.city_id in (?) and a.vendor_id=b.vendor_id and a.gw_type=2 group by b.vendor_name,b.vendor_id";
        }
        PrepareSQL pSQL = new PrepareSQL(vendor_sql_bbms);
        //属地
        SelectCityFilter scf = new SelectCityFilter();
        String citys = scf.getAllSubCityIds(city_id,true);
        pSQL.setStringExt(1, citys, false);
        Cursor cursor =  DataSetBean.getCursor(pSQL.getSQL());
        StringBuffer sbAll = new StringBuffer();
        sbAll.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=center>");
        sbAll.append("<tr><td HEIGHT=20>&nbsp;&nbsp;</td></tr>");
        sbAll.append("<TR><TD><table width=\"95%\" align=center  height=\"30\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"green_gargtd\">");
        sbAll.append("<tr><td width=\"162\" align=\"center\" class=\"title_bigwhite\">设备资源统计</td></tr>");
        sbAll.append("</TABLE></TD></TR><TR><TD>");
        sbAll.append("<TABLE border=0 cellspacing=1 cellpadding=2 width=\"95%\" align=center bgcolor=#999999>");
        //sbAll.append("<tr><TH colspan='4' align=center>设备资源统计报表  "+city_name+"</TH></tr>");
        sbAll.append(printTable(cursor));
        sbAll.append("</table>");
        sbAll.append("</TD></TR></TABLE>");
        return sbAll.toString();
    }

    public String getVendorPerfTable () {
        // teledb
        if (DBUtil.GetDB() == Global.DB_MYSQL) {
            vendor_sql_bbms = "select b.vendor_name, b.vendor_id, count(*) num from tab_gw_device a, tab_vendor b where a.city_id in (?) and a.vendor_id=b.vendor_id and a.gw_type=2 group by b.vendor_name,b.vendor_id";
        }
        PrepareSQL pSQL = new PrepareSQL(vendor_sql_bbms);
        //属地
        SelectCityFilter scf = new SelectCityFilter();
        String citys = scf.getAllSubCityIds(city_id,true);
        pSQL.setStringExt(1, citys, false);

        Cursor cursor =  DataSetBean.getCursor(pSQL.getSQL());
        StringBuffer sbAll = new StringBuffer();
        sbAll.append("<TABLE border=0 cellspacing=1 cellpadding=2 width=\"95%\" align=center bgcolor=#999999>");
        sbAll.append("<tr bgcolor=#ffffff><td colspan=4 align=right><a href='javascript:subscribe_to()'>订阅此报表</a></td></tr>");
        sbAll.append(printTable(cursor));
        sbAll.append("</table>");
        return sbAll.toString();
    }

    public String printTable(Cursor cursor){

    	StringBuffer sb = new StringBuffer();
    	Map fields = cursor.getNext();
        sb.append("<th nowrap>厂商</th><th nowrap>台数</th>");
        if(fields == null){
            sb.append("<tr bgcolor=#ffffff><td colspan=4>查询无数据!</td></tr>");
        }
        while(fields != null){
            sb.append("<tr bgcolor=#ffffff>");
            sb.append("<td align=center>"+ fields.get("vendor_name") + "(" + fields.get("vendor_id") + ")" +"</td>");
            sb.append("<td align=center>"+ fields.get("num") +"</td>");

            sb.append("</tr>");

            fields = cursor.getNext();
        }
        cursor.Reset();
        PieChartDemo pie = new PieChartDemo();
        pie.setTitle("各厂商设备资源统计");
    	pie.setCursor(cursor);

    	pie.setSession(request.getSession());
    	try {
			pie.setPw(response.getWriter());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String filename = pie.generatePieDemo();

//		String filename = pie.jfreeTest();

    	if(filename != null){
            String graphURL = request.getContextPath()
                + "/servlet/DisplayChart?filename=" + filename;
            logger.debug("graphURL------------------" + graphURL);
            sb.append("<tr bgcolor=#ffffff><td colspan=4 align=center><img src='"+ graphURL +"' border=0 usemap=\'#"+ filename +"\'></td></tr>");
        }

        sb.append("<tr class=green_foot><td colspan=4>&nbsp;</td></tr>");
        return sb.toString();

    }

}

