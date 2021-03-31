<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/inmp/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/inmp/My97DatePicker/WdatePicker.js"/>"></script>
<%
String startDealdate1=request.getParameter("startDealdate1");
String endDealdate1=request.getParameter("endDealdate1");

%>
<script language="JavaScript">
function doQuery(){
	var isNew = '1';
	var cityId = $.trim($("select[@name='cityId']").val());
	var isActive = $.trim($("select[@name='isActive']").val());
    var startDealdate = $.trim($("input[@name='startDealdate']").val());
    var endDealdate = $.trim($("input[@name='endDealdate']").val());
    if(cityId == "-1"){
         alert("请选择属地");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("正在统计，请稍等....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/inmp/report/E8CActiveReport!getE8CData.action'/>'; 
	$.post(url,{
		cityId:cityId,
		isActive:isActive,
		startDealdate:startDealdate,
		endDealdate:endDealdate
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});

	
}

function ToExcel() {
// 	var page="<s:url value='/itms/report/E8CActiveReport!getCityExcel.action'/>?"
// 		+ "&startDealdate=" + starttime1
// 		+ "&endDealdate=" + endtime1
// 		+ "&isActive=" + isActive
// 		+ "&cityId=" + cityId;
	var mainForm = document.getElementById("selectForm");
	mainForm.action = "<s:url value='/inmp/report/E8CActiveReport!getCityExcel.action'/>";
	mainForm.submit();
	mainForm.action = "";
	//document.all("childFrm",0).src=page;
}

function openCus(cityId,isActive,starttime1,endtime1,type){
	
	var page="<s:url value='/inmp/report/E8CActiveReport!getCustomerList.action'/>?"
		+ "cityId=" + cityId 
		+ "&startDealdate1=" +starttime1
		+ "&endDealdate1=" +endtime1
		+ "&type=" +type
		+ "&isActive=" + isActive;
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
			<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="back_blue">
				<tr>
					<td width="162" align="center" class="title_bigwhite">
						E8C活跃终端管理率
					</td>
					<td>
						<img src="<s:url value="/images/inmp/attention_2.gif"/>" width="15" height="12">
						查询时间为用户受理时间，是否活跃为终端是否活跃，活跃终端为终端最后上线时间一个月内，活跃率=E8C活跃竣工用户绑定活跃终端数/E8C活跃竣工用户数
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
					E8C活跃度统计
					</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							开始时间
						</td>
						<td>
							<input type="text" name="startDealdate" class='bk' readonly
								value="<s:property value='startDealdate'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.startDealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/inmp/dateButton.png'/>" width="15" height="12"
								border="0" alt="选择" />					
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endDealdate" class='bk' readonly
								value="<s:property value='endDealdate'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endDealdate,dateFmt:'yyyy-MM-dd  HH:mm:ss',skin:'whyGreen'})"
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
						<td class=column align=center width="15%">
							终端是否活跃
						</td>
						
							<td>
								<SELECT name="isActive">
									<option selected value="">全部</option>
									<option value="1">是</option>
									<option value="0">否</option>
								</SELECT>				
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

<%@ include file="../foot.jsp"%>
