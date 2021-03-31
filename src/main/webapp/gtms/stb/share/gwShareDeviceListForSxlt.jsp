<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备查询</title>
<base target="_self"/>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
/*------------------------------------------------------------------------------
//函数名:		初始化函数（ready）
//参数  :	无
//功能  :	初始化界面（DOM初始化之后）
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
$(function(){
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	if("checkbox"==gwShare_queryResultType){
		$("[name='gwShare_selectall']").val("全部取消");
		$("[type='checkbox']").attr("checked","true");
		$("[type='checkbox']").attr("disabled","true");
		$("[name='gwShare_selectall']").css("display","");
	}
});
	
function gwShare_comfirmDevice(){
	var _rs = new Array();
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var gwShare_total = $("input[@name='gwShare_total']").val();
	var childDevice = "";
	var deviceSum = 0;
	if("radio"==gwShare_queryResultType){
		_rs[0] = 0;
		childDevice = $("input[@name='radioDeviceId'][@checked]").val();
		if(""==childDevice || null==childDevice){
			alert("请选择设备！");
			return false;
		}
	}else{
		if("全部取消"==$("[name='gwShare_selectall']").val()){
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
			if(confirm("选择的设备数量为:"+(gwShare_total-deviceSum)+",是否继续?")){
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
				alert("请选择设备！");
				return false;
			}
			if(confirm("选择的设备数量为:"+(deviceSum)+",是否继续?")){
				
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
			rs_[i] = new Array(device[0],device[1],device[2],device[3],device[4],device[5],device[6],device[7],device[8],device[9],device[10]);
		}
	}
	_rs[1] = $("input[@name='gwShare_param_one']").val();
	_rs[2] = rs_;
	// 山西联通不显示中间设备选择页面
    deviceResult(_rs);
}

function gwShare_rsselectall(){
	var temp = $("[name='gwShare_selectall']").val();
	
	if("全部选中"==temp){
		$("[name='gwShare_selectall']").val("全部取消");
		$("[type='checkbox']").attr("checked","true");
	}else{
		$("[name='gwShare_selectall']").val("全部选中");
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
		  |<s:property value="gwShare_bindType"/>|<s:property value="gwShare_deviceSerialnumber"/>|linkage" />
<table class="listtable" width="100%" align="center">
	<thead>
		<tr>
			<td colspan="7" align="center" class="green_foot" height="20" width="100%">
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
			<th align="center">设备厂商</th>
			<th align="center">设备型号</th>
			<th align="center" width="10%">软件版本</th>
			<ms:inArea areaCode="hn_lt" notInMode="false">
			<th align="center" width="10%">EPG版本</th>
			</ms:inArea>
			<th align="center" width="10%">属地</th>
			<th align="center" width="35%">设备序列号</th>
			<ms:inArea areaCode="hn_lt" notInMode="false">
			<th align="center" width="10%">APK版本</th>
			<th align="center" width="25%">设备最近上线时间</th>
			</ms:inArea>
			<ms:inArea areaCode="hn_lt" notInMode="true">
			<th align="center" width="25%">设备上报时间</th>
			</ms:inArea>
		</tr>
	</thead>
	<tbody>	
		<s:iterator value="deviceList">
			<tr bgcolor="#FFFFFF">
				<td class=column1>
					<input type="<s:property value="gwShare_queryResultType"/>" name="radioDeviceId"  onclick="gwShare_comfirmDevice()"
						value="<s:property value="device_id"/>|<s:property value="oui"/>|<s:property value="device_serialnumber"/>|<s:property value="loopback_ip"/>|<s:property value="city_id"/>|<s:property value="city_name"/>|<s:property value="devicetype_id"/>|<s:property value="customer_id"/>|<s:property value="serv_account"/>|<s:property value="cpe_allocatedstatus"/>|<s:property value="zero_account"/>" />
				</td>
				<td class=column1>
					<s:property value="vendor_add"/>(<s:property value="oui"/>)
				</td>
				<td class=column1><s:property value="device_model"/></td>
				<td class=column1><s:property value="softwareversion"/></td>
				<ms:inArea areaCode="hn_lt" notInMode="false">
				<td class=column1><s:property value="epg_version"/></td>
				</ms:inArea>
				<td class=column1><s:property value="city_name"/></td>
				<td class=column1><s:property value="device_serialnumber"/></td>
				<ms:inArea areaCode="hn_lt" notInMode="false">
				<td class=column1><s:property value="apk_version_name"/></td>
				<td class=column1><s:property value="cpe_currentupdatetime"/></td>
				</ms:inArea>
				<ms:inArea areaCode="hn_lt" notInMode="true">
				<td class=column1><s:property value="complete_time"/></td>
				</ms:inArea>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr>
			<td colspan="7" align="right">
				查询结果条数：<s:property value="total"/>&nbsp;&nbsp;
				
				<span id="goPageForStbInst" style="display: none;">
					<lk:pages url="/gtms/stb/share/shareDeviceQuery!goPageForStbInst.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
				</span>
				<span id="goPageForStbRelease" style="display:none;">
					<lk:pages url="/gtms/stb/share/shareDeviceQuery!goPageForStbRelease.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
				</span>
				<span id="goPageForStbServiceDone" style="display:none;">
					<lk:pages url="/gtms/stb/share/shareDeviceQuery!goPageForStbServiceDone.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
				</span>
				<span id="goPageForStbDevicePingTest" style="display:none;">
					<lk:pages url="/gtms/stb/share/shareDeviceQuery!goPageForStbDevicePingTest.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
				</span>
				<span id="goPageForStbDeviceTraceRouteTest" style="display:none;">
					<lk:pages url="/gtms/stb/share/shareDeviceQuery!goPageForStbDeviceTraceRouteTest.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
				</span>
			</td>
		</tr>
	</tfoot>
</table>
</form>
</body>