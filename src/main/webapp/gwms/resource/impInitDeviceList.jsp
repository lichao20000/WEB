<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/edittable.js"/>"></SCRIPT>
<script type="text/javascript"
			src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
var gw_type = '<s:property value="gw_type"/>';
$(function() 
		   {
			 var isFirst = '<s:property value="no_query"/>' ;
				 if(isFirst!='true'){
					$("input[@name='button']").attr("disabled", false); 
		    }
		});



function ToExcel() {
	var page="../../gwms/resource/queryDevice!getInfoExcelUnconfirmDevice.action?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&timeType=" + document.frm.timeType.value
		+ "&device_serialnumber=" + $.trim(document.frm.device_serialnumber.value)
		+ "&loopback_ip=" + document.frm.loopback_ip.value;
	document.all("childFrm").src=page;
	//window.open(page);
}

function do_test() {
	
	var device_serialnumber = $.trim(document.frm.device_serialnumber.value);
	if(device_serialnumber.length<6&&device_serialnumber.length>0){
		alert("�������������6λ�豸���кŽ��в�ѯ��");
		document.frm.device_serialnumber.focus();
		return false;
	}
	
	document.frm.gw_type.value = gw_type;
	
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
function ConfirmDev(device_id){
	if(!confirm("ȷ��Ҫȷ�ϸ��豸������")){
		return false;
	}
	var url = "../../Resource/DeviceSave.jsp";
	var gw_type = <s:property value="infoType" />;
	var _action = "status";
	$.post(url,{
		device_id:device_id,
		gw_type:gw_type,
		tt:new Date().getTime(),
		_action:_action
	},function(ajax){	
		alert($.trim(ajax));
		//do_test();
		resetFrm();
		document.frm.gw_type.value = gw_type;
		frm.submit();
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
	document.frm.device_serialnumber.value="";
	
}

//ȫѡ
function SelectAll(_this,_name){
	var chkArr = document.all(_name);
	if(_this.checked){
		if(typeof(chkArr) == "object" ) {
			if(typeof(chkArr.length) != "undefined") {
				for(var i=0; i<chkArr.length; i++){
					chkArr[i].checked = true;
				}
			} else {
				chkArr.checked = true;
			}
		}
	}else{
		if(typeof(chkArr) == "object" ) {
			if(typeof(chkArr.length) != "undefined") {
				for(var i=0; i<chkArr.length; i++){
					chkArr[i].checked = false;
				}
			} else {
				chkArr.checked = false;
			}
		}
	}
}

//���ݸ�ѡ��ѡ��״̬����ȡ�豸id
function getDeviceIDByCheck(_name){
	var arrObj = document.all(_name);
	var strDeviceIDs = "";
	if(typeof(arrObj.length) == "undefined"){
		if(arrObj.checked){
			strDeviceIDs = arrObj.value + ",";
		}
	}else{
		for(var i=0;i<arrObj.length;i++){
			if(arrObj[i].checked){
				strDeviceIDs += arrObj[i].value + ",";
			}
		}
	}
	strDeviceIDs = strDeviceIDs.substring(0, strDeviceIDs.length-1);
	return strDeviceIDs;
}

//����ȷ���豸
function ConfirmDevs(_name){
	var strDeviceIDs = getDeviceIDByCheck(_name);
	var device_id = strDeviceIDs.split(",");
	if(strDeviceIDs == ""){
		alert("��ѡ��Ҫȷ�ϵ��豸");
		return false;
	}
	if(!confirm("ȷ��Ҫȷ��ѡ�е��豸��")){
		return false;
	}
	var url = "../../Resource/DeviceSave.jsp";
	var gw_type = <s:property value="infoType" />;
	var _action = "status";
	$.post(url,{
		device_id:device_id,
		gw_type:gw_type,
		tt:new Date().getTime(),
		_action:_action
	},function(ajax){	
		alert($.trim(ajax));
		resetFrm();
		frm.submit();
	});
	return true;
}

</script>

<form name="frm"
	action="<s:url value='/gwms/resource/queryDevice!impInitDevice.action"'/>"
	method="POST">

	<input type="hidden" name="gw_type" value="" >

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
							&nbsp;
							<img src="<s:url value="/images/attention_2.gif"/>" width="15"
								height="12">
							&nbsp; Ԥ���豸�б�,�ɸ����豸���кŲ�ѯָ���豸��
						</td>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th colspan="4">
							Ԥ���豸��ѯ
						</th>
					</tr>
					<TR bgcolor=#ffffff>
						<td class="column" width='15%' align="right">
							�豸���кţ�
						</td>
						<td width='35%' align="left">
							<input name="device_serialnumber" type="text" class='bk'
								value="<s:property value='device_serialnumber'/>">
						</td>
						
					</TR>
					
					

					<TR>
						<td class="green_foot" colspan="4" align="right">
							<input class="btn" name="button" type="button"
								onclick="do_test();" value=" �� ѯ ">
							<INPUT CLASS="btn" TYPE="button" value=" �� �� "
								onclick="resetFrm()">
							<s:if test='#session.isReport=="1"'>
								<INPUT TYPE="hidden" value=" �� �� " class=btn onclick="ToExcel()">
							</s:if>
						</TD>
					</TR>

				</TABLE>
			</TD>
		</TR>
		<tr>
			<td bgcolor=#ffffff>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<td class="green_title" align='center' width="10%">
							�豸OUI
						</td>
						
						<td class="green_title" align='center' width="15%">
							�豸���к�
						</td>
						<td class="green_title" align='center' width="10%">
							����
						</td>
						<td class="green_title" align='center' width="10%">
							����ʱ��
						</td>
						<td class="green_title" align='center' width="10%">
							�豸MAC
						</td>
						<td class="green_title" align='center' width="10%">
							����״̬
						</td>
						<td class="green_title" align='center' width="10%">
							ʹ��״̬
						</td>
						<!--  
						<td class="green_title" align='center' width="10%">
							����
						</td>
						-->
					</tr>
					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								
									<td class=column nowrap align="center">
										<s:property value="oui" />
									</td>
									<td class=column nowrap align="center">
										<s:property value="device_serialnumber" />
									</td>
									<td class=column nowrap align="center">
										&nbsp;
									</td>
									<td class=column nowrap align="center">
										&nbsp;
									</td>
									<td class=column nowrap align="center">
										&nbsp;
									</td>
									<td class=column nowrap align="center">
										�ɹ�
									</td>
									<td class=column nowrap align="center">
										<s:property value="status" />
									</td>
									<!--  
									<td class=column nowrap align="center">
										<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
											ALT='ɾ��'
											onclick="DelDevice('<s:property value="device_id"/>')"
											style='cursor: hand'>
									</td>
									-->
							</tr>
						</s:iterator>
					</s:if>
					<s:else>

						<tr>
							<td colspan=8 align=left class=column>
								ϵͳû����ص��豸��Ϣ!
							</td>
						</tr>

					</s:else>
					<tr>
						<td align="right" class=column2 colspan="8">
							<lk:pages
								url="/gwms/resource/queryDevice!gopageImpInitDevice.action"
								styleClass="" showType="" isGoTo="true" changeNum="true" />
						</td>
					</tr>
					<tr>
						<td class=column2 colspan="8" height="25">
							&nbsp;&nbsp;
							
							
						</td>
					</tr>
				</table>
			</td>
		</tr>

		<tr STYLE="display: none">
			<s:if test="infoType == 1">
				<td>
					<iframe id="childFrm" src=""></iframe>
				</td>
			</s:if>
			<s:else>
				<td>
					<iframe id="childFrm" src=""></iframe>
				</td>
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
