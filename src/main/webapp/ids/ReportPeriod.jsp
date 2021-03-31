<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
  String gw_type = request.getParameter("gw_type"); 
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../Js/prototype.js"></SCRIPT>
<script type="text/javascript" src="../Js/jquery.js"/></script>

<SCRIPT LANGUAGE="JavaScript">
var gw_type = "<%=gw_type%>" ;
function checkForm(){
	
	var excute_type_radios = document.all("excute_type");
    var excute_type = "";
    for(var i=0;i<excute_type_radios.length;i++)
    {
      if(excute_type_radios[i].checked)
	  {
	    excute_type = excute_type_radios[i].value;
	    break;
	  }
    }
	//var reporttimelist = $("input[@name='reporttimelist']").val();
	var reporttimelist =$("#reporttimelist").val();
	//var targettimelist = $("select[@name='targettimelist']").val();
	var targettimelist = $("#targettimelist").val();
	var deviceSN = $("input[@name='deviceSN']").val();
	
	var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
	
	
	if(excute_type==0&&deviceSN=="")
	{
		alert("请选择设备！");
		return false;
	}
	
	if(excute_type==1&&gwShare_fileName=="")
	{
		alert("请导入设备！");
		return false;
	}
	$("input[@name='excute_type']").attr("value",excute_type);
		
    $("tr[@id='trData']").show();
    $("#btn").attr("disabled",true);
    $("div[@id='QueryData']").html("正在努力为您定制，请稍等....");
    var url = "reportPeroid.action"; 
	$.post(url,{
		deviceSN  : deviceSN,
		reporttimelist  : reporttimelist,
		targettimelist : targettimelist,
		gwShare_fileName : gwShare_fileName,
		excute_type : excute_type 
	},function(ajax){
		$("#btn").attr("disabled",false);
	    $("div[@id='QueryData']").html("");
		$("div[@id='QueryData']").append(ajax);
	});
}

function displayType(type)
{
	if (type == 1)
	{
		$("tr[@id='device']").css("display","none");
		$("tr[@id='upload']").css("display","");
		$("tr[@id='gwShare_tr32']").css("display","");

		
	}else
		{
			$("tr[@id='device']").css("display","");
			$("tr[@id='upload']").css("display","none");
			$("tr[@id='gwShare_tr32']").css("display","none");

		}
		
}
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<form name="frm" action="reportPeroid.action" onsubmit="return checkForm()">
	
<TABLE border=0 cellspacing=0 cellpadding=0 width="95%" align="center">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr><td>
	<table width="98%" height="30" border="0" align="center"
			cellpadding="0" cellspacing="0" class="green_gargtd">
			<tr>
				<td width="162">
				<div align="center" class="title_bigwhite">上报周期变更</div>
				</td>
			</tr>
		</table>
</td></tr>
<TR><TD>
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR >
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=0 width="100%" id="outTable">
					<TR>
						<TH bgcolor="#ffffff" colspan="4" align="center">上报周期变更</TH>
					</TR>
					<TR bgcolor="#FFFFFF" >
						<TD width="30%" align="left" colspan=5>
							<input type="radio" name="excute_type" value="0" onclick="displayType(this.value)" checked>
							单台设备定制
							<input type="radio" name="excute_type" value="1" onclick="displayType(this.value)">
							导入文件定制
						</TD>
					</TR>
					<TR bgcolor="#FFFFFF" width="4%" id="device">
						<TD class=column align="right" width="15%" nowrap>设备序列号</TD>
						<TD width="85%" colspan=3>
							<INPUT TYPE="text" name="deviceSN" class="bk">&nbsp;
						</TD>
					</TR>
					
				<tr bgcolor="#FFFFFF" id="upload" STYLE="display:none">
					<td align="right" width="15%">提交文件</td>
					<td colspan="3" width="85%">
						<div id="importUsername">
							<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO src="../gwms/share/FileUpload.jsp" height="20" width="100%">
							</iframe>
							<input type="hidden" name=gwShare_fileName value=""/>
						</div>
					</td>
				</tr>
				<tr id="gwShare_tr32" style="display:none">
					<td CLASS="green_foot" align="right">注意事项</td>
					<td colspan="3" CLASS="green_foot">
					1、需要导入的文件格式限于Excel、即xls格式 。
					 <br>
					2、文件的第一行为标题行，即【设备序列号】。
					 <br>
					3、文件只有一列。
					 <br>
					4、文件行数不要太多，以免影响性能。
					</td>
				</tr>
				
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="10%" nowrap>上报周期(分钟)</TD>
							<TD width="40%" colspan="3" >
							<input type="text" onkeyup="value=value.replace(/[^\d]/g,'')"  id ="reporttimelist" name="reporttimelist" class="bk" style='width:150px' >
							</TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" width="10%" nowrap>目标周期(分钟)</TD>
							<TD width="40%" colspan="3" >
								<input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" id ="targettimelist" name="targettimelist"  class="bk" style='width:150px'>
							</TD>
					</TR>
					<TR class="green_foot">
						<TD  align="right" height="23" colspan=4>
							<input type=button name="queryBtn" value="定制 " onclick="checkForm();">
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	<tr id="trData" style="display: none" >
			<td>
				<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
					正在努力为您定制，请稍等....
				</div>
			</td>
		</tr>
</TD></TR>
</TABLE>
</form>

