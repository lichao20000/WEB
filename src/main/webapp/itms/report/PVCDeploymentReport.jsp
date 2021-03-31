<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    $("input[@name='button']").attr("disabled", true);
    var url = '<s:url value='/itms/report/PVCDeploymentReport.action'/>'; 
	$.post(url,{
		cityId:cityId,
		starttime:starttime,
		endtime:endtime
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("input[@name='button']").attr("disabled", false);
	});

	
	
}

function ToExcel() {
	var page="<s:url value='/itms/report/PVCDeploymentReport!getExcel.action'/>?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&cityId=" + $.trim($("select[@name='cityId']").val());
	document.all("childFrm").src=page;
}

function openHgw(cityId,starttime1,endtime1,flag,isAll){
	var page="<s:url value='/itms/report/PVCDeploymentReport!getHgw.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&reform_flag=" +flag
		+ "&isAll=" +isAll;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

</script>

<br>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite"
						ondblclick="ShowHideLog()">
						IPTV部署考核
					</td>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						针对使用e8-B家庭网关的IPTV用户；查询时间为iTV账户生效时间。
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table width="100%" border="0" cellspacing="1" cellpadding="2"
					bgcolor="#999999">
					<tr>
					<th colspan=4>
					IPTV部署考核
					</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img
								onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">							
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">						
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							属 地
						</td>
						<td colspan="3">
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column colspan=4 align=right>
							<input type="button" name="button" value=" 统 计 " onclick="doQuery()"
								class="jianbian">
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
