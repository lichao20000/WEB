<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage" %>

<%@ include file="../../head.jsp"%>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="../../Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/prototype.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="../../Js/CheckFormForm.js"/>"></SCRIPT>
<script language="JavaScript">
$(function(){
	gwShare_setGaoji();
});


function doQuery(){  
	//alert("11");
	var device_id = $.trim($("input[@name='device_id']").val());
	//var tdDeviceSn = $.trim($("input[@name='tdDeviceSn']").val());
	//var stat_time = $.trim($("input[@name='stat_time']").val());
	//var logType = $.trim($("select[@name='logType']").val());
	//alert(device_id);
	if(device_id!=""){
		if(device_id < 6){
           	alert("���Ȳ�ѯ�豸!");
           	$("input[@name='device_id']").focus();
            return false;        
	    }
	}
		
    //if(stat_time==""){
    //	alert("�������ѯʱ��");
    //	//alert("�������ѯ��ʼʱ��");
	//	return false;
    //}
    
    //if(end_time==""){
    //	alert("�������ѯ����ʱ��");
	//	return false;
    //}
    
    //alert(tdDeviceSn);
    
	//alert($("input[@id='tdDeviceSn']").val());
	//alert("22");
	frm.submit();
	//alert("33");
	return true;
	//alert("44");
	
	
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    
    var url = '<s:url value='/bbms/report/SyslogQuery.action'/>'; 
	$.post(url,{
		tdDeviceId:device_id,
		tdDeviceSn:tdDeviceSn,
		stat_time:stat_time,
		logType:logType
	},function(ajax){	
	    $("div[@id='dataForm']").html("");
		$("div[@id='dataForm']").append(ajax);
	});
	
    //return true;
    
   
	
    
}

function deviceResult(returnVal){
	
	$("tr[@id='trDeviceResult']").css("display","");

	$("td[@id='tdDeviceSnShow']").html("");
	$("td[@id='tdDeviceCityName']").html("");
	
	for(var i=0;i<returnVal[2].length;i++){
		$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceSnShow']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
		$("input[@id='tdDeviceSn']").val(returnVal[2][i][1]+"-"+returnVal[2][i][2]);
		$("input[@id='tdDeviceId']").val(returnVal[2][i][0]);
		$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
	}
	
	$("tr[@id='trUserData']").show();
	var url = '<s:url value='/gwms/blocTest/QueryCustomerInfo!query.action'/>'; 
	$.post(url,{
		device_id:returnVal[2][0][0]
	},function(ajax){	
	    $("div[@id='UserData']").html("");
		$("div[@id='UserData']").append(ajax);
	});
	
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

<%@ include file="../../toolbar.jsp"%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
		<table width="95%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">Syslog��־��ѯ</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
				<table width="100%" border=0 align="center" cellpadding="1"
					cellspacing="1" bgcolor="#999999" class="text">
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
						<div id="selectDevice">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
						</div>
						</td>
					</TR>
					<tr>
						<td bgcolor="#FFFFFF" colspan="4">
						<FORM NAME="frm" METHOD="POST" ACTION="<s:url value="/bbms/report/SyslogQuery.action"/>" target="dataForm">
							<input id="tdDeviceId" name="tdDeviceId" type="hidden" value="">
							<input id="tdDeviceSn" name="tdDeviceSn" type="hidden" value="">
						<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
							<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column width="15%">
											�豸����
									</td>
									<td id="tdDeviceCityName">
									</td>
									<td nowrap align="right" class=column width="15%">
											�豸���к�
											<input type="hidden" name="textDeviceId" value="">
									</td>
									
									<td id="tdDeviceSnShow">
									</td>
							</TR>
							<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
												<td class="colum" colspan="4">
													<div id="UserData"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
							</tr>
							<TR bgcolor="#FFFFFF">
								<td class=column width="15%" align="right">��ѯ����&nbsp;</td>
								<td width="35%"><input type="text" name="stat_time"
									class='bk' readonly value="<s:property value='stat_time'/>">
								<img name="shortDateimg"
									onclick="WdatePicker({el:document.frm.stat_time,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									src="<s:url value='../../images/dateButton.png'/>" width="15"
									height="12" border="0" alt="ѡ��"></td>
								<td class=column width="15%" align="right">��־����&nbsp;</td>
								<td width="35%">
								<select name='logType' class='bk'>
									<s:iterator value="logTypes" status="status">
										<option value="<s:property value='logTypes[#status.index].type_id'/>"><s:property value='logTypes[#status.index].type_name'/></option>
									</s:iterator>
								</select>
								</td>
							</TR>
							<tr align="right" CLASS="green_foot">
								<td colspan="4"><INPUT TYPE="button" value=" �� ѯ �� ־ "
									class=jianbian onclick="doQuery()"> &nbsp;&nbsp; <INPUT
									TYPE="hidden" name="action" value="add"> &nbsp;&nbsp;</td>
							</tr>
						</table>
						</FORM>
						</td>
					</tr>
					<tr id="trData">
						<td colspan="4" valign="top" class=column>
							<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</TD>
	</TR>
	<tr>
			<td bgcolor=#ffffff>
				&nbsp;
			</td>
	</tr>
</TABLE>
<script language="JavaScript">
//��ʼ��ʱ��  ���� by zhangcong 2011-06-02
	var theday=new Date();
	var day=theday.getDate();
	var month=theday.getMonth()+1;
	var year=theday.getFullYear();
	document.getElementById("stat_time").value = year+"-" + month + "-" + day;
</script>
<%@ include file="/foot.jsp"%>
