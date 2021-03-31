<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>工单查询</title>
<%
	/**
	 * 工单查询
	 * 
	 * @author gaoyi
	 * @version 4.0.0
	 * @since 2013-08-02
	 * @category
	 */
%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript">

function query(){
	
	var _devicesn = $("input[@name='devicesn']");
	var _username = $("input[@name='username']");
	
	if(($.trim(_devicesn.val())).length>0 || ($.trim(_username.val())).length>0 ){
		
	}else{
		alert('设备序列号与LOID必须有一个不为空');
		return false;
	}
	
	/**
	//受理时间
	if(!IsNull(_devicesn.val(), "设备序列号")){
		_devicesn.focus();
		return false;
	}
	if(!IsNull(_username.val(),"LOID")){
		_username。focus();
		return false;
	}
	*/
	document.selectForm.submit();
}



//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids = [ "dataForm" ]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//如果用户的浏览器是NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//如果用户的浏览器是IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide == "no") {
			var tempobj = document.all ? document.all[iframeids[i]]
					: document.getElementById(iframeids[i])
			tempobj.style.display = "block"
		}
	}
}

$(function() {
	dyniframesize();
});

$(window).resize(function() {
	dyniframesize();
});
</script>
</head>
<body>

	<form id="form" name="selectForm" action="<s:url value='/itms/service/orderInfo!getOrderInfo.action' />"  target="dataForm">
		<table>
			<tr>
				<td height="20">&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								工单查询</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<TH colspan="4">工单信息查询</TH>
						</TR>
						
						<TR>
							<%-- <TD class="column" width='15%' align="right">设备序列号</TD>
							<TD width="35%"><input type="text" name="devicesn" size="20"
								maxlength="30" class=bk />&nbsp;<font color="red">*</font></TD>--%>
							<TD class="column" width="15%" align="right">LOID</TD>
							<TD width="35%"><input type="text" name="username" size="20"
								maxlength="30" class="bk" />&nbsp;<font color="red">*</font> </TD>
						</TR>
						<TR>
							<TD colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;查&nbsp;询&nbsp;</button>
							</TD>
						</TR>
					</table>
				</td>
			</tr>
			
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
		</table>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>