<%--
业务手工工单
Author: Jason
Version: 1.0.0
Date: 2009-09-25
--%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/prototype.js"></SCRIPT>
		<link rel="stylesheet" href="../../css/liulu.css" type="text/css">

		<title>局向和VOIP服务器对应关系</title>
		<script type="text/javascript">
<!--//
function editExpert(_id, _faultDesc, _suggest){
	$("input[@name='expertId']").val(_id);
	$("input[@name='faultDesc']").val(_faultDesc);
	$("input[@name='suggest']").val(_suggest);
}

function saveCheck(){
	var _id = $("input[@name='expertId']").val();
	var _faultDesc = $("input[@name='faultDesc']").val();
	var _suggest = $("input[@name='suggest']").val();

	if(false == IsNull(_id,"专家建议ID")){
		return false;
	}
	var diagUrl = '<s:url value="/gwms/diagnostics/expertManage!updateExpert.action"/>';
	//查询
	$.post(diagUrl,{
		expertId:_id,
		faultDesc:_faultDesc,
		suggest:_suggest
	},function(ajaxMesg){
		alert(ajaxMesg);
	});
}
//-->

var mouse_y = 200;
function CreateAjaxReq(url,param,successFunc,errorFunc){
	var myAjax
		= new Ajax.Request(url,{method:"post",
								parameters:param,
								onFailure:errorFunc,
								onSuccess:successFunc} );
}

function showDetailResult(response){
	var _divDetail = $("divDetail");
	_divDetail.show();
	_divDetail.style.top = mouse_y + 6;
	_divDetail.innerHTML = response.responseText;
}

function showError(response){
	_debug.innerHTML = response.responseText;
}


function showDetail(cityName,officeName,proxServ,proxPort,proxServ2,proxPort2,regiServ,regiPort,standRegiServ,standRegiPort,outBoundProxy,outBoundPort,standOutBoundProxy,standOutBoundPort){
	mouse_y = event.clientY;//鼠标所在位置的y坐标，即距顶端距离。

	var param = "cityName=" + cityName;
	param += "&officeName=" + officeName;
	param += "&proxServ=" + proxServ;
    param += "&proxPort=" +proxPort;
    param += "&proxServ2=" + proxServ2;
    param += "&proxPort2=" + proxPort2;
    param += "&regiServ=" + regiServ;
    param += "&regiPort=" + regiPort;
    param += "&standRegiServ=" + standRegiServ;
    param += "&standRegiPort=" + standRegiPort;
    param += "&outBoundProxy=" + outBoundProxy;
    param += "&outBoundPort=" + outBoundPort;
    param += "&standOutBoundProxy=" + standOutBoundProxy;
	param += "&standOutBoundPort=" + standOutBoundPort;

        CreateAjaxReq("officeVoipData.jsp",param,showDetailResult,showError);

}


function CloseDetail(){
	$("divDetail").hide();
}


</script>
	</head>
	<body>
		<TABLE CELLPADDING="0" CELLSPACING="0" WIDTH="100%" BORDER="0">
			<TR>
				<TD height="20">
				</TD>
			</TR>
			<TR>
				<TD valign=top>
					<FORM NAME="frm" ACTION="">
						<TABLE width="95%" border=0 cellspacing=0 cellpadding=0
							align="center">
							<tr>
								<td>
									<table width="100%" height="30" border="0" cellspacing="0"
										cellpadding="0" class="green_gargtd">
										<tr>
											<td width="162" align="center" class="title_bigwhite">
												VOIP服务器地址
											</td>
											<td>
												<img src="../../images/attention_2.gif" width="15"
													height="12">
												维护局向和VOIP服务器地址的关系
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<TR>
								<TD bgcolor=#999999>
									<table border="0" cellspacing="1" cellpadding="2" id="myTable"
										width="100%">
										<tr>
											<th width="10%">
												属地
											</th>
											<th width="25%">
												局向
											</th>
											<th width="25%">
												SIP服务器地址
											</th>
											<th width="30%">
												SIP服务器端口
											</th>
											<th width="10%">
												操作
											</th>
										</tr>
										<s:iterator value="officeVoipList">
											<tr>
												<td class=column align="center">
													<s:property value="cityName" escapeHtml="false" />
												</td>
												<td class="column">
													<s:property value="officeName" />
												</td>
												<td class="column">
													<s:property value="proxServ" />
												</td>
												<td class="column">
													<s:property value="proxPort" escapeHtml="false" />
												</td>

												<td class="column" align="center">
													<a href=javascript:
														//
														onclick="showDetail('<s:property value="cityName"/>','<s:property value="officeName"/>',
                                                                                                    '<s:property value="proxServ"/>','<s:property value="proxPort"/>',
                                                                                                    '<s:property value="proxServ2"/>','<s:property value="proxPort2"/>',
                                                                                                    '<s:property value="regiServ"/>','<s:property value="regiPort"/>',
                                                                                                    '<s:property value="standRegiServ"/>','<s:property value="standRegiPort"/>',
                                                                                                    '<s:property value="outBoundProxy"/>','<s:property value="outBoundPort"/>',
                                                                                                    '<s:property value="standOutBoundProxy"/>','<s:property value="standOutBoundPort"/>'
                                                                                                    )">详细信息</a>
												</td>

											</tr>
										</s:iterator>
									</table>
									<div id="divDetail"
										style="position: absolute; z-index: 255; top: 200px; left: 350px; width: 40%; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none"></div>
									<div id=_debug></div>
								</TD>
							</TR>
							<tr>
								<td align="right">
									<lk:pages url="/gwms/resource/officeVoip!goPage.action"
										styleClass="" showType="" isGoTo="true" changeNum="true" />
								</td>
							</tr>
							<!--
		<TR>
			<TD bgcolor=#999999>
				<table border="0" cellspacing="1" cellpadding="2" id="myTable" width="100%">
					<tr>
						<th colspan="2">服务器地址编辑</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td width="25%" align="right">局向</td>
						<td width="75%" class=column>
							<input type="text" name="officeName" readonly size="30">
							<input type="hidden" name="officeId" >
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td align="right">SIP服务器地址</td>
						<td class=column>
							<input type="text" name="proxServ" size="50">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td align="right">SIP服务器端口</td>
						<td class=column>
							<input type="text" name="proxPort" size="50">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan="2" align="right">
							<input type="button" value="保 存" name="foot" onclick="saveCheck()">&nbsp;&nbsp;
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		 -->

						</TABLE>
					</FORM>
	</body>
</html>
<%@ include file="../foot.jsp"%>
