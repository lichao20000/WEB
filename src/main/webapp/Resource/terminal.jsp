<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>

<script language="JavaScript">
function selectDeviceUnit()
{   
   var device_oui = document.all("device_oui").value;
   var device_serialnumber = document.all("device_serialnumber").value;
   if(device_serialnumber == ""){   
     alert("请输入设备序列号!");   
     document.all("device_serialnumber").focus();
     return false;  
   }   
     document.all("childFrm").src ="terminal_submit.jsp?device_oui="+device_oui+"&device_serialnumber="+device_serialnumber;
}
function showService(oui,serialnumber,gw_type){

	if(gw_type == 0){
		alert("未知设备,未开通业务!");
	} else {
		var strpage = "deviceServiceShow.jsp?oui=" + oui + "&serialnumber="+ serialnumber + "&gw_type=" + gw_type;
		window.open(strpage,"","left=20,top=20,width=500,height=400,resizable=yes,scrollbars=yes");
	}
	
}
</script>
<form>
<TABLE boder=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
	</TR>
	<TR>
		<TD>
		<TABLE width="95%" border=0 cellspacing=0 cellpadding=0 align="center">
			<tr>
				<td>
				<table width="100%" height="30" border="0" cellspacing="0"
					cellpadding="0" class="green_gargtd">
					<tr>
						<td width="162"  align="center" class="title_bigwhite">报表统计</td>
					</tr>
				</table>
				</td>
			</tr>
			
			<TR>
				
				
				<TD bgcolor=#000000>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR><TH colspan="4">终端能力统计</TH></TR>
					<TR bgcolor="#ffffff">
						<TD align="right" width="15%"> 设备OUI：</TD>
						<TD align="left"  width="35%"><input type="text" name="device_oui"
							value="" class="bk" size=20>
						</TD>
						<TD  align="right" width="15%">设备序列号：</TD>
						<TD align="left"  width="35%">
							<input type="text"
								name="device_serialnumber" value="" class="bk" size=40>&nbsp;&nbsp; 
						</TD>
					</TR>
					<TR bgcolor="#ffffff"><TD colspan="4" align="right" class="green_foot"><input type="button" value=" 查 询 " class="btn"  onClick="selectDeviceUnit()"></TD></TR>										
				</TABLE>
				</TD>
			</TR>
			<TR>
				<TD HEIGHT=20>&nbsp;</TD>
			</TR>
			<TR id="line" style="display:none">
				<TD bgcolor=#000000>
				<DIV id="idLayer"></DIV>
				</TD>
			</TR>
			<TR>
				<TD HEIGHT=20>&nbsp;</TD>
			</TR>

		</TABLE>
	</TD>
</TR>

</TABLE>
<IFRAME name="childFrm" ID=childFrm SRC="" STYLE="display:none;width:500;height:200"></IFRAME>
</form>

<%@ include file="../foot.jsp"%>
