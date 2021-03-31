<%@ page language="java" import="java.util.*" pageEncoding="gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">

        <link rel="stylesheet" href="../../css/css_green.css" type="text/css">
		<!-- <link rel="stylesheet" href="../../css/tree.css" type="text/css">  -->
		<link rel="stylesheet" href="../../css/tab.css" type="text/css">
		<link rel="stylesheet" href="../../css/listview.css" type="text/css">
		<link rel="stylesheet" href="../../css/css_ico.css" type="text/css">
		<link rel="stylesheet" href="../../css/liulu.css" type="text/css">
		<link rel="stylesheet" href="../../css/user-defined.css"
			type="text/css">
		<title>EVDO卡信息</title>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckFormForm.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.blockUI.js"></SCRIPT>
		<SCRIPT LANGUAGE="JavaScript">
$(document).ready(function(){

    var deviceId = "<s:property value="dataCardInfoMap.deviceId"/>";
    var dataCardId = "<s:property value="dataCardInfoMap.dataCardId"/>";
    if(deviceId!=""){
    var url = "<s:url value='/bbms/resource/showCardInfo!searchCardPlugStat.action'/>";
   
    $("div[@id='plugStat']").html("");
	$("div[@id='plugStat']").append("<FONT COLOR=\"red\">查询设备插入状态中......</FONT>");
	$("div[@id='useStat']").html("");
	$("div[@id='useStat']").append("<FONT COLOR=\"red\">查询设备使用状态中......</FONT>");
	$("div[@id='workMode']").html("");
	$("div[@id='workMode']").append("<FONT COLOR=\"red\">查询设备工作模式中......</FONT>");

	$.post(url,{
	               deviceId:deviceId
	           },function(ajax){
		           if (ajax == "1") {		
			           $("div[@id='plugStat']").html("");
			           $("div[@id='plugStat']").append("<FONT COLOR=\"green\">卡已在位</FONT>");	
			           var url2= "<s:url value='/bbms/resource/showCardInfo!searchCardUseStat.action'/>";	
			           $.post(url2,{
		                               deviceId:deviceId
	                               },function(ajax){
	                               	   var s=ajax.split(";"); 
	                               	   
	                               	   if("卡正在使用"==s[0]){ 
	                               	                           	          
	                                   $("div[@id='useStat']").html("");
	                                   $("div[@id='useStat']").append("<FONT COLOR=\"green\">"+s[0]+"</FONT>");
	                                   $("div[@id='workMode']").html("");
	                                   $("div[@id='workMode']").append("<FONT COLOR=\"green\">"+s[1]+"</FONT>");
	                                   
	                                   }else{
	                                   
	                                   $("div[@id='useStat']").html("");
	                                   $("div[@id='useStat']").append("<FONT COLOR=\"red\">"+s[0]+"</FONT>");
	                                   $("div[@id='workMode']").html("");
	                                   $("div[@id='workMode']").append("<FONT COLOR=\"red\">"+s[1]+"</FONT>");	                               
	                                   
	                                   }
			                         });			
		           } else{
		           
		               $("div[@id='plugStat']").html("");
			           $("div[@id='plugStat']").append("<FONT COLOR=\"#FF0000\">"+ajax+"</FONT>");	
			           $("div[@id='useStat']").html("");
	                   $("div[@id='useStat']").append("<FONT COLOR=\"red\">---</FONT>");
	                   $("div[@id='workMode']").html("");
	                   $("div[@id='workMode']").append("<FONT COLOR=\"red\">---</FONT>");
	                                   		
		           }
	});
   } 
});
</SCRIPT>
	</head>
	<body>
		<center>
			<br>
			<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
				<TR>
					<TD>
						<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
							align="center">
							<TR>
								<TD bgcolor=#999999>
									<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
										<TR>
											<TH colspan="4" align="center">
												EVDO卡详细信息
											</TH>
										</TR>
										<s:if test="dataCardInfoMap!=null&&uimCardInfoMap!=null">
										<TR bgcolor="#FFFFFF" height="20">
												<TD class=green_title2 align="center" colspan=4>
													EVDO卡状态信息
												</TD>
											</TR>
										<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														插入状态
													</TD>
													<TD>
														<DIV id="plugStat"></DIV>

													</TD>
													<TD class=column align="right">
														使用状态
													</TD>
													<TD>
														<DIV id="useStat"></DIV>
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														工作模式
													</TD>
													<TD>
														<DIV id="workMode"></DIV>

													</TD>
													<TD class=column align="right">
														
													</TD>
													<TD width="30%">
														
													</TD>
												</TR>
											<TR bgcolor="#FFFFFF" height="20">
												<TD class=green_title2 align="center" colspan=4>
													数据卡基本信息
												</TD>
											</TR>
											<s:if test="dataCardInfoMap!=null">
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right" width="20%">
														ESN
													</TD>
													<TD width="30%">
														<s:property value="dataCardInfoMap.dataCardEsn" />
													</TD>
													<TD class=column align="right">
厂商
													</TD>
													<TD width="30%">
<s:property value="dataCardInfoMap.modelName" />
													</TD>

												</TR>

												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														型号
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.vendorAlias" />
													</TD>
													<TD class=column align="right">
														硬件版本
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.hardName" />
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														固件版本
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.firmName" />
													</TD>
													<TD class=column align="right">
														注册时间
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.completeTime" />
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														绑定时间
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.bindTime" />
													</TD>
													<TD class=column align="right">
														更新时间
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.updateTime" />
													</TD>
												</TR>
												


												<!-- <TR bgcolor="#FFFFFF" height="20">
									<TD class=column align="right">
										描述
									</TD>
									<TD>
										<textarea cols=40 rows=4 readonly></textarea>
									</TD>
									<TD class=column align="right">
										备注
									</TD>
									<TD>
										<textarea cols=40 rows=4 readonly></textarea>
									</TD>
								</TR>
 -->
											</s:if>
											<s:else>
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="center" colspan=4>
														<font color="red">该EVDO无数据卡信息</font>
													</TD>
												</TR>
											</s:else>

											<TR bgcolor="#FFFFFF" height="20">
												<TD class=green_title2 align="center" colspan=4>
													UIM卡基本信息
												</TD>
											</TR>
											<s:if test="uimCardInfoMap!=null">
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right" width="20%">
														IMSI
													</TD>
													<TD width="30%">
														<s:property value="uimCardInfoMap.uimCardImsi" />
													</TD>
													<TD class=column align="right">
														供电电压
													</TD>
													<TD width="30%">
														<s:property value="uimCardInfoMap.voltage" />
													</TD>
												</TR>

												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														注册时间
													</TD>
													<TD>
														<s:property value="uimCardInfoMap.completeTime" />
													</TD>
													<TD class=column align="right">
														绑定时间
													</TD>
													<TD>
														<s:property value="uimCardInfoMap.bindTime" />
													</TD>
												</TR>

												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														更新时间
													</TD>
													<TD width=140>
														<s:property value="uimCardInfoMap.updateTime" />
													</TD>
													<TD class=column align="right">

													</TD>
													<TD></TD>
												</TR>
												<!-- 
								<TR bgcolor="#FFFFFF" height="20">
									<TD class=column align="right">
										描述
									</TD>
									<TD>
										<textarea cols=40 rows=4 readonly></textarea>
									</TD>
									<TD class=column align="right">
										备注
									</TD>
									<TD>
										<textarea cols=40 rows=4 readonly></textarea>
									</TD>
								</TR>
 -->
											</s:if>
											<s:else>
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="center" colspan=4>
														<font color="red">该EVDO无UIM卡信息</font>
													</TD>
												</TR>
											</s:else>
										</s:if>
										<s:else>
											<TR bgcolor="#FFFFFF" height="20">
												<TD class=column align="center" colspan=4>
													<font color="red">该设备不具备EVDO业务</font>
												</TD>
											</TR>
										</s:else>
										<TR>
											<TD colspan="4" align="center" class=foot>
												<INPUT TYPE="submit" value=" 关 闭 " class=btn
													onclick="javascript:window.close();">
											</TD>
										</TR>

									</TABLE>


								</TD>
							</TR>
						</TABLE>
					</TD>
				</TR>
				<TR>
					<TD HEIGHT=20>
						&nbsp;
						<div id="setPWDIV"></div>
					</TD>
				</TR>
			</TABLE>
		</center>
	</body>
</html>
<%@ include file="../foot.jsp"%>