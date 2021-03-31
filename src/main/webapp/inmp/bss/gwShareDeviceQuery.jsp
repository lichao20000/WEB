<%@page import="com.linkage.litms.LipossGlobals"%>
<%@page import="com.linkage.litms.system.*"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>设备查询</title>
<link rel="stylesheet" href="<s:url value="../../css/inmp/css/css_green.css"/>" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/inmp/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/inmp/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/inmp/WdatePicker.js"></script>

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

//验证输入参数的长度是否合法
function do_test()
{
	//获取输入框内容，trim一下
	//var gwShare_queryParam = document.gwShare_selectForm.gwShare_queryParam.value; 
	  var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
		//$.trim($("input[@name='gwShare_queryParam']").val());
		gwShare_queryParam = $.trim(gwShare_queryParam);
	
	//必须输入内容  注释by zhangchy 2011-09-21 
	// 注释原因是 如果是高级查询的话，gwShare_queryParam.length始终为0，所以会一直alert("请输入查询参数！"); 不能进行查询
	// 此段逻辑在下面进行了改写，区分简单查询 和 高级查询 
	//if(0 == gwShare_queryParam.length)
	//{
	//	alert("请输入查询参数！");
	//	//document.gwShare_selectForm.gwShare_queryParam.focus();
	//	$("input[@name='gwShare_queryParam']").focus();
	//	return false;
	//}
	
	//简单查询必须输入内容  add by zhangchy 2011-09-21 -----begin ---------------
	var title = document.getElementById("gwShare_thTitle").innerHTML;
	if(title == "简 单 查 询"){
		if(0 == gwShare_queryParam.length){
			alert("请输入查询参数！");
			//document.gwShare_selectForm.gwShare_queryParam.focus();
			$("input[@name='gwShare_queryParam']").focus();
			return false;
		}	
	}else{  
		if(title == "导 入 查 询" || title == "升 级 导 入 查 询"){
			//alert(123);
			return true;
		}

       // 高级查询必须输入设备SN，支持
		var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
		var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
		var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
		var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
		var gwShare_isBatch = <%=isBatch %>;
		if(parseInt(gwShare_cityId) == -1 && false==<%=allSelect%>){
			alert("至少选择属地！");
			return false;
		}
		if(parseInt(gwShare_vendorId) == -1){
			alert("至少选择厂商！");
			return false;
		}
		if(parseInt(gwShare_deviceModelId) == -1){
			alert("至少选择设备型号！");
			return false;
		}
		if("1"==gwShare_isBatch){
			if(parseInt(gwShare_devicetypeId) == -1){
				alert("至少选择设备版本！");
				return false;
			}
		}
	}
	// --------  add by zhangchy 2011-09-21 ----- end ---------------
	
	//获取选择的类型
	var gwShare_queryFields = document.getElementsByName("gwShare_queryField");
	
	//"设备序列号"被选中
	if(gwShare_queryFields[0].checked)
	{
		if(gwShare_queryParam.length<6&&gwShare_queryParam.length>0){
			alert("请至少输入最后6位设备序列号进行查询！");
			document.gwShare_selectForm.gwShare_queryParam.focus();
			return false;
		}
	}
	//"设备IP"被选中
	else if(gwShare_queryFields[1].checked)
	{
		if(!reg_verify(gwShare_queryParam))
		{
			alert("请输入合法的IP地址！");
			document.gwShare_selectForm.gwShare_queryParam.focus();
			return false;
		}
	}//"用户帐号"被选中   --暂时不检测
	//else if(gwShare_queryFields[2].checked)
	//{
	//	
	//}
	gwShare_spellSQL();
	
	
	//如果没有异常则允许查询
	return true;
}

function do_query(){
	if(!do_test()){
		return;
	}
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
	var url="<s:url value="/inmp/bss/gwDeviceQuery!queryDeviceList.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
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
		url="<s:url value="/inmp/bss/gwDeviceQuery!queryDeviceListBySQL.action"/>?gwShare_matchSQL="+gwShare_matchSQL+"&gwShare_queryResultType="+gwShare_queryResultType;
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
	gwShare_queryChange(queryType);
	
	var str = Array($("input[@name='gwShare_queryParam']"),
					$("div[@id='gwShare_msgdiv']"),
					$("input[@name='gwShare_queryField_temp']"));
	
	//针对现网慢的情况，暂时去掉自适应匹配
	//$.autoMatch3("<s:url value='/gwms/share/gwDeviceQuery!getDeviceSn.action'/>",str,"#");
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
	var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
	if("1"==gwShare_queryType){
		$("input[@name='gwShare_queryField']").get(0).checked = true;
		$("input[@name='gwShare_queryParam']").val("");
	}else if("2"==gwShare_queryType){
		$("select[@name='gwShare_cityId']").attr("value",'-1')
		$("select[@name='gwShare_onlineStatus']").attr("value",'-1')
		$("select[@name='gwShare_vendorId']").attr("value",'-1')
		$("select[@name='gwShare_bindType']").attr("value",'-1')
       	$("input[@name='gwShare_deviceSerialnumber']").val("");
		gwShare_change_select('deviceModel','-1');
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
function gwShare_queryChange(change){
	var gwShare_gaoji_value = $("input[@name='gwShare_gaoji_value']").val();
	var gwShare_import_value = $("input[@name='gwShare_import_value']").val();
	var gwShare_upimport_value = $("input[@name='gwShare_upimport_value']").val();

	if(true==<%=isMatchSQL%>)
	{
		var chbx_isMactchSQL = document.getElementsByName("chbx_isMactchSQL");
		chbx_isMactchSQL[0].checked=false;
		$("font[@id='gwShare_td1']").css("display","");
		$("input[@id='chbx_isMactchSQL']").css("display","");
		$("textarea[@name='gwShare_matchSQL']").val("");
		$("textarea[@name='gwShare_matchSQL1']").val("");
	}
	switch (change){
		case "2":
			$("th[@id='gwShare_thTitle']").html("高 级 查 询");
			$("input[@name='gwShare_queryType']").val("2");
			$("input[@name='gwShare_jiadan']").css("display","");
			$("input[@name='gwShare_gaoji']").css("display","none");
			$("input[@name='gwShare_import']").css("display",gwShare_import_value);
			$("input[@name='gwShare_up_import']").css("display",gwShare_upimport_value);
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","");
			$("tr[@id='gwShare_tr22']").css("display","");
			$("tr[@id='gwShare_tr23']").css("display","");
			$("tr[@id='gwShare_tr24']").css("display","");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("input[@name='gwShare_queryButton']").val(" 查 询 ");
			//加载相关项
			gwShare_change_select("city","-1");
			gwShare_change_select("vendor","-1");
			break;
		case "3":
			$("th[@id='gwShare_thTitle']").html("导 入 查 询");
			$("input[@name='gwShare_queryType']").val("3");
			$("input[@name='gwShare_jiadan']").css("display","");
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display","none");
			$("input[@name='gwShare_up_import']").css("display","none");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr25']").css("display","none");
			$("tr[@id='gwShare_tr26']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("input[@name='gwShare_queryButton']").val("分析文件");
			break;
		case "4":
			$("th[@id='gwShare_thTitle']").html("升 级 导 入 查 询");
			$("input[@name='gwShare_queryType']").val("4");
			$("input[@name='gwShare_jiadan']").css("display","");
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display","none");
			$("input[@name='gwShare_up_import']").css("display","none");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr25']").css("display","none");
			$("tr[@id='gwShare_tr26']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("input[@name='gwShare_queryButton']").val("分析升级文件");
			break;
		case "1":
			$("th[@id='gwShare_thTitle']").html("简 单 查 询");
			$("input[@name='gwShare_queryType']").val("1");
			$("input[@name='gwShare_jiadan']").css("display","none");
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display",gwShare_import_value);
			$("input[@name='gwShare_up_import']").css("display",gwShare_upimport_value);
			$("tr[@id='gwShare_tr11']").css("display","");
			$("tr[@id='gwShare_tr12']").css("display","");
			$("tr[@id='gwShare_tr25']").css("display","none");
			$("tr[@id='gwShare_tr26']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("input[@name='gwShare_queryButton']").val(" 查 询 ");
			break;
		default:
			$("th[@id='gwShare_thTitle']").html("简 单 查 询");
			$("input[@name='gwShare_queryType']").val("1");
			$("input[@name='gwShare_jiadan']").css("display","none");
			$("input[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("input[@name='gwShare_import']").css("display",gwShare_import_value);
			$("input[@name='gwShare_up_import']").css("display",gwShare_upimport_value);
			$("tr[@id='gwShare_tr11']").css("display","");
			$("tr[@id='gwShare_tr12']").css("display","");
			$("tr[@id='gwShare_tr25']").css("display","none");
			$("tr[@id='gwShare_tr26']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("input[@name='gwShare_queryButton']").val(" 查 询 ");
			break;
	}
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
			var url = "<s:url value="/inmp/bss/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/inmp/bss/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/inmp/bss/gwDeviceQuery!getDeviceModel.action"/>";
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
			var url = "<s:url value="/inmp/bss/gwDeviceQuery!getDevicetype.action"/>";
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
	var url = "<s:url value='/inmp/bss/gwDeviceQuery!queryDeviceListSQL.action'/>";
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
</SCRIPT>
<style>
span
{
	position:static;
	border:0;
}
</style>
</head>
<form name="gwShare_selectForm" action="<s:url value="/inmp/bss/gwDeviceQuery!queryDeviceList.action"/>" target="dataForm">
<input type="hidden" name="gwShare_queryType" value="1" />
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
				<tr><th colspan="4" id="gwShare_thTitle">简 单 查 询</th></tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr11" STYLE="display:">
					<td colspan="4" align="center" width="100%">
						<div>
							<input type="input" class="bk" name="gwShare_queryParam" size="60" maxlength="60"/>
							<!-- <label style="CURSOR:hand" onclick="javascript:gwShare_queryChange('2');">高级查询</label> -->
							<br /> 
							<div id="gwShare_msgdiv" STYLE="display:none" > 
									
							</div>
						</div>
					</td>
				</tr>
				<tr bgcolor="#FFFFFF" id="gwShare_tr12" STYLE="display:">
					<td colspan="4" align="center" width="100%">
						<input type="radio" name="gwShare_queryField" value="deviceSn" checked onclick="gwShare_queryField_selected(this)"/> 设备序列号 &nbsp;&nbsp;
						<input type="radio" name="gwShare_queryField" value="deviceIp" onclick="gwShare_queryField_selected(this)"/> 设备IP &nbsp;&nbsp;
						<input type="radio" name="gwShare_queryField" value="username" onclick="gwShare_queryField_selected(this)"/> LOID &nbsp;&nbsp;
						<input type="radio" name="gwShare_queryField" value="kdname" onclick="gwShare_queryField_selected(this)"/> 宽带账号 &nbsp;&nbsp;
						<input type="radio" name="gwShare_queryField" value="voipPhoneNum" onclick="gwShare_queryField_selected(this)"/> VOIP电话号码 &nbsp;&nbsp;
						<!-- <input type="radio" class=jianbian name="gwShare_queryField" value="all" onclick="gwShare_queryField_selected(this)"/> 全 部 &nbsp;&nbsp; -->
					</td>
				</tr>
				<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="display:none">
					<TD align="right" class=column width="15%">属    地</TD>
					<TD align="left" width="35%">
						<select name="gwShare_cityId" class="bk">
							<option value="-1">==全部==</option>
						</select>
					</TD>
					<TD align="right" class=column width="15%">上线状态</TD>
					<TD width="35%">
						<select name="gwShare_onlineStatus" class="bk">
							<option value="-1">==全部==</option>
							<option value="0">下线</option>
							<option value="1">在线</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr22" STYLE="display:none">
					<TD align="right" class=column width="15%">厂    商</TD>
					<TD width="35%">
						<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
							<option value="-1">==请选择==</option>
						</select>
					</TD>
					<TD align="right" class=column width="15%">设备型号</TD>
					<TD align="left" width="35%">
						<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
							<option value="-1">请先选择厂商</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr23"  STYLE="display:none">	
					<TD align="right" class=column width="15%">设备版本</TD>
					<TD width="35%">
						<select name="gwShare_devicetypeId" class="bk"">
							<option value="-1">请先选择设备型号</option>
						</select>
					</TD>	
					<TD align="right" class=column width="15%">是否绑定</TD>
					<TD width="35%">
						<select name="gwShare_bindType" class="bk">
							<option value="-1">==全部==</option>
							<option value="0">未绑定</option>
							<option value="1">已绑定</option>
						</select>
					</TD>
				</TR>
				<TR bgcolor="#FFFFFF" id="gwShare_tr24"  STYLE="display:none">
					<TD align="right" class=column width="15%">设备序列号</TD>
					<TD width="35%">
						<input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="25" class="bk"/>
						<font color="red">*可模糊匹配</font>
					</TD>
					<TD align="right" class=column width="15%" ><font id="gwShare_td1"  STYLE="display:none">自定义查询</font></TD>
					<TD width="35%">
						<input type="checkbox" name="chbx_isMactchSQL" id="chbx_isMactchSQL"  STYLE="display:none" onclick="spellSQL(this);">
					</TD>
				</TR>
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
				<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:none">
					<td align="right" width="15%">提交文件</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/inmp/bss/FileUpload.jsp"/>" height="20" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32" style="display:none">
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
						<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
						onclick="javascript:gwShare_queryChange('1');" name="gwShare_jiadan" value="简单查询" />
						<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
						onclick="javascript:gwShare_queryChange('3');" name="gwShare_import" value="导入查询" />
						
						<input type="button" class=jianbian style="CURSOR:hand" style="display: none" 
						onclick="javascript:gwShare_queryChange('4');" name="gwShare_up_import" value="升级导入查询" />
						
						<input type="button" onclick="javascript:do_query()" class=jianbian 
						name="gwShare_queryButton" value=" 查 询 " />
						<input type="button" class=jianbian onclick="javascript:gwShare_revalue()" 
						name="gwShare_reButto" value=" 重 置 " />
					</td>
				</tr>
			</table>
		</td>
	</tr>
</TABLE>
</form>