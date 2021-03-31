<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>编辑告警过滤规则</title>
		<%
			/**
			 * WebTopo实时告警牌告警模板详情编辑页面
			 * <li>REQ: GZDX-REQ-20080402-ZYX-001
			 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
			 *
			 * @author	段光锐
			 * @version 1.0
			 * @since	2008-4-8
			 * @category	WebTopo/实时告警牌/告警规则
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

		//退出前关闭全部子页面
		//window.onbeforeunload=closeAllChild;
		//关闭全部新增详情页面
		function closeAllChild(){
			if (window.myAddChild && window.myAddChild.open && !window.myAddChild.closed)
				window.myAddChild.close();
			if (window.myEditChild && window.myEditChild.open && !window.myEditChild.closed)
				window.myEditChild.close();
		}
		//打开添加详情页面
		function addRuleDetailInfo(){
			//添加规则在子页面结束的时候调用
			myAddChild = window.open("<s:url value='/webtopo/warn/EditDetailRule.jsp'/>?ruleId="+$("input[@name='ruleId']").val()+"&text=&rowIndex=-1","","height=565, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no");
		}
		//编辑已有详情信息
		function editRuleDetailInfo(rowIndex,text){
			myEditChild = window.open("<s:url value='/webtopo/warn/EditDetailRule.jsp'/>?ruleId="+$("input[@name='ruleId']").val()+"&text="+text+"&rowIndex="+rowIndex,"","height=565, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=n o, status=no");
		}
		// 供EditDetailRule.jsp页面调用,修改按钮的状态,置为不可用
		function initButton(){
			$("#addButton").attr("disabled","true");
			$("#saveButton").attr("disabled","true");
			$("#goBackButton").attr("disabled","true");
		}
		// 供EditDetailRule.jsp页面调用,重置按钮状态,改为可用
		function resetButton(){
			$("#addButton").attr("disabled","");
			$("#saveButton").attr("disabled","");
			$("#goBackButton").attr("disabled","");
		}
		// 保存规则详情
		function saveRuleDetailInfo(){
			CheckForm();
			if(!confirm("确认保存?"))
				return;
			$.ajax({
				type:"POST",
				url:"<s:url value="/webtopo/warnRuleDef!saveDetailInfo.action" />",
				data:{"ruleId":$("input[@name='ruleId']").val(),"rulePriority":$("input[@name='rulePriority']").val(),"ruleContent":$.cc("input[@name='ruleContent']"),"ruleInvocation":$("input[@name='ruleInvocation']").val(),"ruleLength":$("input[@name='ruleLength']").val()},
				success:function(data){
					if(data=="1"){
						alert("保存成功");
					}else{
						alert("保存失败");
					}
				},
				error:function(xmlR,msg,other){alert("保存时异常,请与管理员联系");}
			});
		}
		// 返回到模板定义页面
		function goBack(){
			window.location="<s:url value='/webtopo/warnRuleDef.action'/>";
		}
		///////////////////////////
		//表单检查
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
		// 编辑规则
		function edit(){
			var oTab = document.all("myTable");
			var sourceRowIndex = event.srcElement.parentElement.parentElement.rowIndex;
			var proirity = getCellValue(oTab,sourceRowIndex,0);
			var text = getCellValue(oTab,sourceRowIndex,1);
			editRuleDetailInfo(sourceRowIndex,text);
		}
		// 移动行
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
					<td width="162" align="center"  class="title_bigwhite">编辑告警过滤规则</td>
					<td>
					当前编辑模板名称:<font color="red"><s:property value="ruleName"/></font>
					</td>
				</tr>
			</table>
			<table id="myTable" width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr bgcolor="#ffffff">
					<td colspan="4" class="foot" align="right">
						<input type="button" id="addButton" class="jianbian" title="新增规则详情" value="新增规则详情" />
						<input type="button" class="jianbian" title="表格行上移" onclick="myRowMove(1)" value="↑" >
						<input type="button" class="jianbian" title="表格行下移" onclick="myRowMove(-1)" value="↓" >
						<input type="button" id="saveButton" title="保存告警过滤规则详情" class="jianbian" value="保  存" />
						<input type="button" id="goBackButton" title="返回告警过滤规则模板页面" class="jianbian" value="返  回" />
					</td>
				</tr>
				<tr>
					<th nowrap>优先级</th>
					<th>规则内容</th>
					<th>是否启用</th>
					<th nowrap>操作</th>
				</tr>
				<s:iterator value="resultList" var="rt"  status="status">
				<tr id="<s:property value="#rt.rule_priority"/>" onclick="clkRow()" bgcolor="#FFFFFF">
					<td align="center"><s:property value="#rt.rule_priority"/></td>
					<td><s:property value="#rt.rule_content"/></td>
					<td align="center">
						<select>
							<option value="1" <s:if test="#rt.rule_invocation == 1">selected</s:if>>启用</option>
							<option value="0" <s:if test="#rt.rule_invocation == 0">selected</s:if>>禁用</option>
						</select>
					</td>
					<td align="center" nowrap>
			  			<a href="javascript://" title="编辑规则详情" name="edit" onclick='edit()'>编辑</a>
			  			<a href="javascript://" title="删除规则详情" name="del" onclick='delRow()'>删除</a>
					</td>
				</tr>
				<span id="test"></span>
				</s:iterator>
			</table>
			<br>
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table">
				<tr>
				<td colspan="2">说明:</td>
				</tr>
				<tr>
					<td>1</td>
					<td>在新增或编辑规则详情时,请勿刷新或关闭该页面.</td>
				</tr>
				<tr>
					<td>2</td>
					<td>选中表格行后,可以通过【↑】或【↓】按钮来更改规则的优先级.</td>
				</tr>
				<tr>
					<td>3</td>
					<td>在规则详情变更后,请点击【保存】按钮来保存您的规则内容.</td>
				</tr>
				<tr>
					<td>4</td>
					<td>每条规则之间是<font color="blue">【并且】</font>的逻辑关系.</td>
				</tr>
			</table>
		</form>
	</body>
</html>
