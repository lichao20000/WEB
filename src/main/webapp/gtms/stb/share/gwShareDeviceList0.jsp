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
		if("全部取消"==$("input[@name='gwShare_selectall']").val()){
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
	
	var alldata = childDevice.split("#");
	var rs_ = new Array();
	for(var i=0;i<alldata.length;i++){
		var device = alldata[i].split("|");
		rs_[i] = new Array(device[0],device[1],device[2],device[3],device[4],device[5],device[6],device[7],device[8],device[9]);
	}
	_rs[1] = $("input[@name='gwShare_param_one']").val();
	_rs[2] = rs_;
	window.returnValue = _rs;
	window.close();
}

function gwShare_rsselectall(){
	var temp = $("input[@name='gwShare_selectall']").val();
	
	if("全部选中"==temp){
		$("input[@name='gwShare_selectall']").val("全部取消");
		$("[type='checkbox']").attr("checked","true");
	}else{
		$("input[@name='gwShare_selectall']").val("全部选中");
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
		<input type="hidden" name="is_awifi" value="<s:property value="is_awifi" />"/>  
<table class="listtable" width="100%" align="center">
	<s:if test="0==gwShare_RecordNum">
		查询结果条数为：<s:property value="total"/>！
	</s:if>
	<s:else>
		查询结果条数限定为：<s:property value="gwShare_RecordNum"/>！
	</s:else>
			<s:if test="#gwShare_msg!='null' && #gwShare_msg!='' ">
				&nbsp;&nbsp;<s:property value="gwShare_msg"/>
			</s:if>
	<thead>
		<tr>
			<th align="center">
				<s:if test="'checkbox'==gwShare_queryResultType">
					<input type="<s:property value="gwShare_queryResultType"/>" name="gwShare_select" onclick="javascript:gwShare_rsselect();" />
				</s:if>
			</th>
			<th align="center">设备厂商</th>
			<th align="center">设备型号</th>
			<th align="center">软件版本</th>
			<th align="center">属地</th>
			<th align="center">设备序列号</th>
			<ms:inArea areaCode="jx_dx" notInMode="false">
			<th align="center">设备IP</th>
			</ms:inArea>
		</tr>
	</thead>
	<tbody>	
		<s:iterator value="deviceList">
			<tr bgcolor="#FFFFFF" id="JK">
				<td class=column1>
					<input type="<s:property value="gwShare_queryResultType"/>" name="radioDeviceId" 
						value="<s:property value="device_id"/>|<s:property value="oui"/>|
							   <s:property value="device_serialnumber"/>|<s:property value="loopback_ip"/>|
							   <s:property value="city_id"/>|<s:property value="city_name"/>|<s:property value="devicetype_id"/>|<s:property value="customer_id"/>|<s:property value="serv_account"/>|<s:property value="cpe_allocatedstatus"/>" /></td>
				<td class=column1>
					<s:property value="vendor_add"/>(
					<s:property value="oui"/>)
				</td>
				<td class=column1><s:property value="device_model"/></td>
				<td class=column1><s:property value="softwareversion"/></td>
				<td class=column1><s:property value="city_name"/></td>
				<td class=column1><s:property value="device_serialnumber"/></td>
				<ms:inArea areaCode="jx_dx" notInMode="false">
				<td class=column1><s:property value="loopback_ip"/></td>
				</ms:inArea>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="7" align="right" class="green_foot" width="100%">
				<button onclick="javascript:gwShare_comfirmDevice();"> 确 定 </button>
				<button onclick="javascript:window.close();"> 取 消 </button>
			</td>
		</tr>
	</tfoot>
</table>
<br>
</form>
</body>