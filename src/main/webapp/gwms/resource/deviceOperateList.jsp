<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/edittable.js"/>"></script>
<%
String isJs= LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script type="text/javascript">
var flag = "";
$(function() 
{
	initDate();
    var isFirst = '<s:property value="no_query"/>' ;
    var isJs= "<%=isJs%>";
    if(isFirst!='true' && isJs=='js_dx'){
		$("input[@name='button']").attr("disabled", false); 
	}

});

//��ʼ��ʱ��
function initDate()
{
	//��ʼ��ʱ��  ���� by zhangcong 2011-06-02
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	hour = theday.getHours();
	mimute = theday.getMinutes();
	second = theday.getSeconds();
	
	flag = '<s:property value="starttime"/>' ;
    if(null!=flag &&""!=flag){
    	$("input[@name='starttime']").val('<s:property value="starttime"/>');
    }else{
    	//modify by zhangcong ��ʼʱ��Ĭ��Ϊ����ĵ�һ��2011-06-02
	    $("input[@name='starttime']").val(year+"-1-1 00:00:00");
    }
	<%  if(!LipossGlobals.isXJDX()){%>
			if(null!=flag &&""!=flag){
		    	$("input[@name='endtime']").val('<s:property value="endtime"/>');
		    }else {
				//modify by zhangcong ����ʱ��Ĭ��Ϊ����
				$("input[@name='endtime']").val(year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second);
			}
	//$("input[@name='endtime']").val($.now("-",true));
	<%} %>
}

function ToExcel() {
	var page="../../gwms/resource/queryDevice!getInfoExcelDevice2.action?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&timeType=" + document.frm.timeType.value
		+ "&device_serialnumber=" + $.trim(document.frm.device_serialnumber.value)
		+ "&loopback_ip=" + document.frm.loopback_ip.value;
	document.all("childFrm").src=page;
	//window.open(page);
}

function do_test() {
	
	//�豸���к�
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	//�豸IP
	var loopback_ip = $.trim(document.frm.loopback_ip.value);
	
	<%  if(!LipossGlobals.isXJDX()){%>
	//2����������һ��
	if(device_serialnumber.length == 0 && loopback_ip.length == 0)
	{
		alert("��ѡ��������������һ����");
		document.frm.device_serialnumber.focus();
		return false;
	}
	<%} %>
	
	//��������豸���кţ��ͱ���Ϸ�
	if(device_serialnumber.length<6&&device_serialnumber.length>0){
		alert("�������������6λ�豸���кŽ��в�ѯ��");
		document.frm.device_serialnumber.focus();
		return false;
	}
	$("input[@name='button']").attr("disabled", true); 
	frm.submit();
	
}

function DelDevice(device_id){
	if(!confirm("���Ҫɾ���������豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return false;
	}
	var url = "../../Resource/DeviceSave.jsp";
	var pars = "device_id=" + device_id;
	pars += "&gw_type=" + <s:property value="infoType" />;
	pars += "&tt=" + new Date().getTime();
	pars += "&_action=delete";
	var gw_type = <s:property value="infoType" />;
	var deluser;
	if(confirm("�Ƿ���Ҫɾ���豸��Ӧ���û��ʺ�")){
		deluser = true;
	}else{
		deluser = false;
	}
	var _action = "delete";
	$.post(url,{
		device_id:device_id,
		gw_type:gw_type,
		tt:new Date().getTime(),
		_action:_action,
		deluser:deluser
	},function(ajax){	
		eval(ajax);
	});
	return true;
}

function EditDevice(device_id){
	var strpage = "../../Resource/AddDeviceForm.jsp?_action=update&device_id=" + device_id;
	window.location.href=strpage;
}
function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function refresh(){
	window.location.href=window.location.href;
}

//reset
function resetFrm() {
	//document.frm.starttime.value="";
	//document.frm.endtime.value="";
	//��ʼ��ʱ�� modify by zhangcong 2011-06-03
	initDate();
	document.frm.device_serialnumber.value="";
	document.frm.loopback_ip.value="";
	document.frm.timeType.value="1";
	document.frm.device_logicsn.value = "";
}

function refreshDev(device_id,deviceSN,oui){
	var url = '<s:url value='/gwms/resource/refDelDev!refresh.action'/>';
	$.post(url,{
		deviceId:device_id,
		deviceSN:deviceSN,
		oui:oui
	},function(ajax){	
	    alert(ajax);
	    frm.submit();
	});
}
function delDev(device_id,cpe_allocatedstatus,oui,deviceSN){
	if(!confirm("���Ҫɾ���������豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return false;
	}
	var url = "<s:url value='/gwms/resource/refDelDev!delete.action'/>";
	var tmp = '<s:property value="infoType" />'; // modify by chenjie ԭ����ط�д����'1',���ڸ���gw_type�ж�
	$.post(url,{
		deviceId:device_id,
		cpe_allocatedstatus:cpe_allocatedstatus,
		oui:oui,
		deviceSN:deviceSN,
		gw_type:tmp
	},function(ajax){	
	    alert(ajax);
	    frm.submit();
	});
}
//-->
</script>

<form name="frm"
	action="<s:url value='/gwms/resource/queryDevice!deviceOperate.action"'/>"
	method="POST">
<input name="gw_type" type="hidden" value="<s:property value='gw_type'/>"/>
<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td height="20"></td>
	</tr>
	<TR>
		<TD>
		<TABLE width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<TR>
				<TD width="164" align="center" class="title_bigwhite">�豸��Դ</TD>
				<td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12"> &nbsp; �豸�����б�,ѡ��ʱ������ȷ����Ҫ��ѯ��ʱ�䡣</td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#999999>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<tr>
				<th colspan="4">�豸��ѯ</th>
			</tr>
			<TR bgcolor=#ffffff>
				<td class="column" width='15%' align="right">�豸���кţ�</td>
				<td width='35%' align="left"><input name="device_serialnumber"
					type="text" class='bk'
					value="<s:property value='device_serialnumber'/>"> <%  if(!LipossGlobals.isXJDX()){%>
				<font color="red">*</font> <%} %>
				</td>
				<td class="column" width='15%' align="right">�豸IP��</td>
				<td width='35%' align="left"><input name="loopback_ip"
					type="text" class='bk' value="<s:property value='loopback_ip'/>">
				<%  if(!LipossGlobals.isXJDX()){%><font color="red">*</font>
				<%} %>
				</td>
			</TR>
			<TR bgcolor=#ffffff>
				<td class="column" width='15%' align="right">��ʼʱ��</td>
				<td width='35%' align="left"><input type="text"
					name="starttime" class='bk' readonly
					value="<s:property value='starttime'/>"> <img
					name="shortDateimg"
					onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="ѡ��"></td>
				<td class="column" width='15%' align="right">����ʱ��</td>
				<td width='35%' align="left"><input type="text" name="endtime"
					class='bk' readonly value="<s:property value='endtime'/>">
				<img name="shortDateimg"
					onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../images/dateButton.png" width="15" height="12" border="0"
					alt="ѡ��"></td>
			</TR>
			<TR bgcolor=#ffffff>
				<td class="column" width='15%' align="right">��ѯʱ������</td>
				<td width='35%' align="left"><select name="timeType" class="bk">
					<option value="1"
						<s:property value='"1".equals(timeType)||(!"2".equals(timeType)&&!"-1".equals(timeType))?"selected":""'/>>
					==�ϱ�ʱ��==</option>
					<option value="2"
						<s:property value='"2".equals(timeType)?"selected":""'/>>
					==��ʱ��==</option>
				</select></td>
				<td class="column" width='15%' align="right">LOID��</td>
				<td width='35%' align="left"><input name="device_logicsn"
					type="text" class='bk' value="<s:property value='device_logicsn'/>" />
				</td>
			</TR>

			<TR>
				<td class="green_foot" colspan="4" align="right"><input
					class=jianbian name="button" type="button" onclick="do_test();"
					value=" �� ѯ "> <INPUT CLASS=jianbian TYPE="button"
					value=" �� �� " onclick="resetFrm()"> <s:if
					test='#session.isReport=="1"'>
					<INPUT TYPE="button" value=" �� �� " class=jianbian
						onclick="ToExcel()">
				</s:if></TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#ffffff>&nbsp;</td>
	</tr>

	<s:if test="data==null">
		<!-- ��һ����ɼ��������κβ�ѯ -->
	</s:if>
	<s:else>
		<s:if test="data.size()>0">
			<tr>
				<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<s:iterator value="title" status="status">
							<td class="green_title"><s:property
								value="title[#status.index]" /></td>

						</s:iterator>
						<td class="green_title" align='center' width="10%">����</td>
					</tr>
					<s:iterator value="data">
						<tr bgcolor="#ffffff">
							<s:if test="infoType == 1">
								<td class=column nowrap align="center"><s:property
									value="city_name" /></td>
								<td class=column nowrap align="center"><s:property
									value="vendor_add" /></td>
								<td class=column nowrap align="center"><s:property
									value="device_model" /></td>
								<td class=column nowrap align="center"><s:property
									value="device" /></td>
								<td class=column nowrap align="center"><s:property
									value="device_id_ex" /></td>
								<td class=column nowrap align="center"><s:property
									value="loopback_ip" /></td>
								<td class=column nowrap align="center"><s:property
									value="complete_time" /></td>
								<td class=column nowrap align="center">
									<%
										if (!"ah_dx".equals(isJs))
										{
									%>
										<IMG
										SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭'
										onclick="EditDevice('<s:property value="device_id"/>')"
										style='cursor: hand'>
									<%
										}
									%>
									<%
										if (!"sx_lt".equals(isJs))
										{
									%>
									<IMG
											SRC="<s:url value="/images/button_s.gif"/>" BORDER='0'
											ALT='������'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
									<%
										}
									%>
									<IMG
									SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ"
									onclick="DetailDevice('<s:property value="device_id"/>')"
									style="cursor: hand"> <IMG
									SRC="<s:url value="/images/refresh.png"/>" BORDER="0" ALT="ˢ��"
									onclick="refreshDev('<s:property value="device_id"/>','<s:property value="device_serialnumber"/>','<s:property value="oui"/>')"
									style="cursor: hand"> <s:if
									test="#session.curUser.user.areaId==1">
									
									<%
										if (!"ah_dx".equals(isJs))
										{
									%>
									<IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="ɾ��"
										onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
										style="cursor: hand">
									<% } %>
										
								</s:if></td>
							</s:if>
							<s:else>
								<td class=column nowrap align="center"><s:property
									value="city_name" /></td>
								<td class=column nowrap align="center"><s:property
									value="vendor_add" /></td>
								<td class=column nowrap align="center"><s:property
									value="device_model" /></td>
								<td class=column nowrap align="center"><s:property
									value="device" /></td>
								<td class=column nowrap align="center"><s:property
									value="device_id_ex" /></td>
								<td class=column nowrap align="center"><s:property
									value="loopback_ip" /></td>
								<td class=column nowrap align="center"><s:property
									value="complete_time" /></td>
								<td class=column nowrap align="center"><!--<IMG
									SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭'
									onclick="EditDevice('<s:property value="device_id"/>')"
									style='cursor: hand'> -->
									<%
										if (!"sx_lt".equals(isJs))
										{
									%>
									<IMG
											SRC="<s:url value="/images/button_s.gif"/>" BORDER='0'
											ALT='������'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
									<%
										}
									%>
									<IMG
									SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ"
									onclick="DetailDevice('<s:property value="device_id"/>')"
									style="cursor: hand"> <IMG
									SRC="<s:url value="/images/refresh.png"/>" BORDER="0" ALT="ˢ��"
									onclick="refreshDev('<s:property value="device_id"/>','<s:property value="device_serialnumber"/>','<s:property value="oui"/>')"
									style="cursor: hand"> <s:if
									test="#session.curUser.user.areaId==1">
									<!-- <IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="ɾ��"
										onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
										style="cursor: hand"> -->
								</s:if></td>
							</s:else>
						</tr>
					</s:iterator>
				</table>
				</td>
			</tr>
		</s:if>

		<s:else>
			<tr>
				<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<s:iterator value="title" status="status">
							<td class="green_title"><s:property
								value="title[#status.index]" /></td>
						</s:iterator>
						<td class="green_title" align='center' width="10%">����</td>
					</tr>
					<tr>
						<td colspan=8 align=left class=column>ϵͳû����ص��豸��Ϣ!</td>
					</tr>
				</table>
				</td>
			</tr>
		</s:else>

		<s:if test="data.size()>0">
			<tr>
				<td align="right"><lk:pages
					url="/gwms/resource/queryDevice!gopageDeviceOperate.action"
					styleClass="" showType="" isGoTo="true" changeNum="true" /></td>
			</tr>
		</s:if>
	</s:else>

	<tr STYLE="display: none">
		<s:if test="infoType == 1">
			<td><iframe id="childFrm" src=""></iframe></td>
		</s:if>
		<s:else>
			<td><iframe id="childFrm" src=""></iframe></td>
		</s:else>
	</tr>
	<TR>
		<TD HEIGHT=20 align="center">
		<div id="_process"></div>
		</TD>
	</TR>
</table>
</form>

<%@ include file="../foot.jsp"%>
