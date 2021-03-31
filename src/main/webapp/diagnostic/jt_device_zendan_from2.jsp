<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_setGaoji();
});

     function ExecMod(){
	   	if(CheckForm()){
	   		var __device_id = $("input[@name='textDeviceId']").val();
		
			if(__device_id == null || __device_id == ""){
				alert("���Ȳ�ѯ�豸!");
				return false;
			}	
	        page = "jt_device_zendan_from2_save.jsp?device_id=" +__device_id + "&NumberOfRepetions=" +$("input[@name='NumberOfRepetions']").val()+ "&ATMF5=" +$("select[@id='Interface']").val()+ "&Timeout=" +$("input[@name='Timeout']").val();
			document.all("div_ATMF").innerHTML = "����������Ͻ���������ĵȴ�....";
			document.all("childFrm").src = page;
		}else{
			return false;
		}	
    }
	
	function CheckForm(){
		var rule = /^[0-9]*[1-9][0-9]*$/;//������ʽ��/��/֮��    	
		var obj = document.frm;
	    if($("select[@id='Interface']").val() == "-1"){
	    	alert("���ȡWAN���ӣ�");
			$("select[@id='Interface']").focus();
			return false;
		}
		var Timeout = $("input[@name='Timeout']").val();
	    if(!IsNull(Timeout,'��ʱʱ��')){
			$("input[@name='Timeout']").focus();
			return false;
		}
		if(Timeout!="" && !IsNumber(Timeout,"��ʱʱ��")){	
			$("input[@name='Timeout']").focus();
			return false;
	    }
	    var NumberOfRepetions = $("input[@name='NumberOfRepetions']").val();			
		if(!IsNull(NumberOfRepetions,'����Ŀ')){
			$("input[@name='NumberOfRepetions']").focus();
			return false;
		}		
		if(!rule.test(NumberOfRepetions)){
	    	alert("����Ŀ���������0������");
	        return false;
	    }
		
			return true;
					
	}
	
	function getInterfaceAct(){
		var __device_id = $("input[@name='textDeviceId']").val();
		
		if(__device_id == null || __device_id == ""){
			alert("���Ȳ�ѯ�豸!");
			return false;
		}
		getPingInterface(__device_id);
	}

	function getPingInterface(_device_id){
		$("span[@id='divATMF5Interface']").html("<font color=blue>���ڻ�ȡ...</font>");
		//document.getElementById("Interface").onfocus = null;
		var page = "getATMF5LOOPInterface.jsp";
		page += "?device_id="+_device_id;
		page += "&TT="+new Date();
		document.all("childFrm").src = page;
	}
	function setPingInterface(html){

		if(html == "���òɼ�ʧ��" || html == "û�л�ȡ��Wan�ӿ�")
		{
			alert(html);
			document.getElementById("divPingInterface").innerHTML = "<font color='red'>" + html + "</font>";
		}else{
			document.getElementById("divPingInterface").innerHTML = html;
		}
	}
	
	function deviceResult(returnVal){
		
		$("tr[@id='trDeviceResult']").css("display","");

		$("td[@id='tdDeviceSn']").html("");
		$("td[@id='tdDeviceCityName']").html("");
		
		for(var i=0;i<returnVal[2].length;i++){
			$("input[@name='textDeviceId']").val(returnVal[2][i][0]);
			$("td[@id='tdDeviceSn']").append(returnVal[2][i][1]+" -"+returnVal[2][i][2]);
			$("td[@id='tdDeviceCityName']").append(returnVal[2][i][5]);		
		}
	}
	
	
</SCRIPT>
<%@ include file="../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="POST" ACTION=""
				onSubmit="return CheckForm();">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											ATMF5LOOP����
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td bgcolor="#FFFFFF">
							<table width="100%" border=0 align="center" cellpadding="1"
								cellspacing="1" bgcolor="#999999" class="text">
								<TR bgcolor="#FFFFFF">
									<td colspan="4">
									<%@ include file="/gwms/share/gwShareDeviceQuery.jsp"%>
									</td>
								</TR>
								<TR bgcolor="#FFFFFF" id="trDeviceResult" style="display: none">
									<td nowrap align="right" class=column>
											�豸����
									</td>
									<td id="tdDeviceCityName">
									</td>
									<td nowrap align="right" class=column>
											�豸���к�
											<input type="hidden" name="textDeviceId" value="">
									</td>
									<td id="tdDeviceSn">
									</td>
									
								</TR>
								<tr bgcolor="#FFFFFF">
									<td nowrap class=column>
										<div align="right" width="10%">
											WAN����:
										</div>
									</td>
									<td class=column nowrap colspan="3">
										<span id="divATMF5Interface"> 
											<select id="Interface" class="bk">
												<option value="-1">==���ȡWAN����==</option>
											</select> 
										</span>
										&nbsp;&nbsp;<input type="button" value="�� ȡ" onclick="getInterfaceAct()">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td nowrap class=column>
										<div align="right" width="10%">
											��ʱʱ��(ms):
										</div>
									</td>
									<td class=column>
										<input type="text" name="Timeout" class="bk" size="16"
											value="2000">
									</td>
									<td nowrap class=column>
										<div align="right" width="10%">
											����Ŀ:
										</div>
									</td>
									<td class=column>
										<input type="text" name="NumberOfRepetions" class="bk"
											size="16" value="2">
									</td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<INPUT TYPE="button" value=" �� �� " class=btn
																onclick="ExecMod()">
															&nbsp;&nbsp;
													<INPUT TYPE="hidden" name="action" value="add">
													&nbsp;&nbsp;
												</td>
											</tr>
										</table>
									</td>
								</tr>
								
								<TR bgcolor="#FFFFFF">
									<TH colspan="4">
										��Ͻ��
									</TH>
								</TR>
								<TR bgcolor="#FFFFFF">
									<td colspan="4" valign="top" class=column>
										<div id="div_ATMF"
											style="width:100%; height:120px; z-index:1; top: 100px;"></div>
									</td>
								</TR>
							</table>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display:none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display:none"></IFRAME>
			&nbsp;
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
