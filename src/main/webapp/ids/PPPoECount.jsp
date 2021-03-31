<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>PPPoEʧ��ԭ��ͳ�Ʋ�ѯ</title>
<link rel="stylesheet" href="../css3/c_table.css" type="text/css">
<link rel="stylesheet" href="../css3/global.css" type="text/css">
<link rel="stylesheet" href="../css/tab.css" type="text/css">
<script type="text/javascript" src="../Js/jquery.js"></script>
<script type="text/javascript" src="../Js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    if(cityId == "-1"){
         alert("��ѡ������");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    $("button[@name='button']").attr("disabled", true);
    var operType = "<s:property value = 'operType' />";
    var url = "<s:url value='/ids/pppoeCount.action'/>";
	$.post(url,{
		starttime:starttime,
		endtime:endtime,
		cityId:cityId
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
}
</script>
</head>
<body>
	<br>
	<table>
		<tr>
			<td>
				<table class="green_gargtd">
					<tr>
						<th>
							PPPoEʧ��ԭ��ͳ�Ʊ���
						</th>
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
							PPPoEʧ��ԭ��ͳ��
						</th>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=column align=center width="15%">
								��ʼʱ��
							</td>
							<td>
								<input type="text" name="starttime" class='bk' readonly
									value="<s:property value='starttime'/>">
								<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��" />						
							</td>
							<td class=column align=center width="15%">
								����ʱ��
							</td>
							<td>
								<input type="text" name="endtime" class='bk' readonly
									value="<s:property value='endtime'/>">
								<img name="shortDateimg"
									onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
									src="../images/dateButton.png" width="15" height="12"
									border="0" alt="ѡ��" />							
							</td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=column align=center width="15%">
								�� ��
							</td>
							<td colspan="3">
								<s:select list="cityList" name="cityId" headerKey="-1"
									headerValue="��ѡ������" listKey="city_id" listValue="city_name"
									value="cityId" cssClass="bk"></s:select>
							</td>
						</tr>
						<tr bgcolor=#ffffff>
							<td class=foot colspan=4 align=right>
								<button onclick="doQuery()" name="button">&nbsp;��ѯ&nbsp;</button>
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
	</table>
</body>
</html>
<%@ include file="../foot.jsp"%>
