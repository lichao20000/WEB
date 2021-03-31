<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�澯��������ʾ����</title>
		<%
			/**
			 * WebTopoʵʱ�澯�Ƹ澯������ҳ��
			 * <li>REQ: GZDX-REQ-20080402-ZYX-001
			 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
			 *
			 * @author	�ι���
			 * @version 1.0
			 * @since	2008-4-14
			 * @category	WebTopo/ʵʱ�澯��/�澯����
			 *
			 */
		%>
		<link href="<s:url value="/css/css_blue.css"/>" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script language="JavaScript" src="<s:url value="/webtopo/warn/warnRuleDef.js"/>"></script>
		<script language="javascript" type="text/javascript"><!--
		var tabRowIndex = -1;
		$(function(){
			$("#saveButton").click(function (){saveWarnColumnInfo();});
			$("#myBody").click(function(){bodyClk();});
		});
		function saveWarnColumnInfo(){
			CheckForm();
			$.ajax({
				type:"POST",
				url:"<s:url value="/webtopo/warnColumnDef!saveWarnColumn.action" />",
				data:{"sequence":$("input[@name='newSequence']").val(),"fieldName":$("input[@name='newFieldName']").val(),"visible":$("input[@name='newVisible']").val()},
				success:function(data){
					if(data=="1"){
						alert("����ɹ�");
					}else{
						alert("����ʧ��");
					}
				},
				error:function(xmlR,msg,other){alert("����ʱ�쳣,�������Ա��ϵ");}
			});
		}
		function myRowMove(v){
			upBtn(v,"1","0");
		}
		//�����
		function CheckForm(){
			var oTab = document.all("myTable");
			var length = oTab.rows.length;
			if(length == 2) return;
			var fieldName = "";
			var sequence = "";
			var visible = "";
			var visibletemp = "";
			var intRowIndex = 0;
			for(var i=2;i<length;i++){
				intRowIndex = i;
				sequence += "#" + getCellValue(oTab,intRowIndex,0);
				fieldName += "#" + getCellValue(oTab,intRowIndex,1);
				visible += "#" + getCellChildState(oTab,intRowIndex,3);
			}
			sequence = sequence.substr(1);
			fieldName = fieldName.substr(1);
			visible =  visible.substr(1);
			$("input[@name='newSequence']").val(sequence);
			$("input[@name='newFieldName']").val(fieldName);
			$("input[@name='newVisible']").val(visible);
			return true;
		}
		--></script>
	</head>
	<body id="myBody">
		<form name="frm" method ="post">
		<input type="hidden" name="newSequence" />
		<input type="hidden" name="newFieldName" />
		<input type="hidden" name="newVisible" />
		<br>
			<table width="90%" height="30" border=0 align="center" cellpadding="0" cellspacing="0" class="blue_gargtd">
				<tr>
					<td width="162" align="center"  class="title_bigwhite">�澯��������ʾ����</td>
				</tr>
			</table>
			<table id="myTable" width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr bgcolor="#ffffff">
					<td colspan="4" class="foot" align="right">
						<input type="button" class="jianbian" title="���������" onclick="myRowMove(1)" value="��" >
						<input type="button" class="jianbian" title="���������" onclick="myRowMove(-1)" value="��" >
						<input type="button" id="saveButton" title="����澯��������ʾ����" class="jianbian" value="��  ��" />
					</td>
				</tr>
				<tr>
					<th nowrap>���</th>
					<th>������</th>
					<th nowrap>�Ƿ���ʾ</th>
				</tr>
				<s:iterator value="resultList" var="rt"  status="status">
				<tr onclick="clkRow()" bgcolor="#FFFFFF">
					<td align="center"><s:property value="#rt.sequence "/></td>
					<td style="display:none"><s:property value="#rt.field_name"/></td>
					<td align="center"><s:property value="#rt.field_desc"/></td>
					<td align="center">
						<input type="checkbox" name="visible" onclick="clearColor();" title="ѡ���򽫸���չʾ����" <s:if test="#rt.visible == 1">checked</s:if> />
					</td>
				</tr>
				</s:iterator>
			</table>
			<br>
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table">
				<tr>
				<td colspan="2">˵��:</td>
				</tr>
				<tr>
					<td>1</td>
					<td>��Ŵ�����������ڸ澯���д����ҵ�չʾ˳��</td>
				</tr>
				<tr>
					<td>2</td>
					<td>ѡ�б���к�,����ͨ���������򡾡�����ť�����������Ƶ���ʾ˳��.</td>
				</tr>
				<tr>
					<td>3</td>
					<td>ѡ��"�Ƿ���ʾ",��澯���оͻ�չʾ�����е�����</td>
				</tr>
				<tr>
					<td>4</td>
					<td>�����ñ����,���������桿��ť����������������Ϣ.</td>
				</tr>
				<tr style="color:blue">
					<td>5</td>
					<td>��ʾ����Խ��,�澯������Խ��.</td>
				</tr>
			</table>
		</form>
	</body>
</html>
