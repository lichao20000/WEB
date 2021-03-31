<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>路由用户查询</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

  <%
  
  String isBatch = request.getParameter("isBatch");
  String gw_type = request.getParameter("gw_type");
  if(null == gw_type ||  "".equals(gw_type)){
	  gw_type="1";  
  }
  boolean isMatchSQL = false;
  
long roleId = ((UserRes)session.getAttribute("curUser")).getUser().getRoleId();
String shortName = LipossGlobals.getLipossProperty("InstArea.ShortName");
//安徽的省中心管理员权限才可以自定sql查询设备
if("ah_dx".equals(shortName)&&(1==roleId||2==roleId))
{
	isMatchSQL = true;
}
boolean allSelect = "ah_dx".equals(shortName);
%>


<SCRIPT LANGUAGE="JavaScript">
//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["mainForm","dataForm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

function dyniframesize() 
{
	var dyniframe=new Array()
	for (i=0; i<iframeids.length; i++)
	{
		if (document.getElementById)
		{
			//自动调整iframe高度
			dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
			if (dyniframe[i] && !window.opera)
			{
     			dyniframe[i].style.display="block"
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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block"
		}
	}
}


$(function(){
	dyniframesize();
});

$(window).resize(function(){
	dyniframesize();
}); 

/* reg_verify - 完全用正则表达式来判断一个字符串是否是合法的IP地址，
如果是则返回true，否则，返回false。*/


//验证输入参数的长度是否合法
function do_test()
{
	var single_condition = $.trim($("input[@name='routeBean.single_condition']").val());
		$("input[@name='routeBean.single_condition']").val(single_condition);
	  var queryType = $("select[@name='routeBean.queryType']").val();
	  
	 	if(""==single_condition){
	 		$("input[@name='routeBean.query_type']").val("2");
	}else if(""!=single_condition){
		
		if("1" == queryType){
			if(single_condition.length<6&&single_condition.length>0){
			alert("请至少输入最后6位设备序列号进行查询！");
			document.mainForm.single_condition.focus();
			return false;
		}
		}
		$("input[@name='routeBean.query_type']").val("1");
	}
	
	return true;
}

function do_query(){
	
	if(!do_test()){
		return;
	}
	
	setTimeout("gwShare_queryDevice()", 2000);
}



/*------------------------------------------------------------------------------
//函数名:		queryChange
//参数  :	change 1:简单查询、2:高级查询
//功能  :	根据传入的参数调整显示的界面
//返回值:		调整界面
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_queryDevice(){
	$("input[@name='gwShare_queryButton']").attr("disabled",true);
	var form = document.getElementById("mainForm");
	form.action = "<s:url value="/gwms/resource/routeInfoQuery!queryRouteInfo.action"/>";
	form.submit();
}



/*------------------------------------------------------------------------------
//函数名:		初始化函数（ready）
//参数  :	无
//功能  :	初始化界面（DOM初始化之后）
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function timeDefault(){
	var date = new Date();
	 var dt = date;
	 var seperator1 = "-"; 
	 var seperator2 = ":"; 
	 var year = date.getFullYear();
	 var month = date.getMonth() + 1;
   var strDate = date.getDate();
  
   if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
   
    var currentdate = year + seperator1 + month + seperator1 + strDate;
    
    dt.setMonth( dt.getMonth()-6 );
   	 var yearBefore = dt.getFullYear();
	 var monthBefore = dt.getMonth() + 1;
   var strDateBefore = dt.getDate();
   if (monthBefore >= 1 && monthBefore <= 9) {
        monthBefore = "0" + monthBefore;
    }
    if (strDateBefore >= 0 && strDateBefore <= 9) {
        strDateBefore = "0" + strDateBefore;
    }
   var beforetdate = yearBefore + seperator1 + monthBefore + seperator1 + strDateBefore;
   $("input[@name='routeBean.start_time']").val(beforetdate);
	 $("input[@name='routeBean.end_time']").val(currentdate);
}



$(function(){
	timeDefault();
	gwShare_change_select("routeBean.city","-1");
	gwShare_change_select("routeBean.vendor","-1");
	var gw_type=<%=gw_type%>;
	$("input[@name='routeBean.gw_type']").val(gw_type);
});

/*------------------------------------------------------------------------------
//函数名:		重写reset
//参数  :	change 1:简单查询、2:高级查询、3、导入查询
//功能  :	对页面进行重置
//返回值:		页面重置
//说明  :	
//描述  :	Create 2010-4-26 of By qxq
------------------------------------------------------------------------------*/
function gwShare_revalue(){
		$("input[@name='routeBean.single_condition']").val("");
		$("input[@name='routeBean.query_type']").val("");
		$("select[@name='routeBean.queryType']").attr("value",'-1');
	  $("select[@name='routeBean.gwShare_cityId']").attr("value",'-1');
		$("select[@name='routeBean.gwShare_vendorId']").attr("value",'-1');
		$("select[@name='routeBean.gwShare_deviceModelId']").attr("value",'-1');
		$("select[@name='routeBean.gwShare_devicetypeId']").attr("value",'-1');
		$("select[@name='routeBean.gwShare_netType']").attr("value",'2');
    timeDefault();
		gwShare_change_select('routeBean.deviceModel','-1');
	
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
		case "routeBean.city":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='routeBean.gwShare_cityId']"),selectvalue);
			});
			break;
		case "routeBean.vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='routeBean.gwShare_vendorId']"),selectvalue);
				$("select[@name='routeBean.gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='routeBean.gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "routeBean.deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='routeBean.gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='routeBean.gwShare_deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='routeBean.gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='routeBean.gwShare_deviceModelId']"),selectvalue);
				$("select[@name='routeBean.gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "routeBean.devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='routeBean.gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='routeBean.gwShare_deviceModelId']").val();
			var gwShare_isBatch = <%=isBatch %>;
			if("-1"==deviceModelId){
				$("select[@name='routeBean.gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId,
				isBatch:gwShare_isBatch
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='routeBean.gwShare_devicetypeId']"),selectvalue);
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
	if("routeBean.city" == type && true==<%=allSelect%>)
	{
		option = "<option value='-1' selected>==全部==</option>";
	}
	else
	{
		option = "<option value='-1' selected>==请选择==</option>";
	}
	
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

function showMsg(){
	var ind = $("#queryType ").val();
	if("1" == ind){
		$("#warnMsg").show();
	}else{
		$("#warnMsg").hide();
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
<form name="mainForm" action="" target="dataForm">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<tr>
		<td >
			<table border=0 cellspacing=1 cellpadding=2 width="98%" align="center" bgcolor=#999999 >
				<tr><th colspan="4" id="gwShare_thTitle">路由用户查询</th></tr>
				<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="display:">
					<input type="hidden" name="routeBean.query_type"
							value=''>
							<input type="hidden" name="routeBean.gw_type"
							value=''>
					<TD width="15%" class=column align="right">
						<select id="queryType" name="routeBean.queryType" class="bk" onchange="showMsg()">
							<option value="1">设备序列号</option>
							<option value="2">宽带账号</option>
							<option value="3">逻辑ID</option>
							<option value="4">电话号码</option>
						</select>
					</TD>
					<TD align="left" width="35%"><input type="text" name="routeBean.single_condition" value="" size="25" maxlength="" />
						<font color="red" id ="warnMsg" >*可模糊匹配</font></TD>
					<TD align="right" class=column width="15%">属    地</TD>
					<TD align="left" width="35%">
						<select name="routeBean.gwShare_cityId" class="bk" >
							<option value="-1">==全部==</option>
						</select>
					</TD>
					
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr22" STYLE="display:">
					<TD align="right" class=column width="15%">厂    商</TD>
					<TD width="35%">
						<select name="routeBean.gwShare_vendorId" class="bk" onchange="gwShare_change_select('routeBean.deviceModel','-1')">
							<option value="-1">==请选择==</option>
						</select>
					</TD>
					<TD align="right" class=column width="15%">设备型号</TD>
					<TD align="left" width="35%">
						<select name="routeBean.gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('routeBean.devicetype','-1')">
							<option value="-1">请先选择厂商</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr23"  STYLE="display:">	
					<TD align="right" class=column width="15%">设备版本</TD>
					<TD width="35%">
						<select name="routeBean.gwShare_devicetypeId" class="bk"">
							<option value="-1">请先选择设备型号</option>
						</select>
					</TD>	
					<TD align="right" class=column width="15%">上网方式</TD>
					<TD width="35%">
						<select name="routeBean.gwShare_netType" class="bk">
							<option value="2" selected = "selected">路由</option>
							<option value="1">桥接</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr24"  STYLE="display:">
				<td align="right" class=column width="15%">
													开始时间：
												</td>
												<td align="left" width="35%">
													<input type="text" id="start_time" name="routeBean.start_time" readonly  class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('start_time'),dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">
												</td>
					<td align="right" class=column width="15%">
													结束时间：
												</td>
												<td align="left" width="35%">
													<input type="text" id="end_time" name="routeBean.end_time" readonly  class=bk >
														<img name="shortDateimg"
									 						onClick="WdatePicker({el:document.getElementById('end_time'),dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
									 							src="../../images/dateButton.png" width="15" height="12"
									 								border="0" alt="选择">
												</td>
				</TR>
			
				<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
						<input type="button" onclick="javascript:do_query()" class=jianbian 
						name="gwShare_queryButton" value=" 查 询 " />
						<input type="button" class=jianbian onclick="javascript:gwShare_revalue()" 
						name="gwShare_reButto" value=" 重 置 " />
					</td>
				</tr>
				
			</table>
		</td>
	</tr>
	<tr>
		<td height="25"></td>
	</tr>
		<TR>
				<TD bgcolor=#ffffff id="idData" align="center"><iframe id="dataForm"
					name="dataForm" height="0" frameborder="0" scrolling="no"
					width="98%" src=""></iframe></TD>
			</TR>
</TABLE>
</form>