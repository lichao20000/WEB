<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript">

function init(){
	// 初始化厂家
	gwShare_change_select("city","00");
	gwShare_change_select("vendor","-1");
}


function doQuery(){
	var vendorId = $.trim($("select[@name='vendorId']").val());            // 设备厂商ID
	var deviceModelId = $.trim($("select[@name='deviceModelId']").val());  // 设备型号ID
	var deviceTypeId = $.trim($("select[@name='deviceTypeId']").val());    // 设备版本ID
	var cityId = $.trim($("select[@name='cityId']").val());                // 设备属地
	var accessType = $.trim($("select[@name='accessType']").val());        // 接入方式
	var usertype = $.trim($("select[@name='usertype']").val());            // 终端类型
    var starttime = $.trim($("input[@name='starttime']").val());           // 开始时间(注册时间)
    var endtime = $.trim($("input[@name='endtime']").val());               // 结束时间(注册时间)
    
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/gwms/report/deviceBindWay!countAll.action'/>'; 
	$.post(url,{
		vendorId:vendorId,
		deviceModelId:deviceModelId,
		deviceTypeId:deviceTypeId,
		cityId:cityId,
		accessType:accessType,
		usertype:usertype,
		starttime:starttime,
		endtime:endtime
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function countBycityId(cityId,starttime,endtime,vendorId,deviceModelId,deviceTypeId,accessType,usertype){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/gwms/report/deviceBindWay!countAll.action'/>'; 
	$.post(url,{
		cityId:cityId,
		usertype:usertype,
		starttime:starttime,
		endtime:endtime,
		vendorId:vendorId,
		deviceModelId:deviceModelId,
		deviceTypeId:deviceTypeId,
		accessType:accessType
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(cityId,starttime1,endtime1,vendorId,deviceModelId,deviceTypeId,accessType,usertype) {
	var page="<s:url value='/gwms/report/deviceBindWay!getAllBindWayExcel.action'/>?"
		+ "cityId=" + cityId
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&vendorId=" + vendorId
		+ "&deviceModelId=" + deviceModelId
		+ "&deviceTypeId=" + deviceTypeId
		+ "&accessType=" + accessType
		+ "&usertype=" + usertype;
	document.all("childFrm").src=page;
}


function openHgw(flag, cityId, starttime1, endtime1, vendorId, deviceModelId, deviceTypeId, accessType, usertype){
	
	var page = "";
	
	if(2 == flag){
		page="<s:url value='/gwms/report/deviceBindWay!getUserList.action'/>?"
	}else{
		page="<s:url value='/gwms/report/deviceBindWay!getBindDeviceList.action'/>?"
	}
	var url = page
			+ "cityId=" + cityId 
			+ "&starttime1=" +starttime1
			+ "&endtime1=" +endtime1
			+ "&vendorId=" +vendorId
			+ "&deviceModelId=" +deviceModelId
			+ "&deviceTypeId=" +deviceTypeId
			+ "&accessType=" +accessType
			+ "&usertype=" +usertype
			+ "&flag=" +flag;
	window.open(url,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
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
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='deviceTypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceTypeId']"),selectvalue);
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





</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						终端绑定情况统计
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						注：以下时间为设备注册时间，自动绑定用户=设备序列号自动绑定+桥接帐号自动绑定+逻辑SN自动绑定+路由帐号自动绑定，绑定率=所有绑定的用户/注册终端数
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							终端绑定情况统计
						</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">
							设备厂商
						</TD>
						<TD align="left" width="35%">
                               <select name="vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
								<option value="-1">==请选择==</option>
							</select>
						</TD>
						<TD align="right" class=column width="15%">
							设备型号
						</TD>
						<TD width="35%">
                               <select name="deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
								<option value="-1">请先选择厂商</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" width='15%' align="right">
							软件版本
						</td>
                      	<td width='35%' align="left">
                         	<select name="deviceTypeId" class="bk"">
								<option value="-1">请先选择设备型号</option>
							</select>
                    	</td>
						<td class="column" align="right" width="15%">
							设备属地
						</td>
						<td width="35%">
							<select name="cityId" class="bk">
								<option value="-1">==请选择==</option>
							</select>
							&nbsp;<font style="color:red">*</font>
						</td>
					</TR>
					<tr bgcolor=#ffffff>
						<TD class="column" align="right" width="15%">接入方式</TD>
						<TD width="35%">
						    <SELECT name="accessType" class=bk>
								<option value="1">ADSL接入</option>
								<option value="2">LAN接入</option>
								<option value="3">光纤接入</option>
							</SELECT>
						</TD>
						<td class="column" width="15%" align="right">
							终端类型
						</td>
						<td width="35%">
							<SELECT name="usertype">
								<option selected value="2">E8-C</option>
								<option value="1">E8-B</option>
							</SELECT>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;统 计&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				正在统计，请稍等....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
	init();
</SCRIPT>

