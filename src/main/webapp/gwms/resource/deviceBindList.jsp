<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<%
String isJs= LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script type="text/javascript">

    $(function() 
		   {
			 var isFirst = '<s:property value="no_query"/>' ;
			 var isJs= "<%=isJs%>";
		     if(isFirst!='true'&& isJs=='js_dx'){
					$("input[@name='button']").attr("disabled", false); 
		    }
		});

function ToExcel() {
	var page="../../gwms/resource/queryDevice!getInfoExcel.action?bindState="+$("input[@name='bindState'][@checked]").val()
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&timeType=" + document.frm.timeType.value;
	document.all("childFrm").src=page;
	//window.open(page);
}

function do_test() {
	if($("input[@name='bindState'][@checked]").val()==""){
		alert("��ѡ���״̬��");
		return false;
	}
	$("input[@name='button']").attr("disabled", true); 
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("���ڲ�ѯ�����Ե�....");
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
		deluser = true;
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
	var strpage = "../../Resource/AddDeviceForm.jsp?_action=update&device_id=" + device_id+"&gw_type=" + <s:property value="infoType" />;
	window.location.href=strpage;
}
function DetailDevice(device_id){
	var strpage = "../../Resource/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function refresh(){
	window.location.href=window.location.href;
}
//-->
</script>

<form name="frm"
	action="<s:url value='/gwms/resource/queryDevice!init.action'/>"
	method="POST">
	<input type="hidden" name="gw_type2" value=>
	<input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">
	<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<tr>
			<td height="20"></td>
		</tr>
		<TR>
			<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="164" align="center" class="title_bigwhite">
							�豸��Դ
						</TD>
						<td>

						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999>
					<tr bgcolor="#ffffff" >
						<td width='15%' class="column" align="right">
							��״̬
						</td>
						<td width='35%'>
							<input type="radio" value="2" name="bindState"
								<s:property value='"2".equals(bindState)?"checked":""'/>>
							�Ѱ�
							<input type="radio" value="1" name="bindState"
								<s:property value='"1".equals(bindState)?"checked":""'/>>
							δ��&nbsp;
						</td>
						<TD class="column" width='15%' align="right">
							ʱ������
						</td>
						<td width='35%'>
							<select name="timeType" class="bk">
								<option value="-1"
									<s:property value='"-1".equals(timeType)?"selected":""'/>>
									==��ѡ��==
								</option>
								<option value="1"
									<s:property value='"1".equals(timeType)||(!"2".equals(timeType)&&!"-1".equals(timeType))?"selected":""'/>>
									==�ϱ�ʱ��==
								</option>
								<s:if test="bindState==2">
									<option value="2"
										<s:property value='"2".equals(timeType)?"selected":""'/>>
										==��ʱ��==
									</option>
								</s:if>
							</select>

						</TD>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" align="right">
							��ʼʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
						</td>
						<td class="column" align="right">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
										onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
						</td>
					</tr>
					<TR>
						<td align="right" class='green_foot' colspan="4">
							<input name="button" type="button" onclick="do_test();"
								class="jianbian" value=" �� ѯ ">
						</td>
					</TR>
				</table>
			</td>
		</tr>
		<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						���ڲ�ѯ�����Ե�....</div>
				</td>
			</tr>
	</table>
</form>

<%@ include file="../foot.jsp"%>
