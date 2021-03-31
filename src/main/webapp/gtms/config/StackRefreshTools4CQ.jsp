<%--
Author      : 王森博
Date		: 2010-4-15
Desc		: 手动的批量软件升级
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%
 	String gwType = request.getParameter("gw_type");

	String type = "2";

	if("1".equals(type)){
	}else if("2".equals(type)){
	}else{
		response.sendRedirect("../../login.jsp");
	}

 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
});
var deviceIds = "";
var returnValthis ="init";
var max = '<s:property value="maxActive" escapeHtml="false" />';
//function CheckForm(){
//	if($("input[@name='deviceIds']").val()==""){
//		alert("请选择设备！");
//		return false;
//	}
//	var mode = $("#mode").val();
//	if(mode=="2"){
//		if((returnValthis[0]==0&&returnValthis[2].length>max)||(returnValthis[0]!=0&&returnValthis[0]>max)){
//			alert("设备数量大于"+max+"只能选择被动模式！");
//			$("#mode").val("1");
//			$("#softUp").show();
//			return false;
//		}
//	}
//	return true;
//}


function CheckForm(){

	var taskWanTypeStr = "";
	$.each($("input[@name='taskWanType']"),function(){
		taskWanTypeStr += $(this).val() + ",";
	});

	$("input[@name='task_wan_type']").val(taskWanTypeStr);

	var rtnflag = "";

	if($("input[@name='deviceIds']").val()==""){
		alert("请选择设备！");
		return false;
	}

	if($.trim($("input[@name='taskName']").val()).length==0){
		alert("请输入任务名称！");
		return false;
	}

 	$("#fatherDiv div").each(function(i){
		//遍历配置
		var tempPath = $("#paramNodePath"+i).val();
		var tempValue = $("#paramValue"+i).val();
		var tempType = $("#paramType"+i).val();
		if(tempPath==""){
			alert("第"+(i+1)+"个参数节点路径为空！");
			rtnflag = "false";
		}
		if(tempValue==""){
			alert("第"+(i+1)+"个参数值为空！")
			rtnflag = "false";
		}
		if(tempType=="-1"){
			alert("第"+(i+1)+"个参数类型为空！")
			rtnflag = "false";
		}
    });
 	if(rtnflag=="false"){
 		return false;
 	}
 	 var paramNodePath = "";
	 var paramValue = "";
	 var paramType = "";
 	$("#fatherDiv div").each(function(i){
		//遍历配置
		paramNodePath += "," + $("#paramNodePath"+i).val();
		paramValue    += "," + $("#paramValue"+i).val();
		paramType     += "," + $("#paramType"+i).val();
    });

 	$("#paramPath").val(paramNodePath);
 	$("#paramValue").val(paramValue);
 	$("#paramType").val(paramType);

 	$("input[id='submitId']").attr("disabled", true);

	return true;
}

function deviceResult(returnVal){
	returnValthis = returnVal;
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{

		if(returnVal[0]>200000){
			alert("定制数量不可以超过20万！");
			return;
		}

		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	$("#gwShare_queryType_this").val(gwShare_queryType);
}


var iscqsoft = true;

//点击增加的时候增加一组
var flag = 0;
function doAdd(){
	flag++ ;
	//复制paramAddDiv0里的元素
	$("#fatherDiv").append($("#paramAddDiv0").clone(true).attr("id","paramAddDiv"+flag));
	//$("#paramAddDiv"+flag).each(function(){
		//将克隆过来的输入框全部置空
		$("#paramAddDiv"+flag).find("input[type='text']").val("");
		$("#paramAddDiv"+flag).find("select[name='paramType0']").val("");
		//更改克隆过来div的各个输入框的id
		$("#paramAddDiv"+flag).find("input[name='paramNodePath0']").attr("id","paramNodePath"+flag).attr("name","paramNodePath"+flag);
		$("#paramAddDiv"+flag).find("input[name='paramValue0']").attr("id","paramValue"+flag).attr("name","paramValue"+flag);
		$("#paramAddDiv"+flag).find("select[name='paramType0']").attr("id","paramType"+flag).attr("name","paramType"+flag);
	//});
}

//动态的删除删除掉一组,并且只删除最后一组
function doDelete(){
   if($("#fatherDiv div").size()>1){
   		$("#fatherDiv div:last-child").remove();
   }else{
   		alert("这是最后一行，无法再删除");
   		return;
   }
    flag--;
}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

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
									Ipv6策略定制
								</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12">

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
				<TR>
					<TH colspan="4" align="center">
						Ipv6策略定制
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION="<s:url value="/gtms/config/stackRefreshTools!addTaskInfo4CQ.action"/>"
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="<%=gwType%>" />
							<input type="hidden" name="maxActive" id="maxActive"  />
							<input type="hidden" name="gwShare_queryType_this" id="gwShare_queryType_this"  />
							<input type="hidden" name="paramPath" id="paramPath"  />
							<input type="hidden" name="paramValue" id="paramValue"  />
							<input type="hidden" name="paramType" id="paramType"  />
							<input type="hidden" name="task_wan_type" id="task_wan_type"  />
							<input type="hidden" name="type" id="type"  value="<%=type %>"/>

							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev" >
														请查询设备！
													</div>
												</td>
											</tr>
											<!-- add begin -->
											<tr bgcolor="#FFFFFF">
											 <td align="right" width="20%">任务名称:</td>
											 <TD width="80%" colspan="3">
											  <input type="text" name='taskName' id="taskName" style="width:25%"/>
											 </TD>
											</tr>
											<tr bgcolor="#FFFFFF">
											 <td align="right" width="20%">选择定制的WAN:</td>
											 <TD width="80%" colspan="3" align="left">
											  <input type="checkbox" value="1" name='taskWanType' id="taskWanType" checked="checked" disabled="disabled"/>
											   宽带WAN
											 </TD>
											</tr>

											<tr>
								<td colspan="4" align="right" class=column>
									<button type="button" id="doAddButton" onclick="doAdd();" class=btn>
										增加节点
									</button>
								</td>
							</tr>
							<TR>
									<TD class=column colspan="4">
										 <div id="fatherDiv">
											<div id="paramAddDiv0">
												<table border=0 cellspacing=0 cellpadding=0 width="100%">
												<TR bgcolor="#FFFFFF">
													<td nowrap align="right" class=column width="20%">
														<span ><a href="javascript:doDelete()">删除    |</a></span>参数节点路径
													</td>
													<td width="70%" colspan="3" class=column>
														<input type="text" id="paramNodePath0" name="paramNodePath0" style="width:800px;"
															value="" >
													</td>
												</TR>
												<TR bgcolor="#FFFFFF">
													<td align="right" class=column width="20%">参数值</td>
													<td width="30%" class=column>
														<input type="text" id="paramValue0" name="paramValue0" value=""/>
													</td>
													<td align="right" class=column width="30%">参数类型</td>
													<td width="70%" class=column>
														<select name="paramType0" id="paramType0" cssClass="bk">
														 	<option value="1">string</option>
														 	<option value="2">int</option>
														 	<option value="3">unsignedInt</option>
														 	<option value="4">boolean</option>
														 </select>
													</td>
												</TR>
												</table>
											</div>
										</div>
									</TD>
								</TR>

											<!-- add end -->
											<tr bgcolor="#FFFFFF" >
												<TD align="right" width="20%">
													升级策略方式(默认重启触发)：
												</TD>
												<TD width="80%" colspan="3">
													<select name="strategy_type" id="strategy_type" class=bk >
														<option value="3">终端启动</option>
														<option value="4">Inform周期上报信息</option>
													</select>
												</TD>
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="submit" value=" 执 行 " class=btn id="submitId">
												</TD>
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
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../../foot.jsp"%>
