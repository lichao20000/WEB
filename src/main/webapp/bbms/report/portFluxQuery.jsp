<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


function doQuery(){  
	var queryType = $("input[@name='queryType'][@checked]").val();
	var deviceSN = $.trim($("input[@name='deviceSN']").val());
	var loopbackIp = $.trim($("input[@name='loopbackIp']").val());
	var userName = $.trim($("input[@name='userName']").val());
	var customerName = $.trim($("input[@name='customerName']").val());
	var linkphone = $.trim($("input[@name='linkphone']").val());
	var stat_time = $.trim($("input[@name='stat_time']").val());
	var reportType = $.trim($("select[@name='reportType']").val());
	if(queryType=="device"){
		if(deviceSN==""&&loopbackIp==""){
			alert("�����������豸���кź��豸IP�е�һ�");
			$("input[@name='deviceSN']").focus();
			return false;
		}
		if(deviceSN!=""){
			if(deviceSN.length < 6){
           		alert("�������������6λ�豸���к� !");
           		$("input[@name='deviceSN']").focus();
            	return false;        
	    	}
		}
		if(loopbackIp!=""){
			if(!IsIPAddr2(loopbackIp,"�豸IP")){
				$("input[@name='loopbackIp']").focus();
				return false;
			}
		}
	}
	if(queryType=="user"){
		if(userName==""){
			alert("�������û��˺ţ�");
			$("input[@name='userName']").focus();
			return false;
		}
	}
	if(queryType=="customer"){
		if(customerName==""&&linkphone==""){
			alert("����������ͻ����ƺ���ϵ�绰�е�һ�");
			$("input[@name='customerName']").focus();
			return false;
		}
	}
    if(stat_time==""){
    	alert("�������ѯʱ��");
		return false;
    }
    $("tr[@id='trData']").show();
    $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
    var url = '<s:url value='/bbms/report/portFluxQuery.action'/>'; 
	$.post(url,{
		queryType:queryType,
		deviceSN:deviceSN,
		loopbackIp:loopbackIp,
		userName:encodeURIComponent(userName),
		customerName:encodeURIComponent(customerName),
		linkphone:linkphone,
		stat_time:stat_time,
		reportType:reportType
	},function(ajax){	
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function ToExcel(queryType,deviceSN,loopbackIp,userName,customerName,linkphone,reportType,stat_time) {
	var page="<s:url value='/bbms/report/portFluxQuery!getExcel.action'/>?"
		+ "&queryType=" + queryType
		+ "&deviceSN=" + deviceSN
		+ "&loopbackIp=" + loopbackIp
		+ "&userName=" + userName
		+ "&customerName=" + customerName
		+ "&linkphone=" + linkphone
		+ "&reportType=" + reportType
		+ "&stat_time=" + stat_time;
	document.all("childFrm").src=page;
}

function ToAllExcel() {
	var page="<s:url value='/bbms/report/portFluxQuery!getAllExcel.action'/>";
	document.all("childFrm1").src=page;
}

function queryTypeChange(){
    var queryType = $("input[@name='queryType'][@checked]").val();
	if(queryType=="device"){
		$("tr[@name='device']").show();
		$("tr[@name='user']").hide();	
		$("tr[@name='customer']").hide();	
	}
	if(queryType=="user"){
		$("tr[@name='user']").show();
		$("tr[@name='device']").hide();
		$("tr[@name='customer']").hide();
	}
	if(queryType=="customer"){
		$("tr[@name='customer']").show();
		$("tr[@name='device']").hide();
		$("tr[@name='user']").hide();
	}
}
</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						�˿�������
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
						�Զ˿������ʽ��в�ѯ
					</td>
					<td align="right">
						<a href="javascript:ToAllExcel();">���������豸�˿�������</a>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							�˿������ʲ�ѯ
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							��ѯ��ʽ
						</td>
						<td colspan="3" width="85%">
							<input type="radio" name="queryType" checked value="device"
								onclick="queryTypeChange();">
							���豸��ѯ
							<input type="radio" name="queryType" value="user"
								onclick="queryTypeChange();">
							���û���ѯ
							<input type="radio" name="queryType" value="customer"
								onclick="queryTypeChange();">
							���ͻ���ѯ
						</td>
					</tr>
					<tr bgcolor=#ffffff name="device" style="display: ">
						<td class=column width="15%">
							�豸���к�
						</td>
						<td width="35%">
							<input type="text" name="deviceSN" class='bk' value="">
							<font color="red">(�������6λ)</font>
						</td>
						<td class=column width="15%">
							�豸IP
						</td>
						<td width="35%">
							<input type="text" name="loopbackIp" class='bk' value="">
						</td>
					</tr>
					<tr bgcolor=#ffffff name="user" style="display: none">
						<td class=column width="15%">
							�û��˺�
						</td>
						<td colspan="3" width="85%">
							<input type="text" name="userName" class='bk' value="">
							<font color="red">*</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff name="customer" style="display: none">
						<td class=column width="15%">
							�ͻ�����
						</td>
						<td width="35%">
							<input type="text" name="customerName" class='bk' value="">
							<font color="red">(ģ��ƥ��)</font>
						</td>
						<td class=column width="15%">
							��ϵ�绰
						</td>
						<td width="35%">
							<input type="text" name="linkphone" class='bk' value="">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column width="15%">
							��������
						</td>
						<td width="35%">
							<select name="reportType" class="bk">
								<option value="day">
									�ձ���
								</option>
								<option value="week">
									�ܱ���
								</option>
								<option value="month">
									�±���
								</option>
							</select>
						</td>
						<td class=column width="15%">
							��ѯʱ��
						</td>
						<td width="35%">
							<input type="text" name="stat_time" class='bk' readonly
								onclick="new WdatePicker(document.frm.stat_time,'%Y-%M-%D',true,'whyGreen')"
								value="<s:property value='stat_time'/>">
							<img
								onclick="new WdatePicker(document.frm.stat_time,'%Y-%M-%D',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;�� ѯ&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
	<tr STYLE="display: none">
		<td>
			<iframe id="childFrm1" src=""></iframe>
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
