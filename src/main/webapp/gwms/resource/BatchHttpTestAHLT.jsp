<%--
Author      : fanjiaming
Date		: 2017-6-14
Desc		: 批量HTTP下载业务质量检测(安徽联通)
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../head.jsp"%>
<%
	String gwType = request.getParameter("gw_type");
%>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

<SCRIPT LANGUAGE="JavaScript">
	$(function(){
		$("input[@name='gwShare_queryResultType']").val("checkbox");
		gwShare_setGaoji();
		gwShare_setImport();
	});
	var deviceIds = "";


	/**
	 * @return {boolean}
	 */
	function CheckForm(){
		var task_name = $("input[@name='task_name']").val();
		var speed_frequency = $("input[@name='speed_frequency']").val();

		var startDate = $("input[@name='startDate']").val();
		var endDate = $("input[@name='endDate']").val();
		if(task_name===""){
			alert("请输入任务名称！");
			return false;
		}

		if(speed_frequency===""){
			alert("请输入频次！");
			return false;
		}
		if(startDate===""){
			alert("请输入任务开始时间！");
			return false;
		}

		if(endDate===""){
			alert("请输入任务结束时间！");
			return false;
		}

		var starttime =str2Date(startDate);

		var endtime = str2Date(endDate);


		if(starttime.getHours() > 7 &&starttime.getHours() < 23){
			alert("开始时间应大于23点小于7点！");
			return false;
		}
		//0-7点结束时间<开始时间，且不能跨天
		var maxendtime;
		if(starttime.getHours()>=0&&starttime.getHours()<7){
			maxendtime = str2Date(getNextDate(starttime,0)+" 7:00:00").getTime();
		}else{
			maxendtime = str2Date(getNextDate(starttime,1)+" 7:00:00").getTime();
		}

		if(!(endtime.getTime()>starttime.getTime()&&endtime.getTime()<maxendtime)){
			alert("结束时间应大于开始时间且不允许跨天");
			return false;
		}

		var url = "<s:url value='/gwms/resource/batchHttpTest!checkTime.action'/>";
		var ajaxre=0;
		$.ajaxSettings.async = false;
		$.post(url, {
			startDate : startDate,
			endDate:endDate
		}, function(ajax) {
			if(ajax>0){

				ajaxre=1;
			}
		});
		$.ajaxSettings.async = true;
		if(ajaxre===1){
			alert("所选时间段存在任务重叠，请重新选取！");
			return false;
		}

		if($("input[@name='deviceIds']").val()==""){
			alert("请选择设备！");
			return false;
		}

		return true;
	}

	function deviceResult(returnVal){
		deviceIds="";
		var totalmem=0;
		$("input[@name='task_desc']").val(returnVal[3]);

		if(returnVal[0]==0){

			if(returnVal[3]!==null&&returnVal[3]!==""){
				totalmem = returnVal[3];
			}else{
				totalmem = returnVal[2].length;
			}
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+totalmem+"</strong></font>");
			for(var i=0;i<totalmem;i++){
				deviceIds = deviceIds + returnVal[2][i][0]+",";
			}
			$("input[@name='deviceIds']").val(deviceIds);
		}else{
			if(returnVal[3]!==null&&returnVal[3]!==""){
				totalmem = returnVal[3];
			}else{
				totalmem = returnVal[0];
			}
			$("div[@id='selectedDev']").html("<font size=2><strong>待操作设备数目:"+totalmem+"</strong></font>");
			$("input[@name='deviceIds']").val("0");
			$("input[@name='param']").val(returnVal[1]);
		}
	}


	// date 代表指定的日期，格式：2018-09-27
	// day 传-1表始前一天，传1表始后一天
	// JS获取指定日期的前一天，后一天
	function getNextDate(date, day) {
		var dd = new Date(date);
		dd.setDate(dd.getDate() + day);
		var y = dd.getFullYear();
		var m = dd.getMonth() + 1 < 10 ? "0" + (dd.getMonth() + 1) : dd.getMonth() + 1;
		var d = dd.getDate() < 10 ? "0" + dd.getDate() : dd.getDate();
		return y + "-" + m + "-" + d;
	}

	// date 代表指定的日期字符串
	function str2Date(date) {
		date = date.substring(0,19);
		date = date.replace(/-/g,'/');
		return new Date(date);
	}


	function subForm(){
		if(CheckForm()){
			document.getElementById('frm').submit();
		}
	}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>

	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ID="frm"
				  ACTION="<s:url value="/gwms/resource/batchHttpTest!speedTestAHLT.action"/>"
				  onsubmit="return CheckForm()">
				<input type="hidden" name="hblt_SpeedTest_flg" value="true" />
				<input type="hidden" name="hblt_BatchSpeedTest_flag" value="true" />
				<input type="hidden" name="deviceIds" value="" />
				<input type="hidden" name="param" value="" />
				<input type="hidden" name="gw_type" value="1" />
				<input type="hidden" name="fileName_st" value="" />
				<input type="hidden" name="gw_type" value="<%=gwType%>" />
				<input type="hidden" name="task_desc" value="" />
				<table width="98%" border="0" align="center" cellpadding="0"
					   cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								   cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										批量HTTP下载测速
									</td>
									<td nowrap>
										<img src="<s:url value='/images/attention_2.gif'/>" width="15"
											 height="12">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<TR>
						<TD bgcolor=#999999>
							<TABLE width="100%" border=0 cellspacing=1 cellpadding=2
								   align="center">
								<TR>
									<TH colspan="4" align="center">
										任务信息
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD align="right" class=column width="15%">任务名称</TD>
									<TD>
										<input type="text" name="task_name" value="" size="25" maxlength="50" class="bk" required="required"/>
										<font color="red">*</font>
									</TD>
									<TD align="right" class=column width="15%">测试频次</TD>
									<TD>
										<input type="number" name="speed_frequency" value="" size="25" maxlength="50" class="bk" required="required"/>
										<font color="red">* /2min</font>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF" >
									<TD align="right" class=column width="15%">开始时间</TD>
									<TD width="35%">
										<input type="text"   name="startDate" readonly value="" class=bk required="required" >
										<img name="shortDateimg"
											 onClick="WdatePicker({el:document.frm.startDate,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d 23:00:00', skin:'whyGreen'})"
											 src="../../images/dateButton.png" width="15" height="12"
											 border="0" alt="选择">
										<font color="red">*开始时间在23点之后</font>
									</TD>
									<TD align="right" class=column width="15%" >结束时间</TD>
									<TD width="35%" >
										<input type="text" name="endDate" readonly value="" class=bk required="required">
										<img name="shortDateimg"
											 onClick="WdatePicker({el:document.frm.endDate,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d 23:00:00',skin:'whyGreen'})"
											 src="../../images/dateButton.png" width="15" height="12"
											 border="0" alt="选择">
										<font color="red">*结束时间在7点之前</font>
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<jsp:include page="/gwms/share/gwShareDeviceQuery.jsp" flush="false">
								<jsp:param name="CQ_softUp" value="1"/>
								<jsp:param name="AHLT_softUp" value="1"/>
							</jsp:include>
						</td>
					</TR>
					<TR>
						<TH colspan="4" align="center">
							配置HTTP下载业务质量检测策略
						</TH>
					</TR>

					<tr>
						<td>

							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								   align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev" >
														请查询设备！
													</div>
												</td>
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<input type="button" onclick="subForm()" class=jianbian value=" 执 行 " />
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>

	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
