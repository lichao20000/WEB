<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�豸��ѯ</title>
<base target="_self"/>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
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

function ToExcel1() {
	var a = $("#gwShare_total").val();
	if(a > 100000){
		alert("���ݳ���10w��������������������ѡ���С��ʱ��Σ�");
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
			�����ѯ���
	</caption>
	<thead>
		<tr>
			<th align="center">loid</th>
			<th align="center">����˺�</th>
			<th align="center">�ɳ���</th>
			<th align="center">��SN</th>
			<!-- <th align="center">������汾</th>
			<th align="center">��Ӳ���汾</th> -->
			<th align="center">���ͺ�</th>
			<th align="center">�³���</th>
			<th align="center">��SN</th>
			
			<!-- <th align="center">������汾</th>
			<th align="center">��Ӳ���汾</th> -->
			<th align="center">���ͺ�</th>
			
			<s:if test="isNewNeed != 'yes'">
			  <th align="center">���ʱ��</th>
			</s:if>
			<th align="center">��ʱ��</th>
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
					<td colspan=14>û�и������豸��Ϣ</td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=14>ϵͳû�д��û�!</td>
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
		<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='�����б�'
			style='cursor: hand' onclick="ToExcel1()"></td>
	</tr>
	</tfoot>
</table>
<br>
</form>
</body>