<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
	/**
	 * ��jsp��������Ϣ
	 *
	 * @author zhangchy(����) tel��
	 * @version 1.0
	 * @since 2012-9-18 ����02:42:14
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>
<html>
<head>
<title>��������Ԥ���豸</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>

<%
	String gw_type = request.getParameter("gw_type"); 
%>

<SCRIPT LANGUAGE="JavaScript">

var gw_type = '<%=gw_type%>';

$(function(){
	var retResult = '<s:property value="retResult" />';
	var fileName = '<s:property value="fileName" />';
	
	if("" != retResult){
		if(retResult == '-6'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" δ�ܵ��룬���а����ֶΡ����̡�Ϊ�յļ�¼����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-5'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" δ�ܵ��룬���а����ֶΡ��豸�ͺš�Ϊ�յļ�¼����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-3'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" δ�ܵ��룬���а����ֶΡ����ء�Ϊ�յļ�¼����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-2'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" δ�ܵ��룬���а����ֶΡ��豸���С���Ϊ�յļ�¼����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '-1'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" δ�ܵ��룬���а����ֶΡ�OUI��Ϊ�յļ�¼����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '0'){
			alert("����ɹ���");
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" ����ɹ���</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '1'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>��������ļ� "+fileName+" ������Ҫ���밴��ע�������ϴ��ļ����ߵ������ģ��</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '2'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" �д��ڷǷ����صļ�¼����ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '3'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>���ϴ��� "+fileName+" Ϊ�գ���ȷ�ϣ�</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '4'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>�ļ� "+fileName+" û�гɹ��ϴ����������ϴ���</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
		if(retResult == '5'){
			document.getElementById("actLabel").innerHTML = "<font color='red'>���� "+fileName+" ������</font>";
			$("tr[@id='tr00']").css("display","none");
			$("tr[@id='tr01']").css("display","");
		}
	}
});

// ����ģ��
function getExcelTemplate(){
	$("form[@name='importFrm']").attr("action","importDeviceInit!downloadTemplate.action");
	$("form[@name='importFrm']").submit();
}

function doUpload(){
	// ����
	$("tr[@id='tr01']").css("display","none");

	var filePath = document.importFrm.file.value;
	if(filePath.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("����ѡ���ļ���");
		$("input[@name='file']").focus();
		return false;
	}
	
	var filet = "";
	var fileP = "";
	 
	filet = filePath.split(".");
	if(filet.length < 2){
		alert("������ļ���ʽ����ȷ��");
		$("input[@name='file']").focus();
		return false;
	}
	
	if("xls"!=filet[filet.length-1] && "XLS"!=filet[filet.length-1] && 
	   "txt"!=filet[filet.length-1] && "TXT"!=filet[filet.length-1]){
		alert("ֻ֧��Excel2003����TXT�ı�");
		$("input[@name='file']").focus();
		return false;
	}else{
		if("xls" == filet[filet.length-1] || "XLS" == filet[filet.length-1]){
			$("input[@name='fileType']").val("xls");
		}else if("txt" == filet[filet.length-1] || "TXT" == filet[filet.length-1]){
			$("input[@name='fileType']").val("txt");
		}
	}
	
	fileP = filePath.split("\\");
	$("input[@name='fileName']").val(fileP[fileP.length-1]);
	
	document.importFrm.upload.disabled = true;
	$("tr[@id='tr00']").css("display","");
	
	$("form[@name='importFrm']").attr("action", "importDeviceInit!readUploadFile.action?gw_type="+gw_type);
	$("form[@name='importFrm']").submit();
}

</SCRIPT>
</head>
<body>

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
						�豸��Դ����
						</td>
						<td nowrap><img src="../../images/attention_2.gif" width="15"
							height="12"> &nbsp;Ϊ�˹ܿ��������������豸���ڴ˽����ȵ�����Ҫ�ɹ����豸��Ȼ���豸�Զ��ϱ�ʱ��û���ڴ˵���ģ�һ�ɲ���������</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td>
				<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
					align="center">
					<TR>
						<TD bgcolor=#999999>
						<table border=0 cellspacing=1 cellpadding=2 width="100%">
							<TR>
								<TH colspan="4" align="center">������Ҫ�ɹ����豸</TH>
							</TR>
							<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
								<td width="10%" align="right">�ύ�ļ���</td>
								<td colspan="3">

								<form name="importFrm" action="importDeviceInit!readUploadFile.action"
									method="POST" enctype="multipart/form-data">
									<input type="hidden" name="fileType" value="" >
									<input type="hidden" name="fileName" value="" >
									<input name="rowNum" type="hidden" value="20000"><!-- ֻ����Excel��ǰ20000�У������������� -->
								<table>
									<tr>
										<td>
											<input type="file" size="60" name="file" />
											<input type="button" value=" �� �� " class=jianbian name="upload" onclick="doUpload()" />
											&nbsp;&nbsp;
											<a href="javascript:void(0);" onClick="getExcelTemplate();"><font color='red'>�������ģ��</font></a>
											&nbsp;&nbsp;
										</td>
									</tr>
								</table>
								</form>
                              </td>
							</tr>
                            <tr bgcolor="#FFFFFF">
								<td align="right">ע�����
								<td colspan="3"><font color="#7f9db9">
								1����Ҫ������ļ���ʽΪExcel2003��TXT�ı��ļ�����xls��txt��ʽ �� <br> 
								2���ļ��ĵ�һ��Ϊ�����У���OUI���豸���кš����ء�����ʱ��(YYYY-MM-DD)���豸MAC�����̡��豸�ͺš�����ʱ�䡢�豸���ţ�����</font><font color='red'>OUI���豸���кţ����̣��豸�ͺ�Ϊ������������ɲ���</font><font color="#7f9db9">�� <br>
								3��Excel2003��ʽ��ÿ�����֣�TXT�ı���ʽ�м��ԡ�|������(������˫����)�������ֶηֱ�Ϊ��OUI|�豸���к�|����|����ʱ��(YYYY-MM-DD)|�豸MAC|����|�豸�ͺ�|����ʱ��(YYYY-MM-DD)|�豸���� <br>
								4���ļ�ֻ�о��У�����OUI���豸���кţ����أ����̣��豸�ͺ�Ϊ�������ʱ�䣬�豸MAC������ʱ�䣬�豸���ſ���Ϊ�գ����Ϊ�գ���ô�ָ�����|��������Ҫ�ģ��磺1880F5|373001880F5A00272|ʡ����|||||�� <br>
								5���ļ���������಻����2����,�糬��2���У�ֻ����ǰ2�������ݡ� <br>
								</font></td>
							</tr>
							<!-- ����һ�����ص�ռλ�������ֽ�����һ�� -->
							<tr bgcolor="#FFFFFF">
								<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
							</tr>
							<tr id="tr00" bgcolor="#FFFFFF" style="display: none" >
								<td colspan="4" >�����ϴ����������Ե�....</td>
							</tr>
							<tr id="tr01" style="display: none" bgcolor="#FFFFFF">
								<td align="right">��������</td>
								<td id="td02" colspan="3" height="20"><SPAN id="actLabel"></SPAN></td>
							</tr>
						</table>
						</td>
					</TR>
				</table>
				</td>
				<tr>
		</table>
		</TD>
	</TR>
	<tr>
		<td height=20></td>
	</tr>
</TABLE>

</body>
<%@ include file="../../foot.jsp"%>
</html>