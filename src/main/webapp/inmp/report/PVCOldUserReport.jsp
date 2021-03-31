<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="../../css/inmp/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/inmp/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="<s:url value="../../Js/inmp/My97DatePicker/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){
	var isNew = '0';
	var cityId = $.trim($("select[@name='cityId']").val());
	var prod_spec_id = $.trim($("select[@name='prod_spec_id']").val());
    var starttime = $.trim($("input[@name='starttime']").val());
    var endtime = $.trim($("input[@name='endtime']").val());
    if(cityId == "-1"){
         alert("��ѡ������");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    $("button[@name='button']").attr("disabled", true);
    var url = '<s:url value='/inmp/report/PVCReport!countPvc.action'/>'; 
	$.post(url,{
		cityId:cityId,
		prodSpecId:prod_spec_id,
		starttime:starttime,
		endtime:endtime,
		isNew:isNew
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false);
	});
	
}

function ToExcel() {
	var page="<s:url value='/inmp/report/PVCReport!getExcel.action'/>?"
		+ "&starttime=" + document.frm.starttime.value
		+ "&endtime=" + document.frm.endtime.value
		+ "&cityId=" + $.trim($("select[@name='cityId']").val())
		+ "&isNew=" + 0
		+ "&prodSpecId=" + $.trim($("select[@name='prod_spec_id']").val());
	document.all("childFrm").src=page;
}

function openHgw(isItv,cityId,starttime1,endtime1,flag,isAll,prod_spec_id){
	var page="<s:url value='/inmp/report/PVCReport!getHgw.action'/>?"
		+ "cityId=" + cityId 
		+ "&starttime1=" +starttime1
		+ "&endtime1=" +endtime1
		+ "&reform_flag=" +flag
		+ "&isAll=" +isAll
		+ "&isNew=" + 0
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
						IPTV���𿼺�
					</th>
					<td>
						<img src="<s:url value="/images/inmp/attention_2.gif"/>" width="15" height="12">
						���ʹ�������ײͼ�ͥ���ص�IPTV�û�����ѯʱ��ΪiTV�˻���Чʱ�䡣
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
					���û�IPTV���𿼺�
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
								src="../../images/inmp/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />						
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="2010-12-31 23:59:59">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/inmp/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />								
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
						<td class=column align=center width="15%">
							���з�ʽ
						</td>
						<td>
							<s:select list="#{9:'ADSL',15:'EPON'}"  name="prod_spec_id" headerKey="-1"
								headerValue="��ѡ�����з�ʽ" listKey="key" listValue="value"
								value="prod_spec_id" cssClass="bk"></s:select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" name="button">&nbsp;ͳ ��&nbsp;</button>
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

<%@ include file="../foot.jsp"%>
