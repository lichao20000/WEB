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
	var isNew = '1';
	var cityId = $.trim($("select[@name='cityId']").val());
	var prod_spec_id = $.trim($("select[@name='prod_spec_id']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    var isItv = $.trim($("select[@name='isItv']").val());
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/itms/report/PVCReport!countPvc.action'/>'; 
	$.post(url,{
		cityId:cityId,
		prodSpecId:prod_spec_id,
		starttime:starttime,
		endtime:endtime,
		isNew:isNew,
		isItv:isItv
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
	
}

function ToExcel() {
	var page="<s:url value='/itms/report/PVCReport!getExcel.action'/>?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&cityId=" + $.trim($("select[@name='cityId']").val())
		+ "&isNew=" + 1
		+ "&isItv=" + $.trim($("select[@name='isItv']").val())
		+ "&prodSpecId=" + $.trim($("select[@name='prod_spec_id']").val());
	document.all("childFrm").src=page;
}

function openHgw(isItv,cityId,starttime1,endtime1,flag,isAll,prod_spec_id){
	var page="<s:url value='/itms/report/PVCReport!getHgw.action'/>?"
		+ "isItv=" + isItv 
		+ "&cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&reform_flag=" +flag
		+ "&isAll=" +isAll
		+ "&isNew=" + 1
		+ "&prodSpecId=" + prod_spec_id;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						IPTV部署考核
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						查询时间为iTV账户生效时间。
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
					IPTV部署考核
					</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="2002-01-01 00:00:00">
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
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							属 地
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="请选择属地" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
						<td class=column align=center width="15%">
							上行方式
						</td>
						<td>
							<s:select list="#{9:'ADSL',15:'EPON'}"  name="prod_spec_id" headerKey="-1"
								headerValue="请选择上行方式" listKey="key" listValue="value"
								value="prod_spec_id" cssClass="bk"></s:select>					
						</td>
					</tr>
					<tr>
						<td class="column" align="center" width="15%">是否纯ITV</td>
						<td colspan="3" class="bk">
							<select id="isItv" name="isItv">
								<option value="-1" >==请选择==</option>
								<option value="1" >==是==</option>
								<option value="0"  selected="selected">==否==</option>
							</select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
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
