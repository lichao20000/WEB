<%@ include file="../../timelater.jsp"%>
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
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<script type="text/javascript">

$(function(){
	setValue();
});
	
	
function query(){
	var LOID = $.trim($("input[@name='username']").val());
	var deviceSn = $.trim($("input[@name='deviceSn']").val());
	var bindStartTime = $.trim($("input[@name='bindStartTime']").val());
	var bindEndTime = $.trim($("input[@name='bindEndTime']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	if(""!=deviceSn && deviceSn.length<6){
		alert("�����������豸���кź���λ��");
		return false;
	}
	if(""==deviceSn && ""==LOID){
		alert("�û��ʺŻ��豸���к���������һ��!");
		return false;
	}
	isShow();
	$("button[@name='button']").attr("disabled", true);
	$("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
	 var url = '<s:url value='/gwms/resource/bindLogView!startQuery.action'/>';
	 document.selectForm.submit();
	 $("div[@id='QueryData']").html("");
		$("button[@name='button']").attr("disabled", false);
	
}
function isShow() {
	$("tr[@id='trData']").show();
}
function setValue(){
	theday=new Date();
	day=theday.getDate()-1;
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	startDay=day;
	startMonth=theday.getMonth()+1;
	if(startDay==1){
		startMonth=theday.getMonth();
		if(startMonth==1||startMonth==3||startMonth==5||startMonth==7||startMonth==8||startMonth==10||startMonth==12){
			startDay=31;
		}else if(startMonth==2){
			if(year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
				startDay=28;
			}else{
				startDay=29;
			}
		}else{
			startDay=30;
		}
	}else{
		startDay=startDay-1;
	}
	
	document.selectForm.bindStartTime.value = year+"-"+startMonth+"-"+startDay+" 00:00:00";
	document.selectForm.bindEndTime.value = year+"-"+month+"-"+day+" 23:59:59";
}
function res(){
	$("input[@name='deviceSn']").val('');
	$("input[@name='username']").val('');
	var obj = document.getElementById("cityId");
	obj.options[0].selected=true;
	setValue();
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
		action="<s:url value="/gwms/resource/bindLogView!startQuery.action"/>"
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
								�ֳ���װ��Ϣ
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
								�ֳ���װ��Ϣ
							</th>
						</tr>
						<TR bgcolor="#FFFFFF" leaf="simple">
							<TD class=column name = "LOID" width="15%" align='right'>
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
								<select name="cityId" id="cityId" class="bk">
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
						<TR>
							<td colspan="4" class=foot>
								<button onclick="query()" name="button">&nbsp;��  ѯ&nbsp;</button>
								&nbsp;&nbsp;
								<button type="button" onclick="res()" >&nbsp;�� ��&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr id="trData" style="display: none">
				<td class="colum">
					<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
						����ͳ�ƣ����Ե�....
					</div>
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