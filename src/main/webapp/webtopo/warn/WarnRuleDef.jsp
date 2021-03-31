<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>�༭�澯���˹���ģ��</title>
		<%
			/**
			 * WebTopoʵʱ�澯�Ƹ澯������ҳ��
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
		<script language="javascript" type="text/javascript"><!--
		$(function(){
			$("#addTable").hide();
			$("#addButton").click(function (){addRuleInfo();});
			$("#saveButton").click(function (){checkForm();});
			$("#closeButton").click(function (){$("#addTable").hide();$("#addButton").attr("disabled","");});
		});
		//�༭����ģ��
		function editDetailInfo(ruleId,ruleName){
			window.location="<s:url value='/webtopo/warnRuleDef!editDetailInfo.action' />?ruleId="+ruleId+"&ruleName="+ruleName;
		}
		//�鿴ʵʱ�澯��
		function showRealTimeWarn(ruleId,max){
			window.open("<s:url value='/webtopo/RealTimeWarn.action' />?rule="+ruleId+"&max="+max);
		}
		//չʾ����ģ�������
		function showShareDetailInfo(ruleId,ruleName,userName){
			window.location="<s:url value='/webtopo/warnRuleDef!showDetailInfo.action' />?ruleId="+ruleId + "&ruleName="+ruleName+"&userName="+userName;
		}
		//���ƹ���ģ�嵽���û�����ģ��֮��
		function copyShareDetailInfo(ruleId){
			$.ajax({
				type:"POST",
				url:"<s:url value='/webtopo/warnRuleDef!copyRuleInfo.action' />",
				data:{"ruleId":ruleId},
				success:function(data){
					if(data=="1"){
						alert("ģ�帴�Ƴɹ�");
						window.location.reload();
					}else{
						alert("ģ�帴��ʧ��");
					}
				},
				error:function(xmlR,msg,other){alert("ģ�帴��ʱ�쳣,�������Ա��ϵ");}
			});
		}
		//չ����ӹ���Table
		function addRuleInfo(ruleId,ruleName,maxNum){
			$("#addTable").show();
			$("#addButton").attr("disabled","true");
			$("#reset").attr("disabled","");
			$("#showTitle").text("�����澯����ģ��");
			$("#ruleId").attr("value",-1);
			$("#ruleName").attr("value","");
			$("#maxNum").attr("value","200");
			// ��ӹ���ʱ��ʼ��checkbox
			$("input[@type='checkbox'][@name='selected']").attr("checked","");
			$("input[@type='checkbox'][@name='isPublic']").attr("checked","");
			$("input[@type='checkbox'][@name='visible']").attr("disabled","");
			$("input[@type='checkbox'][@name='visible']").attr("checked","true");
		}
		// չ���༭����Table
		function editRuleInfo(ruleId,ruleName,maxNum,selected,ispublic,visible){
			$("#addTable").show();
			$("#addButton").attr("disabled","");
			$("#showTitle").text("�޸ĸ澯����ģ��");
			$("#reset").attr("disabled","true");
			$("#ruleId").attr("value",ruleId);
			$("#ruleName").attr("value",ruleName);
			$("#maxNum").attr("value",maxNum);
			initCheckBox(selected,ispublic,visible);
			$("input[@type='checkbox'][@name='visible']").attr("disabled","true");
		}
		// ��������༭���ݳ�ʼ��checkbox
		function initCheckBox(selected,ispublic,visible){
			$("input[@type='checkbox']").each(function() {
				if(this.name=='selected'){
					if(selected == 1){
						this.checked = true;
					}else{
						this.checked = false;
					}
				}
				if(this.name=='isPublic'){
					if(ispublic == 1){
						this.checked = true;
					}else{
						this.checked = false;
					}
				}
				if(this.name=='visible'){
					if(visible == 1){
						this.checked = true;
					}else{
						this.checked = false;
					}
				}
			});
		}
		//ɾ������ģ��
		function delRuleInfo(ruleId){
			if(!confirm("ɾ������ģ��ʱ��ɾ����ģ����ȫ����������,�Ƿ����?"))
				return;
			$.ajax({
				type:"POST",
				url:"<s:url value='/webtopo/warnRuleDef!delRuleInfo.action' />",
				data:{"ruleId":ruleId},
				success:function(data){
					if(data=="1"){
						alert("ɾ���ɹ�");
						window.location.reload();
					}else{
						alert("ɾ��ʧ��");
					}
				},
				error:function(xmlR,msg,other){alert("ɾ��ʱ�쳣,�������Ա��ϵ");}
			});
		}
		//�������ģ��
		function saveRuleInfo(){
			if(!confirm("ȷ�ϱ���?"))
				return;
			var title = "";
			if($("#ruleId").val() == "-1")
				title = "���ģ��";
			else
				title = "�޸�ģ��";
			var selected = "0";
			var isPublic = "0";
			var visible = "1";
			$("input[@type='checkbox']").each(function() {
				if(this.name=='selected'){
					if(this.checked){
						selected = 1;
					}else{
						selected = 0;
					}
				}
				if(this.name=='isPublic'){
					if(this.checked){
						isPublic = 1;
					}else{
						isPublic = 0;
					}
				}
				if(this.name=='visible'){
					if(this.checked){
						visible = 1;
					}else{
						visible = 0;
					}
				}
			});
			$.ajax({
				type:"POST",
				url:"<s:url value='/webtopo/warnRuleDef!saveRuleInfo.action' />",
				data:{"ruleId":$("#ruleId").val(),"ruleName":encodeURIComponent($("#ruleName").val()),"maxNum":$("#maxNum").val(),"selected":selected,"isPublic":isPublic,"visible":visible},
				success:function(data){
					if(data=="1"){
						alert(title+"�ɹ�");
						window.location.reload();
					}else{
						alert(title+"ʧ��");
					}
				},
				error:function(xmlR,msg,other){alert("����ģ��ʱ�쳣,�������Ա��ϵ");}
			});
		}

		function checkForm(){
			if($("#ruleName").val() == ""){
				alert("�������������");
				return;
			}
			if($("#maxNum").val() == ""){
				alert("������澯�����ʾ����");
				return;
			}
			saveRuleInfo();
		}
		--></script>
	</head>
	<body>
		<form name="frm" action="<s:url value="/webtopo/warnRuleDef.action"/>"  method ="post">
		<br>
			<table width="90%" height="30" border=0 align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
				<tr>
					<td width="162" align="center"  class="title_bigwhite">�澯���˹���ģ��</td>
				</tr>
			</table>
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr>
					<th>ģ������</th>
					<th>����ʱ��</th>
					<th>�����ʾ����</th>
					<th>�Ƿ�Ĭ��</th>
					<th>�Ƿ���</th>
					<th>��������</th>
					<th>����</th>
				</tr>
				<s:iterator value="resultList" var="rt"  status="status">
				<tr>
					<td class="column text" align="center">
						<a href="#" title="�鿴����ģ������" onclick="editDetailInfo('<s:property value="#rt.rule_id"/>','<s:property value="#rt.rule_name"/>');"><s:property value="#rt.rule_name"/></a></td>
					<td class="column text" align="center"><s:property value="#rt.createtime"/></td>
					<td class="column text" align="center"><s:property value="#rt.maxnum"/></td>
					<td class="column text" align="center">
						<s:if test="#rt.selected==1">��</s:if>
					</td>
					<td class="column text" align="center">
						<s:if test="#rt.ispublic==1">��</s:if>
					</td>
					<td class="column text" align="center">
						 <s:if test="#rt.visible==1">չʾ</s:if>
						 <s:else>��չʾ</s:else>
					</td>
					<td class="column text" align="center">
						<a href="#" title="ʹ�ô˹���ģ��鿴ʵʱ�澯��" onclick="showRealTimeWarn('<s:property value="#rt.rule_id"/>','<s:property value="#rt.maxnum"/>');">�澯��</a>&nbsp;|&nbsp;
						<a href="#" title="�༭����ģ��" onclick="editRuleInfo('<s:property value="#rt.rule_id"/>','<s:property value="#rt.rule_name"/>','<s:property value="#rt.maxnum"/>','<s:property value="#rt.selected"/>','<s:property value="#rt.ispublic"/>','<s:property value="#rt.visible"/>');">�༭</a>&nbsp;|&nbsp;
						<a href="#" title="ɾ������ģ��" onclick="delRuleInfo('<s:property value="#rt.rule_id"/>');">ɾ��</a>
					</td>
				</tr>
				</s:iterator>
      			<tr bgcolor="#ffffff">
					<td colspan="7" class="foot" >
						<input type="button" id="addButton" title="����ģ��" class="jianbian" value="����ģ��" />
						<s:if test="resultList==null || resultList.size() == 0">
								<span style="width:20%;"></span><font color="red">δ����澯����ģ��</font>
      					</s:if>
					</td>
				</tr>
			</table>
		</form>
		<form name="addfrm" method ="post">
			<table id="addTable" width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr>
					<th colspan="5" id="showTitle"></th>
				</tr>
				<tr>
					<td class="column text" align="center">ģ������</td>
					<td class="column text" align="center">�����ʾ����</td>
					<td class="column text" align="center">�Ƿ�Ĭ��</td>
					<td class="column text" align="center">�Ƿ���</td>
					<td class="column text" align="center">��������</td>
				</tr>
				<tr>
					<td class="column text" align="center"><input type="text" id="ruleName" size="30"></td>
					<td class="column text" align="center">
						<select id="maxNum">
							<option value="100" >100</option>
							<option value="200" selected>200</option>
							<option value="300" >300</option>
							<option value="400" >400</option>
							<option value="500" >500</option>
							<option value="600" >600</option>
							<option value="700" >700</option>
							<option value="800" >800</option>
							<option value="900" >900</option>
							<option value="1000" >1000</option>
							<option value="2000" >2000</option>
						</select>
					</td>
					<td class="column text" align="center">
						<input type="checkbox" name="selected" title="����ΪĬ�Ϻ�,�򿪸澯�����Ĭ�ϼ��ظù���ģ����й���" />
					</td>
					<td class="column text" align="center">
						<input type="checkbox" name="isPublic" title="���ù���ģ����Ϊ�����,�����û��Ϳ��Կ�����ģ��,���ܸ��ƴ�ģ��" />
					</td>
					<td class="column text" align="center">
						<input type="checkbox" name="visible" title="ѡ�����ʾ:���ϸù��˹���ĸ澯����չʾ����;���򱻹�����˵�,����ҳ������ʾ" checked />
					</td>
				</tr>
				<tr>
					<td colspan="5" class="column text" align="center">
						<input type="button" value="����ģ��" title="����ģ��" id="saveButton" class="jianbian" />&nbsp;&nbsp;
						<input type="reset" id="reset" value="��   ��" title="����" class="jianbian"/>&nbsp;&nbsp;
						<input type="button" value="��  ��" id="closeButton" title="�ر�" class="jianbian"/>
						<input type="hidden" id="ruleId" value="-1" />
					</td>
				</tr>
			</table>
		</form>
		<br>
		<form name="templateFrm" method ="post">
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr class="green_title">
					<td colspan="6">�����û�����ģ��</td>
				</tr>
				<tr>
					<th>�����û�</th>
					<th>ģ������</th>
					<th>����ʱ��</th>
					<th>�澯�����ʾ����</th>
					<th>��������</th>
					<th>����</th>
				</tr>
				<s:iterator value="shareList" var="sl"  status="status">
				<tr>
					<td class="column text" align="center"><s:property value="#sl.acc_loginname"/></td>
					<td class="column text" align="center">
						<a href="#" title="�鿴����ģ������" onclick="showShareDetailInfo('<s:property value="#sl.rule_id"/>','<s:property value="#sl.rule_name"/>','<s:property value="#sl.acc_loginname"/>');">
							<s:property value="#sl.rule_name"/>
						</a>
					</td>
					<td class="column text" align="center"><s:property value="#sl.createtime"/></td>
					<td class="column text" align="center"><s:property value="#sl.maxnum"/></td>
					<td class="column text" align="center">
						<s:if test="#sl.visible==1">չʾ</s:if>
						<s:else>��չʾ</s:else>
					</td>
					<td class="column text" align="center">
						<a href="#" title="ʹ�ô˹���ģ��鿴ʵʱ�澯��" onclick="showRealTimeWarn('<s:property value="#sl.rule_id"/>','<s:property value="#sl.maxnum"/>');">�澯��</a>&nbsp;|&nbsp;
						<a href="#" title="������ģ�帴�Ƶ���ǰ�û�ģ����" onclick="copyShareDetailInfo('<s:property value="#sl.rule_id"/>');">һ������</a>
					</td>
				</tr>
				</s:iterator>
	     		<tr bgcolor="#ffffff">
					<td colspan="6" class="foot" >
						<s:if test="shareList==null || shareList.size() == 0">
								<font color="red">�������û�����ģ��</font>
      					</s:if>
					</td>
				</tr>
			</table>
		</form>
		<table width="90%" border=0 align="center" cellpadding="0" cellspacing="0" class="table" >
			<tr>
				<td colspan="2">˵��:</td>
			</tr>
			<tr>
				<td>1.</td>
				<td>�������ʾ�������澯�����������ʾ�ĸ澯����.</td>
			</tr>
			<tr>
				<td>2.</td>
				<td>���Ƿ�Ĭ�ϡ�����ΪĬ�Ϻ�,�򿪸澯����Ĭ�ϼ��ظù���ģ��;ֻ����һ��������ΪĬ��.</td>
			</tr>
			<tr>
				<td>3.</td>
				<td>���Ƿ������ù���ģ����Ϊ�����,�����û��Ϳ��Կ�����ģ��.</td>
			</tr>
			<tr>
				<td>4.</td>
				<td>���������͡�<font color="blue">���ú�㲻�ɸ���.</font>��Ϊչʾ,����ϸù��˹���ĸ澯����չʾ����;��Ϊ��չʾ,����ϸù��˹���ĸ澯����չʾ.</td>
			</tr>
			<tr>
				<td>5.</td>
				<td>�������û�����ģ�塿��ʾ����ϵͳ�������û�����Ĺ���ģ��,��ֻ�ܲ鿴��Ӧ�ø�ģ��,�����ܽ��б༭.</td>
			</tr>
			<tr>
				<td>6.</td>
				<td>��һ�����ơ�������ģ�帴�Ƶ���ǰ�û��Ĺ���ģ��֮��,�������Ϳ��Խ��б༭.</td>
			</tr>
			<tr>
				<td>7.</td>
				<td>�������ģ�����Ʊ�ɲ鿴��������.</td>
			</tr>
		</table>
	</body>
</html>
