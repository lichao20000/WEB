<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>

<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * ��������ʱ��ȡ�豸
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-9 PM02:18:24
 *
 * @��Ȩ �Ͼ����� ���ܲ�Ʒ��;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������<s:property value="pmeeflg?'����':'����'" /></title>

<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">

<script type="text/javascript">

	var gw_type = '<s:property value="gw_type"/>';

	$(function(){
		init();
		//ѡ���ѯ��ʽ
		$("input[@name='sel_type']").click(function(){
			SelType($(this).val());
		});
		//ѡ����
		$("select[@name='vendor_id']").change(function(){
			if($(this).val()!=""){
				$("select[@name='device_model']").html("<option value=''>���ڻ�ȡ�豸�ͺ�...</option>");
				$.post(
					"<s:url value="/performance/configPmee!getDevModelByVendor.action"/>",
					{
						vendor_id:$(this).val()
					},
					function(data){
						data="<option value=''>��ѡ��</option>"+data;
						$("select[@name='device_model']").html(data);
					}
				);
			}
		});

	});
	//��ѯ��ʽ
	function SelType(type){
		if(type==1){
			$("tr[name='tr_model']").show();
			$("tr[@name='tr_name']").hide();
		}else{
			$("tr[name='tr_model']").hide();
			$("tr[@name='tr_name']").show();
		}
	}

	//��ѯ�豸
	function Query(){
		var type=$("input[@name='sel_type'][@checked]").val();
		var pmeeflg="<s:property value="pmeeflg"/>";
		if(type==1){//�����̲�ѯ
			$.SelDevByModel(pmeeflg, gw_type);
		}else{//���豸IP��ѯ
			$.SelDevByName(pmeeflg, gw_type);
		}
	}

	//���õ������ܱ��ʽ
	function ConSPmee(device_id){
		var url="<s:url value="/performance/configPmee.action"/>"+"?isbatch=false&device_id="+device_id;
		window.open(url,"��������");
	}
	//���õ�������
	function ConSFlux(device_id){
		var url="<s:url value="/performance/configFlux.action"/>?isbatch=false&device_id="+device_id;
		window.open(url,"��������");
	}
	//��������
	function ConfigFlux(){
		var device_id="";
		$("input[@name='chk'][checked]").each(function(){
			device_id+=","+$(this).val();
		});
		if(device_id==""){
			alert("��ѡ����Ҫ���õ��豸!");
			return false;
		}
		var url="<s:url value="/performance/configFlux.action"/>?isbatch=true&device_id="+device_id.substring(1);
		window.open(url);
	}
	//��������
	function ConfigPmee(){
		var device_id="";
		$("input[@name='chk'][checked]").each(function(){
			device_id+=","+$(this).val();
		});
		if(device_id==""){
			alert("��ѡ����Ҫ���õ��豸!");
			return false;
		}
		var url="<s:url value="/performance/configPmee.action"/>?isbatch=true&device_id="+device_id.substring(1);
		window.open(url);
	}
	//��ʼ��
	function init(){

	}
</script>
</head>
<body>
<br>
<table width="94%" height="30" border="0" cellspacing="0"
	cellpadding="0" class="green_gargtd" align="center">
	<tr>
		<s:if test="pmeeflg">
			<td width="162" align="center" class="title_bigwhite">������������</td>
		</s:if>
		<s:else>
			<td width="162" align="center" class="title_bigwhite">������������</td>
		</s:else>
	</tr>
</table>

<table width="94%" align="center" class="querytable">

	<tr>
		<th colspan="4" class="title_1">�豸��ѯ</th>
	</tr>
	<tr>
		<td class="title_2" width="15%" align="right">��ѯ��ʽ��</td>
		<td colspan="3" align="left" width="85%">
			<input type="radio" name="sel_type" value="1" id="sel_1" checked>
			<label for="sel_1" onclick="SelType(1)">�߼���ѯ</label>

			<input type="radio" name="sel_type" value="2" id="sel_2" style="margin-left: 80px;" >
			<label for="sel_2" onclick="SelType(2)">���豸��ѯ</label>

		</td>
	</tr>
	<tr name="tr_name" style="display: none;">
		<td class="title_2" width="15%" align="right">�豸���кţ�</td>
		<td width="35%"><input type="text" name="device_name"></td>
		<td class="title_2" width="15%" align="right">�豸IP��</td>
		<td width="35%"><input type="text" name="loopback_ip"></td>
	</tr>

	<tr name="tr_model">
		<td class="title_2" width="15%" align="right">���أ�</td>
		<td colspan="3" width="85%">
			<select name="city_id" style="width:150px">
			<option value="">��ѡ��</option>
			<s:iterator var="clist" value="CityList">
				<option value="<s:property value="#clist.city_id"/>"><s:property
					value="#clist.city_name" /></option>
			</s:iterator>
		</select></td>
	</tr>

	<tr name="tr_model">
		<td class="title_2" width="15%" align="right">�豸���̣�</td>
		<td width="35%">
			<select name="vendor_id" style="width:150px">
			<option value="">��ѡ��</option>
			<s:iterator var="vlist" value="VendorList">
				<option value="<s:property value="#vlist.vendor_id"/>"><s:property
					value="#vlist.vendor_add" /></option>
			</s:iterator>
		</select></td>
		<td class="title_2" width="15%" align="right">�豸�ͺţ�</td>
		<td width="35%">
			<select name="device_model" style="width:150px">
				<option value="">����ѡ���̣�</option>
			</select>
		</td>
	</tr>

	<tr>
		<td align="right" class="foot" colspan="4">
		<button onclick="Query()">&nbsp;��&nbsp;ѯ&nbsp;</button>
		</td>
	</tr>
</table>
<br>
<table width="94%" align="center" class="listtable">
	<thead>
		<tr>
			<th colspan="6">�豸�б�</th>
		</tr>
		<tr>
			<th width="5%"><input type="checkbox" id="chkall"><label
				for="chkall"></label></th>
			<th>����</th>
			<th>�豸����</th>
			<th>�豸IP</th>
			<th>�豸���к�</th>
			<th>����</th>
		</tr>
	</thead>
	<tbody name="tbody">
	</tbody>
	<tfoot>
		<tr>
			<td colspan="6" align="right">
				<s:if test="pmeeflg">
					<button onclick="ConfigPmee()">��������</button>
				</s:if>
				<s:else>
					<button onclick="ConfigFlux()">��������</button>
				</s:else>
			</td>
		</tr>
	</tfoot>
</table>
</body>
</html>
