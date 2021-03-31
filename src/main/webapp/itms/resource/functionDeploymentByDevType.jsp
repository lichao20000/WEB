<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�������ܲ��𱨱��豸�ͺ����</title>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">
	function query() {
		
		var __vendorId = $("select[@name='vendorId']").val();

		if(__vendorId == null || __vendorId == "-1"){
			alert("��ѡ����");
			$("select[@name='vendorId']").focus();
			return false;
		}
		
		$("#qy").attr('disabled',true);
		showMsgDlg();
		var url = "<s:url value='/itms/resource/FunctionDeploymentByDevType!quertFunctionDeployByDevType.action'/>";
		var endtime = $("input[@name='endOpenDate']").val();
		var modelId = $("select[@name='modelId']").val();
		var gn = $("select[@name='gn']").val();
		$.post(url,{endOpenDate:endtime,
			    	vendorId:__vendorId,
			    	modelId:modelId,
			    	gn:gn},function(ajax){
				$("div[@id='QueryData']").html("");
				$("div[@id='QueryData']").append(ajax);
				closeMsgDlg();
			});
	}

	function change_select(){
			$("select[@name='modelId']").html("");
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='modelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='modelId']").attr('disabled',true);
				return;
			}	
			$("select[@name='modelId']").attr('disabled',false);
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				parseMessage(ajax,$("select[@name='modelId']"));
			});
		
	}
	
	//������ѯ�豸�ͺŷ���ֵ�ķ���
	function parseMessage(ajax,field){
		var flag = true;
		if(""==ajax){
			return;
		}
		var lineData = ajax.split("#");
		if(!typeof(lineData) || !typeof(lineData.length)){
			return false;
		}
		field.html("");
		option = "<option value='-1' selected>==��ѡ��==</option>";
		field.append(option);
		for(var i=0;i<lineData.length;i++){
			var oneElement = lineData[i].split("$");
			var xValue = oneElement[0];
			var xText = oneElement[1];
			option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
			try{
				field.append(option);
			}catch(e){
				alert("�豸�ͺż���ʧ�ܣ�");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}
	
	
	
	//��ʼ����ʱ�����
	function showMsgDlg(){
			w = document.body.clientWidth;
			h = document.body.clientHeight;
			
			l = (w-250)/2;
			t = h/2-120;
			PendingMessage.style.left = l;
			PendingMessage.style.top  = t;
			PendingMessage.style.display="";
	}

	//������ݣ�����ҳ��
	function closeMsgDlg(){
			$("#qy").attr('disabled',false);
			PendingMessage.style.display="none";
	}
	
	function  openModel(modelId){
	    var endOpenDate = $.trim($("input[@name='endOpenDate']").val());
	    var vendorId = $.trim($("select[@name='vendorId']").val());
	    var gn = $.trim($("select[@name='gn']").val());
		var page="<s:url value='/itms/resource/FunctionDeploymentByDevType!quertFunctionDeployByDevTypeList.action'/>?"+ "vendorId=" + vendorId +"&gn="+gn+"&modelId="+modelId+"&endOpenDate=" +endOpenDate;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
	
	function ToExcel(){
		var mainForm = document.getElementById("selectForm");
		mainForm.action = "<s:url value='/itms/resource/FunctionDeploymentByDevType!quertFunctionDeployByDevTypeExcel.action' />";
		mainForm.submit();
		mainForm.action = "<s:url value='/itms/resource/FunctionDeploymentByDevType!quertFunctionDeployByDevType.action' />";
	}
	
</script>
</head>

<body>
	<form id="form" name="selectForm"
		action="<s:url value='/itms/resource/FunctionDeploymentByDevType!quertFunctionDeployByDevType.action'/>">
		<table>
			<tr>
				<td HEIGHT=20>&nbsp;</td>
			</tr>
			<tr>
				<td>
					<table class="green_gargtd">
						<tr>
							<td width="162" align="center" class="title_bigwhite">
							�������ܲ��𱨱���Ϣ</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>"
								width="15" height="12" />&nbsp;&nbsp;�豸�ͺ�</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table class="querytable">

						<TR>
							<th colspan="4">�������ܲ��𱨱���Ϣ</th>
						</tr>
						<TR>
							<TD class=column width="15%" align='right'>ʱ��</TD>
							<TD  width="35%">
									<input type="text" name="endOpenDate" readonly class=bk
										value="<s:property value="endOpenDate" />">
									<img name="shortDateimg"
										onClick="WdatePicker({el:document.selectForm.endOpenDate,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
										src="../../images/dateButton.png" width="15" height="12"
										border="0" alt="ѡ��">
							</TD>
							<TD  class=column width="15%" align='right'>����</TD>
							<TD width="35%">
								<select name="gn" id="gn" class="bk">
									<option value="-1" >==��ѡ��==</option>
									<option value="8001" selected="selected" >==Ԥ��Ԥ��==</option>
									<option value="911" >==��������==</option>
									<option value="17" >==��Ϊ����==</option>
								</select>
							</TD>
						</TR>

						<TR>
							<TD class=column width="15%" align='right'>����</TD>
							<TD width="35%"><s:select list="vendorMap" name="vendorId" onchange="change_select()" headerKey="-1" headerValue="==��ѡ����=="
									 listKey="key" listValue="value" cssClass="bk"></s:select>&nbsp;&nbsp;<font color="red">*</font> </TD>
									 
							<TD class="column" width='15%' align="right">�豸�ͺ�</td>
							<td width='35%' align="left"><select name="modelId" class="bk" >
								<option value="-1" selected="selected">==��ѡ���豸�ͺ�==</option>
								</select></TD>
						</TR>
						<TR>
							<td colspan="4" align="right" class=foot>
								<button id="qy" onclick="javaScript:query()" >&nbsp;��ѯ&nbsp;</button>
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
				<div id="PendingMessage"
						style="position:absolute;z-index:3;top:240px;left:250px;width:300;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;filter:alpha(opacity=80);display:none">
							<center>
									<table border="0" style="background-color:#eeeeee">
										<tr>
											<td valign="middle"><img src="<s:url value='/images/cursor_hourglas.gif'/>"  
												border="0" WIDTH="30" HEIGHT="30"></td>
											<td>&nbsp;&nbsp;</td>
											<td valign="middle"><span id=txtLoading
												style="font-size:14px;font-family: ����">���Ե�,��ѯ�С�����������</span></td>
										</tr>
									</table>
							</center>
				</div>
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				</div>
			</tr>
			
			<tr>
				<td height="25"></td>
			</tr>
			<tr>
				<td id="bssSheetInfo"></td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
<%@ include file="../../foot.jsp"%>