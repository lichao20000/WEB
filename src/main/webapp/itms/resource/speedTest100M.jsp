<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���ٽ��ͳ��</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<%=request.getContextPath()%>/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<% 
	request.setCharacterEncoding("GBK");
%>
<script type="text/javascript">
function isButn(flag)
{
	if(flag){
		$("button[@id='btn']").css("display", "");
	}else{
		$("button[@id='btn']").css("display", "none");
	}
}

function query() 
{
	isButn(true);
	var mainForm = document.getElementById("selectForm");
	mainForm.action = "<s:url value='/itms/resource/speedTest1000M!query.action' />";
	mainForm.submit();
}

function ToExcel() 
{
	var mainForm = document.getElementById("selectForm");
	mainForm.action = "<s:url value='/itms/resource/speedTest1000M!toExcel.action' />";
	mainForm.submit();
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids = [ "dataForm" ]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide = "yes"

function dyniframesize()
{
	var dyniframe = new Array()
	for (i = 0; i < iframeids.length; i++) {
		if (document.getElementById) {
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
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

$(function(){
	dyniframesize();
});

$(window).resize(function() {
	dyniframesize();
});
</script>
</head>

<body>
	<form id="selectForm" name="selectForm" action="" target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								 ������ٽ��ͳ��
							</td>
							<td>
								<img src="<s:url value="/images/attention_2.gif"/>"
									width="15" height="12" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">������ٽ��ͳ�� </th>
						</tr>
						<TR>
							<TD align="right" class=column width="15%">����</TD>
							<TD width="35%">
								<s:select list="cityList" name="cityId" headerKey="-1" headerValue="��ѡ������" 
								listKey="city_id" listValue="city_name" cssClass="bk">
							</s:select>
							</TD>
							<TD align="right" class=column width="15%">���ٽ��</TD>
							<TD align="left" width="35%">
								<select name="speedRet" value='<s:property value="speedRet"/>' class="bk" onchange="gwShare_change_select()">
									<option value="0" checked>ȫ��</option>
									<option value="1">���ٺϸ�</option>
									<option value="-1">���ٲ��ϸ�</option>
								</select>
							</TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%">
								<input type="text" name="startTime" readonly class=bk
									value="<s:property value="startTime" />">
								<img name="shortDateimg"
									onClick="WdatePicker({el:document.selectForm.startTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��">
							</TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%">
								<input type="text" name="endTime" readonly class=bk
									value="<s:property value="endTime" />">
								<img name="shortDateimg"
									onClick="WdatePicker({el:document.selectForm.endTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��">
							</TD>
						</TR>
						<TR>	
							<TD align="right" class=column width="15%">�������</TD>
							<TD width="35%">
								<select name="bandwidth" class="bk">
									<option value="1000M" checked>1000M</option>
								</select>
							</TD>
							<TD align="right" class="column" colspan=2> </TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button id="btn" onclick="query()">&nbsp;��&nbspѯ&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td>
					<iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src="">
					</iframe>
				</td>
			</tr>
			<tr><td height="25"></td></tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>