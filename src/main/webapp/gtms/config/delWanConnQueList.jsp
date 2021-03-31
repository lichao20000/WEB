<%@page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备查询</title>
<base target="_self"/>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
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
	var alldata = childDevice.split("#");
	var rs_ = new Array();
	for(var i=0;i<alldata.length;i++){
		var device = alldata[i].split("|");
		rs_[i] = new Array(device[0],device[1],device[2],device[3],device[4],device[5],device[6]);
	}
	
	_rs[1] = $("input[@name='gwShare_param_one']").val();
	_rs[2] = $("input[@name='resStr']").val();
	_rs[3] = $("input[@id='faultListStr']").val();
	_rs[4] = $("input[@id='gwShare_total']").val();
	
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
<input type="hidden" name="gwShare_queryResultType" value="checkbox" />
<input type="hidden" name="gwShare_total" id="gwShare_total" value="<s:property value="total"/>" />
<input type="hidden" name="gwShare_param_one" 
	value="<s:property value="loid"/>|<s:property value="serv_type"/>|<s:property value="vlanid"/>|linkage" />
		  
<input type="hidden" name="resStr" id="resStr" value="<s:property value="resStr"/>" />
<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor=#999999>
	<tr>
		<td colspan="7" align="center" class="green_foot" height="20" width="100%">
			结果条数为：<s:property value="total"/>！
			<s:if test="#gwShare_msg!='null' && #gwShare_msg!='' ">
				&nbsp;&nbsp;<s:property value="gwShare_msg"/>
			</s:if>
		</td>
	</tr>
	<tr>
		<th align="center">
			<input type="checkbox" name="gwShare_select"   checked ='checked' disabled="disabled"  onclick="javascript:gwShare_rsselect();" />
		</th>
		<th align="center">LOID</th>
		<th align="center">业务类型</th>
		<th align="center">VLAN信息</th>
	</tr>
	<s:iterator value="deviceList">
		<tr bgcolor="#FFFFFF">
			<td class=column1>
				<input type="checkbox" name="radioDeviceId"  checked ='checked' disabled="disabled" 
					value="<s:property value="loid"/>|<s:property value="serv_type"/>|<s:property value="vlanid"/>" /></td>
			<td class=column1><s:property value="loid"/></td>
			<td class=column1><s:property value="serv_type"/></td>
			<td class=column1><s:property value="vlanid"/>
			</td>
		</tr>
	</s:iterator>
	<tr bgcolor="#FFFFFF">
		<td colspan="7" align="right" class="green_foot" width="100%">
			<input type="button" onclick="javascript:gwShare_comfirmDevice();" class=jianbian name="gwShare_deviceConfirm" value=" 确 定 " />
			<input type="button" onclick="javascript:window.close();" class=jianbian name="gwShare_canel" value=" 取 消 " />
		</td>
	</tr>
</table>
<br>
</form>
</body>