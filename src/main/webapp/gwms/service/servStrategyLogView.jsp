<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>������ʷ���ò�ѯ</title>
<%
	/**
	 * ������ʷ���ò�ѯ
	 * 
	 * @author qixueqi(4174)
	 * @version 1.0
	 * @since 2009-03-05
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<lk:res />
<script type="text/javascript">

		var isHigh=false;
function query(){

var  username =	document.selectForm.username.value 
var  device_serialnumber =document.selectForm.device_serialnumber.value 
	if(isHigh){
		document.selectForm.username.value = "";
		document.selectForm.device_serialnumber.value = "";
		document.selectForm.submit();
	}
	else
	{  
		if(username=="" && device_serialnumber=="" )
		{
	    	alert("��������һ���ѯ����");
	   		return;
        }
		// ���ڽ��յ��ţ�����ѡ���������
		var instArea = $('#instArea').val();
		if(instArea == 'js_dx'){
			var  strategyType =	document.selectForm.strategyType.value 
			if(strategyType=="-1")
			{
		    	alert("��ѡ���������");
		     	return;
	        }
		}
		document.selectForm.submit();
	}
	
}

//������Ҫѡ��߼���ѯѡ��
function ShowDialog(leaf){
	//pobj = obj.offsetParent;
	oTRs = document.getElementsByTagName("TR");
	var m_bShow;
	var setvalueTemp = 0;
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	if(m_bShow){
		setvalueTemp = "1";
	}
	setValue(setvalueTemp);
	if(isHigh){
		isHigh=false;
	}
	else{
		isHigh=true;
	}
	document.selectForm.username.value = "";
	document.selectForm.device_serialnumber.value = "";
//	sobj = obj.getElementsByTagName("IMG");
//	if(m_bShow) {
//		sobj[0].src = "../images/up_enabled.gif";
//		obj.className="yellow_title";
//	}
//	else{
//		sobj[0].src = "../images/down_enabled.gif";
//		obj.className="green_title";
//	}
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
	document.selectForm.type.value = "";
	document.selectForm.status.value = "";
	document.selectForm.vendor_id.value = "";
	document.selectForm.service_id.value = "";
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

$(function(){
	var instArea = $('#instArea').val();
	if('js_dx' == instArea || 'jx_dx' == instArea){
		setValue(1);
	}
	
	if('jx_dx' == instArea){
		setValue(1);
		$("#titile").html("�û��˺�");
	}
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
<form name="selectForm"
	action="<s:url value="/gwms/service/servStrategyLog.action"/>"
	target="dataForm"><input type="hidden" name="selectType"
	value="0" />
<table>
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table class="green_gargtd">
			<tr>
				<td width="162" align="center" class="title_bigwhite">������ʷ���ò�ѯ
				</td>
				<td><img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12">�����״̬�ǡ�ִ����ɡ������Խ��δ�ɹ���������������</td>
				<td align="right">
				<ms:inArea areaCode="js_dx" notInMode="true">
				<button name="highselect" onclick="ShowDialog('high');">�߼���ѯ</button>
				</ms:inArea>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table class="querytable">

			<tr leaf="simple">
				<th colspan="4">���ٲ�ѯ</th>
			</tr>

			<!-- �򵥲�ѯ���� -->
			<TR leaf="simple">
				<TD class=column width="15%" align='right' id="titile">LOID</TD>
				<TD width="35%"><input type="input" name="username" size="30"
					class=bk /></TD>
				<TD class=column width="15%" align='right'>�豸���к�</TD>
				<TD width="35%"><input type="input" name="device_serialnumber"
					size="30" class=bk /></TD>
			</TR>
			<ms:inArea areaCode="js_dx,jx_dx" notInMode="false">
			<TR leaf="simple">
				<td class=column width="15%" align='right'>��ʼʱ��</td>
				<td width="35%">
					<input type="text" name="time_start" readonly class=bk size="30">
					<img name="shortDateimg" onClick="WdatePicker({el:document.selectForm.time_start,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��" />
				</td>
				<td class=column width="15%" align='right'>��ֹʱ��</td>
				<td width="35%">
					<input type="text" name="time_end" readonly class=bk size="30">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.selectForm.time_end,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��" />
				</td>
			</TR>
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
			<input type="hidden" name="datasource" value='<s:property value="datasource"/>'/>
			</ms:inArea>
			<!--					
					<tr bgcolor="#FFFFFF" leaf="simple">
						<td class=column width="15%">�����ˣ�</td>
						<td >
							<input type="input" name="operatesName" size="30"/>
						</td>
						<td colspan="2" align="center" class="green_foot">
							<input type="button" value="��  ѯ" onclick="query()">
						</td>
					</tr>
					
					<!-- �߼���ѯ���� -->
			<!--					<TR class="green_title" onclick="ShowDialog('high',this);">
						<TD colspan="4">
						<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
							<TR class=column>
								<TD><font size="3">�߼���ѯѡ��</font></TD>
								<TD align="right"><IMG SRC="../images/up_enabled.gif"
									WIDTH="7" HEIGHT="9" BORDER="0" ALT="">&nbsp;</TD>
							</TR>
						</TABLE>
						</TD>
					</TR> -->
			<tr leaf="high" style="display: none">
				<Th align="center" colspan="4">�߼���ѯ</Th>
			</tr>
			<!-- ������ڸ��������ͨ��ѯ��input name ��ͬ�ˣ�Ӱ����wdatepicker����ʾ���ʶ�ע�͵��� -->
			<%-- <tr leaf="high" style="display: none">
				<td class=column width="15%" align='right'>������ʼʱ��</td>
				<td width="35%"><lk:date id="time_start" name="time_start"
					type="all" /></td>
				<td class=column width="15%" align='right'>���ƽ�ֹʱ��</td>
				<td width="35%"><lk:date id="time_end" name="time_end"
					type="all" /></td>
			</tr> --%>
			<TR leaf="high" style="display: none">
				<TD class=column align='right'>���Է�ʽ</TD>
				<TD><select name="type" class="bk">
					<option value="">==��ѡ��==</option>
					<option value="0">==����ִ��==</option>
					<option value="1">==��һ������ϵͳ==</option>
					<option value="2">==�����ϱ�==</option>
					<option value="3">==��������==</option>
					<option value="4">==�´�����ϵͳ==</option>
					<option value="5">==�ն�����==</option>
				</select></TD>
				<td class=column width="15%" align='right'>����״̬</td>
				<td width="35%"><select name="status" class="bk">
					<option value="">==��ѡ��==</option>
					<option value="0">==�ȴ�ִ��==</option>
					<option value="1">==Ԥ��PVC==</option>
					<option value="2">==Ԥ���󶨶˿�==</option>
					<option value="3">==Ԥ������==</option>
					<option value="4">==ҵ���·�==</option>
					<option value="100">==ִ�����==</option>
				</select></td>
			</TR>
			<TR leaf="high" style="display: none">
				<TD class=column align='right'>����</TD>
				<TD><select name="vendor_id" class="bk">
					<option value="">==��ѡ��==</option>
					<s:iterator value="vendor_idLsit">
						<option value="<s:property value="vendor_id" />">== <s:property
							value="vendor_name" /> ==</option>
					</s:iterator>
				</select></TD>
				<TD class=column align='right'>ҵ������</TD>
				<td width="35%"><select name="service_id" class="bk">
					<option value="">==��ѡ��==</option>
					<s:iterator value="service_idLsit">
						<option value="<s:property value="serv_type_id" />">== <s:property
							value="serv_type_name" /> ==</option>
					</s:iterator>
				</select></td>
			</TR>
			<TR>
				<input type="hidden" id="instArea" value="<%=LipossGlobals.getLipossProperty("InstArea.ShortName") %>">
				<td colspan="4" align="right" class=foot>
				<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
				</td>
			</TR>
		</table>
		</td>
	</tr>
	<tr>
		<td height="25"></td>
	</tr>
	<tr>
		<td><iframe id="dataForm" name="dataForm" height="0"
			frameborder="0" scrolling="no" width="100%"
			src=""></iframe>
		</td>
	</tr>
</table>
<br>
</form>
</body>
</html>
<%@ include file="../foot.jsp"%>