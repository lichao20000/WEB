<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>��è���к�ƥ��״̬��ѯ</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
	var uname = $.trim($("input[@name='loid']").val());
	if(uname == null || uname.length == 0){
		$("input[@name='loid']").focus();
		alert("������LOID���в�ѯ");
		return false;
	}
	else
	{
		document.selectForm.submit();
	}
}

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

</script>
	</head>

	<body>
		<form id="form" name="selectForm" action="<s:url value='/itms/service/QueryDevsnCompareStatus!getStatusInfo.action'/>"
			target="dataForm">
			<table>
			<tr>
				<td HEIGHT=20>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									��è���к�ƥ��״̬��ѯ
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
									��ѯ��è���к�ƥ��״̬���
								</td>
							</tr>
						</table>
					</td>
				</tr>
			<TR>
				<td>
					<table class="querytable">
						<TR>
							<th colspan="4">
								��è���к�ƥ��״̬��ѯ
							</th>
						</tr>
						<TR>
							<TD class=column width="15%" align='right'>
								LOID
							</TD>
							<TD width="35%">
								<input type="text" name="loid" size="20" maxlength="30"
									class=bk />
									&nbsp;<font color="red"> *</font>
							</TD>
						<TR>
					</table>
				</td>
			</TR>
			<TR>
				<td colspan="4" align="right" class=foot>
					<input type="button" onclick='query()' value="�� ѯ"/>
				</td> 
			</TR>
			<tr>
				<td>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
						scrolling="no" width="100%" src=""></iframe>
				</td>
			</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>