<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>

		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
		<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>

	<lk:res />

<script language="JavaScript">

var gw_type =    '<s:property value="gw_type"/>';
function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/gtms/report/configRate.action'/>'; 
	$.post(url,{
		cityId:cityId,
		endtime:endtime,
		gw_type:gw_type
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
	 
}

function ToExcel(cityId,endtime1) {
	var page="<s:url value='/gtms/report/configRate!getExcel.action'/>?"
		+ "&endtime1=" + endtime1
		+ "&cityId=" + cityId
		+ "&gw_type=" +gw_type;
	document.all("childFrm").src=page;
}

function openHgw(cityId,endtime1,flag,isAll){
	var page="<s:url value='/itms/report/PVCReport!getHgw.action'/>?"
		+ "cityId=" + cityId 
		+ "&endtime1=" +endtime1
		+ "&reform_flag=" +flag
		+ "&isAll=" +isAll
		+ "&gw_type=" +gw_type;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function countBycityId(cityId,endtime){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/gtms/report/configRate.action'/>'; 
	$.post(url,{
		cityId:cityId,
		endtime:endtime,
		gw_type:gw_type
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

</script>
</head>
	
<br>
<TABLE>
		<tr>
		<td>
			<table class="green_gargtd">
				<tr>
				<th>新疆FTTH配置成功率报表 </th>
				<td>
					<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
				</td>
				</tr>
			</table>
		</td>
		</tr>
		<tr>
		<td>
			<form name=frm>
			<table class="querytable">
				<tr> <th colspan=4>FTTH配置成功率报表统计 </th> </tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center width="15%">属地 </td>
					<td width="35%">
						<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
					</td>
					<td class=column align=center width="15%">
							活跃时间
						</td>
						<td width="35%">
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<!-- 
							<img
								onclick="new WdatePicker(document.frm.activetime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">						
							 -->
							 <img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="选择" />
						</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=foot colspan=4 align=right>
						<button name="button" onclick="doQuery();">&nbsp;统 计&nbsp;</button>
					</td>
				</tr>
			</table>
			</form>
		</td>
		</tr>
		<tr id="trData" style="display: none" >
			<td>
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
					正在努力为您统计，请稍等....
				</div>
			</td>
		</tr>
		<tr>
			<td height="20"> </td>
		</tr>
	</TABLE>
	
<%@ include file="/foot.jsp"%>
</html>