<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<%
	String gwType = request.getParameter("gwType");
%>
<script language="JavaScript">
var flag = "";
function init(){
	// 初始化属地
	change_select("city","00");
	gwShare_change_select("vendor","-1");
	initDate();
}
//初始化时间
function initDate()
{
	//初始化时间  开启 by zhangcong 2011-06-02
	theday=new Date();
	day=theday.getDate();
	month=theday.getMonth()+1;
	year=theday.getFullYear();
	hour = theday.getHours();
	mimute = theday.getMinutes();
	second = theday.getSeconds();
	
	flag = '<s:property value="starttime"/>' ;
    if(null!=flag &&""!=flag){
    	$("input[@name='starttime']").val('<s:property value="starttime"/>');
    	$("input[@name='endtime']").val('<s:property value="endtime"/>');
    }else{
    	//开始时间默认为当年的第一天
	    $("input[@name='starttime']").val(year+"-1-1 00:00:00");
	    $("input[@name='endtime']").val(year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second);
    }
}
function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());                // 设备属地
    var starttime = $.trim($("input[@name='starttime']").val());           // 开始时间(受理时间)
    var endtime = $.trim($("input[@name='endtime']").val());              // 结束时间(受理时间)
    var gwType = $("input[@name='gwType']").val();
    var gwShare_vendorId = $.trim($("select[@name='gwShare_vendorId']").val());           //厂商
    var gwShare_deviceModelId = $.trim($("select[@name='gwShare_deviceModelId']").val()); //型号
    var gwShare_devicetypeId = $.trim($("select[@name='gwShare_devicetypeId']").val());   //版本
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("button[@name='button']").attr("disabled", true); 
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/itms/report/busOnceDownSucACTNxdx!queryList.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		gwType:gwType,
		gwShare_vendorId:gwShare_vendorId,
		gwShare_deviceModelId:gwShare_deviceModelId,
		gwShare_devicetypeId:gwShare_devicetypeId
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false); 
	});
}

function countBycityId(cityId,starttime,endtime,gwType,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId) {

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/itms/report/busOnceDownSucACTNxdx!queryList.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		gwShare_vendorId:gwShare_vendorId,
		gwShare_deviceModelId:gwShare_deviceModelId,
		gwShare_devicetypeId:gwShare_devicetypeId,
		gwType:gwType
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}
function ToExcel(cityId,starttime1,endtime1,gwType,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId) {
	var page="<s:url value='/itms/report/busOnceDownSucACTNxdx!getAllResultExcel.action'/>?"
		+ "cityId=" + cityId
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&gwShare_vendorId=" +gwShare_vendorId
		+ "&gwShare_deviceModelId=" +gwShare_deviceModelId
		+ "&gwShare_devicetypeId=" +gwShare_devicetypeId
		+ "&gwType=" + gwType;
	document.all("childFrm").src=page;
}
function openHgw(cityId, starttime1, endtime1,servTypeId,gwType,gwShare_vendorId,gwShare_deviceModelId,gwShare_devicetypeId) {
  
	var page="<s:url value='/itms/report/busOnceDownSucACTNxdx!getServInfoDetail.action'/>?";
	var url = page
			+ "cityId=" + cityId 
			+ "&starttime1=" +starttime1 
			+ "&endtime1=" +endtime1
			+ "&servTypeId="+servTypeId
			+ "&gwShare_vendorId=" +gwShare_vendorId
			+ "&gwShare_deviceModelId=" +gwShare_deviceModelId
			+ "&gwShare_devicetypeId=" +gwShare_devicetypeId
			+ "&gwType=" + gwType;
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
function change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		default:
			alert("未知查询选项！");
			break;
	}	
}

function gwShare_change_select(type,selectvalue){
	switch (type){
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_vendorId']"),selectvalue);
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择设备厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='gwShare_vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='gwShare_deviceModelId']").html("<option value='-1'>==请先选择厂商==</option>");
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='gwShare_deviceModelId']"),selectvalue);
				$("select[@name='gwShare_devicetypeId']").html("<option value='-1'>==请先选择设备型号==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
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
				gwShare_parseMessage(ajax,$("select[@name='gwShare_devicetypeId']"),selectvalue);
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
						业务一次下发成功率统计
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						时间为受理时间
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
							业务一次下发成功率统计
							<input type="hidden" name="gwType" id="gwType" value="<%=gwType %>">
						</th>
						
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align="center" width="15%">
							开始时间
						</td>
						<td width="35%">
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
						<td class=column align="center" width="15%">
							结束时间
						</td>
						<td width="35%">
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" align="right" width="15%">厂    商</TD>
						<TD width="35%">
							<select name="gwShare_vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
								<option value="-1">==请选择==</option>
							</select>
						</TD>
						<td class="column" align="right" width="15%">设备型号</TD>
						<TD align="left" width="35%">
							<select name="gwShare_deviceModelId" class="bk" onchange="gwShare_change_select('devicetype','-1')">
								<option value="-1">请先选择厂商</option>
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" align="right" width="15%">设备版本</TD>
							<TD width="35%">
								<select name="gwShare_devicetypeId" class="bk" onchange="">
									<option value="-1">请先选择设备型号</option>
								</select>
						</TD>
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
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">
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

