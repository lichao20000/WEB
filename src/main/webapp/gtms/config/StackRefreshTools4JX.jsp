<%--
Author      : �ο���
Date		: 2015-05-25
Desc		: ���DHCP�Ĺر�
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
 String gwType = "1";
 String do_type = "2";
 String type = request.getParameter("type");
 String typeName = "";
 if("1".equals(type)){
	 typeName = "��ջ";
 }else if("2".equals(type)){
	 typeName = "˫ջ";
 }else{
	 response.sendRedirect("../../login.jsp");
 }
 %> 
<html>
<head>
<script language="JavaScript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript">
$(function(){
	$("#doButton").attr("disabled",false);
	do_type = <%=do_type%>
	gwShare_queryChange('3');
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
function CheckForm(){
	if($("input[@name='gwShare_fileName']").val()==""){
		alert("���ϴ��ļ���");
		return false;
	}
	return true;
}


$(function(){
	$("button[@name='button']").attr("disabled", true);
	var starttime = "00:00:00";
	var endtime = "23:59:59";
	starttime = parseInt(starttime.split(":")[0]) * 3600 + parseInt(starttime.split(":")[1]) * 60 + parseInt(starttime.split(":")[2]);
	endtime = parseInt(endtime.split(":")[0]) * 3600 + parseInt(endtime.split(":")[1]) * 60 + parseInt(endtime.split(":")[2]);
	var currenttime = parseInt(new Date().toLocaleTimeString().split(":")[0]) * 3600 + parseInt(new Date().toLocaleTimeString().split(":")[1]) * 60 + parseInt(new Date().toLocaleTimeString().split(":")[2]);
	do_type = <%=do_type%>;
	if(do_type == 2){
		/* if(!(currenttime >= starttime && currenttime <= endtime)){
			$("#doButton,#query,#reset,#strategy_type").attr("disabled",true);
			$("#msg").html("�˹��ܽ���18:00-23:59ʹ��");
			$("#gwShare_tr31").hide();
		} */
	}
});
function doExecute(){
	var strategy_type  = $("select[@id='strategy_type']").val();
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	
	if(CheckForm()){
	var url = "<s:url value="/gtms/config/stackRefreshTools!doConfigAll4JX.action"/>";
	$("tr[@id='trData']").show();
	$("#doButton").attr("disabled",true);
	$("div[@id='QueryData']").html("���ڲ��������Ե�....");
		$.post(url,{
            strategy_type : strategy_type,
            gwShare_fileName : gwShare_fileName,
            gw_type: <%=gwType%>,
            type: <%=type%>
         },function(ajax){
				if("1"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("��ִ̨�гɹ�");
				}else if ("-4"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("��ִ̨��ʧ��");
				}else{
					$("#resultDIV").html("");
					$("#resultDIV").append(ajax);
				}
				$("div[@id='QueryData']").append(ajax);
				$("div[@id='QueryData']").html("");
				$("button[@name='button']").attr("disabled", true);
          });
	}
} 

</script>
</head>
<%@ include file="../../toolbar.jsp"%>
<body>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td HEIGHT=20>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									��������ͨ���<%=typeName %>ҵ��
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">
									<span id="msg" style="color: red"></span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery_StackRefreshTools4JX.jsp"%>
					</td>
				</tr>
				<tr>
					<th colspan="4" align="center">
						��������ͨ���<%=typeName %>ҵ��
					</th>
				</tr>
				<tr>
					<td>
						<form name="frm" method="post" action="" onsubmit="return CheckForm()">
							<table width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<tr>
									<td bgcolor=#999999>
										<table border=0 cellspacing=1 cellpadding=2 width="100%" id="table_showConfig">
											<tr bgcolor="#FFFFFF" id="tr_strategybs"  >

												<td align="right" width="10%">
													���Է�ʽ��
												</td>
												<td width="20%">
													<select id="strategy_type" name ="strategy_type">
														<option value="1">�ն�����</option>
													</select>
												</td>
												<td align="right" width="30%">
												</td>
											</tr>
											<tr>
												<td colspan="4" align="right" CLASS="green_foot">
												<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute()" value=" ִ��" class=btn>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
													<td colspan="6" align="left" class="green_foot">
														<div id="resultDIV" />
													</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
								<td height="20">
								</td>
								</tr>
								<tr id="trData" style="display: none">
									<td class="colum">
										<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
											���ڲ��������Ե�....
										</div>
									</td>
								</tr>
								<tr>
									<td height="20">
									</td>
								</tr>
							</table>
						</form>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</td>
	</tr>
</table>
</body>
</html>
<%@ include file="../../foot.jsp"%>
