<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">

	function doQuery()
	{
		var cityId = $.trim($("select[@name='cityId']").val());
	    var starttime = $.trim($("input[@name='starttime']").val());
	    var endtime = $.trim($("input[@name='endtime']").val());
	    if(cityId == "-1"){
	         alert("请选择属地");
	         return false;
	    }
	    $("tr[@id='trData']").show();
	    $("button[@name='button']").attr("disabled", true);
	    $("div[@id='QueryData']").html("正在统计，请稍等....");
	    var url = '<s:url value='/itms/report/delWanConnReportACT.action'/>'; 
		$.post(url,{
			cityId:cityId,
			starttime:starttime,
			endtime:endtime
		},function(ajax){	
		    $("div[@id='QueryData']").html("");
			$("div[@id='QueryData']").append(ajax);
			$("button[@name='button']").attr("disabled", false);
		});
	}
	
	function countBycityId(cityId,starttime,endtime)
	{
	    $("tr[@id='trData']").show();
	    $("input[@name='button']").attr("disabled", true);
	    $("div[@id='QueryData']").html("正在统计，请稍等....");
	    var url = '<s:url value='/itms/report/delWanConnReportACT.action'/>'; 
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
	
	function ToExcel(cityId,starttime1,endtime1) 
	{
		var page="<s:url value='/itms/report/delWanConnReportACT!getExcel.action'/>?"
			+ "&starttime1=" + starttime1
			+ "&endtime1=" + endtime1
			+ "&cityId=" + cityId
			+ "&status=" +status;
		document.all("childFrm").src=page;
	}
	
	function openDev(cityId,starttime1,endtime1,status)
	{
		var page="<s:url value='/itms/report/delWanConnReportACT!getDev.action'/>?"
			+ "cityId=" + cityId 
			+ "&starttime1=" +starttime1
			+ "&endtime1=" +endtime1
			+ "&status=" +status;
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
						批量光猫业务参数删除任务统计
					</th>
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
					<tr>
						<th colspan=4>批量光猫业务参数删除任务统计</th>
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
