<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�����û�������Ϣ��ѯ</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<SCRIPT LANGUAGE="JavaScript">
	/** add by chenjie 2011.4.22 **/
	function block() {
		$.blockUI({
			overlayCSS : {
				backgroundColor : '#CCCCCC',
				opacity : 0.6
			},
			message : "<font size=3>���ڲ��������Ժ�...</font>"
		});
	}

	function unblock() {
		$.unblockUI();
	}
	/*------------------------------------------------------------------------------
	 //������:		��ʼ��������ready��
	 //����  :	��
	 //����  :	��ʼ�����棨DOM��ʼ��֮��
	 //����ֵ:		
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	$(function() {
		<%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
		$("TR[@id='biaotou1']").css("display","none");
		<%}%>
		var queryType = "<s:property value="queryType"/>";
		var queryResultType = "<s:property value="queryResultType"/>";
		if ("" != queryType && null != queryType) {
			$("input[@name='queryType']").val(queryType);
		}
		if ("" != queryResultType && null != queryResultType) {
			$("input[@name='queryResultType']").val(queryResultType);
		}
		$("input[@name='startLastTime']").val("");
		$("input[@name='endLastTime']").val("");
	});

	/*------------------------------------------------------------------------------
	 //������:		checkip
	 //����  :	str �������ַ���
	 //����  :	���ݴ���Ĳ����ж��Ƿ�Ϊ�Ϸ���IP��ַ
	 //����ֵ:		true false
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function checkip(str) {
		var pattern = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
		return pattern.test(str);
	}

	/*------------------------------------------------------------------------------
	 //������:		trim
	 //����  :	str �������ַ���
	 //����  :	���ݴ���Ĳ�������ȥ�����ҵĿո�
	 //����ֵ:		trim��str��
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function trim(str) {
		if(str){
			return str.replace(/(^\s*)|(\s*$)/g, ""); 
		}else{
			return "";
		}
	}

	/*------------------------------------------------------------------------------
	 //������:		queryChange
	 //����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
	 //����  :	���ݴ���Ĳ���������ʾ�Ľ���
	 //����ֵ:		��������
	 //˵��  :	
	 //����  :	Create 2009-12-25 of By qxq
	 ------------------------------------------------------------------------------*/
	function queryDevice() 
	{
		 if('hn_lt'!=trim($("input[@name='instArea']").val()))
		 {
			var username = trim($("input[@name='username']").val());
			var servAccount = trim($("input[@name='servAccount']").val());
			var deviceSerialnumber = trim($("input[@name='deviceSerialnumber']")
					.val());
			var loopbackIp = trim($("input[@name='loopbackIp']").val());
			if ("" != username) {
				$("input[@name='username']").val(username);
			}
			if ("" != deviceSerialnumber && deviceSerialnumber.length < 6) {
				alert("�������������6λ��");
				return false;
			} else {
				$("input[@name='deviceSerialnumber']").val(deviceSerialnumber);
			}
			if ("" != loopbackIp && !checkip(loopbackIp)) {
				alert("��������ȷ��IP��ַ��");
				return false;
			} else {
				$("input[@name='loopbackIp']").val(loopbackIp);
			}
		 }
		 if('jx_dx'!=trim($("input[@name='instArea']").val()))
		 {
			 var loopbackIpSix = trim($("input[@name='loopbackIpSix']").val());
			 if ("" != loopbackIpSix) {
			 	$("input[@name='loopbackIpSix']").val(loopbackIpSix);
			 }
		 }
		document.selectForm.submit();
	}

	function reset() {
		document.selectForm.username.value = "";
		document.selectForm.deviceSerialnumber.value = "";
		document.selectForm.servAccount.value = "";
		document.selectForm.cityId.value = "00";
		document.selectForm.loopbackIp.value = "";
		document.selectForm.addressingType.value = "-1";
		document.selectForm.startLastTime.value = "";
		document.selectForm.endLastTime.value = "";
		document.selectForm.status.value = "-1";
		document.selectForm.cpeMac.value = "";
	}

	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for (var i = 0; i < iframeids.length; i++) {
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
						dyniframe[i].height = dyniframe[i].document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block";
			}
		}
	}
	/* $(function() {
		dyniframesize();
	}); */

	/* $(window).resize(function() {
		dyniframesize();
	}); */
</SCRIPT>
</head>
<body>
	<form name="selectForm" method="post"
		action="<s:url value="/gtms/stb/resource/gwDeviceQueryStb!zeroqueryDeviceList.action"/>"
		target="dataForm">
		<input type="hidden" name="queryType" value="2" />
		<input type="hidden" name="queryResultType" value="none" />
		<input type="hidden" name="type" value="<s:property value='type'/>" />
		<input type="hidden" name="instArea" value="<s:property value='instArea'/>" />
		<TABLE width="98%" align="center" class="querytable">
			<TR>
				<TD><IMG height=20 src="<s:url value='/images/bite_2.gif'/>"
					width=24> <s:if test="type==0">
            	����ǰ��λ�ã������в�ѯͳͳ��
            </s:if> <s:else>
            	����ǰ��λ�ã������в�ѯͳͳ��(��ɾ��)
            </s:else></TD>
			</TR>
		</TABLE>
		<TABLE width="98%" class="querytable" align="center">
			<tr>
				<th colspan="4" id="thTitle" class="title_1">�����û�������Ϣ��ѯ</th>
			</tr>
			<s:if test="instArea=='hn_lt'">
			<TR id="tr21" STYLE="display:">
				<TD width="10%" class="title_2">ҵ���˺�</TD>
				<TD width="40%">
					<input type="text" name="servAccount" value=""
						size="20" maxlength="40" class="bk" />
				</TD>
				<TD width="10%" class="title_2">�豸���к�</TD>
				<TD width="40%">
					<input type="text" name="deviceSerialnumber"
						value="" size="20" maxlength="40" class="bk" />
				</TD>
			</TR>
			<TR id="tr22" STYLE="display:">
				<TD width="10%" class="title_2">�豸MAC��ַ</TD>
				<TD width="40%">
					<input type="text" name="cpeMac" value=""
						size="23" maxlength="23" class="bk" />
				</TD>
				<TD width="10%" class="title_2">�豸����</TD>
				<TD width="40%">
					<select name="cityId">
						<option value="">==��ѡ��==</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id"/>">
								<s:property value="city_name" />
							</option>
						</s:iterator>
					</select>
				</TD>
			</TR>
			<TR id="tr23" STYLE="display:">
				<TD width="10%" class="title_2">�״��ϱ�ʱ��</TD>
				<TD width="40%"><input type="text" name="startLastTime"
					readonly class=bk value="<s:property value="startLastTime" />">
					<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.startLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../../images/dateButton.png" width="13" height="12" border="0"
					alt="ѡ��">&nbsp;~&nbsp; <input type="text" name="endLastTime"
					readonly class=bk value="<s:property value="endLastTime" />">
					<img name="shortDateimg"
					onClick="WdatePicker({el:document.selectForm.endLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
					src="../../../images/dateButton.png" width="13" height="12" border="0"
					alt="ѡ��"></TD>
				<TD width="10%" class="title_2">���һ���ϱ�IP</TD>
				<TD width="40%"><input type="text" name="loopbackIp" value=""
					size="20" maxlength="40" class="bk" /></TD>
			</TR>
			</s:if>
			<s:else>
				<s:if test="instArea=='sx_lt'">
					<TR id="tr21" STYLE="display:">
						<TD width="10%" class="title_2">�豸���к�</TD>
						<TD width="40%">
							<input type="text" name="deviceSerialnumber" 
							value="" size="20" maxlength="40" class="bk" />
						</TD>
						<TD width="10%" class="title_2">�豸״̬</TD>
						<TD width="40%">
							<s:select list="statusList" name="status"
								headerKey="-1" headerValue="===��ѡ��===" listKey="status_id"
								listValue="status_name" value="status_id" cssClass="bk"
								theme="simple">
							</s:select>
						</TD>
					</TR>
				</s:if>
				<s:else>
					<TR id="tr21" STYLE="display:">
						<TD width="10%" class="title_2">����˺�</TD>
						<TD width="40%"><input type="text" name="username" value=""
							size="20" maxlength="20" class="bk" /></TD>
						<TD width="10%" class="title_2">�豸���к�</TD>
						<TD width="40%"><input type="text" name="deviceSerialnumber"
							value="" size="20" maxlength="40" class="bk" /></TD>
					</TR>
				</s:else>
			<TR id="tr22" STYLE="display:">
				<TD width="10%" class="title_2">ҵ���˺�</TD>
				<TD width="40%"><input type="text" name="servAccount" value=""
					size="20" maxlength="40" class="bk" /></TD>
				<TD width="10%" class="title_2">�豸����</TD>
				<TD width="40%"><select name="cityId" class="bk">
						<option value="">===��ѡ��===</option>
						<s:iterator value="cityList">
							<option value="<s:property value="city_id"/>">
								<s:property value="city_name" />
							</option>
						</s:iterator>
				</select></TD>
			</TR>

			<TR id="tr22" STYLE="display:">
				<s:if test="instArea=='jx_dx'">
				<TD width="10%" class="title_2">���һ���ϱ�IP(IPV4��ַ)</TD>
				</s:if>
				<s:else>
				<TD width="10%" class="title_2">���һ���ϱ�IP</TD>
				</s:else>
				<TD width="40%"><input type="text" name="loopbackIp" value=""
					size="20" maxlength="40" class="bk" /></TD>
				<TD width="10%" class="title_2">��������</TD>
				<TD width="40%"><select name="addressingType"
					id="addressingType" class="bk">
						<option value="-1">===��ѡ��===</option>
						<option value="PPPoE">PPPoE</option>
						<option value="DHCP">DHCP</option>
				</select></TD>
			</TR>

			<s:if test="instArea=='sx_lt'">
				<TR id="tr23" STYLE="display:">
					<TD width="10%" class="title_2">
						�״��ϱ���ʼʱ��
					</TD>
					<TD width="40%">
						<lk:date id="startTime" name="startLastTime" defaultDate="%{startLastTime}" />
					</TD>
					<TD width="10%" class="title_2">
						�״��ϱ�����ʱ��
					</TD>
					<TD width="40%">
						<lk:date id="endTime" name="endLastTime" defaultDate="%{endLastTime}"/>
					</TD>
				</TR>
				<tr>
					<TD width="10%" class="title_2">�豸MAC��ַ</TD>
					<TD width="40%"><input type="text" name="cpeMac" value="" 
						size="23" maxlength="23" class="bk" /></TD>
					<td></td>
					<td></td>
				</tr>
			</s:if>
			<s:else>
				<TR id="tr23" STYLE="display:">
					<TD width="10%" class="title_2">�״��ϱ�ʱ��</TD>
					<TD width="40%"><input type="text" name="startLastTime"
						readonly class=bk value="<s:property value="startLastTime" />">
						<img name="shortDateimg"
						onClick="WdatePicker({el:document.selectForm.startLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="13" height="12" border="0"
						alt="ѡ��">&nbsp;~&nbsp; <input type="text" name="endLastTime"
						readonly class=bk value="<s:property value="endLastTime" />">
						<img name="shortDateimg"
						onClick="WdatePicker({el:document.selectForm.endLastTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="13" height="12" border="0"
						alt="ѡ��"></TD>
					<TD width="10%" class="title_2">�豸MAC��ַ</TD>
					<TD width="40%"><input type="text" name="cpeMac" value="" 
						size="23" maxlength="23" class="bk" /></TD>
				</TR>
			</s:else>
			
			<TR id="biaotou1"  STYLE="display:">
				<s:if test="instArea=='jx_dx'">
					<TD width="10%" class="title_2">���һ���ϱ�IP(IPV6��ַ)</TD>
					<TD width="40%"><input type="text" name="loopbackIpSix" value=""
					size="20" maxlength="40" class="bk" /></TD>
				</s:if>
				<s:else>
					<s:if test="instArea!='sx_lt'">
						<TD width="10%" class="title_2">�豸״̬</TD>
						<TD width="40%">
							<s:select list="statusList" name="status"
								headerKey="-1" headerValue="===��ѡ��===" listKey="status_id"
								listValue="status_name" value="status_id" cssClass="bk"
								theme="simple">
							</s:select>
						</TD>
					</s:if>
				</s:else>
				<s:if test="instArea!='sx_lt'">
					<TD width="10%" class="title_2">ʧ��ԭ��</TD>
					<TD width="40%">
						<select name="failReason">
							<option value="-1">===��ѡ��===</option>
							<s:iterator value="failReasonList">
								<option value="<s:property value="reason_id"/>">
								<s:property value="reason_desc" />
							</option>
							</s:iterator>
						</select>
					</TD>
				</s:if>
			</TR>
			 <%if("hb_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
			<TR id="tr22" STYLE="display:">
				<TD width="10%" class="title_2">�ն�״̬</TD>
				<TD width="40%">
				<select name="device_status"
					id="device_status" class="bk">
						<option value="-1">ȫ��</option>
						<option value="1">��ȷ��</option>
						<option value="0">δȷ��</option>
				</select></TD>
				<TD width="10%" class="title_2"></TD>
				<TD width="40%"></TD>
			</TR>
			 <%} %> 
			</s:else>
			
			<tr align="right">
				<td colspan="4" class="foot" align="right">
					<div class="right">
						<!-- <input type="button" onclick="javascript:queryDevice()" align="right" class=jianbian
							name="queryButton" value=" �� ѯ " /> -->
						<button onclick="javascript:queryDevice()" align="right" class=jianbian> �� ѯ </button>
					</div>
				</td>
			</tr>
		</TABLE>
		<br>
		<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
	</form>
</body>
<style>
	.querytable input[type=text],.querytable select{ width: 160px; }
	.querytable #startTime,.querytable #endTime{ background-color: #f1f1f1; }
</style>
<br>
<br>