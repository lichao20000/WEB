<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�ֹ�����·��</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="../../Js/My97DatePicker/WdatePicker.js"></SCRIPT>

</head>
<style>
span {
	position: static;
	border: 0;
}
</style>
<body>

	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR>
			<TD>
				<TABLE id="searchLayer" width="98%" border=0 cellspacing=0
					cellpadding=0 align="center" style="display:">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td id="titleTd" width="162" align="center"
										class="title_bigwhite">�ֹ���ͨ·��</td>
									<td><img src="../../images/attention_2.gif" width="15"
										height="12"></td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH id="columTd" colspan="4" align="center">�ֹ���ͨ·��</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD class="column" width='15%' align="right">����ʺ�</TD>
									<TD width='15%' align="right"><input type="text"
										id="username" name="username" /> <font color="red">*</font></TD>
									<TD width='70%' align="left">
										<div id="result" name="result" />
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" style="CURSOR: hand; display: none"
									id="netNumtr">
									<TD class="column" width='15%' align="right">��������</TD>
									<TD width='15%' align="left"><SELECT id="netNum"
										name="netNum" class="bk">
											<OPTION value="-1">--��ѡ��--</OPTION>
											<OPTION value="4">4</OPTION>
											<OPTION value="8">8</OPTION>
									</SELECT> <font color="red">*</font></TD>
									<TD width='70%' align="left">
										<div id="loidParam" name="loidParam"  style="display: none"/>
									</TD>
								</TR>
								<tr bgcolor="#FFFFFF">
									<td class=green_foot align="right" colspan="3">
										<div>
											<input type="button" name="button" class=jianbian
												value=" �� ѯ " style="CURSOR: hand" class=btn
												onclick="doQuery()" /> <input type="button" id="sendBtn"
												class=jianbian name="sendBtn" value=" ��ͨ·��" class=btn
												onclick="doIssued(1)" style="CURSOR: hand; display: none" />
												<input type="button" id="sendBtnParam"
												class=jianbian name="sendBtnParam" value=" ��ͨ·��" class=btn
												onclick="doIssued(2)" style="CURSOR: hand; display: none" />
											<input type="button" id="closeBtn" class=jianbian
												name="closeBtn" value=" �ر�·�� " class=btn
												onclick="doIssued(0)" style="CURSOR: hand; display: none" />
												<input type="button" id="closeBtnParam" class=jianbian
												name="closeBtnParam" value=" �ر�·�� " class=btn
												onclick="doIssued(3)" style="CURSOR: hand; display: none" />
										</div>
									</td>
								</tr>
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" align="left" class="green_foot">
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
		if(username==""){
			alert("���������ʺ�");
			return ;
		}
		var url = "<s:url value='/gtms/config/wirelessConfigAction!getServUserInfo.action'/>"; 
		$.post(url,
			   {username : username,
			    gw_type : <%=gwType%>},
			   function(ajax){
			    	if(ajax.indexOf(",") > 0 ){
			    		var loidArray = ajax.split(",");
			    	    if('2'==loidArray[0]){
			    	    	$("#username").attr("disabled", true);
			    	    	$("#netNumtr").css("display","");
			    	    	$("#loidParam").css("display","");
			    	    	$("#loidParam").html(loidArray[1]);
			    	    	$("#result").html("��ҵ���û���Ӧ����û���");
			    	    	$("#sendBtnParam").css("display","");
			    	    	$("#closeBtnParam").css("display","");
			    	    	$("#sendBtn").css("disabled",true);
				   			$("#closeBtn").css("disabled",true);
					   	}
			    	}else if('1'==ajax){
			   			$("#result").html("��ҵ���û��Ѵ���!");
			   			$("#loidParam").html("");
			   			$("#sendBtn").css("display","");
			   			$("#closeBtn").css("display","");
			   			$("#netNumtr").css("display","");
			   			$("#sendBtnParam").css("display","none");
			   			$("#closeBtnParam").css("display","none");
			   		}else if('0'==ajax){
			   			$("#loidParam").html("");
			   			$("#result").html("���û������ڣ�");
			   			$("#sendBtn").css("disabled",true);
			   			$("#sendBtnParam").css("display","none");
			   			$("#closeBtnParam").css("display","none");
			   			$("#closeBtn").css("disabled",true);
			   		}else if('-1'==ajax){
			   			$("#loidParam").html("");
			   			$("#result").html("ҵ���û�������,���ȿ�ͨ���ҵ��");
			   			$("#sendBtn").css("disabled",true);
			   			$("#sendBtnParam").css("display","none");
			   			$("#closeBtnParam").css("display","none");
			   			$("#closeBtn").css("disabled",true);
			   		}
			   		else if('-2'==ajax){
			   			$("#loidParam").html("");
			   			$("#result").html("���Ȱ��豸��");
			   			$("#sendBtn").css("disabled",true);
			   			$("#sendBtnParam").css("display","none");
			   			$("#closeBtnParam").css("display","none");
			   			$("#closeBtn").css("disabled",true);
			   		}
			   });
	}
	/**
	*���Ϳ�ͨ·�ɹ���	
	**/
	function doIssued(flag){
		var username = $.trim($("#username").val());
		var loid;
		if("2" == flag || "3" == flag){
			loid = $("input[name='LOID']:checked").val();
			if (typeof(loid) == "undefined") { 
   				alert("��ѡ��LOID!"); 
   				return false;
				} 
			if("2" == flag){
				flag = "1";
			}else{
				flag = "0";
			}
		}
		$("#sendBtn").css("disabled",true);
		$("#closeBtn").css("disabled",true);
		
		var netNum = $.trim($("#netNum").val());
		if("-1" == netNum)
		{
			alert("����������������");
			return false;
		}
		if(typeof(loid) == "undefined"){
			loid = "";
		}
		var url = "<s:url value='/gtms/config/wirelessConfigAction!doExecute.action'/>"; 
		$.post(url,
			   {username : username,
			    loidParam : loid,
			    gw_type : <%=gwType%>,
			    flag : flag,
			    netNum : netNum
			    },
			   function(ajax){
			   		$("#sendBtn").css("disabled",false);
			   		$("#closeBtn").css("disabled",false);
			   		$("#username").attr("disabled", "");
			   		var array  = ajax.split("|||");
			   		if(array.length > 2){
			   			$("#resultDIV").html("���ؽ����"+array[2]);
			   		}else{
			   			$("#resultDIV").html("���ؽ�����������ͳɹ�");
			   		}
			   });
	}
</script>
</html>
<%@ include file="../../foot.jsp"%>