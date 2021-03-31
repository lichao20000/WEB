<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	gwShare_setImport();
});
var deviceId = "";
var devicetypeId = "";
function ExecMod(){
	deviceId = $("input[@name='deviceIds']").val();
    if(deviceId==""){
		alert("请查询设备！");
		return false;
    }
// 	if($("select[@name='goal_softwareversion']").val()==undefined){
// 		alert("请重新选择设备！");
// 		return false;
// 	}
// 	if($("select[@name='goal_softwareversion']").val() == -1){
// 		alert("请选择版本文件！");
// 		return false;
// 	}
	$('#softUpgrade_btn').attr("disabled",true);
	var deviceIds = $("input[@name='deviceIds']").val();
	var param = $("input[@name='param']").val();
	var task_desc = $("input[@name='task_desc']").val();
	if ("" == task_desc){
		alert("任务描述不可为空！");
		return;
	}

	var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!batchSoftUpgradeNew.action'/>";
	goal_softwareversion = $("select[@name='goal_softwareversion']").val();

	$.post(url,{
		deviceIds:deviceIds,
		pathId:$("select[@name='goal_softwareversion']").val(),
		devicetypeId:devicetypeId,
		goal_softwareversion:goal_softwareversion,
		param:param,
		taskDesc : task_desc,
		gw_type:"4",
		softStrategy_type:$("select[@name='softStrategy_type']").val()
	},function(ajax){
		var s = ajax.split(";");
		if(s[0]=="1"){
			alert("调用后台成功！");
			
	    }else
		{
			alert("调用后台失败！");
		}
		var url = "<s:url value='/gtms/stb/resource/patchSoftUpgradeNew.jsp'/>";
        window.location.href=url;
		$('#softUpgrade_btn').attr("disabled",false);
	});
}


function deviceResult(returnVal){
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
	
// 	$("tr[@id='softwareversion']").show();
// 	var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!showALLSoftwareversion.action'/>";
// 	$("div[@id='div_goal_softwareversion']").html("");
// 	$.post(url,{
// 		deviceIds:deviceIds
// 	},function(ajax){
// 		if(ajax!=""){
// 			var lineData = ajax.split("#");
// 			if(typeof(lineData)&&typeof(lineData.length)){
// 				$("div[@id='div_goal_softwareversion']").append("<select name='goal_softwareversion' class='bk' style='width: 400px'>");
// 				$("select[@name='goal_softwareversion']").append("<option value='-1' selected>==请选择==</option>");
// 				for(var i=0;i<lineData.length;i++){
// 					var oneElement = lineData[i].split("$");
// 					var xValue = oneElement[0];
// 					var xText = oneElement[1];
// 					option = "<option value='"+xValue+"'>=="+xText+"==</option>";
// 					$("select[@name='goal_softwareversion']").append(option);
// 				}
// 			}else{
// 				$("div[@id='div_goal_softwareversion']").append("该设备没有可升级的版本文件！");
// 			}
// 		}else{
// 			$("div[@id='div_goal_softwareversion']").append("该设备没有可升级的版本文件！");
// 		}
// 	});
}

</SCRIPT>



<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			您当前的位置：设备批量软件升级
		</TD>
	</TR>
</TABLE>
<br>
<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR>
		<td>
			<%@ include file="../share/gwShareDeviceQueryCheckNew.jsp"%>
		</td>
	</TR>


	<TR id="softwareversion" >
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="">
				<input type="hidden" name="deviceIds" value="" />
				<input type="hidden" name="param" value="" />
				<table width="100%" class="querytable">
					<TR>
						<TH colspan="5" class="title_1">
							版本升级
						</TH>
					</TR>
					<TR id="trDeviceResult" style="display: none">
						<td nowrap class="title_2" width="15%">
							设备序列号
						</td>
						<td id="tdDeviceSn" width="35%">
						</td>
						<td nowrap class="title_2" width="15%">
							属地
						</td>
						<td id="tdDeviceCityName" width="35%">
						</td>
					</TR>
					<TR>
					<TD nowrap class="title_2" width="15%">
							<font color="red">*</font>&nbsp;目标版本
						</TD>
						<TD width="">
							根据设备自动匹配
					</TD>
					 <%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) 
                                		   ){ %>
					<TD align="right" width="15%" id="upgradeType" style="display:none">软件升级策略方式：</TD>
                     <TD width="30%" id="selectType" style="display:none">
                     <SELECT name="softStrategy_type" >
                       <option value="5">
                               	终端启动
                       </option>
                       <option value="4">
                              	 下次连接到终端
                       </option>
                     </SELECT>
                     </TD>
					  <% }else if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
                     <TD align="right" width="15%" id="upgradeType" >软件升级策略方式：</TD>
                     <TD width="30%" id="selectType" >
                     <SELECT name="softStrategy_type" >
                       <option value="5">
                               	终端启动
                       </option>
                     </SELECT>
                     </TD>
                     <% }else{%>
                     <TD align="right" width="15%" id="upgradeType" >软件升级策略方式：</TD>
                     <TD width="30%" id="selectType" >
                     <SELECT name="softStrategy_type" >
                       <option value="5">
                               	终端启动
                       </option>
                       <option value="4">
                              	 下次连接到终端
                       </option>
                     </SELECT>
                     </TD>
                    <% }%>
					</TR>
					
                                   <%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
                                		|| "jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")) 
                                		   ){ %>
                                   <TR>
                                        <TD align="right" width="15%">任务描述：</TD>
					                     <TD width="30%">
					                     <input type="text" name="task_desc" class="bk" value=""/>
					                     </TD>
					                     <TD colspan='2'/>
                                   	</TR>
                                        <%}%>
					<TR bgcolor="#FFFFFF">
						<TD colspan="5" CLASS="foot" id="softUpgrade_foot">
							<div class="right">
								<button id="softUpgrade_btn" onclick="ExecMod()">
									升 级
								</button>
							</div>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>

