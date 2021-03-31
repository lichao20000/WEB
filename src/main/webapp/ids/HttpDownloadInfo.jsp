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
<SCRIPT LANGUAGE="JavaScript">
	function doQuery(){
		var taskname = $("input[@name='taskname']").val();
		var filename = $("input[@name='gwShare_fileName']").val();
		var endtime = $("input[@name='endtime']").val();
		var starttime = $("input[@name='starttime']").val();
		var enddate = $("input[@name='enddate']").val();
		var urls = $("input[@name='url']").val();
		
		//var httptype = $("input[@name='httptype']:checked").val();
		var httptype = $("input[@name='httptype']").val();
		var testUserName = $("input[@name='testUserName']").val();
		var testPWD = $("input[@name='testPWD']").val();
		if(starttime>=endtime){
			if(starttime>endtime){
				alert("��ʼʱ�䲻�ܴ��ڽ���ʱ��");
			}else{
				alert("��ʼʱ�䲻�ܵ��ڽ���ʱ��");
			}
			return;
		}
		if(taskname == ""){
			alert("����������Ϊ��");
			return;
		}
		if(filename == ""){
			alert("�����ϴ��ļ�");
			return;
		}
		if(testUserName == ""){
			alert("�����˺Ų���Ϊ��");
			return;
		}
		if(testPWD == ""){
			alert("�����˺����벻��Ϊ��");
			return;
		}
		if(urls == ""){
			alert("url��ַ����Ϊ��");
			return;
		}
		if(testUserName.length>30){
			alert("�����˺Ų��ܳ���30���ַ�");
			return;
		}
		if(testPWD.length>30){
			alert("�����˺����벻�ܳ���30���ַ�");
			return;
		}
		var url = "<s:url value='/ids/httpDownload!getExcelRows.action'/>";
		$.post(url,{
			filename : filename
		},function(ajax){
			if(ajax == "false"){
				alert("�������10000��");
				return;
			}
			$("button[@id='btn']").attr("disabled",true);
			$("tr[@id='trData']").show();
			$("div[@id='QueryData']").html("����ִ���������Ʋ��������Ե�....");
			url = "<s:url value='/ids/httpDownload!addTaskInfo.action'/>";
			$.post(url,{
				taskname : taskname,
				filename : filename,
				endtime : endtime,
				starttime : starttime,
				enddate : enddate,
				url : urls,
				httpType : httptype,
				testUserName : testUserName,
				testPWD : testPWD
			},function(ajax){
				if(ajax == "false"){
					$("div[@id='QueryData']").html("�������Ʋ���ִ��ʧ��");
				}else if(ajax=="������������ִ�гɹ���"){
					$("div[@id='QueryData']").html("�������Ʋ���ִ�гɹ�");
					
				}
				else
				{
					$("div[@id='QueryData']").html("����ʧ�ܣ�ҳ�泤ʱ��δ������ˢ��ҳ�棡");
				}
				$("button[@id='btn']").attr("disabled",false);
			});
			
			
			
		});
		
		
		
	}
</SCRIPT>
	
	
	
</head>

<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>����HTTP��������</th>
					<td><img src="<s:url value="/images/attention_2.gif"/>"
						width="15" height="12">�������10000̨</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name="frm" id="selectForm" method="post"
				action="<s:url value='/ids/httpDownload!addTaskInfo.action'/>">
				<table class="querytable">
					<tr>
						<th colspan=4>����������</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">�������ƣ�</td>
						<td colspan="3"><input type="text" name="taskname"  > </td>
					</tr>
					<tr class=column bgcolor="#FFFFFF" id="upload">
					<td  class=column align=center width="10%" height="12">�����ļ���</td>
					<td colspan="3">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="FileUpload.jsp" height="22" width=500>
							</iframe><br/><span style="color:green;">Ŀǰֻ֧��xls</span>
							<input type="hidden" name="gwShare_fileName" value=""/>
						</div>
					</td>
					</tr>
					
					
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">��ʼʱ�䣺</td>
						<td width="20%"><input type="text" name="starttime" class='bk' readonly
							value="<s:property value="starttime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'HH:mm',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="ѡ��" /></td>
						<td class=column align=center width="10%">����ʱ�䣺</td>
						<td width="20%"><input type="text" name="endtime" class='bk' readonly
							value="<s:property value="endtime"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.endtime,dateFmt:'HH:mm',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="ѡ��" /></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">�ɼ���ֹʱ�䣺</td>
						<td colspan="3"><input type="text" name="enddate" class='bk' readonly
							value="<s:property value="enddate"/>"> <img
							name="shortDateimg"
							onClick="WdatePicker({el:document.frm.enddate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="<s:url value="/images/dateButton.png"/>" width="15"
							height="12" border="0" alt="ѡ��" />
							<input type="hidden" name="httptype" value="1" />
							</td>
							
							
							
								<!-- 
					<td class=column align=center width="15%">ѡ���ӽӿ�</td>
					<td colspan="3">
					<input type="radio" width="45%" name="httptype" value="1" checked="checked">�������
					<input type="radio" width="45%" name="httptype" value="2">TR069
					</td>						
					 -->
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">�����˺ţ�</td>
						<td colspan="3"><input type="text" name="testUserName" ><span style="color:green">�����ܳ���30���ַ���</span></td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="10%">�����˺����룺</td>
						<td colspan="3"><input type="password" name="testPWD" ><span style="color:green">�����ܳ���30���ַ���</span></td>
					</tr>
						
					 <tr bgcolor=#ffffff>
							<td class=column align=center width="10%">����url��</td>
							<td colspan="3"><input type="text" name="url"  ><span style="color:green">���磺http://www.baidu.com</span> </td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button id="btn" onclick="doQuery();">&nbsp;��&nbsp;��&nbsp;</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>
	<tr id="trData" style="display: none">
		<td>
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ִ���������Ʋ��������Ե�....</div>
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