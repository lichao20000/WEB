<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s"  uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%
	String gw_type = request.getParameter("gw_type");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>��¼ʧ����־</title>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
var gw_type = "<%= gw_type%>"
$(function(){
	document.selectForm.submit();
});
	
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
		<style type="text/css">
<!--
select {
	position: relative;
	font-size: 12px;
	width: 160px;
	line-height: 18px;
	border: 0px;
	color: #909993;
}
-->
</style>
	</head>

	<body>
		<form name="selectForm" action="<s:url value="loginLog_cqdx.jsp"/>" target="dataForm">
			<input type="hidden" name="selectType" value="0" />
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
								<th>
									��¼ʧ����־
								</th>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">��¼ʧ����־��ѯҳ��
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">
							<tr leaf="simple">
								<th colspan="6">��¼ʧ����־��ѯ</th>
							</tr>
							<tr leaf="high" style="display: ">
								<td class=column width="10%" align='right'>
									�û���
								</td>
								<td width="24%">
									<input type="text" name="username" class=bk>
								</td>
								<td class=column width="10%" align='right'>
									��ʼʱ�䣺
								</td>
								<td width="23%">
									<input type="text" name="time_start" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({maxDate:'#F{$dp.$D(\'time_end\',{d:-0});}',el:document.selectForm.time_start,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��" />
								</td>
								<td class=column width="10%" align='right'>
									����ʱ��:
								</td>
								<td width="23%">
									<input type="text" name="time_end" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({minDate:'#F{$dp.$D(\'time_start\',{d:0});}',el:document.selectForm.time_end,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��" />
								</td>
							</tr>
							<TR>
								<td colspan="6" align="right" class=foot>
									<button onclick="query()">&nbsp;��  ѯ&nbsp;</button>
								</td>
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" frameborder="0" style="width: 100%" scrolling="no" src=""></iframe>
					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../foot.jsp"%>