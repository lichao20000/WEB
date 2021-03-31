<%--
Author		: lizhaojun
Date		: 2007-4-27
Note		:
Modifiy		: 
--%>

<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%

request.setCharacterEncoding("GBK");


Map gatherMap = (Map)com.linkage.litms.common.util.CommonMap.getGatherMap();

Map cityMap = com.linkage.litms.common.util.CommonMap.getCityMap();


Map fields = sheetManage.getSheetDetail(request);
String tmp="";
String sheet_id = "";
String city_id = "";
String gather_id = "";
String device_id = "";
String exec_status = "";
String exec_desc = "";
String fault_code = "";
String receive_time = "";
String fault_desc = "";
String start_time = "";
String end_time = "";
String service_name = "";
String sheet_source = "";
String username = "";

if(fields!= null){
	sheet_id = (String)fields.get("sheet_id");
	gather_id =(String)fields.get("gather_id");
	device_id =(String)fields.get("device_id");
	city_id = (String)fields.get("city_id");
	exec_status =(String)fields.get("exec_status");
	username = (String)fields.get("username");
	exec_desc = (String)fields.get("exec_desc");
	if(exec_status.equals("0")){
		exec_status="正在执行";
	}else{
		exec_status = "执行完成";
	}
	tmp = (String)fields.get("fault_code");
    if(tmp.equals("1")){
    	tmp = "执行成功";
    }else if(tmp.equals("-1")){
    	tmp = "连接不上";
    }else if(tmp.equals("-2")){
    	tmp = "连接超时";
    }else if(tmp.equals("-3")){
    	tmp = "没有工单";
    }else if(tmp.equals("-4")){
     	tmp = "没有设备";                   
    }else if(tmp.equals("-5")){
     	tmp = "没有模板";                                         
    }else if(tmp.equals("-6")){
     	tmp = "设备正忙";                       
    }else if(tmp.equals("-7")){
    	tmp = "参数错误";   
    } 
	fault_code = (String)fields.get("fault_code");
	fault_desc =(String)fields.get("fault_desc");
	receive_time = (String)fields.get("receive_time");
	
	if(receive_time != null && !receive_time.equals("")){
		long l_receive_time = Long.parseLong(receive_time);
		receive_time = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",l_receive_time);
	}
	
	
	start_time =(String)fields.get("start_time");
	if(start_time!=null && !start_time.equals("")){
		long l_start_time = Long.parseLong(start_time);
		start_time = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",l_start_time);
	}
	
	end_time = (String)fields.get("end_time");
	if(end_time!= null && !end_time.equals("")){
		long l_end_time = Long.parseLong(end_time);
		end_time = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",l_end_time);
	}
	service_name = (String)fields.get("service_name");
	sheet_source = (String)fields.get("sheet_source");
	if(sheet_source.equals("0")){
		sheet_source = "界面工单";
	}else if(sheet_source.equals("1")){
		sheet_source = "BSS工单";
	} else {
		sheet_source = "FTP工单";
	}
}

String _oui = "";
String _serialnumber = "";

String _strSql = "select oui,device_serialnumber from tab_gw_device where device_id='" + device_id + "'";
Map _tmpField = DataSetBean.getRecord(_strSql);
if(_tmpField != null ){
	_oui = (String)_tmpField.get("oui");
	_serialnumber = (String)_tmpField.get("device_serialnumber");
}

String office_id = "";
String zone_id = "";
String office_name = "";
String zone_name = "";

String phonenumber = "";

Map fields1 = sheetManage.getHgwDetail(device_id);
if(fields1 != null){
	phonenumber = (String)fields1.get("phonenumber");	
	office_id = (String)fields1.get("office_id");
	zone_id = (String)fields1.get("zone_id");
}

Map field2 = null;
if(office_id != null && !office_id.equals("")){
    String sql1 = "select * from tab_office where office_id='"+ office_id +"'";
    // teledb
    if (DBUtil.GetDB() == 3) {
        sql1 = "select office_name from tab_office where office_id='"+ office_id +"'";
    }
    com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql1);
    psql.getSQL();
	field2 = DataSetBean.getRecord(sql1);
	office_name = (field2 != null)?(String.valueOf(field2.get("office_name"))):"";
}
if(zone_id !=null && !zone_id.equals("")){
    String sql2 = "select * from tab_zone where zone_id='"+ zone_id +"'";
    // teledb
    if (DBUtil.GetDB() == 3) {
        sql2 = "select zone_name from tab_zone where zone_id='"+ zone_id +"'";
    }
    com.linkage.commons.db.PrepareSQL psql2 = new com.linkage.commons.db.PrepareSQL(sql2);
    psql2.getSQL();
	field2 = DataSetBean.getRecord(sql2);
	zone_name = (field2 != null)?(String.valueOf(field2.get("zone_name"))):"";
}
%>
<%@ include file="../head.jsp"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
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
                <td class=column width="19%" nowrap>用户账户</td>
                <td class=column width="28%"><%=username%>&nbsp;</td>
                <td class=column width="22%">Adsl绑定电话</td>
                <td class=column width="31%"><%=phonenumber%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">属地</td>
                <td class=column width="28%" colspan="3"><%=cityMap.get(city_id)%>&nbsp;</td>
              </TR>
              <TR>
                <td class=column width="22%">局向</td>
                <td class=column width="31%"><%=office_name%>&nbsp;</td>
                <td class=column width="22%">小区</td>
                <td class=column width="31%"><%=zone_name%>&nbsp;</td>
              </TR>
               <TR> 
                <td class=green_foot colspan="4">&nbsp;</td>
              </TR>                          			
              <TR> 
                <td class=column width="19%">工单编号</td>
                <td class=column width="28%"><%=sheet_id%>&nbsp;</td>
                <td class=column width="22%">采集点</td>
                <td class=column width="31%"><%=gatherMap.get(gather_id)%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">业务名称</td>
                <td class=column width="28%"><%=service_name%>&nbsp;</td>
                <td class=column width="22%">工单来源</td>
                <td class=column width="31%"><%=sheet_source%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">设备序列号</td>
                <td class=column colspan="3"><%=_oui%> - <%=_serialnumber%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=green_foot colspan="4">&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="">执行状态</td>
                <td class=column width="" colspan="3"><%=exec_status%>&nbsp;</td>
              </TR>
              <TR> 
                <td class=column width="19%">执行结果</td>
                <td class=column width="28%"><%=tmp%>&nbsp;</td>
                <td class=column width="19%">接收时间</td>
                <td class=column width="28%"><%=receive_time%>&nbsp;</td>
              </TR>
              <TR>
              	 <td class=column width="" nowrap>执行结果描述</td>
                 <td class=column colspan="3" width="">
                	 <textarea name="exec_desc" class="jive-description" rows="4" cols="50"><%=exec_desc%></textarea>
                 </td>
              </TR>
              <TR> 
                <td class=column width="19%">开始时间</td>
                <td class=column width="28%"><%=start_time%>&nbsp;</td>
                <td class=column width="22%">结束时间</td>
                <td class=column width="31%" nowrap><%=end_time%>&nbsp;</td>
              </TR>              
              <TR> 
                <td class=column width="19%">失败原因描述</td>
                <td class=column colspan="3" width="">
                	 <textarea name="fault_desc" class="jive-description" rows="4" cols="50"><%=fault_desc%></textarea>
                </td>
              </TR>			  

              <TR> 
                <TD colspan=7 class="green_foot" align=right> 
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
</HTML>