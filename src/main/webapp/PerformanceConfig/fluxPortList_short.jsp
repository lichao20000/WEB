<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
/**
 * �����˿��б�;
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-28 ??06:13:02
 *
 * @��Ȩ �Ͼ���������Ƽ� ���ܲ�Ʒ��;
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title><s:property value="device_name"/>��<s:property value="loopback_ip"/>���˿��б�</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript">
	//ɾ���˿�
	function delPort(){
		if($("input[@name='chk'][@checked]").length<1){
			alert("������ѡ��һ���˿ڣ�");
			return false;
		}
		var chk="";
		$("input[@name='chk'][@checked]").each(function(){
			chk+="-/-"+$(this).val();
		});
		chk=chk.substring(3);
		$.post(
			"<s:url value="/performance/configFlux!delPort.action"/>",
			{
				device_id:"<s:property value="device_id"/>",
				port_info:chk
			},
			function(data){
				if(data=="0"){
					alert("ɾ���ɹ���");
				}else if(data=="-1"){
					alert("ɾ��ʧ�ܣ�");
				}else if(data=="-2"){
					alert("����ɾ���ɹ���֪ͨ��̨ʧ�ܣ�");
				}else{
					alert("��ʱ�������ԣ�");
				}
				window.location=window.location;
			}
		);
	}
	//�༭�˿�
	function EditPort(){
		if($("input[@name='chk'][@checked]").length<1){
			alert("������ѡ��һ���˿ڣ�");
			return false;
		}
		var chk="";
		$("input[@name='chk'][@checked]").each(function(){
			chk+="-/-"+$(this).val();
		});
		chk=chk.substring(3);
		var url="<s:url value="/performance/configFlux!EditPort.action"/>?device_id=<s:property value="device_id"/>&port_info="
		+chk+"&device_name=<s:property value="device_name"/>&loopback_ip=<s:property value="loopback_ip"/>"+"&t="+new Date();
		window.open(url);
	}
</script>
<style type="text/css">
</style>
</head>
<body>
	<form action="">
		<br>
		<table width="98%" align="center" class="listtable">
			<thead>
				<tr>
					<th colspan="11"><s:property value="device_name"/>��<s:property value="loopback_ip"/>���˿��б�</th>
				</tr>
				<tr>
					<th>ѡ��</th>
					<th>�˿�����</th>
					<th>�˿�����</th>
					<th>�˿�����</th>
					<th>�˿ڱ���</th>
					<th>�˿�IP</th>
					<th>�˿ڱ�ʶ��ʽ</th>
					<th>�ɼ����(��)</th>
					<th>�Ƿ����</th>
					<th>Snmp�汾</th>
					<th>�������</th>
				</tr>
			</thead>
			<tbody>
				<s:if test="cfgPortList==null || cfgPortList.size()==0">
					<tr>
						<td colspan="11">û�ж�Ӧ�Ķ˿�����</td>
					</tr>
				</s:if>
				<s:iterator var="cpl" value="cfgPortList" status="rowstatus">
					<tr class="<s:property value="#rowstatus.odd==true?'odd':'even'"/>"
						onmouseover="className='odd'"
						onmouseout="className='<s:property value="#rowstatus.odd==true?'odd':'even'"/>'"
					>
						<td>
							<input type="checkbox" name="chk" value="<s:property value="#cpl.getway+'|||'+#cpl.port_info"/>">
						</td>
						<td><s:property value="#cpl.ifindex"/></td>
						<td><s:property value="#cpl.ifdescr"/></td>
						<td><s:property value="#cpl.ifname"/></td>
						<td><s:property value="#cpl.ifnamedefined"/></td>
						<td><s:property value="#cpl.ifportip"/></td>
						<td><s:property value="#cpl.getway+':'+#cpl.getwayStr"/></td>
						<td><s:property value="#cpl.polltime"/></td>
						<td><s:property value="#cpl.intodb"/></td>
						<td><s:property value="#cpl.snmpversion"/></td>
						<td><s:property value="#cpl.ifconfigflag"/></td>
					</tr>
				</s:iterator>
			</tbody>
			<tfoot>
				<tr>
					<td colspan="11" align="right">
						<button onclick="EditPort();">�� ��</button><button onclick="delPort();">ɾ ��</button><button onclick="window.close();">�� ��</button>
					</td>
				</tr>
			</tfoot>
		</table>
	</form>
</body>
</html>
