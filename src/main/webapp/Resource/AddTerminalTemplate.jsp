<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * ���������ն�ģ��
 *
 * @author Duangr(5250) tel��13770931606
 * @version 1.0
 * @since 2008-6-11 ����09:50:07
 *
 * ��Ȩ���Ͼ������Ƽ� ���ܿƼ���
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������ն�ģ��</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<style type="text/css">
</style>
<script language="JavaScript">
<!--
	var wanPort = null;
	var checkflag = true;// checkForm �õ��ı�־λ,��Ҫ��each����ʹ��
	var curWanPortNum = null; // ��ŵ�ǰ���֧��WAN����
	$(function(){
		var flag = "<s:property value='flag' />";
		var action = "<s:property value='action' />";
		if(action=="1"){
			if(flag == "1")
				alert("����ģ��ɹ�");
			else
				alert("����ģ��ʧ��");
		}
		wanPort = "<tr name=\"wanPort\">"+$("#infoTemplate").html()+"</tr>";
		wanPort = wanPort.replace(/_test_/g,"")
		//alert(wanPort);
	});

	// ����֤
	function CheckForm(){
		checkflag = true;
		if($("input[@name='template_name']").val()==""){
			alert("������ģ������");
			$("input[@name='template_name']").focus();
			return false;
		}
		if($("select[@name='vendor_id']").val()=="-1"){
			alert("��ѡ��������");
			$("select[@name='vendor_id']").focus();
			return false;
		}
		if($("select[@name='device_type']").val()=="-1"){
			alert("��ѡ�����ն�����");
			$("select[@name='device_type']").focus();
			return false;
		}
		if($("select[@name='access_style_id']").val()=="-1"){
			alert("��ѡ�����ʽ");
			$("select[@name='access_style_id']").focus();
			return false;
		}
		if($("input[@name='max_lan_num']").val()==""){
			alert("���������֧��LAN�˿���");
			$("input[@name='max_lan_num']").focus();
			return false;
		}
		if($("input[@name='max_ssid_num']").val()==""){
			alert("���������֧��SSID��");
			$("input[@name='max_ssid_num']").focus();
			return false;
		}
		if($("input[@name='max_wan_num']").val()==""){
			alert("���������֧��WAN�˿���");
			$("input[@name='max_wan_num']").focus();
			return false;
		}
		$("input[@name='addresspool']").each(function (){
			var obj = $(this);
			if(obj.val()==""){
				alert("������WAN�˿�֧������ַ��");
				obj.focus();
				checkflag = false;
				return false;
			}
		});
		if(checkflag == false)
			return false;
		$("select[@name='wan_type']").each(function (){
			var obj = $(this);
			if(obj.val()=="-1"){
				alert("��ѡ��WAN�˿����ӷ�ʽ");
				obj.focus();
				checkflag = false;
				return false;
			}
		});
		if(checkflag == false)
			return false;
		return true;
	}
	// ʧȥ�����¼�
	function showInfo(){
		try{
			var v = $("#max_wan_num").val();
			v = parseInt(v,10);
			//�����WAN�����뵱ǰWAN������ͬ,����Ҫ�����仯
			if(curWanPortNum == v)
				return;
			if(v>100){
				if(!confirm("�Ƿ��������?\n\n�������[���֧��WAN�˿���]�Ƚϴ�,���ᵼ������������ٶȼ���.\n�����������,���ļ����������ֹͣ��Ӧ.")){
					$("#max_wan_num").val(curWanPortNum);
					return;
				}
			}
			$("tr[@name='wanPort']").remove();
			if(v>0){
				for(var i=1;i<v+1;i++){
					$("#infoTemplate").before(wanPort.replace(/portNum/g,i));
				}
				curWanPortNum = v;
			}else{
				curWanPortNum = 0;
			}
		}catch(e){
			//alert(e);
		}
	}
	// ���ص�ģ���б�ҳ��
	function goList(){
		window.location="<s:url value='/Resource/terminalTemplate!showList.action' />";
	}
	// ���ƽ�����������
	function onlyNum(v){
		v.value=v.value.replace(/[^\d]/g,"");
	}
//-->
</script>
</head>
<body>
<form name="speclinefrm" action="<s:url value="/Resource/terminalTemplate.action"/>" onSubmit="return CheckForm();"  method ="post" >
	<table width="98%" border=0 align="center" cellpadding="0" cellspacing="0">
		<tr><td height=20>&nbsp;</td></tr>
		<tr>
			<td>
				<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">
							���������ն�ģ��
						</td>
						<td>
							<img src="../images/attention_2.gif" width="15" height="12">
							��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td bgcolor="#999999">
				<table border=0 cellspacing=1 cellpadding=1 width="100%">
					<tr>
						<th colspan="4" align="center">���������ն�ģ��
						</th>
					</tr>
					<tr>
						<td class="column" height="17" align="right" width="13%" nowrap>ģ������</td>
						<td class="column" height="17" width="29%">
							<input type="text" class="bk" name="template_name">
							<font color="#FF0000">*</font>
						</td>
						<td class="column" height="17" align="right" width="13%" nowrap>��������</td>
						<td class="column" height="17" width="29%">
							<select class="bk" name="vendor_id">
								<option value="-1">==��ѡ��==
								<s:iterator value="vendorList" var="vl"  status="status">
								<option value="<s:property value="#vl.vendor_id" />"><s:property value="#vl.vendor_name" />/<s:property value="#vl.vendor_add" />
								</s:iterator>
							</select>
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr>
						<td class="column" height="17" align="right" width="13%" nowrap>�����ն�����</td>
						<td class="column" height="17" width="29%">
							<select class="bk" name="device_type">
								<option value="-1">==��ѡ��==
								<option value="Navigator1-1">Navigator1-1
								<option value="Navigator1-2">Navigator1-2
								<option value="Navigator2-1">Navigator2-1
								<option value="Navigator2-2">Navigator2-2
							</select>
							<font color="#FF0000">*</font>
						</td>
						<td class="column" height="17" align="right" width="13%" nowrap>����ʽ</td>
						<td class="column" height="17" width="29%">
							<select class="bk" name="access_style_id">
								<option value="-1">==��ѡ��==
								<option value="0">ADSL����
								<option value="1">LAN����
							</select>
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr>
						<td class="column" height="17" align="right" width="13%" nowrap>���֧��LAN�˿���</td>
						<td class="column" height="17" width="29%">
							<input type="text" class="bk" name="max_lan_num" onkeyup="onlyNum(this);" >
							<font color="#FF0000">*</font>
						</td>
						<td class="column" height="17" align="right" width="13%" nowrap>���֧��SSID��</td>
						<td class="column" height="17" width="29%">
							<input type="text" class="bk" name="max_ssid_num"  onkeyup="onlyNum(this);">
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr>
						<td class="column" height="17" align="right" width="13%" nowrap>���֧��WAN�˿���</td>
						<td class="column" height="17" colspan="3" >
							<input type="text" class="bk" name="max_wan_num" id="max_wan_num" onblur="showInfo();"  onkeyup="onlyNum(this);">
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr style="display:none" id="infoTemplate">
						<td class="column" height="17" align="right" width="13%" nowrap>WAN�˿�portNum֧������ַ��</td>
						<td class="column" height="17" width="29%">
							<input type="text" class="bk" name="addresspool_test_"  onkeyup="onlyNum(this);">
							<font color="#FF0000">*</font>
						</td>
						<td class="column" height="17" align="right" width="13%" nowrap>WAN�˿�portNum���ӷ�ʽ</td>
						<td class="column" height="17" width="29%">
							<select class="bk" name="wan_type_test_">
								<option value="-1">==��ѡ��==
								<option value="0">�Ž�
								<option value="1">·��
								<option value="2">��̬IP
								<option value="3">DHCP
							</select>
							<font color="#FF0000">*</font>
						</td>
					</tr>
					<tr>
						<td colspan="4" align="center" class="green_foot">
							<input type="submit" value=" �� �� " class="btn"> &nbsp;&nbsp;
							<input type="reset" value=" �� д " class="btn"> &nbsp;&nbsp;
							<input type="button" value="�����б�" onclick="goList();" class="btn">
							<input type="hidden" name="action" value="1">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
</body>
</html>
