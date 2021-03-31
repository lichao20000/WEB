<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>OUI查询修改</title>
<%
	/**
	 * OUI查询修改页面
	 * 
	 * @author gaoyi
	 * @version 1.0
	 * @since 2013-05-15
	 * @category
	 */
%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

	function query() {
		document.selectForm.submit();
	}
	
	function addeditOUI(id,vendorName,ouiId,ouiDesc,remark,type){
		$("table[@id='addedit']").show();
		if(type=="1"){
			document.all("actLabel").innerHTML = "添加";
			$("input[@name='type']").val("1");
			$("input[@name='id']").val("");
			$("input[@name='vendorName']").val("");
			$("input[@name='ouiId']").val("");
			$("input[@name='ouiDesc']").val("");
			$("input[@name='remark']").val("");
		}else{
			document.all("actLabel").innerHTML = "编辑";
			$("input[@name='type']").val(type);
			$("input[@name='id']").val(id);
			$("input[@name='vendorName']").val(vendorName);
			$("input[@name='ouiId']").val(ouiId);
			$("input[@name='ouiDesc']").val(ouiDesc);
			$("input[@name='remark']").val(remark);
		}
	}
	
	function ExecMod(){
		$("#save").attr('disabled',true);
		var type= $("input[@name='type']").val();
		var id=$("input[@name='id']").val();
		var ouiId=$("input[@name='ouiId']").val();
		if(ouiId==null || $.trim(ouiId)==""){
			alert("OUI不可为空！");
			$("#save").attr('disabled',false);
			return false;
		}
		var vendorName =$("input[@name='vendorName']").val();
		if(vendorName==null || $.trim(vendorName)==""){
			alert("厂商不可为空！");
			$("#save").attr('disabled',false);
			return false;
		}
		var ouiDesc =$("input[@name='ouiDesc']").val();
		if(ouiDesc==null || $.trim(ouiDesc)==""){
			alert("OUI描述不可为空！");
			$("#save").attr('disabled',false);
			return false;
		}
		var remark=	$("input[@name='remark']").val();
		var url ="";
		if("1"==type){
			 url = "<s:url value='/itms/resource/deviceOUIInfoACT!addOUI.action'/>";
		}else if("2"==type){
			url = "<s:url value='/itms/resource/deviceOUIInfoACT!editOUI.action'/>";
		}
		 $.post(url,{
			 	id:id,
			 	vendorName:vendorName,
			 	ouiId:ouiId,
			 	ouiDesc:ouiDesc,
			 	remark:remark
			},function(ajax){
				alert(ajax);
				var form = window.document.getElementById("form");
				form.action = "<s:url value="/itms/resource/deviceOUIInfoACT!getDeviceOuiInfo.action"/>";
				form.submit();
				$("#save").attr('disabled',false);
		    });
	}
	
	
	
	//** iframe自动适应页面 **//
	//输入你希望根据页面高度自动调整高度的iframe的名称的列表
	//用逗号把每个iframe的ID分隔. 例如: ["myframe1", "myframe2"]，可以只有一个窗体，则不用逗号。
	//定义iframe的ID
	var iframeids = [ "dataForm" ]

	//如果用户的浏览器不支持iframe是否将iframe隐藏 yes 表示隐藏，no表示不隐藏
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//自动调整iframe高度
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//如果用户的浏览器是NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//如果用户的浏览器是IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//根据设定的参数来处理不支持iframe的浏览器的显示问题
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
		query();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<form id="form" name="selectForm" method="post"
		action="<s:url value='/itms/resource/deviceOUIInfoACT!getDeviceOuiInfo.action'/>"
		target="dataForm">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
								OUI信息管理</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" /> 设备OUI信息情况</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">设备OUI查询</th>
						</TR>
						<TR>
							<TD class="column" width='15%' align="right">OUI</TD>
							<TD width="35%">
								<input type="text" name="oui" id="oui" value="" maxlength=50 class=bk>
							</TD>
						 	<TD class=column width="15%" align='right'>厂商</TD>
							<TD width="35%">
								<s:select list="vendorMap" name="vendor_name" id="vendor_name"
									headerKey="0" headerValue="请选择厂商" listKey="vendor_name"
									listValue="vendor_name" cssClass="bk">
								</s:select>
							</TD>
						</TR>
						
						<TR>
							<td colspan="4" align="right" class=foot>
							
							<s:if test='%{userName=="admin" || userName=="szxadmin"}'>
								<button onclick="addeditOUI('','','','','','1')" >&nbsp;新&nbsp;&nbsp;增&nbsp;</button>&nbsp;&nbsp;&nbsp;&nbsp;
							</s:if>
								<button onclick="query()" >&nbsp;查&nbsp;&nbsp;询&nbsp;</button>
							</td>
						</TR>
					</table>
				</td>
			</tr>
			<tr>
				<td height="25" id="resultStr"></td>
			</tr>
			<tr>
				<td><iframe id="dataForm" name="dataForm" height="0"
						frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
			</tr>
			<tr>
				<td height="25" ></td>
			</tr>
			
			<tr>
				<td>
						<table class="querytable" width="98%" align="center"
							style="display: none" id="addedit">
							<tr>
								<th  colspan="4" ><SPAN id="actLabel">添加</SPAN>OUI信息 
								<input type="hidden" name="type" id="type"  value="1" />
								<input type="hidden" name="id" id="id"  value="" />
								</th>
							</tr>
							<TR>
								<TD class="column" align="center" width="15%">OUI</TD>
								<TD width="35%"><input type="text" name="ouiId"
									id="ouiId" class="bk" value="" size="40" maxlength="20" />
									<font color="red">*</font>
								</TD>
								<TD class="column" align="center" width="15%">OUI描述</TD>
								<TD width="35%"><input type="text" name="ouiDesc"
									id="ouiDesc" class="bk" value="" size="40" maxlength="20">
									<font color="red">*</font>
								</TD>
							</TR>
							<TR>
								<TD class="column" align="center" width="15%">厂商</TD>
								<TD width="35%"><input type="text" name="vendorName"
									id="vendorName" class="bk" value="" size="40" maxlength="20">
									<font color="red">*</font>
								</TD>
								<TD class="column" align="center" width="15%">备注</TD>
								<TD width="85%" colspan="3"><input type="text" name="remark"
									id="remark" class="bk" value="" size="40" maxlength="20" />
								</TD>
							</TR>
							<tr>
								<td colspan="4" class="foot" align="right">
								<div class="right">
								<button onclick="ExecMod()" id="save">保存</button>
								</div>
								</td>
							</tr>
						</table>
				</td>
			</tr>
			
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>
</body>
</html>