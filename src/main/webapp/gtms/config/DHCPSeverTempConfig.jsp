<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
 %>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>DHCP Server配置参数节点</title>
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
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									DHCP Server配置参数节点
								</td>
							</tr>
						</table>
					</td>
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
									配置   节点值
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
												<td nowrap align="right" class=column width="10%">
													DHCP 服务器配置
												</td>
												<td width="40%" >
													<input type="radio" name="serverConfig"  class=btn value="1" checked="checked">开启&nbsp;
													<input type="radio" name="serverConfig"  class=btn value="0">关闭  
												</td>
												<td nowrap align="right" class=column width="10%">
													是否开启DHCP服务器
												</td>
												<td width="40%" >
													<input type="radio" name="isOpenDHCP"  class=btn value="1" checked>开启&nbsp;
													<input type="radio" name="isOpenDHCP"  class=btn value="0">关闭  
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td  align="right" class=column width="10%">
													是否开启DHCPRelay
												</td>
												<td  width="40%" >
													<input type="radio" name="isRelay"  class=btn value="1" checked>开启&nbsp;
													<input type="radio" name="isRelay"  class=btn value="0">关闭  
												</td>
												<td  align="right" class=column width="10%">
													MinAddress
												</td>
												<td  width="40%" >
													<input type="text" name="minAddress" id="minAddress" class="bk" value="">&nbsp;
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td  align="right" class=column width="10%">
													MaxAddress
												</td>
												<td  width="40%" >
													<input type="text" name="maxAddress" id="maxAddress" class="bk" value="">&nbsp;
												</td>
												<td  align="right" class=column width="10%">
													ReservedAddresses
												</td>
												<td  width="40%" >
													<input type="text" name="reservedAddresses" id="reservedAddresses" class="bk" value="">&nbsp;
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td  align="right" class=column width="10%">
													SubnetMask
												</td>
												<td  width="40%" >
													<input type="text" name="subnetMask" id="subnetMask" class="bk" value="">&nbsp;
												</td>
												<td  align="right" class=column width="10%">
													DNSServers 
												</td>
												<td  width="40%" >
													<input type="text" name="DNSServers" id="DNSServers" class="bk" value="">&nbsp;
												</td>
											</TR>
											<TR bgcolor="#FFFFFF">
												<td  align="right" class=column width="10%">
													DHCPLeaseTime
												</td>
												<td  width="40%" colspan="3">
													<input type="text" id="DHCPLeaseTime" name="DHCPLeaseTime" value=""/>
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
		$("input[@name='gwShare_queryResultType']").val("radio");
	});
	function deviceResult(returnVal){
		$("#exeButton").attr("disabled",false);
		deviceId ="";
		$("table[@id='table_showConfig']").css("display","");
		$("#resultDIV").html("");
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备序列号为:"+returnVal[2][0][1]+"-"+returnVal[2][0][2]+"</strong></font>");
		deviceId = returnVal[2][0][0] ;		
	}
  
	//执行配置
	function doExecute(){
		   var url = "<s:url value='/gtms/config/serverConfig!doConfigAll.action'/>"; 
		   var gwType = <%=gwType%> ;
		   var serverConfig = $("input[@name='serverConfig']:checked").val();
		   var isOpenDHCP = $("input[@name='isOpenDHCP']:checked").val();
		   var isRelay = $("input[@name='isRelay']:checked").val();
		   
		   var minAddress = $("#minAddress").val();
		   var maxAddress = $("#maxAddress").val();
		   var reservedAddresses  = $("#reservedAddresses").val();
		   var subnetMask  = $("#subnetMask").val();
		   var DNSServers  = $("#DNSServers").val();
		   var DHCPLeaseTime  = $("#DHCPLeaseTime").val();
		   $.post(
	          url,{
	          deviceId : deviceId,
	          serverConfig : serverConfig,
	          isOpenDHCP  : isOpenDHCP,
	          isRelay : isRelay,
	          minAddress  : minAddress,
	          maxAddress  : maxAddress,
	          reservedAddresses : reservedAddresses ,
	          subnetMask : subnetMask,
	          DNSServers : DNSServers,
	          DHCPLeaseTime : DHCPLeaseTime,
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