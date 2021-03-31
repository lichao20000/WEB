<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备查询</title>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
  <%
  	String isBatch = request.getParameter("isBatch");
 	String gw_type = request.getParameter("gw_type");
  if(null == gw_type ||  "".equals(gw_type)){
	  gw_type="4";  
  }
  boolean isMatchSQL = false;
%>


<SCRIPT LANGUAGE="JavaScript">
	var servType = null;
	$(function(){
	servType =getUrlParam('servType');
	
	setValue();
	
    initPage()
	
	//加载相关项
	gwShare_change_select("city","-1");
	gwShare_change_select("vendor","-1");
	
});
	
function initPage(){
	    $("input[@name='gwShare_queryType']").val("3");
		$("#singlePart").hide();
		$("#batchPart").show();
		$("#doButton_a").val("分析文件");
		$("#gwShare_thTitle").text("导 入 文 件");
}
	
	
	function setValue(){
		theday=new Date();
		day=theday.getDate();
		month=theday.getMonth()+1;
		year=theday.getFullYear();
		document.getElementById("startOpenDate").value=year+"-"+1+"-"+1+" 00:00:00";
		document.getElementById("endOpenDate").value=year+"-"+month+"-"+day+" 23:59:59";
	}	
	
function do_test()
{
	//获取输入框内容，trim一下
	//var gwShare_queryParam = document.gwShare_selectForm.gwShare_queryParam.value; 
	  var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
		//$.trim($("input[@name='gwShare_queryParam']").val());
		gwShare_queryParam = $.trim(gwShare_queryParam);
	
	var title = document.getElementById("gwShare_thTitle").innerHTML;

  	if(title == "导 入 查 询"){
	 		 gwShare_spellSQL();	
			return true;
	}
	return true;
}

function gwShare_queryField_selected(value){
	$("input[@name='gwShare_queryField_temp']").val(value.value);
}
function do_query(){
	if(!do_test()){
		return;
	}
	document.getElementById("dataForm").contentWindow.document.body.innerText = "";
	setTimeout("gwShare_queryDevice()", 2000);
}
function gwShare_queryDevice(){
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value='/gtms/stb/resource/stbBindChage!queryDeviceList.action'/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
   if("1"==gwShare_queryType){
    var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
		var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
	}else {
		if(""==gwShare_fileName){
			alert("请先上传文件！");
			return;
		}
	}
	var tips = "";
	var isHaveBind;
	if("batch_on" == servType || "batch_off" == servType || "4" == <%=gw_type%>){
		var chbx_isMactchSQL = $("input[@name='chbx_isMactchSQL']").val();
	 	var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
	    var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
	    var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
	    var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
	    var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
	    var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
	    var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
	    var gwShare_matchSQL = $.trim($("textarea[@name='gwShare_matchSQL1']").val() +" " +$("textarea[@name='gwShare_matchSQL']").val());
	    gwShare_matchSQL = gwShare_matchSQL.replace(new RegExp("'", 'g'), "[");
		
		$.post(url,{
			gwShare_matchSQL:gwShare_matchSQL,
			gwShare_cityId:gwShare_cityId,
			gwShare_onlineStatus:gwShare_onlineStatus,
			gwShare_vendorId:gwShare_vendorId,
			gwShare_deviceModelId:gwShare_deviceModelId,
			gwShare_devicetypeId:gwShare_devicetypeId,
			gwShare_bindType:gwShare_bindType,
			gwShare_fileName:gwShare_fileName,
			gwShare_deviceSerialnumber:gwShare_deviceSerialnumber
	    },function(ajax){
	    	if(ajax == "1"){
	    		alert("分析成功！");
	    	}else{
	    		alert("分析失败！");
	    	}
	    });
	}
}

function doTest1(){
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	if(""==gwShare_fileName){
		alert("请先上传文件！");
		return;
	}
	$("input[@name='gwShare_queryType']").val("3");
	var url="<s:url value='/gtms/stb/resource/stbBindChage!getDeviceLists.action'/>";
	var form = document.getElementById("gwShare_selectForm");
	form.action = url;
	
	form.submit();
}
/*------------------------------------------------------------------------------
//函数名:		重写reset
//参数  :	change 1:简单查询、2:高级查询、3、导入查询
//功能  :	对页面进行重置
//返回值:		页面重置
//说明  :	
//描述  :	Create 2010-4-26 of By qxq
------------------------------------------------------------------------------*/
function gwShare_revalue(){
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	
}
function gwShare_revalue(){
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	
		$("input[@name='gwShare_queryField']").get(0).checked = true;
		$("input[@name='gwShare_queryParam']").val("");
	
}
function gwShare_setImport(){
	$("input[@name='gwShare_import_value']").val("");
	
}

function spellSQL(obj) {
	if(!do_test())
	{
		var chbx_isMactchSQL = document.getElementsByName("chbx_isMactchSQL");
		chbx_isMactchSQL[0].checked=false;
		return;
	}
	if(obj.checked) {
		$("tr[@id='gwShare_tr25']").css("display","");
		$("tr[@id='gwShare_tr26']").css("display","");
		gwShare_spellSQL();
    } else {
    	$("tr[@id='gwShare_tr25']").css("display","none");
		$("tr[@id='gwShare_tr26']").css("display","none");
		$("textarea[@name='gwShare_matchSQL1']").val("");
		$("textarea[@name='gwShare_matchSQL']").val("");
    }
}
function gwShare_spellSQL(){
	var url = "<s:url value='/gtms/stb/resource/stbBindChage!queryDeviceListSQL.action'/>";
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
    var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
    var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
    var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
    var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
    var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
    var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
    var refresh= new Date().getTime();
    var gw_type= <%=gw_type%>;
	$.post(url,{
		gwShare_queryResultType:gwShare_queryResultType,
		gwShare_queryType:gwShare_queryType,
		gwShare_cityId:gwShare_cityId,
		gwShare_onlineStatus:gwShare_onlineStatus,
		gwShare_vendorId:gwShare_vendorId,
		gwShare_deviceModelId:gwShare_deviceModelId,
		gwShare_devicetypeId:gwShare_devicetypeId,
		gwShare_bindType:gwShare_bindType,
		gwShare_deviceSerialnumber:gwShare_deviceSerialnumber,
		refresh:refresh,
		gw_type:gw_type
    },function(ajax){
    	$("textarea[@name='gwShare_matchSQL1']").val(ajax);
    });
}





/*------------------------------------------------------------------------------
//函数名:		deviceSelect_change_select
//参数  :	type 
	            vendor      加载设备厂商
	            deviceModel 加载设备型号
	            devicetype  加载设备版本
//功能  :	加载页面项（厂商、型号级联）
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value='/gtms/stb/resource/stbBindChage!getCityNextChild.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "nextCity":
			if($("input[@name='hblt_SpeedTest_flg']").val()!="true"){
				return false;
			}
			var city_id = $("select[@name='gwShare_cityId']").val();
			var url = "<s:url value='/gtms/stb/resource/stbBindChage!getCityNextChildCore.action'/>";
			$.post(url,{
				gwShare_cityId:city_id
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_nextCityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value='/gtms/stb/resource/stbBindChage!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/resource/stbBindChage!getDeviceModel.action'/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value='/gtms/stb/resource/stbBindChage!getDevicetype.action'/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			var gwShare_isBatch = <%=isBatch %>;
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId,
				isBatch:gwShare_isBatch
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("未知查询选项！");
			break;
	}	
}

/*------------------------------------------------------------------------------
//函数名:		deviceSelect_parseMessage
//参数  :	ajax 
          	类似于XXX$XXX#XXX$XXX
          field
          	需要加载的jquery对象
//功能  :	解析ajax返回参数
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
//解析查询设备型号返回值的方法
function gwShare_parseMessage(type,ajax,field,selectvalue){
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
	//河北选择非全部地区、省中心的情况下显示县级地区
	if($("input[@name='hblt_SpeedTest_flg']").val()=="true" && "nextCity" == type){
		option = "<option value='-1' selected>==全部==</option>";
	}
	
	field.append(option);
	//河北选择全部地区、省中心，隐藏县级地区下拉框，value为-1
	if($("input[@name='hblt_SpeedTest_flg']").val()=="true" && "nextCity" == type){
		var city_id = $("select[@name='gwShare_cityId']").val();
		if("-1" == city_id || "00" == city_id){
			$("#nextCityDiv").hide();
			return false;
		}
	}
	
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
			alert("设备型号检索失败！");
		}
	}
	//河北选择非全部地区、省中心的情况下显示县级地区
	if($("input[@name='hblt_SpeedTest_flg']").val()=="true" && "nextCity" == type){
		$("#nextCityDiv").show();
	}
	if(flag){
		field.attr("value","-1");
	}
}
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<form name="gwShare_selectForm" action="<s:url value="/inmp/bss/gwDeviceQuery!queryDeviceList.action"/>"
	METHOD="post" target="dataForm"  style="overflow:auto;">
<input type="hidden" name="gwShare_queryType" value="" />
<input type="hidden" name="gwShare_queryResultType" value="radio" />
<input type="hidden" name="gwShare_queryField_temp" value="deviceSn" />
<input type="hidden" name="gwShare_import_value" value="none" />
 
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center" >
	<th colspan="4" id="gwShare_thTitle"></th>
	<tr>
		<td bgcolor=#999999>
			<table class="querytable" id="singlePart" border=0 cellspacing=1 cellpadding=2 width="100%" align="center" display="none">
				<tr bgcolor="#FFFFFF"  >
					<td colspan="4" align="center" width="100%">
						<div>
							<input type="input" class="bk" name="gwShare_queryParam" size="60" maxlength="60"/>
							<br /> 
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF"  >
					<td colspan="4" align="center" width="100%">
						<input type="radio" name="gwShare_queryField" value="deviceSn" checked onclick="gwShare_queryField_selected(this)"/> 设备序列号 &nbsp;&nbsp;
						<input type="radio" name="gwShare_queryField" value="username" onclick="gwShare_queryField_selected(this)"/> LOID &nbsp;&nbsp;
						<input type="radio" name="gwShare_queryField" value="kdname" onclick="gwShare_queryField_selected(this)"/> 宽带账号 &nbsp;&nbsp;
					</td>
					<TD align="right" class=column width="15%" ><font id="gwShare_td1"  STYLE="display:none">自定义查询</font></TD>
					<TD width="35%">
						<input type="checkbox" name="chbx_isMactchSQL" id="chbx_isMactchSQL"  STYLE="display:none" onclick="spellSQL(this);">
					</TD>
				</tr>
				<TR bgcolor="#FFFFFF" id="gwShare_tr25"  STYLE="display:none">
					<TD align="right" class=column width="15%">已生成的SQL</TD>
					<TD width="35%" colspan="3">
					    <textarea name="gwShare_matchSQL1" cols="100" rows="4" class=bk readonly="readonly">
					    </textarea>
						<font color="red">*已生成的SQL</font>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr26"  STYLE="display:none">
					<TD align="right" class=column width="15%">自定义查询</TD>
					<TD width="35%" colspan="3">
					    <textarea name="gwShare_matchSQL" cols="100" rows="4" class=bk></textarea>
						<font color="red">*定制SQL</font>
					</TD>
				</TR>
			</table>
			<table class="querytable" id="batchPart" border=0 cellspacing=1 cellpadding=2 width="100%" align="center" display="display">
				<tr  bgcolor="#FFFFFF" >
					<td align="right" width="15%">提交文件</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/inmp/bss/FileUpload.jsp"/>" height="20" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr >
					<td CLASS="green_foot" align="right">注意事项</td>
					<td colspan="3" CLASS="green_foot">
					1、需要导入的文件格式限于Excel、文本文件，即xls、txt格式 。
					 <br>
					2、文件的第一行为标题行，即【username】。
					 <br>
					3、文件只有一列。
					 <br>
					4、文件行数不要太多，最多2w行，以免影响性能。
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
			<td bgcolor=#999999 colspan="4">
							<input type="hidden" name="prot_type" value="tr069" />
							<TABLE id="tb1" border=0 cellspacing=1 cellpadding=2 width="100%" class="querytable">
								<TR>
									<TH colspan="4">
										查询条件
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
								 <TD class=column width="15%" align='right'>开始时间</TD>
									<TD width="35%">
										<input type="text" id="startOpenDate" name="startOpenDate" readonly class=bk > 
										<img name="shortDateimg"
											 onClick="WdatePicker({el:document.gwShare_selectForm.startOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											 src="<s:url value='/images/dateButton.png' />" width="15" height="12"
											 border="0" alt="选择">
									</TD>
									<TD class=column width="15%" align='right'>结束时间</TD>
									<TD width="35%">
										<input type="text" id="endOpenDate" name="endOpenDate" readonly class=bk >
										<img name="shortDateimg"
											 onClick="WdatePicker({el:document.gwShare_selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
											 src="<s:url value='/images/dateButton.png' />" width="15" height="12" border="0" alt="选择">
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" >
									<TD align="right" class=column width="15%">属    地</TD>
									<TD align="left" width="35%">
										<div style="float: left;">
											<select name="gwShare_cityId" class="bk" onchange="gwShare_change_select('nextCity','-1')">
												<option value="-1">==全部==</option>
											</select>
										</div>
										<div style="float: left; display: none;" id="nextCityDiv">
											<select name="gwShare_nextCityId" class="bk" >
												<option value="-1">==全部==</option>
											</select>
										</div>
									</TD>
									<TD align="right" class=column width="15%">厂    商</TD>
									<TD width="35%">
										<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
											<option value="-1">==请选择==</option>
										</select>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="right" class=column width="15%">设备型号</TD>
									<TD align="left" width="35%">
										<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
											<option value="-1">请先选择厂商</option>
										</select>
									</TD>
									<TD align="right" class=column width="15%">设备版本</TD>
									<TD width="35%">
										<select name="gwShare_devicetypeId" class="bk"">
											<option value="-1">请先选择设备型号</option>
										</select>
									</TD>	
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD align="right" class=column width="15%">业务账号</TD>
									<TD align="left" width="35%">
										<input type="text" name="username" class="bk" value=""/>
									</TD>
									<TD align="right" class=column width="15%">mac地址</TD>
									<TD width="35%">
										<input type="text" name="mac" class="bk" value=""/>
									</TD>	
								</TR>
							</TABLE>
			</td>
		</tr>	
	
		<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" id="doButton_a" onclick="javascript:do_query()" class=jianbian 
						name="gwShare_queryButton" value="分析文件" /> 
						
						<input type="button" class=jianbian onclick="javascript:doTest1()" 
						name="gwShare_query" value=" 导 入 查 询 " />
						
						<input type="button" class=jianbian onclick="javascript:doTest()" 
						name="gwShare_query" value=" 查 询 " />
					</td>
		</tr>
</TABLE>
</form>
</html>