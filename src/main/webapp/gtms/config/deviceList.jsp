<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����ʺŲ�ѯ</title>
<base target="_self"/>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
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
	
function gwShare_comfirmDevice(){
	var _rs = new Array();
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var gwShare_total = $("input[@name='gwShare_total']").val();
	var childDevice = "";
	var deviceSum = 0;
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
		if(confirm("ѡ��Ŀ���ʺ�����Ϊ:"+(gwShare_total-deviceSum)+",�Ƿ����?")){
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
			alert("��ѡ�����ʺţ�");
			return false;
		}
		if(confirm("ѡ��Ŀ���ʺ�����Ϊ:"+(deviceSum)+",�Ƿ����?")){
			
		}else{
			return;
		}
	}
	
	var alldata = childDevice.split("#");
	var rs_ = new Array();
	for(var i=0;i<alldata.length;i++){
		var device = alldata[i].split("|");
		rs_[i] = new Array(device[0],device[1]);
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
<table class="listtable" width="100%" align="center">
	<s:if test="'0'!=gwShare_RecordNum">
		<div style="text-align: center;width: 100%;height: 20px;" class="green_foot">��ѯ�������Ϊ��<s:property value="total"/>��</div>
	</s:if>
	<thead>
		<tr>
			<th align="center" width="5%">
				<input type="checkbox" name="gwShare_select" onclick="javascript:gwShare_rsselect();" />
			</th>
			<th align="center" width="47.5%">����ʺ�</th>
			<th align="center" width="47.5%">��������</th>
		</tr>
	</thead>
	<tbody>	
		<s:iterator value="deviceList">
			<tr bgcolor="#FFFFFF" align="center">
				<td class=column1>
					<input type="checkbox" name="radioDeviceId" 
						value="<s:property value="username"/>|<s:property value="netNum"/>" />
				</td>
				<td class=column1><s:property value="username"/></td>
				<td class=column1><s:property value="netNum"/></td>
			</tr>
		</s:iterator>
	</tbody>
	<tfoot>
		<tr bgcolor="#FFFFFF">
			<td colspan="7" align="right" class="green_foot" width="100%">
				<button onclick="javascript:gwShare_comfirmDevice();"> ȷ �� </button>
				<button onclick="javascript:window.close();"> ȡ �� </button>
			</td>
		</tr>
	</tfoot>
</table>
<br>
</form>
</body>