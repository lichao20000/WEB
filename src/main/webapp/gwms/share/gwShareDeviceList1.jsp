<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备查询</title>
<base target="_self"/>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
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
$(function() {
	parent.dyniframesize();
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
		rs_[i] = new Array(device[0],device[1],device[2],device[3],device[4],device[5],device[6],device[7],device[8],device[9],device[10],device[11],device[12],device[13],device[14]);
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

function ToExcel1() {
	var a = $("#gwShare_total").val();
	if(a > 100000){
		alert("数据超过10w条，不允许导出，请重新选择更小的时间段！");
		return;
	}else{
		parent.ToExcel1();
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
<form id="windowForm" name="windowForm" action="">
<input type="hidden" name="gwShare_queryResultType" value="<s:property value="gwShare_queryResultType"/>" />
<input type="hidden" id="gwShare_total" name="gwShare_total" value="<s:property value="total"/>" />
<input type="hidden" name="gwShare_param_one" 
	value="<s:property value="gwShare_queryType"/>|<s:property value="gwShare_queryField"/>
		  |<s:property value="gwShare_cityId"/>|<s:property value="gwShare_queryParam"/>
		  |<s:property value="gwShare_onlineStatus"/>|<s:property value="gwShare_vendorId"/>
		  |<s:property value="gwShare_deviceModelId"/>|<s:property value="gwShare_devicetypeId"/>
		  |<s:property value="gwShare_bindType"/>|<s:property value="gwShare_deviceSerialnumber"/>|linkage" />
		  
<table class="listtable" id="listTable">
	<caption>
			导入查询结果
	</caption>
	<thead>
		<tr>
			<th align="center">loid</th>
			<th align="center">宽带账号</th>
			<th align="center">旧厂家</th>
			<th align="center">旧SN</th>
			<!-- <th align="center">旧软件版本</th>
			<th align="center">旧硬件版本</th> -->
			<th align="center">旧型号</th>
			<th align="center">新厂家</th>
			<th align="center">新SN</th>
			
			<!-- <th align="center">新软件版本</th>
			<th align="center">新硬件版本</th> -->
			<th align="center">新型号</th>
			
			<s:if test="isNewNeed != 'yes'">
			  <th align="center">解绑时间</th>
			</s:if>
			<th align="center">绑定时间</th>
		</tr>
	</thead>
	<tbody>
		<s:if test="deviceList!=null">
			<s:if test="deviceList.size()>0">
				<s:iterator value="deviceList">
						<tr>
							<td class=column1><s:property value="loid"/></td>
							<td class=column1><s:property value="username"/></td>
							<td class=column1><s:property value="old_vendor_name"/></td>
							<td class=column1><s:property value="old_devsn"/></td>
							<%-- <td class=column1><s:property value="old_hardwareversion"/></td>
							<td class=column1><s:property value="old_softwareversion"/></td> --%>
							<td class=column1><s:property value="old_device_model"/></td>
							
							<td class=column1><s:property value="vendor_name"/></td>
							<td class=column1><s:property value="new_devsn"/></td>
							<%-- <td class=column1><s:property value="softwareversion"/></td>
							<td class=column1><s:property value="hardwareversion"/></td> --%>
							<td class=column1><s:property value="device_model"/></td>
							<s:if test="isNewNeed != 'yes'">
							   <td class=column1><s:property value="unbinddate"/></td>
							</s:if>
							<td class=column1><s:property value="binddate"/></td>
						</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=14>没有更换的设备信息</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=14>系统没有此用户!</td>
			</tr>
		</s:else>
	</tbody>

		<tfoot>
	<tr bgcolor="#FFFFFF">
		<td colspan="14" align="right">
			<lk:pages url="/gwms/service/gwBindDevQuery!getDeviceLists.action" styleClass="" showType="" isGoTo="true" changeNum="false"/>
		</td>
	</tr>
	<tr bgcolor="#FFFFFF">
		<td colspan="14" align="right">
		<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
			style='cursor: hand' onclick="ToExcel1()"></td>
	</tr>
	</tfoot>
</table>
<br>
</form>
</body>