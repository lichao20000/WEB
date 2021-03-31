<%--
FileName	: selectDevice.jsp
Date		: 2009年2月2日
Desc		: 选择设备.
--%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/inmp/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

//查询设备的方式转换
function deviceSelect_ShowDialog(param)
{
	
	//根据设备版本来查询
	if(param==0)
	{
		this.tr11.style.display="";
		this.tr12.style.display="";
		this.tr13.style.display="";
		this.tr14.style.display="";
		this.tr15.style.display="none";  // VOIP电话号码
		this.tr16.style.display="none";  // iTV帐号
		this.tr17.style.display="none";  // 宽带主帐号
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//根据用户来查询
	if(param==1)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP电话号码
		this.tr16.style.display="none";  // iTV帐号
		this.tr17.style.display="none";  // 宽带主帐号
		this.tr21.style.display="";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//根据设备序列号来查询
	if(param==2)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP电话号码
		this.tr16.style.display="none";  // iTV帐号
		this.tr17.style.display="none";  // 宽带主帐号
		this.tr21.style.display="none";
		this.tr31.style.display="";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//根据文件导入来查询来查询
	if(param==3)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP电话号码
		this.tr16.style.display="none";  // iTV帐号
		this.tr17.style.display="none";  // 宽带主帐号
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="";
		this.tr42.style.display="";
	}
	
	//根据VOIP电话号码来查询来查询  add by zhangchy 2012-02-21 
	if(param==4)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="";      // VOIP电话号码
		this.tr16.style.display="none";  // iTV帐号
		this.tr17.style.display="none";  // 宽带主帐号
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//根据iTV帐号来查询来查询  add by zhangchy 2012-02-29
	if(param==5)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP电话号码
		this.tr16.style.display="";      // iTV帐号
		this.tr17.style.display="none";  // 宽带主帐号
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
	
	//根据宽带主帐号来查询来查询 add by zhangchy 2012-02-29
	if(param==6)
	{
		this.tr11.style.display="none";
		this.tr12.style.display="none";
		this.tr13.style.display="none";
		this.tr14.style.display="none";
		this.tr15.style.display="none";  // VOIP电话号码
		this.tr16.style.display="none";  // iTV帐号
		this.tr17.style.display="";      // 宽带主帐号
		this.tr21.style.display="none";
		this.tr31.style.display="none";
		this.tr41.style.display="none";
		this.tr42.style.display="none";
	}
}

//厂商、型号级联
function deviceSelect_change_select(type){
	
	var city_id = $("select[@name='city_id']").val();
	var vendor_id = $("select[@name='vendor_id']").val();
	var device_model_id = $("select[@name='device_model_id']").val();
	var devicetype_id = $("select[@name='devicetype_id']").val();
	
	//根据厂商查询设备型号，设备版本重置
	if("vendor"==type){

		var url = "selectDeviceTag!getDeviceModel.action";
		$.post(url,{
			vendor_id:vendor_id
		},function(ajax){
			deviceSelect_parseMessageVendor(ajax);
		});
	}
	
	//根据型号查询设备版本
	if("model"==type){
		var url = "selectDeviceTag!getVersion.action";
		$.post(url,{
			vendor_id:vendor_id,
			device_model_id:device_model_id
		},function(ajax){
			deviceSelect_parseMessageModel(ajax);
		});
		
	}
	
}

//解析查询设备型号返回值的方法
function deviceSelect_parseMessageVendor(ajax){
	
	var select_model = $("select[@name='device_model_id']");
	var select_DeviceType = $("select[@name='devicetype_id']");
	select_model.html("");
	select_DeviceType.html("");
	
	var option = "<option value='-1'>==请选择==</option>";
	try{
		select_model.append(option);
	}catch(e){
		alert("设备型号检索失败！");
		return false;
	}
	
	var option = "<option value='-1'>==请先选择设备型号==</option>";
	try{
		select_DeviceType.append(option);
	}catch(e){
		alert("设备型号检索失败！");
		return false;
	}
	
	if(""==ajax){
		select_DeviceType.attr("value","-1");
		return;
	}
	
	var lineModel = ajax.split("#");
	
	if(!typeof(lineModel) || !typeof(lineModel.length)){
		return false;
	}
	
	for(var i=0;i<lineModel.length;i++){
		var oneElement = lineModel[i].split(",");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		//根据每组value和text标记的值创建一个option对象
		option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		try{
			select_model.append(option);
		}catch(e){
			alert("设备型号检索失败！");
		}
	}
	
	select_model.attr("value","-1");
}

//根据设备型号查询设备版本
function deviceSelect_parseMessageModel(ajax){

	var select_DeviceType = $("select[@name='devicetype_id']");
	select_DeviceType.html("");
	
	var option = "<option value='-1'>==请选择==</option>";
	try{
		select_DeviceType.append(option);
	}catch(e){
		alert("设备版本检索失败！");
		return false;
	}

	if(""==ajax){
		select_DeviceType.attr("value","-1");
		return;
	}
	
	var lineDeviceType = ajax.split("#");
	
	if(!typeof(lineDeviceType) || !typeof(lineDeviceType.length)){
		return false;
		
	}
	
	for(var i=0;i<lineDeviceType.length;i++){
		var oneElement = lineDeviceType[i].split(",");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		//根据每组value和text标记的值创建一个option对象
		option = "<option value='"+xValue+"'>=="+xText+"==</option>";
		try{
			select_DeviceType.append(option);
		}catch(e){
			alert("设备版本检索失败！");
		}
	}
	select_DeviceType.attr("value","-1");
}

//根据用户账号查询
function deviceSelect_relateDeviceByUsername(){
	
	var select_type = "1";
	
	var hguser = $("input[@name='hguser']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();

	if(""==hguser || null==hguser){
		alert("请输入完整的用户账号");
		$("input[@name='hguser']")[0].focus();
		return false;
	}

	var url = "selectDeviceTag!getDeviceByUsername.action";
	$.post(url,{
		select_type:select_type,
		hguser:hguser,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}


/**
 * 根据VOIP电话号码查询
 */
function deviceSelect_relateDeviceByVOIPNum(){
	
	var select_type = "3";
	
	var voipPara = $("input[@name='voipPara']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();

	if(""==voipPara || null==voipPara){
		alert("请输入VOIP电话号码！");
		$("input[@name='voipPara']")[0].focus();
		return false;
	}

	var url = "selectDeviceTag!getDeviceByVoipTelNum.action";
	$.post(url,{
		select_type:select_type,
		voipPara:voipPara,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}



/**
 * 按iTV
 * 需求单：JSDX_ITMS-REQ-20120222-LUHJ-001
 */
function deviceSelect_relateDeviceByItvUserName(){
	
	var select_type = "5";
	
	var iTVUserName = $("input[@name='iTVUserName']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();

	if(""==iTVUserName || null==iTVUserName){
		alert("请输入iTV帐号！");
		$("input[@name='iTVUserName']")[0].focus();
		return false;
	}

	var url = "selectDeviceTag!getDeviceByITVUserName.action";
	$.post(url,{
		select_type:select_type,
		itvUserName:iTVUserName,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}



/**
 * 按宽带主帐号
 * 需求单：JSDX_ITMS-REQ-20120222-LUHJ-001
 */
function deviceSelect_relateDeviceByWideNet(){
	
	var select_type = "6";
	
	var wideNetPara = $("input[@name='wideNetPara']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();

	if(""==wideNetPara || null==wideNetPara){
		alert("请输入宽带帐号！");
		$("input[@name='wideNetPara']")[0].focus();
		return false;
	}

	var url = "selectDeviceTag!getDeviceByWideAccount.action";
	$.post(url,{
		select_type:select_type,
		wideNetPara:wideNetPara,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}

//根据设备序列号和IP查询
function deviceSelect_relateDeviceBySerialno(){
	
	var select_type = "2";
	
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var device_serialnumber = $("input[@name='device_serialnumber']").val();
	var loopback_ip = $("input[@name='loopback_ip_']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();
	
	if(device_serialnumber.length>0 && device_serialnumber.length<6){
		alert("请至少输入六位设备序列号！");
		$("input[@name='device_serialnumber']").focus();
		return false;
	}
	
	if(""==device_serialnumber || null==device_serialnumber){
		if(""==loopback_ip || null==loopback_ip){
			alert("请至少输入一项查询条件！");
			$("input[@name='device_serialnumber']").focus();
			return false;
		}
	}else{
		device_serialnumber = $.trim(device_serialnumber);
	}

	var url = "selectDeviceTag!getDeviceBySerialno.action";
	$.post(url,{
		select_type:select_type,
		device_serialnumber:device_serialnumber,
		loopback_ip:loopback_ip,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}

//高级查询：根据属地、厂商等查询
function deviceSelect_relateDeviceBySenior(){

	var select_type = "0";
	
	if(!deviceSelect_filterselected()){
		alert("请填写相关查询条件！");
		return false;
	}	
	
	var city_id = $("select[@name='city_id']").val();
	var office_id = $("select[@name='office_id']").val();
	var vendor_id = $("select[@name='vendor_id']").val();
	var device_model_id = $("select[@name='device_model_id']").val();
	var devicetype_id = $("select[@name='devicetype_id']").val();
	var loopback_ip = $("input[@name='loopback_ip']").val();
	var online_status = $("select[@name='online_status']").val();
	var selectType = $("input[@name='selectType']").val();
	var jsFunctionName = $("input[@name='jsFunctionName']").val();
	var jsFunctionNameBySn = $("input[@name='jsFunctionNameBySn']").val();
	var listControl = $("input[@name='listControl']").val();

	var url = "selectDeviceTag!getDeviceByCity.action";
	$.post(url,{
		city_id:city_id,
		office_id:office_id,
		select_type:select_type,
		vendor_id:vendor_id,
		device_model_id:device_model_id,
		devicetype_id:devicetype_id,
		loopback_ip:loopback_ip,
		online_status:online_status,
		selectType:selectType,
		jsFunctionName:jsFunctionName,
		jsFunctionNameBySn:jsFunctionNameBySn,
		listControl:listControl,
		gw_type:gw_type
	},function(ajax){
		$("div[@id='div_device']").html("");
		$("div[@id='div_device']").append(ajax);
	});
}

//判断有没有设备被选中
//有:返回true
//无:返回false
function deviceSelectedCheck(){
	
	var oSelect = document.all("device_id");
	
	if(oSelect !=null ) {
		for(var i=0; i<oSelect.length; i++) {
			if(oSelect[i].checked) {
				return true;
			}
		}
		if(oSelect.checked){
			return true;
		}
		return false;
	}else{
		return false;
	}
}

//全选按钮
function deviceSelect_selectAll(elmID){
	
	t_obj = document.all(elmID);
	if(!t_obj) return;
	obj = event.srcElement;
	if(obj.checked){
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = true;
				}
			} else {
				t_obj.checked = true;
			}
		}
	}else{
		if(typeof(t_obj) == "object" ) {
			if(typeof(t_obj.length) != "undefined") {
				for(var i=0; i<t_obj.length; i++){
					t_obj[i].checked = false;
				}
			} else {
				t_obj.checked = false;
			}
		}
	}
}

//在高级查询时，判断是否所有的查询条件都没查询
function deviceSelect_filterselected(){
	var city_id = $("select[@name='city_id']").val();
	if(""!=city_id && null!=city_id && "-1"!=city_id){
		return true;
	}
	var vendor_id = $("select[@name='vendor_id']").val();
	if(""!=vendor_id && null!=vendor_id && "-1"!=vendor_id){
		return true;
	}
	var loopback_ip = $("input[@name='loopback_ip']").val();
	if(""!=loopback_ip && null!=loopback_ip){
		return true;
	}
	var online_status = $("select[@name='online_status']").val();
	if(""!=online_status && null!=online_status && "-1"!=online_status){
		return true;
	}
	return false;
}

//加载URL
var deviceSelect_url = "selectDeviceTag!reload.action";

var selectType='<%=request.getParameter("selectType")%>';
var jsFunctionName='<%=request.getParameter("jsFunctionName")%>';
var jsFunctionNameBySn='<%=request.getParameter("jsFunctionNameBySn")%>';
var maxFileNum='<%=request.getParameter("maxFileNum")%>';

var byVoipTelNum = '<%=request.getParameter("byVoipTelNum")%>';  //  按VOIP电话号码查询 add by zhangchy 2012-02-21
var byItv = '<%=request.getParameter("byItv")%>';                //  按iTV帐号查询 add by zhangchy 2012-02-29
var byWideNet = '<%=request.getParameter("byWideNet")%>';        //  按宽带主帐号查询 add by zhangchy 2012-02-29
var byDeviceno='<%=request.getParameter("byDeviceno")%>';
var buUsername='<%=request.getParameter("buUsername")%>';
var byCity='<%=request.getParameter("byCity")%>';
var byImport='<%=request.getParameter("byImport")%>';
var listControl='<%=request.getParameter("listControl")%>';
var div_device_height = '<%=request.getParameter("div_device_height")%>';
var gw_type = '<%=request.getParameter("gw_type")%>';
//alert(gw_type+'js');
//作为首次进入加载
$.post(deviceSelect_url,{
	selectType:selectType,
	jsFunctionName:jsFunctionName,
	jsFunctionNameBySn:jsFunctionNameBySn,
	maxFileNum:maxFileNum,
	byDeviceno:byDeviceno,
	buUsername:buUsername,
	byVoipTelNum:byVoipTelNum,  // add by zhangchy 2012-02-21
	byCity:byCity,
	byImport:byImport,
	listControl:listControl
},function(ajax){
	
	$("div[@id='selectDevice']").html("");
	$("div[@id='selectDevice']").append(ajax);

	if("null"!=div_device_height && ""!=div_device_height){
		$("div[@id='div_device']").height(div_device_height);
	}
	
});


</SCRIPT>