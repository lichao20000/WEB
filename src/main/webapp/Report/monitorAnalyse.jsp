<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ITMS��ط���</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	//** iframe�Զ���Ӧҳ�� **//
//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
//����iframe��ID
var iframeids=["selectform","dataForm"]

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
	timeDefault();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 
		

	function checkForm(){
		var sTime = $("input[@name='startDate']").val();
		var eTime = $("input[@name='endDate']").val();
		var date1=new Date(sTime);
		var date2=new Date(eTime);
		return(Date.parse(date1)>Date.parse(date2));
		
		
		
	}
	function query(){
		var mainForm = window.parent.document.getElementById("selectform");
		mainForm.action="<s:url value='/gwms/report/monitorAnalyse!queryMonitorAnalyse.action'/>";
		
		document.getElementById("dataForm").contentWindow.document.body.innerText = "";
		if(checkForm()){
			alert("����ʱ�䲻ӦС�ڿ�ʼʱ��!");
			return false;
		}
	}

	function showIp(t){
		if(t==4){
			
			
			$("#trPart").show();
			$("#ipTitle").show();
			$("#ipValue").show();
			
		}else{
			$("#trPart").hide();
			$("#ipTitle").hide();
			$("#ipValue").hide();
			
		}
	}
	
	function timeDefault(){
	}
	
function timeDefault(){
	var date = new Date();
	 var dt = date;
	 var seperator1 = "-"; 
	 var seperator2 = ":"; 
	 var year = date.getFullYear();
	 var month = date.getMonth() + 1;
   var strDate = date.getDate();
  
   if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
   
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    $("input[@name='startDate']").val(currentdate);
   $("input[@name='endDate']").val(currentdate);
}

</script>
</head>

<body>
	<form id="selectform" name="selectForm" action="<s:url value='/gwms/report/monitorAnalyse!queryMonitorAnalyse.action'/>" target="dataForm" onsubmit="return query()">
		<table border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd" >
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								ITMS��ط���</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> </td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">ITMS��ط���</th>
						</tr>

						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%">
									<input type="text" name="startDate" readonly value=""> 
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%">
									<input type="text" name="endDate" readonly value=""> 
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endDate,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>ָ������</TD>
							<TD width="35%">
								<select name="indexType" class=column onchange="showIp(this.options[this.options.selectedIndex].value)">
								<option value="1">socket��ʱ</option>
								<option value="2">�����ϱ�bind�¼�</option>
								<option value="3">Ƶ���ϱ�</option>
								<option value="4">acs������</option>
							</select>
							</TD>
							<TD class=column width="15%" align='right' id="ipTitle" STYLE="display:none">IP��ַ</TD>
							<TD width="35%" id="ipValue" STYLE="display:none">
								<s:select list="ipMsgList" name="ipSelect" id="ipSelect" listKey="hostip" listValue="hostValue" ></s:select>
							</TD>
						</TR>
						<TR id="trPart" STYLE="display:none">
							<TD class=column width="15%" align='right' id="conTitle" >����������</TD>
							<TD width="35%" id="conType" >
								<s:select list="conList" name="instance" id="instance"  headerKey="" headerValue="==ȫ��==" listKey="instance" listValue="instance" ></s:select>
							</TD>
							<TD class=column width="15%"   ></TD>
							<TD width="35%"  >
								
							</TD>
						</TR>
						<TR>
						
							<td colspan="4" align="right" class=foot>
								<button type="submit">&nbsp;��ѯ&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
		<td height="25"></td>
	</tr>
			<tr>
				<td align="center"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></td>
			</tr>
			
		</table>
		
	</form>
	
</body>
</html>
<%@ include file="../../foot.jsp"%>
