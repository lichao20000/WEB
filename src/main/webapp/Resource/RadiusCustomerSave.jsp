<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@page import="com.linkage.litms.common.util.StringUtils"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct"/>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");

String strSQL="";
String arrAddressSQL = null;
String strMsg=null;
String strAction = request.getParameter("action");
ArrayList Str_sql=new ArrayList();
if(strAction.equals("delete")){	//删除操作
	String str_userid = request.getParameter("user_id");
	String username = request.getParameter("username");
	strSQL = "delete from cus_radiuscustomer where user_id="+ str_userid+" and username = '" + username + "'";
	Str_sql.add(strSQL);
}
else{
	String str_username = request.getParameter("username");
	String str_passwd = request.getParameter("passwd");

	String str_basdevice_id = request.getParameter("basdevice_id");

	if(str_basdevice_id.equals("0")){
		str_basdevice_id = "null";
	}
	String str_basdevice_slot = request.getParameter("basdevice_slot");
	String str_basdevice_port = request.getParameter("basdevice_port");
	String str_vlanid = request.getParameter("vlanid");
	//modify by 2004-1-8
	String str_vpiid = request.getParameter("vpiid");
	String str_vciid = request.getParameter("vciid");
	String str_adsl_hl = request.getParameter("adsl_hl");
	String str_userline = request.getParameter("userline");
	//end
	//modify by 2004-6-30
	String str_basdevice_shelf = request.getParameter("basdevice_shelf");
	String str_basdevice_frame = request.getParameter("basdevice_frame");
	String str_device_frame = request.getParameter("device_frame");
	//end
	String str_hidipaddress = request.getParameter("ipaddress");
	String str_device_ip = null;
	String str_ipaddress = request.getParameter("ipaddress");
	String str_macaddress = request.getParameter("macaddress");
	String str_device_slot = request.getParameter("device_slot");
	String str_device_port = request.getParameter("device_port");
	String str_workid = request.getParameter("workid");
	String str_cotno = request.getParameter("cotno");
	String str_service_set = request.getParameter("service_set");
	String str_realname = request.getParameter("realname");
	String str_sex = request.getParameter("sex");
	String str_address = request.getParameter("address");
	String str_city_id = request.getParameter("city_id");
	String str_office_id = request.getParameter("office_id");
	String str_zone_id = request.getParameter("zone_id");
	String str_vipcardno = request.getParameter("vipcardno");
	String str_contractno = request.getParameter("contractno");
	String str_linkman = request.getParameter("linkman");
	String str_adsl_card = request.getParameter("adsl_card");
	String str_adsl_dev = request.getParameter("adsl_dev");
	String str_adsl_ser = request.getParameter("adsl_ser");
	String str_isrepair = request.getParameter("isrepair");
	String str_linkman_credno = request.getParameter("linkman_credno");
	String str_linkphone = request.getParameter("linkphone");
	String str_agent = request.getParameter("agent");
	String str_agent_credno = request.getParameter("agent_credno");
	String str_agentphone = request.getParameter("agentphone");
	String str_bandwidth = request.getParameter("bandwidth");
	String str_opendate = request.getParameter("hidopendate");
	String str_onlinedate = request.getParameter("hidonlinedate");
	String str_pausedate = request.getParameter("hidpausedate");
	String str_closedate = request.getParameter("hidclosedate");
	String str_updatetime = request.getParameter("hidupdatetime");
	
	
	String str_staff_id = request.getParameter("staff_id");
	String str_remark = request.getParameter("remark");
	String str_phonenumber = request.getParameter("phonenumber");
	String str_cableid = request.getParameter("cableid");
	String str_bwlevel = request.getParameter("bwlevel");
	String str_device_shelf = request.getParameter("device_shelf");
	
	String str_device_id = request.getParameter("device_id");
	
	String str_basdevice_ip = null;
	String str_gather_id = null;
	//out.println(str_device_shelf);
	
	if(str_bwlevel != null && str_bwlevel.length()>0){
		double d = Double.parseDouble(str_bwlevel)/100;
		//double d = (double)(tmp/100);
		str_bwlevel = Double.toString(d);
		str_bwlevel = StringUtils.formatNumber(str_bwlevel,3);
	}

	if(strAction.equals("add")){
		strSQL = "select username from cus_radiuscustomer where username='"+str_username+"'";
		Map fields = DataSetBean.getRecord(strSQL);
		if(fields != null){
			//strSQL = "";
			strMsg = "该ADSL客户已经存在！";
		}
		else{
			Map field = DeviceAct.getDeviceInfoMap(str_device_id);
			
			
			str_device_ip = (!field.get("loopback_ip").equals(""))?(String.valueOf(field.get("loopback_ip"))):"";
			str_gather_id = (field != null)?(String.valueOf(field.get("gather_id"))):"";
			
			field = DeviceAct.getDeviceInfoMap(str_basdevice_id);
			str_basdevice_ip = (field!=null&&!field.get("loopback_ip").equals(""))?(String.valueOf(field.get("loopback_ip"))):"";
			//str_gather_id = DeviceAct.getGatherIdOfDeviceId(str_device_id);
			//取得新的userid
			long userid = DataSetBean.getMaxId("cus_radiuscustomer","user_id");
			String str_userid = String.valueOf(userid);
			
			strSQL = "insert into cus_radiuscustomer (user_id,username,passwd,ipaddress,macaddress,basdevice_id,basdevice_ip,basdevice_slot,basdevice_port,vlanid,device_ip,device_slot,device_port,workid,cotno,service_set,realname,sex,address,city_id,office_id,zone_id,vipcardno,contractno,linkman,adsl_card,adsl_dev,adsl_ser,isrepair,linkman_credno,linkphone,agent,agent_credno,agentphone,bandwidth,opendate,onlinedate,pausedate,closedate,updatetime,staff_id,remark,phonenumber,cableid,bwlevel,device_shelf,vpiid,vciid,adsl_hl,userline,basdevice_shelf,basdevice_frame,device_frame,user_state,gather_id,device_id) values ("+ str_userid +",'"+ str_username +"','"+ str_passwd +"','"+ str_ipaddress +"','"+ str_macaddress +"','"+ str_basdevice_id +"','"+ str_basdevice_ip +"',"+ str_basdevice_slot +","+ str_basdevice_port +","+ str_vlanid +",'"+ str_device_ip +"',"+ str_device_slot +","+ str_device_port +",'"+ str_workid +"','"+ str_cotno +"','"+ str_service_set +"','"+ str_realname +"','"+ str_sex +"','"+ str_address +"','"+ str_city_id +"','"+ str_office_id +"','"+ str_zone_id +"','"+ str_vipcardno +"','"+ str_contractno +"','"+ str_linkman +"','"+ str_adsl_card +"','"+ str_adsl_dev +"','"+ str_adsl_ser +"','"+ str_isrepair +"','"+ str_linkman_credno +"','"+ str_linkphone +"','"+ str_agent +"','"+ str_agent_credno +"','"+ str_agentphone +"',"+ str_bandwidth +","+ str_opendate +","+ str_onlinedate +","+ str_pausedate +","+ str_closedate +","+ str_updatetime +",'"+ str_staff_id +"','"+ str_remark +"','"+str_phonenumber+"','"+ str_cableid +"',"+ str_bwlevel +","+ str_device_shelf +",'"+ str_vpiid+"',"+ str_vciid +",'"+ str_adsl_hl +"',"+ str_userline +","+str_basdevice_slot+","+str_basdevice_frame+","+str_device_frame+",'1','"+ str_gather_id +"','"+ str_device_id +"')";
			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",,",",null,");
			strSQL = StringUtils.replace(strSQL,",)",",null)");
			strSQL = StringUtils.replace(strSQL,"'null'","null");
			Str_sql.add(strSQL);
		}
	}
	else{
		String old_username = request.getParameter("old_username");
		
		if(!old_username.equals(str_username)){
			//判断是否已经存在
			strSQL = "select * from cus_radiuscustomer where username='"+str_username+"'";
			// teledb
			if (DBUtil.GetDB() == 3) {
				strSQL = "select username from cus_radiuscustomer where username='"+str_username+"'";
			}
			com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(strSQL);
			psql.getSQL();
			if(DataSetBean.getRecord(strSQL)!=null)
				strMsg = "该ADSL客户已经存在！";
		}
		if(strMsg == null){
			Map field = DeviceAct.getDeviceInfoMap(str_device_id);
			str_device_ip = (!field.get("loopback_ip").equals(""))?(String.valueOf(field.get("loopback_ip"))):"";
			str_gather_id = String.valueOf(field.get("gather_id"));		
		
			field = DeviceAct.getDeviceInfoMap(str_basdevice_id);
			str_basdevice_ip = (field!=null&&!field.get("loopback_ip").equals(""))?(String.valueOf(field.get("loopback_ip"))):"";
		
			String str_userid = request.getParameter("user_id");
			//String user_state = request.getParameter("user_state");
			strSQL = "update cus_radiuscustomer set device_id='"+ str_device_id +"',gather_id='"
					 + str_gather_id +"',username='"
					 + str_username +"',passwd='"+ str_passwd 
					 + "',ipaddress='"+ str_ipaddress +"',macaddress='"
					 + str_macaddress +"',basdevice_id='"+ str_basdevice_id +"',basdevice_ip='"
					 + str_basdevice_ip +"',basdevice_slot="+ str_basdevice_slot 
					 + ",basdevice_port="+ str_basdevice_port +",vlanid="+ str_vlanid 
					 + ",device_ip='"+ str_device_ip +"',device_slot="+ str_device_slot 
					 + ",device_port="+ str_device_port +",workid='"+ str_workid 
					 + "',cotno='"+ str_cotno +"',service_set='"+ str_service_set 
					 + "',realname='"+ str_realname +"',sex='"+ str_sex 
					 + "',address='"+ str_address +"',city_id='"+ str_city_id 
					 + "',office_id='"+ str_office_id +"',zone_id='"+ str_zone_id 
					 + "',vipcardno='"+ str_vipcardno +"',contractno='"+ str_contractno 
					 + "',linkman='"+ str_linkman +"',adsl_card='"+ str_adsl_card 
					 + "',adsl_dev='"+ str_adsl_dev +"',adsl_ser='"+ str_adsl_ser 
					 + "',isrepair='"+ str_isrepair +"',linkman_credno='"+ str_linkman_credno 
					 + "',linkphone='"+ str_linkphone +"',agent='"+ str_agent 
					 + "',agent_credno='"+ str_agent_credno +"',agentphone='"+ str_agentphone 
					 + "',bandwidth="+ str_bandwidth +",opendate="+ str_opendate 
					 + ",onlinedate="+ str_onlinedate +",pausedate="+ str_pausedate 
					 + ",closedate="+ str_closedate +",updatetime="+ str_updatetime 
					 + ",staff_id='"+ str_staff_id +"',remark='"+ str_remark 
					 + "',phonenumber='"+ str_phonenumber +"',cableid='"+ str_cableid 
					 + "',bwlevel="+ str_bwlevel +",device_shelf="+ str_device_shelf 
					 + ",vpiid='"+ str_vpiid +"',vciid="+ str_vciid +",adsl_hl='"+ str_adsl_hl 
					 + "',userline="+ str_userline +",basdevice_shelf="+str_basdevice_shelf
					 + ",basdevice_frame="+str_basdevice_frame+",device_frame="+str_device_frame
					 + ",user_state='"+1+"' where user_id="+ str_userid 
					 + " and username='"+old_username+"'";
			strSQL = StringUtils.replace(strSQL,"=,","=null,");
			strSQL = StringUtils.replace(strSQL,"= where","=null where");
			strSQL = StringUtils.replace(strSQL,"'null'","null");
			Str_sql.add(strSQL);
			strSQL = "update  tab_deviceresource set city_id='"+ str_city_id+"' where device_serialnumber='"+ str_adsl_ser +"'";
			strSQL = StringUtils.replace(strSQL,"=,","=null,");
			strSQL = StringUtils.replace(strSQL,"= where","=null where");
			strSQL = StringUtils.replace(strSQL,"'null'","null");
			Str_sql.add(strSQL);
			
		}
	}
}
if (strMsg == null || strSQL != null) {
	int iCode[] = DataSetBean.doBatch(Str_sql);
	
	if (iCode != null && iCode.length > 0) {
		strMsg = "ADSL客户资源操作成功！";
	}
	else{
		strMsg = "ADSL客户资源操作失败，请返回重试或稍后再试！";
	}
}

%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
function GoList(page){
	this.location = page;
}
//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="50%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH align="center">ADSL客户资源操作提示信息</TH>
					</TR>
						<TR  height="50">
							<TD align=center valign=middle class=column><%out.println(strMsg);%></TD>
						</TR>
					<TR>
						<TD class=foot align="right">
						<INPUT TYPE="button" NAME="cmdJump" onclick="GoList('RadiusCustomerList.jsp')" value=" 列 表 " class=btn>

						<INPUT TYPE="button" NAME="cmdBack" onclick="javascript:history.go(-1);" value=" 返 回 " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>