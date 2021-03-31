<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>

<lk:res />
<script language="JavaScript">
		window.onload = function(){
		 initTime();
		}
		function initTime(){
			var vDate = new Date();
			
			var y  = vDate.getFullYear();
			var m  = vDate.getMonth()+1;
			var d  = vDate.getDate();
			var h  = vDate.getHours(); 
			var mm = vDate.getMinutes();
			var s  = vDate.getSeconds();
			var strM = m<10?"0"+m:""+m;
			var strD = d<10?"0"+d:""+d;
			
			document.frm.starttime.value = y+"-"+m+"-"+d+" 00:00:00";
			document.frm.endtime.value = y+"-"+m+"-"+d+" "+h+":"+mm+":"+s;
		}
		function doQuery(){
		    $("tr[@id='trData']").show();
		    $("#btn").attr("disabled",true);
		    $("div[@id='QueryData']").html("����Ŭ��Ϊ��ͳ�ƣ����Ե�....");
		    var mainForm = document.getElementById("selectForm");
		    mainForm.submit();
// 		    var starttime = $.trim($("input[@name='starttime']").val());   
// 		    var endtime = $.trim($("input[@name='endtime']").val());  
// 		    var url = "<s:url value='/ids/idsStatusQuery!doQueryStatus.action'/>"; 
// 			$.post(url,{
// 				starttime : starttime,
// 				endtime : endtime
// 			},function(ajax){
// 				$("#btn").attr("disabled",false);
// 			    $("div[@id='QueryData']").html("");
// 				$("div[@id='QueryData']").append(ajax);
// 			});
		}
		
		//** iframe�Զ���Ӧҳ�� **//
		//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
		//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
		//����iframe��ID
		var iframeids = [ "dataForm" ]

		//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
		var iframehide = "yes"

		function dyniframesize() {
			var dyniframe = new Array()
			for (i = 0; i < iframeids.length; i++) {
				if (document.getElementById) {
					//�Զ�����iframe�߶�
					dyniframe[dyniframe.length] = document
							.getElementById(iframeids[i]);
					if (dyniframe[i] && !window.opera) {
						dyniframe[i].style.display = "block"
						//����û����������NetScape
						if (dyniframe[i].contentDocument
								&& dyniframe[i].contentDocument.body.offsetHeight)
							dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
						//����û����������IE
						else if (dyniframe[i].Document
								&& dyniframe[i].Document.body.scrollHeight)
							dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
					}
				}
				//�����趨�Ĳ���������֧��iframe�����������ʾ����
				if ((document.all || document.getElementById) && iframehide == "no") {
					var tempobj = document.all ? document.all[iframeids[i]]
							: document.getElementById(iframeids[i])
					tempobj.style.display = "block"
				}
			}
		}

		$(function() {
			dyniframesize();
		});

		$(window).resize(function() {
			dyniframesize();
		});
	</script>

</head>

<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>״̬��Ϣ�����Ʋ�ѯ</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12">ʱ��Ϊ���������ʱ��</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="selectForm" method="post"
				action="<s:url value='/ids/idsStatusQuery!doQueryStatus.action'/>"
				target="dataForm">
				<table class="querytable">
					<tr>
						<th colspan=4>�豸��ѯ</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">��ʼʱ��</td>
						<td><input type="text" name="starttime" class='bk' readonly
							value="<s:property value="starttime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="ѡ��" /></td>
						<td class=column align=center width="15%">����ʱ��</td>
						<td><input type="text" name="endtime" class='bk' readonly
							value="<s:property value="endtime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="ѡ��" /></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button id="btn" onclick="doQuery();">&nbsp;��&nbsp;ѯ&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td>
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����Ŭ��Ϊ����ѯ�����Ե�....</div>
		</td>
	</tr>
	<tr>
		<td height="80%"><iframe id="dataForm" name="dataForm"
				height="100%" frameborder="0" scrolling="no" width="100%" src=""></iframe></td>
	</tr>
	<tr>
		<td height="20"></td>
	</tr>
</TABLE>
<%@ include file="/foot.jsp"%>
</html>
