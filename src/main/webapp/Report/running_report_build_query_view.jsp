<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ include file="../head.jsp"%>
<%@ include file="../toolbar.jsp"%>
<%@ page import="com.linkage.litms.common.database.DataSetBean,java.util.Map,java.io.BufferedReader,java.io.FileNotFoundException,java.io.FileReader,java.io.IOException"%>
<%
	request.setCharacterEncoding("GBK");
	String file_path = request.getParameter("file_path");
	String html = "";
	String suggestion = "";

	String sql = "select suggestion from tab_rrct_report where file_path='" + file_path + "'";
	Map map = DataSetBean.getRecord(sql);
	suggestion = map.get("suggestion") == null ? "" : (String)map.get("suggestion");
    try {
		BufferedReader reader = new BufferedReader(new FileReader(file_path));
		String line = reader.readLine();
		while (line != null) {
			html += line;
			line = reader.readLine();
		}
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
%>

<style type='text/css'>
<!--
BODY.bd {
	BACKGROUND-COLOR: #ffffff;
	COLOR: #000000;
	FONT-FAMILY: '宋体', 'Arial';
	FONT-SIZE: 12px;
	MARGIN: 0px
}
TH.thh {
	background-color: #B0E0E6;
	font-size: 9pt;
	color: #000000;
	text-decoration: none;
	font-weight: bold;
	line-height: 22px;
	text-align: center;
}
TR.trr {
	background-color:'#FFFFFF';
}
TD.tdd {
	FONT-FAMILY: '宋体', 'Tahoma';
	FONT-SIZE: 12px;
	background-color:'#FFFFFF';
}
TD.hd {
	background-color:'#EEE8AA';
}
-->
</style>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>
			&nbsp;
		</TD>
	</TR>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" ACTION="paramAllList.jsp" onsubmit="return CheckForm()">
				<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="text">
					<tr>
						<td>
						<table width="100%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd">
							<tr>
								<td width="162" align="center" class="title_bigwhite" nowrap>
									运行报告查看
								</td>
								<td nowrap>
									&nbsp;&nbsp;
									<IMG SRC="../images/print.gif" WIDTH="16" HEIGHT="16" BORDER="0" ALT="打印" onclick="window.print()" style="cursor:hand">
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR>
												<TH colspan="2" align="center">
													报表内容
												</TH>
											</TR>
											<TR bgcolor="#FFFFFF" id="tr_filecontext">
												<TD>
													<span id="id_filecontext"><%=html%></span>
												</TD>
												<TD width="250" valign="bottom">
													<h3>专家意见：</h3>
													<br><TEXTAREA name="suggestion" cols="30" rows="30"><%=suggestion%></TEXTAREA>
													<br><br><input type="button" class="btn" value=" 保 存 " onclick="saveSuggestion()">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
	<TR>
		<TD HEIGHT=20>
			<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id="WebBrowser" width=0></OBJECT>
			<IFRAME ID=childFrm SRC="" STYLE="display:none"></IFRAME>
		</TD>
	</TR>
</TABLE>
<%@ include file="../foot.jsp"%>
<SCRIPT LANGUAGE="JavaScript">
<!--
	function sendFile() {
		var file_path = document.forms[0].fileList.value ;
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&action_type=4&refresh=" + new Date().getTime();
		//alert(page);
		document.all("childFrm").src = page;
	}
	
	function saveSuggestion() {
		var file_path = "<%=file_path%>";
		var suggestion = document.forms[0].suggestion.value;
  		page = "running_report_build_save.jsp?file_path=" + file_path + "&suggestion=" + suggestion + "&action_type=3&refresh=" + new Date().getTime();
		document.all("childFrm").src = page;
	}

	function setId(id, msg) {
		document.getElementById(id).innerHTML = msg;
	}

	function showId(id) {
		document.getElementById(id).style.display = "";
	}

	function hideId(id) {
		document.getElementById(id).style.display = "none";
	}
//-->
</SCRIPT>