<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<title>�����м������ѯ</title>
<script type="text/javascript">
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ]

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
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
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});

	function doQuery() {
		var form = document.getElementById("frm");
		form.action = "<s:url value='/gtms/stb/resource/CheckFruitQuery!Query.action'/>";
		form.submit();
	}
	function time()
	{
		var starttime = $.trim($("input[@name='starttime']").val());
		var endtime = $.trim($("input[@name='endtime']").val());
		var url = "<s:url value='/gtms/stb/resource/CheckFruitQuery!bijiao.action' />";
		$.post(url,{
			starttime:starttime,
			endtime:endtime
	    },function(ajax){
	    	if(ajax==1)
	    		{
	    		doQuery();
	    		}
	    	else
	    		{
	    		alert("ʱ���ȹ�������ʼʱ�������ʱ����1���ڣ�");
	    		}
	    });	
	}

</script>
</head>
<body>
	<form name="frm" id="frm" method="post" target="dataForm">
	<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								�����м������ѯ</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
			<td>
			<table class="querytable" >
				<TR >
					<TD class=column width="15%" align='right'>ҵ���ʺ�:</TD>
					<TD width="35%"><input type="text" maxlength="50" name="user_id"
						id="user_id" /></TD>
					<TD class=column width="15%" align='right'>MAC:</TD>
					<TD width="35%"><input type="text" maxlength="50" name="mac" id="mac" /></td>
				</TR>
				<TR>
							<TD class=column width="15%" align='right'>��ʼʱ��:</TD>
							<TD width="35%">
							<input type="text" name="starttime" readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> </TD>
							<TD class=column width="15%" align='right'>����ʱ��:</TD>
							<TD width="35%">
							<input type="text" name="endtime" readonly value="<s:property value='endtime'/>"> 
							<img name="shortDateimg" onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��" /> </TD>
				</TR>
				<TR>
				<TR>
							<td colspan="4" align="right" class=foot>
								<button onclick="time()">&nbsp;�� ѯ&nbsp;</button>
							</td>
						</TR>
			</table>
			</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25" id="configInfoEm" style="display: none"></td>
			</tr>
			<tr>
				<td id="configInfo"></td>
			</tr>
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
	</form>
</body>
</html>