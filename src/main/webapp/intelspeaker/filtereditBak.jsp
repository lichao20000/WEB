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
<title>�༭���°������汾��Ϣ</title>
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
										�༭���°������汾��Ϣ</td>
									<td align="right">
										<button type="button" id="addTer" class=btn>����Terminal</button>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<th align="center" width="100%">�༭������</th>

					</tr>

					<tr bgcolor="#FFFFFF">

						<td>
							<form id="form">
								<input type="hidden" name="version" value="${filterTmList.filterVer}" />
								<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" >
									<tr>
										<td align="left" class=column width="20%">��Filters��ǩ������İ������İ汾��<input
											type="text" id="FilterVer" name="filterTmList.filterVer"
											value="${filterTmList.filterVer}" /></td>
									</tr>
								</table>
								<div id="filterFaDiv" class="filterFaDiv">
									<div id="filterDivT0">
										<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0 cellpadding=0 align="center" class="querytable">
											<tr>
												<td align="right">
													<button type="button" id="doRemoveButton" class=btn>ɾ��Terminal</button>
													<button type="button" id="doAdd" class=btn>����Filter</button>
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
																				class=btn>ɾ��Filter</button>
																		</span>����������������Ŀ���ַ
																	</td>
																	<td width="70%" colspan="1">
																		<input type="text" id="Destination" name="Destination" value='' style="width: 800px;">
																	</td>
																</TR>
																<TR bgcolor="#FFFFFF">
																	<td align="right" class=column width="20%">��������</td>
																	<td width="30%"><input type="text" id="Netmask" name="Netmask" value="" />
																	</td>
																</TR>
															</table>
														</div>
													</div>

												</TD>
											</TR>
										</TABLE>
									</div>

								<s:iterator value="filterTmList.filterTerminals" var="filterTerminal" status="rtl">

									<div id="filterDiv<s:property value='#rtl.index'/>" class="filterDiv">
										<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0 cellpadding=0 align="center" class="querytable">
											<tr>
												<td align="right">
													<button type="button" id="doRemoveButton<s:property value='#rtl.index'/>" class=btn>ɾ��Terminal
														</button>
													<button type="button" id="doAddButton<s:property value='#rtl.index'/>" class=btn>����Filter
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
																	value='<s:property value="#filterTerminal.type" />' /></td>
																<td align="right" class=column width="30%">�����й���汾��</td>
																<td width="70%"><input type="text" id="Softver" name="Softver"
																	value='<s:property value="#filterTerminal.softver" />' /></td>
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
																				class=btn>ɾ��Filter</button>
																		</span>����������������Ŀ���ַ
																	</td>
																	<td width="70%" colspan="1">
																		<input type="text" id="Destination" name="Destination" value='' style="width: 800px;">
																	</td>
																</TR>
																<TR bgcolor="#FFFFFF">
																	<td align="right" class=column width="20%">��������</td>
																	<td width="30%"><input type="text" id="Netmask" name="Netmask" value="" />
																	</td>
																</TR>
															</table>
														</div>
														<s:iterator value="#filterTerminal.filters" var="filter" status="stuts">
															<div id="paramAddDiv<s:property value='#rtl.index'/>" class="paramAddDiv">
																<table border=0 cellspacing=1 cellpadding=2 width="100%">
																	<TR bgcolor="#FFFFFF">
																		<td nowrap align="right" class=column width="20%">
																			<span>
																					<button type="button" id="delete" name="delete" class=btn>ɾ��Filter</button>
																			</span>����������������Ŀ���ַ
																		</td>
																		<td width="70%" colspan="1">
																			<input type="text" id="Destination" name="Destination" value='<s:property value="#filter.destination" />'
																			style="width: 800px;">
																		</td>
																	</TR>
																	<TR bgcolor="#FFFFFF">
																		<td align="right" class=column width="20%">��������</td>
																		<td width="30%"><input type="text" id="Netmask" name="Netmask"
																			value="<s:property value='#filter.netmask' />" />
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
		$("#filterDivT0").hide();
		$(".paramAddDivT0").hide();

		// ����terminal��ť������ʱ���¡һ��terminal
		$("#addTer").bind('click',function(e){
			var routerNum = $(".filterDiv").size();
			var cloneTm=$("#filterDivT0").clone();
			$("#filterFaDiv").append(cloneTm);
			cloneTm.attr("id","filterDiv"+routerNum);
			cloneTm.addClass("filterDiv");
			cloneTm.show();
			cloneTm.find("#paramAddDivT0").show();
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
			$("#filterDiv0").remove();
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
			$("#filterDiv1").remove();
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
			$("#filterDiv2").remove();
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
			$("#filterDiv3").remove();
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
			$("#filterDiv4").remove();
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
			$("#filterDiv5").remove();
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
			$("#filterDiv6").remove();
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
			$("#filterDiv7").remove();
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
			$("#filterDiv8").remove();
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
			$("#filterDiv9").remove();
		})


		$(".paramAddDiv").each(function(i,e){
			$(e).find("#delete").bind('click',function(){
				$(e).remove();
			})
		})


		$("#exeButton").bind('click',function(e){
			var exitEmptyRt = 1;
			$(".filterDiv").each(function(j,e){
				$(e).find("#Type").attr("name","filterTmList.filterTerminals["+j+"].type");
				$(e).find("#Softver").attr("name","filterTmList.filterTerminals["+j+"].Softver");
				var routeSize = $(e).find(".paramAddDiv").size();
				if (0 == routeSize){
					exitEmptyRt = 0;
				}
				$(e).find(".paramAddDiv").each(function(i,f){
					$(f).find("#Destination").attr("name","filterTmList.filterTerminals["+j+"].filters["+i+"].destination");
					$(f).find("#Netmask").attr("name","filterTmList.filterTerminals["+j+"].filters["+i+"].netmask");
				})

			})
			if(exitEmptyRt == 0){
				alert("����terminalû�а�������Ϣ����ȷ�ϣ�");
				return;
			}

			var version = $.trim($("input[@name='version']").val());
			var routeVer = $.trim($("input[@name='filterTmList.filterVer']").val());
			if (version == routeVer){
				if (confirm('��ǰ�汾��δ���и��ģ���ȷ���Ƿ����޸�?')) {
					if(version == routeVer){
						alert("��ǰ�汾�ź����а汾��һ�£��������޸�!");
						return;
					}
				}else{
					return;
				}
			}
			var postUrl='<s:url value="/intelspeaker/intelSpeakerConfigMgrAction!saveNewstBak.action?type=2"/>';
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
		var rD  = "#filterDiv"+num;

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
