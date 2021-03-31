<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.linkage.litms.LipossGlobals"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></SCRIPT>
<%
	String gwType = request.getParameter("gw_type");
%>
<script language="JavaScript">

var flag = "";
function init(){
	// 初始化属地
	gwShare_change_select("city","00");
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
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("button[@name='button']").attr("disabled", true); 
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/itms/resource/deviceAct!getDeviceStatByOnline.action'/>';
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		gwType:gwType
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false); 
	});
}

function ToExcel(cityId, starttime, endtime, gwType) {
	var page="<s:url value='/itms/resource/deviceAct!getDeviceStatByOnlineToExcel.action'/>?"
		+ "cityId=" + cityId
		+ "&starttime=" + starttime
		+ "&endtime=" + endtime
		+ "&gwType=" + gwType;
	document.all("childFrm").src=page;
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
	
	// 山西联通 分地市只会查到一个属地，默认选中当前属地
	var option = '';
	if (lineData.length == 1) {
        // 如果只有一个属地就不可再选
		
    } else {
      option = "<option value='-1' selected>==请选择==</option>";
    }
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if(selectvalue==xValue || lineData.length == 1){
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
						设备是否在线、确认统计
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						时间为系统注册时间
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
							<%if(gwType.equals("1")){%>
								家庭网关设备在线统计
							<%}else if(gwType.equals("4")){%>
								 机顶盒设备在线统计
							<%}else{%>
								 设备在线统计
							<%}%>
							<input type="hidden" name="gwType" id="gwType" value="<%=gwType %>">
						</th>
						
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align="center" width="15%">
							开始时间
						</td>
						<td width="35%">
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>"  style="width: 225px" >
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
						<td class=column align="center" width="15%">
							结束时间
						</td>
						<td width="35%">
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>" style="width: 225px" >
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<td class="column" align="right" width="15%">
							设备属地
						</td>
						<td width="35%" colspan="3">
							<select name="cityId" class="bk" style="width: 225px" >
								<option value="-1">==请选择==</option>
							</select>
							&nbsp;<font style="color:red">*</font>
						</td>
						
					</TR>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align="right" padding="4px">
							<input type="button" onclick="doQuery()" name="button" value="统 计"/>
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
