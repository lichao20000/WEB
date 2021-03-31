<%--
Author		: yanhj
Date		: 2002-9-24
Note		:
Modifiy		: yanhj for maxupstreamrate,oper_accounts 2007-1-2
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.common.filter.*,com.linkage.litms.common.database.*"%>
<%
request.setCharacterEncoding("GBK");

//转换业务类型
Map productTypeMap = com.linkage.litms.netcutover.CommonMap.getProductTypeMap();
//转换操作类型
Map servTypeMap = com.linkage.litms.netcutover.CommonMap.getServTypeMap();

SearchSheet searchSheet = new SearchSheet();
searchSheet.setRequest(request);
Map field 	= searchSheet.getWorkSheetRecord();
Map field1 	= searchSheet.get97orginalRecord();

//工单相关信息
String username 		= null;
String worksheet_source		= null;
String system_id		= null;
String sheet_id			= null;
String product_id		= null;
String worksheet_receive_time	= null;
String producttype		= null;
String worksheet_exec_time	= null;
String servtype			= null;
String worksheet_start_time	= null;
String worksheet_end_time	= null;
String worksheet_priority	= null;
String worksheet_desc		= null;
String worksheet_error_desc	= null;

if(field != null){
	username 		= String.valueOf(field.get("username"));
	worksheet_source	= (field.get("worksheet_source").equals("0"))?"97系统":"手工录入";
	system_id		= String.valueOf(field.get("system_id"));
	sheet_id		= String.valueOf(field.get("sheet_id"));
	product_id		= String.valueOf(field.get("product_id"));
	worksheet_receive_time	= String.valueOf(field.get("worksheet_receive_time"));
	producttype		= String.valueOf(productTypeMap.get(String.valueOf(field.get("producttype"))));
	worksheet_exec_time	= String.valueOf(field.get("worksheet_exec_time"));
	servtype		= String.valueOf(servTypeMap.get(String.valueOf(field.get("servtype"))));
	worksheet_start_time	= String.valueOf(field.get("worksheet_start_time"));
	worksheet_end_time	= String.valueOf(field.get("worksheet_end_time"));
	worksheet_priority	= String.valueOf(field.get("worksheet_priority"));
	worksheet_desc		= String.valueOf(field.get("worksheet_desc"));
	worksheet_error_desc	= String.valueOf(field.get("worksheet_error_desc"));
	if (worksheet_desc == null || worksheet_desc.equals("null"))
        worksheet_desc = "";
	if (worksheet_error_desc == null || worksheet_error_desc.equals("null"))
        worksheet_error_desc = "";
}else{
	username 		= "";
	worksheet_source	= "";
	system_id		= "";
	sheet_id		= "";
	product_id		= "";
	worksheet_receive_time	= "";
	producttype		= "";
	worksheet_exec_time	= "";
	servtype		= "";
	worksheet_start_time	= "";
	worksheet_end_time	= "";
	worksheet_priority	= "";
	worksheet_desc		= "";
	worksheet_error_desc	= "";
}

//把相应地区、设备编码等转换成中文描述
String city_id 			= null;
String office_id 		= null;
String zone_id 			= null;
String adslbindphone		= null;
String dslam_ban_old_ip 	= null;
String dslam_ban_old_shelf 	= null;
String dslam_ban_old_frame 	= null;
String dslam_ban_old_slot 	= null;
String dslam_ban_old_port 	= null;
String maxdownstreamrate 	= null;
String maxupstreamrate		= null;
String oper_accounts		= null;
String dslam_ban_devicecode	= null;
String dslam_ban_old_devicecode	= null;
String dslam_ban_ip 		= null;
String dslam_ban_shelf 		= null;
String dslam_ban_frame 		= null;
String dslam_ban_slot 		= null;
String dslam_ban_port 		= null;
try{
city_id		= (String)field.get("system_id");
}
catch(Exception e){
city_id = "-1";
}

if(field1 != null){
	office_id 	= String.valueOf(field1.get("office_id"));	//局向
	zone_id 	= String.valueOf(field1.get("zone_id"));	//小区
	adslbindphone	= String.valueOf(field1.get("adslbindphone"));	//小区
	
	dslam_ban_old_devicecode= String.valueOf(field1.get("dslam_ban_old_devicecode"));	//旧接入设备编码
	dslam_ban_old_ip 	= String.valueOf(field1.get("dslam_ban_old_ip"));		//旧接入设备IP
	dslam_ban_old_shelf 	= String.valueOf(field1.get("dslam_ban_old_shelf"));	//旧接入设备机架号
	dslam_ban_old_frame 	= String.valueOf(field1.get("dslam_ban_old_frame"));	//旧接入设备框号
	dslam_ban_old_slot 	= String.valueOf(field1.get("dslam_ban_old_slot"));	//旧接入设备槽位号
	dslam_ban_old_port 	= String.valueOf(field1.get("dslam_ban_old_port"));	//旧接入设备端口	
	
	dslam_ban_devicecode	= String.valueOf(field1.get("dslam_ban_devicecode"));	//新接入设备编码
	dslam_ban_ip 		= String.valueOf(field1.get("dslam_ban_ip"));		//新接入设备IP
	dslam_ban_shelf 	= String.valueOf(field1.get("dslam_ban_shelf"));		//新接入设备机架号
	dslam_ban_frame 	= String.valueOf(field1.get("dslam_ban_frame"));		//新接入设备框号
	dslam_ban_slot 		= String.valueOf(field1.get("dslam_ban_slot"));		//新接入设备槽位号
	dslam_ban_port 		= String.valueOf(field1.get("dslam_ban_port"));		//新接入设备端口
	
	maxdownstreamrate 	= String.valueOf(field1.get("maxdownstreamrate"));	//最大下行速率
	maxupstreamrate 	= String.valueOf(field1.get("maxupstreamrate"));	//最大上行速率
	oper_accounts 	= String.valueOf(field1.get("oper_accounts"));	//操作员
	if (maxupstreamrate == null || maxupstreamrate.equals("null"))
        maxupstreamrate = "";
	if (oper_accounts == null || oper_accounts.equals("null"))
        oper_accounts = "";
}else{
	office_id 		= "";	//局向
	zone_id 		= "";	//小区
	adslbindphone 		= "";	//Adsl绑定电话

	dslam_ban_old_devicecode= "";	//旧接入设备编码
	dslam_ban_old_ip 	= "";	//旧接入设备IP
	dslam_ban_old_shelf 	= "";	//旧接入设备机架号
	dslam_ban_old_frame 	= "";	//旧接入设备框号
	dslam_ban_old_slot 	= "";	//旧接入设备槽位号
	dslam_ban_old_port 	= "";	//旧接入设备端口	
	
	dslam_ban_devicecode	= "";	//新接入设备编码
	dslam_ban_ip 		= "";	//新接入设备IP
	dslam_ban_shelf 	= "";	//新接入设备机架号
	dslam_ban_frame 	= "";	//新接入设备框号
	dslam_ban_slot 		= "";	//新接入设备槽位号
	dslam_ban_port 		= "";	//新接入设备端口

	maxdownstreamrate 	= "";	//带宽	
	maxupstreamrate		= "";	//上行速率
	oper_accounts		= "";	//操作员
}

String city_name = null;
String office_name = null;
String zone_name = null;

if(field1 != null){
    String sql1 = "select * from tab_city where city_id='"+ ((city_id.length() == 0)?"-1":city_id)+"'";
    // teledb
    if (DBUtil.GetDB() == 3) {
        sql1 = "select city_name from tab_city where city_id='"+ ((city_id.length() == 0)?"-1":city_id)+"'";
    }
    com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql1);
    psql.getSQL();
	Map field2 = DataSetBean.getRecord(sql1);
	city_name = (field2 != null)?(String.valueOf(field2.get("city_name"))):"";

    String sql2 = "select * from tab_office where office_id='"+ office_id +"'";
    // teledb
    if (DBUtil.GetDB() == 3) {
        sql2 = "select office_name from tab_office where office_id='"+ office_id +"'";
    }
    com.linkage.commons.db.PrepareSQL psql2 = new com.linkage.commons.db.PrepareSQL(sql2);
    psql2.getSQL();
	field2 = DataSetBean.getRecord(sql2);
	office_name = (field2 != null)?(String.valueOf(field2.get("office_name"))):"";

    String sql3 = "select * from tab_zone where zone_id='"+ zone_id +"'";
    // teledb
    if (DBUtil.GetDB() == 3) {
        sql3 = "select zone_name from tab_zone where zone_id='"+ zone_id +"'";
    }
    com.linkage.commons.db.PrepareSQL psql3 = new com.linkage.commons.db.PrepareSQL(sql3);
    psql3.getSQL();
	field2 = DataSetBean.getRecord(sql3);
	zone_name = (field2 != null)?(String.valueOf(field2.get("zone_name"))):"";
}else{
	city_name = "";
	office_name = "";
	zone_name = "";
}

productTypeMap.clear();
servTypeMap.clear();
%>
<%@ include file="../head.jsp"%>
<HTML>
<BODY>
<TABLE width="95%" height="30"  border="0" cellpadding="0" cellspacing="0" class="green_gargtd" align="center">
<TR>
  <TD width="162" align="center" class="title_bigwhite"> 工单详细信息</TD>
</TR>
</TABLE>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD>
	<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
			<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
              <TR> 
                <td class=column width="19%">用户帐号</td>
                <td class=column width="28%"><%=username%>&nbsp;</td>
                <td class=column width="22%">Adsl绑定电话</td>
                <td class=column width="31%"><%=adslbindphone%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">属地</td>
                <td class=column width="28%"><%=city_name%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">局向</td>
                <td class=column width="28%"><%=office_name%>&nbsp;</td>
                <td class=column width="22%">小区</td>
                <td class=column width="31%"><%=zone_name%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=green_foot colspan="4">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">旧设备编码</td>
                <td class=column width="28%"><%=com.linkage.litms.common.util.CommonMap.getDeviceIdExByDeviceId(dslam_ban_old_devicecode)== ""?dslam_ban_old_devicecode:com.linkage.litms.common.util.CommonMap.getDeviceIdExByDeviceId(dslam_ban_old_devicecode)%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>			  
              <TR> 
                <td class=column width="19%">旧设备IP</td>
                <td class=column width="28%"><%=dslam_ban_old_ip%>&nbsp;</td>
                <td class=column width="22%">旧设备机架号</td>
                <td class=column width="31%"><%=dslam_ban_old_shelf%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">旧设备框号</td>
                <td class=column width="28%"><%=dslam_ban_old_frame%>&nbsp;</td>
                <td class=column width="22%">旧设备槽位号</td>
                <td class=column width="31%"><%=dslam_ban_old_slot%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">旧设备端口</td>
                <td class=column width="28%"><%=dslam_ban_old_port%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>
              <%if(null!=DataSetBean.getRecord("select * from tab_deviceresource where device_id='"+dslam_ban_old_devicecode+"'")) {%>
              <TR> 
                <td class=blue_foot colspan="4" align="right"><A HREF=../Resource/UpdateDeviceForm.jsp?device_id=<%=dslam_ban_old_devicecode %>>编辑旧设备</A></td>
              </TR>
             <%} %>
              <TR> 
                <td class=column width="19%">新设备编码</td>
                <td class=column width="28%"><%=com.linkage.litms.common.util.CommonMap.getDeviceIdExByDeviceId(dslam_ban_devicecode)==""?dslam_ban_devicecode:com.linkage.litms.common.util.CommonMap.getDeviceIdExByDeviceId(dslam_ban_devicecode)%>&nbsp;</td>
				<%
				String portcode=" ";
				boolean isSZ_b = SelectCityFilter.isSZ(curUser.getCityId());
				if(isSZ_b == true && (field.get("producttype").equals("3")||field.get("producttype").equals("31"))){
						portcode=String.valueOf(field1.get("deviceencode"));
				%>
                <td class=column width="22%">端口编码</td>
                <td class=column width="31%"><%=portcode%></td>
				<%}
				else{%>
				<td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
				<%}%>
              </TR>			  
              <TR> 
                <td class=column width="19%">新设备IP</td>
                <td class=column width="28%"><%=dslam_ban_ip%>&nbsp;</td>
                <td class=column width="22%">新设备机架号</td>
                <td class=column width="31%"><%=dslam_ban_shelf%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">新设备框号</td>
                <td class=column width="28%"><%=dslam_ban_frame%>&nbsp;</td>
                <td class=column width="22%">新设备槽位号</td>
                <td class=column width="31%"><%=dslam_ban_slot%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">新设备端口</td>
                <td class=column width="28%"><%=dslam_ban_port%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">最大下行速率</td>
                <td class=column width="28%"><%=maxdownstreamrate%>&nbsp;</td>
                <td class=column width="22%">最大上行速率</td>
                <td class=column width="31%"><%=maxupstreamrate%>&nbsp;</td>
              </TR>
              <%if(null!=DataSetBean.getRecord("select * from tab_deviceresource where device_id='"+dslam_ban_devicecode+"'")) { %>
              <TR> 
                <td class=blue_foot colspan="4" align="right"><A HREF=../Resource/UpdateDeviceForm.jsp?device_id=<%=dslam_ban_devicecode %>>编辑新设备</A></td>
              </TR>
              <%} %>
              <TR> 
                <td class=column width="19%">来自何处</td>
                <td class=column width="28%"><%=worksheet_source%>&nbsp;</td>
                <td class=column width="22%">97属地标识</td>
                <td class=column width="31%"><%=system_id%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">97工单唯一标识</td>
                <td class=column width="28%"><%=sheet_id%>&nbsp;</td>
                <td class=column width="22%">业务唯一标识</td>
                <td class=column width="31%"><%=product_id%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">工单接收时间</td>
                <td class=column width="28%"><%=worksheet_receive_time%>&nbsp;</td>
                <td class=column width="22%">业务类型</td>
                <td class=column width="31%"><%=producttype%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">工单执行次数</td>
                <td class=column width="28%"><%=worksheet_exec_time%>&nbsp;</td>
                <td class=column width="22%">操作类型</td>
                <td class=column width="31%"><%=servtype%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">本次开始执行时间</td>
                <td class=column width="28%"><%=worksheet_start_time%>&nbsp;</td>
                <td class=column width="22%">本次执行完成时间</td>
                <td class=column width="31%"><%=worksheet_end_time%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">处理优先级</td>
                <td class=column width="28%"><%=worksheet_priority%>&nbsp;</td>
                <td class=column width="22%">工单描述</td>
                <td class=column width="31%"><%=worksheet_desc%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">操作员</td>
                <td class=column width="28%"><%=oper_accounts%>&nbsp;</td>
                <td class=column width="22%">&nbsp;</td>
                <td class=column width="31%">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">工单执行结果描述</td>
                <td class=column colspan="3"><%=worksheet_error_desc%>&nbsp;</td>
              </TR>
              <TR> 
                <TD colspan=7 class=green_foot align=right> 
                  <INPUT TYPE="button" value=" 关 闭 " onclick="javascript:window.close();" class=jianbian>
                </TD>
              </TR>
            </TABLE>
		  </TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
</BODY>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.netcutover.SearchSheet"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
</HTML>