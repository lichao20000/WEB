<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

$(function() {
	Init();
});

function Init() {
	gwShare_change_select("vendor", "-1");
}

function gwShare_change_select(type, selectvalue) {
	switch (type) {
	case "vendor":
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
		$.post(url, {}, function(ajax) {
			gwShare_parseMessage(ajax, $("select[@name='vendor']"),
					selectvalue);
		});
		break;
	case "deviceModel":
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
		var vendorId = $("select[@name='vendor']").val();
		$("select[@name='devicetype']").html(
				"<option value='-1'>==����ѡ���豸�ͺ�==</option>");
		if ("-1" == vendorId) {
			$("select[@name='devicemodel']").html(
					"<option value='-1'>==����ѡ���豸����==</option>");
			break;
		}
		$.post(url, {
			gwShare_vendorId : vendorId
		}, function(ajax) {
			gwShare_parseMessage(ajax, $("select[@name='devicemodel']"),
					selectvalue);
		});
		break;
	case "devicetype":
		var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
		var vendorId = $("select[@name='vendor']").val();
		var deviceModelId = $("select[@name='devicemodel']").val();
		if ("-1" == deviceModelId) {
			$("select[@name='devicetype']").html(
					"<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			break;
		}
		$.post(url, {
			gwShare_vendorId : vendorId,
			gwShare_deviceModelId : deviceModelId
		}, function(ajax) {
			gwShare_parseMessage(ajax, $("select[@name='devicetype']"),
					selectvalue);
		});
		break;
	default:
		alert("δ֪��ѯѡ�");
		break;
	}
}

function gwShare_parseMessage(ajax, field, selectvalue) {
	var flag = true;
	if ("" == ajax) {
		return;
	}
	var lineData = ajax.split("#");
	if (!typeof (lineData) || !typeof (lineData.length)) {
		return false;
	}
	field.html("");

	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for ( var i = 0; i < lineData.length; i++) {
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		if (selectvalue == xValue) {
			flag = false;
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"' selected>==" + xText
					+ "==</option>";
		} else {
			//����ÿ��value��text��ǵ�ֵ����һ��option����
			option = "<option value='"+xValue+"'>==" + xText
					+ "==</option>";
		}
		try {
			field.append(option);
		} catch (e) {
			alert("�豸�ͺż���ʧ�ܣ�");
		}
	}
	if (flag) {
		field.attr("value", "-1");
	}
}

function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
	var vendor = $.trim($("select[@name='vendor']").val());
	var devicemodel = $.trim($("select[@name='devicemodel']").val());
    $("tr[@id='trData']").show();
    $("button[@name='button']").attr("disabled", true);
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/itms/resource/DPIQuery!queryDPIDev.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		vendor:vendor,
		devicemodel:devicemodel
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
}

function ToExcel(cityId,starttime1,endtime1,vendor,devicemodel) {
	var page="<s:url value='/itms/resource/DPIQuery!queryDPIDevExcel.action'/>?"
		+ "starttime=" + starttime1
		+ "&endtime=" + endtime1
		+ "&cityId=" + cityId
		+ "&vendor=" + vendor
		+ "&devicemodel=" +devicemodel;
	document.all("childFrm").src=page;
}

function openDev(cityId,starttime1,endtime1,vendor,devicemodel,status,resultId){
	var page="<s:url value='/itms/resource/DPIQuery!queryDetail.action'/>?"
		+ "starttime=" + starttime1
		+ "&endtime=" + endtime1
		+ "&cityId=" + cityId
		+ "&vendor=" + vendor
		+ "&devicemodel=" +devicemodel
		+ "&status=" +status
		+ "&resultId=" +resultId;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}  

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>DPI���ܲ��𱨱�</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>DPI���ܲ��𱨱�</th>
					</tr>
					<tr bgcolor=#ffffff>
					<TR>
						<td class=column align=center width="15%">��ʼʱ��</td>
						<td><input type="text" name="starttime" class='bk' readonly
							value="<s:property value='starttime'/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��" /></td>
						<td class=column align=center width="15%">����ʱ��</td>
						<td><input type="text" name="endtime" class='bk' readonly
							value="<s:property value='endtime'/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��" /></td>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>�豸����</TD>
						<TD width="35%"><select name="vendor" class="bk"
							onchange="gwShare_change_select('deviceModel','-1')">
						</select></TD>

						<TD class=column width="15%" align='right'>�豸�ͺ�</TD>
						<TD width="35%"><select name="devicemodel" class="bk"
							onchange="gwShare_change_select('devicetype','-1')">
								<option value="">==��ѡ����==</option>
						</select></TD>
					</TR>
					<TR>
						<TD class=column width="15%" align='right'>����</TD>
						<TD width="35%"><s:select list="cityList" name="cityId"
								headerKey="-1" headerValue="��ѡ������" listKey="city_id"
								listValue="city_name" cssClass="bk"></s:select></TD>
						<TD class="column" align="right" width="15%"></TD>
						<TD width="35%"></TD>
					</TR>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">&nbsp;��
								ѯ&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....</div>
		</td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
