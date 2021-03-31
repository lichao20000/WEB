<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import ="com.linkage.litms.common.util.DateTimeUtil"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean" %>
<%@ page import="com.linkage.litms.common.database.Cursor" %>
<%@ page import="com.linkage.litms.common.util.FormUtil" %>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<jsp:useBean id="DeviceAct" scope="request" class="com.linkage.litms.resource.DeviceAct" />

<%
String strVendorList = DeviceAct.getVendorList(true, "", "");

String start_time = new DateTimeUtil().getDate();
String end_time = new DateTimeUtil().getDate();

//�ɼ���
String gatherList = DeviceAct.getGatherList(session, "", "", true);

String sql = "select * from tab_alarm_knowledge";
// teledb
if (DBUtil.GetDB() == 3) {
	sql = "select id, name from tab_alarm_knowledge";
}

com.linkage.commons.db.PrepareSQL psql = new com.linkage.commons.db.PrepareSQL(sql);
psql.getSQL();

//�澯����������
Cursor cursor = DataSetBean.getCursor(sql);
String warnList = FormUtil.createListBox(cursor,"id", "name", false, "", "warnID");

%>

<script language="javascript">
function query()
{
	if (document.frm.useTime.checked){
		document.frm.useflag.value = "1";
		
		if(!IsNull(document.frm.start_time.value,"��ʼʱ��")){
			document.frm.start_time.focus();
			document.frm.start_time.select();
			return false;
		}
		else if(!IsNull(document.frm.end_time.value,"��ֹʱ��")){
			document.frm.end_time.focus();
			document.frm.end_time.select();
			return false;
		}
	}
	else{
		document.frm.useflag.value = "0";
	}
	
	document.frm.hidstart.value=document.frm.start_time.value + " " + document.frm.start_ms.value;
	document.frm.hidend.value=document.frm.end_time.value + " " + document.frm.end_ms.value;
	document.frm.submit();
	return true;
	
}

function showChild(param)
{
	var page ="";
	if(param == "gather_id")
	{
		document.all("div_vendor").innerHTML = "<%=strVendorList%>";
	}if(param == "devicetype_id"){
			// document.frm.device.checked = false;
			page = "../paramConfig/showDeviceVersion.jsp?vendor_id="+document.frm.vendor_id.value + "&devicetype_id="+ encodeURIComponent(document.frm.devicetype_id.value);
			document.all("childFrm2").src = page;
		}
	if(param == "softwareversion")
	{		
		//softwareversion�������valueΪ devicetype_id
		//devicetype_id�������valueΪ device_model
		page = "../paramConfig/showDevice.jsp?gather_id="+document.frm.gather_id.value+"&vendor_id="+document.frm.vendor_id.value+"&devicetype_id="+document.frm.softwareversion.value+"&flag=paramInstanceadd_Config&refresh="+Math.random();	
		document.all("childFrm").src = page;
	}
	if(param == "vendor_id")
	{
		page = "../paramConfig/showDevicetype.jsp?vendor_id="+document.frm.vendor_id.value;
		document.all("childFrm1").src = page;	
	}
}

function isUseTime(){
	if (document.frm.useTime.checked){
		document.all("queryTime").style.display = "";
	}
	else{
		document.all("queryTime").style.display = "none";
	}
}

</script>
<%@ include file="../head.jsp"%>
<form name="frm" action="historyWarn.jsp" method="POST">
	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height=20></td>
		</tr>
		<TR>
			<TD colspan="2">
				<TABLE width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							�澯����
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2 bgcolor=#999999>
					<TR><TH colspan="4">��ʷ��ѯ</TH></TR>
					<TR BGCOLOR=#ffffff>
						<TD class=column align=right>�Ƿ�ʹ��ʱ��</TD>
						<TD colspan="3">
							<input type="checkbox" name="useTime" checked onclick="isUseTime()">
							<input type="hidden" name="useflag" value="1">
						</TD>
					</TR>
					<TR BGCOLOR=#ffffff style="display:" id="queryTime">
    					<TD class=column align=right width=13%>��ʼʱ��</TD>
      					<TD nowrap width=37%>
							<input type="text" name="start_time" id="start_time" class=bk value="<%=start_time%>" readonly class="form_kuang">
							<input type="button" value="��" class=btn onClick="showCalendar('day',event)" name="button2">
							<input type="text" name="start_ms" id="start_ms" class=bk value="00:00:00" class="form_kuang" size="8" maxlength="8">
							<input type="hidden" name="hidstart" value="">
							<font color="#FF0000">*</font>
						</TD>
     					<TD class=column align=right width=13%>��ֹʱ��
						</TD>
      					<TD nowrap>
        					<input type="text" name="end_time" id="end_time" class=bk value="<%=end_time%>" readonly class="form_kuang">
							<input type="button" value="��" class=btn onClick="showCalendar('day',event)" name="button2">
							<input type="text" name="end_ms" id="end_ms" class=bk  value="23:59:59" class="form_kuang" size="8" maxlength="8">
							<input type="hidden" name="hidend" value="">
							<font color="#FF0000">*</font>
						</TD>
    				</TR>
    				<tr  bgcolor=#ffffff>
    					<td class=column align=right width=13%>�澯����</td>
    					<td width=37%><%=warnList %></td>
    					<td class=column align=right width=13%>�澯����</td>
    					<td width=37%>
    						<select name="warnType" id="warnType" class=bk>
    							<option value="-1">--��ѡ��--</option>
    							<option value="1">�豸�澯</option>
    							<option value="2">���������澯</option>
    							<option value="3">ͨ�Ÿ澯</option>
    							<option value="4">����ʧ�ܸ澯</option>
    							<option value="5">����ϵͳ�����ĸ澯</option>
    						</select>
    					</td>
    				</tr>
    				<tr  bgcolor=#ffffff>
    					<td class=column align=right>�澯����</td>
    					<td>
    						<select name="warnLevel" id="warnLevel" class=bk>
    							<option value="-1">--��ѡ��--</option>
    							<option value="1">���ظ澯</option>
    							<option value="2">��Ҫ�澯</option>
    							<option value="3">��Ҫ�澯</option>
    							<option value="4">����澯</option>
    							<option value="5">�¼��澯</option>
    							<option value="6">����澯</option>
    						</select>
    					</td>
    					<td class=column align=right>�澯״̬</td>
    					<td>
    						<select name="warnStatus" id="warnStatus" class=bk>
    							<option value="-1">--��ѡ��--</option>
    							<option value="1">δȷ��δ����澯</option>
    							<option value="2">��ȷ��δ����澯</option>
    							<option value="3">δȷ�ϵ�������澯</option>
    							<option value="4">��ȷ�ϲ�������澯</option>
    						</select>
    					</td>
    				</tr>
    				<TR bgcolor="#FFFFFF" id="tr1" STYLE="display:">
						<TD align="right" width="20%" class=column>�ɼ���:</TD>
						<TD align="left" width="30%"><%=gatherList%></TD>
						<TD align="right" width="20%" class=column>����:</TD>
						<TD align="left" width="30%">
							<div id="div_vendor">
								<select name="vendor_id" class="bk">
									<option value="-1">--����ѡ��ɼ���--</option>
								</select>													
							</div>
						</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="tr2" STYLE="display:">
							<TD align="right" width="20%" class=column>�豸�ͺ�:</TD>
							<TD width="30%">
								<div id="div_devicetype">
									<select name=devicetype_id class="bk">
										<option value="-1">--����ѡ����--</option>
									</select>
								</div>
							</TD>
							<TD align="right" width="20%" class=column>�豸�汾:</TD>
								<TD width="30%">
									<div id="div_deviceversion">
										<select name="softwareversion" class="bk">
											<option value="-1">--����ѡ���豸�ͺ�--</option>
										</select>													
									</div>
								</TD>	
							</TR>
							<TR bgcolor="#FFFFFF">
								<TD align="right" class=column>�豸�б�:<br>
								</TD>
								<TD colspan="3">
									<div id="div_device" style="width:95%; height:100px; z-index:1; top: 100px; overflow:scroll">
										<span>��ѡ���豸��</span>
									</div>
								</TD>
							</TR>
    				<tr bgcolor=#ffffff class=green_foot>
    					<td align="right" colspan="4"><input type="button" onclick="query()" value=" �� ѯ "></td>
    				</tr>
				</table>
			</td>
		</tr>
		<TR>
		<TD HEIGHT=10>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
	</table>
</form>
<%@ include file="../foot.jsp"%>