<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="jsRigth.css" type="text/css" rel="stylesheet">
<SCRIPT LANGUAGE="JavaScript" SRC="jsRightMenu.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/jquery.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--//

function query(){
	
	var device_serialnumber = document.frm.devSn.value.trim();
    if(device_serialnumber.length < 6){
	    alert("请输入至少最后6位设备序列号 !");
	    document.frm.devSn.focus();
	    return ;
    }
    var deviceSelect_url = "advancedDevInfo.action";
	
	//document.getElementById("devListForm").src = deviceSelect_url + "?devSn="+device_serialnumber;
	
	//查询
	$.post(deviceSelect_url,{
		devSn:device_serialnumber
	},function(ajaxPage){
		$("div[@id='devListBody']").html("");
		$("div[@id='devListBody']").append(ajaxPage);
		
		var device_id = document.getElementById("device_id").value;
		
		var url = '<s:url value='/gwms/blocTest/QueryCustomerInfo!query.action'/>'; 
		$.post(url,{
			device_id:device_id
		},function(ajax){	
		    $("div[@id='UserData']").html("");
			$("div[@id='UserData']").append(ajax);
			$("table[@id='trUserData']").show();
		});
	});
}

function show(deviceId){
	//alert(event.srcElement.tagName);
	//alert(deviceId);
	ShowRightMenu(deviceId);
}

function DetailDevice(device_id){
	var strpage = "../gwms/blocTest/DeviceShow.jsp?device_id=" + device_id;
	window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function GoContent(user_id)
{
	 	var strpage="../gwms/blocTest/EGWUserRelatedInfo.jsp?user_id=" + user_id;
		window.open(strpage,"","left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function openCust(customer_id)
{
	var strpage="../bbms/CustomerInfo!detailInfo.action?customer_id=" + customer_id;
	window.open(strpage,"","left=20,top=20,height=450,width=700,resizable=yes scrollbars=yes,status=yes,toolbar=no,menubar=no,location=no");
}

//-->
</SCRIPT>

<body>
<FORM name="frm" action="" method="post">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="95%" oncontextmenu="return false;">
		<TR>
			<TD HEIGHT="20">
				&nbsp;
			</TD> 
		</TR>
		<TR>
			<TD>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											性能流量配置查询
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										“右键”或“双击”配置及查询设备的性能、流量和设备状态等信息
									</td>
								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="3">设备查询</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="25%">
										&nbsp;设备序列号(至少最后6位)：
									</TD>
									<TD align="left" width="75%">
										<input type="text" name="devSn" class="bk" maxlength="100" size="35">&nbsp;&nbsp;
										<font color='red'>*</font>
											&nbsp;&nbsp;
											<button type="button" class="btn" value=" 查 询 " onclick="query();"> &nbsp;查 询&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
									</TD>
								</TR>
								<TR>
									<TD colspan="4" height="20" bgcolor="#ffffff">
											
									</TD>
								</TR>
							</TABLE>
					 		<div id="devListBody">
							</div>
							<div id="custInfo">
							</div>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</FORM>
</body>
