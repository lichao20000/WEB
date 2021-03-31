<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
	String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<SCRIPT LANGUAGE="JavaScript">
	var area = '<%=area%>'
	$(function(){
		gwShare_setGaoji();
		var isGoPage = '<s:property value="isGoPage"/>';
	 	if(isGoPage != 1){
	 		$("#chooseDevHtml").html("");
		}else{
			// 页面跳转
			$("#goPageForStbRelease").show().siblings("span").html("");
		}
	});
	
	// 山西联通不显示中间设备选择页面
	function deviceInfo(url){
		$.post(url,{},function(ajax){
			$("#chooseDevHtml").html("");
	   		$("#chooseDev").html("");
	   		$("#chooseDev").append(ajax);
	   		
	   		$("#trDeviceResult").hide();
	   		$("#msgTr").hide();
	   		
	   		// 页面跳转
	   		$("#goPageForStbRelease").show().siblings("span").html("");
	   	});
	}
	
	function deviceResult(returnVal){
		if(area == "sx_lt")
		{
			frm.reset();
			$("#msgTr").hide();
		}
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("tr[@id='trqueryUser']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
			$("td[@id='tdDeviceSn']").val(returnVal[2][i][0]);
			$("td[@id='serv_account']").html(returnVal[2][i][8]);
			$("td[@id='oui']").html(returnVal[2][i][1]);
			$("input[@name='tdDeviceSn']").val(returnVal[2][i][2]);
			$("input[@name='customer_id']").val(returnVal[2][i][7]);
			$("input[@name='serv_account']").val(returnVal[2][i][8]);
			if(returnVal[2][i][9]==0){
				$("td[@id='allocatedstatus']").html("未绑定");
			}else{
				$("td[@id='allocatedstatus']").html("已绑定");
			}
		}
	}
	/*------------------------------------------------------------------------------
	//函数名:		queryChange
	//参数  :	change 1:简单查询、2:高级查询
	//功能  :	根据传入的参数调整显示的界面
	//返回值:		调整界面
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var url="<s:url value='/gtms/stb/resource/gwDeviceQueryStb!queryDeviceList.action'/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if(!has_showModalDialog) return;
		if(typeof(returnVal)=='undefined'){
			return;
		}else{
			$("input[@name='deviceId']").val(returnVal[0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	
	function CheckForm(){
		var obj = document.frm;
		if(obj.deviceId.value==""){
			alert("请查询设备！");
			return false;
		}
		return true;
	}
	
	function userOnclick(){
		$("div[@id='div_bind']").css("display","");
		$("div[@id='div_bind']").html("<button id='save_btn' type='button' onclick='CheckForms()'> 开 始 解 绑  </button>");
	}
	
	function CheckForms(){
		var deviceId = $("input[@name='deviceId']").val();
		var customer_id =$("input[@name='customer_id']").val();
		var deviceId =$("input[@name='deviceId']").val();
		if ("" == deviceId ){
			alert('请先选择一个设备！');
			return false;
		}
		if(customer_id == ""){
			alert("该设备未绑定，无需解绑!");
			return false;
		}else{
			var message = "请确认！业务帐号："+$("input[@name='serv_account']").val()+"，设备序列号："+$("input[@name='tdDeviceSn']").val();
			if (!confirm(message+'！是否继续?')){
				return false;
			}
			var url = "<s:url value='/gtms/stb/resource/stbInst!release.action'/>";
			$.post(url,{
				customer_id:customer_id,
				deviceId:deviceId
			},function(ajax){
				if(area == "sx_lt")
				{
					$("#msgTr").show();
					$("#msgStr").html(ajax);
				}else{
					alert(ajax);
				}
			});
		}
	}
	
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
			width=24> 您当前的位置：手工解绑</TD>
	</TR>
</TABLE>
<TABLE width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR>
		<td>
			<ms:inArea areaCode="sx_lt" notInMode="false">
				<%@ include file="/gtms/stb/share/gwShareDeviceQueryForSxlt.jsp"%>
			</ms:inArea>
			<ms:inArea areaCode="sx_lt" notInMode="true">
				<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
			</ms:inArea>
		</td>
	</TR>
	
	<tr>
		<td id="chooseDev"></td>
	</tr>
	
	<ms:inArea areaCode="sx_lt" notInMode="false">
	<tr id="chooseDevHtml">
		<td>
			<%@ include file="/gtms/stb/share/gwShareDeviceListForSxlt.jsp"%>
		</td>
	</tr>
	</ms:inArea>
	
	<TR id="trDeviceResult" style="display: none">
		<td>
			<table width="100%" class="listtable">
				<FORM NAME="frm" METHOD="POST" ACTION=""onSubmit="return CheckForm();">
					<input type="hidden" name="deviceId"  value="" />
					<input type="hidden" name="customer_id" value="" />
					<input type="hidden" name="serv_account" value="" />
					<input type="hidden" name="tdDeviceSn" value="" />
					<thead>
						<tr>
							<th align="center"nowrap class=title_2 width=10></th>
							<th align="center"nowrap class=title_2 >业务帐号</th>
							<th align="center"nowrap class=title_2 >属地</th>
							<th align="center"nowrap class=title_2 >绑定设备oui</th>
							<th align="center"nowrap class=title_2 >绑定设备序列号</th>
							<th align="center"nowrap class=title_2 >绑定状态</th>
						</tr>
						
					</thead>
					<tr>
						<td><input type="radio" name="radioUserId" value="1" onclick="userOnclick()" /></td>
						<td id="serv_account" width="15%"></td>
						<td id="tdDeviceCityName" width="15%"></td>
						<td id="oui"  width="35%"></td>
						<td id="tdDeviceSn" width="35%"></td>
						<td id="allocatedstatus" width="35%"></td>
					</tr>
					<tr>
						<td align="center" colspan="6">
							<div  align="right" id="div_bind" style="display: none"
								style="width: 100%; z-index: 1; top: 100px"></div>
						</td>
					</tr>
				</Form>
			</table>
		</td>
	</TR>
	<ms:inArea areaCode="sx_lt" notInMode="false">
		<tr style="background-color: #C6EBFE;height: 22.4px; display: none" id="msgTr">
			<td align="center" class="title_2" colspan="6" id="msgStr"></td>
		</tr>
	</ms:inArea>
</TABLE>
<script>
var gwShare_queryType = '<s:property value="gwShare_queryType"/>';
var gwShare_queryField = '<s:property value="gwShare_queryField"/>';
var gwShare_queryParam = '<s:property value="gwShare_queryParam"/>';
gwShare_queryChange(gwShare_queryType);
//参数类型
if(gwShare_queryType == 1){
	var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
	for(var i = 0; i < gwShare_queryField_arr.length; i++){
		var item = gwShare_queryField_arr[i];
		if(item.value ==  gwShare_queryField){
			item.checked = true;
		}
	}
	//参数
	$("input[@name='gwShare_queryParam']").val(gwShare_queryParam);
}
</script>

