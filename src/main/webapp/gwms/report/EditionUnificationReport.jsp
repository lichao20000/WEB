<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<html>
<head>
<title>�������ذ汾ͳһ�ʱ���</title>
<%-- <SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jsDate/WdatePicker.js"/>"></SCRIPT> --%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/progress.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">

<script type="text/javascript">
	
	function doQuery(){
		$("#button1").attr("disabled", true);
		var gw_type = "<s:property value='gwType' />";
		
		var startTime=$("input[@name='startTime']").val();
		var endTime=$("input[@name='endTime']").val();
		var vendorId=$("select[@name='vendorId']").val();
		var deviceModelId=$("select[@name='deviceModelId']").val();
		var recent_start_Time=$("input[@name='recent_start_Time']").val();
		var recent_end_Time=$("input[@name='recent_end_Time']").val();
		
		var obj = document.getElementsByName("isExcludeUpgrade");
		var isExcludeUpgrade = '';
		for (var i = 0; i < obj.length; i++) {
			if (obj[i].checked) {
				isExcludeUpgrade += "" + obj[i].value + "";
			}
		}
		$("div[@id='resultData']").html("���ڻ�ȡͳ������...");
		startProgress();
		
		var url="";
		<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
				|| "jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
				|| "nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
			url = "<s:url value='/gwms/report/tyGateAgreeCountReport!queryDataListJXDX.action'/>";
		<%}else if("sd_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
			url = "<s:url value='/gwms/report/tyGateAgreeCountReport!queryDataListSDDX.action'/>";
		<%}%>
		$.post(url,{
			gwType:gw_type,
			vendorId:vendorId,
			deviceModelId:deviceModelId,
			<%if( "jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
					|| "jl_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))
					|| "nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
			startTime:startTime,
			endTime:endTime,
			<%}%>
			recent_start_Time:recent_start_Time,
			recent_end_Time:recent_end_Time,
			isExcludeUpgrade:isExcludeUpgrade
		},function(ajax){
		    $("div[@id='resultData']").html("");
			$("div[@id='resultData']").append(ajax);
			stopProgress();
		});	
		
		$("#button1").attr("disabled", false);
	}
	
	//������ֶ���ҳ��
	function getdetailListJXDX(gwType,vendorId,deviceModelId,isExcludeUpgrade,
			startTime,endTime,recent_start_Time,recent_end_Time,hardwareversion,is_highversion){
		var page = "<s:url value='/gwms/report/tyGateAgreeCountReport!querydetailListJXDX.action'/>"
			page+="?gwType=" + gwType+"&vendorId="+vendorId+"&deviceModelId="+deviceModelId+"&isExcludeUpgrade="+isExcludeUpgrade+"&startTime="+startTime;
			page+="&endTime="+endTime+"&recent_start_Time="+recent_start_Time+"&recent_end_Time="+recent_end_Time+"&hardwareversion="+hardwareversion+"&is_highversion="+is_highversion;
		window.open(page,"","left=20,top=20,width=800,height=600,resizable=no,scrollbars=yes");
	}
	
	// �����б�����
	function queryDataForExcelJXDX(gwType,vendorId,deviceModelId,isExcludeUpgrade,
			startTime,endTime,recent_start_Time,recent_end_Time){
		var url = "<s:url value='/gwms/report/tyGateAgreeCountReport!queryDataListJXDX.action'/>";
		document.reportForm.action = url+"?reportType=excel&gwType="+gwType+"&vendorId="+vendorId+"&deviceModelId="+deviceModelId+"&isExcludeUpgrade="+isExcludeUpgrade+"&startTime="+startTime+"&endTime="+endTime+"&recent_start_Time="+recent_start_Time+"&recent_end_Time="+recent_end_Time;
		document.reportForm.method = "post";
		document.reportForm.submit();
	}
	// �����б�����
	function queryDataForExcelSDDX(gwType,vendorId,deviceModelId,isExcludeUpgrade,
			startTime,endTime,recent_start_Time,recent_end_Time){
		var url = "<s:url value='/gwms/report/tyGateAgreeCountReport!queryDataListSDDX.action'/>";
		document.reportForm.action = url+"?reportType=excel&gwType="+gwType+"&vendorId="+vendorId+"&deviceModelId="+deviceModelId+"&isExcludeUpgrade="+isExcludeUpgrade+"&startTime="+startTime+"&endTime="+endTime+"&recent_start_Time="+recent_start_Time+"&recent_end_Time="+recent_end_Time;
		document.reportForm.method = "post";
		document.reportForm.submit();
	}
	
	$(function() {
		gwShare_change_select("vendor","-1");
	});

	function gwShare_change_select(type,selectvalue){
	switch (type){
		case "city":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
			});
			break;
		case "vendor":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getVendor.action"/>";
			$.post(url,{
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='vendorId']"),selectvalue);
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ���豸����==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "deviceModel":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDeviceModel.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			if("-1"==vendorId){
				$("select[@name='deviceModelId']").html("<option value='-1'>==����ѡ����==</option>");
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceModelId']"),selectvalue);
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
			});
			break;
		case "devicetype":
			var url = "<s:url value="/gwms/share/gwDeviceQuery!getDevicetype.action"/>";
			var vendorId = $("select[@name='vendorId']").val();
			var deviceModelId = $("select[@name='deviceModelId']").val();
			if("-1"==deviceModelId){
				$("select[@name='deviceTypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
				break;
			}
			$.post(url,{
				gwShare_vendorId:vendorId,
				gwShare_deviceModelId:deviceModelId
			},function(ajax){
				gwShare_parseMessage(ajax,$("select[@name='deviceTypeId']"),selectvalue);
			});
			break;	
		default:
			alert("δ֪��ѯѡ�");
			break;
	}	
}
	
	function gwShare_parseMessage(ajax,field,selectvalue){
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
			if(selectvalue==xValue){
				flag = false;
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
			}else{
				//����ÿ��value��text��ǵ�ֵ����һ��option����
				option = "<option value='"+xValue+"'>=="+xText+"==</option>";
			}
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
	
</script>
</head>

<body>
<form id="form" name="selectForm" action="" method="post">
<br>
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
			<tr>
				<td>
					<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
						<tr>
							<td width="182" align="center" class="title_bigwhite">
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								������������ͳһ�ʱ���
							<%}else{%>
								��������ͳһ�ʱ���
							<%}%>
							</td>
							<td><img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12"/></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="1" cellpadding="2" bgcolor="#999999">

						<tr>
							<th colspan="6">
							<%if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){%>
								������������ͳһ�ʱ���
							<%}else{%>
								��������ͳһ�ʱ���
							<%}%>
							</th>
						</tr>

						<tr>
							<td class=column width="10%" align='right'>����</td>
							<td width="30%" class=column>
								<select name="vendorId" class="bk" onchange="gwShare_change_select('deviceModel','-1')">
									<option value="-1">==��ѡ��==</option>
								</select>
							</td>
							<td class=column width="10%" align='right'>�豸�ͺ�</td>
							<td width="30%" class=column>
								<select name="deviceModelId">
									<option value="-1">����ѡ����</option>
								</select>
							</td>
							<td class=column width="15%" align='left' colspan="1">
								<input  type="checkbox" name="isExcludeUpgrade" value="1" checked="checked"/><strong>�ų�3�������й�����</strong>
							</td>
						</tr>
						<tr>
							<td class=column width="10%" align='right'>����ϱ�ʱ��</td>
							<td width="30%" class=column <ms:inArea areaCode="jx_dx,jl_dx,nx_dx" notInMode="true">colspan="5"</ms:inArea>>
							<input type="text" name="recent_start_Time" value='<s:property value="recent_start_Time" />' readonly >
							<img name="shortDateimg" onClick="WdatePicker({el:document.form.recent_start_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��">&nbsp;~&nbsp;
							<input type="text" name="recent_end_Time" value='<s:property value="recent_end_Time" />' readonly>
							<img name="shortDateimg" onClick="WdatePicker({el:document.form.recent_end_Time,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��"> 
							</td>
							<ms:inArea areaCode="jx_dx,jl_dx,nx_dx" notInMode="false">
								<td class=column width="10%" align='right'>BSS����ʱ��</td>
								<td width="30%" class=column colspan="3">
								<input type="text" name="startTime" readonly  value="<s:property value="startTime" />">
								<img name="shortDateimg" onClick="WdatePicker({el:document.form.startTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��">&nbsp;~&nbsp; 
								<input type="text" name="endTime" readonly  value="<s:property value="endTime" />">
								<img name="shortDateimg" onClick="WdatePicker({el:document.form.endTime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��">
								</td>
							</ms:inArea>
						</tr>
						
						
						<tr>
							<td colspan="6" align="right" class=foot>
								<button id="button1" onclick="doQuery()">&nbsp;�� ѯ&nbsp;</button>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td height = "20">
				</td>
			</tr>
			<tr>
				<td align="center">
					<div id="resultData" ></div>
					<div id="progress"></div>
				</td>
			</tr>

		</table>
		<br>
	</form>
<form name="reportForm"></form>
</body>
</html>
<%@ include file="../../foot.jsp"%>