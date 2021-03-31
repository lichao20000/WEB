<%@ page contentType="text/html;charset=GBK"%>
<%@ page
	import="com.linkage.litms.common.database.Cursor,
	com.linkage.litms.resource.*,
	com.linkage.litms.common.util.DateTimeUtil,
	java.util.List,
	java.util.Iterator"%>
<%@page import="com.linkage.litms.common.util.DateTimeUtil"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<jsp:useBean id="EGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.EGWUserInfoAct" />
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<%@page import="com.linkage.module.gwms.util.StringUtil"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ include file="../timelater.jsp"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
function DetailDevice(device_id){
	var strpage = "DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

</SCRIPT>

<%
	String user_id = request.getParameter("user_id");
	//��õ�ǰ�û��Ļ�����Ϣ
	Cursor cursor = EGWUserInfoAct.getUserRelatedBaseInfo(request);
	//�󶨷�ʽ
	Map<String, String> bindTypeMap = HGWUserInfoAct.getBindType();
	Map fields = cursor.getNext();
	
	/**
	 * �� �� �ˣ���ѧ��
	 * �޸����ڣ�2009-10-12
	 * �޸�ԭ�������±�Ľ�����hgwcust_serv_info�����˱�Ϊҵ���û���
	 */

//	//��ÿ���ҵ��
//	List servList = EGWUserInfoAct.getServiceInfo(user_id);
//	//����ѿ�ͨ��ҵ��
//	List curServList = EGWUserInfoAct.getUserCurRelatedSerInfo(user_id);
//	//���ҵ������Ϣ
//	Cursor cursor_all_chg_ser = HGWUserInfoAct.getUserRelatedSerInfo(user_id, true);
//	Map fields_all_chg_ser = cursor_all_chg_ser.getNext();
//	//����豸�����Ϣ
//	Cursor cursor_all_chg_dev = HGWUserInfoAct.getUserRelatedDevInfo(user_id);	
//	Map fields_all_chg_dev = cursor_all_chg_dev.getNext();
//	//����û��Ĺ�����
//	Cursor cursor_area = HGWUserInfoAct.getEGWUserArea(user_id);
//	Map fields_area = cursor_area.getNext();

	//��ѯ�Ƿ���ڸ��豸��ҵ��
	Cursor businessCursor = EGWUserInfoAct.getEgwServInfo(user_id);
	Map bMap = businessCursor.getNext();
	String wan = "";
	String msg_info = "δ֪";
	boolean flg = false;
	if(null != bMap){
		//��������
		wan = (String) bMap.get("wan_type");
		if ("1".equals(wan)) {
			msg_info = "�Ž�";
		} else if ("2".equals(wan)) {
			msg_info = "·��";
		} else if ("3".equals(wan)) {
			msg_info = "��̬IP";
			flg = true;
		} else if ("4".equals(wan)) {
			msg_info = "DHCP";
		}
	}

	
	//��ȡ���û��ն���Ϣ
	Cursor cursor_device_type = EGWUserInfoAct.getCustDeviceType(user_id);
	Map fields_device_type = cursor_device_type.getNext();
	//��ȡ���û��ײ���Ϣ
	Cursor cursor_package = EGWUserInfoAct.getCustPackag(user_id);
	Map fields_package = cursor_package.getNext();
	
	//�ն�����
	String type_name = "";
	//�ײ�����
	String serv_package_name = "";
	if(null != fields_device_type){
		type_name = String.valueOf(fields_device_type.get("type_name"));
	}
	if(null != fields_package){
		serv_package_name = String.valueOf(fields_package.get("serv_package_name")).toString();
	}

	String ipaddress = (String) fields.get("ipaddress".toLowerCase());
	String ipmask = (String) fields.get("ipmask".toLowerCase());
	String gateway = (String) fields.get("gateway".toLowerCase());
	String adsl_ser = (String) fields.get("adsl_ser".toLowerCase());
	
	//��������
	String wan_type = (String) fields.get("wan_type");
	String wan_type_msg = "δ֪";
	if ("1".equals(wan_type)) {
		wan_type_msg = "�Ž�";
	} else if ("2".equals(wan_type)) {
		wan_type_msg = "·��";
	} else if ("3".equals(wan_type)) {
		wan_type_msg = "��̬IP";
		flg = true;
	} else if ("4".equals(wan_type)) {
		wan_type_msg = "DHCP";
	}
	//����ģʽ
	String work_model = (String) fields.get("work_model");
	String work_model_msg = "δ֪";
	if ("1".equals(work_model)) {
		work_model_msg = "��";
	} else if ("2".equals(work_model)) {
		work_model_msg = "��";
	} else if ("3".equals(work_model)) {
		work_model_msg = "���ؾ���";
	}
	String access_style_id = (String) fields.get("access_style_id");
	String typeKey = "";
	String typeValue = "";
	if (null != access_style_id && !access_style_id.isEmpty()) {
		switch (Integer.parseInt(access_style_id)) {
		case 2:
	access_style_id = "��ͨLAN";
	typeKey = "pvc";
	typeValue = (String) fields.get("vpiid") + "/" + (String) fields.get("vciid");
	break;
		case 3:
	access_style_id = "��ͨ����";
	typeKey = "pvc";
	typeValue = (String) fields.get("vpiid") + "/" + (String) fields.get("vciid");
	break;
		case 4:
	access_style_id = "ר��LAN";
	typeKey = "vlanid";
	typeValue = (String) fields.get("vlanid");
	break;
		case 5:
	access_style_id = "ר�߹���";
	typeKey = "vlanid";
	typeValue = (String) fields.get("vlanid");
	break;
		default:
	access_style_id = "ADSL";
	typeKey = "pvc";
	typeValue = (String) fields.get("vpiid") + "/" + (String) fields.get("vciid");
		}
	}
	
	String customer_id = (String) fields.get("customer_id");
/*********************�ͻ���Ϣ********************************/
	String customer_name = "";
	String customer_type = "";
	String customer_size = "";
	String customer_address = "";
	String linkman = "";
	String linkphone = "";
	String email = "";
	String city = "";
	String office = "";
	String zone = "";
	Map map = com.linkage.litms.resource.EGWCustomerAct.getInfoById(customer_id);
	if(null != map && !map.isEmpty()){
		customer_name = (String)map.get("customer_name");
		customer_type = (String)map.get("customer_type");
		customer_size = (String)map.get("customer_size");
		customer_address = (String)map.get("customer_address");
		linkman = (String)map.get("linkman");
		linkphone = (String)map.get("linkphone");
		email = (String)map.get("email");
		city = (String)CityDAO.getCityIdCityNameMap().get((String)map.get("city_id"));
		office = (String)OfficeAct.getOfficeMap().get((String)map.get("office_id"));
		if(null==office){
			office = "";
		}
		zone = (String)ZoneAct.getOfficeMap().get((String)map.get("zone_id"));
		if(null==zone){
			zone = "";
		}
	}
/***********************************************************/
%>
<TABLE border=0 align="center" cellspacing=0 cellpadding=0 width="95%" id="myTable">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor="#999999">

				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">
						�û�������Ϣ</TH>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD width="15%" class=column align="left">�û��ʻ�</TD>
						<TD width="35%"><%=(String) fields.get("username".toLowerCase())%>
						<TD width="15%" class=column align="left">�û�״̬</TD>
						<TD width="35%">
						<%
							String user_state = "";
							if ("0".equals((String) fields.get("user_state"))) {
								user_state = "ɾ���û�";
							} else if ("1".equals((String) fields.get("user_state"))) {
								user_state = "����";
							} else if ("2".equals((String) fields.get("user_state"))) {
								user_state = "��ͣ";
							} else if ("3".equals((String) fields.get("user_state"))) {
								user_state = "����";
							} else if ("4".equals((String) fields.get("user_state"))) {
								user_state = "�����豸";
							} else {
								user_state = "-";
							}
						%> <%=user_state%></TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">��������</td>
						<td><%=msg_info%></td>
						<td class=column align="left">����ģʽ</td>
						<td ><%=work_model_msg%></td>
					</tr>
					<%if(flg){%>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">IP��ַ</td>
						<td><%=ipaddress%></td>
						<td class=column align="left">����</td>
						<td><%=ipmask%></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">����</td>
						<td><%=gateway%></td>
						<td class=column align="left">DNS��ַ</td>
						<td><%=adsl_ser%></td>
					</tr>
					<%}%>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">���LAN����</td>
						<td><%=(String) fields.get("lan_num")%>������</td>
						<td class=column align="left">SSID����</td>
						<td><%=(String) fields.get("ssid_num")%>������</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">��������</td>
						<td><%=(String) fields.get("upwidth")%>&nbsp;(bps)</td>
						<td class=column align="left">��������</td>
						<td><%=(String) fields.get("maxattdnrate")%>&nbsp;(bps)</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">��������</td>
						<td ><%=access_style_id%></td>
						<td class=column align="left"><%= typeKey%></td>
						<td ><%=typeValue%></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">�ն�����</td>
						<td ><%=type_name%></td>
						<td class=column align="left">�ײ�����</td>
						<td ><%=serv_package_name%></td>
					</tr>
					
					</TABLE>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD height="20" colspan="4"></TD>
			</TR>
			<TR>
				<TD bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">
						�ͻ���Ϣ</TH>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td width="15%" class=column align="left">�ͻ�����</td>
						<td width="35%">
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
						    	<input type="password" value="<%=customer_name%>" readonly="true" style="border:none"/>
							<%}else{%>
						   		<%=customer_name%>
						  	<%} %>
						</td>
						<td width="15%" class=column align="left">�ͻ�����</td>
						<td width="35%" ><%=customer_type%></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">�ͻ���ģ</td>
						<td><%=customer_size%></td>
						<td class=column align="left">�ͻ���ַ</td>
						<td>
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
						    	<input type="password" value="<%=customer_address%>" readonly="true" style="border:none"/>
							<%}else{%>
						   		<%=customer_address%>
						  	<%} %>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">��ϵ��</td>
						<td>
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
						    	<input type="password" value="<%=linkman%>" readonly="true" style="border:none"/>
							<%}else{%>
						   		<%=linkman%>
						  	<%} %>
						</td>
						<td class=column align="left">��ϵ�绰</td>
						<td>
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
						    	<input type="password" value="<%=linkphone%>" readonly="true" style="border:none"/>
							<%}else{%>
						   		<%=linkphone%>
						  	<%} %>
						</td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">E-mail:</td>
						<td>
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))) {%>
						    	<input type="password" value="<%=email%>" readonly="true" style="border:none"/>
							<%}else{%>
						   		<%=email%>
						  	<%} %>
						</td>
						<td class=column align="left">�� ��</td>
						<td><%=city%></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td class=column align="left">�� ��</td>
						<td><%=office%></td>
						<td class=column align="left">С ��</td>
						<td><%=zone%></td>
					</tr>
					
					
					</TABLE>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD height="20" colspan="4"></TD>
			</TR>
			<TR>
				<TD bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					
					<TR>
						<TH colspan="4" align="center">�豸��Ϣ</TH>
					</TR>
					<%String oui = (String) fields.get("oui".toLowerCase());
						oui = ("null".equals(oui))? "":oui;
						String device_serialnumber = (String) fields.get("device_serialnumber".toLowerCase());
						device_serialnumber = ("null".equals(device_serialnumber))? "":device_serialnumber;
					%>
					
					<% if("".equals(device_serialnumber)){%>
					 
						<TR bgcolor="#FFFFFF">
						<TD colspan="4">��ǰû�а��κ��豸��</TD>
						</TR>
					<%
					}else{
					%>
						<TR bgcolor="#FFFFFF">
							<TD width="15%" class=column align="left">��ǰ�󶨵��豸</TD>
							<TD width="35%">
							<a onclick="DetailDevice('<%=((String) fields.get("device_id"))%>')" href="javascript:">
							<%=( oui + "-" + device_serialnumber )%>&nbsp;
							</a>
							</TD>
							<TD width="15%" class=column align="left">��ʱ��</TD>
							<TD width="35%">
							<%
				  		String binddate = "";
				  		if(!(fields.get("binddate").equals("") && fields.get("binddate") != null)){
				  		DateTimeUtil dt = new DateTimeUtil(Long.parseLong((String)fields.get("binddate"))*1000);
						binddate = dt.getLongDate();
						}
				  
				   %>					  
                   <%=binddate%>
							</TD>
						</TR>
						<tr bgcolor="#FFFFFF">
							<TD width="15%" class=column align="left">�󶨷�ʽ</TD>
							<TD width="35%">
								<%
									String bindtype = "";
									String userline = (String) fields.get("userline");
									bindtype = bindTypeMap.get(userline);
									if (false == StringUtil.IsEmpty(bindtype)){
										
									}else{
										bindtype = "-";
									}
									%>
									<%=bindtype%>
							</TD>
							<TD width="15%" class=column align="left">����֤</TD>
							<TD width="35%">
								<%
									String is_chk_bind = "";
									if ("1".equals((String) fields.get("is_chk_bind"))){
										is_chk_bind = "�Զ�����֤�ذ�ɹ�&nbsp;<IMG SRC='../images/check.gif' BORDER='0' ALT='��֤�ɹ�' style='cursor:hand'>";
									}else if ("2".equals((String) fields.get("is_chk_bind"))){
										is_chk_bind = "�Զ�����֤�ɹ�&nbsp;<IMG SRC='../images/check.gif' BORDER='0' ALT='��֤�ɹ�' style='cursor:hand'>";
									}else{
										is_chk_bind = "δ��֤&nbsp;<IMG SRC='../images/button_s.gif' BORDER='0' ALT='δ��֤' style='cursor:hand'>";
									}
									%>
									<%=is_chk_bind%>
							</TD>
						</tr>
					<%
					}
					%>
					</TABLE>
				</TD>
			</TR>
			<TR bgcolor="#FFFFFF">
				<TD height="20" colspan="4"></TD>
			</TR>
			<TR>
				<TD bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center">ҵ����Ϣ</TH>
					</TR>
					<% 
						if(null==bMap){
					%>
					<TR bgcolor="#FFFFFF">
						<TD colspan="4">��ǰû��ʹ���κ�ҵ��</TD>
					</TR>
					<%
						}else{
							int i = 1;
							while(null != bMap) {
								
					%>
					<TR bgcolor="#FFFFFF">
						<td width="15%" class=column align="center">���</td>
						<td class=column align="center">ҵ������</td>
						<td class=column align="center">ҵ��ͨ״̬</td>
						<td class=column align="center">��ǰʵ��״̬</td>
					</TR>
					

					<TR bgcolor="#FFFFFF">
						<td align="center"><%=i%></td>
						<td align="center"><%=bMap.get("serv_type_name")%>&nbsp;</td>
						<% 
							String bServStatus = String.valueOf(bMap.get("serv_status")); 
							if("1".equals(bServStatus)){
								bServStatus = "��ͨ";
							}else{
								bServStatus = "��ͣ";
							}
						%>
						<td align="center"><%=bServStatus%></td>
						<% 
							String bOpenStatus = String.valueOf(bMap.get("open_status")); 
							if("1".equals(bOpenStatus)){
								bOpenStatus = "����";
							}else{
								bOpenStatus = "�ر�";
							}
						%>
						<td align="center"><%=bOpenStatus%></td>
					</TR>
							
					<%		
								bMap = businessCursor.getNext();
								i++;
							}
						}
					%>
					<TR>
						<TD colspan="4" align="right" class=green_foot><INPUT TYPE="button"
							value=" �� �� " class=jianbian onclick="javascript:window.close()">
						&nbsp;&nbsp;</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
