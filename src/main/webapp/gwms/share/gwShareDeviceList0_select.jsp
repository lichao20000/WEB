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

});
var InstArea = '<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>';
function gwShare_comfirmDevice(){
	var _rs = new Array();
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var gwShare_total = $("input[@name='gwShare_total']").val();
	var childDevice = "";
	var deviceSum = 0;
	var task_desc = $("input[@name='task_desc']").val();
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
			if(""===task_desc || null==task_desc){
				task_desc =gwShare_total-deviceSum;
			}
			if(confirm("ѡ����豸����Ϊ:"+task_desc+",�Ƿ����?")){
			    if(gwShare_total-deviceSum < task_desc)
                {
                    alert("ѡ����豸�����������е��豸������������ѡ��");
                    return;
                }
				_rs[0] = task_desc;
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
			if(""===task_desc || null==task_desc){
				task_desc = deviceSum;
			}
			if(confirm("ѡ����豸����Ϊ:"+task_desc+",�Ƿ����?")){

			}else{
				return;
			}
		}
	}
	var alldata = childDevice.split("#");
	var rs_ = new Array();
	for(var i=0;i<alldata.length;i++){
		var device = alldata[i].split("|");
		rs_[i] = new Array(device[0],device[1],device[2],device[3],device[4],device[5],device[6],device[7],device[8],device[9],device[10],device[11],device[12],device[13],device[14]);
	}
	
	
	_rs[1] = $("input[@name='gwShare_param_one']").val();
	_rs[2] = rs_;
	if(InstArea== "ah_lt"){
		_rs[3] = $("input[@name='task_desc']").val();
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
<input type="hidden" name="instArea" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>" />
<input type="hidden" name="gwShare_param_one"
	value="<s:property value="gwShare_queryType"/>|<s:property value="gwShare_queryField"/>
		  |<s:property value="gwShare_cityId"/>|<s:property value="gwShare_queryParam"/>
		  |<s:property value="gwShare_onlineStatus"/>|<s:property value="gwShare_vendorId"/>
		  |<s:property value="gwShare_deviceModelId"/>|<s:property value="gwShare_devicetypeId"/>
		  |<s:property value="gwShare_bindType"/>|<s:property value="gwShare_deviceSerialnumber"/>|linkage" />
		  
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor=#999999>
	<tr>
		<td colspan="7" align="center" class="green_foot" height="20" width="100%">
			��ѯ�������Ϊ��<s:property value="total"/>��
			<s:if test="#gwShare_msg!='null' && #gwShare_msg!='' ">
				&nbsp;&nbsp;<s:property value="gwShare_msg"/>
			</s:if>
		</td>
	</tr>

	<tr <%if (!"ah_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>style="display: none" <%}%>>
		<td colspan="7" align="center" class="green_foot" height="20" width="100%">
			ѡ��ǰ��<input type="number" name="task_desc" value="" size="10" maxlength="50" class="bk"/>��
			<font color="red">*����С�ڲ�ѯ�������</font>
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
					value="<s:property value="device_id"/>|<s:property value="oui"/>|<s:property value="device_serialnumber"/>|<s:property value="loopback_ip"/>|<s:property value="city_id"/>|<s:property value="city_name"/>|<s:property value="ssid"/>|<s:property value="vlanid"/>|<s:property value="priority"/>|<s:property value="channel"/>|<s:property value="wlanport"/>|<s:property value="devicetype_id"/>|<s:property value="paramvalue"/>|<s:property value="username"/>|<s:property value="user_id"/>" /></td>
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
	<tr bgcolor="#FFFFFF">
		<td colspan="7" align="right" class="green_foot" width="100%">
			<input type="button" onclick="javascript:gwShare_comfirmDevice();" class=jianbian name="gwShare_deviceConfirm" value=" ȷ �� " />
			<input type="button" onclick="javascript:window.close();" class=jianbian name="gwShare_canel" value=" ȡ �� " />
		</td>
	</tr>
</table>
<br>
</form>
</body>