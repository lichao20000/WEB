<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��ѯ</title>
<base target="_self"/>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	
/*------------------------------------------------------------------------------
//������:		��ʼ��������ready��
//����  :	��
//����  :	��ʼ�����棨DOM��ʼ��֮��
//����ֵ:		
//˵��  :	
//����  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
$(function(){
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	if("checkbox"==gwShare_queryResultType){
		$("input[@name='gwShare_selectall']").val("ȫ��ȡ��");
		$("[type='checkbox']").attr("checked","true");
		$("[type='checkbox']").attr("disabled","true");
		$("input[@name='gwShare_selectall']").css("display","");
	}
});
	
function gwShare_comfirmDevice(){
	var newBatchSetRes = '<s:property value="newBatchSetRes"/>';
	var _rs = new Array();
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var gwShare_total = $("input[@name='gwShare_total']").val();
	var childDevice = "";
	var deviceSum = 0;
	if("radio"==gwShare_queryResultType){
		_rs[0] = 0;
		childDevice = $("input[@name='radioDeviceId'][@checked]").val();
		if(""==childDevice || null==childDevice){
			alert("��ѡ���豸��");
			return false;
		}
	}else{
		if("ȫ��ȡ��"==$("input[@name='gwShare_selectall']").val()){
			$("[name='radioDeviceId']").each(function(){
				if(!$(this).attr("checked")){
					if(""==childDevice){
						childDevice+=$(this).val();
					}else{
						childDevice+="#"+$(this).val();
					}
					deviceSum = deviceSum + 1;
				}
	   		})
			if(confirm("ѡ����豸����Ϊ:"+(gwShare_total-deviceSum)+",�Ƿ����?")){
				_rs[0] = gwShare_total-deviceSum;
			}else{
				return;
			}
		}else{
			_rs[0] = 0;
			$("[name='radioDeviceId'][checked]").each(function(){
				if(""==childDevice){
					childDevice+=$(this).val();
				}else{
					childDevice+="#"+$(this).val();
				}
				deviceSum = deviceSum + 1;
	   		})
	   		if(""==childDevice || null==childDevice){
				alert("��ѡ���豸��");
				return false;
			}
			if(confirm("ѡ����豸����Ϊ:"+(deviceSum)+",�Ƿ����?")){
				
			}else{
				return;
			}
		}
	}
	var rs_ = new Array();
	if(""!=childDevice){
		var alldata = childDevice.split("#");
		for(var i=0;i<alldata.length;i++){
			var device = alldata[i].split("|");
			rs_[i] = new Array(device[0],device[1],device[2],device[3],device[4],device[5]);
		}
	}
	
	_rs[1] = $("input[@name='gwShare_param_one']").val();
	_rs[2] = rs_;
	_rs[3] = newBatchSetRes;
	
	if("nx_dx"==$("input[@name='instArea']").val() 
			&& "ȫ��ȡ��"==$("input[@name='gwShare_selectall']").val()){
		_rs[0] = gwShare_total;
		_rs[3] ="nx_dx";
	}
    if (window.opener != undefined) {
         //for chrome  
         window.opener.deviceResult(_rs);
     }
     else {
         window.returnValue = _rs;
     }
	//window.returnValue = _rs;
	window.close();
}

function gwShare_rsselectall(){
	var temp = $("input[@name='gwShare_selectall']").val();
	
	if("ȫ��ѡ��"==temp){
		$("input[@name='gwShare_selectall']").val("ȫ��ȡ��");
		$("[type='checkbox']").attr("checked","true");
	}else{
		$("input[@name='gwShare_selectall']").val("ȫ��ѡ��");
		$("[type='checkbox']").removeAttr("checked");
	}
}

function gwShare_rsselect(){
	if($("[name='gwShare_select']").attr("checked")){
		$("[name='radioDeviceId']").attr("checked","true");
	}else{
		$("[name='radioDeviceId']").removeAttr("checked");
	}
}

</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<body>
<form action="">
<input type="hidden" name="gwShare_queryResultType" value="<s:property value="gwShare_queryResultType"/>" />
<input type="hidden" name="gwShare_total" value="<s:property value="total"/>" />
<input type="hidden" name="gwShare_param_one" 
	value="<s:property value="gwShare_queryType"/>|<s:property value="gwShare_queryField"/>
		  |<s:property value="gwShare_cityId"/>|<s:property value="gwShare_queryParam"/>
		  |<s:property value="gwShare_onlineStatus"/>|<s:property value="gwShare_vendorId"/>
		  |<s:property value="gwShare_deviceModelId"/>|<s:property value="gwShare_devicetypeId"/>
		  |<s:property value="gwShare_bindType"/>|<s:property value="gwShare_deviceSerialnumber"/>
		  |<s:property value="gwShare_matchSQL"/>|<s:property value="total"/>|linkage" />
<input type="hidden" name="instArea" value="<s:property value="instArea"/>" />
		  
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor=#999999>
	<tr>
		<td colspan="7" align="center" class="green_foot" height="20" width="100%">
			��ѯ�������Ϊ��<s:property value="total"/>��
			<s:if test="#gwShare_msg!='null' && #gwShare_msg!='' ">
				&nbsp;&nbsp;<s:property value="gwShare_msg"/>
			</s:if>
		</td>
	</tr>
	<tr>
		<th align="center">
			<s:if test="'checkbox'==gwShare_queryResultType">
				<input type="<s:property value="gwShare_queryResultType"/>" name="gwShare_select" onclick="javascript:gwShare_rsselect();" />
			</s:if>
		</th>
		<th align="center">�豸����</th>
		<th align="center">�豸�ͺ�</th>
		<th align="center">����汾</th>
		<th align="center">����</th>
		<th align="center">�豸���к�</th>
		<th align="center">�豸IP</th>
	</tr>
	<s:iterator value="deviceList">
		<tr bgcolor="#FFFFFF">
			<td class=column1>
				<input type="<s:property value="gwShare_queryResultType"/>" name="radioDeviceId" 
					value="<s:property value="device_id"/>|<s:property value="oui"/>|
						   <s:property value="device_serialnumber"/>|<s:property value="loopback_ip"/>|
						   <s:property value="city_id"/>|<s:property value="city_name"/>" /></td>
			<td class=column1>
				<s:property value="vendor_add"/>(
				<s:property value="oui"/>)
			</td>
			<td class=column1><s:property value="device_model"/></td>
			<td class=column1><s:property value="softwareversion"/></td>
			<td class=column1><s:property value="city_name"/></td>
			<td class=column1><s:property value="device_serialnumber"/></td>
			<td class=column1><s:property value="loopback_ip"/></td>
		</tr>
	</s:iterator>
	
	
	<tr bgcolor="#FFFFFF" <s:if test="noSplitFlag=='yes'">style="display: none;"</s:if>>
		<td colspan="8" align="right">
			<lk:pages url="/gwms/share/gwDeviceQuery!goPage.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
		</td>
	</tr>
	
	<tr bgcolor="#FFFFFF">
		<td colspan="7" align="right" class="green_foot" width="100%">
			<input type="button" onclick="javascript:gwShare_rsselectall();" class=jianbian style="display:none" name="gwShare_selectall" value="ȫ��ѡ��" />
			<input type="button" onclick="javascript:gwShare_comfirmDevice();" class=jianbian name="gwShare_deviceConfirm" value=" ȷ �� " />
			<input type="button" onclick="javascript:window.close();" class=jianbian name="gwShare_canel" value=" ȡ �� " />
		</td>
	</tr>
</table>
</form>
</body>