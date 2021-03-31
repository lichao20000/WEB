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
	//������:		trim
	//����  :	str �������ַ���
	//����  :	���ݴ���Ĳ�������ȥ�����ҵĿո�
	//����ֵ:		trim��str��
	//˵��  :	
	//����  :	Create 2015-6-4 of By yinlei3
	------------------------------------------------------------------------------*/
	function trim(str) {
		return str.replace(/(^\s*)|(\s*$)/g, "");
	}

	// ȷ�ϸ���
	function save() {
		// ������
		var targetVersion_add = trim($("input[@name='targetVersion_add']")
				.val());
		var upgradeRange_add = trim($("input[@name='upgradeRange_add']").val());
		var upgradeMethod_add = trim($("input[@name='upgradeMethod_add']")
				.val());
		var starttime_add = trim($("input[@name='starttime_add']").val());
		var endtime_add = trim($("input[@name='endtime_add']").val());

		// �Ǳ�����
		var file_path_add = $("input[@name='file_path_add']").val();
		var fileName = file_path_add
				.substring(file_path_add.lastIndexOf("\\") + 1);
		$("input[@name='fileName_modify']").val(fileName);
		var deviceCount_add = trim($("input[@name='deviceCount_add']").val());
		if ("" == targetVersion_add) {
			alert("������Ŀ��汾��");
			return;
		}
		if ("" == upgradeRange_add) {
			alert("������������Χ��");
			return;
		}
		if ("" == upgradeMethod_add) {
			alert("������������ʽ��");
			return;
		}
		if ("" == starttime_add) {
			alert("��ѡ��������ʼʱ�䣡");
			return;
		}
		if ("" == endtime_add) {
			alert("��ѡ����������ʱ�䣡");
			return;
		}
		if("" != deviceCount_add && !checkDevCount(deviceCount_add)){
			alert("�ն��������������֣�");
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

	//���ز�ѯ����
	function back() {
		var url = "<s:url value='/itms/resource/softUpgradRecordQuery!init.action'/>"; // ���¼��ز�ѯҳ��
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
								<div align="center" class="title_bigwhite">���������¼�޸�</div>
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
										<TH colspan="4" align="center">�޸����������¼</TH>
									</TR>
									<input type="hidden" name="recordId"
										value="<s:property value="recordMap['record_id']" />"
										readonly="readonly" />
									<input type="hidden" name="fileName_modify" value="" />
									<input type="hidden" name="fileName"
										value="<s:property value="recordMap['upgrade_file_name']" />"
										readonly="readonly" />
									<TR bgcolor="#FFFFFF" id="vendor_idID">
										<TD class=column align="center" width="15%">�ն˳���</TD>
										<TD width="85%"><input type="text" name="vendor_add"
											value="<s:property value="recordMap['vendorName']" />"
											size="20" maxlength="40" class="bk" readonly="readonly" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF" id="device_ModelID">
										<TD class=column align="center" width="15%">�ն��ͺ�</TD>
										<TD width="85%"><input type="text"
											name="device_model_add"
											value="<s:property value="recordMap['device_model']" />"
											size="20" maxlength="40" class="bk" readonly="readonly" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">���а汾</TD>
										<TD width="85%"><input type="text"
											name="currentVersion_add"
											value="<s:property value="recordMap['currentSoftWareVersion']" />"
											size="20" maxlength="40" class="bk" readonly="readonly" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">Ŀ��汾</TD>
										<TD width="85%"><input type="text"
											name="targetVersion_add"
											value="<s:property value="recordMap['target_devicetype']" />"
											size="20" maxlength="40" class="bk" />&nbsp; <font
											color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">������Χ</TD>
										<TD width="85%"><input type="text"
											name="upgradeRange_add"
											value="<s:property value="recordMap['upgrade_range']" />"
											size="20" maxlength="40" class="bk" />&nbsp; <font
											color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">�ն�����</TD>
										<TD width="85%"><input type="text" name="deviceCount_add"
											value="<s:property value="recordMap['device_count']" />"
											size="20" maxlength="40" class="bk" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">����ԭ��</TD>
										<TD width="85%"><textarea rows="3" cols="60"
												name="upgradeReason_add"><s:property
													value="recordMap['upgrade_reason']" /></textarea></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">������ʽ</TD>
										<TD width="85%"><input type="text"
											name="upgradeMethod_add"
											value="<s:property value="recordMap['upgrade_method']" />"
											size="20" maxlength="40" class="bk" />&nbsp; <font
											color="#FF0000">*</font></TD>
									</TR>
									<TR bgcolor=#FFFFFF>
										<td class="column" width="15%" align="center">������ʼʱ��:</td>
										<td width="85%" align="left"><input type="text"
											name="starttime_add" class='bk' readonly
											value="<s:property value="recordMap['start_time']" />">
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.modifyForm.starttime_add,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��"> &nbsp; <font color="#FF0000">*</font></td>
									</TR>
									<TR bgcolor="#FFFFFF">
										<td class="column" width="15%" align="center">��������ʱ��:</td>
										<td width="85%" align="left"><input type="text"
											name="endtime_add" class='bk' readonly
											value="<s:property value="recordMap['end_time']" />">
											<img name="shortDateimg"
											onClick="WdatePicker({el:document.modifyForm.endtime_add,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
											src="../../images/dateButton.png" width="15" height="12"
											border="0" alt="ѡ��"> &nbsp; <font color="#FF0000">*</font></td>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">�ն˳�����ϵ��ʽ</TD>
										<TD width="85%"><input type="text" name="contactWay_add"
											value="<s:property value="recordMap['contact_way']" />"
											size="20" maxlength="40" class="bk" /></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD class=column align="center" width="15%">�����ϴ�</TD>
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
											name="saveButton" onclick="javascript:save()" value=" �� �� "
											class=jianbian>&nbsp;&nbsp;<INPUT TYPE="reset"
											name="resetButton" value=" �� д " class=jianbian>&nbsp;&nbsp;<INPUT
											TYPE="button" name="return" onclick="javascript:back()"
											value=" �� �� " class=jianbian></TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</FORM> <!-- ��Ӻͱ༭part end -->
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