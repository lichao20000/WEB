<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

$(function(){
	gwShare_setGaoji();
});
var deviceId = "";
var devicetypeId = "";
var currDevCount = -1;
function ExecMod(){
	<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
	 var strategyType = $("select[@name='softStrategy_type']").val();
     if(strategyType == 0 && currDevCount > 1000){
 	   alert("����ִ������������Ե��豸�������ܳ���1000̨");
 	   return ;
     }
     var vendorId = $("select[@name='gwShare_vendorId']").val();
     var deviceModelId = $("select[@name='gwShare_deviceModelId']").val();
     devicetypeId = $("select[@name='gwShare_devicetypeId']").val();
   <%} %>
	deviceId = $("input[@name='deviceIds']").val();
    if(deviceId==""){
		alert("���ѯ�豸��");
		return false;
    }
	if($("select[@name='goal_softwareversion']").val()==undefined){
		alert("������ѡ���豸��");
		return false;
	}
	if($("select[@name='goal_softwareversion']").val() == -1){
		alert("��ѡ��汾�ļ���");
		return false;
	}
	$('#softUpgrade_btn').attr("disabled",true);
	var deviceIds = $("input[@name='deviceIds']").val();
	var param = $("input[@name='param']").val();

	var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!xjBatchSoftUpgrade.action'/>";
	goal_softwareversion = $("select[@name='goal_softwareversion']").val();

	$.post(url,{
		deviceIds:deviceIds,
		pathId:$("select[@name='goal_softwareversion']").val(),
		devicetypeId:devicetypeId,
		goal_softwareversion:goal_softwareversion,
		param:param,
		gw_type:"4",
		<%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
		vendorId:vendorId,
		deviceModelIds:deviceModelId,
		<%}%>
		softStrategy_type:$("select[@name='softStrategy_type']").val()
	},function(ajax){
		var s = ajax.split(";");
		if(s[0]=="1"){
			alert("���ú�̨�ɹ���");
			
	    }else
		{
			alert("���ú�̨ʧ�ܣ�");
		}
		var url = "<s:url value='/gtms/stb/resource/patchSoftUpgrade.jsp'/>";
        window.location.href=url;
		$('#softUpgrade_btn').attr("disabled",false);
	});
}


function deviceResult(returnVal){
	deviceIds="";
	if(returnVal[0]==0){
        currDevCount = returnVal[2].length;
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+currDevCount+"</strong></font>");
		for(var i=0;i<returnVal[2].length;i++){
			deviceIds = deviceIds + returnVal[2][i][0]+",";			
		}
		$("input[@name='deviceIds']").val(deviceIds);
	}else{
// 		if (returnVal[0]>200)
// 		{
// 			alert("�������豸��Ŀ̫�࣬������ѡ��");
// 			return;
// 		}
        currDevCount = returnVal[0];
		$("div[@id='selectedDev']").html("<font size=2><strong>�������豸��Ŀ:"+currDevCount+"</strong></font>");
		$("input[@name='deviceIds']").val("0");
		$("input[@name='param']").val(returnVal[1]);
	}
	
	$("tr[@id='softwareversion']").show();
	var url = "<s:url value='/gtms/stb/resource/stbSoftUpgrade!showALLSoftwareversion.action'/>";
	$("div[@id='div_goal_softwareversion']").html("");
	$.post(url,{
		deviceIds:deviceIds
	},function(ajax){
		if(ajax!=""){
			var lineData = ajax.split("#");
			if(typeof(lineData)&&typeof(lineData.length)){
				$("div[@id='div_goal_softwareversion']").append("<select name='goal_softwareversion' class='bk' style='width: 400px'>");
				$("select[@name='goal_softwareversion']").append("<option value='-1' selected>==��ѡ��==</option>");
				for(var i=0;i<lineData.length;i++){
					var oneElement = lineData[i].split("$");
					var xValue = oneElement[0];
					var xText = oneElement[1];
					option = "<option value='"+xValue+"'>=="+xText+"==</option>";
					$("select[@name='goal_softwareversion']").append(option);
				}
			}else{
				$("div[@id='div_goal_softwareversion']").append("���豸û�п������İ汾�ļ���");
			}
		}else{
			$("div[@id='div_goal_softwareversion']").append("���豸û�п������İ汾�ļ���");
		}
	});
}

</SCRIPT>



<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD>
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
			����ǰ��λ�ã��豸�����������
		</TD>
	</TR>
</TABLE>
<br>
<table width="98%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<TR>
		<td>
			<%@ include file="../share/gwShareDeviceQueryCheck.jsp"%>
		</td>
	</TR>


	<TR id="softwareversion" style="display: none">
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="">
				<input type="hidden" name="deviceIds" value="" />
				<input type="hidden" name="param" value="" />
				<table width="100%" class="querytable">
					<TR>
						<TH colspan="5" class="title_1">
							�汾����
						</TH>
					</TR>
					<TR id="trDeviceResult" style="display: none">
						<td nowrap class="title_2" width="15%">
							�豸���к�
						</td>
						<td id="tdDeviceSn" width="35%">
						</td>
						<td nowrap class="title_2" width="15%">
							����
						</td>
						<td id="tdDeviceCityName" width="35%">
						</td>
					</TR>
					<TR>
					<TD align="right" width="15%">����������Է�ʽ��</TD>
                     <TD width="30%">
                     <SELECT name="softStrategy_type">
                       <option value="5">
                               	�ն�����
                       </option>
                       <option value="4">
                             	  �´����ӵ�ϵͳ
                       </option>
                       <%if("xj_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ %>
                       <option value="0">����ִ��</option>
                       <%} %>
                     </SELECT>

                     </TD>
						<td nowrap class="title_2" width="15%">
							<font color="red">*</font>&nbsp;Ŀ��汾
						</TD>
						<TD width="">
							<div id="div_goal_softwareversion">

							</div>
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD colspan="5" CLASS="foot">
							<div class="right">
								<button id="softUpgrade_btn" onclick="ExecMod()">
									�� ��
								</button>
							</div>
						</TD>
					</TR>
				</TABLE>
			</FORM>
		</TD>
	</TR>
</TABLE>

