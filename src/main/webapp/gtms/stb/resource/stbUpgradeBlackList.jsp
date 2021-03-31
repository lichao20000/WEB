<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>" type="text/css">

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});


var deviceIds = "";
function deviceResult(returnVal){
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		if(returnVal[0]>400000){
			alert("定制数量不可以超过40万！");
			return;
		}
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
}

function doQuery(){
	var deviceIds = $("input[@name='deviceIds']").val();
	var param = $("input[@name='param']").val();
	var gw_type = $("input[@name='gw_type']").val();
	var task_name = $("input[@name='task_name']").val();
	
	if(deviceIds == ""){
		alert("请选择设备");
		return false;
	}
	
	if(task_name==""){
		alert("请输入任务名称！");
		return false;
	}
		
	$("button[@id='btn']").attr("disabled",true);
	$("tr[@id='trData']").show();
	$("div[@id='QueryData']").html("正在执行批量定制操作，请稍等....");
	url = "<s:url value='/gtms/stb/resource/stbUpgradeBlackList!addBlackList.action'/>";
	$.post(url,{
		task_name : task_name,
		gw_type : gw_type,
		deviceIds : deviceIds,
		param : param
	},function(ajax){
		$("div[@id='QueryData']").html(ajax);
		$("button[@id='btn']").attr("disabled",true);
	});
}
</SCRIPT>
<%@ include file="../../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									配置机顶盒黑名单
								</td>
								<td nowrap>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gtms/stb/share/gwShareDeviceQuery_StbBlackList.jsp"%>
					</td>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="" target="childFrm" >
							<input type="hidden" name="hblt_SpeedTest_flg" value="true" />
							<input type="hidden" name="hblt_BatchSpeedTest_flag" value="true" />
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="4" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														请查询设备！
													</div>
												</td>
											</tr>
											
											<tr bgcolor="#FFFFFF">
												<TD colspan="1" class="column" >
													任务名称：
												</TD>
												<TD colspan="3" class="column" >
													<input type="text" name="task_name" class="bk" value=""/>
												</TD>
											</tr>
						
											<tr bgcolor=#ffffff>
												<td class=foot colspan=4 align=right>
													<button id="btn" onclick="doQuery();">&nbsp;定&nbsp;制&nbsp;</button>
												</td>
											</tr>
																	
											<tr id="trData" style="display: none" bgcolor=#ffffff>
												<td colspan=4>
													<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
														正在努力为您查询，请稍等....</div>
												</td>
											</tr>						
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm name=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
