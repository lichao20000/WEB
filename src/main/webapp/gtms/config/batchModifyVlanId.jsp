<%--
Author      : yaoli
Date		: 2019-04-01
Desc		: ITMS+家庭网关批量修改业务通道vlanid需求
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>

<html>
<head>
<script language="JavaScript" src="<s:url value='/Js/jquery.js'/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript">
$(function(){
	$("#doButton").attr("disabled",false);
 	gwShare_queryChange('3');
	$("input[@name='gwShare_queryResultType']").val("checkbox");
});
function CheckForm(){
	if($("input[@name='gwShare_fileName']").val()==""){
		alert("请上传文件！");
		return false;
	}
	return true;
}

function doExecute(){

	var iptvBus = 0;
	if($("input[name='iptvBus']").is(":checked")){
		iptvBus = 1;
	}
	
	var wanBus = 0;
	if($("input[name='wanBus']").is(":checked")){
		wanBus = 1;
	}
	
	var voipBus = 0;
	if($("input[name='voipBus']").is(":checked")){
		voipBus = 1;
	}
	
	if(iptvBus == 0 && wanBus == 0 && voipBus == 0){
		alert("请选择至少一个操作业务类型");
		return false;
	}
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	
	if(CheckForm()){
	var url = "<s:url value="/gtms/config/BatchModifyVlanId!doConfig.action"/>";
	$("tr[@id='trData']").show();
	$("#doButton").attr("disabled",true);
	$("div[@id='QueryData']").html("正在操作，请稍等....");
		$.post(url,{
			wanBus : wanBus,
			voipBus : voipBus,
			iptvBus : iptvBus,
            gwShare_fileName : gwShare_fileName
         },function(ajax){
				if("1"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("后台执行成功");
				}else if ("-4"==ajax){
					$("#resultDIV").html("");
					$("#resultDIV").append("后台执行失败");
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
									批量修改vlanid业务通道
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
						<%@ include file="/gwms/share/gwShareDeviceQuery_batchModifyVlanId.jsp"%>
					</td>
				</tr>
				<tr>
					<th colspan="4" align="center">
						批量修改vlanid业务通道
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
													修改业务：
												</td>
												<td width="20%">
												<input type="checkbox" name="iptvBus" value="1">iptv业务
												<input type="checkbox" name="wanBus" value="1">宽带业务
												<input type="checkbox" name="voipBus" value="1">语音业务
												</td>
												<td align="right" width="30%">
												</td>
											</tr>
											<tr>
												<td colspan="4" align="right" CLASS="green_foot">
												<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute()" value=" 执行" class=btn>
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
											正在操作，请稍等....
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
