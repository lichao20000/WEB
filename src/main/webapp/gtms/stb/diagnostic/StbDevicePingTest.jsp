<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.linkage.module.gtms.stb.diagnostic.serv.PingInfoBIO" %>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	var gw_type = "<%=request.getParameter("gw_type")%>";
	
	$(function(){
		gwShare_setGaoji();
		changeSelect("city", "-1");
		
		var isGoPage = '<s:property value="isGoPage"/>';
	 	if(isGoPage != 1){
	 		$("#chooseDevHtml").html("");
		}else{
			// 页面跳转
			$("#goPageForStbDevicePingTest").show().siblings("span").html("");
		}
	});
	
	// 山西联通不显示中间设备选择页面
	function deviceInfo(url){
		$.post(url,{},function(ajax){
			$("#chooseDevHtml").html("");
	   		$("#chooseDev").html("");
	   		$("#chooseDev").append(ajax);
	   		// 页面跳转
	   		$("#goPageForStbDevicePingTest").show().siblings("span").html("");
	   	});
	}
	
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
		}
	}
	
	/*------------------------------------------------------------------------------
	//函数名:		queryChange
	//参数  :	change 1:简单查询、2:高级查询
	//功能  :	根据传入的参数调整显示的界面
	//返回值:		调整界面
	//说明  :	
	//描述  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var url="<s:url value="/gtms/stb/resource/gwDeviceQuery!queryDeviceList.action"/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if(typeof(returnVal)=='undefined'){
			return;
		}else{
			$("input[@name='deviceId']").val(returnVal[0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	
  	function ExecMod(){ 
  		if(!CheckForm()){
  			return;
  		}
  		
		$('#ping_btn').attr("disabled",true);
		$("div[@id='div_ping']").html("正在诊断，请稍后...");
		var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!pingDiag.action'/>";
		var deviceId = $("input[@name='deviceId']").val();
		var dataBlockSize = $("input[@name='dataBlockSize']").val();
		var numberOfRepetitions = $("input[@name='numberOfRepetitions']").val();
		var hostIp = $("input[@name='hostIp']").val();
		var timeout  = $("input[@name='timeout']").val();
		$.post(url,{
			deviceId:deviceId,
			dataBlockSize:dataBlockSize,
			numberOfRepetitions:numberOfRepetitions,
			hostIp:hostIp,
			timeout:timeout,
			gw_type:gw_type
		},function(ajax){
		    $("div[@id='div_ping']").html("");
			$("div[@id='div_ping']").append(ajax);
			$('#ping_btn').attr("disabled",false);
		});	
    }

	function CheckForm(){
		var obj = document.frm;
		if(obj.deviceId.value==""){
			alert("请查询设备！");
			return false;
	    }
        if(!IsNull(obj.dataBlockSize.value,'包大小')){
			obj.dataBlockSize.focus();
			return false;
		}
        if(!IsNull(obj.numberOfRepetitions.value,'包数目')){
			obj.numberOfRepetitions.focus();
			return false;
		}
        if(!IsNull(obj.hostIp.value,'测试IP')){
			obj.hostIp.focus();
			return false;
		}
		if(!IsIPAddr(obj.hostIp.value)){
			obj.hostIp.focus();
			return false;
		}
        if(!IsNull(obj.timeout.value,'超时时间')){
			obj.timeout.focus();
			return false;
		}
		return true;					
	}
	
	function changeIP(obj)
	{
		if("-1"==obj.value){
			$("input[name=hostIp]").val("");
		}else{
			$("input[name=hostIp]").val(obj.value);
		}
	}
	
	function changeSelect(type, selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!getCityNextChild.action'/>";
				$.post(url,{
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
					$("select[@name='ipType']").html("<option value='-1'>==请选属地==</option>");
					$("select[@name='ipValue']").html("<option value='-1'>==请选IP类别==</option>");
				});
				break;
				
			case "ipType":
				var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!getIpTypeByCityId.action'/>";
				var cityId = $("select[@name='cityId']").val();
				
				if("-1"==cityId){
					$("select[@name='ipType']").html("<option value='-1'>==请选属地==</option>");
					$("select[@name='ipValue']").html("<option value='-1'>==请选IP类别==</option>");
					break;
				}
				
				$.post(url,{
					cityId:cityId
				},function(ajax){
					if("" == ajax){
						$("select[@name='ipType']").html("<option value='-1'>==未检索到数据==</option>");
						$("select[@name='ipValue']").html("<option value='-1'>==未检索到数据==</option>");
						$("input[name=hostIp]").val("");
					}else{
						gwShare_parseMessage(ajax,$("select[@name='ipType']"),selectvalue);
						$("select[@name='ipValue']").html("<option value='-1'>==请选IP类别==</option>");
					}
				});
				break;
				
			case "ipValue":
				var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!getIpByIpType.action'/>";
				var cityId = $("select[@name='cityId']").val();
				var ipType = $("select[@name='ipType']").val();
				
				if("-1"==ipType){
					$("select[@name='ipValue']").html("<option value='-1'>==请选IP类别==</option>");
					break;
				}
				$.post(url,{
					cityId:cityId,
					ipType:ipType
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='ipValue']"),selectvalue);
				});
				break;	
				
			default:
				alert("未知查询选项！");
				break;
		}	
	}
	
	function gwShare_parseMessage(ajax,field,selectvalue){
		var flag = true;
		if(""==ajax){
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");
		option = "<option value='-1' selected>==请选择==</option>";
		field.append(option);
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			if(selectvalue==xValue){
				flag = false;
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
			}else{
				//根据每组value和text标记的值创建一个option对象
				option = "<option value='"+xValue+"'>=="+xText+"==</option>";
			}
			try{
				field.append(option);
			}catch(e){
				alert("检索失败！");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD >
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	您当前的位置：设备诊断
		</TD>
	</TR>
</TABLE>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<TR >
		<td>
			<ms:inArea areaCode="sx_lt" notInMode="false">
				<%@ include file="/gtms/stb/share/gwShareDeviceQueryForSxlt.jsp"%>
			</ms:inArea>
			<ms:inArea areaCode="sx_lt" notInMode="true">
				<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
			</ms:inArea>
		</td>
	</TR>
	<tr>
		<td id="chooseDev"></td>
	</tr>
	
	<ms:inArea areaCode="sx_lt" notInMode="false">
	<tr id="chooseDevHtml">
		<td>
			<%@ include file="/gtms/stb/share/gwShareDeviceListForSxlt.jsp"%>
		</td>
	</tr>
	</ms:inArea>
	
	<tr>
		<td bgcolor="#FFFFFF">
			<table width="100%"  class="querytable">
				<FORM NAME="frm" METHOD="POST" ACTION="">
					<input type="hidden" value="" name="deviceId"/>
					<TR  id="trDeviceResult" style="display: none">
						<td nowrap  class="title_2"  width="15%">设备序列号</td>
						<td id="tdDeviceSn" width="35%">
						</td>
						<td nowrap  class="title_2"  width="15%">属地</td>
						<td id="tdDeviceCityName" width="35%">
						</td>
					</tr>
					<TR  >
						<Td colspan="4" class="title_1">配置选项</Td>
					</TR>
					<tr id="line1" style="display:">
						<td nowrap class="title_2" width="10%">包大小(byte)</td>
						<td width="40%">
							<input type="text" name="dataBlockSize" class="bk" size="18"
								value="32" >&nbsp;
						</td>
						<td nowrap class="title_2" width="10%">包数目</td>
						<td width="40%">
							<input type="text" name="numberOfRepetitions" class="bk"
								size="16" value="2">
						</td>
					</tr>
					<tr id="line2" style="display:">
						<td nowrap  class="title_2" >目的IP</td>
						<td>
							<input type="text" maxlength="15" name="hostIp" class="bk" width="10%"/>
							<%
								if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
							%>
								<br>
								<select name="cityId" class="bk" onchange="changeSelect('ipType','-1')">
									<option value="-1">==请选择==</option>
								</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="ipType" class="bk" onchange="changeSelect('ipValue','-1')">
									<option value="-1">==请选属地==</option>
								</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="ipValue" class="bk" onchange="changeIP(this)">
									<option value="-1">==请选IP类别==</option>
								</select>
							<%
								}else if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ 
							%>
							<s:select onchange="changeIP(this)" list="IpMap"
									  headerKey="-1" headerValue="请选择" listKey="ip_value"
									  listValue="ip_name" cssClass="bk">
							</s:select>
							<%
								}else{
							%>
							<select onchange="changeIP(this)">
								<option value="">--请选择--</option>
								<!-- 云外的现网和测试环境 -->
								<!-- <option value="58.223.80.57">iTV综合网管</option> -->
								<!-- 云内 -->
								<option value="61.128.117.21">iTV综合网管</option>
								<!-- <option value="58.223.251.175">主EPG服务器</option>
								<option value="58.223.251.139">业务管理平台</option> -->
								<option value="172.16.4.8">中兴ITV平台</option>
								<option value="172.23.1.11">华为ITV平台</option>
								<option value="172.0.10.172">升级服务器</option>
								<option value="172.0.10.178">网管服务器</option>
							</select>
							<%
								}
							%>
						</td>
						<td nowrap class="title_2" >超时时间(s)</td>
						<td >
							<input type="text" name="timeout" class="bk" size="16" value="10">
						</td>
					</tr>
					<tr >
						<td class="foot" colspan="4">
							<div align="right">
								<button type="button" id="ping_btn" onclick="ExecMod()"> 诊 断 </button>
							</div>
						</td>
					</tr>
					<TR  >
						<TH colspan="4">诊断结果</TH>
					</TR>
					<TR >
						<td colspan="4" valign="top">
							<div id="div_ping"
								style="width:100%; height:120px; z-index:1; top: 100px;"></div>
						</td>
					</TR>
				</FORM>
			</table>
		</td>
	</tr>
</table>
<script>
var gwShare_queryType = '<s:property value="gwShare_queryType"/>';
var gwShare_queryField = '<s:property value="gwShare_queryField"/>';
var gwShare_queryParam = '<s:property value="gwShare_queryParam"/>';
gwShare_queryChange(gwShare_queryType);
//参数类型
if(gwShare_queryType == 1){
	var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
	for(var i = 0; i < gwShare_queryField_arr.length; i++){
		var item = gwShare_queryField_arr[i];
		if(item.value ==  gwShare_queryField){
			item.checked = true;
		}
	}
	//参数
	$("input[@name='gwShare_queryParam']").val(gwShare_queryParam);
}
</script>