<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ include file="../head.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>ҵ�����ò�����ͼ</title>
		<%
			 /**
			 * ҵ�����ò�����ͼ
			 * 
			 * @author qixueqi(4174)
			 * @version 1.0
			 * @since 2009-03-05
			 * @category
			 */
		%>
		<link href="<s:url value="../css/css_green.css"/>" rel="stylesheet"
			type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<lk:res />
		<script type="text/javascript">
function query(){
	document.selectForm.submit();
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
			action="<s:url value="/servStrategy/ServStrategyAlone.action"/>"
			target="dataForm">
			<input type="hidden" name="selectType" value="0" />
			<table border=0 cellspacing=0 cellpadding=0 width="98%"
				align="center">
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
								<td width="162" align="center" class="title_bigwhite">
									���Բ�ѯ
								</td>
								<td>
									<img src="../images/attention_2.gif" width="15" height="12">�����״̬�ǡ�ִ����ɡ������Խ��δ�ɹ���������������
								</td>
								<td align="right">
									<input type="button" value="�߼���ѯ" name="highselect"
										onclick="ShowDialog('high');" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table border=0 cellspacing=1 cellpadding=2 width="100%"
							align="center" bgcolor="#999999">

							<tr leaf="simple">
								<th colspan="4">
									���ٲ�ѯ
								</th>
							</tr>

							<!-- �򵥲�ѯ���� -->
							<TR bgcolor="#FFFFFF" leaf="simple">
								<TD class=column width="15%" align='right'>
									ҵ���˺�
								</TD>
								<TD width="35%">
									<input type="input" name="username" size="30" class=bk />
								</TD>
								<TD class=column width="15%" align='right'>
									�豸���к�
								</TD>
								<TD width="35%">
									<input type="input" name="device_serialnumber" size="30"
										class=bk />
								</TD>
							</TR>
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
							<tr bgcolor="#FFFFFF" leaf="high" style="display: none">
								<Th align="center" colspan="4">
									�߼���ѯ
								</Th>
							</tr>
							<tr bgcolor="#FFFFFF" leaf="high" style="display: none">
								<td class=column width="15%" align='right'>
									������ʼʱ��
								</td>
								<td width="35%">
									<lk:date id="time_start" name="time_start" type="all" />
								</td>
								<td class=column width="15%" align='right'>
									���ƽ�ֹʱ��
								</td>
								<td width="35%">
									<lk:date id="time_end" name="time_end" type="all" />
								</td>
							</tr>
							<TR bgcolor="#FFFFFF" leaf="high" style="display: none">
								<TD class=column align='right'>
									���Է�ʽ
								</TD>
								<TD>
									<select name="type" class="bk">
										<option value="">
											==��ѡ��==
										</option>
										<option value="0">
											==����ִ��==
										</option>
										<option value="1">
											==��һ������ϵͳ==
										</option>
										<option value="2">
											==�����ϱ�==
										</option>
										<option value="3">
											==��������==
										</option>
										<option value="4">
											==�´�����ϵͳ==
										</option>
										<option value="5">
											==�ն�����==
										</option>
									</select>
								</TD>
								<td class=column width="15%" align='right'>
									����״̬
								</td>
								<td width="35%">
									<select name="status" class="bk">
										<option value="">
											==��ѡ��==
										</option>
										<option value="0">
											==�ȴ�ִ��==
										</option>
										<option value="1">
											==Ԥ��PVC==
										</option>
										<option value="2">
											==Ԥ���󶨶˿�==
										</option>
										<option value="3">
											==Ԥ������==
										</option>
										<option value="4">
											==ҵ���·�==
										</option>
										<option value="100">
											==ִ�����==
										</option>
									</select>
								</td>
							</TR>
							<TR bgcolor="#FFFFFF" leaf="high" style="display: none">
								<TD class=column align='right'>
									����
								</TD>
								<TD>
									<select name="vendor_id" class="bk">
										<option value="">
											==��ѡ��==
										</option>
										<s:iterator value="vendor_idLsit">
											<option value="<s:property value="vendor_id" />">
												==
												<s:property value="vendor_name" />
												==
											</option>
										</s:iterator>
									</select>
								</TD>
								<TD class=column align='right'>
									ҵ������
								</TD>
								<td width="35%">
									<select name="service_id" class="bk">
										<option value="">
											==��ѡ��==
										</option>
										<s:iterator value="service_idLsit">
											<option value="<s:property value="serv_type_id" />">
												==
												<s:property value="serv_type_name" />
												==
											</option>
										</s:iterator>
									</select>
								</td>
							</TR>
							<TR>
								<td colspan="4" align="right" class='green_foot'>
									<input type="button" value=" ��  ѯ " onclick="query()" />
									&nbsp;&nbsp;
									<input type="reset" value=" ȡ �� " />
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
							src="<s:url value="/servStrategy/ServStrategyAlone.action"/>"></iframe>
					</td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>