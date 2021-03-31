<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>ipsec查询</title>
		<%
			 /**
			 * BSS业务查询
			 * 
			 * @author qixueqi(4174)
			 * @version 1.0
			 * @since 2010-09-08
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<%-- <script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script> --%>
		
		<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
		<SCRIPT type="text/javascript" SRC="../../Js/inmp/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/inmp/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
	configInfoClose();
	bssSheetClose();
	var uname = $.trim($("input[@name='username']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
		if(uname == null || uname.length == 0){
			$("input[@name='username']").focus();
			alert("请输入LOID等用户帐号信息进行查询");
			return false;
		}
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("input[@name='username']").val(Trim($("input[@name='username']").val()));
	document.selectForm.submit();
}


function ToExcel(){
	var usernameType=$("select[@name='cityId']").val();
	var username=Trim($("input[@name='username']").val());
	var cityId=$("select[@name='cityId']").val();
	var servTypeId=$("select[@name='servTypeId']").val();
	var openstatus=$("select[@name='openstatus']").val();
	var startOpenDate=$("input[@name='startOpenDate']").val();
	var endOpenDate=$("input[@name='endOpenDate']").val();
	var devicetype=$("select[@name='devicetype']").val();
	var user_type_id=$("select[@name='user_type_id']").val();
	var url = "<s:url value='/inmp/bss/bssSheetServ!getBssSheetServInfoCount.action'/>";
	$.post(url, {
		usernameType : usernameType,
		username : username,
		cityId : cityId,
		servTypeId : servTypeId,
		openstatus : openstatus,
		startOpenDate : startOpenDate,
		endOpenDate : endOpenDate,
		devicetype : devicetype,
		user_type_id: user_type_id
		}, function(ajax) {
			if(ajax>100000){
   				alert("数据量太大不支持导出 ");
   				return;
			}
			else{
				var mainForm = document.getElementById("form");
				mainForm.action="<s:url value='/inmp/bss/bssSheetServ!getBssSheetServInfoExcel.action'/>";
				mainForm.submit();
	    		mainForm.action="<s:url value='/inmp/bss/bssSheetServ!getBssSheetServInfo.action'/>";
			}
	});
}


function queryServerType(){
   var serValue = document.selectForm.servTypeId.value;
   if(serValue=="14"){
		this.tr_voipProtocalType.style.display="";
   }else{
   		this.tr_voipProtocalType.style.display="none";
   		document.selectForm.voipProtocalType.value="";
   }
}

function configInfoClose(){
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}

function configDetailClose(){
	$("td[@id='bssSheetDetail']").hide();
}

function bssSheetClose(){
	$("td[@id='bssSheetInfo']").hide();
}

function configDetailInfo(strategyId,deviceSN,servTypeId){
	servTypeId = servTypeId ? servTypeId : "";
	var page = "<s:url value='/inmp/bss/bssSheetServ!getConfigDetail.action'/>?strategyId="+strategyId+"&deviceSN="+deviceSN
			+ "&servTypeId="+servTypeId;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function solutions(result_id,deviceSN){
	var url = "<s:url value='/inmp/bss/bssSheetServ!getSolution.action'/>?&deviceSN="+deviceSN+"&result_id="+result_id;
	window.open(url,"","left=50,top=50,width=600,height=450,resizable=yes,scrollbars=yes");
}

function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts) {
	if (deviceId == "") {
		alert("用户未绑定设备，请先绑定设备，再激活！");
		return;
	}
	if (confirm('重新激活是将该业务置为未做状态，确实要重置吗?')) {
		var url = "<s:url value='/inmp/bss/bssSheetServ!callPreProcess.action'/>";
		$.post(url, {
			userId : userId,
			deviceSN : deviceSN,
			deviceId : deviceId,
			servTypeId : servTypeId,
			servstauts : servstauts,
			oui : oui
		}, function(ajax) {
			if (ajax == "1") {
				alert("调预读成功！");
				query();
			} else if (ajax == "-1") {
				alert("参数为空！");
			} else if (ajax == "-2") {
				alert("调预读失败！");
			}
		});
		//$("td[@id='temp123']").html("<font color='red'>未做</font>");
		//$('#resultStr',window.parent.document).html("<font color='red'>02586588146重新激活成功！</font>");
	}
}
function configLog(deviceSN,deviceId,servTypeId,servstauts,wanType){
	var page = "<s:url value='/inmp/bss/bssSheetServ!getConfigLogInfo.action'/>?"
		+ "deviceSN=" + deviceSN
		+ "&deviceId=" + deviceId
		+ "&servTypeId=" + servTypeId
		+ "&servstauts=" + servstauts
		+ "&wanType=" + wanType;
	window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
}

//根据需要选择高级查询选项
function ShowDialog(leaf){
	//pobj = obj.offsetParent;
	oTRs = document.getElementsByTagName("TR");
	var m_bShow;
	var setvalueTemp = 0;
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	if(m_bShow){
		setvalueTemp = "1";
	}
	setValue(setvalueTemp);
}

function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	$("input[@name='time_start']").val(year+"-"+month+"-"+day+" 00:00:00");
	$("input[@name='time_end']").val(year+"-"+month+"-"+day+" 23:59:59");
	
}

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
</script>
	</head>

	<body>
		<form id="form" name="selectForm" action="<s:url value='/itms/service/ipsecSheetServ!getIpsecSheetServInfo.action'/>"
			target="dataForm">
			<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
			<table>
				<tr>
					<td HEIGHT=20> &nbsp; </td>
				</tr>
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">ipsec查询</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
									查询ipsec开通情况
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">

							<TR>
								<th colspan="4">
									ipsec查询
								</th>
							</tr>

							<TR>
								<TD class=column width="15%" align='right'>
									<SELECT name="usernameType">
										<option value="1">
											LOID
										</option>
										<option value="2">
											上网宽带账号
										</option>
									</SELECT>
								</TD>
								<TD width="35%">
									<input type="text" name="username" size="20" maxlength="30"
										class=bk />
								</TD>
								<TD class=column width="15%" align='right'>
									属地
								</TD>
								<TD width="35%">
									<s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="请选择属地" listKey="city_id" listValue="city_name"
										 cssClass="bk"></s:select>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									开始时间
								</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/inmp/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									 结束时间
								</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/inmp/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									开通状态:
								</TD>
								<TD width="35%">
									<SELECT name="openstatus">
										<option value="">
											==请选择==
										</option>
										<option value="1">
											==成功==
										</option>
										<option value="0">
											==未做==
										</option>
										<option value="-1">
											==失败==
										</option>
									</SELECT>
								</TD>
							</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()">
										&nbsp;查 询&nbsp;
									</button>
								</td> 
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25" id="resultStr">

					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
					</td>
				</tr>
				<tr>
					<td height="25" id="configInfoEm" style="display: none">

					</td>
				</tr>
				<tr>
					<td id="configInfo">

					</td>
				</tr>
				<tr>
					<td height="25">

					</td>
				</tr>
				<tr>
					<td id="bssSheetInfo">

					</td>
				</tr>
				
				<tr>
					<td id="bssSheetDetail">

					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../foot.jsp"%>