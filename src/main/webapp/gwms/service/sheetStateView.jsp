<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����״̬��ѯ</title>
<%
	/**
		* ����״̬��ѯ
		* 
		* @author qixueqi(4174)
		* @version 1.0
		* @since 2009-06-16
		* @category
		*/
%>
<link href="<s:url value="../../css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="../../Js/jquery.js"/>"></script>
<script type="text/javascript" src="../../Js/jsDate/WdatePicker.js"></script>
<script type="text/javascript">

function query(){
	document.selectForm.submit();
	dyniframesize();
	
}

//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["dataForm"]

//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//�Զ�����iframe�߶�
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}


$(window).resize(function(){
	dyniframesize();
}); 

</script>

</head>

<body>
<form name="selectForm" action="<s:url value="/gwms/service/sheetStateView!startQuery.action"/>"  target="dataForm">
	<table border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
		<tr><td HEIGHT=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							����״̬��ѯ
						</td>
						<td>
							<img src="../../images/attention_2.gif" width="15" height="12">		
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center" bgcolor="#999999">
					
					<tr leaf="simple">
						<th colspan="4">����״̬��ѯ</th>
					</tr>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="15%">ҵ�����ͣ�</TD>
						<TD class=column width="35%">
							<select name="productSpecId" class="bk">
								<option value="60">==����ҵ��==</option>
							</select>
						</TD>
						<TD class=column width="15%">��ѡ�����أ�</TD>
						<TD class=column width="35%">
							<select name="city_id" class="bk">
								<s:iterator value="cityList">
									<option value="<s:property value="city_id" />">==<s:property value="city_name" />==</option>
								</s:iterator>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column width="15%">�������ͣ�</TD>
						<TD class=column width="35%">
							<select name="oper_type_id" class="bk">
								<option value="-1" selected="selected"></option>
								<s:iterator value="gwOperTypeList">
									<option value="<s:property value="oper_type_id" />">==<s:property value="oper_type_name" />==</option>
								</s:iterator>
							</select>
						</TD>
						<TD class=column width="15%">��״̬��</TD>
						<TD class=column width="35%">
							<select name="bind_state" class="bk">
								<option value="-1" selected="selected"></option>
								<option value="0">==δ��==</option>
								<option value="1">==�Ѱ�==</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td class=column width="15%">������ʼʱ�䣺</td>
						<td width="35%">
							<input type="text" name="startTime" value='<s:property value="startTime" />' readonly class=bk>
							<img name="shortDateimg" onclick="new WdatePicker(document.selectForm.startTime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="../../images/search.gif" width="15" height="12" border="0" alt="ѡ��">
						</td>
						<td class=column width="15%">���ƽ�ֹʱ�䣺</td>
						<td width="35%">
							<input type="text" name="endTime" value='<s:property value="endTime" />' readonly class=bk>
							<img name="shortDateimg" onclick="new WdatePicker(document.selectForm.endTime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="../../images/search.gif" width="15" height="12" border="0" alt="ѡ��">
						</td>
					</TR>
					
					<TR bgcolor="#FFFFFF">
						<TD class=column width="15%">����˺ţ�</TD>
						<TD class=column width="35%"><input type="input" name="username" size="30"/></TD>
						<TD class=column align="center" width="50%" colspan="4">
							<input type="button" value="��  ѯ" class="jianbian" onclick="query()"/>
						</TD>
					</TR>
					
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<br>
				<iframe id="dataForm" name="dataForm" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>
</body>
</html>