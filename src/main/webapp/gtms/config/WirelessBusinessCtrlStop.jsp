<%--
Author      : ������
Date		: 2015-02-10
Desc		: ���ߵĹر�
--%>
<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ include file="../../head.jsp"%>
<%@page import="com.linkage.commons.util.DateTimeUtil"%>
<%
 String gwType = request.getParameter("gw_type");
 String awifi_type = request.getParameter("awifi_type");
//��ȡ��ǰʱ��
		int hour = new DateTimeUtil().getHour();
		String used = "0";
		if(hour == 22 ||  hour ==23){
			used = "1";
		}
 %>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	gwShare_queryChange('3');
	$("input[@name='gwShare_queryResultType']").val("checkbox");
	var used = <%=used%>;
	if(used != '1'){
		$("#doButton").attr("disabled",true);
		$("input[@name='gwShare_queryButton']").attr("disabled",true);
		$("input[@name='gwShare_reButto']").attr("disabled",true);
		$("tr[@id='gwShare_tr31']").css("display","none");
	}
});
function CheckForm(){
	if($("input[@name='deviceId']").val()==""){
		alert("��ѡ���豸��");
		return false;
	}
	return true;
}


	
</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>

			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0" class="text">
				<tr>
					<td>
						<table width="100%" height="30" border="0" cellspacing="0"
							cellpadding="0" class="green_gargtd">
							<tr>
									<td width="162" align="center" class="title_bigwhite" nowrap>
										��������ҵ��ر�
									</td>
								<td nowrap>
									<img src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12"><font color="red">�˹��ܽ���22:00-23:59ʹ��</font>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<%@ include file="/gwms/share/gwShareDeviceQuery_wirelessStop.jsp"%>
					</td>
				</TR>
				<TR>
					<TH colspan="4" align="center">
						������������
					</TH>
				</TR>
				<tr>
					<td>
						<FORM NAME="frm" METHOD="post"
							ACTION=""
							onsubmit="return CheckForm()">
							<input type="hidden" name="deviceIds" value="" />
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%" id="table_showConfig">
											<tr bgcolor="#FFFFFF">
												<td colspan="6">
													<div id="selectedDev">
														���ѯ�豸��
													</div>
												</td>
											</tr>
											<tr bgcolor="#FFFFFF" id="tr_strategybs"  >

												<TD align="right" width="10%">
													���Է�ʽ��
												</TD>
												<TD width="20%">
													<select id="strategy_type" name ="strategy_type">
														<option value="1">����ִ��</option>
														<option value="2">�ն��ϱ�ִ��</option>
														<option value="3">�ն�������ִ��</option>
													</select>
												</TD>
												<TD align="right" width="10%">
													���߹رն˿ڣ�
												</TD>
												<TD width="20%">
													<select id="wireless_port" name ="wireless_port">
														<option value="3">SSID3</option>
														<option value="4" selected= "selected">SSID4</option>
													</select>
												</TD>
											</tr>
											<TR>
												<TD colspan="4" align="right" CLASS="green_foot">
													<INPUT TYPE="button" id="doButton" name="doButton" onclick="doExecute(0,1)" value=" ִ�� " class=btn>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
													<TD colspan="6" align="left" class="green_foot">
														<div id="resultDIV" />
													</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
								<tr>
								<td height="20">
								</td>
								</tr>
								<tr id="trData" style="display: none">
									<td class="colum">
										<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
											���ڲ��������Ե�....
										</div>
									</td>
								</tr>
								<tr>
									<td height="20">
									</td>
								</tr>
							</TABLE>
						</FORM>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			&nbsp;
			<IFRAME ID=childFrm SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm1 SRC="" STYLE="display: none"></IFRAME>
			<IFRAME ID=childFrm2 SRC="" STYLE="display: none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<script type="text/javascript">
var deviceId ;
//��ѯ���豸
function deviceResult(returnVal){	
	$("#doButton").attr("disabled",false);
	
	deviceId="";
	$("table[@id='tr_strategybs']").css("display","");
	$("#resultDIV").html("");
	var totalNum = returnVal[0];
	if(returnVal[0]==0)
	{
		totalNum = returnVal[2].length;
		var deviceIdArray = returnVal[2];
		for(var i=0 ;i<deviceIdArray.length;i++){
			//����������deviceId
			deviceId +=  deviceIdArray[i][0]+",";
		}
		var endIndex = deviceId.lastIndexOf(",");
		deviceId = deviceId.substring(0,endIndex);
		var tips = "";
		var url = "<s:url value='/gtms/config/wirelessBusnissCtrl!isHaveStrategy.action'/>";
			$.post(url,{
	          deviceIds : deviceId
	       },function(ajax){
	    	   tips = ajax;
	    	   if(ajax != ""){
	    		$("#doButton").attr("disabled",true);
	    	   }
	    		$("div[@id='selectedDev']").html("<font size=2>����"+totalNum+"̨�豸</font>" + "  <font color='red'>"+tips+"</font>");
	        });
			$("div[@id='selectedDev']").html("<font size=2>����"+totalNum+"̨�豸</font>" + "  <font color='red'>"+tips+"</font>");
	}
	else{
		//������ѯ
		deviceId= returnVal[2][0][0] ;
		if(deviceId==""){
			$("#selectedDev").html("���û������ڻ�δ���նˣ�");
		}
	}
}

function doExecute(flag,do_type){
	
	var strategy_type  = $("select[@id='strategy_type']").val();
	var wireless_port  = $("select[@id='wireless_port']").val();
	var vlanid;
	if(wireless_port == 3){
		vlanid = 33;
	}else if(wireless_port == 4){
		vlanid = 32;
	}
	var url = "<s:url value='/gtms/config/wirelessBusnissCtrl!doConfigAll.action'/>";
	$("tr[@id='trData']").show();
	$("#doButton").attr("disabled",true);
	$("div[@id='QueryData']").html("���ڲ��������Ե�....");
	if(CheckForm())
	{
		$.post(url,{
            flag : flag,
            vlanIdMark : vlanid,
            wireless_port : wireless_port,
            strategy_type : strategy_type,
            deviceIds : deviceId,
            gw_type: <%=gwType%>,
            do_type: do_type,
            awifi_type :��<%=awifi_type%>
         },function(ajax){
	         	$("#resultDIV").html("");
	            $("#doButton").attr("disabled",true);
				if("1"==ajax){
					$("#resultDIV").append("��ִ̨�гɹ�");
				}else if ("-4"==ajax){
					$("#resultDIV").append("��ִ̨��ʧ��");
				}else{
					$("#resultDIV").append("��ִ̨���쳣");
				} 
				$("#doButton").attr("disabled",false);
	         	 $("div[@id='QueryData']").html("");
				 //$("div[@id='QueryData']").append(ajax);
				$("button[@name='button']").attr("disabled", false);
          });
	}
	
}
</script>
<%@ include file="../../foot.jsp"%>
