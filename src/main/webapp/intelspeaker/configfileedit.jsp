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
										<button type="button" id="doAddButton"
											class=btn>&nbsp;��&nbsp;�� &nbsp;</button>
									</td>
								</tr>
							</table>
						</td>
					</tr>

					<tr bgcolor="#FFFFFF">

						<td>
						<form id="form">
<%-- 						<s:iterator value="routerTmList.routerTerminals" var="routerTerminal" status="stuts">  --%>
						<input type="hidden" name="version" value="${routerTmList.routeVer}"/>
						<input type="hidden" name="param" value="" />
							<TABLE id="table_showConfig" width="100%" border=0 cellspacing=0
								cellpadding=0 align="center" class="querytable">
								<tr>
									<th align="center" width="100%">�༭·�ɱ�</th>
								</tr>
								<tr>
									<td align="left" class=column width="20%">��Routes��ǩ�������·�ɱ�İ汾��<input type="text" id="RouteVer"
										name="routerTmList.routeVer" value="${routerTmList.routeVer}" /></td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td colspan="4">
										<div id="selectedDev">
<!-- 											<form action=""> -->
												<table border=0 cellspacing=1 cellpadding=2 width="100%">
													<TR bgcolor="#FFFFFF">
														<td align="right" class=column width="20%">����������</td>
														<td width="30%"><input type="text" id="Type"
															name="routerTmList.routerTerminals.type" value="${routerTmList.routerTerminals.type}" /></td>
														<td align="right" class=column width="30%">�����й���汾��</td>
														<td width="70%"><input type="text" id="Softver"
															name="routerTmList.routerTerminals.softver" value="${routerTmList.routerTerminals.softver}" /></td>
													</TR>
													<TR bgcolor="#FFFFFF">
														<td align="right" class=column width="20%">����DNS������������ƽ��</td>
														<td width="30%"><select name="routerTmList.routerTerminals.DNSSlect"
															id="DNSSlect" cssClass="bk" value="${routerTmList.routerTerminals.DNSSlect}">
																<option value="A">������Aƽ��</option>
																<option value="B">IPTVר������Bƽ��</option>
														</select></td>
														<td align="right" class=column width="30%">Bƽ������VLAN��ʶ</td>
														<td width="70%"><input type="text" id="VLANID"
															name="routerTmList.routerTerminals.VLANID" value="${routerTmList.routerTerminals.VLANID}" /></td>
													</TR>
<!-- 													<TR bgcolor="#FFFFFF"> -->
<!-- 														<td align="right" class=column width="20%">��Routes��ǩ�������·�ɱ�İ汾��</td> -->
<!-- 														<td width="30%"><input type="text" id="RouteVer" -->
<%-- 															name="routerTmList.routeVer" value="${routerTmList.routeVer}" /></td> --%>
<!-- 													</TR> -->
												</table>
<!-- 											</form> -->
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
<!-- 																	<a  id="delete"> ɾ�� | </a> -->
																	<button type="button" id="delete" name="delete" class=btn>ɾ��</button>
															</span>��·�ɱ���������Ŀ���ַ</td>
															<td width="70%" colspan="3"><input type="text"
																id="Destination" name="Destination"
																value=''
																style="width: 800px;"></td>
														</TR>
														<TR bgcolor="#FFFFFF">
															<td align="right" class=column width="20%">��������</td>
															<td width="30%"><input type="text" id="Netmask"
																name="Netmask"  value="" /></td>
															<td align="right" class=column width="30%">����ƽ���ʶ</td>
															<td width="70%"><select name="Plane" value=''
																id="Plane" cssClass="bk">
																	<option value="A">������Aƽ��</option>
																	<option value="B">IPTVר������Bƽ��</option>
															</select></td>
														</TR>
													</table>
												</div>
											 <s:iterator value="routerTmList.routerTerminals.routers" var="router" status="stuts">
<%-- 											   		<s:property value="#id.attrName" />  --%>
													   		<div id="paramAddDiv" class="paramAddDiv">
																	<table border=0 cellspacing=1 cellpadding=2 width="100%">
																		<TR bgcolor="#FFFFFF">
																			<td nowrap align="right" class=column width="20%"><span>
<!-- 																					<a id="delete"> ɾ�� | </a> -->
																					<button type="button" id="delete" name="delete" class=btn>ɾ��</button>
																			</span>��·�ɱ���������Ŀ���ַ</td>
																			<td width="70%" colspan="3"><input type="text"
																				id="Destination" name="Destination"
																				value='<s:property value="#router.destination" />'
																				style="width: 800px;"></td>
																		</TR>
																		<TR bgcolor="#FFFFFF">
																			<td align="right" class=column width="20%">��������</td>
																			<td width="30%"><input type="text" id="Netmask"
																				name="Netmask" value="<s:property value='#router.netmask' />" /></td>
																			<td align="right" class=column width="30%">����ƽ���ʶ<s:property value='#router.plane' /></td>
																			<td width="70%"><select name="Plane"
																				id="Plane" cssClass="bk">
																					<option value="A">������Aƽ��</option>
																					<option value="B" <s:if test='#router.plane=="B"'>selected</s:if>>IPTVר������Bƽ��</option>
																			</select></td>
																		</TR>
																	</table>
													</div>
											 </s:iterator>

										</div>


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
									</TD>
								</TR>
							</TABLE>
<%-- 							</s:iterator> --%>
							</form>
						</td>


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
				$(e).find("#Destination").attr("name","routerTmList.routerTerminals.routers["+i+"].destination");
				$(e).find("#Netmask").attr("name","routerTmList.routerTerminals.routers["+i+"].netmask");
				$(e).find("#Plane").attr("name","routerTmList.routerTerminals.routers["+i+"].plane");

			})
			var version = $.trim($("input[@name='version']").val());
			var routeVer = $.trim($("input[@name='routerTmList.routeVer']").val());
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
			var postUrl='<s:url value="/intelspeaker/intelSpeakerConfigMgrAction!saveNewst.action?type=1"/>';
			$.ajax({
				  type: 'POST',
				  url: postUrl,
				  data: $("#form").serialize(),
// 				  data: $("#form").serialize(),
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
</script>
</html>
<%@ include file="../foot.jsp"%>
