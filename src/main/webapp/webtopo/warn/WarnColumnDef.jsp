<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<title>告警牌列名显示配置</title>
		<%
			/**
			 * WebTopo实时告警牌告警规则定义页面
			 * <li>REQ: GZDX-REQ-20080402-ZYX-001
			 * <li>REQ: HBDX-REQ-20080220-WANGBING-001
			 *
			 * @author	段光锐
			 * @version 1.0
			 * @since	2008-4-14
			 * @category	WebTopo/实时告警牌/告警规则
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
						alert("保存成功");
					}else{
						alert("保存失败");
					}
				},
				error:function(xmlR,msg,other){alert("保存时异常,请与管理员联系");}
			});
		}
		function myRowMove(v){
			upBtn(v,"1","0");
		}
		//表单检查
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
					<td width="162" align="center"  class="title_bigwhite">告警牌列名显示配置</td>
				</tr>
			</table>
			<table id="myTable" width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table" bgcolor="#000000">
				<tr bgcolor="#ffffff">
					<td colspan="4" class="foot" align="right">
						<input type="button" class="jianbian" title="表格行上移" onclick="myRowMove(1)" value="↑" >
						<input type="button" class="jianbian" title="表格行下移" onclick="myRowMove(-1)" value="↓" >
						<input type="button" id="saveButton" title="保存告警牌列名显示配置" class="jianbian" value="保  存" />
					</td>
				</tr>
				<tr>
					<th nowrap>序号</th>
					<th>列名称</th>
					<th nowrap>是否显示</th>
				</tr>
				<s:iterator value="resultList" var="rt"  status="status">
				<tr onclick="clkRow()" bgcolor="#FFFFFF">
					<td align="center"><s:property value="#rt.sequence "/></td>
					<td style="display:none"><s:property value="#rt.field_name"/></td>
					<td align="center"><s:property value="#rt.field_desc"/></td>
					<td align="center">
						<input type="checkbox" name="visible" onclick="clearColor();" title="选中则将该列展示出来" <s:if test="#rt.visible == 1">checked</s:if> />
					</td>
				</tr>
				</s:iterator>
			</table>
			<br>
			<table width="90%" border=0 align="center" cellpadding="1" cellspacing="1" class="table">
				<tr>
				<td colspan="2">说明:</td>
				</tr>
				<tr>
					<td>1</td>
					<td>序号代表该列名称在告警牌中从左到右的展示顺序</td>
				</tr>
				<tr>
					<td>2</td>
					<td>选中表格行后,可以通过【↑】或【↓】按钮来更改列名称的显示顺序.</td>
				</tr>
				<tr>
					<td>3</td>
					<td>选中"是否显示",则告警牌中就会展示出该列的内容</td>
				</tr>
				<tr>
					<td>4</td>
					<td>在配置变更后,请点击【保存】按钮来保存您的配置信息.</td>
				</tr>
				<tr style="color:blue">
					<td>5</td>
					<td>显示的列越少,告警牌性能越高.</td>
				</tr>
			</table>
		</form>
	</body>
</html>
