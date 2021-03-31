<%@  page  language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸�汾��ѯ</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQuerySplitPage-linkage.js"/>"></script>

<script type="text/javascript">
$(function(){
	parent.dyniframesize();
});

function editDevice(device_type_id,vendor_id,device_model_id,specversion,hardwareversion,softwareversion,
		is_check,zeroconf,bootadv,category,is_probe)
{
	if(is_check=="" ){
		  is_check=-2;
  	}
	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("is_check_add")[0].value=is_check;
	window.parent.document.getElementById("updateId").value=device_type_id;
	
	if(zeroconf == 1){
		window.parent.document.getElementsByName("machineConfig_add")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("machineConfig_add")[1].checked = "checked";
	}
	
	if(bootadv == 1){
		window.parent.document.getElementsByName("bootadv")[0].checked = "checked";
	}else{
		window.parent.document.getElementsByName("bootadv")[1].checked = "checked";
	}
	
	if(category == 1){
		window.parent.document.getElementsByName("category")[0].checked = "checked";
	}else if(category == 2){
		window.parent.document.getElementsByName("category")[1].checked = "checked";
	}else if(category == 3){
		window.parent.document.getElementsByName("category")[2].checked = "checked";
	}else if(category == 4){
		window.parent.document.getElementsByName("category")[3].checked = "checked";
	}
	
	<%if( "jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		if(is_probe == 1){
			window.parent.document.getElementsByName("is_probe")[0].checked = "checked";
		}else{
			window.parent.document.getElementsByName("is_probe")[1].checked = "checked";
		}
	<%}%>
	parent.showAddPart(true);
	parent.disableLabel(true);
}
	
function editDevice_hnlt(device_type_id,vendor_id,device_model_id,specversion,
		hardwareversion,softwareversion,epg_version,net_type)
{
	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("epg_version")[0].value=epg_version;
	window.parent.document.getElementsByName("epg_version_old")[0].value=epg_version;
	window.parent.document.getElementsByName("net_type_old")[0].value=net_type;
	window.parent.document.getElementsByName("net_type")[0].value=net_type;
	window.parent.document.getElementById("updateId").value=device_type_id;
	
	parent.showAddPart(true);
}
</script>
</head>

<body>
<table class="listtable">
	<caption>��ѯ���</caption>
	<thead>
		<tr>
			<th>�豸����</th>
			<th>�豸�ͺ�</th>
			<th>�ض��汾</th>
			<th>Ӳ���汾</th>
			<th>����汾</th>
			<ms:inArea areaCode="hn_lt" notInMode="false">
			<th>EPG�汾</th>
			<th>������������</th>
			</ms:inArea>
			<th>����</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceList!=null && deviceList.size()>0">
			<s:iterator value="deviceList">
				<tr>
					<td><s:property value="vendor_add" /></td>
					<td><s:property value="device_model" /></td>
					<td><s:property value="specversion" /></td>
					<td><s:property value="hardwareversion" /></td>
					<td><s:property value="softwareversion" /></td>
					
					<ms:inArea areaCode="hn_lt" notInMode="false">
					<td><s:property value="epg_version" /></td>
					<td>
						<s:if test="net_type=='public_net'">����</s:if>
						<s:elseif test="net_type=='private_net'">ר��</s:elseif>
						<s:else>δ֪</s:else>
					</td>
					</ms:inArea>
					
					<td>
					<span class="check_no_js">
						<ms:inArea areaCode="hn_lt" notInMode="false">
							<a href="javascript:editDevice_hnlt('<s:property value="devicetype_id" />',
															'<s:property value="vendor_id" />',
															'<s:property value="device_model_id" />',
															'<s:property value="specversion" />',
															'<s:property value="hardwareversion" />',
															'<s:property value="softwareversion" />',
															'<s:property value="epg_version" />',
															'<s:property value="net_type" />'
														)">�༭
							</a>
						</ms:inArea>
						<ms:inArea areaCode="hn_lt" notInMode="true">
							<a href="javascript:editDevice('<s:property value="devicetype_id" />',
														'<s:property value="vendor_id" />',
														'<s:property value="device_model_id" />',
														'<s:property value="specversion" />',
														'<s:property value="hardwareversion" />',
														'<s:property value="softwareversion" />',
														'<s:property value="is_check" />',
														'<s:property value="zeroconf" />',
														'<s:property value="bootadv" />',
														'<s:property value="category" />',
														'<s:property value="is_probe" />'
													)">�༭</a>&nbsp;&nbsp;
							<a href="javascript:delDevice('<s:property value="devicetype_id" />','4')">ɾ��</a>
						</ms:inArea>
					</span>
					</td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<ms:inArea areaCode="hn_lt" notInMode="false">
					<td colspan=8>û�в�ѯ������豸��</td>
				</ms:inArea>
				<ms:inArea areaCode="hn_lt" notInMode="true">
					<td colspan=6>û�в�ѯ������豸��</td>
				</ms:inArea>
			</tr>
		</s:else>
	</tbody>

	<tfoot>
		<tr>
		<ms:inArea areaCode="hn_lt" notInMode="false">
			<td colspan="8" align="right">
				<lk:pages url="/gtms/stb/resource/stbDeviceTypeInfo!queryList.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</ms:inArea>
		<ms:inArea areaCode="hn_lt" notInMode="true">
			<td colspan="6" align="right">
				<lk:pages url="/gtms/stb/resource/stbDeviceTypeInfo!queryList.action" 
					styleClass="" showType="" isGoTo="true" changeNum="true" />
			</td>
		</ms:inArea>
		</tr>
	</tfoot>
</table>
</body>
<script>
function detailDevice(devicetype_id,specId)
{
	window.open("<s:url value='/itms/resource/deviceTypeInfo!queryDetail.action'/>?deviceTypeId="+devicetype_id+"&detailSpecId="+specId,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
}

function editDeviceOld(device_type_id,vendor_id,device_model_id,specversion,hardwareversion,
		softwareversion,is_check,rela_dev_type_id,type_id,ip_type,is_normal,spec_id)
{     
  if(is_check=="" ){
	  is_check=-2;
	 
  }
  if(rela_dev_type_id=="" ){
	  rela_dev_type_id=-1;
  }

	window.parent.document.getElementsByName("vendor_add")[0].value=vendor_id;
	parent.change_model('deviceModel',device_model_id);
	window.parent.document.getElementsByName("speversion")[0].value=specversion;
	window.parent.document.getElementsByName("hard_version_add")[0].value=hardwareversion;	
	window.parent.document.getElementsByName("soft_version_add")[0].value=softwareversion;	
	window.parent.document.getElementsByName("is_check_add")[0].value=is_check;	
    window.parent.document.getElementsByName("rela_dev_type_add")[0].value=rela_dev_type_id;	
    window.parent.document.getElementsByName("specId")[0].value=spec_id;	
	window.parent.document.getElementById("updateId").value=device_type_id;
	if(ip_type==0){
		 window.parent.document.getElementsByName("ipType")[0].checked="checked";
	}else{
		window.parent.document.getElementsByName("ipType")[1].checked="checked";
	}
	if(is_normal==1){
		window.parent.document.getElementsByName("isNormal")[0].checked="checked";
	}else
		window.parent.document.getElementsByName("isNormal")[1].checked="checked";
	window.parent.document.getElementById("actLabel").innerHTML="�༭";
	parent.queryTypeName(type_id);
	debugger;
	parent.disableLabel(true);
	parent.showAddPart(true);
		
}

function delDevice(id,gw_type)
{
	if(!delWarn())
		return;
	var url = "<s:url value="/gtms/stb/resource/stbDeviceTypeInfo!deleteDevice.action"/>";
	
	$.post(url,{
		deleteID:id,
		gw_type:gw_type
	},function(ajax){
		var result = parseInt(ajax);
		if(result == 0)
		{
			alert("���豸�汾��������豸��������ɾ����");
			return;
		}
		if(result > 0)
		{
			alert("�豸�ͺ�ɾ���ɹ�");
			debugger;
			// ��ͨ��ʽ�ύ
			var form = parent.document.getElementById("mainForm");
			form.action = "<s:url value="/gtms/stb/resource/stbDeviceTypeInfo!queryList.action"/>";
			// parent.reset();
            //form.target = "dataForm";
			form.submit();
		}
		else
		{
			alert("�豸�ͺ�ɾ��ʧ�ܣ�");
		}
	});
}

function delWarn(){
	if(confirm("���Ҫɾ�����豸�汾��\n��������ɾ���Ĳ��ָܻ�������")){
		return true;
	}
	else{
		return false;
	}
}

function checkDevice(id)
{
	var url = "<s:url value="/itms/resource/deviceTypeInfo!updateIsCheck.action"/>";
	$.post(url,{
		deviceTypeId:id
	},function(ajax){
		var result = parseInt(ajax);
		if(result > 0)
		{
			alert("����豸�ɹ�");
			var form = window.parent.document.getElementById("mainForm");
			form.action = "<s:url value="/itms/resource/deviceTypeInfo!queryList.action"/>";
			//form.target = "dataForm";
			form.submit();
		}
		else
		{
			alert("����豸ʧ��");
		}
	});
}
</script>
</html>