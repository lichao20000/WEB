<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>


<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>

<script type="text/javascript">

var _gw_type = "<%=request.getParameter("gw_type")%>";

function deviceResult(url){
	$("#tr_deviceInfo1").html("");
	$("tr[@id='tr_deviceInfo']").show();
	$("div[@id='div_deviceInfo']").html("���ڲ�ѯ�����Ե�....");
	$.post(url,{},function(ajax){
   	    $("div[@id='div_deviceInfo']").html("");
   		$("div[@id='div_deviceInfo']").append(ajax);
   	});
}

function ToExcel() {
	var page="../../gwms/resource/queryDevice!getInfoExcelDevice.action?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&timeType=" + document.frm.timeType.value
		+ "&device_serialnumber=" + $.trim(document.frm.device_serialnumber.value)
		+ "&loopback_ip=" + document.frm.loopback_ip.value;
	document.all("childFrm").src=page;
	//window.open(page);
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

function refreshDev(device_id,deviceSN,oui){
	var url = '<s:url value='/gwms/resource/refDelDev!refresh.action'/>';
	$.post(url,{
		deviceId:device_id,
		deviceSN:deviceSN,
		oui:oui
	},function(ajax){	
	    alert(ajax);
	    // frm.submit();
	});
}
//��������
var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
for(var i = 0; i < gwShare_queryField_arr.length; i++){
	var item = gwShare_queryField_arr[i];
	if(item.value ==  gwShare_queryField){
		item.checked = true;
	}
}

function delDev(device_id,cpe_allocatedstatus,oui,deviceSN){
	if(!confirm("���Ҫɾ���������豸��\n��������ɾ���Ĳ��ָܻ�������")){
		return false;
	}
	
	var url = '<s:url value='/gwms/resource/refDelDev!delete.action'/>';
	var tmp = '<s:property value="gw_type" />'; // modify by chenjie ԭ����ط�д����'1',���ڸ���gw_type�ж�
	
	$.post(url,{
		deviceId:device_id,
		cpe_allocatedstatus:cpe_allocatedstatus,
		oui:oui,
		deviceSN:deviceSN,
		gw_type:tmp 
	},function(result){
		if(result){
			if(result.indexOf('ɾ���ɹ�') === -1){
				alert(result);
			}else{
				var tr_deviceId = "#device_id" + device_id;
				$(tr_deviceId).hide();
			}
		}else{
			alert('ɾ��ʧ��:�������쳣');
		}
	});
}

</script>

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
		<td colspan="4">
			<%@ include file="/gwms/share/gwShareDeviceQueryForSxlt.jsp"%>
		</td>
	</TR>
	<!-- ��ѯʱ��ʾ -->
	<tr id="tr_deviceInfo" style="display: none">
		<TD>
			<div id="div_deviceInfo" style="width: 100%; z-index: 1; top: 100px; text-align: center;">
				���ڲ�ѯ�����Ե�....
			</div>
		</td>
	</tr>
	<!-- ��תҳ�����ʾ -->
	<tr id="tr_deviceInfo1">
		<td>		
		<s:if test="deviceList != null">
		  <table width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td bgcolor=#ffffff>&nbsp;</td>
			</tr>
		
			<s:if test="deviceList==null">
				<!-- ��һ����ɼ��������κβ�ѯ -->
			</s:if>
			<s:else>
				<s:if test="deviceList.size()>0">
					<tr>
						<td>
						<table width="100%" border=0 cellspacing=1 cellpadding=2
							bgcolor=#999999 id=userTable>
							<tr>
								<td class="green_title">����</td>
								<td class="green_title">�豸����</td>
								<td class="green_title">�ͺ�</td>
								<td class="green_title">����汾</td>
								<td class="green_title">OUI</td>
								<td class="green_title">�豸���к�</td>
								<td class="green_title">������IP</td>
								<td class="green_title">�ϱ�ʱ��</td>
								<td class="green_title" align='center' width="10%">����</td>
							</tr>
							<s:iterator value="deviceList">
								<tr bgcolor="#ffffff" id="device_id<s:property value="device_id" />">
									<s:if test="gw_type == 1">
										<td class=column nowrap align="center"><s:property
												value="city_name" /></td>
										<td class=column nowrap align="center"><s:property
												value="vendor_add" /></td>
										<td class=column nowrap align="center"><s:property
												value="device_model" /></td>
										<td class=column nowrap align="center"><s:property
												value="softwareversion" /></td>
										<td class=column nowrap align="center"><s:property
											value="oui" /></td>
										<td class=column nowrap align="center"><s:property
												value="device_serialnumber" /></td>
										<td class=column nowrap align="center"><s:property
												value="loopback_ip" /></td>
										<td class=column nowrap align="center"><s:property
												value="complete_time" /></td>
										<td class=column nowrap align="center"> 
											<%-- <IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭'
											onclick="EditDevice('<s:property value="device_id"/>')"
											style='cursor: hand'> --%>
											<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ" title="��ϸ��Ϣ"
												onclick="DetailDevice('<s:property value="device_id"/>')"
												style="cursor: hand"> 
											<IMG SRC="<s:url value="/images/refresh.png"/>" BORDER="0" ALT="ˢ��" title="ˢ��"
												onclick="refreshDev('<s:property value="device_id"/>','<s:property value="device_serialnumber"/>','<s:property value="oui"/>')"
												style="cursor: hand"> 
											<s:if test="#session.curUser.user.areaId==1">
											 <IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="ɾ��" title="ɾ��"
												onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
												style="cursor: hand">
											</s:if>
										</td>
									</s:if>
									<s:else>
										<td class=column nowrap align="center"><s:property
												value="city_name" /></td>
										<td class=column nowrap align="center"><s:property
												value="vendor_add" /></td>
										<td class=column nowrap align="center"><s:property
												value="device_model" /></td>
										<td class=column nowrap align="center"><s:property
												value="softwareversion" /></td>
										<td class=column nowrap align="center"><s:property
												value="device_serialnumber" /></td>
										<td class=column nowrap align="center"><s:property
												value="loopback_ip" /></td>
										<td class=column nowrap align="center"><s:property
												value="complete_time" /></td>
										<td class=column nowrap align="center">
											<%-- <IMG SRC="<s:url value="/images/edit.gif"/>" BORDER='0' ALT='�༭'
												onclick="EditDevice('<s:property value="device_id"/>')"
												style='cursor: hand'> --%>
											<IMG SRC="<s:url value="/images/view.gif"/>" BORDER="0" ALT="��ϸ��Ϣ" title="��ϸ��Ϣ"
												onclick="DetailDevice('<s:property value="device_id"/>')"
												style="cursor: hand"> 
											<IMG SRC="<s:url value="/images/refresh.png"/>" BORDER="0" ALT="ˢ��" title="ˢ��"
												onclick="refreshDev('<s:property value="device_id"/>','<s:property value="device_serialnumber"/>','<s:property value="oui"/>')"
												style="cursor: hand">
											<s:if test="#session.curUser.user.areaId==1">
											<IMG SRC="<s:url value="/images/del.gif"/>" BORDER="0" ALT="ɾ��" title="ɾ��"
												onclick="delDev('<s:property value="device_id"/>','<s:property value="cpe_allocatedstatus"/>','<s:property value="oui"/>','<s:property value="device_serialnumber"/>')"
												style="cursor: hand"> 
											</s:if>
										</td>
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
								<td class="green_title">����</td>
								<td class="green_title">�豸����</td>
								<td class="green_title">�ͺ�</td>
								<td class="green_title">����汾</td>
								<td class="green_title">�豸���к�</td>
								<td class="green_title">������IP</td>
								<td class="green_title">�ϱ�ʱ��</td>
								<td class="green_title" align='center' width="10%">����</td>
							</tr>
							<tr>
								<td colspan=8 align=left class=column>ϵͳû����ص��豸��Ϣ!</td>
							</tr>
						</table>
						</td>
					</tr>
				</s:else>
		
				<s:if test="deviceList.size()>0">
					<tr>
						<td align="right">
							<lk:pages url="/gwms/share/gwDeviceQuery!goPageDeviceListForSxlt.action"
								styleClass="" showType="" isGoTo="true" changeNum="true" />
						</td>
					</tr>
				</s:if>
			</s:else>
		
			<tr STYLE="display: none">
				<s:if test="gw_type == 1">
					<td><iframe id="childFrm" src=""></iframe></td>
				</s:if>
				<s:else>
					<td><iframe id="childFrm" src=""></iframe></td>
				</s:else>
			</tr>
		</table>
		</s:if>
		</td>
	</tr>
	
</table>

<%@ include file="../foot.jsp"%>
<script>
// ��תҳ�洫ֵ
// �򵥲�ѯ
var gwShare_queryType = "<s:property value='gwShare_queryType' />"; // ��ѯ����
var gwShare_queryField = "<s:property value='gwShare_queryField' />";// ��ѯ����
var gwShare_queryParam = "<s:property value='gwShare_queryParam' />";// ��ѯ����ֵ
var timeType = "<s:property value='timeType' />";// ʱ������
var starttime = "<s:property value='startTime' />";// ��ʼʱ��
var endtime = "<s:property value='endTime' />";// ����ʱ��
var gw_type = "<s:property value='gw_type' />";// ��ʼʱ��
// �߼���ѯ
var gwShare_cityId = "<s:property value='gwShare_cityId' />";// ����
var gwShare_onlineStatus = "<s:property value='gwShare_onlineStatus' />";// ����״̬
var gwShare_vendorId = "<s:property value='gwShare_vendorId' />";// ����
var gwShare_deviceModelId = "<s:property value='gwShare_deviceModelId' />";// �ͺ�
var gwShare_devicetypeId = "<s:property value='gwShare_devicetypeId' />";// �汾
var gwShare_bindType = "<s:property value='gwShare_bindType' />";// ��״̬
var gwShare_deviceSerialnumber = "<s:property value='gwShare_deviceSerialnumber' />";// �豸���к�

//�򵥲�ѯ
if(gwShare_queryType == 1){
	gwShare_queryChange('1');
	//��������
	var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
	for(var i = 0; i < gwShare_queryField_arr.length; i++){
		var item = gwShare_queryField_arr[i];
		if(item.value ==  gwShare_queryField){
			item.checked = true;
		}
	}
	// ����
	$("input[@name='gwShare_queryParam']").val(gwShare_queryParam);
	// ��ʼʱ��
	$("input[@name='starttime']").val(starttime);
	// ����ʱ��
	$("input[@name='endtime']").val(endtime);
	// ��ѯʱ������
	$("select[@name='timeType']").val(timeType);
}else if(gwShare_queryType == 2){
	gwShare_queryChange('2');
	// ����
	$("select[@name='gwShare_cityId']").val(gwShare_cityId);
	// ����״̬
	$("select[@name='gwShare_onlineStatus']").val(gwShare_onlineStatus);
	// ����
	$("select[@name='gwShare_vendorId']").val(gwShare_vendorId);
	// �ͺ�
	$("select[@name='gwShare_deviceModelId']").val(gwShare_deviceModelId);
	// �汾
	$("select[@name='gwShare_devicetypeId']").val(gwShare_devicetypeId);
	// ��״̬
	$("select[@name='gwShare_bindType']").val(gwShare_bindType);
	// �豸���к�
	$("input[@name='gwShare_deviceSerialnumber']").val(gwShare_deviceSerialnumber);
}else{
	$(function(){
		/* $("input[@name='gwShare_jiadan']").css("display","none");
		$("input[@name='gwShare_gaoji']").css("display","none"); */
		$("input[@name='gwShare_import']").css("display","none");
		$("input[@name='gwShare_up_import']").css("display","none");
		
		gwShare_setGaoji();
	});
}


</script>