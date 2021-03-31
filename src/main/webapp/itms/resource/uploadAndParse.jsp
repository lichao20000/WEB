<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ taglib prefix="ms" uri="/ailk-itms-web-tags"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<title>���豸���������ϴ�</title>
<link href="<s:url value="/css/uploadAndParse.css"/>" rel="stylesheet"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT type="text/javascript">
	//���ie�汾�ϵ�ʱ, onclick����input fileʱ,�ܾ�����(IE��һ�ְ�ȫ����)
	function add() 
	{
		document.getElementById("filePath").style.posLeft=event.x-document.getElementById("filePath").offsetWidth/2;
	}

	function change() {
		var filePath = document.getElementById("filePath").value;
		document.getElementById("filePathshow").value = filePath;
		document.getElementById("uploadButton").disabled = false;
		document.getElementById("it_stips").style.display="none";
		document.getElementById("it_ok").style.display="none";
	}

	function upload() {
		var filePath = document.getElementById("filePath").value;
		if(""==filePath){
			alert("����ѡ���ļ���");
			return;
		}
		if(filePath.length<4 || ("txt"!= filePath.substr(filePath.length-3,3) && "xls"!= filePath.substr(filePath.length-3,3))){
			document.getElementById("it_stips").style.display="";
			document.getElementById("it_ok").style.display="none";
			return false;
		}
		var fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
		if("����ģ��.txt"==fileName || "����ģ��.xls"==fileName){
			alert("���޸��ļ���,�����԰���ģ��Ϊ��!");
			return;
		}
		document.getElementById("fileName").value =fileName;
		document.getElementById("it_stips").style.display="none";
		var url = "<s:url value='/itms/resource/devVendorDataUpload!uploadAndParse.action'/>";
		document.mainForm.action = url;
		document.mainForm.submit();
		parent.document.getElementById("uploadButton").disabled = true;
	}

	function cancel() {
		document.getElementById("mainForm").reset();
		document.getElementById("it_stips").style.display="none";
		document.getElementById("it_ok").style.display="none";
		document.getElementById("uploadButton").disabled = false;
	}

	function downloadcase() {
		var url = "<s:url value='/itms/resource/devVendorDataUpload!download.action'/>";
		document.getElementById("mainForm").action = url;
		document.getElementById("mainForm").submit();
		document.getElementById("mainForm").reset();
	}

	//** iframe�Զ���Ӧҳ�� **//
	//������ϣ������ҳ��߶��Զ������߶ȵ�iframe�����Ƶ��б�
	//�ö��Ű�ÿ��iframe��ID�ָ�. ����: ["myframe1", "myframe2"]������ֻ��һ�����壬���ö��š�
	//����iframe��ID
	var iframeids = [ "dataForm" ];

	//����û����������֧��iframe�Ƿ�iframe���� yes ��ʾ���أ�no��ʾ������
	var iframehide = "yes";

	function dyniframesize() {
		var dyniframe = new Array();
		for (var i = 0; i < iframeids.length; i++) {
			if (document.getElementById) {
				//�Զ�����iframe�߶�
				dyniframe[dyniframe.length] = document
						.getElementById(iframeids[i]);
				if (dyniframe[i] && !window.opera) {
					dyniframe[i].style.display = "block";
					//����û����������NetScape
					if (dyniframe[i].contentDocument
							&& dyniframe[i].contentDocument.body.offsetHeight)
						dyniframe[i].height = dyniframe[i].contentDocument.body.offsetHeight;
					//����û����������IE
					else if (dyniframe[i].Document
							&& dyniframe[i].Document.body.scrollHeight)
						dyniframe[i].height = dyniframe[i].document.body.scrollHeight;
				}
			}
			//�����趨�Ĳ���������֧��iframe�����������ʾ����
			if ((document.all || document.getElementById) && iframehide == "no") {
				var tempobj = document.all ? document.all[iframeids[i]]
						: document.getElementById(iframeids[i])
				tempobj.style.display = "block";
			}
		}
	}
	$(function() {
		dyniframesize();
	});

	$(window).resize(function() {
		dyniframesize();
	});
</SCRIPT>
</head>

<body>
	<div class="it_main">
		<h1 class="it_webtt">���豸���������ϴ�</h1>
		<FORM NAME="mainForm" id="mainForm" METHOD="post" ACTION=""
			target="dataForm" enctype="multipart/form-data">
			<div class="file_search">
				<div class="input_line">
					<input type="file" name="filePath" id="filePath" onchange=change() style="position:absolute;filter:alpha(opacity=0);width:40px; height:40px" hidefocus />
					 <input type="hidden" name="fileName" id="fileName" value="" />
					 <input id="filePathshow" type="text" class="it_ipt file_ipt" disabled="disabled" /><input type="button" class="file_btn" value="����ļ�" onmouseover="add();" />
				</div>
				<div class="notice">ע������ļ�������2000M����ʽ����xls��txt��ʽ&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
				<div id="download" >
					<input type="radio" width="22%" name="caseDownload" value="0" onclick="downloadcase()">xls�������� </input>
					<input type="radio" width="22%" name="caseDownload" value="1" onclick="downloadcase()">txt��������</input>
				</div>
				<div class="it_stips" id ="it_stips" style="display: none">�ļ���ʽ����</div>
				<div class="it_stips it_ok" id ="it_ok" style="display: none">�ϴ��ɹ���</div>
				<div class="foot_btn">
					<a href="javascript:void(0);" id="uploadButton" onclick="upload()" class="putin">�ϴ�</a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="javascript:void(0);" onclick="cancel()" class="cancel">ȡ��</a>
				</div>
			</div>
		</FORM>
	</div>
	<div>
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
