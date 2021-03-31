<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.linkage.module.gtms.stb.diagnostic.serv.PingInfoBIO" %>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage" %>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/CheckForm.js"/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript">
	var gw_type = "<%=request.getParameter("gw_type")%>";
	
	$(function(){
		gwShare_setGaoji();
		changeSelect("city", "-1");
		
		var isGoPage = '<s:property value="isGoPage"/>';
	 	if(isGoPage != 1){
	 		$("#chooseDevHtml").html("");
		}else{
			// ҳ����ת
			$("#goPageForStbDevicePingTest").show().siblings("span").html("");
		}
	});
	
	// ɽ����ͨ����ʾ�м��豸ѡ��ҳ��
	function deviceInfo(url){
		$.post(url,{},function(ajax){
			$("#chooseDevHtml").html("");
	   		$("#chooseDev").html("");
	   		$("#chooseDev").append(ajax);
	   		// ҳ����ת
	   		$("#goPageForStbDevicePingTest").show().siblings("span").html("");
	   	});
	}
	
	function deviceResult(returnVal){
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='deviceId']").val(returnVal[2][i][0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[2][i][1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[2][i][5]);
		}
	}
	
	/*------------------------------------------------------------------------------
	//������:		queryChange
	//����  :	change 1:�򵥲�ѯ��2:�߼���ѯ
	//����  :	���ݴ���Ĳ���������ʾ�Ľ���
	//����ֵ:		��������
	//˵��  :	
	//����  :	Create 2009-12-25 of By qxq
	------------------------------------------------------------------------------*/
	function queryDevice(){
		var	width=800;    
		var height=500;    
		var url="<s:url value="/gtms/stb/resource/gwDeviceQuery!queryDeviceList.action"/>?queryResultType=radio";
		var returnVal=window.showModalDialog(url ,'','dialogWidth='+width+'px;dialogHeight='+ height+'px;resizable=no;help=no;center=yes;status=no;scroll=no;edge=sunken');    
		if(typeof(returnVal)=='undefined'){
			return;
		}else{
			$("input[@name='deviceId']").val(returnVal[0]);
			$("tr[@id='trDeviceResult']").css("display","");
			$("td[@id='tdDeviceSn']").html(returnVal[1]);
			$("td[@id='tdDeviceSn']").append("-");
			$("td[@id='tdDeviceSn']").append(returnVal[2]);
			$("td[@id='tdDeviceCityName']").html(returnVal[5]);
		}
	}
	
  	function ExecMod(){ 
  		if(!CheckForm()){
  			return;
  		}
  		
		$('#ping_btn').attr("disabled",true);
		$("div[@id='div_ping']").html("������ϣ����Ժ�...");
		var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!pingDiag.action'/>";
		var deviceId = $("input[@name='deviceId']").val();
		var dataBlockSize = $("input[@name='dataBlockSize']").val();
		var numberOfRepetitions = $("input[@name='numberOfRepetitions']").val();
		var hostIp = $("input[@name='hostIp']").val();
		var timeout  = $("input[@name='timeout']").val();
		$.post(url,{
			deviceId:deviceId,
			dataBlockSize:dataBlockSize,
			numberOfRepetitions:numberOfRepetitions,
			hostIp:hostIp,
			timeout:timeout,
			gw_type:gw_type
		},function(ajax){
		    $("div[@id='div_ping']").html("");
			$("div[@id='div_ping']").append(ajax);
			$('#ping_btn').attr("disabled",false);
		});	
    }

	function CheckForm(){
		var obj = document.frm;
		if(obj.deviceId.value==""){
			alert("���ѯ�豸��");
			return false;
	    }
        if(!IsNull(obj.dataBlockSize.value,'����С')){
			obj.dataBlockSize.focus();
			return false;
		}
        if(!IsNull(obj.numberOfRepetitions.value,'����Ŀ')){
			obj.numberOfRepetitions.focus();
			return false;
		}
        if(!IsNull(obj.hostIp.value,'����IP')){
			obj.hostIp.focus();
			return false;
		}
		if(!IsIPAddr(obj.hostIp.value)){
			obj.hostIp.focus();
			return false;
		}
        if(!IsNull(obj.timeout.value,'��ʱʱ��')){
			obj.timeout.focus();
			return false;
		}
		return true;					
	}
	
	function changeIP(obj)
	{
		if("-1"==obj.value){
			$("input[name=hostIp]").val("");
		}else{
			$("input[name=hostIp]").val(obj.value);
		}
	}
	
	function changeSelect(type, selectvalue){
		switch (type){
			case "city":
				var url = "<s:url value='/gtms/stb/share/shareDeviceQuery!getCityNextChild.action'/>";
				$.post(url,{
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
					$("select[@name='ipType']").html("<option value='-1'>==��ѡ����==</option>");
					$("select[@name='ipValue']").html("<option value='-1'>==��ѡIP���==</option>");
				});
				break;
				
			case "ipType":
				var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!getIpTypeByCityId.action'/>";
				var cityId = $("select[@name='cityId']").val();
				
				if("-1"==cityId){
					$("select[@name='ipType']").html("<option value='-1'>==��ѡ����==</option>");
					$("select[@name='ipValue']").html("<option value='-1'>==��ѡIP���==</option>");
					break;
				}
				
				$.post(url,{
					cityId:cityId
				},function(ajax){
					if("" == ajax){
						$("select[@name='ipType']").html("<option value='-1'>==δ����������==</option>");
						$("select[@name='ipValue']").html("<option value='-1'>==δ����������==</option>");
						$("input[name=hostIp]").val("");
					}else{
						gwShare_parseMessage(ajax,$("select[@name='ipType']"),selectvalue);
						$("select[@name='ipValue']").html("<option value='-1'>==��ѡIP���==</option>");
					}
				});
				break;
				
			case "ipValue":
				var url = "<s:url value='/gtms/stb/diagnostic/pingInfo!getIpByIpType.action'/>";
				var cityId = $("select[@name='cityId']").val();
				var ipType = $("select[@name='ipType']").val();
				
				if("-1"==ipType){
					$("select[@name='ipValue']").html("<option value='-1'>==��ѡIP���==</option>");
					break;
				}
				$.post(url,{
					cityId:cityId,
					ipType:ipType
				},function(ajax){
					gwShare_parseMessage(ajax,$("select[@name='ipValue']"),selectvalue);
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
				alert("����ʧ�ܣ�");
			}
		}
		if(flag){
			field.attr("value","-1");
		}
	}
</SCRIPT>
<TABLE width="98%" align="center" class="querytable">
	<TR>
		<TD >
			<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
            	����ǰ��λ�ã��豸���
		</TD>
	</TR>
</TABLE>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<TR >
		<td>
			<ms:inArea areaCode="sx_lt" notInMode="false">
				<%@ include file="/gtms/stb/share/gwShareDeviceQueryForSxlt.jsp"%>
			</ms:inArea>
			<ms:inArea areaCode="sx_lt" notInMode="true">
				<%@ include file="/gtms/stb/share/gwShareDeviceQuery.jsp"%>
			</ms:inArea>
		</td>
	</TR>
	<tr>
		<td id="chooseDev"></td>
	</tr>
	
	<ms:inArea areaCode="sx_lt" notInMode="false">
	<tr id="chooseDevHtml">
		<td>
			<%@ include file="/gtms/stb/share/gwShareDeviceListForSxlt.jsp"%>
		</td>
	</tr>
	</ms:inArea>
	
	<tr>
		<td bgcolor="#FFFFFF">
			<table width="100%"  class="querytable">
				<FORM NAME="frm" METHOD="POST" ACTION="">
					<input type="hidden" value="" name="deviceId"/>
					<TR  id="trDeviceResult" style="display: none">
						<td nowrap  class="title_2"  width="15%">�豸���к�</td>
						<td id="tdDeviceSn" width="35%">
						</td>
						<td nowrap  class="title_2"  width="15%">����</td>
						<td id="tdDeviceCityName" width="35%">
						</td>
					</tr>
					<TR  >
						<Td colspan="4" class="title_1">����ѡ��</Td>
					</TR>
					<tr id="line1" style="display:">
						<td nowrap class="title_2" width="10%">����С(byte)</td>
						<td width="40%">
							<input type="text" name="dataBlockSize" class="bk" size="18"
								value="32" >&nbsp;
						</td>
						<td nowrap class="title_2" width="10%">����Ŀ</td>
						<td width="40%">
							<input type="text" name="numberOfRepetitions" class="bk"
								size="16" value="2">
						</td>
					</tr>
					<tr id="line2" style="display:">
						<td nowrap  class="title_2" >Ŀ��IP</td>
						<td>
							<input type="text" maxlength="15" name="hostIp" class="bk" width="10%"/>
							<%
								if("jx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){
							%>
								<br>
								<select name="cityId" class="bk" onchange="changeSelect('ipType','-1')">
									<option value="-1">==��ѡ��==</option>
								</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="ipType" class="bk" onchange="changeSelect('ipValue','-1')">
									<option value="-1">==��ѡ����==</option>
								</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<select name="ipValue" class="bk" onchange="changeIP(this)">
									<option value="-1">==��ѡIP���==</option>
								</select>
							<%
								}else if("sx_lt".equals(LipossGlobals.getLipossProperty("InstArea.ShortName"))){ 
							%>
							<s:select onchange="changeIP(this)" list="IpMap"
									  headerKey="-1" headerValue="��ѡ��" listKey="ip_value"
									  listValue="ip_name" cssClass="bk">
							</s:select>
							<%
								}else{
							%>
							<select onchange="changeIP(this)">
								<option value="">--��ѡ��--</option>
								<!-- ����������Ͳ��Ի��� -->
								<!-- <option value="58.223.80.57">iTV�ۺ�����</option> -->
								<!-- ���� -->
								<option value="61.128.117.21">iTV�ۺ�����</option>
								<!-- <option value="58.223.251.175">��EPG������</option>
								<option value="58.223.251.139">ҵ�����ƽ̨</option> -->
								<option value="172.16.4.8">����ITVƽ̨</option>
								<option value="172.23.1.11">��ΪITVƽ̨</option>
								<option value="172.0.10.172">����������</option>
								<option value="172.0.10.178">���ܷ�����</option>
							</select>
							<%
								}
							%>
						</td>
						<td nowrap class="title_2" >��ʱʱ��(s)</td>
						<td >
							<input type="text" name="timeout" class="bk" size="16" value="10">
						</td>
					</tr>
					<tr >
						<td class="foot" colspan="4">
							<div align="right">
								<button type="button" id="ping_btn" onclick="ExecMod()"> �� �� </button>
							</div>
						</td>
					</tr>
					<TR  >
						<TH colspan="4">��Ͻ��</TH>
					</TR>
					<TR >
						<td colspan="4" valign="top">
							<div id="div_ping"
								style="width:100%; height:120px; z-index:1; top: 100px;"></div>
						</td>
					</TR>
				</FORM>
			</table>
		</td>
	</tr>
</table>
<script>
var gwShare_queryType = '<s:property value="gwShare_queryType"/>';
var gwShare_queryField = '<s:property value="gwShare_queryField"/>';
var gwShare_queryParam = '<s:property value="gwShare_queryParam"/>';
gwShare_queryChange(gwShare_queryType);
//��������
if(gwShare_queryType == 1){
	var gwShare_queryField_arr = $("input[@name='gwShare_queryField']");
	for(var i = 0; i < gwShare_queryField_arr.length; i++){
		var item = gwShare_queryField_arr[i];
		if(item.value ==  gwShare_queryField){
			item.checked = true;
		}
	}
	//����
	$("input[@name='gwShare_queryParam']").val(gwShare_queryParam);
}
</script>