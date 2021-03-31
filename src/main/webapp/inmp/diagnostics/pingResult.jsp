<%@ include file="../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<html>
<title>PING诊断查询</title>
<%@ include file="../toolbar.jsp"%>
<script type="text/javascript" src="../../Js/inmp/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/inmp/jquery.js"></SCRIPT>
<script language="JavaScript">
<!--

$(document).ready(function(){

	var deviceId = "<s:property value="deviceId"/>";

	if (deviceId != undefined && deviceId != "") {


	}

});


function checkForm(){
	//if (document.frm.shortDate.value == "") {
		//alert("请选择日期！");
		//return;
	//}
	var startdate = strTime2Second(document.frm.startDate.value);
	var enddate = strTime2Second(document.frm.endDate.value);

	var startdatesrc = document.frm.startDate.value;
	var enddatesrc = document.frm.endDate.value;

	var cityid = document.frm.cityId.value;
	var devSn = document.frm.devSn.value;

	var url = "pingResult!statExecute.action";
	$.post(url,{
	  startDate:startdate,
	  endDate:enddate,

      cityId:cityid,
      devSn:devSn,
      startDateSrc:startdatesrc,
      endDateSrc:enddatesrc

    },function(mesg){
    	var htmlMesg = init(mesg);
    	//$("div[@id='resultData']").innerHTML = mesg;
    	document.all("resultData").innerHTML = htmlMesg;
    });
}

function queryDataForExcel(shortDate,cityId,reportType,chartType,time_point){
	var url = "pingResult!excel.action";
	url = url + "?shortDate="+shortDate+"&cityId="+cityId+"&reportType="+reportType+"&chartType="+chartType+"&time_point="+time_point;
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPdf(shortDate,cityId,reportType,chartType,time_point){
	var url = "pingResult!pdf.action";
	url = url + "?shortDate="+shortDate+"&cityId="+cityId+"&reportType="+reportType+"&chartType="+chartType+"&time_point="+time_point;
	document.frm2.action = url;
	document.frm2.method = "post";
	document.frm2.submit();
}

function queryDataForPrint(shortDate,cityId,reportType,chartType,time_point){
	var url = "pingResult!statExecute.action";

	url = url + "?shortDate="+shortDate+"&cityId="+cityId+"&reportType="+reportType+"&chartType="+chartType+"&time_point="+time_point;
	url = url+"&isReport=print"

	window.open(url,"","left=40,top=40,width=920,height=660,resizable=yes,scrollbars=yes");
}

function init(strHtml){
	var htmlStr = strHtml;
	htmlStr = htmlStr.replace("<table>", "<table width=\"95%\" align=\"center\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\" bgcolor=\"#999999\">");
	htmlStr = htmlStr.replace(new RegExp("<td>","gm"), "<td class=column>");
	return htmlStr;
}
//strTime:yyyy-MM-dd
function strTime2Second(strTime){
	if(strTime == "" || typeof(strTime) == "undefined"){
		return 0;
	}
	var arrayTime = strTime.split("-");
	var second = (new Date(arrayTime[0], arrayTime[1]-1, arrayTime[2])).getTime()/1000;
	return second;
}

function show(type){
	document.frm.reportTypetxt.value = type;
	if("0"==type){
		this.month.style.display="none";
	}
	else{
		this.month.style.display="";
	}
}

//-->
</script>
<body>
<form name="frm" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" id="myTable" align=center>

	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>

	<tr>
		<TD>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%" >
				<tr align="center" height="25">
					<td colspan="3" class="green_title">
						宽带上网
					</td>
				</tr>

				<tr align="left" id="trnet" STYLE="display:">
					<td colspan="3"  bgcolor=#999999>
						<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" >
							<tr align="center" bgcolor="#FFFFFF">
								<TD class=column5 align="center" width="15%">测试时间</TD>
								<!-- <TD class=column5 align="center">端口</TD> -->
								<TD class=column5 align="center" width="5%">结果</TD>

								<TD class=column5 align="center" width="12%">测试IP</TD>
								<TD class=column5 align="center" width="8%">成功数</TD>
								<TD class=column5 align="center" width="7%">失败数</TD>
								<TD class=column5 align="center" width="7%">包大小</TD>
								<TD class=column5 align="center" width="7%">包数目</TD>
								<TD class=column5 align="center" width="10%">平均响应时间</TD>
								<TD class=column5 align="center" width="10%">最小响应时间</TD>
								<TD class=column5 align="center" width="10%">最大响应时间</TD>
								<TD class=column5 align="center" width="9%">丢包率</TD>

							</tr>

							<s:iterator var = "pingResultList" value="pingResultList">
								<tr align="center" bgcolor="#FFFFFF">

									<s:if test="#pingResultList.time=='N/A' || #pingResultList.time=='null' ">
										<TD align="center">-</TD>
									</s:if>
									<s:else>
										<TD align="center"><s:property value="time"/></TD>
							        </s:else>

							        <!--
									<s:if test="#pingResultList.device_port=='N/A' || #pingResultList.device_port=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="device_port"/></TD>
							        </s:else>
									 -->

									<s:if test="#pingResultList.is_ping_succ=='N/A' || #pingResultList.is_ping_succ=='null' ">
										<TD align="center">-</TD>
									</s:if>
									 <s:else>
									 	 <s:if test="#pingResultList.is_ping_succ==1">
											<TD align="center">成功</TD>
										</s:if>
										 <s:else>

										 	 <s:if test="#pingResultList.is_ping_succ==0">
											<TD align="center">失败</TD>
											</s:if>
											 <s:else>
									            <TD align="center">-</TD>
									        </s:else>

								        </s:else>

							        </s:else>

							        <s:if test="#pingResultList.test_ip=='N/A' || #pingResultList.test_ip=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="test_ip"/></TD>
							        </s:else>

							        <s:if test="#pingResultList.succ_num=='N/A' || #pingResultList.succ_num=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="succ_num"/></TD>
							        </s:else>

							        <s:if test="#pingResultList.fail_num=='N/A' || #pingResultList.fail_num=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="fail_num"/></TD>
							        </s:else>

							        <s:if test="#pingResultList.package_size=='N/A' || #pingResultList.package_size=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="package_size"/></TD>
							        </s:else>

							        <s:if test="#pingResultList.package_num=='N/A' || #pingResultList.package_num=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="package_num"/></TD>
							        </s:else>

							         <s:if test="#pingResultList.avg_res_time=='N/A' || #pingResultList.avg_res_time=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="avg_res_time"/></TD>
							        </s:else>

							         <s:if test="#pingResultList.min_res_time=='N/A' || #pingResultList.min_res_time=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="min_res_time"/></TD>
							        </s:else>

							         <s:if test="#pingResultList.max_res_time=='N/A' || #pingResultList.max_res_time=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="max_res_time"/></TD>
							        </s:else>

							         <s:if test="#pingResultList.rate=='N/A' || #pingResultList.rate=='null' ">
							            <TD align="center">-</TD>
							        </s:if>
							        <s:else>
							            <TD align="center"><s:property value="rate"/>%</TD>
							        </s:else>

								</tr>
							</s:iterator>

							 <s:if test="pingResultList.size()==0 || pingResultList=='null' ">
							 	<tr align="center" bgcolor="#FFFFFF">
							 		<TD colspan="20" align="left">此设备未进行过PING操作！</TD>
							 	</tr>

							 </s:if>

						</TABLE>
					</td>
				</tr>

			</table>
		</TD>
	</tr>
	</TABLE>

</form>
</body>
</html>
<br>
<br>
<%@ include file="../foot.jsp"%>
