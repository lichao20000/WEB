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


function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    var wanAccessTypeId = $.trim($("select[@name='wanAccessTypeId']").val());
    var typeId = $.trim($("select[@name='typeId']").val());
    var forbidNet = $.trim($("select[@name='forbidNet']").val());
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("button[@name='button']").attr("disabled", true);
    
    $("div[@id='QueryData']").html("正在统计，请稍等....");
     var url = '<s:url value='/itms/report/iTVCount!iTVCount.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		wanAccessTypeId:wanAccessTypeId,
		forbidNet:forbidNet,
		typeId:typeId
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
     
	
}

function ToExcel(cityId,starttime1,endtime1,wanAccessTypeId,forbidNet,typeId) {
	var page="<s:url value='/itms/report/iTVCount!getExcel.action'/>?"
		+ "&cityId=" + cityId
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&wanAccessTypeId=" + wanAccessTypeId
		+ "&forbidNet=" + forbidNet
		+ "&typeId=" + typeId;
	document.all("childFrm").src=page;
}

function openHgw(cityId,starttime1,endtime1,wanAccessTypeId,forbidNet,typeId){
	var page="<s:url value='/itms/report/iTVCount!getHgw.action'/>?"
		+ "&cityId=" + cityId
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&wanAccessTypeId=" + wanAccessTypeId
		+ "&forbidNet=" + forbidNet
		+ "&typeId=" + typeId;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function countBycityId(cityId,starttime,endtime,wanAccessTypeId,forbidNet,typeId){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/itms/report/iTVCount!iTVCount.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		wanAccessTypeId:wanAccessTypeId,
		forbidNet:forbidNet,
		typeId:typeId
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						iTV业务统计
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						时间条件是业务竣工时间
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
					iTV业务统计
					</th>
					
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							属地
						</td>
						<td width="35%">
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
						<td class=column width="15%">
							上行方式
						</td>
						<td width="35%">
							<s:select list="wanAccessTypeList" name="wanAccessTypeId" headerKey="-1"
								headerValue="请选择上行方式" listKey="prod_spec_id" listValue="prod_spec_name"
								value="wanAccessTypeId" cssClass="bk"></s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							IPTV类型
						</td>
						<td width="35%">
							<select name="forbidNet" class="bk">
							<option value="-1" selected>
									请选择IPTV类型
								</option>
								<option value="0">
									非纯IPTV
								</option>
								<option value="1">
									纯IPTV
								</option>								
							</select>
						</td>
						<td class=column width="15%">
							终端类型
						</td>
						<td width="35%">
							<s:select list="deviceTypeList" name="typeId" headerKey="-1"
								headerValue="请选择终端类型" listKey="type_id" listValue="type_name"
								value="typeId" cssClass="bk"></s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							开始时间
						</td>
						<td width="35%">
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择" />						
						</td>
						<td class=column width="15%">
							结束时间
						</td>
						<td width="35%">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../../images/dateButton.png" width="15" height="12"
									border="0" alt="选择" />					
						</td>
					</tr>
					
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">&nbsp;统 计&nbsp;</button>&nbsp;&nbsp;&nbsp;
							<button onclick="reset()">&nbsp;重 置&nbsp;</button>
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
