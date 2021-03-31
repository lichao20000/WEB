<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%
		request.setCharacterEncoding("GBK");
		String instAreaName = (String)request.getAttribute("instAreaName");
		String telecom = LipossGlobals.getLipossProperty("telecom");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>BSS业务查询</title>
		<%
			 /**
			 * BSS业务查询
			 * 
			 * @author qixueqi(4174)
			 * @version 1.0
			 * @since 2010-09-08
			 * @category
			 */
		%>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<style type="text/css">
		#openUserTr{
			text-align: right;
		}
		</style>
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		<script type="text/javascript">
function query(){
	configInfoClose();
	bssSheetClose();
	var cityId = $.trim($("select[@name='cityId']").val());
	var area = '<%=instAreaName%>';
	if(area!="cq_dx"){
		if(cityId == "-1"){
	         alert("请选择属地");
	         return false;
	    } 
	}
	if(area == "sd_lt"){
		$("#tip_loading").show();
	}
    var username = $.trim($("input[@name='username']").val());
    if(username.length > 0 && username.length <6){
    	alert("请至少输入最后6位LOID进行查询！");
    	return false;
    }
    $("input[@name='username']").val(Trim($("input[@name='username']").val()));
	document.selectForm.submit();
}
function openUser(){
	var strpage = "/itms/itms/service/bssSheetByHand4HBLT.action?gw_type=1";
	window.open(strpage, "",
					"left=20,top=20,width=900,height=650,resizable=yes,scrollbars=yes");
}

function ToExcel(){
	var usernameType=$("select[@name='cityId']").val();
	var username=Trim($("input[@name='username']").val());
	var cityId=$("select[@name='cityId']").val();
	var servTypeId=$("select[@name='servTypeId']").val();
	var openstatus=$("select[@name='openstatus']").val();
	var startOpenDate=$("input[@name='startOpenDate']").val();
	var endOpenDate=$("input[@name='endOpenDate']").val();
	var devicetype=$("select[@name='devicetype']").val();
	var area = '<%=instAreaName%>';
	
	var url = "<s:url value='/itms/service/bssSheetServSDLT!getBssSheetServInfoCount.action'/>";
	$.post(url, {
		usernameType : usernameType,
		username : username,
		cityId : cityId,
		servTypeId : servTypeId,
		openstatus : openstatus,
		startOpenDate : startOpenDate,
		endOpenDate : endOpenDate,
		devicetype : devicetype
		}, function(ajax) {
			if(area!="cq_dx" && ajax>100000){
   				alert("数据量太大不支持导出 ");
   				return;
			}else if (area=="cq_dx" && ajax>20000){
				alert("数据量太大不支持导出 ");
   				return;
			}else{
				var mainForm = document.getElementById("form");
				mainForm.action="<s:url value='/itms/service/bssSheetServSDLT!getBssSheetServInfoExcel.action'/>";
				mainForm.submit();
	    		mainForm.action="<s:url value='/itms/service/bssSheetServSDLT!getBssSheetServInfo.action'/>";
			}
	});
}


function queryServerType(){
   var serValue = document.selectForm.servTypeId.value;
   if(serValue=="14"){
		this.tr_voipProtocalType.style.display="";
   }else{
   		this.tr_voipProtocalType.style.display="none";
   		document.selectForm.voipProtocalType.value="";
   }
}

function configInfoClose(){
	$("td[@id='configInfoEm']").hide();
	$("td[@id='configInfo']").hide();
}

function configDetailClose(){
	$("td[@id='bssSheetDetail']").hide();
}

function bssSheetClose(){
	$("td[@id='bssSheetInfo']").hide();
}

function loading(){
	$("#tip_loading").hide();
}


function configDetailInfo(strategyId,deviceSN,servTypeId){
	var gw_type = document.getElementsByName("gw_type")[0].value;
	servTypeId = servTypeId ? servTypeId : "";
	var page = "<s:url value='/itms/service/bssSheetServSDLT!getConfigDetail.action'/>?gw_type=" +gw_type+ "&strategyId="+strategyId+"&deviceSN="+deviceSN
			+ "&servTypeId="+servTypeId;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function solutions(result_id,deviceSN){
	var url = "<s:url value='/itms/service/bssSheetServSDLT!getSolution.action'/>?&deviceSN="+deviceSN+"&result_id="+result_id;
	/*
	var tempForm = document.createElement("fm");
	tempForm.id="tempForm1";    
	tempForm.method="post";    
	tempForm.action=url;
	
	var hideInput1 = document.createElement("input");
	hideInput1.type="hidden";    
	hideInput1.name= "solution"  
	hideInput1.value= solution;  
	tempForm.appendChild(hideInput1);
	tempForm.attachEvent("onsubmit",function(){ openWindow();});
 	tempForm.fireEvent("onsubmit");  
	tempForm.submit();  
	document.body.removeChild(tempForm);
	*/
	//var obj= window.showModelessDialog(url,"","dialogHeight=350px;dialogwidth=410px;dialogLeft=260;dialogTop=100;help=no;resizable=o;status=no;scrollbars=no;"); 
	window.open(url,"","left=50,top=50,width=600,height=450,resizable=yes,scrollbars=yes");
}

function resetData(userId, deviceId, oui, deviceSN, servTypeId, servstauts) {
	if (deviceId == "") {
		alert("用户未绑定设备，请先绑定设备，再激活！");
		return;
	}
	if (confirm('重新激活是将该业务置为未做状态，确实要重置吗?')) {
		var url = "<s:url value='/itms/service/bssSheetServSDLT!callPreProcess.action'/>";
		$.post(url, {
			userId : userId,
			deviceSN : deviceSN,
			deviceId : deviceId,
			servTypeId : servTypeId,
			servstauts : servstauts,
			oui : oui
		}, function(ajax) {
			if (ajax == "1") {
				alert("调预读成功！");
				query();
			} else if (ajax == "-1") {
				alert("参数为空！");
			} else if (ajax == "-2") {
				alert("调预读失败！");
			}
		});
		//$("td[@id='temp123']").html("<font color='red'>未做</font>");
		//$('#resultStr',window.parent.document).html("<font color='red'>02586588146重新激活成功！</font>");
	}
}
function configLog(deviceSN,deviceId,servTypeId,servstauts,wanType){
	var gw_type = document.getElementsByName("gw_type")[0].value;
	var page = "<s:url value='/itms/service/bssSheetServSDLT!getConfigLogInfo.action'/>?"
		+ "deviceSN=" + deviceSN
		+ "&deviceId=" + deviceId
		+ "&servTypeId=" + servTypeId
		+ "&servstauts=" + servstauts
		+ "&wanType=" + wanType
		+ "&gw_type=" + gw_type;
	window.open(page,"","left=20,top=20,width=700,height=350,resizable=yes,scrollbars=yes");
}

//根据需要选择高级查询选项
function ShowDialog(leaf){
	//pobj = obj.offsetParent;
	oTRs = document.getElementsByTagName("TR");
	var m_bShow;
	var setvalueTemp = 0;
	for(var i=0; i<oTRs.length; i++){
		if(oTRs[i].leaf == leaf){
			m_bShow = (oTRs[i].style.display=="none");
			oTRs[i].style.display = m_bShow?"":"none";
		}
	}
	if(m_bShow){
		setvalueTemp = "1";
	}
	setValue(setvalueTemp);
}

function setValue(){
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	
	$("input[@name='time_start']").val(year+"-"+month+"-"+day+" 00:00:00");
	$("input[@name='time_end']").val(year+"-"+month+"-"+day+" 23:59:59");
	
}

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["dataForm"];

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes";

function dyniframesize() 
{
	var dyniframe=new Array();
	for (var i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block";
     			//如果用户的浏览器是NetScape
     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
      			//如果用户的浏览器是IE
     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
   			 }
   		}
		//根据设定的参数来处理不支持iframe的浏览器的显示问题
		if ((document.all || document.getElementById) && iframehide=="no")
		{
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
    		tempobj.style.display="block";
		}
	}
}

$(function(){
	//setValue();
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
});

<%-- 只有当家庭用户和政企用户融合时，才展示用户类型和终端类型查询条件 --%>
<s:if test="gw_type == 3">
$(function(){
	initSpecOptions();
	changeCustomerType();
	$("#cust_type_id").bind("change", changeCustomerType);
});

var itmsOptions = new Array();
var bbmsOptions = new Array();
function initSpecOptions()
{
	var itmsIndex = 0;
	var bbmsIndex = 0;
	itmsOptions[itmsIndex++] = new Option("==请选择==", "");
	bbmsOptions[bbmsIndex++] = new Option("==请选择==", "");
	var specSelect = $("#spec_id").get(0);
	for (var i = 0; i < specSelect.length; i++)
	{
		var option = specSelect.options[i];
		var spIndex = option.value.indexOf(",");
		var suffix = option.value.substring(0, spIndex);
		option.value = option.value.substring(spIndex + 1);
		if ("2" == suffix){
			// itms
			itmsOptions[itmsIndex++] = option;
		}
		else{
			bbmsOptions[bbmsIndex++] = option;
		}
	}
}

<%-- 更新客户类型，即实现客户类型与客户终端规格级联 --%>
function changeCustomerType() {
	var specSelect = $("#spec_id").get(0);
	specSelect.length = 0;
	if ("1" == $("#cust_type_id option:selected").val()){
		// itms
		for (var i = 0; i < itmsOptions.length; i++){
			specSelect.options[i] = itmsOptions[i];
		}
	}
	else{
		for (var i = 0; i < bbmsOptions.length;i++){
			specSelect.options[i] = bbmsOptions[i];
		}
	}
	specSelect.selectedIndex = 0;
}
</s:if>
</script>
	</head>

	<body>
		<form id="form" name="selectForm" action="<s:url value='/itms/service/bssSheetServSDLT!getBssSheetServInfo.action'/>"
			target="dataForm">
			<input type="hidden" name="selectType" value="0" />
			<input type="hidden" name="gw_type" value='<s:property value="gw_type" />' />
			<input type="hidden" name="netServUp" value='<s:property value="netServUp" />' />
			<table>
				<tr>
					<td HEIGHT=20>
						&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						<table class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite">
									BSS业务查询
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15"
										height="12" />
									查询BSS业务开通情况
								</td>
								<s:if test='%{instAreaName == "hb_lt"}'>
								<td id="openUserTr">
									<button onclick="openUser()">
										&nbsp;开&nbsp;户&nbsp;
									</button>
								</td>
								</s:if>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="querytable">

							<TR>
								<th colspan="4">
									BSS业务查询
								</th>
							</tr>

							<TR>
								<TD class=column width="15%" align='right'>
									<SELECT name="usernameType">
										<option value="1">
											LOID
										</option>
										<option value="2">
											上网宽带账号
										</option>
										<option value="3">
											IPTV宽带账号
										</option>
										<option value="4">
											VoIP认证号码
										</option>
										<option value="5">
											VoIP电话号码
										</option>
										<ms:inArea areaCode="sd_lt" notInMode="true">
                                            <option value="6">
                                                VPDN宽带账号
                                            </option>
										</ms:inArea>
										<ms:inArea areaCode="hb_lt">
											<option value="7">
												语音EID
											</option>
										</ms:inArea>
										<ms:inArea areaCode="cq_dx">
											<option value="8">
												随选入云私网账号
											</option>
										</ms:inArea>
										<%if(telecom.equals("CTC")){%>
											<option value="12">云网超宽带账号</option>
										<%}%>
									</SELECT>
								</TD>
								<TD width="35%">
									<input type="text" name="username" size="20" maxlength="30"
										class=bk />
								</TD>
								<TD class=column width="15%" align='right'>
									属地
								</TD>
								<TD width="35%">
									<s:select list="cityList" name="cityId" headerKey="-1"
										headerValue="请选择属地" listKey="city_id" listValue="city_name"
										 cssClass="bk"></s:select>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									业务类型
								</TD>
								<TD width="35%">
									<SELECT name="servTypeId"  onchange="queryServerType()">
										<option value="">
											==请选择==
										</option>
										<option value="10">
											==上网业务==
										</option>
										<ms:inArea areaCode="sd_lt" notInMode="false">
											<option value="10_2">
												==停机==
											</option>
											<option value="10_1">
												==复机==
											</option>
										</ms:inArea>
										<option value="11">
											==IPTV==
										</option>
										<option value="14">
											==VoIP==
										</option>
										<option value="16">
											==路由宽带==
										</option>
										<ms:inArea areaCode="sd_lt" notInMode="true">
                                            <option value="28">
                                                ==VPDN==
                                            </option>
										</ms:inArea>
										<ms:inArea areaCode="cq_dx">
											<option value="33">
												==随选入云私网==
											</option>
										</ms:inArea>
										<%if(telecom.equals("CTC")){%>
											<option value="47">==云网超宽带==</option>
										<%}%>
									</SELECT>
								</TD>
								<TD class=column width="15%" align='right'>
									语音协议类型
								</TD>
								<TD id="tr_voipProtocalType" width="35%" style="display:none">
									<SELECT name="voipProtocalType"  >
										<option value="">
											==请选择==
										</option>
										<option value="2">
											==H248==
										</option>
										<option value="1">
											==软交换SIP==
										</option>
										<option value="0">
											==IMS SIP==
										</option>
									</SELECT>
								</TD>
							</TR>
							<TR>
								<TD class=column width="15%" align='right'>
									BSS受理开始时间
								</TD>
								<TD width="35%">
									<input type="text" name="startOpenDate" readonly class=bk
										value="<s:property value="startOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
								<TD class=column width="15%" align='right'>
									BSS受理结束时间
								</TD>
								<TD width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="选择">
									&nbsp;
									<font color="red"> *</font>
								</TD>
							</TR>
							<TR>
							<s:if test='%{instAreaName != "jl_dx"}'>
								<TD class=column width="15%" align='right'>
									BSS终端类型
								</TD>
								<TD width="35%" >
									<!-- <SELECT name="devicetype">
										<option selected value="2">E8-C</option>
										<option value="1">E8-B</option>
										<option value="0">全部</option>
									</SELECT>-->
									<s:select list="devicetypeList" name="devicetype" listKey="type_id" listValue="type_name"
										 cssClass="bk"></s:select>
								</TD>
							</s:if>
								<TD class=column width="15%" align='right'>
									开通状态 <s:property value="gwType"/>
								</TD>
								<TD width="35%">
									<SELECT name="openstatus">
										<option value="">
											==请选择==
										</option>
										<option value="1">
											==成功==
										</option>
										<option value="0">
											==未做==
										</option>
										<option value="-1">
											==失败==
										</option>
									</SELECT>
								</TD>
							</TR>
							<s:if test="gw_type == 3">
							<TR>
								<TD class=column width="15%" align='right'>客户类型</TD>
								<TD width="35%" >
									<SELECT id="cust_type_id" name="cust_type_id">
										<option selected value="">==请选择==</option>
										<option value="2">家庭客户</option>
										<option value="1">政企客户</option>
									</SELECT>
								</TD>
								<TD class=column width="15%" align='right'>客户规格</TD>
								<TD width="35%">
									<lk:select name="spec_id" listValue="spec_name" listKey="gw_type+','+id" 
									listTable="tab_bss_dev_port"/>
								</TD>
							</TR>
							</s:if>
							<TR>
								<td colspan="4" align="right" class=foot>
									<button onclick="query()">
										&nbsp;查 询&nbsp;
									</button>
								</td> 
							</TR>
						</table>
					</td>
				</tr>
				<tr>
					<td height="25" id="resultStr">

					</td>
				</tr>
				<tr>
		           <td>
					  <div style="width: 100%; display: none; text-align: center;"
						id="tip_loading">
						正在加载数据,请耐心等待......
					   </div>
				    </td>
			    </tr>
				<tr>
					<td>
						<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
							scrolling="no" width="100%" src=""></iframe>
					</td>
				</tr>
				<tr>
					<td height="25" id="configInfoEm" style="display: none">

					</td>
				</tr>
				
				<tr>
					<td id="configInfo">

					</td>
				</tr>
				<tr>
					<td height="25">

					</td>
				</tr>
				<tr>
					<td id="bssSheetInfo">

					</td>
				</tr>
				
				<tr>
					<td id="bssSheetDetail">

					</td>
				</tr>
				
				
			</table>
			<br>
		</form>
	</body>
</html>
<%@ include file="../../foot.jsp"%>
