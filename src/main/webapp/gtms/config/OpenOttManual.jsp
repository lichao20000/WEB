<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�ֹ���ͨOTTҵ��</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<link href="../../css/listview.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/My97DatePicker/WdatePicker.js"></SCRIPT>
		
</head>
<style>
span
{
	position:static;
	border:0;
}
</style>
<body>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
				<TABLE id="searchLayer" width="98%" border=0 cellspacing=0
					cellpadding=0 align="center" style="display: ">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td id="titleTd" width="162" align="center" class="title_bigwhite">
										OTTҵ�����
									</td>
									<td>
										<img src="../../images/attention_2.gif" width="15" height="12">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH id="columTd" colspan="3" align="center">
										OTTҵ�����
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column width="15%" align='right'>
										<SELECT name="searchType">
											<option value="1">
												LOID
											</option>
											<option value="2">
												��������˺�
											</option>
											<option value="3">
												VoIP��֤����
											</option>
											<option value="4">
												VoIP�绰����
											</option>
										</SELECT>
									</TD>
									<TD width='15%' align="left">
										<input type="text" id="username" name="username"/>
										<font color="red">*</font>
									</TD>
									<TD  width='70%' align="left">
										<div id="result" name="result"/>
									</TD>
								</TR>
							<!--  	<TR bgcolor="#FFFFFF"  style="CURSOR:hand;display: none" id = "netNumtr" >
									<TD class="column" width='15%' align="right">
										��������
									</TD>
									<TD width='15%' align="left">
										<SELECT id="netNum" name="netNum"  class="bk" >
											<OPTION value="-1">--��ѡ��--</OPTION>
											<OPTION value="4">4</OPTION>
											<OPTION value="8">8</OPTION>
								</SELECT>
										<font color="red">*</font>
									</TD>
									<TD  width='70%' align="left">
										<div id="result" name="result"/>
									</TD>
								</TR>  -->
								<tr bgcolor="#FFFFFF">
									<td class=green_foot align="right" colspan="3">
									<div>
										<input type="hidden" name="userId" id = "userId" value="">
										<input type="button" name="button" class=jianbian value=" �� ѯ "  
												style="CURSOR:hand" class=btn onclick="doQuery()"/>
										<input  type="button" id="sendBtn" class=jianbian name="sendBtn" value=" ��ͨOTT" 
												class=btn onclick="doIssued(1)" style="CURSOR:hand;display: none" />
									    <input  type="button" id="closeBtn" class=jianbian name="closeBtn" value=" �ر�OTT " 
												class=btn onclick="doIssued(0)" style="CURSOR:hand;display: none" />
									</div>	
									</td>
								</tr>
								<TR bgcolor="#FFFFFF">
									<TD colspan="3" align="left" class="green_foot">
										<div id="resultDIV" />
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
		</TD>
	</TR>
</TABLE>
</body>
<script type="text/javascript">

//���ݿ���ʺŲ�ѯ�Ƿ���Ҫ�ֹ���·��
	function doQuery(){
		$("#sendBtn").css("display","none");
		$("#closeBtn").css("display","none");
		$("#resultDIV").html("");
		var username = $.trim($("#username").val());
		var searchType = $("select[@name='searchType']").val();
		if(username==""){
			alert("�������ʺ�");
			return ;
		}
		var url = "<s:url value='/gtms/config/openOttServAction!getUserInfo.action'/>"; 
		$.post(url,
			   {username : username,
				searchType:searchType,
			    gw_type : <%=gwType%>},
			   function(ajax){
			    	
				   	if('0'==ajax){
				   		$("#result").html("���û������ڣ�");
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
				   	}
				   	else if("1"==ajax)
				   	{
				   		$("#result").html("���û�û�п��ҵ��");
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
				   	}
					else if("-2"==ajax)
				   	{
				   		$("#result").html("�û����ն���ֻ��һ��������");
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
				   	}
			   		else if('-1'==ajax){
			   			$("#result").html("���û��ѿ�ͨITVҵ��");
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
			   		}
			   		else {
			   			$("#userId").val(ajax);
			   			$("#sendBtn").css("display","");
			   			$("#closeBtn").css("display","");
			   		}
			   });
	}
	/**
	*���Ϳ�ͨ·�ɹ���	
	**/
	function doIssued(flag){
		var username = $.trim($("#username").val());
		$("#sendBtn").css("disabled",true);
		$("#closeBtn").css("disabled",true);
		
		var userId = $.trim($("#userId").val());
		
		var url = "<s:url value='/gtms/config/openOttServAction!doExecute.action'/>"; 
		$.post(url,
			   {
			    gw_type : <%=gwType%>,
			    flag : flag,
			    userId : userId
			    },
			   function(ajax){
			   		$("#sendBtn").css("disabled",false);
			   		$("#closeBtn").css("disabled",false);
			   		if('0'==ajax){
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
			   			$("#resultDIV").html("���ؽ���������ɹ���");
				   	}
			   		else
			   		{
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
			   			$("#resultDIV").html("���ؽ��������ʧ�ܡ�");
			   		}
			   		
			   });
	}
</script>
</html>
<%@ include file="../../foot.jsp"%>