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
function doQuery()
{
    var startTime = $.trim($("input[@name='starttime']").val());
    var endTime = $.trim($("input[@name='endtime']").val());
    var cityId =  $.trim($("select[@name='cityId']").val());
    var servTypeId =  $.trim($("select[@name='servTypeId']").val());
    var gw_type=$.trim($("input[@name='gw_type']").val());

    $("tr[@id='trData']").show();
    $("#btn").attr("disabled",true);
    $("div[@id='QueryData']").html("����ͳ��....");
    
    var url = "<s:url value='/gtms/resource/countByServTypeId!countHaveOpenService.action'/>"; 
	$.post(url,{
		cityId:cityId,
		startTime:startTime,
		endTime:endTime,
		servTypeId:servTypeId,
		gw_type:gw_type
	},function(ajax){
		$("#btn").attr("disabled",false);
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}
</script>
</head>

<input type="hidden" name="gw_type" value="<s:property value='gw_type'/>">
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>�ѿ�ͨҵ���豸ͳ�� </th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" onsubmit="return false;">
			<table class="querytable">
				<tr>
					<th colspan=7>�ѿ�ͨҵ���豸ͳ�Ʋ�ѯ </th>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center width="10%">���� </td>
					<td width="15%">
						<s:select list="cityList" id="cityId" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk">
						</s:select>
					</td>
					<td class=column align=center width="10%">ҵ������</td>
					<td width="15%">
						<select id="servTypeId" name="servTypeId" >
						<option value="10,14"></option>
						<option value="10">��ͥ���ؿ��ҵ��</option>
						<option value="14">��ͥ����VOIPҵ��</option>
						<option value="11,14">��ͥ���ص�VOIPҵ��</option>
						</select>
					</td>
					<td class=column align="center" width="10%">
						��ͨʱ��
					</td>
					<td width="40%" colspan="2">
						<input type="text" name="starttime" class='bk' readonly value="<s:property value='startTime'/>">
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��" />
						&nbsp;&nbsp;-&nbsp;&nbsp;
						<input type="text" name="endtime" class='bk' readonly value="<s:property value='endTime'/>">
						<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��" />
					</td>
				</tr>
				<tr bgcolor=#ffffff>
					<td class=foot colspan=7 align=right>
						<button id="btn" name="btn" onclick="doQuery();">&nbsp;ͳ ��&nbsp;</button>
					</td>
				</tr>
			</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px;height:400px">
				����Ŭ��Ϊ��ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
</html>