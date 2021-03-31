/*
 * 
 * 创建日期 2006-1-17
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.webtopo;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;

/**
 * 实现对VPN用户相关添加、删除、修改操作项
 * 
 * @author suzr
 * 
 * TODO 要更改此生成的类型注释的模板，请转至 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
public class VPNAct {

    //获取未关联用户列表
	private String m_VPNUsers_List_SQL = "select vpn_id,username,vpn_name,cust_level_id,cust_type_name,vpn_type_id,topo_type FROM vpn_customer a,vpn_customer_type b where a.cust_type_id=b.cust_type_id and a.vpn_id not in (select distinct vpn_id from vpn_auto_customer)";

    /**
     * 新增LAN用户信息
     */
    private String m_VPNUserAdd_SQL = "insert into vpn_customer (vpn_id,ext_vpn_id,username,vpn_name,cust_level_id,cust_type_id,vpn_type_id,topo_type,linkman,cred_type_id,cardno,address,user_state,linkphone,mobile,email,complete_time,remark1) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    private String m_RelateUserInfo_SQL = "update vpn_auto_customer set vpn_id=? where vpn_auto_id=?";

    private Cursor cursor = null;

    private String str_vpnid = null;
    
    private String str_vpn_auto_id = null;

    private PrepareSQL pSQL;
    
	private int offset;

	private int MaxLine;

	private int total;

	private int curPage;

	private int totalPage;

	private String query;

    public VPNAct() {
        pSQL = new PrepareSQL();
    }

    /**
     * 所有VPN用户列表
     * 
     * @return ArrayList（strBar,cursor）
     */
    public ArrayList getUnrelateVPNUsersCursor(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.clear();

        String stroffset = request.getParameter("offset");
        int pagelen = 15;
        int offset;
        if (stroffset == null)
            offset = 1;
        else
            offset = Integer.parseInt(stroffset);

        //HttpSession session = request.getSession();
        //UserRes curUser = (UserRes) session.getAttribute("curUser");
       
        initPage(m_VPNUsers_List_SQL, offset, pagelen);
        String strBar = getPageBar();
        list.add(strBar);
        PrepareSQL psql = new PrepareSQL(m_VPNUsers_List_SQL);
    	psql.getSQL();
        cursor = DataSetBean.getCursor(m_VPNUsers_List_SQL, offset, pagelen);
        list.add(cursor);
        return list;
    }

    /**
     * 增加、删除、更新
     * 
     * @param request
     * @return String
     */
    public int[] VPNUserInfoAct(HttpServletRequest request) {
        String strSQL = "";
        //String strMsg = "";
        int[] iCode = {0,0};
        String strAction = request.getParameter("action");

        if (strAction.equals("delete")) { // 删除操作
            String str_vpnid = request.getParameter("vpn_id");
            strSQL = "delete from vpn_customer where vpn_id=" + str_vpnid;
            //arrAddressSQL = "delete from cus_lanip where user_id=" + str_userid;
        } else {
        	String ext_vpn_id = request.getParameter("ext_vpn_id");
        	String username = request.getParameter("username");
        	String vpn_name = request.getParameter("vpn_name");
        	String cust_level_id = request.getParameter("cust_level_id");
        	String cust_type_id = request.getParameter("cust_type_id");
        	String vpn_type_id = request.getParameter("vpn_type_id");
        	String topo_type = request.getParameter("topo_type");
        	String linkman = request.getParameter("linkman");
        	String cred_type_id = request.getParameter("cred_type_id");

        	String cardno = request.getParameter("cardno");
        	String address = request.getParameter("address");
        	String user_state = request.getParameter("user_state");
        	String linkphone = request.getParameter("linkphone");
        	String mobile = request.getParameter("mobile");
        	String email = request.getParameter("email");
        	String complete_time = request.getParameter("hidcomplete_time");
        	String remark1 = request.getParameter("remark1");

//            if (!str_device_id.equals("0")) {
//                DeviceAct deviceAct = new DeviceAct();
//                str_gather_id = String.valueOf(deviceAct.getDeviceInfoMap(str_device_id).get("gather_id"));
//                str_device_ip = String.valueOf(deviceAct.getDeviceInfoMap(str_device_id).get("loopback_ip"));
//            }
//
//            if (Integer.parseInt(str_basdevice_id) > 0) {
//                DeviceAct deviceAct = new DeviceAct();
//                str_basdevice_ip = String.valueOf(deviceAct.getDeviceInfoMap(str_basdevice_id).get("loopback_ip"));
//            }

            //String[] arr_ipaddress = request.getParameterValues("ipaddress");

            if (strAction.equals("add")) {//手工录入用户信息
                // 取得新的userid
                long vpnid = DataSetBean.getMaxId("vpn_customer", "vpn_id");
                str_vpn_auto_id = request.getParameter("vpn_auto_id");
                str_vpnid = String.valueOf(vpnid);

                // 先处理输入框未填写情况
                if (cred_type_id == null || cred_type_id.length() == 0) {
                	cred_type_id = "0";
                }
                if (cardno == null || cardno.length() == 0) {
                	cardno = "0";
                }
                if (user_state == null || user_state.length() == 0) {
                	user_state = "0";
                }
                if (linkphone == null || linkphone.length() == 0) {
                	linkphone = "0";
                }
                if (mobile == null || mobile.length() == 0) {
                	mobile = "0";
                }
                if (email == null || email.length() == 0) {
                	email = "0";
                }
                if (remark1 == null || remark1.length() == 0) {
                	remark1 = "0";
                }
                if (complete_time == null || complete_time.length() == 0) {
                	complete_time = "0";
                }
                

                pSQL.setSQL(m_VPNUserAdd_SQL);
                //Integer.parseInt(str_userid)
                pSQL.setInt(1, Integer.parseInt(str_vpnid));
                pSQL.setString(2, ext_vpn_id);
                pSQL.setString(3, username);
                pSQL.setString(4, vpn_name);
                pSQL.setInt(5, Integer.parseInt(cust_level_id));
                pSQL.setInt(6, Integer.parseInt(cust_type_id));
                pSQL.setInt(7, Integer.parseInt(vpn_type_id));
                pSQL.setInt(8, Integer.parseInt(topo_type));
                pSQL.setString(9, linkman);
                pSQL.setInt(10, Integer.parseInt(cred_type_id));
                pSQL.setString(11, cardno);
                pSQL.setString(12, address);
                pSQL.setInt(13, Integer.parseInt(user_state));
                pSQL.setString(14, linkphone);
                pSQL.setString(15, mobile);
                pSQL.setString(16, email);
                pSQL.setInt(17, Integer.parseInt(complete_time));
                pSQL.setString(18, remark1);
                strSQL = pSQL.getSQL();
                strSQL = strSQL.replaceAll(",,", ",null,");
                strSQL = strSQL.replaceAll(",\\)", ",null)");                
                
            	pSQL.setSQL(m_RelateUserInfo_SQL);
                pSQL.setInt(1, Integer.parseInt(str_vpnid));
                pSQL.setInt(2, Integer.parseInt(str_vpn_auto_id));
                
                strSQL += ";"+pSQL.getSQL();
                
                //logger.debug("add sql: "+strSQL);
                
            } else if(strAction.equals("edit")){//编辑用户信息
                str_vpnid = request.getParameter("vpn_id");
                strSQL = "update vpn_customer set ext_vpn_id='" + ext_vpn_id + "',username='" + username + "',vpn_name='"
                        + vpn_name + "',cust_level_id=" + cust_level_id + ",cust_type_id=" + cust_type_id + ",vpn_type_id=" + vpn_type_id
                        + ",topo_type=" + topo_type + ",linkman='" + linkman + "',cred_type_id=" + cred_type_id
                        + ",cardno='" + cardno + "',address='" + address + "',user_state=" + user_state + ",linkphone='"
                        + linkphone + "',mobile='" + mobile + "',email='" + email + "',complete_time=" + complete_time + ",remark1='"
                        + remark1 + "' where vpn_id=" + str_vpnid;

                strSQL = strSQL.replaceAll("=,", "=null,");
                strSQL = strSQL.replaceAll("= where", "=null where");
                
                //logger.debug("edit sql: "+strSQL);

            }else if(strAction.equals("relate")){//关联用户信息
            	str_vpnid = request.getParameter("vpn_id");
            	str_vpn_auto_id = request.getParameter("vpn_auto_id");
            	
            	pSQL.setSQL(m_RelateUserInfo_SQL);
                pSQL.setInt(1, Integer.parseInt(str_vpnid));
                pSQL.setInt(2, Integer.parseInt(str_vpn_auto_id));
                
                strSQL = pSQL.getSQL();
                //logger.debug("relate sql: "+strSQL);
            }
        }

        if (!strSQL.equals("")) {
        	PrepareSQL psql = new PrepareSQL(strSQL);
        	psql.getSQL();
            iCode = DataSetBean.doBatch(strSQL);
        }

        return iCode;
    }

	public void initPage(String query, int start, int len) {
		offset = start;
		MaxLine = len;
		this.query = query;
		total = getTotal();
		totalPage = (int) Math.ceil((double) total / MaxLine);
		curPage = (int) Math.floor((double) offset / MaxLine + 1);
	}
	
	private int getTotal() {
		String query_pos;
//		String countSTR = "*";
		int begin, end, begin1;
		if (query.toUpperCase().indexOf("UNION ALL") == -1) {
			begin = query.toUpperCase().indexOf(" FROM ");
			end = query.toUpperCase().indexOf(" GROUP ");
			if (end == -1)
				end = query.toUpperCase().indexOf("ORDER");
			if (end == -1)
				end = query.length();

			query_pos = query.substring(begin, end).trim();
			begin1 = query.toUpperCase().indexOf(" DISTINCT ");

			String strSQL = "select count(1) As total "
					+ query_pos;
			
			PrepareSQL psql = new PrepareSQL(strSQL);
        	psql.getSQL();
			
			HashMap fields = DataSetBean.getRecord(strSQL);
			if (fields != null) {
				//logger.debug("Record Count: " + fields.get("total"));
				return (Integer.parseInt((String) fields.get("total")));
			}
				return 0;
		}
			Cursor cursor = DataSetBean.getCursor(this.query);
			return (cursor.getRecordSize());
	}

	/**
	 * 生成分页导航栏
	 * 
	 * @return 返回导航栏HTML代码
	 */
	public String getPageBar() {
		String strHTML = "";
		String strColor = "#535353";
		int first, next, prev, last;

		first = 1;
		next = offset + MaxLine;
		prev = offset - MaxLine;
		last = (totalPage - 1) * MaxLine + 1;

		if (offset > MaxLine)
			strHTML += "<A HREF='javaScript:getUserList("+first+")'>首页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">首页</FONT>&nbsp;";

		if (prev > 0)
			strHTML += "<A HREF='javaScript:getUserList("+prev+")'>前页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">前页</FONT>&nbsp;";

		if (next <= total)
			strHTML += "<A HREF='javaScript:getUserList("+next+")'>后页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">后页</FONT>&nbsp;";

		if (totalPage != 0 && curPage < totalPage)
			strHTML += "<A HREF='javaScript:getUserList("+last+")'>尾页</A>&nbsp;";
		else
			strHTML += "<FONT COLOR=" + strColor + ">尾页</FONT>&nbsp;";

		strHTML += "  页次：<b>" + curPage + "</b>/<b>" + totalPage + "</b>页 <b>";
		strHTML += MaxLine + "</b>条/页 共<b>" + total + "</b>条";

		return strHTML;
	}
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成方法存根

    }

}
