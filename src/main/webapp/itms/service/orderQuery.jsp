<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������ѯ</title>
<%
	/**
	 * ������ѯ
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
		alert('�豸���к���LOID������һ����Ϊ��');
		return false;
	}
	
	/**
	//����ʱ��
	if(!IsNull(_devicesn.val(), "�豸���к�")){
		_devicesn.focus();
		return false;
	}
	if(!IsNull(_username.val(),"LOID")){
		_username��focus();
		return false;
	}
	*/
	document.selectForm.submit();
}



//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
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
								������ѯ</td>
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
							<TH colspan="4">������Ϣ��ѯ</TH>
						</TR>
						
						<TR>
							<%-- <TD class="column" width='15%' align="right">�豸���к�</TD>
							<TD width="35%"><input type="text" name="devicesn" size="20"
								maxlength="30" class=bk />&nbsp;<font color="red">*</font></TD>--%>
							<TD class="column" width="15%" align="right">LOID</TD>
							<TD width="35%"><input type="text" name="username" size="20"
								maxlength="30" class="bk" />&nbsp;<font color="red">*</font> </TD>
						</TR>
						<TR>
							<TD colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;��&nbsp;ѯ&nbsp;</button>
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