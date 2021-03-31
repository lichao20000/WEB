<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>绑定保护查询</title>
<link href="<s:url value="/css3/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>


<script language="JavaScript">
<%
	String opr = (String)request.getParameter("opr");
	request.setCharacterEncoding("UTF-8");
%>

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

$(window).resize(function() {
	dyniframesize();
});


function doQuery(){
    var mac = $.trim($("input[@name='mac']").val());           
    var userName = $.trim($("input[@name='userName']").val());               
    var opr = '<%=opr%>';
	var frm = document.getElementById("frm");
	frm.action = "<s:url value='/gtms/stb/resource/stbBindProtect!query.action'/>";
	frm.submit();
 }


function Detail(username,mac)
{
	
	var page="<s:url value='/gtms/stb/resource/stbBindProtect!getdetail.action'/>?"+
	"mac="+mac+"&userName="+username;
	
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");

}
function Edit(username,mac,remark)
{
	var page="<s:url value='/gtms/stb/resource/bindProtectEdit.jsp'/>?"+
	"mac="+mac+"&userName="+username+"&remark="+remark;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function Delete(username,mac)
{
	var url = '<s:url value='/gtms/stb/resource/stbBindProtect!deleteData.action'/>'; 
	$.post(url,{
		mac:mac,
		userName:username
	},function(ajax){
		if(1==ajax)
		{
			alert("删除成功");
		}
		else
		{
			alert("删除失败");
		}
		window.location.reload();
	});
	
}

</script>
</head>
<body>
<form name="frm" id="frm" target="dataForm">
	<input type="hidden" name="opr" value='<%=opr%>'>
	<input type="hidden" name="showType" value=<s:property value="showType"/> >
	<table>
		<tr>
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							绑定保护查询</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="querytable" align="center">
					<tr>
						<th colspan="4">绑定保护查询</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" width='15%' align="right">业务账号：</td>
						<td width='35%' align="left">
						<input name="userName" type="text" class='bk' value="<s:property value='username'/>">
						</td>
						<td class="column" width='15%' align="right">设备MAC：</td>
						<td width='35%' align="left">
						<input name="mac" type="text" class='bk' value="<s:property value='mac'/>"> 
						</td>
					</tr>
					<tr>
						<td colspan="4" align="right" class="foot" width="100%">
							<div align="right">
								<button onclick="javascript:doQuery();" 
								name="gwShare_queryButton" style="CURSOR:hand"> 查 询 </button>&nbsp;&nbsp;
							</div>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td height="25" id="resultStr"></td>
		</tr>
		<tr>
			<td>
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>

</body>

