<%@page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>wifi�����ѯ�޸�</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/My97DatePicker/WdatePicker.js"></SCRIPT>
</head>
<style>
span {
	position: static;
	border: 0;
}
</style>
<body>
<form id="form" name="selectForm" action="<s:url value='/gtms/config/wifiPwdManageAction!queryAllInfo.action'/>"
			target="dataForm">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table id="searchLayer" width="98%" border=0 cellspacing=0
				cellpadding=0 align="center" style="display:">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td id="titleTd" width="162" align="center" class="title_bigwhite">wifi�����ѯ�޸�</td>
								<td>
									<img src="../../images/attention_2.gif" width="15" height="12">
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td bgcolor=#999999>
						<table border=0 cellspacing=1 cellpadding=2 width="100%">
							<tr>
								<th id="columTd" colspan="4" align="center">wifi�����ѯ�޸�</th>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="column" width='15%' align="right">����ʺ�</td>
								<td width='15%' align="right">
									<input type="text" id="username" name="username" /> <font color="red">*</font>
								</td>
								<td width='70%' align="left">
									<div id="result" name="result" />
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" style="CURSOR: hand; display: none" id="netNumtr">
								<td class="column" width='15%' align="right">�豸���к�</td>
								<td align="left" colspan="2">
									<div id="loidParam" name="loidParam"  style="display: none"/>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class=green_foot align="right" colspan="3">
									<div>
										<input type="button" name="button" class=jianbian
											value=" �� ѯ " style="CURSOR: hand" class=btn
											onclick="doQuery()" /> 
										<input type="button" id="sendBtn"
											class=jianbian name="sendBtn" value=" ��ȡ" class=btn
											onclick="doIssued()" style="CURSOR: hand; display: none" />
									</div>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td colspan="4" align="left" class="green_foot">
									<div id="resultDIV" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr/>
	<tr/>
	<tr>
		<td>
			<iframe id="dataForm" name="dataForm" height="0" width="98%" align=center
				frameborder="0" scrolling="no" src=""></iframe>
		</td>
	</tr>
		<tr>
			<td id="bssSheetInfo" align="center"  width="98%" colspan="3" ></td>
		</tr>
</table>
</form>
</body>
<script type="text/javascript">
//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"];

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
	   			dyniframe[i].style.display="block";
	   			//����û����������NetScape
	   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
	    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
	    			//����û����������IE
	   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
	    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
  			tempobj.style.display="block";
		}
	}
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

//���ݿ���ʺŲ�ѯ�Ƿ���Ҫ�ֹ���·��
function doQuery()
{
	var username = $.trim($("#username").val());
	if(username==""){
		alert("���������ʺ�");
		return ;
	}
	var url = "<s:url value='/gtms/config/wifiPwdManageAction!getServUserInfo.action'/>"; 
	$.post(url,
		   {username : username},
		   function(ajax){
		    	if(ajax.indexOf(",") > 0 ){
		    		var loidArray = ajax.split(",");
		    		
		    	    if('2'==loidArray[0]){
		    	    	$("#username").attr("disabled", true);
		    	    	$("#netNumtr").css("display","");
		    	    	$("#loidParam").css("display","");
		    	    	$("#loidParam").html(loidArray[1]);
		    	    	$("#result").html("��ҵ���û���Ӧ����û���");
		    	    	var instArea="<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
		    	    	if(instArea=='nx_dx'){
				   			$("#sendBtnParam").css("display","none");
				   			$("#closeBtnParam").css("display","none");
				   			$("#sendBtn").css("display","");
				   			$("#closeBtn").css("display","");
		    	    	}else{
		    	    		$("#sendBtnParam").css("display","");
			    	    	$("#closeBtnParam").css("display","");
			    	    	$("#sendBtn").css("disabled",true);
				   			$("#closeBtn").css("disabled",true);
		    	    	}
				   	}else if('1'==loidArray[0]){
			   			$("#result").html("��ҵ���û�����!");
			   			$("#loidParam").css("display","");
			   			$("#loidParam").html(loidArray[1]);
			   			$("#sendBtn").css("display","");
			   			$("#closeBtn").css("display","");
			   			$("#netNumtr").css("display","");
			   			$("#sendBtnParam").css("display","none");
			   			$("#closeBtnParam").css("display","none");
			   		}
		    	}else if('1'==ajax){
		   			$("#result").html("��ҵ���û�����!");
		   			$("#loidParam").css("display","");
		   			$("#loidParam").html(loidArray[1]);
		   			$("#sendBtn").css("display","");
		   			$("#closeBtn").css("display","");
		   			$("#netNumtr").css("display","");
		   			$("#sendBtnParam").css("display","none");
		   			$("#closeBtnParam").css("display","none");
		   		}else if('0'==ajax){
		   			$("#loidParam").html("");
		   			$("#result").html("���û������ڣ�");
		   			$("#sendBtn").css("disabled",true);
		   			$("#sendBtnParam").css("display","none");
		   			$("#closeBtnParam").css("display","none");
		   			$("#closeBtn").css("disabled",true);
		   		}else if('-1'==ajax){
		   			$("#loidParam").html("");
		   			$("#result").html("ҵ���û�������,���ȿ�ͨ���ҵ��");
		   			$("#sendBtn").css("disabled",true);
		   			$("#sendBtnParam").css("display","none");
		   			$("#closeBtnParam").css("display","none");
		   			$("#closeBtn").css("disabled",true);
		   		}else if('-2'==ajax){
		   			$("#loidParam").html("");
		   			$("#result").html("���Ȱ��豸��");
		   			$("#sendBtn").css("disabled",true);
		   			$("#sendBtnParam").css("display","none");
		   			$("#closeBtnParam").css("display","none");
		   			$("#closeBtn").css("disabled",true);
		   		}
		   });
}
	
function doIssued()
{
	var loid = $("input[name='device_serialnumber']:checked").val();
	if (typeof(loid) == "undefined") { 
  		alert("��ѡ���豸!"); 
  		return false;
	}
	 $("#sendBtn").css("disabled",true);
	 $("#sendBtn").attr("disabled",true); 
	document.selectForm.submit();
}
	
function wifiupdatePwd()
{
	var wifipath = $.trim($("#wifipathupdate").val());
	var wifipwd = $.trim($("#wifipwdupdate").val());
	var deviceid = $.trim($("#deviceidupdate").val());
	if(!confirm("ȷ�ϸ�������?"))
		return;
	var url = "<s:url value='/gtms/config/wifiPwdManageAction!updateWifiPwd.action'/>"; 
	$.post(url,{
		wifipath : wifipath,
		deviceid : deviceid,
		wifipwd : wifipwd
		},function(ajax){
				alert("���½��:"+ajax);
			});
}
</script>
</html>
<%@ include file="../../foot.jsp"%>