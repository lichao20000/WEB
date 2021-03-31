<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
request.setCharacterEncoding("GBK");
%>
<%@ include file="../head.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--

//-->
</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<div id="PendingMessage" style="position:absolute;z-index:3;top:240px;left:250px;width:250;height:60;border-width:1;border-style:ridge;background-color:#eeeeee;padding-top:10px;display:none">
  <center>
    <table border="0">
      <tr>
        <td valign="middle"><img src="../images/cursor_hourglas.gif" border="0" WIDTH="30" HEIGHT="30"></td>
        <td>&nbsp;&nbsp;</td>
        <td valign="middle"><span id=txtLoading style="font-size:14px;">请稍等······</span></td>
      </tr>
    </table>
  </center>
</div>
<FORM NAME="frm" target="childFrm" action="" method="post">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<TABLE width="90%" border=0 cellspacing=0 cellpadding=0 align="center">
	  <TR><TD bgcolor=#999999 id="idBody">
		  <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<TR> 
			  <TH colspan="2" align="center">确认设备文件上传</TH>
			</TR>
			<TR bgcolor="#ffffff" >
			  <TD colspan=2>上传确认设备文件</TD>
			</TR>
			<TR>
			   <TD class=column>文件格式</TD>
			   <TD class=column>
			     <select name="dengji" class="form_kuang">
				<option value="0">CSV</option>
				<!-- <option value="1">其它格式</option> -->
			   </select>
			</TD>
			</TR>
			<TR bgcolor="#ffffff" colspan=2>
			  <TD class=column>文件上传</TD>
			  <TD class=column>
				<iframe name=uploadFrm FRAMEBORDER=0 SCROLLING=NO src="ConfirmDevFileUploadInner.jsp?type=office" height="25" width=600></iframe>
			  </TD>
			</TR>
		  </TABLE>
	  </TD></TR>
	</TABLE>
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;<IFRAME ID=childFrm name="childFrm" SRC="about:blank" STYLE="display:none"></IFRAME></TD></TR>
</TABLE>
</FORM>
<%@ include file="../foot.jsp"%>