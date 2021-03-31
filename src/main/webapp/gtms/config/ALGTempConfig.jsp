<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>ALG配置参数节点</title>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<link href="../../css/listview.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
</head>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="frm" METHOD="post" ACTION="" onsubmit="return CheckForm()">
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<th>
						ALG配置参数节点
					</th>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<tr bgcolor="#FFFFFF">
				    <td>
						<input type="hidden" name="param" value="" />
						<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0 cellpadding=0 
							align="center" class="querytable">
							<tr>
								<th  align="center" width="100%">
									ALG配置参数节点
								</th>
							</tr>
							<tr bgcolor="#FFFFFF">
								<td colspan="4">
									<div id="selectedDev">
										请查询设备！
									</div>
								</td>
							</tr>
							<TR>
								<TD bgcolor=#999999>
									<div id="">
										<table border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF">
												<td  align="right" class=column width="10%">
													在SIP下是否启用ALG功能
												</td>
												<td  width="70%">
													<input type="radio" name="sipEnable"  class=btn value="1" >开启&nbsp;
													<input type="radio" name="sipEnable"  class=btn value="0" checked>关闭
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td  align="right" class=column width="20%">
													在FTP下是否启用ALG功能
												</td>
												<td  width="30%" colspan="3">
													<input type="radio" name="ftpEnable"  class=btn value="1" >开启&nbsp;
													<input type="radio" name="ftpEnable"  class=btn value="0" checked>关闭
												</td>
											</TR>
											</table>
											</div>
										<table  border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<button type="button" id="exeButton" name="exeButton"
														onclick="doExecute();" class=btn>
																&nbsp;配&nbsp;置 &nbsp;
													</button>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="left" class="green_foot">
													<div id="resultDIV" />
												</TD>
											</TR>
										</table>
								</TD>
							</TR>
						</TABLE>
					</td>
				</tr>
			</table>
			</FORM>
		</TD>
	</TR>
</TABLE>
</body>
<script type="text/javascript">
	var deviceId = "";
	var param = "";
	$(function(){
		//gwShare_setGaoji();
		//gwShare_setImport();
		$("input[@name='gwShare_queryResultType']").val("radio");
	});
	function deviceResult(returnVal){

		$("#exeButton").attr("disabled",false);
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
	    $("div[@id='selectedDev']").html("<font size=2><strong>待操作设备序列号:"+returnVal[2][0][1]+returnVal[2][0][2]+"</strong></font>");
		deviceId = returnVal[2][0][0] ;			
	}
	//执行配置
	function doExecute(){
	   var url = "<s:url value='/gtms/config/serverConfig!algConifig.action'/>"; 
	   var gwType = <%=gwType%>;
	   var sipEnable = $("input[@name='sipEnable']:checked").val();
	   var ftpEnable = $("input[@name='ftpEnable']:checked").val();
	  $.post(
        	url,{
		        deviceId : deviceId,
		        sipEnable : sipEnable,
		        ftpEnable : ftpEnable,
		        gw_type : gwType
 	 		} ,function(ajax){
         $("#resultDIV").html("");
         $("#doButton").attr("disabled",false);
         if("1"==ajax){
               $("#resultDIV").append(ajax);
          }else{
              $("#resultDIV").append(ajax);
          }
 		});
	}
	</script>
</html>
<%@ include file="../../foot.jsp"%>