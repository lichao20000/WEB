<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    var specId= $.trim($("select[@name='specId']").val());
    var is_active = $.trim($("select[@name='is_active']").val());
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/itms/report/countServ!getcountAll.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime,
		specId:specId,
		is_active:is_active
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
	
}

function ToExcel(cityId,starttime,endtime,specId,is_active) {
	
	var page="<s:url value='/itms/report/countServ!getCountExcel.action'/>?"
		+ "&cityId=" + cityId
		+ "&starttime=" + starttime
		+ "&endtime=" + endtime
		+ "&specId=" + specId
		+ "&is_active=" + is_active;
	document.all("childFrm").src=page;
	
}

function openDetails(city_id,starttime,endtime,specId,is_active,serv_type_id){
	var page="<s:url value='/itms/report/countServ!getDetails.action'/>?"
		+ "city_id=" + city_id 
		+ "&starttime=" +starttime
		+ "&endtime=" +endtime
		+ "&specId=" +specId
		+ "&is_active=" +is_active
		+"&serv_type_id=" +serv_type_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
					<th colspan=4>
					E8-C用户按业务类型统计
					</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />						
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />							
						</td>
					</tr>
					<tr bgcolor=#ffffff >
						<td class=column align=center width="15%" style="border='0'">
							设备属地
						</td>
						<td style="border='0'">
							<s:select list="cityList" name="cityId" 
								 listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk">
							</s:select>					
						</td>
						<td class=column align=center width="15%" style="border='0'">
							用户规格
						</td>
						<td style="border='0'">
							<s:select list="specList" name="specId" 
								 listKey="id" listValue="spec_name"
								value="specId" cssClass="bk">
							</s:select>						
						</td>
					</tr>
					<tr bgcolor=#ffffff >
						<td class=column align=center width="15%" style="border='0'">
							是否活跃
						</td>
						<td bgcolor=#eeeeee style="border='0'">
							<select name="is_active" class=column>
								<option value="1">是</option>
								<option value="0">否</option>
								<option value="-1">全部</option>
							</select>
						</td>
						<td class=foot colspan=2 align=right style="border='0'">
							<button onclick="doQuery()" name="button">&nbsp;统 计&nbsp;</button>
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
