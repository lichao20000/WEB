<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
			src="../../Js/My97DatePicker/WdatePicker.js"></script>

<script language="JavaScript">
var gw_type = '<s:property value="gw_type"/>';
function doQuery(){
	var cityId = $.trim($("select[@name='cityId']").val());
    var activetime = $.trim($("input[@name='activetime']").val());
	if(activetime==""){
		alert("��ѡ���Ծʱ��");
		return false;
	}
    if(cityId == "-1"){
         alert("��ѡ������");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
	$("button[@name='button']").attr("disabled", true); 
    var url = '<s:url value='/gwms/report/noActiveDev!count.action'/>'; 
	$.post(url,{
		cityId:cityId,
		activetime:activetime,
		gw_type:gw_type
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
		$("button[@name='button']").attr("disabled", false); 
	});
	

}

function ToExcel(city_id,activetime) {
	var page="<s:url value='/gwms/report/noActiveDev!getExcel.action'/>?"
		+ "&activetime=" + activetime
		+ "&cityId=" + city_id
		+ "&gw_type=" + gw_type;
	document.all("childFrm").src=page;
}

function openDev(cityId,activetime1){
	var page="<s:url value='/gwms/report/noActiveDev!getDeviceList.action'/>?"
		+ "cityId=" + cityId 
		+ "&activetime1=" +activetime1
		+ "&gw_type=" + gw_type;
	window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
}

function countBycityId(city_id,activetime){

    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/gwms/report/noActiveDev!count.action'/>'; 
	$.post(url,{
		cityId:city_id,
		activetime:activetime,
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
						����Ծ�豸��ѯ
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
						��Ծʱ��Ϊ�豸���һ�εĽ���ʱ��
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
					����Ծ�豸��ѯ
					</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							�� ��
						</td>
						<td width="35%">
							<s:select list="cityList" name="cityId" headerKey="-1"
								headerValue="��ѡ������" listKey="city_id" listValue="city_name"
								value="cityId" cssClass="bk"></s:select>
						</td>
						<td class=column align=center width="15%">
							��Ծʱ��
						</td>
						<td width="35%">
							<input type="text" name="activetime" class='bk' readonly
								value="<s:property value='activetime'/>">
							<!-- 
							<img
								onclick="new WdatePicker(document.frm.activetime,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">						
							 -->
							 <img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.activetime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()" id="button" name="button">&nbsp;ͳ ��&nbsp;</button>
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
