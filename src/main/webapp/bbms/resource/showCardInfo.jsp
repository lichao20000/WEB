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
		<title>EVDO����Ϣ</title>
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
	$("div[@id='plugStat']").append("<FONT COLOR=\"red\">��ѯ�豸����״̬��......</FONT>");
	$("div[@id='useStat']").html("");
	$("div[@id='useStat']").append("<FONT COLOR=\"red\">��ѯ�豸ʹ��״̬��......</FONT>");
	$("div[@id='workMode']").html("");
	$("div[@id='workMode']").append("<FONT COLOR=\"red\">��ѯ�豸����ģʽ��......</FONT>");

	$.post(url,{
	               deviceId:deviceId
	           },function(ajax){
		           if (ajax == "1") {		
			           $("div[@id='plugStat']").html("");
			           $("div[@id='plugStat']").append("<FONT COLOR=\"green\">������λ</FONT>");	
			           var url2= "<s:url value='/bbms/resource/showCardInfo!searchCardUseStat.action'/>";	
			           $.post(url2,{
		                               deviceId:deviceId
	                               },function(ajax){
	                               	   var s=ajax.split(";"); 
	                               	   
	                               	   if("������ʹ��"==s[0]){ 
	                               	                           	          
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
												EVDO����ϸ��Ϣ
											</TH>
										</TR>
										<s:if test="dataCardInfoMap!=null&&uimCardInfoMap!=null">
										<TR bgcolor="#FFFFFF" height="20">
												<TD class=green_title2 align="center" colspan=4>
													EVDO��״̬��Ϣ
												</TD>
											</TR>
										<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														����״̬
													</TD>
													<TD>
														<DIV id="plugStat"></DIV>

													</TD>
													<TD class=column align="right">
														ʹ��״̬
													</TD>
													<TD>
														<DIV id="useStat"></DIV>
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														����ģʽ
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
													���ݿ�������Ϣ
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
����
													</TD>
													<TD width="30%">
<s:property value="dataCardInfoMap.modelName" />
													</TD>

												</TR>

												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														�ͺ�
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.vendorAlias" />
													</TD>
													<TD class=column align="right">
														Ӳ���汾
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.hardName" />
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														�̼��汾
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.firmName" />
													</TD>
													<TD class=column align="right">
														ע��ʱ��
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.completeTime" />
													</TD>
												</TR>
												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														��ʱ��
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.bindTime" />
													</TD>
													<TD class=column align="right">
														����ʱ��
													</TD>
													<TD>
														<s:property value="dataCardInfoMap.updateTime" />
													</TD>
												</TR>
												


												<!-- <TR bgcolor="#FFFFFF" height="20">
									<TD class=column align="right">
										����
									</TD>
									<TD>
										<textarea cols=40 rows=4 readonly></textarea>
									</TD>
									<TD class=column align="right">
										��ע
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
														<font color="red">��EVDO�����ݿ���Ϣ</font>
													</TD>
												</TR>
											</s:else>

											<TR bgcolor="#FFFFFF" height="20">
												<TD class=green_title2 align="center" colspan=4>
													UIM��������Ϣ
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
														�����ѹ
													</TD>
													<TD width="30%">
														<s:property value="uimCardInfoMap.voltage" />
													</TD>
												</TR>

												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														ע��ʱ��
													</TD>
													<TD>
														<s:property value="uimCardInfoMap.completeTime" />
													</TD>
													<TD class=column align="right">
														��ʱ��
													</TD>
													<TD>
														<s:property value="uimCardInfoMap.bindTime" />
													</TD>
												</TR>

												<TR bgcolor="#FFFFFF" height="20">
													<TD class=column align="right">
														����ʱ��
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
										����
									</TD>
									<TD>
										<textarea cols=40 rows=4 readonly></textarea>
									</TD>
									<TD class=column align="right">
										��ע
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
														<font color="red">��EVDO��UIM����Ϣ</font>
													</TD>
												</TR>
											</s:else>
										</s:if>
										<s:else>
											<TR bgcolor="#FFFFFF" height="20">
												<TD class=column align="center" colspan=4>
													<font color="red">���豸���߱�EVDOҵ��</font>
												</TD>
											</TR>
										</s:else>
										<TR>
											<TD colspan="4" align="center" class=foot>
												<INPUT TYPE="submit" value=" �� �� " class=btn
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