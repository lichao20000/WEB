<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����־��ͼ</title>
<%
	 /**
	 * ����־��ͼ
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2010-05-21
	 * @category
	 */
%>
<link href="../../css/inmp/css/css_green.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../../css/inmp/css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../../css/inmp/css3/global.css" type="text/css">
<script type="text/javascript" src="../../Js/inmp/jquery.js"/></script>
<lk:res />
<script type="text/javascript">

$(function(){
	setValue();
});
	
	
function query(){
var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var username = $.trim($("input[@name='username']").val());
	if(""!=deviceSn && deviceSn.length<6){
		alert("�����������豸���кź���λ��");
		return false;
	}
	if(""==deviceSn && ""==username){
		alert("�û��ʺŻ��豸���к���������һ��!");
		return false;
	}
	document.selectForm.submit();
}

function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	document.selectForm.bindStartTime.value = year+"-"+month+"-"+day+" 00:00:00";
	document.selectForm.bindEndTime.value = year+"-"+month+"-"+day+" 23:59:59";
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
		action="<s:url value="/inmp/resource/bindLogView!startQuery.action"/>"
		target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
						<tr>
							<th width="162" align="center" class="title_bigwhite" nowrap>
								�ֳ���װ��Ϣ
							</th>
							<td nowrap>
								<img src="<s:url value="../../images/inmp/attention_2.gif"/>" width="15" height="12">
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
								�ֳ���װ��Ϣ
							</th>
						</tr>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column width="15%" align='right'>
								LOID
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
								��ʼʱ��
							</TD>
							<TD width="35%">
								<lk:date id="bindStartTime" name="bindStartTime" type="all" />
							</TD>
							<TD class=column width="15%" align='right'>
								����ʱ��
							</TD>
							<TD width="35%">
								<lk:date id="bindEndTime" name="bindEndTime" type="all" />
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
								
							</TD>
							<TD width="35%">
								
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF">
							<td align="right" colspan="4"  class="green_foot" width="100%">
								<input type="button" onclick="query()" class=jianbian value=" �� ѯ " style="margin-left: 1063" />
								<input type="reset" class=jianbian value=" �� �� " style="margin-left: 6"/>
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