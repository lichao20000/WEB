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
<title>�༭����·�ɱ�汾��Ϣ</title>
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
										�༭����·�ɱ�汾��Ϣ</td>
									<td align="right">
										<button type="button" id="addTer" class=btn>����Terminal</button>
<!-- 										<button type="button" id="addTer" class=btn>&nbsp;��&nbsp;�� &nbsp;</button> -->
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<th align="center" width="100%">�༭·�ɱ�</th>

					</tr>

					<tr bgcolor="#FFFFFF">

						<td>
							<form id="form">
								<input type="hidden" name="version" value="${routerTmListBak.routeVer}" />
								<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" >
									<tr>
										<td align="left" class=column width="20%">��Routes��ǩ�������·�ɱ�İ汾��<input
											type="text" id="RouteVer" name="routerTmListBak.routeVer"
											value="${routerTmListBak.routeVer}" /></td>
									</tr>
								</table>
								<div id="routerFaDiv" class="routerFaDiv">
									<div id="routerDivT0">
										<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0 cellpadding=0 align="center" class="querytable">
											<tr>
												<td align="right">
													<button type="button" id="doRemoveButton" class=btn>ɾ��Terminal</button>
													<button type="button" id="doAdd" class=btn>����Route</button>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														<table border=0 cellspacing=1 cellpadding=2 width="100%">
															<TR bgcolor="#FFFFFF">
																<td align="right" class=column width="20%">����������</td>
																<td width="30%"><input type="text" id="Type" name="Type"
																	value='' /></td>
																<td align="right" class=column width="30%">�����й���汾��</td>
																<td width="70%"><input type="text" id="Softver" name="Softver"
																	value='' /></td>
															</TR>
															<TR bgcolor="#FFFFFF">
																<td align="right" class=column width="20%">����DNS������������ƽ��</td>
																<td width="30%"><select name="DNSSlect" id="DNSSlect" cssClass="bk"
																	value=''>
																		<option value="A">������Aƽ��</option>
																		<option value="B">IPTVר������Bƽ��</option>
																</select></td>
																<td align="right" class=column width="30%">Bƽ������VLAN��ʶ</td>
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
																				class=btn>ɾ��Route</button>
																		</span>��·�ɱ���������Ŀ���ַ
																	</td>
																	<td width="70%" colspan="3">
																		<input type="text" id="Destination" name="Destination" value='' style="width: 800px;">
																	</td>
																</TR>
																<TR bgcolor="#FFFFFF">
																	<td align="right" class=column width="20%">��������</td>
																	<td width="30%"><input type="text" id="Netmask" name="Netmask" value="" />
																	</td>
																	<td align="right" class=column width="30%">����ƽ���ʶ</td>
																	<td width="70%">
																		<select name="Plane" value='' id="Plane" cssClass="bk">
																			<option value="A">������Aƽ��</option>
																			<option value="B">IPTVר������Bƽ��</option>
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
													<button type="button" id="doRemoveButton<s:property value='#rtl.index'/>" class=btn>ɾ��Terminal
														</button>
													<button type="button" id="doAddButton<s:property value='#rtl.index'/>" class=btn>����Route
														</button>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														<table border=0 cellspacing=1 cellpadding=2 width="100%">
															<TR bgcolor="#FFFFFF">
																<td align="right" class=column width="20%">����������</td>
																<td width="30%"><input type="text" id="Type" name="Type"
																	value='<s:property value="#routerTerminal.type" />' /></td>
																<td align="right" class=column width="30%">�����й���汾��</td>
																<td width="70%"><input type="text" id="Softver" name="Softver"
																	value='<s:property value="#routerTerminal.softver" />' /></td>
															</TR>
															<TR bgcolor="#FFFFFF">
																<td align="right" class=column width="20%">����DNS������������ƽ��</td>
																<td width="30%"><select name="DNSSlect" id="DNSSlect" cssClass="bk"
																	value='<s:property value="#routerTerminal.dnsslect" />'>
																		<option value="A">������Aƽ��</option>
																		<option value="B">IPTVר������Bƽ��</option>
																</select></td>
																<td align="right" class=column width="30%">Bƽ������VLAN��ʶ</td>
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
																				class=btn>ɾ��Route</button>
																		</span>��·�ɱ���������Ŀ���ַ
																	</td>
																	<td width="70%" colspan="3">
																		<input type="text" id="Destination" name="Destination" value='' style="width: 800px;">
																	</td>
																</TR>
																<TR bgcolor="#FFFFFF">
																	<td align="right" class=column width="20%">��������</td>
																	<td width="30%"><input type="text" id="Netmask" name="Netmask" value="" />
																	</td>
																	<td align="right" class=column width="30%">����ƽ���ʶ</td>
																	<td width="70%">
																		<select name="Plane" value='' id="Plane" cssClass="bk">
																			<option value="A">������Aƽ��</option>
																			<option value="B">IPTVר������Bƽ��</option>
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
																					<button type="button" id="delete" name="delete" class=btn>ɾ��Route</button>
																			</span>��·�ɱ���������Ŀ���ַ
																		</td>
																		<td width="70%" colspan="3">
																			<input type="text" id="Destination" name="Destination" value='<s:property value="#router.destination" />'
																			style="width: 800px;">
																		</td>
																	</TR>
																	<TR bgcolor="#FFFFFF">
																		<td align="right" class=column width="20%">��������</td>
																		<td width="30%"><input type="text" id="Netmask" name="Netmask"
																			value="<s:property value='#router.netmask' />" />
																		</td>
																		<td align="right" class=column width="30%">����ƽ���ʶ</td>
																		<td width="70%">
																			<select name="Plane" id="Plane" cssClass="bk">
																				<option value="A">������Aƽ��</option>
																				<option value="B" <s:if test='#router.plane=="B"'>selected</s:if>>
																					IPTVר������Bƽ��
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
											class=btn>&nbsp;��&nbsp;��&nbsp;</button>
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
		// ����ԭʼterminal������routes
		$("#routerDivT0").hide();
		$(".paramAddDivT0").hide();

		// ����terminal��ť������ʱ���¡һ��terminal
		$("#addTer").bind('click',function(e){
			var routerNum = $(".routerDiv").size();
			var cloneTm=$("#routerDivT0").clone();
			$("#routerFaDiv").append(cloneTm);
			cloneTm.attr("id","routerDiv"+routerNum);
			cloneTm.addClass("routerDiv");
			cloneTm.show();

			// 2020/4/1 ������һ�� cloneTm.find("#paramAddDivT0").show() ע�ͣ�ԭ����paramAddDivT0�ύ��ʱ����������ݲ��ܱ��ύ
			// cloneTm.find("#paramAddDivT0").show();

			// ��������terminal�����������ť��ɾ����ť��fatherDiv�޸�id
			cloneTm.find("#doAdd").attr("id","doAddButton"+routerNum);
			cloneTm.find("#doRemoveButton").attr("id","doRemoveButton"+routerNum);
			cloneTm.find("#fatherDivT0").attr("id","fatherDiv"+routerNum);
			cloneTm.find("#delete").bind('click',function(e){
				cloneTm.remove();
			});
			// ��������ť��ɾ����ť �������¼�
			addBindToNewTer(routerNum);
		})


		// Ϊ����routes��ť���Ӱ��¼�
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
		// Ϊɾ��terminal��ť���Ӱ��¼�
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
				alert("��Terminal��Ϣ������'����Terminal'��ť ��д��Ӧ��Ϣ");
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
				alert("����Terminalû��·�ɱ���Ϣ������'����Route'��ť ��д��Ӧ��Ϣ");
				return;
			}

			var version = $.trim($("input[@name='version']").val());
			var routeVer = $.trim($("input[@name='routerTmListBak.routeVer']").val());
			if (version == routeVer){
				if (confirm('��ǰ�汾��δ���и��ģ���ȷ���Ƿ����޸�?')) {
					if(version == routeVer){
						alert("��ǰ�汾�ź����а汾��һ�£��������޸�! ����İ汾��");
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
					  var alertStr="ʧ�ܣ�";
					  if(res=='-2'){
						  alertStr= "�ð汾�ļ��Ѵ���";
					  }else if(res=='-3'){
						  alertStr= "��������ǰ�汾�ļ�ʧ��";
					  }else if(res=='-4'){
						  alertStr= "д���°汾�ļ�ʧ��";
					  }else if(res=='-1'){
						  alertStr= "�����������ļ�";
					  }else if(res=='1'){
						  alertStr= "�ɹ�";
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
