<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page isELIgnored="false" %>
<%
	String gwType = request.getParameter("gw_type");
	String tempdpi = request.getParameter("dpi");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>编辑最新白名单版本信息</title>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css/listview.css"/>" rel="stylesheet"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/inmp/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/inmp/CheckForm.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

</head>
<body>
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT=20>&nbsp;</TD>
		</TR>
		<TR>
			<TD>

				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										编辑最新白名单版本信息</td>
									<td align="right">
										<button type="button" id="doAddButton"
											class=btn>&nbsp;增&nbsp;加 &nbsp;</button>
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<tr bgcolor="#FFFFFF">
					<form id="form">
						<input type="hidden" name="version" value="${filterTerminal.filterVer}"/>
						<td><input type="hidden" name="param" value="" />
							<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0
								cellpadding=0 align="center" class="querytable">
								<tr>
									<th align="center" width="100%">编辑白名单</th>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<div id="selectedDev">
<!-- 											<form action=""> -->
												<table border=0 cellspacing=1 cellpadding=2 width="100%">
													<TR bgcolor="#FFFFFF">
														<td align="right" class=column width="20%">白名单类型</td>
														<td width="30%"><input type="text" id="Type"
															name="filterTerminal.type" value="${filterTerminal.type}" /></td>
														<td align="right" class=column width="30%">白名单版本号</td>
														<td width="70%"><input type="text" id="Softver"
															name="filterTerminal.softver" value="${filterTerminal.softver}" /></td>
													</TR>
													<TR bgcolor="#FFFFFF">
														<td align="right" class=column width="20%">本白名单标签所代表的白名单的版本号</td>
														<td width="30%"><input type="text" id="RouteVer"
															name="filterTerminal.filterVer" value="${filterTerminal.filterVer}" /></td>
													</TR>
												</table>
										</div>
									</td>
								</tr>
								<TR>
									<TD bgcolor=#999999>
										<div id="fatherDiv">
												<div id="paramAddDiv0">
													<table border=0 cellspacing=1 cellpadding=2 width="100%">
														<TR bgcolor="#FFFFFF">
															<td nowrap align="right" class=column width="20%"><span>
<!-- 																	<a  id="delete"> 删除 | </a> -->
																	<button type="button" id="delete" name="delete" class=btn>删除</button>
															</span>本路由表所包含的目标地址</td>
															<td width="70%" colspan="1"><input type="text"
																id="Destination" name="Destination"
																value=''
																style="width: 800px;"></td>
														</TR>
														<TR bgcolor="#FFFFFF">
															<td align="right" class=column width="20%">子网掩码</td>
															<td width="30%"><input type="text" id="Netmask"
																name="Netmask"  value="" /></td>
														</TR>
													</table>
												</div>
											 <s:iterator value="filterTerminal.filters" var="filter" status="stuts">
											   		<s:property value="#id.attrName" />
													   		<div id="paramAddDiv" class="paramAddDiv">
																	<table border=0 cellspacing=1 cellpadding=2 width="100%">
																		<TR bgcolor="#FFFFFF">
																			<td nowrap align="right" class=column width="20%"><span>
<!-- 																					<a id="delete"> 删除 | </a> -->
																			<button type="button" id="delete" name="delete" class=btn>删除</button>
																			</span>本路由表所包含的目标地址</td>
																			<td width="70%" colspan="3"><input type="text"
																				id="Destination" name="Destination"
																				value='<s:property value="#filter.destination" />'
																				style="width: 800px;"></td>
																		</TR>
																		<TR bgcolor="#FFFFFF">
																			<td align="right" class=column width="20%">子网掩码</td>
																			<td width="30%"><input type="text" id="Netmask"
																				name="Netmask" value="<s:property value='#filter.netmask' />" /></td>
																		</TR>
																	</table>
													</div>
											 </s:iterator>

										</div>


										<table border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<button type="button" id="exeButton" name="exeButton"
														 class=btn>&nbsp;保&nbsp;存
														&nbsp;</button>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="left" class="green_foot">
													<div id="resultDIV" />
												</TD>
											</TR>
										</table>
									</TD>
								</TR>
							</TABLE></td>
					</form>
					</tr>
				</table>
			</TD>
		</TR>
	</TABLE>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$("#paramAddDiv0").hide();
		$("#doAddButton").bind('click',function(e){
			var clone=$("#paramAddDiv0").clone();
			$("#fatherDiv").append(clone);
			clone.attr("id","paramAddDiv");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$(".paramAddDiv").each(function(i,e){
			$(e).find("#delete").bind('click',function(){
				$(e).remove();
			})
		})

		$("#exeButton").bind('click',function(e){
			$(".paramAddDiv").each(function(i,e){
				$(e).find("#Destination").attr("name","filterTerminal.filters["+i+"].destination");
				$(e).find("#Netmask").attr("name","filterTerminal.filters["+i+"].netmask");
			})
			var version = $.trim($("input[@name='version']").val());
			var routeVer = $.trim($("input[@name='filterTerminal.filterVer']").val());
			if (version == routeVer){
				if (confirm('当前版本号未进行更改，请确认是否已修改?')) {
					if(version == routeVer){
						alert("当前版本号和已有版本号一致，不允许修改!");
						return;
					}
				}else{
					return;
				}
			}
			var postUrl='<s:url value="/intelspeaker/intelSpeakerConfigMgrAction!saveNewst.action?type=2"/>';
			$.ajax({
				  type: 'POST',
				  url: postUrl,
				  data: $("#form").serialize(),
				  success: function(res){
					  var alertStr="失败！";
					  if(res=='-2'){
						  alertStr= "该版本文件已存在";
					  }else if(res=='-3'){
						  alertStr= "重命名当前版本文件失败";
					  }else if(res=='-4'){
						  alertStr= "写最新版本文件失败";
					  }else if(res=='-1'){
						  alertStr= "不存在最新文件";
					  }else if(res=='1'){
						  alertStr= "成功";
					  }
					  alert(alertStr);
				  },
				  dataType: "application/x-www-form-urlencoded"
			});
		})
	})
</script>
</html>
<%@ include file="../foot.jsp"%>
