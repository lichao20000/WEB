<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value='/Js/jquery.js'/>"></SCRIPT>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">
	function analysis() {
		var url = "<s:url value='/gtms/report/GBBroadBandMatch!analysis.action'/>";
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if ("" == gwShare_fileName) {
			alert("�����ϴ��ļ���");
			return;
		}
		url = url + "?gwShare_fileName=" + gwShare_fileName;
		$.post(url, function(ajax) {
			var strs= new Array();
			strs = ajax.split("#");
			$("tr[@name='data']").css("display","block");
			if (strs[0] != "1") {
				$("td[@id='file_name']").html(ajax)
			} else {
				$("tr[@id='download']").html("<td>"+strs[1]+"</td>")
			}
		});
	}

	function checkForm() {
		//$("form[@name='fileUpload']").attr("action","fileUpload.action");
		var myFile = $("input[@name='myFile']").val();
		if ("" == myFile) {
			alert("��ѡ���ļ�!");
			return false;
		}
		if (myFile.length < 4
				|| ("txt" != myFile.substr(myFile.length - 3, 3)
						&& "xls" != myFile.substr(myFile.length - 3, 3) && "csv" != myFile
						.substr(myFile.length - 3, 3))) {
			alert("�밴��ע�������ϴ��ļ���");
			return false;
		}
		return true;
	}

	function downloadTemplate() {
		var page = "<s:url value='/gtms/report/GBBroadBandMatch!downloadTemplate.action'/>";
		document.all("childFrm").src = page;
	}

	function downloadDevInfo() {
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		var page = "<s:url value='/gtms/report/GBBroadBandMatch!devInfo.action'/>"+ "?gwShare_fileName=" + gwShare_fileName;
		
		document.all("childFrm").src = page;
	}

</SCRIPT>
<%@ include file="../../toolbar.jsp"%>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD HEIGHT=20>&nbsp;</TD>
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
									���׸�ǧ�׵���ƥ��</td>
								<td nowrap><img
									src="<s:url value='/images/attention_2.gif'/>" width="15"
									height="12"></td>
							</tr>
						</table>
					</td>
				</tr>
				<TR bgcolor="#FFFFFF">
					<td colspan="4">
						<TABLE border=0 cellspacing=0 cellpadding=0 width="100%"
							align="center">
							<tr>
								<td bgcolor=#999999>
									<table border=0 cellspacing=1 cellpadding=2 width="100%"
										align="center">
										<tr>
											<th colspan="6" id="gwShare_thTitle">����ƥ��Ĵ�������</th>
										</tr>
										<tr id="gwShare_tr31" bgcolor="#FFFFFF">
											<td align="right" width="15%">�ύ�ļ�</td>
											<td colspan="5" width="85%">
												<div id="importUsername">
													<iframe name="gwShare_loadForm" FRAMEBORDER=0 SCROLLING=NO
														src="<s:url value="/gwms/share/FileUpload.jsp"/>"
														height="30" width="100%"> </iframe>
													<input type="hidden" name=gwShare_fileName value="" />
												</div> 
											</td>
										</tr>
										<tr>
											<td bgcolor="#FFFFFF" align="right">ע������</td>
											<td bgcolor="#FFFFFF" colspan="5">
												<div>
													1����Ҫ������ļ���ʽΪExcel2003�ļ�����xls��ʽ������txt�ı���ʽ ��<br> 2���ļ�ֻ��һ�С�<br>
													3���ļ���������಻����500�С�
												</div>
											</td>
										</tr>
										<tr>
											<td colspan="5" CLASS="green_foot" align="right">
												<button onclick="analysis()">�����ļ�</button>
											</td>
										</tr>
										</td>
										</TR>
										<TR colspan="6" name="data" style="display: none">
											<TH align="center" colspan="6">��������ɵ�����</TH>
											<tr bgcolor="#FFFFFF" name="data" style="display: none" colspan="6">
												<td colspan="6">
												<FORM NAME="frm" METHOD="post">
													<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
														align="center">
														<TR id="download">
															<TD id="file_name">
																<s:property value="file_name" />
															</TD>
															<TD>
																<a href="javascript:downloadDevInfo()">��������ļ�</a>
															</TD>
														</TR>
													</TABLE>
												</FORM>
												</td>
											</tr>
										</TR>
										
									</table>
								</TD>
							</TR>
							<TR>
								<TD HEIGHT=20>&nbsp; <IFRAME ID=childFrm SRC=""
										STYLE="display: none"></IFRAME> <IFRAME ID=childFrm1 SRC=""
										STYLE="display: none"></IFRAME> <IFRAME ID=childFrm2 SRC=""
										STYLE="display: none"></IFRAME>
								</TD>
							</TR>
						</TABLE>