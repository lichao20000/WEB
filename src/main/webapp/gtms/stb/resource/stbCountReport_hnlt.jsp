<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<title>机顶盒按EPG/APK版本统计</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<script language="JavaScript">
//统计
function doQuery()
{
	var queryType=$.trim($("input[@name='queryType']").val());
	var city_id = $.trim($("select[@name='cityId']").val());
    $("tr[@id='trData']").show();
    $("button[@name='button']").attr("disabled", true);
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    
    var url = '<s:url value='/gtms/stb/resource/stbCountACT.action'/>'; 
	$.post(url,{
		city_id:city_id,
		queryType:queryType
	},function(ajax){
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
}

//导出
function ToExcel(city_id) 
{
	var queryType=$.trim($("input[@name='queryType']").val());
	var queryTime=$.trim($("input[@name='queryTime']").val());
	var page="<s:url value='/gtms/stb/resource/stbCountACT!getExcel.action'/>"
				+"?city_id=" + city_id
				+"&queryType="+queryType
				+"&queryTime="+queryTime;
	document.all("childFrm").src=page;
}

//设备详细
function openDev(city_id,version,cityType)
{
	var queryType=$.trim($("input[@name='queryType']").val());
	var page="<s:url value='/gtms/stb/resource/stbCountACT!getStbDeviceList.action'/>"
			+ "?city_id=" + city_id
			+ "&cityType=" + cityType
			+ "&version=" + version
			+"&queryType="+queryType;
	
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}  

</script>

<input type="hidden" name="queryType" value="<s:property value="queryType" />">
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<s:if test="'epg'==queryType">
						<th>机顶盒按EPG版本统计</th>
					</s:if>
					<s:else>
						<th>机顶盒按APK版本统计</th>
					</s:else>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table class="querytable">
				<tr>
					<s:if test="'epg'==queryType">
						<th colspan=4>机顶盒按EPG版本统计</th>
					</s:if>
					<s:else>
						<th colspan=4>机顶盒按APK版本统计</th>
					</s:else>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center width="15%">属 地</td>
					<td colspan="3">
						<s:select list="cityList" name="cityId" headerKey="-1"
							headerValue="请选择属地" listKey="city_id" listValue="city_name"
							value="cityId" cssClass="bk">
						</s:select>
					</td>
				</tr>
				
				<tr bgcolor=#ffffff>
					<td class=foot colspan=4 align=right>
						<button onclick="doQuery()" name="button">&nbsp;统 计&nbsp;</button>
					</td>
				</tr>
			</table>
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
		<td height="20"></td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
