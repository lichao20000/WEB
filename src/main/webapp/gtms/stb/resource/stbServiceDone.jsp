<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
、<%@page import="com.linkage.litms.LipossGlobals"%>
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
			$("#goPageForStbServiceDone").show().siblings("span").html("");
		}
	});
    
	// 山西联通不显示中间设备选择页面
	function deviceInfo(url){
		$.post(url,{},function(ajax){
			$("#chooseDevHtml").html("");
	   		$("#chooseDev").html("");
	   		$("#chooseDev").append(ajax);
	   		// 页面跳转
	   		$("#goPageForStbServiceDone").show().siblings("span").html("");
	   	});
	}
    
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("tr[@id='trqueryUser']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
			$("input[@name='tdDeviceSn']").val(returnVal[2][i][2]);
			$("input[@name='customer_id']").val(returnVal[2][i][7]);
			$("input[@name='serv_account']").val(returnVal[2][i][8]);
			$("input[@name='zero_account']").val(returnVal[2][i][10]);
			$("input[@name='oui']").val(returnVal[2][i][1]);
		}
		$("div[@id='trqueryConfig']").css("display","none");
		$("tr[@id='trqueryUser']").css("display","none");
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
	
	//输入框得到去焦点
	function inputFocus(s,message){
		var value = $.trim(s.value);
		s.style.color='#0000cc';
		if(value==''||value==message){
			
			s.value='';
		}
	}

	//输入框失去焦点
	function inputBlur(s,message){
		var value = $.trim(s.value);
		if(value==''||value==message){
			s.style.color='#888888';
			s.value=message;
		}
	}
	
	function config(device_id,deviceSn){
		var url = "<s:url value='/gtms/stb/resource/stbInst!getConfigInfo.action'/>";
		$.post(url,{
			deviceId:device_id,
			deviceSN:deviceSn
		},function(ajax){	
			userClear();
		    $("div[@id='div_config']").html("");
			$("div[@id='div_config']").append(ajax);
			$("tr[@id='trqueryConfig']").css("display","");
		});	
	}
	
	function checkUsernameEmpty() {
		if (($.trim(document.frm.servAccount.value) == ""||$.trim(document.frm.servAccount.value)=="完整业务账号")){
			alert('请输入业务账号！');
			document.frm.servAccount.focus();
			return false;
		}
		return true;
	}
	function configDetailInfo(deviceId, deviceSN){
		var page = "<s:url value='/gtms/stb/resource/stbInst!getConfigDetail.action'/>?deviceId=" + deviceId + "&deviceSN=" + deviceSN;
		window.open(page, "", "left=20,top=20,width=1000,height=450,resizable=yes,scrollbars=yes");
	}

	function solutions(resultId, deviceSN){
		var url = "<s:url value='/gtms/stb/resource/stbInst!getSolution.action'/>?deviceSN=" + deviceSN + "&resultId=" + resultId; 
		window.open(url, "", "left=50,top=50,width=600,height=450,resizable=yes,scrollbars=yes");
	}
	function configLog(deviceSN,deviceId,servTypeId,servstauts,wanType){
		var page = "<s:url value='/gtms/stb/resource/stbInst!getConfigLogInfo.action'/>?"
			+ "deviceSN=" + deviceSN
			+ "&deviceId=" + deviceId
			+ "&servTypeId=" + servTypeId
			+ "&servstauts=" + servstauts
			+ "&wanType=" + wanType;
		window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
	}
	
	//重新查询用户时时清空已选择的用户
	function userClear(){
		$("input[@name='customer_id']").val("");
		$("input[@name='serv_account']").val("");
		$("input[@name='typename']").val("");
		$("input[@name='userCityId']").val("");
		$("input[@name='oldDeviceId']").val("");
		$("div[@id='div_bind']").css("display","none");
	}	
	
	//针对选中用户时需要提交的数据
	function userOnclick(customer_id,serv_account,city_id){
		$("input[@name='customer_id']").val(customer_id);
		$("input[@name='serv_account']").val(serv_account);
		$("input[@name='userCityId']").val(city_id);
		
		$("div[@id='div_bind']").css("display","");
		$("div[@id='div_bind']").html("<button id='save_btn' onclick='CheckForms()'> 业务下发  </button>");

	}
	
	function CheckForms(){

		var deviceId = $("input[@name='deviceId']").val();
		var customer_id = $("input[@name='customer_id']").val();
		var deviceSN = $("input[@name='tdDeviceSn']").val();
		var zero_account = $("input[@name='zero_account']").val();
		var serv_account = $("input[@name='serv_account']").val();
		if(zero_account!=serv_account){
			zero_account = serv_account;
		}
		var oui = $("input[@name='oui']").val();
		if ("" == deviceId ){
			alert('请先选择一个设备！');
			return false;
		}
		if ("" == zero_account ){
			alert('机顶盒未绑定，请先进行绑定！');
			return false;
		}
		
		var message = "请确认！业务帐号："+zero_account+"，设备序列号："+$("input[@name='tdDeviceSn']").val();
		if (!confirm(message+'！是否继续业务下发?')){
			return false;
		}
		$("#tip_loading").show();
		$("tr[@id='trqueryConfig']").css("display","none");
		$("tr[@id='trqueryUser']").css("display","none");
		var url = "<s:url value='/gtms/stb/resource/stbInst!callPreProcess.action'/>";
		$.post(url,{
			deviceId:deviceId,
			customer_id:customer_id,
			oui:oui,
			deviceSN:deviceSN
		},function(ajax){	
			if (ajax == "1") {
				alert("调预读成功！");
			} else {
				alert("调预读失败！");
			}
			$("div[@id='tip_loading']").html("正在查询相关信息,请耐心等待......");
			var customer = window.setInterval(function getCustomerList(){
				url = "<s:url value='/gtms/stb/resource/stbInst!serviceDoneList.action'/>";
				$.post(url,{
					deviceId:deviceId,
					customer_id:customer_id,
					oui:oui,
					deviceSN:deviceSN
				},function(ajax){	
					$("#tip_loading").hide();
					$("div[@id='div_user']").html("");
					$("div[@id='div_user']").append(ajax);
					$("tr[@id='trqueryUser']").css("display","");
				});	
			},5000);
			setTimeout(function() {window.clearInterval(customer);},20000);
		});	
	}
	
	function configInfoClose(){
		$("tr[@id='trqueryConfig']").css("display","none");
	}
	
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
			width=24> 您当前的位置：手工业务下发</TD>
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
			<table width="100%" class="querytable">
				<tr>
					<td nowrap class=title_2 width="15%">设备序列号</td>
					<td id="tdDeviceSn" width="35%"></td>
					<td nowrap class=title_2 width="15%">属地</td>
					<td id="tdDeviceCityName" width="35%"></td>
					<input type="hidden" name="deviceId"  value="" />
					<input type="hidden" name="customer_id" value="" />
					<input type="hidden" name="serv_account" value="" />
					<input type="hidden" name="zero_account" value="" />
					<input type="hidden" name="tdDeviceSn" value="" />
					<input type="hidden" name="oui" value="" />
				</tr>
				<tr bgcolor="#FFFFFF">
						<td colspan="4" class=foot>
							<div align="right">
								<button id="reStart_btn" onclick="CheckForms()">业务下发</button>
							</div>
						</td>
					</tr>
				<tr>
					<td colspan="4">
						<div style="display: none; text-align: center;" id="tip_loading">
							正在进行手工业务下发,请耐心等待......
						</div>
					</td>
				</tr>
			</table>
		</td>
	</TR>
	<tr>
		<td HEIGHT=30></td>
	</tr>
	<TR id="trqueryUser" style="display: none">
		<td>
			<div  align="right" id="div_user" ></div>
		</td>
	</TR>
	<tr>
		<td HEIGHT=30></td>
	</tr>
	<TR id="trqueryConfig" style="display: none">
		<td>
			<div  align="center" id="div_config" ></div>
		</td>
	</TR>
	
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