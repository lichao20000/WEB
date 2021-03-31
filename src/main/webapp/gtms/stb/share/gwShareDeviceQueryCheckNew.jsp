<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@ page import="com.linkage.litms.LipossGlobals" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备查询</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
// function gwShare_queryField_selected(value){
// 	$("input[@name='gwShare_queryField_temp']").val(value.value);
// }

/*------------------------------------------------------------------------------
//函数名:		queryChange
//参数  :	change 1:简单查询、2:高级查询
//功能  :	根据传入的参数调整显示的界面
//返回值:		调整界面
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryDevice(){
	var	width=800;    
	var height=450; 
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
    
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("请先上传文件！");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
	
    var gwShare_RecordNum = $("input[@name='gwShare_RecordNum']").val();
    url = url + "&gwShare_RecordNum=" + gwShare_RecordNum;
    var gwShare_OrderByUpdateDate = $("input[@name='gwShare_OrderByUpdateDate']").val();
    url = url + "&gwShare_OrderByUpdateDate=" + gwShare_OrderByUpdateDate;
	url = url + "&refresh=" + new Date().getTime();
	var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=yes;edge=raised');    
	if(typeof(returnVal)=='undefined'){
		return;
	}else{
		deviceResult(returnVal);
	}
}

/*------------------------------------------------------------------------------
//函数名:		初始化函数（ready）
//参数  :	无
//功能  :	初始化界面（DOM初始化之后）
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
$(function(){
	var queryType = "<s:property value="queryType"/>";
	var queryResultType = "<s:property value="queryResultType"/>";
	var startQuery = "<s:property value="startQuery"/>";
	if(""!=queryType && null!=queryType){
		$("input[@name='gwShare_queryType']").val(queryType);
	}
	if(""!=queryResultType && null!=queryResultType){
		$("input[@name='gwShare_queryResultType']").val(queryResultType);
	}
// 	gwShare_queryChange(queryType);
	gwShare_queryChange('3');
	
	var str = Array($("input[@name='gwShare_queryParam']"),
					$("div[@id='gwShare_msgdiv']"),
					$("input[@name='gwShare_queryField_temp']"));
	
	//针对现网慢的情况，暂时去掉自适应匹配
	//$.autoMatch3("<s:url value='/gtms/stb/share/shareDeviceQuery!getDeviceSn.action'/>",str,"#");
});


/*------------------------------------------------------------------------------
//函数名:		queryChange
//参数  :	change 1:简单查询、2:高级查询、3:导入查询
//功能  :	根据传入的参数调整显示的界面
//返回值:		调整界面
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryChange(change){
	switch (change){
		case "3":
			$("td[@id='gwShare_thTitle']").html("导 入 查 询");
			$("input[@name='gwShare_queryType']").val("3");
			$("button[@name='gwShare_import']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("button[@name='gwShare_queryButton']").val("分析文件");
			break;
		default:
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
			alert("设备型号检索失败！");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}

/*------------------------------------------------------------------------------
//函数名:		gwShare_setGaoji
//参数  :	
//功能  :	加载高级查询
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_setGaoji(){
	$("input[@name='gwShare_gaoji_value']").val("");
	$("button[@name='gwShare_gaoji']").css("display","");
}
/*------------------------------------------------------------------------------
//函数名:		gwShare_setImport
//参数  :	
//功能  :	加载导入查询
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_setImport(){
	$("input[@name='gwShare_import_value']").val("");
	$("button[@name='gwShare_import']").css("display","");
}
/*------------------------------------------------------------------------------
//函数名:		trim
//参数  :	str 待检查的字符串
//功能  :	根据传入的参数进行去掉左右的空格
//返回值:		trim（str）
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function trim(str){
     return str.replace(/(^\s*)|(\s*$)/g,"");
}

/*------------------------------------------------------------------------------
//函数名:		gwShare_reButto
//参数  :	
//功能  :	加载重置按钮
//返回值:		
//说明  :	
//描述  :	Create 2018-10-17 of By zzs
------------------------------------------------------------------------------*/
function gwShare_revalue(){
	$("input[@name='gwShare_reButto_value']").val("");
	$("button[@name='gwShare_reButto']").css("display","");
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
<form name="gwShare_selectForm" action="<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceList.action"/>" target="dataForm">
<input type="hidden" name="gwShare_queryType" value="1" />
<input type="hidden" name="gwShare_queryResultType" value="checkbox" />
<input type="hidden" name="gwShare_queryField_temp" value="deviceSn" />
<input type="hidden" name="gwShare_gaoji_value" value="none" />
<input type="hidden" name="gwShare_import_value" value="none" />
<input type="hidden" name="gwShare_OrderByUpdateDate" value="1" /><!-- 1开启记录按照上报时间排序，0不开启 -->
<input type="hidden" name="gwShare_RecordNum" value="0" /><!-- 默认3条记录，0为查询全部，建议配置大小不超过50 -->

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">导 入 查 询</td></tr>
	
	<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:">
		<td align="right" width="15%">提交文件</td>
		<td colspan="3" width="85%">
			<div id="importUsername">
				<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
				</iframe>
				<input type="hidden" name=gwShare_fileName value=""/>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr32" style="display:">
		<td CLASS="green_foot" align="right">注意事项</td>
		<td colspan="3" CLASS="green_foot">
		1、需要导入的文件格式限于Excel、文本文件，即xls、txt格式 。txt文件注意文件编码为ANSI编码，其它编码会导致解析文件错误
		 <br>
		2、文件的第一行为标题行，即【业务账号】、或者【设备序列号】。
		 <br>
		3、文件只有一列。
		 <br>
		4、文件行数不要太多，以免影响性能。
		<br>
		<% if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
				|| "xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
		5、文件行数不要超过30000行，最多只解析前30000行。
		<% 
		} else{
		%>
		5、文件行数不要超过3000行，最多只解析前3000行。
		<% 
		} 
		%>
		</td>
	</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:gwShare_queryDevice();" 
				name="gwShare_queryButton" style="CURSOR:hand" style="display:" > 查 询 </button>
				<button onclick="javascript:gwShare_queryChange('3');" 
				name="gwShare_import" style="CURSOR:hand" style="display: none" >导入查询</button>
				<button onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: none" > 重 置 </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>