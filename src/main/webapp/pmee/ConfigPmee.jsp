<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * ���òɼ���
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-mail:BENYP@LIANCHUANG.COM
 * @version 1.0
 * @since 2008-5-22 11:07:41
 *
 * ��Ȩ���Ͼ������Ƽ� ���ܿƼ���
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���òɼ���</title>
<script type="text/javascript"  src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"  src="<s:url value="/pmee/jquery.PmeeConfig.js"/>"></script>
<script type="text/javascript">
	//ҳ���ʼ��
	$(function(){
		//��ѯ��ʽ�л�
		$("input[@name='checkType']").click(function(){
			$.ToggleQuery($(this).val());
		});
		//����change�¼�
		$("select[@name='city_id']").change(function(){
			$("select[@name='vendor_id']").val("");
			if($(this).val()==""){
				alert("��ѡ������!");
				$(this).select();
				$(this).focus();
				return false;
			}
		});
		//����change�¼�
		$("select[@name='vendor_id']").change(function(){
			$("select[@name='device_model']").val("");
			if($(this).val()==""){
				alert("��ѡ����!");
				$(this).focus();
				return;
			}
			$.VendorChange($(this).val());
			$.post(
				"<s:url value="/pmee/DevPmeeConfig!getExpression.action"/>",
				{
					vendor_id:"'"+$(this).val()+"'"
				},
				function(data){
					$("select[@name='exp_name']").html(data);
				}
			);

		});
		//�豸�ͺ�change�¼�
		$("select[@name='device_model']").change(function(){
			$("select[@name='version']").val("");
			if($(this).val()==""){
				alert("��ѡ���豸�ͺ�!");
				$(this).focus();
				return;
			}
			$.ModelChange($("select[@name='vendor_id']").val(),$(this).val());
		});
		//�豸�汾change�¼�
		$("select[@name='version']").change(function(){
			$("#div_device span").html("");
			if($(this).val()==""){
				alert("��ѡ���豸�汾!");
				$(this).focus();
				return;
			}
			$.VersionChange($("select[@name='city_id']").val(),$("select[@name='vendor_id']").val(),$(this).val());
		});
		//���û���ѯ
		$("input[@name='btn_user']").click(function(){
			if($.trim($("input[@name='username']").val())=="" && $.trim($("input[@name='phone']").val())==""){
				alert("�������û�����绰�����ѯ!");
				$("input[@name='username']").focus();
				return false;
			}
			$.post(
				"<s:url value="/pmee/DevPmeeConfig!getDevByUser.action"/>",
				{
					username:$("input[@name='username']").val(),
					phone:$("input[@name='phone']").val()
				},
				function(data){
					$("#div_device span").html(data);
				}
			);
		});
		//��IP��ѯ
		$("input[@name='btn_ip']").click(function(){
			if($.trim($("input[@name='serial']").val())=="" && $.trim($("input[@name='ip']").val())==""){
				alert("�������豸���кŻ��豸IP��ѯ!");
				$("input[@name='serial']").focus();
				return false;
			}
			$.post(
				"<s:url value="/pmee/DevPmeeConfig!getDevByIP.action"/>",
				{
					serial:$("input[@name='serial']").val(),
					ip:$("input[@name='ip']").val()
				},
				function(data){
					$("#div_device span").html(data);
				}
			);
		});
		//����
		$("input[@name='save']").click(function(){
			if($("input[@name='device'][@checked]").length<=0){
				alert("��ѡ���豸!");
				return false;
			}else if($("select[@name='exp_name']").val()==""){
				alert("��ѡ�����ܱ��ʽ!");
				$("select[@name='exp_name']").focus();
				return false;
			}else if($.trim($("input[@name='samp_distance']").val())==""){
				alert("������������!");
				$("input[@name='samp_distance']").focus();
				return false;
			}
			var device_id=getDevID();
			$.TestConfig($("select[@name='exp_name'] option[@selected]").html(),$("select[@name='exp_name']").val(),device_id);
		});
		//��ѯ���ý��
		$("input[@name='query']").click(function(){
			if($("input[@name='device'][@checked]").length<=0){
				alert("��ѡ���豸!");
				return false;
			}
			$("table[@name='result']").show();
			$("tbody[@name='tbody']").html("<tr bgcolor='#FFFFFF'><td colspan='8'>������������,��ȴ�......</td></tr>");
			$.post(
					"<s:url value="/pmee/DevPmeeConfig!getConfigResult.action"/>",
				{
					device_id:getDevID()
				},
				function(data){
					$("tbody[@name='tbody']").html(data);
				}
			);
		});
		$("input[@name='chk_all']").click(function(){
			var chk=$(this).attr("checked");
			chk=typeof(chk)=="undefined"?false:chk;
			$("input[@name='device']").each(function(){
				$(this).attr("checked",chk);
			});
		});
	});
	//��ȡѡ���豸���豸���к�
	function getSerial(){
		var sel="";
		$("input[@name='device'][@checked]").each(function(){
			sel+="-/-"+$(this).attr("dev_serial");
		});
		return sel==""?"":sel.substring(3);
	}
	//��ȡѡ���豸��ID
	function getDevID(){
		var did="";
		$("input[@name='device'][@checked]").each(function(){
			did+=",'"+$(this).val()+"'";
		});
		return did==""?"":did.substring(1);
	}
	//��ȡ�豸OUI
	function getDevOUI(){
		var did="";
		$("input[@name='device'][@checked]").each(function(){
			did+=",'"+$(this).attr("oui")+"'";
		});
		return did==""?"":did.substring(1);
	}
	//��ȡ���ܱ��ʽ
	function getExp(company){
		if($("input[@name='checkType'][@checked]").val()=="0"){
			return false;
		}
		$.post(
			"<s:url value="/pmee/DevPmeeConfig!getExpression.action"/>",
			{
				vendor_id:getDevOUI()
			},
			function(data){
				$("select[@name='exp_name']").html(data);
			}
		);
	}
	//ɾ�����ܱ��ʽ
	function Del(device_id,expressionid,target){
		if(window.confirm("ȷ��ɾ��?")){
			$.post(
				"<s:url value="/pmee/DevPmeeConfig!DelExpressionID.action"/>",
				{
					device_id:device_id,
					expressionid:expressionid
				},
				function(data){
					if(eval(data)==true){
						alert("ɾ���ɹ�");
						target.parent().parent().remove();
					}else{
						alert("ɾ��ʧ��");
					}
				}
			);
		}
	}
</script>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">

</head>
<body>
	<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td class=column1>
			<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162">
									<div align="center" class="title_bigwhite">�豸����</div>
								</td>
								<td>
									<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
									<input type="radio" value="0" name="checkType" checked id="c_0"><label for="c_0">�����غ��ͺ�</label>&nbsp;&nbsp;
									<input type="radio" value="1" name="checkType" id="c_1"><label for="c_1">���û�</label>&nbsp;&nbsp;
									<input type="radio" value="2" name="checkType" id="c_2"><label for="c_2">���豸</label>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<th colspan=4>
						��������
					</th>
				</tr>
				<tr>
				<td bgcolor="#FFFFFF">
					<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
						<tr bgcolor="#FFFFFF" id="tr1">
							<td align="right" width="10%">����:</td>
							<td align="left" width="30%">
								<select name="city_id" class="bk">
									<option value="" >==��ѡ��==</option>
									<s:iterator var="citylist" value="CityList">
										<option value="<s:property value="#citylist.city_id"/>">
											==<s:property value="#citylist.city_name"/>==
										</option>
									</s:iterator>
								</select>
							</td>
							<td align="right" width="10%">����:</td>
							<td align="left" width="30%">
								<select name="vendor_id" class="bk">
									<option value="">==��ѡ��==</option>
									<s:iterator var="vendorlist" value="VendorList">
										<option value="<s:property value="#vendorlist.vendor_id"/>">
											==<s:property value="#vendorlist.vendor_add+'('+#vendorlist.vendor_id+')'"/>==
										</option>
									</s:iterator>
								</select>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF" id="tr2">
							<td align="right" width="10%">�豸�ͺ�:</td>
							<td width="30%">
								<select name="device_model" class="bk">
									<option value="">--����ѡ����--</option>
								</select>
							</td>
							<td align="right" width="10%">�豸�汾:</td>
							<td width="30%">
								<select name="version" class="bk">
									<option value="">--����ѡ���豸�ͺ�--</option>
								</select>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF" id="tr3" style="display:none">
							<td align="right" width="10%">�û���:</td>
							<td width="30%">
								<input type="text" name="username" value="" class=bk>
							</td>
							<td align="right" width="10%">�û��绰����:</td>
							<td width="30%">
								<input type="text" name="phone" value="" class=bk>
								<input type="button" name="btn_user" class=btn value="��ѯ">
							</td>
						</tr>
						<tr bgcolor="#FFFFFF" id="tr4" style="display:none">
							<td align="right" width="10%">�豸���к�:</td>
							<td width="30%">
								<input type="text" name="serial" value="" class=bk>
							</td>
							<td align="right" width="10%">�豸������IP:</td>
							<td width="30%">
								<input type="text" name="ip" value="" class=bk>
								<input type="button" name="btn_ip" class=btn value=" �� ѯ ">
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td align="right" width="10%">�豸�б�: <br><label for="chk_all_id">ȫѡ</label><input id="chk_all_id" type="checkbox" name="chk_all"></td>
							<td colspan="3">
								<div id="div_device" style="width: 95%; height: 100px; z-index: 1; top: 100px; overflow: scroll">
									<span>��ѡ���豸��</span>
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
			<tr>
				<th colspan=4>
					�������ò���
				</th>
			</tr>
			<tr>
				<td bgcolor="#FFFFFF">
					<table width="100%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
						<tr>
							<td class=column1 align=center width="100">���ܱ��ʽ</td>
							<td class=column2 align=left width="200" colspan="3">
								<div id="pmdiv">
									<select name="exp_name" onchange="Pm_Name()" class="bk">
										<option value="">��ѡ����豸�����ܱ��ʽ</option>
									</select>
								</div>
							</td>
						  </tr>
						  <tr>
							<td class="column1" align="center" width="100">�������</td>
							<td class="column2" align="left">
								<input name="samp_distance" type="text" class="bk" value="900" style="width:100">
							</td>
							<td class="column1" align="center" width="100">ԭʼ�����Ƿ����</td>
							<td class="column2" align="left">
								<select name="ruku" class="bk">
							  		<option value="0">��</option>
							  		<option value="1">��</option>
								</select>
							</td>
						  </tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height="20"></td>
			</tr>
			<tr>
				<td align="center">
					<input type="button" name="save" value=" �� �� ">&nbsp;&nbsp;
					<input type="button" name="query"  value="�鿴���ý��">
				</td>
			</tr>
	</table>
	<tr>
		<td height="20"></td>
	</tr>
	<table name="result" width="98%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text" style="display:none;">
			<tr>
				<th colspan=8>�豸�������ý��</th>
			</tr>
			<tr>
				<th>�豸����</th>
				<th>�豸���к�</th>
				<th>���ʽ����</th>
				<th>����</th>
				<th>�������</th>
				<th>���ý��</th>
				<th>ʧ��ԭ������</th>
				<th>����</th>
			</tr>
			<tbody name="tbody">

			</tbody>
		</table>
	</td>
</tr>
</table>
</body>
</html>
