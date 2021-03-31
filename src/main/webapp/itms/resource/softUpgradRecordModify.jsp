<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>


<html>
<head>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<lk:res />
<SCRIPT type="text/javascript">
	/*------------------------------------------------------------------------------
	//函数名:		trim
	//参数  :	str 待检查的字符串
	//功能  :	根据传入的参数进行去掉左右的空格
	//返回值:		trim（str）
	//说明  :	
	//描述  :	Create 2015-6-4 of By yinlei3
	------------------------------------------------------------------------------*/
	function trim(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

	// 确认更新
	function save() {
		// 必须项
		var targetVersion_add = trim($("input[@name='targetVersion_add']")
				.val());
		var upgradeRange_add = trim($("input[@name='upgradeRange_add']").val());
		var upgradeMethod_add = trim($("input[@name='upgradeMethod_add']")
				.val());
		var starttime_add = trim($("input[@name='starttime_add']").val());
		var endtime_add = trim($("input[@name='endtime_add']").val());

		// 非必须项
		var file_path_add = $("input[@name='file_path_add']").val();
		var fileName = file_path_add
				.substring(file_path_add.lastIndexOf("\\") + 1);
		$("input[@name='fileName_modify']").val(fileName);
		var deviceCount_add = trim($("input[@name='deviceCount_add']").val());
		if ("" == targetVersion_add) {
			alert("请输入目标版本！");
			return;
		}
		if ("" == upgradeRange_add) {
			alert("请输入升级范围！");
			return;
		}
		if ("" == upgradeMethod_add) {
			alert("请输入升级方式！");
			return;
		}
		if ("" == starttime_add) {
			alert("请选择升级开始时间！");
			return;
		}
		if ("" == endtime_add) {
			alert("请选择升级结束时间！");
			return;
		}
		if("" != deviceCount_add && !checkDevCount(deviceCount_add)){
			alert("终端数量请输入数字！");
			return 
		}
		var url = "<s:url value='/itms/resource/softUpgradRecordQuery!modifyByRecordId.action'/>";
		document.modifyForm.action = url;
		document.modifyForm.submit();

	}

	function checkDevCount(str) {
		var pattern = /^\d+$/;
		return pattern.test(str);
	}

	//返回查询界面
	function back() {
		var url = "<s:url value='/itms/resource/softUpgradRecordQuery!init.action'/>"; // 重新加载查询页面
		window.location.href = url;
	}
</SCRIPT>
</head>

<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR>
			<TD>
				<FORM id="modifyForm" name="modifyForm" target="modifyResultForm"
					method="post" action="" enctype="multipart/form-data">
					<table width="98%" height="30" border="0" align="center"
						cellpadding="0" cellspacing="0" class="green_gargtd">
						<tr>
							<td width="162">
								<div align="center" class="title_bigwhite">软件升级记录修改</div>
							</td>
							<td><img src="/itms/images/attention_2.gif" width="15"
								height="12"></td>
						</tr>
					</table>
					<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
						align="center" id="addTable" style="display: show">
						<TR>
							<TD bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%"
									id="allDatas">
									<TR>
										<TH colspan="4" align="center">修改软件升级记录</TH>
									</TR>
									<input type="hidden" name="recordId"
										value="<s:property value="recordMap['record_id']" />"
										readonly="readonly" />
									<input type="hidden" name="fileName_modify" value="" />
									<input type="hidden" name="fileName"
										value="<s:property value="recordMap['upgrade_file_name']" />"
										readonly="readonly" />
									<TR bgcolor="#FFFFFF" id="vendor_idID">
										<TD class=column align="center" width="15%">终端厂家</TD>
										<TD width="85%"><input type="text" name="vendor_add"
											value="<s:property value="recordMap['vendorName']" />"
											size="20" maxlength="40" class="bk" readonly="readonly" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="device_ModelID">
										<TD class=column align="center" width="15%">终端型号</TD>
										<TD width="85%"><input type="text"
											name="device_model_add"
											value="<s:property value="recordMap['device_model']" />"
											size="20" maxlength="40" class="bk" readonly="readonly" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">现有版本</TD>
										<TD width="85%"><input type="text"
											name="currentVersion_add"
											value="<s:property value="recordMap['currentSoftWareVersion']" />"
											size="20" maxlength="40" class="bk" readonly="readonly" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">目标版本</TD>
										<TD width="85%"><input type="text"
											name="targetVersion_add"
											value="<s:property value="recordMap['target_devicetype']" />"
											size="20" maxlength="40" class="bk" />&nbsp; <font
											color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">升级范围</TD>
										<TD width="85%"><input type="text"
											name="upgradeRange_add"
											value="<s:property value="recordMap['upgrade_range']" />"
											size="20" maxlength="40" class="bk" />&nbsp; <font
											color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">终端数量</TD>
										<TD width="85%"><input type="text" name="deviceCount_add"
											value="<s:property value="recordMap['device_count']" />"
											size="20" maxlength="40" class="bk" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">升级原因</TD>
										<TD width="85%"><textarea rows="3" cols="60"
												name="upgradeReason_add"><s:property
													value="recordMap['upgrade_reason']" /></textarea></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">升级方式</TD>
										<TD width="85%"><input type="text"
											name="upgradeMethod_add"
											value="<s:property value="recordMap['upgrade_method']" />"
											size="20" maxlength="40" class="bk" />&nbsp; <font
											color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor=#FFFFFF>
										<td class="column" width="15%" align="center">升级开始时间:</td>
										<td width="85%" align="left"><input type="text"
											name="starttime_add" class='bk' readonly
											value="<s:property value="recordMap['start_time']" />">
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.modifyForm.starttime_add,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择"> &nbsp; <font color="#FF0000">*</font></td>
									</TR>
									<TR bgcolor="#FFFFFF">
										<td class="column" width="15%" align="center">升级结束时间:</td>
										<td width="85%" align="left"><input type="text"
											name="endtime_add" class='bk' readonly
											value="<s:property value="recordMap['end_time']" />">
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.modifyForm.endtime_add,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="选择"> &nbsp; <font color="#FF0000">*</font></td>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">终端厂家联系方式</TD>
										<TD width="85%"><input type="text" name="contactWay_add"
											value="<s:property value="recordMap['contact_way']" />"
											size="20" maxlength="40" class="bk" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">附件上传</TD>
										<TD width="85%"><input type="file" name="file_path_add"
											id="file_path" size="35" /></TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
						<TR>
							<TD bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=0 width="100%">
									<TR bgcolor="#FFFFFF">
										<TD align="center" CLASS=green_foot><INPUT TYPE="button"
											name="saveButton" onclick="javascript:save()" value=" 保 存 "
											class=jianbian>&nbsp;&nbsp;<INPUT TYPE="reset"
											name="resetButton" value=" 重 写 " class=jianbian>&nbsp;&nbsp;<INPUT
											TYPE="button" name="return" onclick="javascript:back()"
											value=" 返 回 " class=jianbian></TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</FORM> <!-- 添加和编辑part end -->
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20><iframe id="modifyResultForm"
					name="modifyResultForm" height="0" frameborder="0" scrolling="no"
					width="100%" src=""></iframe></TD>
		</TR>
		<TR>
			<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
		</TR>

	</TABLE>
</body>

<%@ include file="/foot.jsp"%>


</html>