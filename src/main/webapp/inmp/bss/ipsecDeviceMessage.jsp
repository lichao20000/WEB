<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<!doctype html public "-//w3c//dtd html 4.01 transitional//en" "http://www.w3.org/tr/html4/loose.dtd">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<title>������Ϣ</title>
<link href="<s:url value="../../css/inmp/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/js/jquerysplitpage-linkage.js"/>"></script>
<script type="text/javascript">
	/* $(document).ready(function() { 
	 var device_id_judge = '<s:property value="devicemap.deviceid" />';
	 if(device_id_judge.length>0){
	 checkcon();
	 }
	 });  */
	function showOperation() {
		if ($('#historyMsgTR').css("display") == "none") {
			$('#historyMsgTR').show();
			$('#historyTitle').text("����");
			quertHistoryMsg();
		} else {
			$('#historyMsgTR').hide();
			$('#historyTitle').text("�鿴");
		}
	}
	function quertHistoryMsg() {
		var url = "deviceCustomer!queryHistoryOperation.action";
		var deviceId = '<s:property value="deviceMap.DEVICEID" />';
		$.post(url, {
			deviceId : deviceId
		}, function(ajax) {
			$("#historyMsgTD").html(ajax);
		});
	}
	function checkcon() {
		$("#connmsg").html('<span class="icon_linking">���ڽ���</span>');
		var device_id_judge = '<s:property value="device_id" />';
		if (null != device_id_judge && "" != device_id_judge) {
			var url = "<s:url value='/itms/service/ipsecSheetServ!queryConnCondition.action'/>";
			$.post(url,{
								device_id_judge : device_id_judge
							},
							function(ajax) {
								if ("1" == ajax) {
									$("#connmsg").html('<span style="color:green">��������</span>');
								} else if ("-6" == ajax) {
									$("#connmsg").html('<span style="color:blue">�豸�������������Ժ�����</span>');
								} else {
									$("#connmsg").html('<span style="color:red">����������</span>');
								}
							});
		}
	}
	function generateComPw() {
		var url = "deviceCustomer!generateComPwd.action";
		$.post(url, {
			gw_type : "<s:property value="deviceMap['GWTYPE']" />"
		}, function(ajax) {
			$("#compw_span_new").html(ajax);
		});

	}
	function setComPw() {
		var paramValue = $("#compw_span_new").text();
		if (null == paramValue || '' == paramValue) {
			alert("������������!");
			return false;
		}
		showMsgDlg();
		var url = "deviceCustomer!modyfiyConPwd.action";
		var deviceId = '<s:property value="deviceMap.DEVICEID" />';
		$.post(url, {
			deviceId : deviceId,
			paramValue : paramValue
		}, function(ajax) {
			closeMsgDlg();
			if (ajax == "-2") {
				alert("���ݳ����������");
				$('#compw_span_new').innerHTML = "";
			} else if (ajax == "-1") {
				alert("��������ʧ��,��ȷ���豸����������");
				$('#compw_span_new').innerHTML = "";
			} else if (ajax == "0") {
				alert("�������óɹ��������ݿ����ʧ�ܣ�����ϵ����Ա��");
			} else if (ajax == "1") {
				alert("�������óɹ���");
				document.all.compw_span.innerHTML = $('#compw_span_new').val()
						.trim();
				$('#compw_span_new').innerHTML = "";
			} else {
				alert("δ֪����");
				$('#compw_span_new').innerHTML = "";
			}
			inspireBtns();
		});
		disableBtns();
	}
	function showMsgDlg() {
		w = document.body.clientWidth;
		h = document.body.clientHeight;

		l = (w - 250) / 2;
		t = (h - 60) / 2;
		PendingMessage.style.left = l;
		PendingMessage.style.top = t;
		PendingMessage.style.display = "";
	}
	function closeMsgDlg() {
		PendingMessage.style.display = "none";
	}

	function disableBtns() {
		document.all.compwGen.disabled = true;
		document.all.compwSet.disabled = true;
	}

	function inspireBtns() {
		document.all.compwGen.disabled = false;
		document.all.compwSet.disabled = false;
	}
</script>

</head>
<body>
	<div id="PendingMessage"
		style="position: absolute; z-index: 3; top: 240px; left: 250px; width: 250; height: 60; border-width: 1; border-style: ridge; background-color: #eeeeee; padding-top: 10px; display: none">
		<center>
			<table border="0">
				<tr>
					<td valign="middle"><img src="../images/cursor_hourglas.gif"
						border="0" WIDTH="30" HEIGHT="30"></td>
					<td>&nbsp;&nbsp;</td>
					<td valign="middle"><span id=txtLoading
						style="font-size: 12px; font-family: ����">�����������룬���Եȡ�</span></td>
				</tr>
			</table>
		</center>
	</div>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD>
				<TABLE width="99%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<s:if test="ipsecDeviceInfo.size()>0&&ipsecDeviceInfo!=null">
									<s:iterator value="ipsecDeviceInfo">
										<TR>
											<TH colspan="4" align="center">�豸�� <s:property
													value="device_serialnumber" />����ϸ��Ϣ
											</TH>
										</TR>
										<tr bgcolor="#FFFFFF" height="20">
											<td width="15%" class=column align="right">�豸id</td>
											<td width="35%"><s:property value="device_id" /></td>

											<td width="15%" class=column align="right">�豸�ͺ�</td>
											<td width="35%"><s:property value="device_model" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">�豸����(oui)</td>
											<td><s:property value="manufacturer" />(<s:property
													value="oui" />)</td>

											<td class=column align="right">���к�</td>
											<td><s:property value="device_serialnumber" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">Ӳ���汾</td>
											<td><s:property value="handware_version" /></td>

											<td class=column align="right">�ر�汾</td>
											<td><s:property value="spec_version" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">����汾</td>
											<td><s:property value="software_version" />
											<td class=column align="right">�豸����</td>
											<td><s:property value="device_type" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">������</td>
											<td><s:property value="maxenvelopes" /></td>

											<td class=column align="right">mac ��ַ</td>
											<td><s:property value="cpe_mac" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">�豸���</td>
											<td><s:property value="device_id" /></td>

											<td class=column align="right">������</td>
											<td><s:property value="device_area_name" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">�豸��id</td>
											<td><s:property value="gw_type" /></td>

											<td class=column align="right">�豸������</td>
											<td><s:property value="gw_type_name" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">�ն˹��</td>
											<td><s:property value="spec_name" /></td>
											<td class=column align="right">��ַ��ʽ</td>
											<td><s:property value="ipType" /></td>
										</tr>
									</s:iterator>
								</s:if>
								<TR bgcolor="#FFFFFF" height="20">
									<TD class=column align="center" colspan=4>�豸ҵ����Ϣ</TD>
								</TR>
								<tr bgcolor="#FFFFFF" height="20">
									<td class=column align="right">��ǰ��ͨҵ��</td>
									<td><s:property
											value="null==ServiceStatByDevice||ServiceStatByDevice.size==0?'��':ServiceStatByDevice.size+'��ҵ��'" /></td>
									<td class=column align="right">�Ƿ񼤻�</td>
									<td><s:property
											value="null==ServiceStatByDevice||ServiceStatByDevice.size==0?'��':''" /></td>
								</tr>
								<s:if
									test="null!=ServiceStatByDevice&&ServiceStatByDevice.size>0">
									<s:iterator value="ServiceStatByDevice" var="item">
										<tr bgcolor="#FFFFFF" height="20">
											<td></td>
											<td><s:property value="item[2]" /></td>
											<td></td>
											<td><s:property
													value="'1'==item[1]? '�Ѽ���' :'0'==item[1]?'δ����':'����ʧ��'" /></td>
										</tr>
									</s:iterator>
								</s:if>
								<TR bgcolor="#FFFFFF" height="20">
									<TD class=column align="center" colspan=4>�豸��̬��Ϣ</TD>
								</TR>
								<s:if test="ipsecDeviceInfo.size()>0&&ipsecDeviceInfo!=null">
									<s:iterator value="ipsecDeviceInfo">
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">�豸��ƽ̨��������״̬</td>
											<TD colspan="3" width="30%">
											<div class="nolink" id="connMsg" style="display:inline">
											<span id="icon_linking"><s:property value="online_status" /></span>&nbsp;&nbsp; 
											</div>
											<input name="onlineStatusGet" type="button" value="��⽻��״̬" class="btn_g" onclick="getOnlineStatus()"></TD>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">�豸���� <input type="hidden"
												name="cityid" value="<s:property value='city_id' />">
											</td>
											<td><s:property value="city_name" /></td>
											<td class=column align="right">ע��ϵͳʱ��</td>
											<td><s:property value="complete_time" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">ip��ַ</td>
											<td><s:property value="loopback_ip" /></td>

											<td class=column align="right">�������ʱ��</td>
											<td><s:property value="last_time" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">�豸��ǰע��״̬</td>
											<td><s:property value="device_status" /></td>

											<td class=column align="right">�豸����ģ��</td>
											<td><textarea cols=40 rows=4 readonly>
													<s:property value="deviceModelTemplate" />
												</textarea></td>
										</tr>
										<TR bgcolor="#FFFFFF" height="20">
											<TD class=column align="center" valign="top" colspan=4>
												IPSEC������ʷ��Ϣ �� <a href="javascript:showOperation()"
												style="color: 808080; font-size: 9pt;">�鿴</a>��
											</TD>
										</TR>
										<tr id="historyMsgTR" style="display: none;">
											<td colspan="4" id="historyMsgTD"></td>
										</tr>
										<TR bgcolor="#FFFFFF" height="20">
											<TD class=column align="center" colspan="4">��ǰ���ò���</TD>
										</TR>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">CPE�û���</td>
											<td><s:property value="cpe_username" /></td>

											<td class=column align="right">CPE����</td>
											<td><s:property value="cpe_passwd" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">ACS�û���</td>
											<td><s:property value="acs_username" /></td>

											<td class=column align="right">ACS����</td>
											<td><s:property value="acs_passwd" /></td>
										</tr>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">����ά���˺�</td>
											<td><s:property value="x_com_username" /></td>

											<td class=column align="right">����ά������</td>
											<td><span id="compw_span"><s:property
														value="ipsecDeviceInfo['x_com_passwd']" /></span>&nbsp; <span
												id="compw_span_new"></span>&nbsp; <!-- <input name="compw_span_hid" value="" type="hidden"/> -->
												<input name="compwGen" type="button" value="����"
												class="btn_g" onclick="generateComPw()"> <input
												name="compwSet" type="button" value="����" class="btn_g"
												onclick="setComPw()"></td>
										</tr>
										<TR bgcolor="#FFFFFF" height="20">
											<TD class=column align="center" colspan=4>�û�������Ϣ</TD>
										</TR>
										<tr bgcolor="#FFFFFF" height="20">
											<td class=column align="right">�û��˺�</td>
											<td><a
												href="javascript:GoContent(<s:property value="user_id" />','<s:property value="gw_type" />')"><s:property
														value="username" /></a></td>

											<td class=column align="right">ҵ������</td>
											<td><s:property value="service_name" /></td>
										</tr>
									</s:iterator>
								</s:if>
								<TR>
									<TD colspan="4" align="center" class=foot><INPUT
										TYPE="submit" value=" �� �� " class=jianbian
										onclick="javascript:window.close();"></TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
</body>
</html>