<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){
	var serviceId = $.trim($("select[@name='serviceId']").val());
	var device_serialnumber = $.trim($("input[@name='device_serialnumber']").val());
    var userName = $.trim($("input[@name='userName']").val());

	if(device_serialnumber==""&&userName==""){
		alert("至少输入用户账号或设备序列号其中一项！");
		$("input[@name='userName']").focus();
		return false;
	}
	if(0<device_serialnumber.length&&device_serialnumber.length<6){
		alert("至少输入设备序列号后六位！");
		$("input[@name='device_serialnumber']").focus();
		return false;
	}
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/bbms/report/serviceQuery.action'/>'; 
	$.post(url,{
		serviceId:serviceId,
		device_serialnumber:device_serialnumber,
		userName:userName
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(serviceId,device_serialnumber,userName) {
	var page="<s:url value='/bbms/report/serviceQuery!getExcel.action'/>?"
		+ "&serviceId=" + serviceId
		+ "&device_serialnumber=" + device_serialnumber
		+ "&userName=" + userName;
	document.all("childFrm").src=page;
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						业务使用查询
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
						查询设备的业务开启情况
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
							业务使用查询
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							用户账号
						</td>
						<td>
							<input type="text" name="userName" value="" class="bk">
						</td>
						<td class=column width="15%">
							设备序列号
						</td>
						<td>
							<input type="text" name="device_serialnumber" class='bk' value="">
							<font color="red">(至少最后6位)</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							业务种类
						</td>
						<td colspan="3">
							<s:select list="serviceTypeList" name="serviceId" headerKey="-1"
								headerValue="所有业务" listKey="service_id" listValue="service_name"
								value="serviceId" cssClass="bk"></s:select>
						</td>
						
					</tr>
					
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;查 询&nbsp;
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
				正在查询，请稍等....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
