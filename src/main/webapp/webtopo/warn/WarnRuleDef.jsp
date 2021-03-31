<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>编辑告警过滤规则模板</title>
		<%
			/**
			 * WebTopo实时告警牌告警规则定义页面
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
		<script language="javascript" type="text/javascript"><!--
		$(function(){
			$("#addTable").hide();
			$("#addButton").click(function (){addRuleInfo();});
			$("#saveButton").click(function (){checkForm();});
			$("#closeButton").click(function (){$("#addTable").hide();$("#addButton").attr("disabled","");});
		});
		//编辑规则模板
		function editDetailInfo(ruleId,ruleName){
			window.location="<s:url value='/webtopo/warnRuleDef!editDetailInfo.action' />?ruleId="+ruleId+"&ruleName="+ruleName;
		}
		//查看实时告警牌
		function showRealTimeWarn(ruleId,max){
			window.open("<s:url value='/webtopo/RealTimeWarn.action' />?rule="+ruleId+"&max="+max);
		}
		//展示共享模板的详情
		function showShareDetailInfo(ruleId,ruleName,userName){
			window.location="<s:url value='/webtopo/warnRuleDef!showDetailInfo.action' />?ruleId="+ruleId + "&ruleName="+ruleName+"&userName="+userName;
		}
		//复制共享模板到该用户规则模板之中
		function copyShareDetailInfo(ruleId){
			$.ajax({
				type:"POST",
				url:"<s:url value='/webtopo/warnRuleDef!copyRuleInfo.action' />",
				data:{"ruleId":ruleId},
				success:function(data){
					if(data=="1"){
						alert("模板复制成功");
						window.location.reload();
					}else{
						alert("模板复制失败");
					}
				},
				error:function(xmlR,msg,other){alert("模板复制时异常,请与管理员联系");}
			});
		}
		//展开添加规则Table
		function addRuleInfo(ruleId,ruleName,maxNum){
			$("#addTable").show();
			$("#addButton").attr("disabled","true");
			$("#reset").attr("disabled","");
			$("#showTitle").text("新增告警规则模板");
			$("#ruleId").attr("value",-1);
			$("#ruleName").attr("value","");
			$("#maxNum").attr("value","200");
			// 添加规则时初始化checkbox
			$("input[@type='checkbox'][@name='selected']").attr("checked","");
			$("input[@type='checkbox'][@name='isPublic']").attr("checked","");
			$("input[@type='checkbox'][@name='visible']").attr("disabled","");
			$("input[@type='checkbox'][@name='visible']").attr("checked","true");
		}
		// 展开编辑规则Table
		function editRuleInfo(ruleId,ruleName,maxNum,selected,ispublic,visible){
			$("#addTable").show();
			$("#addButton").attr("disabled","");
			$("#showTitle").text("修改告警规则模板");
			$("#reset").attr("disabled","true");
			$("#ruleId").attr("value",ruleId);
			$("#ruleName").attr("value",ruleName);
			$("#maxNum").attr("value",maxNum);
			initCheckBox(selected,ispublic,visible);
			$("input[@type='checkbox'][@name='visible']").attr("disabled","true");
		}
		// 根据所需编辑数据初始化checkbox
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
		//删除规则模板
		function delRuleInfo(ruleId){
			if(!confirm("删除规则模板时将删除该模板下全部规则详情,是否继续?"))
				return;
			$.ajax({
				type:"POST",
				url:"<s:url value='/webtopo/warnRuleDef!delRuleInfo.action' />",
				data:{"ruleId":ruleId},
				success:function(data){
					if(data=="1"){
						alert("删除成功");
						window.location.reload();
					}else{
						alert("删除失败");
					}
				},
				error:function(xmlR,msg,other){alert("删除时异常,请与管理员联系");}
			});
		}
		//保存规则模板
		function saveRuleInfo(){
			if(!confirm("确认保存?"))
				return;
			var title = "";
			if($("#ruleId").val() == "-1")
				title = "添加模板";
			else
				title = "修改模板";
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
						alert(title+"成功");
						window.location.reload();
					}else{
						alert(title+"失败");
					}
				},
				error:function(xmlR,msg,other){alert("保存模板时异常,请与管理员联系");}
			});
		}

		function checkForm(){
			if($("#ruleName").val() == ""){
				alert("请输入规则名称");
				return;
			}
			if($("#maxNum").val() == ""){
				alert("请输入告警最大显示数量");
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
					<td width="162" align="center"  class="title_bigwhite">告警过滤规则模板</td>
				</tr>
			</table>
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr>
					<th>模板名称</th>
					<th>定义时间</th>
					<th>最大显示数量</th>
					<th>是否默认</th>
					<th>是否共享</th>
					<th>过滤类型</th>
					<th>操作</th>
				</tr>
				<s:iterator value="resultList" var="rt"  status="status">
				<tr>
					<td class="column text" align="center">
						<a href="#" title="查看规则模板详情" onclick="editDetailInfo('<s:property value="#rt.rule_id"/>','<s:property value="#rt.rule_name"/>');"><s:property value="#rt.rule_name"/></a></td>
					<td class="column text" align="center"><s:property value="#rt.createtime"/></td>
					<td class="column text" align="center"><s:property value="#rt.maxnum"/></td>
					<td class="column text" align="center">
						<s:if test="#rt.selected==1">是</s:if>
					</td>
					<td class="column text" align="center">
						<s:if test="#rt.ispublic==1">是</s:if>
					</td>
					<td class="column text" align="center">
						 <s:if test="#rt.visible==1">展示</s:if>
						 <s:else>不展示</s:else>
					</td>
					<td class="column text" align="center">
						<a href="#" title="使用此规则模板查看实时告警牌" onclick="showRealTimeWarn('<s:property value="#rt.rule_id"/>','<s:property value="#rt.maxnum"/>');">告警牌</a>&nbsp;|&nbsp;
						<a href="#" title="编辑规则模板" onclick="editRuleInfo('<s:property value="#rt.rule_id"/>','<s:property value="#rt.rule_name"/>','<s:property value="#rt.maxnum"/>','<s:property value="#rt.selected"/>','<s:property value="#rt.ispublic"/>','<s:property value="#rt.visible"/>');">编辑</a>&nbsp;|&nbsp;
						<a href="#" title="删除规则模板" onclick="delRuleInfo('<s:property value="#rt.rule_id"/>');">删除</a>
					</td>
				</tr>
				</s:iterator>
      			<tr bgcolor="#ffffff">
					<td colspan="7" class="foot" >
						<input type="button" id="addButton" title="新增模板" class="jianbian" value="新增模板" />
						<s:if test="resultList==null || resultList.size() == 0">
								<span style="width:20%;"></span><font color="red">未定义告警规则模板</font>
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
					<td class="column text" align="center">模板名称</td>
					<td class="column text" align="center">最大显示数量</td>
					<td class="column text" align="center">是否默认</td>
					<td class="column text" align="center">是否共享</td>
					<td class="column text" align="center">过滤类型</td>
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
						<input type="checkbox" name="selected" title="设置为默认后,打开告警牌则会默认加载该规则模板进行过滤" />
					</td>
					<td class="column text" align="center">
						<input type="checkbox" name="isPublic" title="将该规则模板设为共享后,其他用户就可以看到该模板,且能复制此模板" />
					</td>
					<td class="column text" align="center">
						<input type="checkbox" name="visible" title="选中则表示:符合该过滤规则的告警将被展示出来;否则被规则过滤掉,不在页面中显示" checked />
					</td>
				</tr>
				<tr>
					<td colspan="5" class="column text" align="center">
						<input type="button" value="保存模板" title="保存模板" id="saveButton" class="jianbian" />&nbsp;&nbsp;
						<input type="reset" id="reset" value="重   置" title="重置" class="jianbian"/>&nbsp;&nbsp;
						<input type="button" value="关  闭" id="closeButton" title="关闭" class="jianbian"/>
						<input type="hidden" id="ruleId" value="-1" />
					</td>
				</tr>
			</table>
		</form>
		<br>
		<form name="templateFrm" method ="post">
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr class="green_title">
					<td colspan="6">其他用户共享模板</td>
				</tr>
				<tr>
					<th>所属用户</th>
					<th>模板名称</th>
					<th>定义时间</th>
					<th>告警最大显示数量</th>
					<th>过滤类型</th>
					<th>操作</th>
				</tr>
				<s:iterator value="shareList" var="sl"  status="status">
				<tr>
					<td class="column text" align="center"><s:property value="#sl.acc_loginname"/></td>
					<td class="column text" align="center">
						<a href="#" title="查看规则模板详情" onclick="showShareDetailInfo('<s:property value="#sl.rule_id"/>','<s:property value="#sl.rule_name"/>','<s:property value="#sl.acc_loginname"/>');">
							<s:property value="#sl.rule_name"/>
						</a>
					</td>
					<td class="column text" align="center"><s:property value="#sl.createtime"/></td>
					<td class="column text" align="center"><s:property value="#sl.maxnum"/></td>
					<td class="column text" align="center">
						<s:if test="#sl.visible==1">展示</s:if>
						<s:else>不展示</s:else>
					</td>
					<td class="column text" align="center">
						<a href="#" title="使用此规则模板查看实时告警牌" onclick="showRealTimeWarn('<s:property value="#sl.rule_id"/>','<s:property value="#sl.maxnum"/>');">告警牌</a>&nbsp;|&nbsp;
						<a href="#" title="将共享模板复制到当前用户模板中" onclick="copyShareDetailInfo('<s:property value="#sl.rule_id"/>');">一键复制</a>
					</td>
				</tr>
				</s:iterator>
	     		<tr bgcolor="#ffffff">
					<td colspan="6" class="foot" >
						<s:if test="shareList==null || shareList.size() == 0">
								<font color="red">无其他用户共享模板</font>
      					</s:if>
					</td>
				</tr>
			</table>
		</form>
		<table width="90%" border=0 align="center" cellpadding="0" cellspacing="0" class="table" >
			<tr>
				<td colspan="2">说明:</td>
			</tr>
			<tr>
				<td>1.</td>
				<td>【最大显示数量】告警牌最大允许显示的告警数量.</td>
			</tr>
			<tr>
				<td>2.</td>
				<td>【是否默认】设置为默认后,打开告警牌则默认加载该规则模板;只能有一个被设置为默认.</td>
			</tr>
			<tr>
				<td>3.</td>
				<td>【是否共享】将该规则模板设为共享后,其他用户就可以看到该模板.</td>
			</tr>
			<tr>
				<td>4.</td>
				<td>【过滤类型】<font color="blue">设置后便不可更改.</font>若为展示,则符合该过滤规则的告警将被展示出来;若为不展示,则符合该过滤规则的告警将不展示.</td>
			</tr>
			<tr>
				<td>5.</td>
				<td>【其他用户共享模板】显示的是系统中其他用户定义的共享模板,您只能查看和应用该模板,但不能进行编辑.</td>
			</tr>
			<tr>
				<td>6.</td>
				<td>【一键复制】将共享模板复制到当前用户的规则模板之中,这样您就可以进行编辑.</td>
			</tr>
			<tr>
				<td>7.</td>
				<td>点击规则模板名称便可查看规则详情.</td>
			</tr>
		</table>
	</body>
</html>
