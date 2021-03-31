<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%
	String area = LipossGlobals.getLipossProperty("InstArea.ShortName");
%>

<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
	var area = '<%=area%>'
	
	$(function(){
		gwShare_setGaoji();
		var isGoPage = '<s:property value="isGoPage"/>';
	 	if(isGoPage != 1){
	 		$("#chooseDevHtml").html("");
		}else{
			// ҳ����ת
			$("#goPageForStbInst").show().siblings("span").html("");
		}
	});
	
	// ɽ����ͨ����ʾ�м��豸ѡ��ҳ��
	function deviceInfo(url){
		$.post(url,{},function(ajax){
	   		$("#chooseDevHtml").html("");
	   		$("#chooseDev").html("");
	   		$("#chooseDev").append(ajax);
	   		
	   		$("#trqueryUser").hide();
	   		$("#msgTr").hide();
	   		// ҳ����ת
	   		$("#goPageForStbInst").show().siblings("span").html("");
	   	});
		
	}
	
	function deviceResult(returnVal){
		if(area == "sx_lt")
		{
			$("#msgTr").hide();
		}
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			if(area != 'sx_lt'){
				$("tr[@id='trDeviceResult']").css("display","");
			}
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
			$("input[@name='tdDeviceSn']").val(returnVal[2][i][2]);
			$("input[@name='customer_id']").val(returnVal[2][i][7]);
		}
		if(""!=$("input[@name='deviceId']").val() && ""!=$("input[@name='customer_id']").val()){
			$("tr[@id='trqueryUser']").css("display","none");
			$("#alreadyBind").css("display","");
		}
		else{
			$("tr[@id='trqueryUser']").css("display","");
			$("#alreadyBind").css("display","none");
		}
		document.all("tr_userinfo").style.display="none";
		$("div[@id='div_bind']").css("display","none");
		$("#resultFrame").hide("");
		$("div[@id='tip_loading']").html("");
	}
	/*------------------------------------------------------------------------------
	//������:		queryChange
	//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
	//����  :	���ݴ���Ĳ���������ʾ�Ľ���
	//����ֵ:		��������
	//˵��  :	
	//����  :	Create 2009-12-25 of By qxq
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
			if(area != 'sx_lt'){
				$("tr[@id='trDeviceResult']").css("display","");
			}
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	
	function CheckForm(){
		var obj = document.frm;
		if(obj.deviceId.value==""){
			alert("���ѯ�豸��");
			return false;
		}
		return true;
	}
	
	//�����õ�ȥ����
	function inputFocus(s,message){
		var value = $.trim(s.value);
		s.style.color='#0000cc';
		if(value==''||value==message){
			
			s.value='';
		}
	}

	//�����ʧȥ����
	function inputBlur(s,message){
		var value = $.trim(s.value);
		if(value==''||value==message){
			s.style.color='#888888';
			s.value=message;
		}
	}
	
	function nextstep(){
		if (!checkUsernameEmpty()) {
			return;
		}
		var servAccount = $.trim(document.frm.servAccount.value);
		if(servAccount=="����ҵ���˺�"){
			servAccount="";
		}
		var url = "<s:url value='/gtms/stb/resource/stbInst!getInstUserInfo.action'/>";
		$.post(url,{
			servAccount:servAccount
		},function(ajax){	
			userClear();
		    $("div[@id='div_user']").html("");
			$("div[@id='div_user']").append(ajax);
		});	
		document.all("tr_userinfo").style.display="";
		$("#resultFrame").hide("");
		$("div[@id='tip_loading']").html("");
	}
	
	function checkUsernameEmpty() {
		if (($.trim(document.frm.servAccount.value) == ""||$.trim(document.frm.servAccount.value)=="����ҵ���˺�")){
			alert('������ҵ���˺ţ�');
			document.frm.servAccount.focus();
			return false;
		}
		return true;
	}
	
	//���²�ѯ�û�ʱʱ�����ѡ����û�
	function userClear(){
		$("input[@name='customer_id']").val("");
		$("input[@name='serv_account']").val("");
		$("input[@name='typename']").val("");
		$("input[@name='userCityId']").val("");
		$("input[@name='oldDeviceId']").val("");
		$("div[@id='div_bind']").css("display","none");
		$("tr[@id='trqueryConfig']").css("display","none");
	}	
	
	//���ѡ���û�ʱ��Ҫ�ύ������
	function userOnclick(customer_id,serv_account,city_id){
		$("input[@name='customer_id']").val(customer_id);
		$("input[@name='serv_account']").val(serv_account);
		$("input[@name='userCityId']").val(city_id);
		
		$("div[@id='div_bind']").css("display","");
		$("div[@id='div_bind']").html("<button id='save_btn' type='button' onclick='CheckForms()'> �� ʼ �� ��  </button>");

	}
	
	function CheckForms()
	{
		var deviceId = $("input[@name='deviceId']").val();
		if ("" == deviceId ){
			alert('����ѡ��һ���豸��');
			return false;
		}
		
		var message = "��ȷ�ϣ�ҵ���ʺţ�"+$("input[@name='serv_account']").val()+"���豸���кţ�"+$("input[@name='tdDeviceSn']").val();
		if (!confirm(message+'���Ƿ����?')){
			return false;
		}
		var url = "<s:url value='/gtms/stb/resource/stbInst.action'/>";
		var customer_id =$("input[@name='customer_id']").val();
		var servAccount =$("input[@name='servAccount']").val();
		var deviceId =$("input[@name='deviceId']").val();
		$("#tip_loading").show();
		$("div[@id='tip_loading']").html("���ڽ����ֹ��󶨣������ĵȴ�.....");
		$.post(url,{
			customer_id:customer_id,
			servAccount:servAccount,
			deviceId:deviceId
		},function(ajax){
			$("div[@id='tip_loading']").html("");
			if(area == "sx_lt")
			{
				$("#msgTr").show();
				$("#msgStr").html(ajax);
			}
			else
			{
				alert(ajax);
				var customer = window.setInterval(function getCustomerList(){
					$("#resultFrame").hide("");
					url = "<s:url value='/gtms/stb/resource/stbCustomer!listCustomer.action'/>?customerDTO.servAccount="+servAccount+"&customerDTO.cityId=00&customerDTO.subordinate=1";
					$("#frm").attr("action", url);
					$("#frm").attr("target", "resultFrame");
					$("#frm").submit();
				},3000);
				setTimeout(function() {window.clearInterval(customer);},7000);
			}
		});	
	}
	
	
	function setDataSize(dataHeight) {
		$("#resultFrame").height(dataHeight);
	}

	function showIframe() {
		$("#tip_loading").hide();
		$("#resultFrame").show();
	}
	
	function config(device_id,deviceSn){
		
		var url = "<s:url value='/gtms/stb/resource/stbInst!getConfigInfo.action'/>";
		$.post(url,{
			deviceId:device_id,
			deviceSN:deviceSn
		},function(ajax){	
		    $("div[@id='div_config']").html("");
			$("div[@id='div_config']").append(ajax);
			$("tr[@id='trqueryConfig']").css("display","");
		});	
	}
	
	function configInfoClose(){
		$("tr[@id='trqueryConfig']").css("display","none");
	}
	
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
			width=24> ����ǰ��λ�ã��ֹ���װ</TD>
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
					<td nowrap class=title_2 width="15%">�豸���к�</td>
					<td id="tdDeviceSn" width="35%"></td>
					<td nowrap class=title_2 width="15%">����</td>
					<td id="tdDeviceCityName" width="35%"></td>
				</tr>
			</table>
		</td>
	</TR>
	<tr>
		<td HEIGHT=10></td>
	</tr>
	<TR id="alreadyBind" style="display: none; background-color: #C6EBFE;height: 22.4px;" >
		<td colspan="4" class="title_2" align="center" >���û������豸�Ѿ���</td>
	</TR>
	<TR id="trqueryUser" style="display: none">
		<TD>
			<FORM name="frm" id="frm" method="POST">
				<input type="hidden" name="deviceId"  value="" />
				<input type="hidden" name="customer_id" value="" />
				<input type="hidden" name="serv_account" value="" />
				<input type="hidden" name="tdDeviceSn" value="" />
				<table width="100%" class="querytable">
					<tr>
						<td colspan="4" class="title_1">�� �� �� ѯ</td>
					</tr>
					<tr>
						<td class=title_2 width="15%">ѡ������</td>
						<td width="35%">ҵ���˺�</td>
						<td class=title_2 width="15%">��ѯֵ</td>
						<td width="35%"><input type="text" size="35" name="servAccount"
							class="bk" value="����ҵ���˺�" onfocus="inputFocus(this,'����ҵ���˺�')"
							onblur="inputBlur(this,'����ҵ���˺�')" /></td>
					</tr>
					<tr bgcolor="#FFFFFF">
						<td colspan="4" class=foot>
							<div align="right">
								<input type="button" id="reStart_btn" onclick="nextstep()" value="��һ��"></input>
							</div>
						</td>
					</tr>
					<tr style="display: none" id="tr_userinfo">
						<td colspan = "4">
							<div id="div_user" style="width: 100%; z-index: 1; top: 100px"></div>
						</td>
					</tr>
					<tr>
						<td align="center" colspan="4">
							<div  align="right" id="div_bind" style="display: none"
								style="width: 100%; z-index: 1; top: 100px"></div>
						</td>
					</tr>
				</table>
			</FORM>
			<iframe id="resultFrame" name="resultFrame" width="100%" frameborder="0" scrolling="no" align="center"></iframe>
		</TD>
	</TR>
	<TR id="trqueryConfig" style="display: none">
		<td>
			<div  align="center" id="div_config" ></div>
		</td>
	</TR>
	<tr>
		<td>
			<div style="width: 100%; display: none; text-align: center;"
					id="tip_loading">
					���ڼ�������,�����ĵȴ�......
				</div>
		</td>
	</tr>
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
//��������
if(gwShare_queryType == 1){
	var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
	for(var i = 0; i < gwShare_queryField_arr.length; i++){
		var item = gwShare_queryField_arr[i];
		if(item.value ==  gwShare_queryField){
			item.checked = true;
		}
	}
	//����
	$("input[@name='gwShare_queryParam']").val(gwShare_queryParam);
}
</script>
