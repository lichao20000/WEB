<%@ include file="../timelater.jsp"%>
<%@ page
	import="com.linkage.litms.common.database.*,java.util.List,java.util.ArrayList"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<meta http-equiv="x-ua-compatible" content="IE=7" >
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<%@ page contentType="text/html;charset=GBK"%>

<%
	String gw_type = request.getParameter("gw_type");
%>

<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

function checkform(){
	
	var completeStart_Time = document.form1.completeStart_Time.value;
	var completeEnd_Time = document.form1.completeEnd_Time.value;
	document.form1.completeStartTime.value = completeStart_Time;
	document.form1.completeEndTime.value =  completeEnd_Time;
	if(""!=completeStart_Time && null!=completeStart_Time){
		document.form1.completeStartTime.value = strTime2Second(completeStart_Time+" 0:0:0");
	}
	if(""!=completeEnd_Time && null!=completeEnd_Time){
		document.form1.completeEndTime.value = strTime2Second(completeEnd_Time+" 23:59:59");
	}
	$("#trData").show();
    $("#QueryData").html("���ڲ�ѯ�����Ե�....");
}

function strTime2Second(dateStr){
	
	var temp = dateStr.split(' ')
	var date = temp[0].split('-');   
    var time = temp[1].split(':'); 
	
	var reqReplyDate = new Date();
	reqReplyDate.setYear(date[0]);
    reqReplyDate.setMonth(date[1]-1);
    reqReplyDate.setDate(date[2]);
    reqReplyDate.setHours(time[0]);
    reqReplyDate.setMinutes(time[1]);
    reqReplyDate.setSeconds(time[2]);

	return Math.floor(reqReplyDate.getTime()/1000);
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
</SCRIPT>
<%@ include file="../head.jsp"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<form name="form1" action="DeviceStatByCompleteDealTimeList.jsp" method="post" target="dataForm" >
<input type="hidden" name="gw_type" value='<%=gw_type%>' />
	
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<TR>
						<TD width="220" align="left" class="title_bigwhite" >�������ʱ���ѯ�汾��Ϣ</TD>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD>
				<TABLE width="100%" height="30" border="0" cellspacing=1
					cellpadding=2 bgcolor=#999999>
					<TR>
						<td class=column bgcolor="#FFFFFF">�״�ע�Ὺʼʱ�� 
							<input type="text"	name="completeStart_Time" size="12" readonly class=bk> 
							<input type="hidden" name="completeStartTime" readonly class=bk> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.form1.completeStart_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> (YYYY-MM-DD) 
						</td>
                        <td class=column bgcolor="#FFFFFF">�״�ע�����ʱ�� 
                            <input	type="text" name="completeEnd_Time" size="12"	readonly class=bk> 
                            <input type="hidden" name="completeEndTime"	 readonly class=bk> 
                            <img name="shortDateimg" onClick="WdatePicker({el:document.form1.completeEnd_Time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
							src="../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> (YYYY-MM-DD)
						</td>
					</TR>
					<TR>
					<td colspan="4" align="right" class=foot>
						 <input type="submit" name="btn0" value=" ��  ѯ  "  class=btn onclick="javascript:checkform()">
						</td>
					</TR>
				</TABLE>
				</TD>
			</TR>
			<tr>
				<td height="20"></td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						����ͳ�ƣ����Ե�....
					</div>
				</td>
			</tr>
			<TR>
				<TD bgcolor=#999999>
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="yes" width="100%" src="">
					</iframe>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</form>
<%@ include file="../foot.jsp"%>
