<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GBK">
		<link href="<s:url value="/css3/global.css"/>" rel="stylesheet"
			type="text/css">
		<link href="<s:url value="/css3/c_table.css"/>" rel="stylesheet"
			type="text/css">
			<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
		<SCRIPT type="text/javascript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
		<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
		<lk:res />
		<%
		String statu=request.getParameter("statu");
		%>
		<SCRIPT LANGUAGE="JavaScript">
		/** iframe�Զ���Ӧҳ�� **/
		//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
		//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
		//����iframe��ID
		var iframeids=["dataForm"];

		//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
		var iframehide="yes";

		function dyniframesize() 
		{
			var dyniframe=new Array();
			for (var i=0; i<iframeids.length; i++)
			{
				if (document.getElementById)
				{
					//�Զ�����iframe�߶�
					dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
					if (dyniframe[i] && !window.opera)
					{
		     			dyniframe[i].style.display="block";
		     			//����û����������NetScape
		     			if (dyniframe[i].contentDocument && dyniframe[i].contentDocument.body.offsetHeight)
		      				dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight; 
		      			//����û����������IE
		     			else if (dyniframe[i].Document && dyniframe[i].Document.body.scrollHeight) 
		      				dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
		   			 }
		   		}
				//�����趨�Ĳ���������֧��iframe�����������ʾ����
				if ((document.all || document.getElementById) && iframehide=="no")
				{
					var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i]);
		    		tempobj.style.display="block";
				}
			}
		}

		$(function(){
			//setValue();
			dyniframesize();
		});

		$(window).resize(function(){
			dyniframesize();
		});
		$(function(){
		document.batchexform.submit();
		});
		function query()
		{
			var taskName = $("input[name=taskName]").val();
			var acc_loginname = $("input[name=acc_loginname]").val();
			var startTime = $("#start_time").val();
			var endTime = $("#end_time").val();
			var url = "<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!init.action'/>?taskName="+taskName+"&acc_loginname="+acc_loginname+"&startTime="+startTime+"&endTime="+endTime;
			document.batchexform.submit();
		}
			
</SCRIPT>

	</head>
	<body>
		<form action="<s:url value='/gtms/stb/resource/OpenDeviceShowPictureNew!init.action'/>" method="post" enctype="multipart/form-data" name="batchexform" target="dataForm">
			<td HEIGHT=20>&nbsp;</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" name="statu" id="statu" value="<%=statu%>"> 
				<table class="green_gargtd">
					<tr>
						<td width="162" align="center" class="title_bigwhite">��������ѯ</td>
						<td>
							<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12" />
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>
				<table class="querytable">
					<tr>
						<th colspan="4">��������ѯ</th>
					</tr>
						<TR>
							<td class="title_1" width="15%">
					��ʼʱ��
				</td>
					<td width="35%">
					<input type="text" name="startTime" id="start_time" class='bk' readonly
						value="<s:property value='startTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.startTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��" />
				</td>
							<td class="title_1" width="15%">
					����ʱ��
				</td>
				<td width="35%">
					<input type="text" name="endTime" id="end_time" class='bk' readonly
						value="<s:property value='endTime'/>">
					<img name="shortDateimg"
						onClick="WdatePicker({el:document.batchexform.endTime,dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
						src="../../../images/dateButton.png" width="15" height="12"
						border="0" alt="ѡ��" />
				</td>
					</TR>
					<TR>
						<TD class=title_1 width="15%" align='right'>����</TD>
						<TD width="35%">
							<input type="text" name="taskName" id="taskName" class="bk" value="" size="40">
						</TD>
					<%-- 	<%if(!"cq_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
						<TD class=title_1 width="15%" align='right'>������</TD>
						<TD width="35%">
							<input type="text" name="acc_loginname" id="acc_loginname" class="bk" value="" size="40">
						</TD>
						<%} %> --%>
					</TR>
					<TR>
						<td colspan="4" align="right" class=foot>
								<button onclick="query()">&nbsp;�� ѯ&nbsp;</button>
									&nbsp;&nbsp;&nbsp;&nbsp;
						</td> 
					</TR>
				</table>
			</td>
		</tr>
		<tr>
			<td height="25" id="resultStr"></td>
		</tr>
		<tr>
			<td>
				<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
			</td>
		</tr>
	</table>
</form>
</body>
