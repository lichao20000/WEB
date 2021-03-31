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
<title>编辑最新路由表版本信息</title>
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
										编辑最新路由表版本信息</td>
									<td align="right">
										<button type="button" id="addTer" class=btn>增加Terminal</button>
<!-- 										<button type="button" id="addTer" class=btn>&nbsp;增&nbsp;加 &nbsp;</button> -->
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<th align="center" width="100%">编辑路由表</th>

					</tr>

					<tr bgcolor="#FFFFFF">

						<td>
							<form id="form">
								<input type="hidden" name="version" value="${routerTmListBak.routeVer}" />
								<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" >
									<tr>
										<td align="left" class=column width="20%">本Routes标签所代表的路由表的版本号<input
											type="text" id="RouteVer" name="routerTmListBak.routeVer"
											value="${routerTmListBak.routeVer}" /></td>
									</tr>
								</table>
								<div id="routerFaDiv" class="routerFaDiv">
									<div id="routerDivT0">
										<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0 cellpadding=0 align="center" class="querytable">
											<tr>
												<td align="right">
													<button type="button" id="doRemoveButton" class=btn>删除Terminal</button>
													<button type="button" id="doAdd" class=btn>增加Route</button>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														<table border=0 cellspacing=1 cellpadding=2 width="100%">
															<TR bgcolor="#FFFFFF">
																<td align="right" class=column width="20%">机顶盒类型</td>
																<td width="30%"><input type="text" id="Type" name="Type"
																	value='' /></td>
																<td align="right" class=column width="30%">机顶盒管理版本号</td>
																<td width="70%"><input type="text" id="Softver" name="Softver"
																	value='' /></td>
															</TR>
															<TR bgcolor="#FFFFFF">
																<td align="right" class=column width="20%">启用DNS服务器所属的平面</td>
																<td width="30%"><select name="DNSSlect" id="DNSSlect" cssClass="bk"
																	value=''>
																		<option value="A">互联网A平面</option>
																		<option value="B">IPTV专用网络B平面</option>
																</select></td>
																<td align="right" class=column width="30%">B平面接入的VLAN标识</td>
																<td width="70%"><input type="text" id="VLANID" name="VLANID"
																	value='' /></td>
															</TR>
														</table>
													</div>
												</td>
											</tr>

											<TR>
												<TD bgcolor=#999999>
													<div id="fatherDivT0">
														<div id="paramAddDivT0" class="paramAddDivT0">
															<table border=0 cellspacing=1 cellpadding=2 width="100%">
																<TR bgcolor="#FFFFFF">
																	<td nowrap align="right" class=column width="20%">
																		<span>
																			<button type="button" id="delete" name="delete"
																				class=btn>删除Route</button>
																		</span>本路由表所包含的目标地址
																	</td>
																	<td width="70%" colspan="3">
																		<input type="text" id="Destination" name="Destination" value='' style="width: 800px;">
																	</td>
																</TR>
																<TR bgcolor="#FFFFFF">
																	<td align="right" class=column width="20%">子网掩码</td>
																	<td width="30%"><input type="text" id="Netmask" name="Netmask" value="" />
																	</td>
																	<td align="right" class=column width="30%">所属平面标识</td>
																	<td width="70%">
																		<select name="Plane" value='' id="Plane" cssClass="bk">
																			<option value="A">互联网A平面</option>
																			<option value="B">IPTV专用网络B平面</option>
																		</select>
																	</td>
																</TR>
															</table>
														</div>
													</div>

												</TD>
											</TR>
										</TABLE>
									</div>

								<s:iterator value="routerTmListBak.routerTerminals" var="routerTerminal" status="rtl">

									<div id="routerDiv<s:property value='#rtl.index'/>" class="routerDiv">
										<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0 cellpadding=0 align="center" class="querytable">
											<tr>
												<td align="right">
													<button type="button" id="doRemoveButton<s:property value='#rtl.index'/>" class=btn>删除Terminal
														</button>
													<button type="button" id="doAddButton<s:property value='#rtl.index'/>" class=btn>增加Route
														</button>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														<table border=0 cellspacing=1 cellpadding=2 width="100%">
															<TR bgcolor="#FFFFFF">
																<td align="right" class=column width="20%">机顶盒类型</td>
																<td width="30%"><input type="text" id="Type" name="Type"
																	value='<s:property value="#routerTerminal.type" />' /></td>
																<td align="right" class=column width="30%">机顶盒管理版本号</td>
																<td width="70%"><input type="text" id="Softver" name="Softver"
																	value='<s:property value="#routerTerminal.softver" />' /></td>
															</TR>
															<TR bgcolor="#FFFFFF">
																<td align="right" class=column width="20%">启用DNS服务器所属的平面</td>
																<td width="30%"><select name="DNSSlect" id="DNSSlect" cssClass="bk"
																	value='<s:property value="#routerTerminal.dnsslect" />'>
																		<option value="A">互联网A平面</option>
																		<option value="B">IPTV专用网络B平面</option>
																</select></td>
																<td align="right" class=column width="30%">B平面接入的VLAN标识</td>
																<td width="70%"><input type="text" id="VLANID" name="VLANID"
																	value='<s:property value="#routerTerminal.VLANID" />' /></td>
															</TR>
														</table>
													</div>
												</td>
											</tr>

											<TR>
												<TD bgcolor=#999999>
													<div id="fatherDiv<s:property value='#rtl.index'/>">
														<div id="paramAddDivT0" class="paramAddDivT0">
															<table border=0 cellspacing=1 cellpadding=2 width="100%">
																<TR bgcolor="#FFFFFF">
																	<td nowrap align="right" class=column width="20%">
																		<span>
																			<button type="button" id="delete" name="delete"
																				class=btn>删除Route</button>
																		</span>本路由表所包含的目标地址
																	</td>
																	<td width="70%" colspan="3">
																		<input type="text" id="Destination" name="Destination" value='' style="width: 800px;">
																	</td>
																</TR>
																<TR bgcolor="#FFFFFF">
																	<td align="right" class=column width="20%">子网掩码</td>
																	<td width="30%"><input type="text" id="Netmask" name="Netmask" value="" />
																	</td>
																	<td align="right" class=column width="30%">所属平面标识</td>
																	<td width="70%">
																		<select name="Plane" value='' id="Plane" cssClass="bk">
																			<option value="A">互联网A平面</option>
																			<option value="B">IPTV专用网络B平面</option>
																		</select>
																	</td>
																</TR>
															</table>
														</div>
														<s:iterator value="#routerTerminal.routers" var="router" status="stuts">
															<div id="paramAddDiv<s:property value='#rtl.index'/>" class="paramAddDiv">
																<table border=0 cellspacing=1 cellpadding=2 width="100%">
																	<TR bgcolor="#FFFFFF">
																		<td nowrap align="right" class=column width="20%">
																			<span>
																					<button type="button" id="delete" name="delete" class=btn>删除Route</button>
																			</span>本路由表所包含的目标地址
																		</td>
																		<td width="70%" colspan="3">
																			<input type="text" id="Destination" name="Destination" value='<s:property value="#router.destination" />'
																			style="width: 800px;">
																		</td>
																	</TR>
																	<TR bgcolor="#FFFFFF">
																		<td align="right" class=column width="20%">子网掩码</td>
																		<td width="30%"><input type="text" id="Netmask" name="Netmask"
																			value="<s:property value='#router.netmask' />" />
																		</td>
																		<td align="right" class=column width="30%">所属平面标识</td>
																		<td width="70%">
																			<select name="Plane" id="Plane" cssClass="bk">
																				<option value="A">互联网A平面</option>
																				<option value="B" <s:if test='#router.plane=="B"'>selected</s:if>>
																					IPTV专用网络B平面
																				</option>
																			</select>
																		</td>
																	</TR>
																</table>
															</div>
														</s:iterator>

													</div>

												</TD>
											</TR>
											<TR><TD HEIGHT=20>&nbsp;</TD></TR>
										</TABLE>
										<div >

										</div>
									</div>
								</s:iterator>
								</div>
							</form>
						</td>

					</tr>
					<tr>
						<td>
							<table border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" align="right" class="green_foot">
										<button type="button" id="exeButton" name="exeButton"
											class=btn>&nbsp;保&nbsp;存&nbsp;</button>
									</TD>
								</TR>
								<TR bgcolor="#FFFFFF">
									<TD colspan="4" align="left" class="green_foot">
										<div id="resultDIV" />
									</TD>
								</TR>
							</table>
						</td>
					</tr>
				</table>
			</TD>
		</TR>
	</TABLE>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		// 隐藏原始terminal，隐藏routes
		$("#routerDivT0").hide();
		$(".paramAddDivT0").hide();

		// 新增terminal按钮，新增时会克隆一个terminal
		$("#addTer").bind('click',function(e){
			var routerNum = $(".routerDiv").size();
			var cloneTm=$("#routerDivT0").clone();
			$("#routerFaDiv").append(cloneTm);
			cloneTm.attr("id","routerDiv"+routerNum);
			cloneTm.addClass("routerDiv");
			cloneTm.show();

			// 2020/4/1 将下面一句 cloneTm.find("#paramAddDivT0").show() 注释，原因是paramAddDivT0提交得时候里面的内容不能被提交
			// cloneTm.find("#paramAddDivT0").show();

			// 给新增的terminal里面的新增按钮，删除按钮，fatherDiv修改id
			cloneTm.find("#doAdd").attr("id","doAddButton"+routerNum);
			cloneTm.find("#doRemoveButton").attr("id","doRemoveButton"+routerNum);
			cloneTm.find("#fatherDivT0").attr("id","fatherDiv"+routerNum);
			cloneTm.find("#delete").bind('click',function(e){
				cloneTm.remove();
			});
			// 给新增按钮，删除按钮 新增绑定事件
			addBindToNewTer(routerNum);
		})


		// 为新增routes按钮增加绑定事件
		$("#doAddButton0").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv0").append(clone);
			clone.attr("id","paramAddDiv0");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		// 为删除terminal按钮增加绑定事件
		$("#doRemoveButton0").bind('click',function(e){
			$("#routerDiv0").remove();
		})

		$("#doAddButton1").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv1").append(clone);
			clone.attr("id","paramAddDiv1");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton1").bind('click',function(e){
			$("#routerDiv1").remove();
		})

		$("#doAddButton2").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv2").append(clone);
			clone.attr("id","paramAddDiv2");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton2").bind('click',function(e){
			$("#routerDiv2").remove();
		})

		$("#doAddButton3").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv3").append(clone);
			clone.attr("id","paramAddDiv3");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton3").bind('click',function(e){
			$("#routerDiv3").remove();
		})

		$("#doAddButton4").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv4").append(clone);
			clone.attr("id","paramAddDiv4");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton4").bind('click',function(e){
			$("#routerDiv4").remove();
		})

		$("#doAddButton5").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv5").append(clone);
			clone.attr("id","paramAddDiv5");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton5").bind('click',function(e){
			$("#routerDiv5").remove();
		})

		$("#doAddButton6").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv6").append(clone);
			clone.attr("id","paramAddDiv6");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton6").bind('click',function(e){
			$("#routerDiv6").remove();
		})

		$("#doAddButton7").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv7").append(clone);
			clone.attr("id","paramAddDiv7");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton7").bind('click',function(e){
			$("#routerDiv7").remove();
		})

		$("#doAddButton8").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv8").append(clone);
			clone.attr("id","paramAddDiv8");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton8").bind('click',function(e){
			$("#routerDiv8").remove();
		})

		$("#doAddButton9").bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$("#fatherDiv9").append(clone);
			clone.attr("id","paramAddDiv9");
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})
		$("#doRemoveButton9").bind('click',function(e){
			$("#routerDiv9").remove();
		})


		$(".paramAddDiv").each(function(i,e){
			$(e).find("#delete").bind('click',function(){
				$(e).remove();
			})
		})


		$("#exeButton").bind('click',function(e){

			if($(".routerDiv").length == 0){
				alert("无Terminal信息，请点击'增加Terminal'按钮 填写相应信息");
				return;
			}

			var exitEmptyRt = 1;
			$(".routerDiv").each(function(j,e){
				$(e).find("#Type").attr("name","routerTmListBak.routerTerminals["+j+"].type");
				$(e).find("#Softver").attr("name","routerTmListBak.routerTerminals["+j+"].Softver");
				$(e).find("#DNSSlect").attr("name","routerTmListBak.routerTerminals["+j+"].DNSSlect");
				$(e).find("#VLANID").attr("name","routerTmListBak.routerTerminals["+j+"].VLANID");
				var routeSize = $(e).find(".paramAddDiv").size();
				if (0 == routeSize){
					exitEmptyRt = 0;
				}
				$(e).find(".paramAddDiv").each(function(i,f){
					$(f).find("#Destination").attr("name","routerTmListBak.routerTerminals["+j+"].routers["+i+"].destination");
					$(f).find("#Netmask").attr("name","routerTmListBak.routerTerminals["+j+"].routers["+i+"].netmask");
					$(f).find("#Plane").attr("name","routerTmListBak.routerTerminals["+j+"].routers["+i+"].plane");

				})

			})

			if(exitEmptyRt == 0){
				alert("存在Terminal没有路由表信息，请点击'增加Route'按钮 填写相应信息");
				return;
			}

			var version = $.trim($("input[@name='version']").val());
			var routeVer = $.trim($("input[@name='routerTmListBak.routeVer']").val());
			if (version == routeVer){
				if (confirm('当前版本号未进行更改，请确认是否已修改?')) {
					if(version == routeVer){
						alert("当前版本号和已有版本号一致，不允许修改! 请更改版本号");
						return;
					}
				}else{
					return;
				}
			}
			var postUrl='<s:url value="/intelspeaker/intelSpeakerConfigMgrAction!saveNewstBak.action?type=1"/>';
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
					  if(res=='1'){
						  window.location.reload();
					  }
				  },
				  dataType: "application/x-www-form-urlencoded"
			});
		})
	})

	function addBindToNewTer(num){
		var dab = "#doAddButton"+num;
		var faD = "#fatherDiv"+num;
		var pAD = "#paramAddDiv"+num;
		var dRb = "#doRemoveButton"+num;
		var rD  = "#routerDiv"+num;

		$(dab).bind('click',function(e){
			var clone=$("#paramAddDivT0").clone();
			$(faD).append(clone);
			clone.attr("id",pAD);
			clone.addClass("paramAddDiv");
			clone.show();
			clone.find("#delete").bind('click',function(e){
				clone.remove();
			});
		})

		$(dRb).bind('click',function(e){
			$(rD).remove();
		})
	}
</script>
</html>
<%@ include file="../foot.jsp"%>
