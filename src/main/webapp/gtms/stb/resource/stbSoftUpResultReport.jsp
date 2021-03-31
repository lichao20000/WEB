<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script language="JavaScript">
var isSoftUp = '<s:property value="isSoftUp"/>';
var InstArea = '<s:property value="InstArea"/>';
$(function(){
	if("hb_lt"==InstArea){
		change_select("vendor");
		var city_ids=$.trim($("input[@name='city_ids']").val());
		parseMessage(city_ids,$("select[@name='cityId']"));
		$("select[@name='city_id']").html("<option value='-1'>==����ѡ������==</option>");
	}
});

function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
	var city_id = $.trim($("select[@name='city_id']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    var vendorId = $.trim($("select[@name='vendorId']").val());
    var modelId = $.trim($("select[@name='modelId']").val());
    if(cityId == "-1"){
         alert("��ѡ������");
         return false;
    }
    $("tr[@id='trData']").show();
    $("button[@name='button']").attr("disabled", true);
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/gtms/stb/resource/StbSoftUpResultReportAction.action'/>'; 
	$.post(url,{
		cityId:cityId,
		city_id:city_id,
		starttime:starttime,
		endtime:endtime,
		isSoftUp:isSoftUp,
		vendorId:vendorId,
		modelId:modelId
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
}

function countBycityId(cityId,starttime,endtime,vendorId,modelId){
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/gtms/stb/resource/StbSoftUpResultReportAction.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		isSoftUp:isSoftUp,
		vendorId:vendorId,
		modelId:modelId
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	//	$("input[@name='button']").attr("disabled", false);
	});
}

function ToExcel(cityId,starttime1,endtime1,vendorId,modelId) {
	var page="<s:url value='/gtms/stb/resource/StbSoftUpResultReportAction!getExcel.action'/>?"
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&cityId=" + cityId
		+ "&isSoftUp=" + isSoftUp
		+ "&vendorId=" + vendorId
		+ "&modelId=" + modelId;
	document.all("childFrm").src=page;
}

function openDev(cityId,starttime1,endtime1,status,resultId,vendorId,modelId){
	var page="<s:url value='/gtms/stb/resource/StbSoftUpResultReportAction!getDev.action'/>?"
			+ "cityId=" + cityId 
			+ "&starttime1=" +starttime1
			+ "&endtime1=" +endtime1
			+ "&status=" +status
			+ "&resultId=" +resultId
			+ "&isSoftUp=" + isSoftUp
			+ "&vendorId=" + vendorId
			+ "&modelId=" + modelId;
	
	
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}  

function change_select(str)
{
	if("city"==str)
	{
		var cityId = $("select[@name='cityId']").val();
		var url = "<s:url value='/gtms/stb/resource/StbSoftUpResultReportAction!getCityNextChild.action'/>";

		$.post(url,{
			cityId:cityId
		},function(ajax){
			parseMessage(ajax,$("select[@name='city_id']"));
		});
	}
	
	if("vendor"==str){
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getVendor.action'/>";
		
		$.post(url,{},function(ajax){
			parseMessage(ajax,$("select[@name='vendorId']"));
			$("select[@name='modelId']").html("<option value='-1'>==����ѡ����==</option>");
		});
	}
	
	if("deviceModel"==str)
	{
		var url = "<s:url value='/gtms/stb/resource/gwDeviceQueryStb!getDeviceModel.action'/>";
		var vendorId = $("select[@name='vendorId']").val();
		if("-1"==vendorId){
			$("select[@name='modelId']").html("<option value='-1'>==����ѡ����==</option>");
			return;
		}
		$.post(url,{
			vendorId:vendorId
		},function(ajax){
			parseMessage(ajax,$("select[@name='modelId']"));
		});
	}
}

function parseMessage(ajax,field)
{
	var flag = true;
	if(""==ajax){
		return;
	}
	var lineData = ajax.split("#");
	if(!typeof(lineData) || !typeof(lineData.length)){
		return false;
	}
	field.html("");
	option = "<option value='-1' selected>==��ѡ��==</option>";
	field.append(option);
	for(var i=0;i<lineData.length;i++){
		var oneElement = lineData[i].split("$");
		var xValue = oneElement[0];
		var xText = oneElement[1];
		option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
		try{
			field.append(option);
		}catch(e){
			alert("����ʧ�ܣ�");
		}
	}
	if(flag){
		field.attr("value","-1");
	}
}
</script>

<br>
<input type='hidden' name="city_ids" value="<s:property value="city_ids" />" />
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>�������ͳ��</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr><th colspan=4>�������ͳ��</th></tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��ʼʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="<s:url value="/images/dateButton.png"/>" width="15" height="12"
									border="0" alt="ѡ��" />
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="<s:url value="/images/dateButton.png"/>" width="15" height="12"
									border="0" alt="ѡ��" />					
						</td>
					</tr>
					<ms:inArea areaCode="hb_lt" notInMode="true">
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							�� ��
						</td>
						<td colspan="3">
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk">
							</s:select>
						</td>
					</tr>
					</ms:inArea>
					
					<ms:inArea areaCode="hb_lt" notInMode="false">
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							�� ��
						</td>
						<td >
							<select id="cityId" name="cityId" class="bk" onchange="change_select('city')">
							</select>
						</td>
						<td class=column align=center width="15%">
							��������
						</td>
						<td >
							<select id="city_id" name="city_id" class="bk">
							</select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							���� 
						</td>
						<td>
							<select id="vendorId" name="vendorId" class="bk" onchange="change_select('deviceModel')">
							</select>
						</td>
						<td class=column align=center width="15%">
							�ͺ�
						</td>
						<td>
							<select id="modelId" name="modelId" class="bk" >
							</select>
						</td>
					</tr>
					</ms:inArea>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">&nbsp;ͳ ��&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
