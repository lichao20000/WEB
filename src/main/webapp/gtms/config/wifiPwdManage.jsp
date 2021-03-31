<%@page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.LipossGlobals" %>
<%@taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>wifi密码查询修改</title>
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
								<td id="titleTd" width="162" align="center" class="title_bigwhite">wifi密码查询修改</td>
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
								<th id="columTd" colspan="4" align="center">wifi密码查询修改</th>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class="column" width='15%' align="right">宽带帐号</td>
								<td width='15%' align="right">
									<input type="text" id="username" name="username" /> <font color="red">*</font>
								</td>
								<td width='70%' align="left">
									<div id="result" name="result" />
								</td>
							</tr>
							<tr bgcolor="#FFFFFF" style="CURSOR: hand; display: none" id="netNumtr">
								<td class="column" width='15%' align="right">设备序列号</td>
								<td align="left" colspan="2">
									<div id="loidParam" name="loidParam"  style="display: none"/>
								</td>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td class=green_foot align="right" colspan="3">
									<div>
										<input type="button" name="button" class=jianbian
											value=" 查 询 " style="CURSOR: hand" class=btn
											onclick="doQuery()" /> 
										<input type="button" id="sendBtn"
											class=jianbian name="sendBtn" value=" 获取" class=btn
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
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
	   			dyniframe[i].style.display="block";
	   			//如果用户的浏览器是NetScape
	   			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
	    				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
	    			//如果用户的浏览器是IE
	   			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
	    				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
 			 }
 		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
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

//根据宽带帐号查询是否需要手工改路由
function doQuery()
{
	var username = $.trim($("#username").val());
	if(username==""){
		alert("请输入宽带帐号");
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
		    	    	$("#result").html("该业务用户对应多个用户！");
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
			   			$("#result").html("该业务用户存在!");
			   			$("#loidParam").css("display","");
			   			$("#loidParam").html(loidArray[1]);
			   			$("#sendBtn").css("display","");
			   			$("#closeBtn").css("display","");
			   			$("#netNumtr").css("display","");
			   			$("#sendBtnParam").css("display","none");
			   			$("#closeBtnParam").css("display","none");
			   		}
		    	}else if('1'==ajax){
		   			$("#result").html("该业务用户存在!");
		   			$("#loidParam").css("display","");
		   			$("#loidParam").html(loidArray[1]);
		   			$("#sendBtn").css("display","");
		   			$("#closeBtn").css("display","");
		   			$("#netNumtr").css("display","");
		   			$("#sendBtnParam").css("display","none");
		   			$("#closeBtnParam").css("display","none");
		   		}else if('0'==ajax){
		   			$("#loidParam").html("");
		   			$("#result").html("该用户不存在！");
		   			$("#sendBtn").css("disabled",true);
		   			$("#sendBtnParam").css("display","none");
		   			$("#closeBtnParam").css("display","none");
		   			$("#closeBtn").css("disabled",true);
		   		}else if('-1'==ajax){
		   			$("#loidParam").html("");
		   			$("#result").html("业务用户不存在,请先开通宽带业务");
		   			$("#sendBtn").css("disabled",true);
		   			$("#sendBtnParam").css("display","none");
		   			$("#closeBtnParam").css("display","none");
		   			$("#closeBtn").css("disabled",true);
		   		}else if('-2'==ajax){
		   			$("#loidParam").html("");
		   			$("#result").html("请先绑定设备！");
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
  		alert("请选择设备!"); 
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
	if(!confirm("确认更新密码?"))
		return;
	var url = "<s:url value='/gtms/config/wifiPwdManageAction!updateWifiPwd.action'/>"; 
	$.post(url,{
		wifipath : wifipath,
		deviceid : deviceid,
		wifipwd : wifipwd
		},function(ajax){
				alert("更新结果:"+ajax);
			});
}
</script>
</html>
<%@ include file="../../foot.jsp"%>