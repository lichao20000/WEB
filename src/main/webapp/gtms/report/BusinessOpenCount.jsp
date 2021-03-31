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
			function doQuery(cityId){
			    var start = $.trim($("input[@name='starttime']").val());    // 
			    var end = $.trim($("input[@name='endtime']").val());    // 
			    var cityId =  $("#cityId").val();
			    if("-1"==cityId){
			    	alert("��ѡ������");
			    	return;
			    }
			    var selectTypeId =  $("#selectTypeId").val();
			    $("tr[@id='trData']").show();
			    $("#btn").attr("diabled",true);
			    $("div[@id='QueryData']").html("����Ŭ��Ϊ��ͳ�ƣ����Ե�....");
			    var url = "<s:url value='/gtms/report/businessOpen!countAll.action'/>"; 
				$.post(url,{
					cityId : cityId,
					start : start,
					end : end,
					selectTypeId : selectTypeId
				},function(ajax){
					 $("#btn").attr("diabled",false);
				    $("div[@id='QueryData']").html("");
					$("div[@id='QueryData']").append(ajax);
				});
			}
			
			function countBycityId(cityId,start,end,selectTypeId){
				$("#btn").attr("diabled",false);
			    $("tr[@id='trData']").show();
			    $("div[@id='QueryData']").html("����Ŭ��Ϊ��ͳ�ƣ����Ե�....");
			    var url = "<s:url value='/gtms/report/businessOpen!countAll.action'/>"; 
				$.post(url,{
					cityId : cityId,
					start : start,
					end : end,
					selectTypeId : selectTypeId
				},function(ajax){	
					$("#btn").attr("diabled",false);
				    $("div[@id='QueryData']").html("");
					$("div[@id='QueryData']").append(ajax);
				});
			}
			
			function ToExcel(cityId,start,end,parentCityId,selectTypeId) {
				var page="<s:url value='/gtms/report/businessOpen!getBusinessOpenCountExcel.action'/>?"
					+ "cityId=" + cityId
					+ "&start=" + start
					+ "&end=" + end
					+ "&parentCityId=" +parentCityId
					+ "&selectTypeId=" + selectTypeId;
				document.all("childFrm").src=page;
			}
			function openHgw(openStatus, cityId,start,end,parentCityId,selectTypeId){
				var  page="<s:url value='/gtms/report/businessOpen!getUserList.action'/>?"
						+ "openStatus=" + openStatus 
						+ "&cityId=" + cityId 
						+ "&start=" + start
						+ "&end=" + end
						+ "&parentCityId=" +parentCityId
						+ "&selectTypeId=" + selectTypeId;
				window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
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
				<tr> <th colspan=4> ҵ��ͨ�ɹ��� </th> </tr>
				<tr bgcolor=#ffffff>
					<td class=column align=center width="15%">���� </td>
					<td width="35%">
						<s:select list="cityList" id="cityId" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
					</td>
					<td class=column align=center width="15%">ͳ�ƿھ�</td>
					<td width="35%">
						<select id="selectTypeId" name="selectTypeId" >
						<option value="-1">ȫ��&nbsp;</option>
						<option value="0">��װ&nbsp;</option>
						<option value="1">ά��&nbsp;</option>
						</select>
					</td>
				</tr>
				<tr bgcolor=#ffffff>
						<td class=column align="center" width="15%">
							��ʼʱ��
						</td>
						<td width="35%">
							<input type="text" name="starttime" class='bk' readonly value="<s:property value='start'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
						<td class=column align="center" width="15%">
							����ʱ��
						</td>
						<td width="35%">
							<input type="text" name="endtime" class='bk' readonly value="<s:property value='end'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
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
	
		<tr id="trData" style="display: none">
			<td class="colum">
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