<%--
FileName	: proProcess.jsp
Date		: 2013年5月21日
Desc		: 新疆下发上网数设计
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%
request.setCharacterEncoding("gbk");
String gwType = request.getParameter("gw_type");
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	//gwShare_setOnlyGaoji();
	gwShare_setGaoji();
});
var device_id = "";
var param = "";
var gw_type = "<%= gwType%>";
var deviceIds="";

function checkForm(){
	if($("input[@name='ttNumber']").val()==""){
		alert("请输入上网数！");
		return false;
	}
	return true;
}

function deviceResult(returnVal){	
	deviceIds="";
	var totalNum = returnVal[0];
	if(returnVal[0]==0){
		totalNum = returnVal[2].length;
		if(100000<totalNum){
			alert("单次配置设备数应小于100000台！请进行属地版本等条件过滤！");
       		$("#exeButton").attr("disabled",true);
            return;
		}
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		deviceIds = "0";
		param=returnVal[1];
	}
	if(100000<returnVal[0]){
		alert("单次配置设备数应小于100000台！请进行属地版本等条件过滤！");
   		$("#exeButton").attr("disabled",true);
        return;
	}
}

//执行配置
function doExecute(){
    var url = "<s:url value='/gtms/config/batchConfigMaxTerminal!maxTerminalConfig.action'/>"; 
    var mode = $("select[@name='mode']").val();
	var total_number = $("input[@name='ttNumber']").val();
	if(checkForm()){
        $.post(
                url,{
                deviceIds : deviceIds,
                mode : mode,
                total_number : total_number,
                param:param
         } ,function(ajax){
                $("#resultDIV").html("");
                $("#doButton").attr("disabled",false);
                if("1"==ajax){
                      $("#resultDIV").append("后台执行成功");
                 }else{
                     $("#resultDIV").append("后台执行失败");
                 }
        });
    }
}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>


<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<table width="100%" border="0" cellpadding="0" cellspacing="0"
				class="text">
				<TR>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									批量最大终端数配置
								</td>
								<td nowrap>
									<img src="../../images/attention_2.gif" width="15" height="12">
									&nbsp;批量最大终端数配置
								</td>
							</tr>
						</table>
					</td>
				</TR>

				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						配置终端数策略
					</TH>
				</TR>
				<tr>
					<td>
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="6">
													<div id="selectedDev" >
														请查询设备！
													</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">

												<td align="right" width="15%">
													策略方式：
												</td>
												<td width="30%">
													<select id="strategy_type" name ="strategy_type">
														<option value="1">立即执行</option>
													</select>
												</td>
												<td align="right" width="15%">
													模式：
												</td>
												<td width="30%">
													<select id="mode" name ="mode">
														<option value="0">关闭</option>
														<option value="1">开启</option>
													</select>
												</td>

												<td align="right" width="15%">
													上网数：
												</td>
												<td align="center">
													<input type="text" name="ttNumber" value="" class="bk">&nbsp;&nbsp; 
												</td>
											</tr>
											<TR bgcolor="#FFFFFF">
													<TD colspan="6" align="right" class="green_foot">
														<button type="button" id="exeButton" name="exeButton"
															onclick="doExecute();" class=btn>
																	&nbsp;执&nbsp;行 &nbsp;
														</button>
													</TD>
												</TR>
											<TR bgcolor="#FFFFFF">
													<TD colspan="6" align="left" class="green_foot"><div id="resultDIV" /></TD>
												</TR>
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
</TABLE>
