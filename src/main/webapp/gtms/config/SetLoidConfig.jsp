<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String gwType = request.getParameter("gw_type");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���������豸LOID</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<link href="../../css/listview.css" rel="stylesheet" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/CheckForm.js"></SCRIPT>
</head>
<body>
	<s:form action="/gtms/config/setLoidAction!doConfig.action"
		method="post" enctype="multipart/form-data" name="batchexform"
		target="dataForm">
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
											���������豸LOID</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td>
								<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
									align="center">
									<tr>
										<td bgcolor=#999999 colspan="2">
											<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
												<TR>
													<TH colspan="2" align="center">���������豸LOID</TH>
												</TR>
											</TABLE>
										</td>
									</tr>
									<tr>
										<td class="foot" align="center" width="15%">��ѡ���ļ�</td>
										<td width="85%" class="foot"><s:file label="�ϴ�"
												theme="simple" name="upload"></s:file><font color="red">*</font>
											xls��ʽ�ĵ� <input type="hidden" name="fileName" value="" />
											<input type="hidden" name="fileType" value="" /></td>
									</tr>
									<tr>
										<td CLASS="green_foot" align="right">ע������</td>
										<td colspan="" CLASS="green_foot">
											1����Ҫ������ļ���ʽ����Excel��xls��ʽ��txt��ʽ �� <br>
											2���ļ��ĵ�һ��Ϊ�����У����豸���кš�����LOID���� <br> 3���ļ�ֻ��2�С� <br>
											4���ļ�������Ҫ̫�࣬����Ӱ�����ܡ����Ϊ5000�С�
										</td>
									</tr>
									<TR>
										<TD colspan="2" align="right" class="foot">
											<button type="button" id="exeButton" name="exeButton"
												onclick="doExecute();" class=btn>&nbsp;��&nbsp;��
												&nbsp;</button>
										</TD>
									</TR>
								</TABLE>
							</td>
						</tr>
						<tr bgcolor="#FFFFFF">
							<td bgcolor=#999999><iframe id="dataForm" name="dataForm"
									height="0" frameborder="0" scrolling="no" width="100%" src="">
								</iframe></td>
						</tr>
					</table>
				</TD>
			</TR>
		</TABLE>
	</s:form>
</body>
<script type="text/javascript">
	var deviceId = "";
	$(function() {
		gwShare_setGaoji();
	});
	function doExecute() {
		var myFile = $("input[@name='upload']").val();
		if ("" == myFile) {
			alert("��ѡ���ļ�!");
			return false;
		}
		var filet = myFile.split(".");
		if (filet.length < 2) {
			alert("��ѡ���ļ�!");
			return false;
		}
		
		if("xls" == filet[filet.length-1] || "XLS" == filet[filet.length-1]){
			$("input[@name='fileType']").val("xls");
		}else if("txt" == filet[filet.length-1] || "TXT" == filet[filet.length-1]){
			$("input[@name='fileType']").val("txt");
		}
		
		if ("xls" == filet[filet.length - 1]||"XLS" == filet[filet.length-1]||"txt" == filet[filet.length-1] || "TXT" == filet[filet.length-1]) {
			var file = myFile.split("\\");
			var fileName = file[file.length - 1];
			$("input[@name='fileName']").attr("value", fileName);
			$("form[@name='batchexform']").submit();
		} else {
			alert("��֧�ֺ�׺Ϊxls��txt���ļ�");
			return false;
		}
	}
	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ]

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes"

	function dyniframesize() {
		var dyniframe = new Array()
		for (i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block"
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].Document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block"
			}
		}
	}

	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</html>
<%@ include file="../../foot.jsp"%>