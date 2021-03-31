<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������������</title>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

	function query() {
		var _avg_delay = $("input[@name='avg_delay']");
		var _appear_count = $("input[@name='appear_count']");
		//�жϲ�����Ϊ��,Ϊ������
		if(!IsNull(_avg_delay.val(), "ƽ���ӳ�")){
			_avg_delay.focus();
			return false;
		}
		if(!IsNull(_appear_count.val(), "���ִ���")){
			_appear_count.focus();
			return false;
		}
		$("#qy").attr('disabled',true);
		showMsgDlg();
		
		document.selectForm.submit();
	}
	
	// ���ƽ�����������
	function onlyNum(v){
		v.value=v.value.replace(/[^\d]/g,"");
	}
	
	function ToExcel(){
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/ids/NetWorkQualityTest!netWorkQualityExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/ids/NetWorkQualityTest!netWorkQualityInfo.action' />";
	}
	
	function appearCount(device_sn){
		var startOpenDate = $("input[@name='startOpenDate']").val();
		var endOpenDate = $("input[@name='endOpenDate']").val();
		<%--
		var avg_delay = $("input[@name='avg_delay']").val();
		var appear_count = $("input[@name='appear_count']").val();--%>
		var page="<s:url value='/ids/NetWorkQualityTest!netWorkQualityTestInfo.action' />?"+"device_sn="+device_sn
				+ "&startOpenDate=" + startOpenDate 
				+ "&endOpenDate=" + endOpenDate;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
	
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for (var i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i]);
				tempobj.style.display = "block";
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
	
	
	//��ʼ����ʱ�����
	function showMsgDlg(){
			w = document.body.clientWidth;
			h = document.body.clientHeight;
			
			l = (w-250)/2;
			t = h/2-100;
			PendingMessage.style.left = l;
			PendingMessage.style.top  = t;
			PendingMessage.style.display="";
	}

	//������ݣ�����ҳ��
	function closeMsgDlg(){
			$("#qy").attr('disabled',false);
			PendingMessage.style.display="none";
	}
	
	
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/ids/NetWorkQualityTest!netWorkQualityInfo.action'/>" target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							��������������</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">��������������</th>
						</tr>
	
						<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��</TD>
							<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
							<TD class=column width="15%" align='right'>����ʱ��</TD>
							<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
						</TR>
	
						<TR>
							<TD class=column width="15%" align='right'>ƽ���ӳ�:</TD>
							<TD width="35%">
							<input type="text" name="avg_delay" size="20" onkeyup="onlyNum(this);"
								maxlength="30" class=bk />&nbsp; <font color="red">*</font>
							</TD>
							<TD class=column width="15%" align='right'>���ִ���:</TD>
							<TD width="35%">
							<input type="text" name="appear_count" size="20" onkeyup="onlyNum(this);"
								maxlength="30" class=bk />&nbsp; <font color="red">*</font>
							</TD>
						</TR>
						<TR>
							<TD class=column width="15%" align='right'>������:</TD>
							<TD width="35%">
							<input type="text" name="loss_pp" size="20" onkeyup="onlyNum(this);"
								maxlength="10" class=bk />&nbsp;
							</TD>
							<TD class=column width="15%" align='right'>&nbsp;</TD>
							<TD width="35%">&nbsp;</TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button  id="qy" onclick="javaScript:query()"    >&nbsp;��&nbsp;��&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td>
				<div id="PendingMessage"
						style="position:absolute;z-index:3;top:240px;left:250px;width:300;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;filter:alpha(opacity=80);display:none">
							<center>
									<table border="0" style="background-color:#eeeeee">
										<tr>
											<td valign="middle"><img src="<s:url value='/images/cursor_hourglas.gif'/>"  
												border="0" WIDTH="30" HEIGHT="30"></td>
											<td>&nbsp;&nbsp;</td>
											<td valign="middle"><span id=txtLoading
												style="font-size:14px;font-family: ����">���Ե�,���ڷ�������������⡤������</span></td>
										</tr>
									</table>
							</center>
				</div>
				<iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>