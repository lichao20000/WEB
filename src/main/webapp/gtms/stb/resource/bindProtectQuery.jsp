<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�󶨱�����ѯ</title>
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

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide = "yes"

function dyniframesize() {
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document
					.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera) {
				dyniframe[i].style.display = "block"
				//����û����������NetScape
				if (dyniframe[i].contentDocument
						&& dyniframe[i].contentDocument.body.offsetHeight)
					dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
				//����û����������IE
				else if (dyniframe[i].Document
						&& dyniframe[i].Document.body.scrollHeight)
					dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
			}
		}
		//�����趨�Ĳ���������֧��iframe�����������ʾ����
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
			alert("ɾ���ɹ�");
		}
		else
		{
			alert("ɾ��ʧ��");
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
							�󶨱�����ѯ</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table width="100%" class="querytable" align="center">
					<tr>
						<th colspan="4">�󶨱�����ѯ</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" width='15%' align="right">ҵ���˺ţ�</td>
						<td width='35%' align="left">
						<input name="userName" type="text" class='bk' value="<s:property value='username'/>">
						</td>
						<td class="column" width='15%' align="right">�豸MAC��</td>
						<td width='35%' align="left">
						<input name="mac" type="text" class='bk' value="<s:property value='mac'/>"> 
						</td>
					</tr>
					<tr>
						<td colspan="4" align="right" class="foot" width="100%">
							<div align="right">
								<button onclick="javascript:doQuery();" 
								name="gwShare_queryButton" style="CURSOR:hand"> �� ѯ </button>&nbsp;&nbsp;
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

