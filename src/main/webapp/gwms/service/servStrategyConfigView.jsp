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
		<title>�������ò�ѯ</title>
		<%
			/**
			 * �������ò�ѯ
			 * 
			 * @author qixueqi(4174)
			 * @version 1.0
			 * @since 2010-05-09
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var gw_type = "<%= gw_type%>"
$(function(){
	taskId = "<s:property value="taskId"/>";
	if(taskId!=""&&taskId!=null){
		document.selectForm.submit();
	}else{
	setValue(1);
	}
});
	
function query(){
	var device_serialnumber = $.trim($("input[@name='device_serialnumber']").val());
	var username = $.trim($("input[@name='username']").val());
	if(""!=device_serialnumber && device_serialnumber.length<6){
		alert("�����������豸���кź���λ��");
		return false;
	}
	if(""==device_serialnumber && ""==username){
		alert("�û��ʺŻ��豸���к���������һ��!");
		return false;
	}
	document.selectForm.submit();
}

function setValue(init){
	if(1==init)
	{
		theday=new Date();
		day=theday.getDate();
		month=theday.getMonth()+1;
		year=theday.getFullYear();
		
		document.selectForm.selectType.value = "1";
		document.selectForm.time_start.value = year+"-"+month+"-"+day+" 00:00:00";
		document.selectForm.time_end.value = year+"-"+month+"-"+day+" 23:59:59";
	}else{
		document.selectForm.selectType.value = "0";
		document.selectForm.time_start.value = "";
		document.selectForm.time_end.value = "";
	}
	//document.selectForm.status.value = "";
	//document.selectForm.vendor_id.value = "";
	//document.selectForm.service_id.value = "";
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
		<form name="selectForm" action="<s:url value="/gwms/service/servStrategyView.action"/>" target="dataForm">
			<input type="hidden" name="selectType" value="0" />
			<input type="hidden" name="taskId" value="<s:property value="taskId"/>" />
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
									�������ò�ѯ
								</th>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">ҳ���ѯʱ��Ϊ���Զ���ʱ�䣡
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">
							<tr leaf="simple">
								<th colspan="4">��ѯ</th>
							</tr>

							<TR leaf="simple">
								<TD class=column width="10%" align='right'>
									�û��˺�
								</TD>
								<TD width="30%">
									<input type="input" name="username" size="20" class=bk />
								</TD>
								<TD class=column width="10%" align='right'>
									�豸���к�
								</TD>
								<TD width="50%">
									<input type="input" name="device_serialnumber" size="20" class=bk />
									�����������豸���кź���λ��
								</TD>
							</TR>
							<tr leaf="high" style="display: ">
								<td class=column width="10%" align='right'>
									��ʼʱ��
								</td>
								<td width="30%">
									<input type="text" name="time_start" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.time_start,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��" />
								</td>
								<td class=column width="10%" align='right'>
									��ֹʱ��
								</td>
								<td width="50%">
									<input type="text" name="time_end" readonly class=bk>
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.time_end,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��" />
								</td>
							</tr>
							<TR leaf="simple">
				<td class=column width="15%" align='right'>��������</td>
				<td width="35%">
					<select name="strategyType">
						<option value="-1">--��ѡ��--</option>
						<option value="1" selected>ҵ���·�</option>
						<option value="2">�����������</option>
						<option value="3">��������</option>
						<option value="4">���������</option>
						<option value="5">�ָ���������</option>
					</select>
				</td>
			</TR>
							<TR>
								<td colspan="4" align="right" class=foot>
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
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%"
							src=""></iframe>
					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../foot.jsp"%>