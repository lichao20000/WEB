<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<title>��������</title>
<script language="Javascript" SRC="<s:url value='/Js/jquery.js'/>"></script>
<script language="Javascript">
	$(function() {
		//setValue();
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});

	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for ( var i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i]);
				tempobj.style.display = "block";
			}
		}
	}

	function deviceResult(ajax) {
		$("input[@name='gwShare_queryButton']").attr("disabled", false);
		$("td[@id='trDeviceResult']").css("display", "");
		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceCityName']").html("");
		var returnVal = ajax.split("#");
		$("td[@id='tdDeviceSn']").append(returnVal[1]);
		$("td[@id='tdDeviceCityName']").append(returnVal[6]);
		$("input[name='deviceno']").val(returnVal[1]);
		$("input[name='oui']").val(returnVal[2]);
		$("#trDeviceResult").attr("style", "");
	}

	function query() {
		$("input[name='queryButton']").attr("disabled", true);
		$("#querying").html("���ڷ��棬���Ժ�...");
		$("#querying").css("display","");
		$("td[@id='tdData']").css("display","");
		$("td[@id='tdData']").html("<iframe height='0px;' id='dataForm' name='dataForm' frameborder=0 scrolling='no' width='100%' src=''></iframe>");
		document.selectForm.action = "<s:url value='/ids/VoiceRegisterQuery!queryVoiceRegister.action'/>";
		document.selectForm.submit();
	}
</script>

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td HEIGHT=20>&nbsp;</td>
	</tr>
	<tr>
		<td>
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									����ҵ��ע�����</td>
								<td nowrap><img src="../images/attention_2.gif" width="15"
									height="12">��̨����ҵ��ע��������</td>
							</tr>
						</table>
					</td>
				</tr>

				<tr bgcolor="#FFFFFF">
					<td colspan="4"><%@ include
							file="/ids/share/idsShareDeviceWAN.jsp"%>
					</td>
				</tr>
				<tr>
					<td id="trDeviceResult" style="display: none;">
						<form id="form" name="selectForm" method="post" action=""
							target="dataForm">
							<table width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<input type="hidden" name="deviceno" value="">
								<input type="hidden" name="oui" value="">
								<tr>
									<td bgcolor=#999999>
										<table border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td nowrap align="right" class=column width="15%">
													�豸���к�</td>
												<td id="tdDeviceSn" width="35%"></td>
												<td nowrap align="right" class=column width="15%">�豸����
												</td>
												<td id="tdDeviceCityName" width="35%"></td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td nowrap align="right" class=column width="15%">���Է�����</td>
												<td colspan="3"><select id="servicesip"
													name="servicesip">
														<option value="1">���÷�����</option>
														<option value="2">���÷�����</option>
												</select></td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="5" align="right" class="green_foot"><INPUT
													TYPE="button" class=jianbian value="��  ��"
													onclick="javascript:query()" class=btn name="queryButton"
													id="queryButton"></td>
											</tr>
										</table>
									</td>
							</table>
						</form>
					</td>
				</tr>
				<tr>
					<td height="5"></td>
				</tr>
				<tr>
					<td><div id="querying"
							style="display:none;background-color: #E1EEEE;height: 20"></div></td>
				</tr>
				<tr>
					<td  id="tdData" style="display: none"></td>
				</tr>
				<tr>
					<td height="30"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<%@ include file="../../foot.jsp"%>