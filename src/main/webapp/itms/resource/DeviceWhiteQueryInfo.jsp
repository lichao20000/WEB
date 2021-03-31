<%@ include file="/timelater.jsp"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%><%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="/toolbar.jsp"%>


<html>
<head>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<lk:res />

<SCRIPT LANGUAGE="JavaScript">
var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";

$(function(){
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	gwShare_setGaoji();
	gwShare_setImport();
	
	var vendor='<s:property value="vendor"/>';
	var device_model='<s:property value="device_model"/>';
	var devicetypeId='<s:property value="devicetypeId"/>';
	// ��ʼ������
	change_select("city","-1");
	change_select("vendor",vendor);
	setTimeout("change_select('deviceModel',"+device_model+")",1000);
	setTimeout("change_select('devicetype',"+devicetypeId+")",2500);
	Init(vendor,device_model,devicetypeId);
});


function Init(vendor,device_model,devicetypeId){
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/deviceWhiteList!queryWhiteList.action'/>?vendor="+vendor+"&device_model="+device_model+"&devicetypeId="+devicetypeId
	form.submit();
}

function doQuery(){
	// ��ͨ��ʽ�ύ
	var form = document.getElementById("mainForm");
	form.action = "<s:url value='/itms/resource/deviceWhiteList!queryWhiteList.action'/>";
	form.submit();
}

var deviceIds = "";
function deviceResult(returnVal){
	deviceIds="";
	if(returnVal[0]==0){
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[2].length+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
		if(returnVal[0]>400000){
			alert("�������������Գ���40��");
			return;
		}
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+returnVal[0]+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
}

function add(){
	var deviceIds = $("input[@name='deviceIds']").val();
	var param = $("input[@name='param']").val();
	var gw_type = $("input[@name='gw_type']").val();
	var task_name = $("input[@name='task_name']").val();
	
	if(deviceIds == ""){
		alert("��ѡ���豸");
		return false;
	}
	
	if(task_name==""){
		alert("�������������ƣ�");
		return false;
	}
		
	$("button[@id='btn']").attr("disabled",true);
	$("tr[@id='trData']").show();
	$("div[@id='QueryData']").html("����ִ���������Ʋ��������Ե�....");
	url = "<s:url value='/itms/resource/deviceWhiteList!addWhiteList.action'/>";
	$.post(url,{
		task_name : task_name,
		gw_type : gw_type,
		deviceIds : deviceIds,
		param : param
	},function(ajax){
		$("div[@id='QueryData']").html(ajax);
		$("button[@id='btn']").attr("disabled",false);
		alert(ajax);
		$("table[@id='addTable']").hide();
		 location.reload();
	});
}

</SCRIPT>
</head>

<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="" target="dataForm">
		<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162"> <div align="center" class="title_bigwhite">�豸����������</div> </td>
				<td><img src="/itms/images/attention_2.gif" width="15" height="12"></td>
			</tr>
		</table>
		<!-- ��ѯpart begin -->
		<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
			<tr>
				<td bgcolor=#999999>
				<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
					<tr>
						<th colspan="4" id="gwShare_thTitle">�豸����������</th>
					</tr>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">����</TD>
						<TD align="left" width="35%" colspan="3">
							<select name="cityId" class="bk">
								<option value="-1">==��ѡ��==</option>
							</select>
						</TD>
					</TR>
					
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">�豸����</TD>
						<TD align="left" width="35%">
							<select name="vendor" class="bk" onchange="change_select('deviceModel','-1')">
							</select>
						</TD>
						<TD align="right" class=column width="15%">�豸�ͺ�</TD>
						<TD width="35%">
							<select name="device_model" class="bk" onchange="change_select('devicetype','-1');change_select('devicetypeHD','-1');">
							</select>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
						<TD align="right" class=column width="15%">�豸�汾</TD>
						<TD align="left" width="35%" >
							<select name="devicetypeId" class="bk"">
							</select>
						</TD>
						<TD align="right" class=column width="15%">�豸���к�</TD>
						<TD align="left" width="35%">
							<input name="deviceSerialnumber" value="">
						</TD>
					</TR>
					<tr bgcolor="#FFFFFF">
						<td colspan="4" align="right" class="green_foot" width="100%">
							<input type="button" class=jianbian onclick="javascript:doQuery()" name="queryButton" value=" �� ѯ " /> 
							<input type="button" class=jianbian onclick="javascript:resetFrm();" name="reButto" value=" �� �� "  />
							<input type="button" class=jianbian onclick="javascript:doAdd()" name="addButto" value=" �� �� " />
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</FORM>
		<!-- ��ѯpart end -->
		
		<!-- չʾ���part begin -->
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
			<TR>
				<TD bgcolor=#999999 id="idData">
					<iframe id="dataForm" name="dataForm" height="0" frameborder="0" scrolling="no" width="100%" src=""></iframe>
				</TD>
			</TR>
		</TABLE>
		<!-- չʾ���part end -->
		<br/><br/>
		<!-- ��Ӻͱ༭part begin --> 
		<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center" id="addTable" style="display: none">
			<tr>
				<th colspan="4" id="">���������豸������</th>
			</tr>
			<TR bgcolor="#FFFFFF">
				<td colspan="4">
					<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
				</td>
			</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post" ACTION="" target="childFrm" >
							<input type="hidden" name="deviceIds" value="" />
							<input type="hidden" name="param" value="" />
							<input type="hidden" name="gw_type" value="1" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<tr bgcolor="#FFFFFF">
												<td colspan="4">
													<div id="selectedDev">
														���ѯ�豸��
													</div>
												</td>
											</tr>
											
											<tr bgcolor="#FFFFFF">
												<TD colspan="1" class="column" >
													�������ƣ�
												</TD>
												<TD colspan="3" class="column" >
													<input type="text" name="task_name" class="bk" value=""/>
												</TD>
											</tr>
						
											<tr bgcolor=#ffffff>
												<td class=foot colspan=4 align=right>
													<button id="btn" onclick="add();">&nbsp;��&nbsp;��&nbsp;</button>
												</td>
											</tr>
																	
											<tr id="trData" style="display: none" bgcolor=#ffffff>
												<td colspan=4>
													<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
														����Ŭ��Ϊ����ѯ�����Ե�....</div>
												</td>
											</tr>						
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</FORM>
					</td>
				</tr>
		</TABLE>
		<!-- ��Ӻͱ༭part end --> 
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20><IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>&nbsp;</TD>
	</TR>

</TABLE>
</body>

<%@ include file="/foot.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
	var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";

	


	//���
	function doAdd() {
		showAddPart(true);
	}
	
	// ����ҳ��������������
	function showAddPart(tag){
		if(tag)
			$("table[@id='addTable']").show();
		else
			$("table[@id='addTable']").hide();
	}
		

	function change_select(type,selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getCityNextChild.action'/>";
				$.post(url,{
				},function(ajax){
					parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
				});
				break;
			case "vendor":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getVendor.action'/>";
				$.post(url,{
				},function(ajax){
					parseMessage(ajax,$("select[@name='vendor']"),selectvalue);
					parseMessage(ajax,$("select[@name='vendor_add']"),selectvalue);
				});
				break;
			case "deviceModel":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getDeviceModel.action'/>";
				var vendorId = $("select[@name='vendor']").val();
				if("-1" == vendorId){
					$("select[@name='device_model']").html("<option value='-1'>==����ѡ���豸����==</option>");
					break;
				}
				$.post(url,{
					gwShare_vendorId:vendorId
				},function(ajax){
					parseMessage(ajax,$("select[@name='device_model']"),selectvalue);
				});
				break;
			case "devicetype":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetype.action'/>";
				var deviceModelId = $("select[@name='device_model']").val();
				if("-1" == deviceModelId){
					$("select[@name='devicetypeId']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
					break;
				}
				$.post(url,{
					gwShare_deviceModelId:deviceModelId
				},function(ajax){
					parseMessage(ajax,$("select[@name='devicetypeId']"),selectvalue);
				});
				break;	
			case "devicetypeHD":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetypeHD.action'/>";
				var deviceModelId = $("select[@name='device_model']").val();
				if("-1" == deviceModelId){
					$("select[@name='hardwareversion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
					break;
				}
				$.post(url,{
					gwShare_deviceModelId:deviceModelId
				},function(ajax){
					parseMessage(ajax,$("select[@name='hardwareversion']"),selectvalue);
				});
				break;	
			default:
				alert("δ֪��ѯѡ�");
				break;
		}	
	}
	

	//������ѯ�豸�ͺŷ���ֵ�ķ���
	function parseMessage(ajax,field,selectvalue){
		var flag = true;
		if(""==ajax){
			option = "<option value='-1' selected>==����û�м�¼==</option>";
			field.html(option);
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

	function resetFrm(){
		document.mainForm.vendor.value = "-1";
		$("select[@name='device_model']").html("<option value=-1>--����ѡ����--</option>");
		$("select[@name='devicetypeId']").html("<option value=-1>--����ѡ���ͺ�--</option>");
	}


	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids=["dataForm"]
	
	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide="yes"
	
	function dyniframesize() 
	{
		var dyniframe=new Array()
		for (i=0; i<iframeids.length; i++)
		{
			if (document.getElementById)
			{
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera)
				{
	     			dyniframe[i].style.display="block"
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
				var tempobj=document.all? document.all[iframeids[i]] : document.getElementById(iframeids[i])
	    		tempobj.style.display="block"
			}
		}
	}
	
	
	$(function(){
		dyniframesize();
	});
	
	$(window).resize(function(){
		dyniframesize();
	}); 
</SCRIPT>

</html>