<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.litms.LipossGlobals"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="../head.jsp"%>

<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

var gw_type = <%=request.getParameter("gw_type")%>

$(function(){
	gwShare_setGaoji();
});
  	function ExecMod(){ 
  		if(CheckForm()){
  			var t_obj = document.all('device_id');
  			
          	var __device_id = $("input[@name='textDeviceId']").val();
		
			if(__device_id == null || __device_id == ""){
				alert("���Ȳ�ѯ�豸!");
				return false;
			}
			var param = "&Interface=" +document.getElementById("Interface").value+ "&Host=" +$("input[@name='Host']").val()+ "&MaxHopCount=" +$("input[@name='MaxHopCount']").val()+ "&Timeout=" +$("input[@name='Timeout']").val();				
       		page = "traceRouteStatus.jsp?device_id=" + __device_id + "&gw_type=" + gw_type + param;
			document.all("div_TraceRoute").innerHTML = "����������Ͻ���������ĵȴ�....";
			document.all("childFrm").src = page;
		}else{
			return false;
		}	
    }

	function CheckForm(){
		var __device_id = $("input[@name='textDeviceId']").val();		
		if(__device_id == null || __device_id == ""){
			alert("���Ȳ�ѯ�豸!");
			return false;
		}
	    var interfaceObj = document.getElementById("Interface");
	    if(interfaceObj == null){
	       alert("���ȡ�ӿڣ�");
	       return false;
	    }
		var Interface = interfaceObj.value;
		if(Interface == "" || Interface == null){
	    	alert("���ȡ�ӿڣ�");
		    document.getElementById("Interface").focus();
			return false;
	    }
	   	var Host = $("input[@name='Host']").val();
        if(!IsNull(Host,'����IP')){
			$("input[@name='Host']").focus();
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
		getTraceRouteInterface(__device_id);

	}

	function getTraceRouteInterface(_device_id){
		document.getElementById("divTraceRouteInterface").innerHTML = "<font color=blue>���ڻ�ȡ...</font>";
		//document.getElementById("Interface").onfocus = null;
		var page = "../diagnostic/getPingInterface.jsp";
		page += "?device_id="+_device_id;
		page += "&gw_type="+gw_type;
		page += "&TT="+new Date();
		//alert(page);
		document.getElementById("childFrm").src = page;
	}
	
	function setPingInterface(html){
		//alert(html);
		if(html == "���òɼ�ʧ��" || html == "û�л�ȡ��Wan�ӿ�"){
			alert(html);
			document.getElementById("divTraceRouteInterface").innerHTML = "<font color='red'>" + html + "</font>";
		}else{
			document.getElementById("divTraceRouteInterface").innerHTML = html;
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

		var area = "<%=LipossGlobals.getLipossProperty("InstArea.ShortName")%>";
		if(!"sx_lt" == area && gw_type == '2'){
			$("tr[@id='trUserData']").show();
			var url = '<s:url value='/gwms/blocTest/QueryCustomerInfo!query.action'/>'; 
			$.post(url,{
				device_id:returnVal[2][0][0]
			},function(ajax){	
			    $("div[@id='UserData']").html("");
				$("div[@id='UserData']").append(ajax);
			});
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
			<FORM NAME="frm" METHOD="POST" ACTION="" onSubmit="return false;">
				<table width="95%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<!-- ɽ����ͨ���ϽǱ��� -->
										<ms:inArea areaCode="sx_lt" notInMode="false">
											<div style="position: relative; left: 27px" class="title_bigwhite">TRACE ROUTE���</div>
										</ms:inArea>
										<ms:inArea areaCode="sx_lt" notInMode="true">
											<div align="center" class="title_bigwhite">TRACE ROUTE���</div>
										</ms:inArea>
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
									<td nowrap align="right" class=column width="15%">
											�豸����
									</td>
									<td id="tdDeviceCityName">
									</td>
									<td nowrap align="right" class=column width="15%">
											�豸���к�
											<input type="hidden" name="textDeviceId" value="">
									</td>
									<td id="tdDeviceSn">
									</td>
								</TR>
								<tr id="trUserData" style="display: none" bgcolor="#FFFFFF">
												<td class="colum" colspan="4">
													<div id="UserData"
														style="width: 100%; z-index: 1; top: 100px">
													</div>
												</td>
											</tr>
								<tr bgcolor="#FFFFFF" id="line1" style="display:">
									<td nowrap class=column width="15%">
										<div align="right">
											�ӿ�:
										</div>
									</td>
									<td class=column nowrap width="35%">
										<span id="divTraceRouteInterface" style="width:auto"> <input type="text" name="Interface" id="Interface" class="bk" size="16">
										</span> &nbsp;&nbsp;
										<button onclick="getInterfaceAct()">�� ȡ</button>
									</td>
									<td nowrap class=column width="15%">
										<div align="right">
											���Ե�ַ:
										</div>
									</td>
									<td class=column width="35%">
										<input type="text" name="Host" class="bk">(IP��ַ������)
									</td>
								</tr>
								<tr bgcolor="#FFFFFF" id="line2" style="display:">
									<td nowrap class=column width="15%">
										<div align="right">
											�����ת��:
										</div>
									</td>
									<td class=column width="35%">
										<input type="text" name="MaxHopCount" class="bk"
											size="16" value="30">
									</td>
									<td nowrap class=column width="15%">
										<div align="right">
											�ȴ�ʱ��(ms):
										</div>
									</td>
									<td class=column width="35%">
										<ms:inArea areaCode="sx_lt" notInMode="false">
										 <input type="text" name="Timeout" class="bk" size="16"
											value="100">
										</ms:inArea>
										<ms:inArea areaCode="sx_lt" notInMode="true">
										 <input type="text" name="Timeout" class="bk" size="16"
											value="5000">
										</ms:inArea>
									</td>
								</tr>
								<tr bgcolor="#FFFFFF">
									<td  colspan="4">
										<table width="100%" border="0" cellspacing="0"
											cellpadding="0">
											<tr align="right" CLASS="green_foot">
												<td>
													<button onclick="ExecMod()">�� ��</button>
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
										<div id="div_TraceRoute"
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