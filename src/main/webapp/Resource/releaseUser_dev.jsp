<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<jsp:useBean id="HGWUserInfoAct" scope="request"
	class="com.linkage.litms.resource.HGWUserInfoAct" />
<%
	request.setCharacterEncoding("GBK");
	String gw_type = request.getParameter("gw_type");
%>
	
<SCRIPT LANGUAGE="JavaScript">
<!--
function nextStep(){

    if(document.frm.device_serialnumber.value.trim().length < 5){
            alert("�������������5λ�豸���к� !");
            document.frm.device_serialnumber.focus();
            return;        
    }
	var page = "releaseUser_devDetail.jsp?device_serialnumber=" + document.frm.device_serialnumber.value + "&gw_type="+<%= gw_type%> +"&refresh="+Math.random();
	
	document.all("childFrm").src = page;
}

function doRelease(gw_type){
	var arrObj = document.all("userDev");
	var strDeviceIDs = "";
	var strUser = "";
	if(typeof(arrObj.length) == "undefined"){
		if(arrObj.checked){
			strDeviceIDs = "'" + arrObj.value + "'";
			strUser= "'" + arrObj.username + "'";
		}
	}else{
		for(var i=0;i<arrObj.length;i++){
			if(arrObj[i].checked){
				if(strDeviceIDs == ""){
					strDeviceIDs += "'" + arrObj[i].value + "'";
					strUser += "'" + arrObj[i].username + "'";
				}else{
					strDeviceIDs +=  ",'" + arrObj[i].value + "'";
					strUser += ",'" + arrObj[i].username + "'";
				}
			}
		}
	}
	if(strDeviceIDs == "" || strUser == ""){
		alert("��ѡ�����豸���û�!");
		return;
	}
	
	document.frm.deviceIDS.value=strDeviceIDs;
	document.frm.userNameS.value=strUser;
	document.frm.gw_type.value=gw_type;	
	document.frm.submit();
	
}
//-->
</SCRIPT>

<link rel="stylesheet" href="../css/listview.css" type="text/css">
<FORM name="frm" action="releaseDevSave.jsp" method="post">
<input type="hidden" name="deviceIDS" value="">
<input type="hidden" name="userNameS" value="">
<input type="hidden" name="gw_type" value="">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD HEIGHT="20">
				&nbsp;
			</TD> 
		</TR>
		<TR>
			<TD>
				<TABLE width="98%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD>
							<table width="100%" height="30" border="0" cellspacing="0"
								cellpadding="0" class="green_gargtd">
								<tr>
									<td width="162">
										<div align="center" class="title_bigwhite">
											�ֳ���װ
										</div>
									</td>
									<td>
										<img src="../images/attention_2.gif" width="15" height="12">
										���û����豸���н��,�������������5λ���к�.&nbsp;
										ע������
										<font color=red>*</font>���
									</td>
								</tr>
							</table>
						</TD>
					</TR>
					<TR>
						<TD bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<TR>
									<TH colspan="3">�û��豸���</TH>
								</TR>
								<TR bgcolor="#ffffff">
									<TD align="right" width="20%">
										�豸���к�: 
									</TD>
									<TD align="left" width="50%">
										<input type="text" name="device_serialnumber" class="bk" size="30">&nbsp;&nbsp;<font color="red">*</font>&nbsp;&nbsp;(�������������5λ.)
									</TD>
									<TD align="center" >
										<input type="button" class="btn" name="button" value=" ��һ�� " style="display:" onclick="nextStep();">&nbsp;
									</TD>
								</TR>
								<TR>
									<TD colspan="4" height="20" bgcolor="#ffffff">
											&nbsp;
									</TD>
								</TR>
							</TABLE>
							<div id="idBody">
							</div>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
		<TR>
			<TD HEIGHT=20>
				&nbsp;
				<IFRAME ID=childFrm name="childFrm" STYLE="display:none"></IFRAME>
			</TD>
		</TR>
	</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>