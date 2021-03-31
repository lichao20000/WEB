<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/inmp/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/inmp/My97DatePicker/WdatePicker.js"/>"></script>

<script language="JavaScript">
	var gw_type = '<s:property value="gw_type"/>' ;
function doQuery(){
	var isNew = '1';
	var cityId = $.trim($("select[@name='cityId']").val());
	var usertype = $.trim($("select[@name='usertype']").val());
    var startTime = $.trim($("input[@name='startTime']").val());
    var endTime = $.trim($("input[@name='endTime']").val());
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/inmp/report/getBindRateByCityid!getBindRateByCityid.action'/>'; 
	$.post(url,{
		cityId:cityId,
		usertype:usertype,
		startTime:startTime,
		endTime:endTime,
		gw_type:gw_type
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});

	
}

function ToExcel(cityId,usertype,starttime1,endtime1) {
	var page="<s:url value='/inmp/report/getBindRateByCityid!getBindRateByCityidExcel.action'/>?"
		+ "&starttime1=" + starttime1
		+ "&endtime1=" + endtime1
		+ "&cityId=" + cityId
		+ "&usertype=" + usertype
		+ "&gw_type="+gw_type;
	document.all("childFrm").src=page;
}

function openHgw(cityId,starttime1,endtime1,flag,usertype){
	var page="<s:url value='/inmp/report/getBindRateByCityid!getUser.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&bind_flag=" +flag
		+ "&usertype=" + usertype
		+ "&gw_type="+gw_type;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}


function countBycityId(cityId,usertype,starttime1,endtime1){
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    var url = '<s:url value='/inmp/report/getBindRateByCityid!getBindRateByCityid.action'/>'; 
	$.post(url,{
		cityId:cityId,
		usertype:usertype,
		startTime:starttime1,
		endTime:endtime1,
		gw_type:gw_type
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
						绑定率统计
					</th>
					<td>
						<img src="<s:url value="/images/inmp/attention_2.gif"/>" width="15" height="12">
						<s:if test='1=="<%=gwType%>"'>
							查询时间：E8-B用户按竣工时间统计，E8-C用户按受理时间统计，从用户角度统计。 
						</s:if> 
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
					按绑定率统计
					</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="startTime" class='bk' readonly
								value="<s:property value='startTime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.startTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="<s:url value='/images/inmp/dateButton.png'/>" width="15" height="12"
								border="0" alt="选择" />					
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endTime" class='bk' readonly
								value="<s:property value='endTime'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="<s:url value='/images/inmp/dateButton.png'/>" width="15" height="12"
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
						<s:if test='1==gw_type'>
						<td class=column align=center width="15%">
							用户终端类型
						</td>
						
							<td>
								<SELECT name="usertype">
									<option selected value="2">E8-C</option>
									<option value="1">E8-B</option>
								</SELECT>				
							</td>
						</s:if>
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

<%@ include file="../foot.jsp"%>
