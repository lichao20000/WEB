<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){
	var countDesc = $.trim($("select[@name='countDesc']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var reportType = $("input[@name='reportType'][@checked]").val();
    var stat_time = $.trim($("input[@name='stat_time']").val());

    if(cityId == "-1"){
		alert("��ѡ������");
		return false;
    }
    if(stat_time==""){
    	alert("�������ѯʱ��");
		return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/bbms/report/bandwidthTopN.action'/>'; 
	$.post(url,{
		reportType:reportType,
		cityId:cityId,
		stat_time:stat_time,
		countDesc:countDesc
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}


function ToExcel(cityId,reportType,stat_time,countDesc) {
	var page="<s:url value='/bbms/report/bandwidthTopN!getExcel.action'/>?"
		+ "&cityId=" + cityId
		+ "&reportType=" + reportType
		+ "&stat_time=" + stat_time
		+ "&countDesc=" + countDesc;
	document.all("childFrm").src=page;
}

</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						����ռ����TopN����
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
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
							����ռ����TopN����
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							��������
						</td>
						<td colspan="3" width="85%">
							<input type="radio" name="reportType" checked value="day">
							�ձ���&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="reportType" value="week">
							�ܱ���&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="reportType" value="month">
							�±���
						
						</td>
					</tr>
					
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							�� ��
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
						<td class=column width="15%">
							��ѯʱ��
						</td>
						<td width="35%">
							<input type="text" name="stat_time" class='bk' readonly onclick="new WdatePicker(document.frm.stat_time,'%Y-%M-%D',true,'whyGreen')"
								value="<s:property value='stat_time'/>">
							<img
								onclick="new WdatePicker(document.frm.stat_time,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							TopN����
						</td>
						<td colspan="3">
							<select name="countDesc" class="bk">
								<option value="asc">
									����
								</option>
								<option value="desc">
									����
								</option>
							</select>
						</td>
					</tr>
					
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;�� ѯ&nbsp;
							</button>
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
