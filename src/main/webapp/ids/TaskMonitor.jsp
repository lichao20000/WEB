<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<HEAD>
<title>������</title>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/CheckFormForm.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
function doSave(){
	
	$("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����Ŭ�������....");
    
	var task_starttime = $("#task_starttime").val();
	var task_peroid = $("#task_peroid").val();
	var mail_receiver= $("#mail_receiver").val();
	var mail_subject= $("#mail_subject").val();
	var mail_content= $("#mail_content").val();
	var monitor_type= $("#monitor_type").val();
	var monitor_content= $("#monitor_content").val();
	
	var url = "<s:url value='/ids/taskMonitor!addTaskMonitor.action' />";
	$("button[@name='button']").attr("disabled", true); 
    $.post(url,{
    	task_starttime : task_starttime,
    	task_peroid : task_peroid,
    	mail_receiver : mail_receiver,
    	mail_subject : mail_subject,
    	mail_content : mail_content,
    	monitor_type : monitor_type,
    	monitor_content : monitor_content
    },function(ajax){
    	 var message = ajax.split(",");
    	 $("div[@id='QueryData']").html("");		
    	 $("div[@id='QueryData']").append(message[1]);
 		 $("button[@name='button']").attr("disabled", false); 
    });	
}
</script>
</HEAD>
<body>
<FORM NAME="frm" action="" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" id="myTable">
	<TR>
		<TD HEIGHT="20">&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD colspan="4">
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162">
						<div align="center" class="title_bigwhite">������</div>
						</td>
						<td><img src="../images/attention_2.gif" width="15" height="12"></td>
					</tr>
				</table>
				</TD>
			</TR>
			
			<TR>
				<TD colspan="4" bgcolor="#999999">
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr align="center">
						<td colspan="4" class="green_title_left">
						�����������
						</td>
					</tr>
					<tbody>
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">��ؿ�ʼʱ�䣺(HH:mm)</TD>
							<TD width="35%" >
								<input type="text" id ="task_starttime" name="task_starttime"></input>
								<font color="#FF0000">*</font>
							</TD>
							<TD class=column align="right" nowrap width="15%">�������(��)��</TD>
							<TD width="35%" >
								<input type="text" id="task_peroid" name="task_peroid" class=bk value="">
							<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD class=column align="right" nowrap width="15%">��������:</TD>
							<TD width="35%" >
								<input type="text" id="mail_receiver" name="mail_receiver" class=bk value="">
							</TD>
							<TD class=column align="right" nowrap width="15%">�ʼ�����:</TD>
							<TD width="35%" >
								<input type="text" id="mail_subject" name="mail_subject" class=bk value="">
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">�ʼ�����:</TD>
							<TD width="35%" >
								<input id="mail_content" name="mail_content" class=bk value="" /> &nbsp;
							</TD>
							<TD class=column align="right" nowrap width="15%">�������:</TD>
							<TD width="35%" >
								<select id="monitor_type" name="monitor_type">
									<option value="1">sqlУ��</option>
									<option value="2">shell�ű�У��</option>
								</select>
							<font color="#FF0000">*</font>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" >
							<TD width="15%" class=column align="right">�������:</TD>
							<TD width="35%" colspan="3">
								<input type="text" id="monitor_content" name="monitor_content" value="" class=bk>&nbsp;
							</TD>
						</TR>
					</tbody>
					
					<TR align="left" id="doBiz" >
						<TD colspan="4" class=foot align="right" nowrap>
							<button type="button" name="subBtn" onclick="doSave()">��&nbsp;&nbsp;��</button>
						</TD>
					</TR>
					<tr id="trData" style="display: none">
						<td class=foot colspan="4">
							<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
								����Ŭ�������....
							</div>
						</td>
					</tr>
				</TABLE>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>
<%@ include file="../foot.jsp"%>