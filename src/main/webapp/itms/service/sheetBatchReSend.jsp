<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<%--
	/**
	 * ������¼����
	 *
	 * @author jianglp(75508)
	 * @version 1.0
	 * @since 2016-11-30
	 * @copyright Ailk NBS-Network Mgt. RD Dept.
	 * 
	 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<lk:res />
<style>
</style>
<title>������¼����</title>
<link href="<s:url value="/css/css_green.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript">
	function reableSubBtn(){
		$("input[@id='reSendButton']").attr("disabled",false);
	}
	function reSendSheets() {
		var width = 800;
		var height = 450;
		var url = "<s:url value="/itms/service/failedSheet!reSendSheet.action"/>";
		//url = url + "&gwShare_fileName=" + gwShare_fileName;
		var gwShare_fileName = $("input[@name='gwShare_fileName']").val();
		if ("" == gwShare_fileName) {
			alert("�����ϴ��ļ���");
			return;
		}
		url = url + "?batsheet_filename=" + gwShare_fileName;
		$("div[@id='selectResInfo']").html(
				"<font size=2><strong>���ύ����̨���ڲ�¼...</strong></font>");
		$("input[@id='reSendButton']").attr("disabled",true);
		
		//$("div[@id='reSendTab']").html("disable",true);
		$.post(url, {}, function(ajax) {
			var _rs = new Array();
			_rs = ajax.split(",");
			var showCont="";
			if (""== _rs[1]) {
				showCont = "<font size=2><strong>�ύ��¼��������:" + _rs[0] + ",�ɹ���:"
						+ _rs[4]+ ",ʧ����:"
						+ _rs[3] + ",��ʱ��:"+_rs[2]+"</strong></font>";
			} else {
				showCont = "<font size=2><strong>" + _rs[1]
						+ "</strong></font>";
			}
			$("div[@id='selectResInfo']").html(showCont);
		});
		return;

	}
	function do_query() {
		setTimeout("gwFailedSheets()", 2000);
	}
</script>
</head>
<body>
	<TABLE id="reSendTab" border=0 cellspacing=0 cellpadding=0 width="100%">
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
										����������¼</td>
									<td nowrap><img
										src="<s:url value='/images/attention_2.gif'/>" width="15"
										height="12"></td>
								</tr>
							</table>
						</td>
					</tr>
					<TR bgcolor="#FFFFFF">
						<td colspan="4">
							<form name="gwShare_selectForm"
								action="<s:url value="/service/failedSheet!queryFailedSheet.action"/>"
								target="dataForm">
								<TABLE border=0 cellspacing=0 cellpadding=0 width="100%"
									align="center">
									<tr>
										<td bgcolor=#999999>
											<table border=0 cellspacing=1 cellpadding=2 width="100%"
												align="center">
												<tr>
													<th colspan="4">�� �� �� �� �� ¼</th>
												</tr>
												<tr id="gwShare_tr31" bgcolor="#FFFFFF">
													<td align="right" width="15%">�ύ�ļ�</td>
													<td colspan="3" width="85%">
														<div id="">
															<iframe name="batsheet_loadForm" FRAMEBORDER=0
																SCROLLING=NO
																src="<s:url value="/gwms/share/FileUpload.jsp"/>"
																height="20" width="100%"> </iframe>
															<input type="hidden" name="gwShare_fileName" value="" onpropertychange="reableSubBtn();"/>
														</div>
													</td>
												</tr>
												<tr id="gwShare_tr32">
													<td CLASS="green_foot" align="right">ע������</td>
													<td colspan="2" CLASS="green_foot">
														1����Ҫ������ļ���ʽ����Excel���ı��ļ�����xls��txt��ʽ �� <br>
														2���ļ��ĵ�һ��Ϊ�����У������貹¼�������� <br> 3���ļ�ֻ��һ�С� <br>
														4��ÿ�а����������ݣ����磺14|||3|||20120418141800|||2587791020000019|||0001|||051487373113LINKAGE��
														<br> 5���ļ�������Ҫ����2000�У�����Ӱ�����ܡ�<br>
													</td>
													<%-- <td>
								<img src="<s:url value='/images/attention_2.gif'/>">
							</td> --%>
												</tr>
												<tr bgcolor="#FFFFFF">
													<td colspan="4" align="right" class="green_foot"
														width="100%"><input type="button"
														onclick="javascript:reSendSheets()" class=jianbian
														id="reSendButton" value=" �� �� " /></td>
												</tr>
											</table>
										</td>
									</tr>
								</TABLE>
							</form>
						</td>
					</TR>
					<tr>
						<td>
							<FORM NAME="frm" METHOD="post"
								ACTION="<s:url value="/gwms/resource/software!batchUp.action"/>"
								onsubmit="return CheckForm()">
								<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
									align="center">
									<TR>
										<TD bgcolor=#999999>
											<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
												<tr bgcolor="#FFFFFF">
													<td colspan="4">
														<div id="selectResInfo">��δ�ύ��</div>
													</td>
												</tr>
											</TABLE>
										</TD>
									</TR>
								</TABLE>
							</FORM>
						</td>
					</tr>
				</table>
			</TD>
		</TR>
	</TABLE>
</body>
</html>