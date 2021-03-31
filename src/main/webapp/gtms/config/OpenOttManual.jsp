<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>手工开通OTT业务</title>
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
										OTT业务操作
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
										OTT业务操作
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class=column width="15%" align='right'>
										<SELECT name="searchType">
											<option value="1">
												LOID
											</option>
											<option value="2">
												上网宽带账号
											</option>
											<option value="3">
												VoIP认证号码
											</option>
											<option value="4">
												VoIP电话号码
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
										上网个数
									</TD>
									<TD width='15%' align="left">
										<SELECT id="netNum" name="netNum"  class="bk" >
											<OPTION value="-1">--请选择--</OPTION>
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
										<input type="button" name="button" class=jianbian value=" 查 询 "  
												style="CURSOR:hand" class=btn onclick="doQuery()"/>
										<input  type="button" id="sendBtn" class=jianbian name="sendBtn" value=" 开通OTT" 
												class=btn onclick="doIssued(1)" style="CURSOR:hand;display: none" />
									    <input  type="button" id="closeBtn" class=jianbian name="closeBtn" value=" 关闭OTT " 
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

//根据宽带帐号查询是否需要手工改路由
	function doQuery(){
		$("#sendBtn").css("display","none");
		$("#closeBtn").css("display","none");
		$("#resultDIV").html("");
		var username = $.trim($("#username").val());
		var searchType = $("select[@name='searchType']").val();
		if(username==""){
			alert("请输入帐号");
			return ;
		}
		var url = "<s:url value='/gtms/config/openOttServAction!getUserInfo.action'/>"; 
		$.post(url,
			   {username : username,
				searchType:searchType,
			    gw_type : <%=gwType%>},
			   function(ajax){
			    	
				   	if('0'==ajax){
				   		$("#result").html("该用户不存在！");
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
				   	}
				   	else if("1"==ajax)
				   	{
				   		$("#result").html("该用户没有宽带业务");
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
				   	}
					else if("-2"==ajax)
				   	{
				   		$("#result").html("用户的终端是只有一个上网口");
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
				   	}
			   		else if('-1'==ajax){
			   			$("#result").html("该用户已开通ITV业务");
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
	*发送开通路由工单	
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
			   			$("#resultDIV").html("返回结果：操作成功！");
				   	}
			   		else
			   		{
			   			$("#sendBtn").css("disabled",true);
			   			$("#closeBtn").css("disabled",true);
			   			$("#resultDIV").html("返回结果：操作失败。");
			   		}
			   		
			   });
	}
</script>
</html>
<%@ include file="../../foot.jsp"%>