<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
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
	var url="<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceListNew.action"/>?gwShare_queryResultType="+gwShare_queryResultType;
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    url = url + "&gwShare_queryType=" + gwShare_queryType;
    if("1"==gwShare_queryType){
		var gwShare_queryField = $("input[@name='gwShare_queryField'][@checked]").val();
		var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
		if(trim(gwShare_queryParam)==""){
			alert("请输入查询条件！");
			return;
		}
		url = url + "&gwShare_queryField=" + gwShare_queryField;
		url = url + "&gwShare_queryParam=" + gwShare_queryParam;
	}else if("2"==gwShare_queryType){
	 	var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
	 	var gwShare_nextCityId = $.trim($("select[@name='gwShare_nextCityId']").val());
        var gwShare_onlineStatus = $("select[@name='gwShare_onlineStatus']").val();
        var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
        var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
        var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
        var gwShare_bindType = $("select[@name='gwShare_bindType']").val();
        var gwShare_deviceSerialnumber = $("input[@name='gwShare_deviceSerialnumber']").val();
        var gwShare_matchSQL = $.trim($("textarea[@name='gwShare_matchSQL1']").val()) ;
        var gwShare_platform = $("select[@name='gwShare_platform']").val();
        gwShare_matchSQL = gwShare_matchSQL.replace("%", "%25").replace(new RegExp("'", 'g'), "[");
        url = url + "&gwShare_matchSQL=" + gwShare_matchSQL;
        url = url + "&gwShare_cityId=" + gwShare_cityId;
        url = url + "&gwShare_nextCityId=" + gwShare_nextCityId;
        url = url + "&gwShare_onlineStatus=" + gwShare_onlineStatus;
        url = url + "&gwShare_vendorId=" + gwShare_vendorId;
        url = url + "&gwShare_deviceModelId=" + gwShare_deviceModelId;
        url = url + "&gwShare_devicetypeId=" + gwShare_devicetypeId;
        url = url + "&gwShare_bindType=" + gwShare_bindType;
        url = url + "&gwShare_deviceSerialnumber=" + gwShare_deviceSerialnumber;
        url = url + "&gwShare_platform=" + gwShare_platform;
	}else {
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if(""==gwShare_fileName){
			alert("请先上传文件！");
			return;
		}
		url = url + "&gwShare_fileName=" + gwShare_fileName;
	}
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
	gwShare_queryChange(queryType);
	
	var str = Array($("input[@name='gwShare_queryParam']"),
					$("div[@id='gwShare_msgdiv']"),
					$("input[@name='gwShare_queryField_temp']"));
	
	//针对现网慢的情况，暂时去掉自适应匹配
	//$.autoMatch3("<s:url value='/gtms/stb/share/shareDeviceQuery!getDeviceSn.action'/>",str,"#");
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

function gwShare_queryChange(change){
	var gwShare_gaoji_value = $("input[@name='gwShare_gaoji_value']").val();
	switch (change){
		case "2":
			$("td[@id='gwShare_thTitle']").html("高 级 查 询");
			$("input[@name='gwShare_queryType']").val("2");
			$("button[@name='gwShare_jiadan']").css("display","");
			$("button[@name='gwShare_gaoji']").css("display","none");
			$("button[@name='gwShare_import']").css("display","");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","");
			$("tr[@id='gwShare_tr22']").css("display","");
			$("tr[@id='gwShare_tr23']").css("display","");
			$("tr[@id='gwShare_tr24']").css("display","");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("button[@name='gwShare_queryButton']").val(" 查 询 ");
			//加载相关项
			gwShare_change_select("city","-1");
			gwShare_change_select("vendor","-1");
			break;
		case "3":
			$("td[@id='gwShare_thTitle']").html("导 入 查 询");
			$("input[@name='gwShare_queryType']").val("3");
			$("button[@name='gwShare_jiadan']").css("display","");
			$("button[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("button[@name='gwShare_import']").css("display","none");
			$("tr[@id='gwShare_tr11']").css("display","none");
			$("tr[@id='gwShare_tr12']").css("display","none");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","");
			$("tr[@id='gwShare_tr32']").css("display","");
			$("button[@name='gwShare_queryButton']").val("分析文件");
			break;
		case "1":
			$("td[@id='gwShare_thTitle']").html("简 单 查 询");
			$("input[@name='gwShare_queryType']").val("1");
			$("button[@name='gwShare_jiadan']").css("display","none");
			$("button[@name='gwShare_gaoji']").css("display",gwShare_gaoji_value);
			$("button[@name='gwShare_import']").css("display","");
			$("tr[@id='gwShare_tr11']").css("display","");
			$("tr[@id='gwShare_tr12']").css("display","");
			$("tr[@id='gwShare_tr21']").css("display","none");
			$("tr[@id='gwShare_tr22']").css("display","none");
			$("tr[@id='gwShare_tr23']").css("display","none");
			$("tr[@id='gwShare_tr24']").css("display","none");
			$("tr[@id='gwShare_tr31']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");
			$("button[@name='gwShare_queryButton']").val(" 查 询 ");
			break;
		default:
			break;
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
function gwShare_setImport(){
	$("input[@name='gwShare_import_value']").val("");
	$("input[@name='gwShare_import']").css("display","");
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
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
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
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChildCore.action"/>";
			$.post(url,{
				gwShare_cityId:city_id
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_nextCityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!getVendor.action'/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!getDeviceModel.action'/>";
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
			var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!getDevicetype.action'/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(type,ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
			});
			break;	
		default:
			alert("未知查询选项！");
			break;
	}	
}

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

function do_query(){
	$("div[@id='QueryData']").html("");
	if(!do_test()){
		return;
	}
	setTimeout("gwShare_queryDevice()", 2000);
}

//验证输入参数的长度是否合法
function do_test()
{
	var gwShare_queryParam = $("input[@name='gwShare_queryParam']").val();
	gwShare_queryParam = $.trim(gwShare_queryParam);
	
	var title = document.getElementById("gwShare_thTitle").innerHTML;
	if(title == "简 单 查 询"){
		if(0 == gwShare_queryParam.length){
			alert("请输入查询参数！");
			$("input[@name='gwShare_queryParam']").focus();
			return false;
		}	
	}else{
		if(title == "导 入 查 询" || title == "升 级 导 入 查 询"){
			return true;
		}

		// 高级查询必须输入设备SN，支持
		var gwShare_cityId = $("select[@name='gwShare_cityId']").val();
		var gwShare_vendorId = $("select[@name='gwShare_vendorId']").val();
		var gwShare_deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
		var gwShare_devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
		var isShowGaoJi=$("input[@name='isShowGJ']").val();
		if("1"!=isShowGaoJi	&& parseInt(gwShare_cityId)==-1){
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
		gwShare_spellSQL();
	}
	
	var gwShare_queryFields = document.getElementsByName("gwShare_queryField");
	
	
	if(title == "简 单 查 询"){
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
		else if(gwShare_queryFields[2].checked)
		{
			if(!reg_verify(gwShare_queryParam))
			{
				alert("请输入合法的IP地址！");
				document.gwShare_selectForm.gwShare_queryParam.focus();
				return false;
			}
		}
	}
	return true;
}


function reg_verify(addr)
{
	//正则表达式
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;

    if(addr.match(reg)){
    	//IP地址正确
        return true;
    }else{
    	//IP地址校验失败
         return false;
    }
}

function gwShare_spellSQL(){
	var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!queryDeviceListSQL.action'/>";
	var gwShare_queryResultType = $("input[@name='gwShare_queryResultType']").val();
    var gwShare_queryType = $("input[@name='gwShare_queryType']").val();
    var gwShare_cityId = $.trim($("select[@name='gwShare_cityId']").val());
   
	
	var gwShare_nextCityId = $.trim($("select[@name='gwShare_nextCityId']").val());
    var gwShare_onlineStatus = $.trim($("select[@name='gwShare_onlineStatus']").val());
    var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());
    var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val());
    var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());
    var gwShare_bindType = $.trim($("select[@name='gwShare_bindType']").val());
    var gwShare_deviceSerialnumber = $.trim($("input[@name='gwShare_deviceSerialnumber']").val());
    var gwShare_OrderByUpdateDate = $("input[@name='gwShare_OrderByUpdateDate']").val();
  
    var refresh= new Date().getTime();
    var gw_type= 4;
  
    var iscqsoft1 = ""; 
     try {
    	if(true==iscqsoft){
        	gwShare_cityId = cqSoftCitys;
        	iscqsoft1 = "true";
    	}
    }
    catch (e) {
    } 
    $.post(url,{
		gwShare_queryResultType:gwShare_queryResultType,
		gwShare_queryType:gwShare_queryType,
		gwShare_cityId:gwShare_cityId,
		gwShare_nextCityId:gwShare_nextCityId,
		gwShare_onlineStatus:gwShare_onlineStatus,
		gwShare_vendorId:gwShare_vendorId,
		gwShare_deviceModelId:gwShare_deviceModelId,
		gwShare_devicetypeId:gwShare_devicetypeId,
		gwShare_bindType:gwShare_bindType,
		gwShare_deviceSerialnumber:gwShare_deviceSerialnumber,
		refresh:refresh,
		gw_type:gw_type,
		iscqsoft:iscqsoft1,
		gwShare_OrderByUpdateDate:gwShare_OrderByUpdateDate
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
<form name="gwShare_selectForm" action="<s:url value="/gtms/stb/share/shareDeviceQuery!queryDeviceList.action"/>" target="dataForm">
<input type="hidden" name="gwShare_queryType" value="1" />
<input type="hidden" name="gwShare_queryResultType" value="checkbox" />
<input type="hidden" name="gwShare_queryField_temp" value="deviceSn" />
<input type="hidden" name="gwShare_gaoji_value" value="none" />
<input type="hidden" name="gwShare_import_value" value="none" />
<input type="hidden" name="gwShare_OrderByUpdateDate" value="0" /><!-- 1开启记录按照上报时间排序，0不开启 -->
<input type="hidden" name="gwShare_RecordNum" value="0" /><!-- 默认3条记录，0为查询全部，建议配置大小不超过50 -->
<input type="hidden" name="isShowGJ" value="0" />

<TABLE width="100%" class="querytable" align="center">
	<tr><td colspan="4" class="title_1" id="gwShare_thTitle">简 单 查 询</td></tr>
	<tr id="gwShare_tr11" STYLE="display:">
		<td colspan="4" align="center" width="100%">
			<div align="center">
				<input type="input" class="bk" name="gwShare_queryParam" size="60" maxlength="60"/>
				<br /> 
				<div id="gwShare_msgdiv" STYLE="display:none" > </div>
			</div>
		</td>
	</tr>
	<tr id="gwShare_tr12" STYLE="display:">
		<td colspan="4" align="center" width="100%">
			<div align="center">
				<input type="radio" class=jianbian name="gwShare_queryField" value="deviceSn" checked onclick="gwShare_queryField_selected(this)"/> 设备序列号 &nbsp;&nbsp;
				<input type="radio" class=jianbian name="gwShare_queryField" value="custaccount" onclick="gwShare_queryField_selected(this)"/> 业务账号 &nbsp;&nbsp;
				<input type="radio" class=jianbian name="gwShare_queryField" value="deviceIp" onclick="gwShare_queryField_selected(this)"/> 设备IP &nbsp;&nbsp;
			</div>
		</td>
	</tr>
	<TR id="gwShare_tr21" STYLE="display:none">
		<TD align="right" class="title_2" width="15%">属    地</TD>
		<TD align="left" width="35%">
		<div id="commonCity" style="display: ">
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
		</div>
		</TD>
		<ms:inArea areaCode="hn_lt" notInMode="true">
		<TD align="right" class="title_2" width="15%">上线状态</TD>
		<TD width="35%">
			<select name="gwShare_onlineStatus" class="bk">
				<option value="-1">==请选择==</option>
				<option value="0">下线</option>
				<option value="1">在线</option>
			</select>
		</TD>
		</ms:inArea>
		<ms:inArea areaCode="hn_lt" notInMode="false">
		<TD align="right" class="title_2" width="15%">设备序列号</TD>
		<TD width="35%">
			<input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="40" class="bk"/>
			<font color="red">可模糊匹配</font>
		</TD>
		</ms:inArea>
	</TR>
	<TR id="gwShare_tr22" STYLE="display:none">
		<TD align="right" class="title_2" width="15%">厂    商</TD>
		<TD width="35%">
			<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
				<option value="-1">==请选择==</option>
			</select>
		</TD>
		<TD align="right" class="title_2" width="15%">设备型号</TD>
		<TD align="left" width="35%">
			<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
				<option value="-1">请先选择厂商</option>
			</select>
		</TD>
	</TR>
	<TR id="gwShare_tr23"  STYLE="display:none">	
		<TD align="right" class="title_2" width="15%">设备版本</TD>
		<TD width="35%">
			<select name="gwShare_devicetypeId" class="bk"">
				<option value="-1">请先选择设备型号</option>
			</select>
		</TD>	
		<TD align="right" class="title_2" width="15%">是否绑定</TD>
		<TD width="35%">
			<select name="gwShare_bindType" class="bk">
				<option value="-1">==请选择==</option>
				<option value="0">未绑定</option>
				<option value="1">已绑定</option>
			</select>
		</TD>
	</TR>
	<ms:inArea areaCode="hn_lt" notInMode="true">
	<TR id="gwShare_tr24"  STYLE="display:none">
		<TD align="right" class="title_2" width="15%">设备序列号</TD>
		<TD width="35%">
			<input type="text" name="gwShare_deviceSerialnumber" value="" size="25" maxlength="40" class="bk"/>
			<font color="red">可模糊匹配</font>
		</TD>
		<TD align="right" class="title_2" width="15%"></TD>
		<TD width="35%"></TD>
	</TR>
	<TR bgcolor="#FFFFFF" id="gwShare_tr25"  STYLE="display:none">
					<TD align="right" class=column width="15%">已生成的SQL</TD>
					<TD width="35%" colspan="3">
					    <textarea name="gwShare_matchSQL1" cols="100" rows="4" class=bk readonly="readonly">
					    </textarea>
						<font color="red">*已生成的SQL</font>
					</TD>
				</TR>
	</ms:inArea>
	<tr id="gwShare_tr31" bgcolor="#FFFFFF"  style="display:none">
		<td align="right" width="15%">提交文件</td>
		<td colspan="3" width="85%">
			<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="<s:url value="/gwms/share/FileUpload.jsp"/>" height="20" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
		</td>
	</tr>
	<!--  height="30" -->
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
		<br>
		5、文件行数不要超过10000行，最多只解析前10000行。
		</td>
	</tr>
	<tr >
		<td colspan="4" align="right" class="foot" width="100%">
			<div align="right">
				<button onclick="javascript:do_query();" 
				name="gwShare_queryButton" style="CURSOR:hand" style="display:" > 查 询 </button>
				<button onclick="javascript:gwShare_queryChange('1');" 
				name="gwShare_jiadan" style="CURSOR:hand" style="display: none" >简单查询</button>
				<button onclick="javascript:gwShare_queryChange('2');" 
				name="gwShare_gaoji" style="CURSOR:hand" style="display: none" >高级查询</button>
				<button onclick="javascript:gwShare_queryChange('3');" 
				name="gwShare_import" style="CURSOR:hand" style="display: " >导入查询</button>
						
				<button onclick="javascript:gwShare_revalue();" 
				name="gwShare_reButto" style="CURSOR:hand" style="display: " > 重 置 </button>
			</div>
		</td>
	</tr>
</TABLE>
</form>