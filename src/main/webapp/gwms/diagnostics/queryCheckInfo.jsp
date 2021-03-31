<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>

<%@ include file="/toolbar.jsp"%>


<html>
<head>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>
<lk:res />

<SCRIPT LANGUAGE="JavaScript">
$(function(){
	Init();
});


function Init(){
	// ��ʼ������
	change_select("vendor","-1");
}

function doQuery(){
	var url = "<s:url value='/gwms/diagnostics/deviceDiagnostics!getCheckList.action'/>";
	document.mainForm.submit();
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
			<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION="<s:url value='/gwms/diagnostics/deviceDiagnostics!getCheckList.action'/>" target="dataForm">
			<table width="98%" height="30" border="0" align="center" cellpadding="0" cellspacing="0" class="green_gargtd">
				<tr>
					<td width="162"> <div align="center" class="title_bigwhite">�Զ�������ѯ</div> </td>
					<td><img src="/itms/images/attention_2.gif" width="15" height="12"></td>
				</tr>
			</table>
			<!-- ��ѯpart begin -->
			<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
				<tr>
					<td bgcolor=#999999>
					<table border=0 cellspacing=1 cellpadding=2 width="100%" align="center">
						<tr>
							<th colspan="4" id="gwShare_thTitle">��ѯ</th>
						</tr>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">�����˺�</TD>
							<TD align="left" width="35%">
								<input type="text" id="account" name="account" class="bk" value="">
							</TD>
							<TD align="right" class=column width="15%">�豸���к�</TD>
							<TD width="35%">
								<input type="text" id="device_serialnumber" name="device_serialnumber" class="bk" value="">
							</TD>
						</TR>
						
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">�豸����</TD>
							<TD width="35%">
								<select name="vendor" class="bk" onchange="change_select('deviceModel','-1')">
									<option value="-1">==��ѡ��==</option>
								</select>
							</TD>
							<TD align="right" class=column width="15%">�豸�ͺ�</TD>
							<TD width="35%">
								<select name="device_model" class="bk" onchange="change_select('softwareversion','-1');change_select('devicetypeHD','-1');">
									<option value="-1">==����ѡ���豸����==</option>
								</select>
							</TD>
						</TR>
						<TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<TD align="right" class=column width="15%">Ӳ���汾</TD>
							<TD align="left" width="35%">
								<select name="hardwareversion" class="bk"">
									<option value="-1">==����ѡ���豸�ͺ�==</option>
								</select>
							</TD>
							<TD align="right" class=column width="15%">����汾</TD>
							<TD align="left" width="35%">
								<select name="softwareversion" class="bk"">
									<option value="-1">==����ѡ���豸�ͺ�==</option>
								</select>
							</TD>
						</TR>
						
						<tr bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
							<td align="right" class=column width="15%">��ʼʱ��</td>
							<td align="left" width="35%">
								<input type="text" name="startTime" id="startTime" readonly
														value="" class=bk>
								<img name="shortDateimg" onClick="WdatePicker({el:'startTime',dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��">
							</td>
							<td align="right" class=column width="15%">����ʱ��</td>
							<td align="left" width="35%">
								<input type="text" name="endTime" id="endTime" readonly
														value="" class=bk>
								<img name="shortDateimg" onClick="WdatePicker({el:'endTime',dateFmt:'yyyy-MM-dd',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12" border="0" alt="ѡ��">
							</td>
						</tr>
						
						<tr bgcolor="#FFFFFF">
							<td colspan="4" align="right" class="green_foot" width="100%">
								<input type="button" class=jianbian onclick="javascript:doQuery()" name="queryButton" value=" �� ѯ " /> 
								<input type="button" class=jianbian onclick="javascript:resetFrm();" name="reButto" value=" �� �� "  />
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			
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
		</FORM>	
		</TD>
	</TR>
</TABLE>
</body>

<%@ include file="/foot.jsp"%>

<SCRIPT LANGUAGE="JavaScript">
	
	function change_select(type,selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getCityNextChild.action'/>";
				$.post(url,{
				},function(ajax){
					parseMessage(ajax,$("select[@name='gwShare_cityId']"),selectvalue);
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
			case "softwareversion":
				var url = "<s:url value='/gwms/share/gwDeviceQuery!getDevicetypeST.action'/>";
				var deviceModelId = $("select[@name='device_model']").val();
				if("-1" == deviceModelId){
					$("select[@name='softwareversion']").html("<option value='-1'>==����ѡ���豸�ͺ�==</option>");
					break;
				}
				$.post(url,{
					gwShare_deviceModelId:deviceModelId
				},function(ajax){
					parseMessage(ajax,$("select[@name='softwareversion']"),selectvalue);
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
		$("select[@name='softwareversion']").html("<option value=-1>--����ѡ���ͺ�--</option>");
	}
	
	
	$(function(){
		dyniframesize();
	});

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
	
</SCRIPT>

</html>