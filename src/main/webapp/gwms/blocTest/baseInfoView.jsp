<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������Ϣ��ѯ</title>
<%
	 /**
	 * ������Ϣ��ѯ
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2011-07-24
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">

/***
$(function(){
	setValue();
});
***/
	
function query(){
	document.selectForm.submit();
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

$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

</script>
</head>
<body>
	<form name="selectForm"
		action="<s:url value="/gwms/blocTest/baseInfoView!startQuery.action"/>"
		target="dataForm">
		<table >
			<tr>
				<td HEIGHT=20>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<th>
								������Ϣ��ѯ
							</th>
							<td>
								<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<tr leaf="simple">
							<th colspan="4">
								��Ϣ��ѯ
							</th>
						</tr>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								�ͻ�����
							</TD>
							<TD width="35%">
								<input type="input" name="customerName" size="25" class=bk />
							</TD>
							<TD class=column width="15%" align='right'>
								��ϵ�绰
							</TD>
							<TD width="35%">
								<input type="input" name="linkphone" size="25" class=bk />
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								����
							</TD>
							<TD width="35%">
								<select name="cityId" class="bk">
									<option value="-1">==��ѡ��==</option>
									<s:iterator value="cityIdList">
										<option value="<s:property value="city_id" />">
											==<s:property value="city_name" />==
										</option>
									</s:iterator>
								</select>
							</TD>
							<TD class=column width="15%" align='right'>
								IP��ַ
							</TD>
							<TD width="35%">
								<input type="input" name="loopbackIp" size="25" class=bk />
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								PPPoE�˺�
							</TD>
							<TD width="35%">
								<input type="input" name="username" size="25" class=bk />
							</TD>
							<TD class=column width="15%" align='right'>
								�豸���к�
							</TD>
							<TD width="35%">
								<input type="input" name="deviceSn" size="25" class=bk />
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								�������
							</TD>
							<TD width="35%">
								<input type="input" name="bssSheetId" size="25" class=bk />
							</TD>
							<TD class=column width="15%" align='right'>
								����״̬
							</TD>
							<TD width="35%">
								<SELECT name="result" class="bk">
									<option value="0">�ɹ�</option>
									<option value="1">ʧ��</option>
								</SELECT>
							</TD>
						</TR>
						<TR>
							<td colspan="4" class=foot>
								<button onclick="query()">&nbsp;��  ѯ&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25">
				</td>
			</tr>
			<tr>
				<td>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../foot.jsp"%>