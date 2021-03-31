<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
	<head>
		<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
		<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
		<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
		<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
		
		<lk:res />
		<script language="JavaScript">
			function doQuery(){
			    var monthDate = $.trim($("input[@name='monthDate']").val());   
			    var cityId =  $("select[@name='cityId']").val();
			    $("tr[@id='trData']").show();
			    $("#btn").attr("disabled",true);
			    $("div[@id='QueryData']").html("����Ŭ��Ϊ��ͳ�ƣ����Ե�....");
			    var url = "<s:url value='/gtms/report/failReason!countAll.action'/>"; 
				$.post(url,{
					cityId : cityId,
					monthDate : monthDate
				},function(ajax){
					 $("#btn").attr("disabled",false);
				    $("div[@id='QueryData']").html("");
					$("div[@id='QueryData']").append(ajax);
				});
			}
			function ToExcel(cityId,monthDate) {
				var page="<s:url value='/gtms/report/failReason!countAll.action'/>?"
					+ "cityId=" + cityId + "&isReport=excel"
					+ "&monthDate=" + monthDate;
				document.all("childFrm").src=page;
			}
		</script>
	
	</head>
	
	<TABLE>
		<tr>
		<td>
			<table class="green_gargtd">
				<tr>
				<th>ҵ��ͨ�ɹ���ͳ�� </th>
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
				<tr> <th colspan=4> ʧ��ԭ��ͳ�� </th> </tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center width="15%">���� </td>
					<td width="35%">
						<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
					</td>
					<td class=column align=center width="15%">ʱ�� </td>
					<td>
						<input type="text" name="monthDate" class='bk' readonly value="<s:property value="monthDate"/>">
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.monthDate,dateFmt:'yyyy-MM',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15" height="12"
							border="0" alt="ѡ��" />
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=foot colspan=4 align=right>
						<button id="btn" onclick="doQuery();">&nbsp;ͳ ��&nbsp;</button>
					</td>
				</tr>
			</table>
			</form>
		</td>
		</tr>
		<tr id="trData" style="display: none" >
			<td>
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
					����Ŭ��Ϊ��ͳ�ƣ����Ե�....
				</div>
			</td>
		</tr>
		<tr>
			<td height="20"> </td>
		</tr>
	</TABLE>
	<%@ include file="/foot.jsp"%>
</html>
