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
         alert("��ѡ������");
         return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
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
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
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
						E8C��Ծ�ն˹�����
					</td>
					<td>
						<img src="<s:url value="/images/inmp/attention_2.gif"/>" width="15" height="12">
						��ѯʱ��Ϊ�û�����ʱ�䣬�Ƿ��ԾΪ�ն��Ƿ��Ծ����Ծ�ն�Ϊ�ն��������ʱ��һ�����ڣ���Ծ��=E8C��Ծ�����û��󶨻�Ծ�ն���/E8C��Ծ�����û���
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
					E8C��Ծ��ͳ��
					</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��ʼʱ��
						</td>
						<td>
							<input type="text" name="startDealdate" class='bk' readonly
								value="<s:property value='startDealdate'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.startDealdate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/inmp/dateButton.png'/>" width="15" height="12"
								border="0" alt="ѡ��" />					
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endDealdate" class='bk' readonly
								value="<s:property value='endDealdate'/>">
							<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endDealdate,dateFmt:'yyyy-MM-dd  HH:mm:ss',skin:'whyGreen'})"
								src="<s:url value='/images/inmp/dateButton.png'/>" width="15" height="12"
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
							�ն��Ƿ��Ծ
						</td>
						
							<td>
								<SELECT name="isActive">
									<option selected value="">ȫ��</option>
									<option value="1">��</option>
									<option value="0">��</option>
								</SELECT>				
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
