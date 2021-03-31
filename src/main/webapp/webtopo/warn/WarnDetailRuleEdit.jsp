<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�༭�澯���˹���</title>
		<%
			/**
			 * WebTopoʵʱ�澯�Ƹ澯ģ������༭ҳ��
			 * <li>REQ: GZDX-REQ-20080402-ZYX-001
			 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
			 *
			 * @author	�ι���
			 * @version 1.0
			 * @since	2008-4-8
			 * @category	WebTopo/ʵʱ�澯��/�澯����
			 *
			 */
		%>
		<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
		<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
		<script language="JavaScript" src="<s:url value="/webtopo/warn/warnRuleDef.js"/>"></script>
		<script language="javascript" type="text/javascript"><!--
		Error.prototype.print = function() {printError(this);}
		var tabRowIndex = -1;

		$(function(){
			$("#addButton").click(function (){addRuleDetailInfo();});
			$("#saveButton").click(function (){saveRuleDetailInfo();});
			$("#goBackButton").click(function (){goBack();});
			$("#myBody").click(function(){bodyClk();});
			$("#edit").click(function(){edit();});
		});

		//�˳�ǰ�ر�ȫ����ҳ��
		//window.onbeforeunload=closeAllChild;
		//�ر�ȫ����������ҳ��
		function closeAllChild(){
			if (window.myAddChild && window.myAddChild.open && !window.myAddChild.closed)
				window.myAddChild.close();
			if (window.myEditChild && window.myEditChild.open && !window.myEditChild.closed)
				window.myEditChild.close();
		}
		//���������ҳ��
		function addRuleDetailInfo(){
			//��ӹ�������ҳ�������ʱ�����
			myAddChild = window.open("<s:url value='/webtopo/warn/EditDetailRule.jsp'/>?ruleId="+$("input[@name='ruleId']").val()+"&text=&rowIndex=-1","","height=565, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no");
		}
		//�༭����������Ϣ
		function editRuleDetailInfo(rowIndex,text){
			myEditChild = window.open("<s:url value='/webtopo/warn/EditDetailRule.jsp'/>?ruleId="+$("input[@name='ruleId']").val()+"&text="+text+"&rowIndex="+rowIndex,"","height=565, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no");
		}
		// ��EditDetailRule.jspҳ�����,�޸İ�ť��״̬,��Ϊ������
		function initButton(){
			$("#addButton").attr("disabled","true");
			$("#saveButton").attr("disabled","true");
			$("#goBackButton").attr("disabled","true");
		}
		// ��EditDetailRule.jspҳ�����,���ð�ť״̬,��Ϊ����
		function resetButton(){
			$("#addButton").attr("disabled","");
			$("#saveButton").attr("disabled","");
			$("#goBackButton").attr("disabled","");
		}
		// �����������
		function saveRuleDetailInfo(){
			CheckForm();
			if(!confirm("ȷ�ϱ���?"))
				return;
			$.ajax({
				type:"POST",
				url:"<s:url value="/webtopo/warnRuleDef!saveDetailInfo.action" />",
				data:{"ruleId":$("input[@name='ruleId']").val(),"rulePriority":$("input[@name='rulePriority']").val(),"ruleContent":$.cc("input[@name='ruleContent']"),"ruleInvocation":$("input[@name='ruleInvocation']").val(),"ruleLength":$("input[@name='ruleLength']").val()},
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
		// ���ص�ģ�嶨��ҳ��
		function goBack(){
			window.location="<s:url value='/webtopo/warnRuleDef.action'/>";
		}
		///////////////////////////
		//�����
		function CheckForm(){
			var oTab = document.all("myTable");
			var length = oTab.rows.length;
			if(length == 2) return;
			var index = "";
			var text = "";
			var result = "";
			var intRowIndex = 0;
			for(var i=2;i<length;i++){
				intRowIndex = i;
				index += "#" + getCellValue(oTab,intRowIndex,0);
				text += "#" + getCellValue(oTab,intRowIndex,1);
				result += "#" + getCellChildValue(oTab,intRowIndex,2);
			}
			index = index.substr(1);
			text = text.substr(1);
			result =  result.substr(1);
			$("input[@name='rulePriority']").val(index);
			$("input[@name='ruleContent']").val(text);
			$("input[@name='ruleInvocation']").val(result);
			$("input[@name='ruleLength']").val(length - 2);
			return true;
		}
		// �༭����
		function edit(){
			var oTab = document.all("myTable");
			var sourceRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
			var proirity = getCellValue(oTab,sourceRowIndex,0);
			var text = getCellValue(oTab,sourceRowIndex,1);
			editRuleDetailInfo(sourceRowIndex,text);
		}
		// �ƶ���
		function myRowMove(v){
			upBtn(v,"1","0");
		}
		--></script>
	</head>
	<body id="myBody" onunload="closeAllChild()" >
		<div id="OptionContain"></div>
		<form name="frm" action="<s:url value="/webtopo/warnRuleDef!saveDetailInfo.action"/>"  method ="post">
		<input type="hidden" name="ruleId" value="<s:property value="ruleId"/>" />
		<input type="hidden" name="rulePriority" />
		<input type="hidden" name="ruleContent" />
		<input type="hidden" name="ruleInvocation" />
		<input type="hidden" name="ruleLength" />
		<br>
			<table width="90%" height="30" border=0 align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
				<tr>
					<td width="162" align="center"  class="title_bigwhite">�༭�澯���˹���</td>
					<td>
					��ǰ�༭ģ������:<font color="red"><s:property value="ruleName"/></font>
					</td>
				</tr>
			</table>
			<table id="myTable" width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr bgcolor="#ffffff">
					<td colspan="4" class="foot" align="right">
						<input type="button" id="addButton" class="jianbian" title="������������" value="������������" />
						<input type="button" class="jianbian" title="���������" onclick="myRowMove(1)" value="��" >
						<input type="button" class="jianbian" title="���������" onclick="myRowMove(-1)" value="��" >
						<input type="button" id="saveButton" title="����澯���˹�������" class="jianbian" value="��  ��" />
						<input type="button" id="goBackButton" title="���ظ澯���˹���ģ��ҳ��" class="jianbian" value="��  ��" />
					</td>
				</tr>
				<tr>
					<th nowrap>���ȼ�</th>
					<th>��������</th>
					<th>�Ƿ�����</th>
					<th nowrap>����</th>
				</tr>
				<s:iterator value="resultList" var="rt"  status="status">
				<tr id="<s:property value="#rt.rule_priority"/>" onclick="clkRow()" bgcolor="#FFFFFF">
					<td align="center"><s:property value="#rt.rule_priority"/></td>
					<td><s:property value="#rt.rule_content"/></td>
					<td align="center">
						<select>
							<option value="1" <s:if test="#rt.rule_invocation == 1">selected</s:if>>����</option>
							<option value="0" <s:if test="#rt.rule_invocation == 0">selected</s:if>>����</option>
						</select>
					</td>
					<td align="center" nowrap>
			  			<a href="javascript://" title="�༭��������" name="edit" onclick='edit()'>�༭</a>
			  			<a href="javascript://" title="ɾ����������" name="del" onclick='delRow()'>ɾ��</a>
					</td>
				</tr>
				<span id="test"></span>
				</s:iterator>
			</table>
			<br>
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table">
				<tr>
				<td colspan="2">˵��:</td>
				</tr>
				<tr>
					<td>1</td>
					<td>��������༭��������ʱ,����ˢ�»�رո�ҳ��.</td>
				</tr>
				<tr>
					<td>2</td>
					<td>ѡ�б���к�,����ͨ���������򡾡�����ť�����Ĺ�������ȼ�.</td>
				</tr>
				<tr>
					<td>3</td>
					<td>�ڹ�����������,���������桿��ť���������Ĺ�������.</td>
				</tr>
				<tr>
					<td>4</td>
					<td>ÿ������֮����<font color="blue">�����ҡ�</font>���߼���ϵ.</td>
				</tr>
			</table>
		</form>
	</body>
</html>
