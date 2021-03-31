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
	var serviceId = $.trim($("select[@name='serviceId']").val());
	var cityId = $.trim($("select[@name='cityId']").val());
	var reportType = $.trim($("select[@name='reportType']").val());
    var stat_day = $.trim($("input[@name='stat_day']").val());

    if(cityId == "-1"){
		alert("��ѡ������");
		return false;
    }
    if(stat_day==""){
    	alert("������ͳ��ʱ��");
		return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/bbms/report/serviceReport.action'/>'; 
	$.post(url,{
		serviceId:serviceId,
		cityId:cityId,
		reportType:reportType,
		stat_day:stat_day
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function countBycityId(cityId,serviceId,reportType,stat_day){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/bbms/report/serviceReport.action'/>'; 
	$.post(url,{
		serviceId:serviceId,
		cityId:cityId,
		reportType:reportType,
		stat_day:stat_day
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(cityId,serviceId,reportType,stat_day) {
	var page="<s:url value='/bbms/report/serviceReport!getExcel.action'/>?"
		+ "&cityId=" + cityId
		+ "&serviceId=" + serviceId
		+ "&reportType=" + reportType
		+ "&stat_day=" + stat_day;
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
						ҵ��ʹ��ͳ��
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
						ͳ�������豸��ҵ��ʹ�����
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
							ҵ��ʹ��ͳ��
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							ҵ������
						</td>
						<td>
							<s:select list="serviceTypeList" name="serviceId" headerKey="-1"
								headerValue="����ҵ��" listKey="service_id" listValue="service_name"
								value="serviceId" cssClass="bk"></s:select>
						</td>
						<td class=column align=center width="15%">
							�� ��
						</td>
						<td>
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��������
						</td>
						<td>
							<select name="reportType" class="bk">
								<option value="week">
									�ܱ���
								</option>
								<option value="month">
									�±���
								</option>
							</select>
						</td>
						<td class=column align=center width="15%">
							ͳ��ʱ��
						</td>
						<td>
							<input type="text" name="stat_day" class='bk' readonly onclick="new WdatePicker(document.frm.stat_day,'%Y-%M-%D',true,'whyGreen')"
								value="<s:property value='stat_day'/>">
							<img
								onclick="new WdatePicker(document.frm.stat_day,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;ͳ ��&nbsp;
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
