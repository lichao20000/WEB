<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备查询</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>

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


/* reg_verify - 完全用正则表达式来判断一个字符串是否是合法的IP地址，
如果是则返回true，否则，返回false。*/
function reg_verify(addr)
{
	//正则表达式
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

    if(addr.match(reg))
    {
    	//IP地址正确
        return true;
    }
    else
    {
    	//IP地址校验失败
         return false;
    }
}

function do_query(){
	setTimeout("gwShare_queryDevice()", 2000);
}

function gwShare_queryField_selected(value){
	$("input[@name='gwShare_queryField_temp']").val(value.value);
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
	
	var	width=800;    
	var height=450; 
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
	var url="<s:url value="/gwms/share/gwDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
    if("1"==gwShare_queryType){
		var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
		var gwShare_queryParam = $.trim($("input[@name='gwShare_queryParam']").val());
		url = url + "&gwShare_queryField=" + gwShare_queryField;
		url = url + "&gwShare_queryParam=" + gwShare_queryParam;
	}else if("2"==gwShare_queryType){
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
        alert(gwShare_matchSQL);
        url = url + "&gwShare_matchSQL=" + gwShare_matchSQL;
        url = url + "&gwShare_cityId=" + gwShare_cityId;
        url = url + "&gwShare_onlineStatus=" + gwShare_onlineStatus;
        url = url + "&gwShare_vendorId=" + gwShare_vendorId;
        url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
        url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
        url = url + "&gwShare_bindType=" + gwShare_bindType;
        url = url + "&gwShare_deviceSerialnumber=" + gwShare_deviceSerialnumber;
	}else if("4"==gwShare_queryType){
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("请先上传文件！");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
		$.post(url,{
			},function(ajax){
					var _rs = new Array();
					if (ajax>2000)
					{
						alert("设备数量超过最大数2000!");
						return;
					}
					if(confirm("选择的设备数量为:"+ajax+",是否继续?")){
						_rs[0] = ajax;
					}else{
						return;
					}
					_rs[1] = gwShare_fileName;
					_rs[2] = gwShare_queryType;
					deviceUpResult(_rs);
			});
		return;
	}else {
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("请先上传文件！");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
	}
	
	url = url +"&refresh=" + new Date().getTime();
	url=url+"&gw_type="+<%=gw_type %>;
	if($("#chbx_isMactchSQL").attr("checked")==true)
    {
		var gwShare_matchSQL = $("textarea[@name='gwShare_matchSQL1']").val() +" " +$("textarea[@name='gwShare_matchSQL']").val();
		gwShare_matchSQL = gwShare_matchSQL.replace(new RegExp("'", 'g'), "[");
		if(gwShare_matchSQL == ""){
	         alert("SQL为空或有语法错误，请检查");
	         return false;
	    }
		url="<s:url value="/gwms/share/gwDeviceQuery!queryDeviceListBySQL.action"/>?gwShare_matchSQL="+gwShare_matchSQL+"&gwShare_queryResultType="+gwShare_queryResultType;
    }
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
	// alert(queryType);
	// gwShare_queryChange(queryType);
	
	var str = Array($("input[@name='gwShare_queryParam']"),
					$("div[@id='gwShare_msgdiv']"),
					$("input[@name='gwShare_queryField_temp']"));
	
	//针对现网慢的情况，暂时去掉自适应匹配
	//$.autoMatch3("<s:url value='/gwms/share/gwDeviceQuery!getDeviceSn.action'/>",str,"#");
});



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
	if("city" == type && true==<%=allSelect%>)
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
	$("input[@name='gwShare_gaoji']").css("display","");
}

function gwShare_setUpImport(){
	$("input[@name='gwShare_upimport_value']").val("");
	$("input[@name='gwShare_up_import']").css("display","");
}
/*------------------------------------------------------------------------------
//函数名:		gwShare_setGaoji
//参数  :	
//功能  :	加载高级查询
//返回值:		
//说明  :	
//描述  :	Create 2009-12-25 of By qxq
------------------------------------------------------------------------------*/
function gwShare_setImport(){
	$("input[@name='gwShare_import_value']").val("");
	$("input[@name='gwShare_import']").css("display","");
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
	var url = "<s:url value='/gwms/share/gwDeviceQuery!queryDeviceListSQL.action'/>";
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

$(function(){
	$('#anaBtn').click(function(){
		var url  = $('#anaUrl').val();
		var fileName = $('#gwShare_fileName').val();
		var page = url + "?gwShare_fileName="+fileName;
		// alert(page);
		document.all("childFrm").src = page;
		$('#childFrm').show();
	});
});

//** iframe自动适应页面 **//
//输入你希望根据页面高度自动调整高度的iframe的名称的列表
//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
//定义iframe的ID
var iframeids=["childFrm"]

//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
var iframehide="yes"

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
			var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
    		tempobj.style.display="block";
		}
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
<input type="hidden" name="gwShare_queryType" value="3" />
<input type="hidden" name="gwShare_queryResultType" value="radio" />
<input type="hidden" name="gwShare_queryField_temp" value="deviceSn" />
<input type="hidden" name="gwShare_gaoji_value" value="none" />
<input type="hidden" name="gwShare_import_value" value="none" />
<input type="hidden" name="gwShare_upimport_value" value="none" />
<input type="hidden" name="isBatch" value="<%=isBatch %>" />

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" align="center">
	<tr>
		<td bgcolor=#999999>
			<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
				<tr><th colspan="4" id="gwShare_thTitle">导 入 查 询</th></tr>
				
				<tr id="gwShare_tr31" bgcolor="#FFFFFF">
					<td align="right" width="15%">提交文件</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
							</iframe>
							<!-- <input type="text" name=gwShare_fileName value=""/> -->
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32">
					<td CLASS="green_foot" align="right">注意事项</td>
					<td colspan="3" CLASS="green_foot">
					1、需要导入的文件格式限于Excel、文本文件，即xls、txt格式 。
					 <br>
					2、文件的第一行为标题行，即【用户账号】、或者【设备序列号】。
					 <br>
					3、文件只有一列。
					 <br>
					4、文件行数不要太多，以免影响性能。
					</td>
				</tr>
				<tr bgcolor="#FFFFFF">
					<td colspan="4" align="right" class="green_foot" width="100%">
					<form action="" method="post">
						<input type="hidden" name="anaUrl" id="anaUrl" value='<s:url value="/itms/resource/QueryDeviceByVoipIpACT.action"/>'/>
						<input type="hidden" name=gwShare_fileName id="gwShare_fileName" value=""/>
						<input id="anaBtn" type="button" class=jianbian name="gwShare_queryButton" value="分析文件" />
					</form>
					</td>
				</tr>
				
			</table>
		</td>
	</tr>
</TABLE>
<IFRAME ID=childFrm height="0" frameborder="0" scrolling="no" width="100%" SRC=""></IFRAME>
