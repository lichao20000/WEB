<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<%@ include file="/toolbar.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>�ն˺�����һ���嵥ͳ��</title>
<link href="<s:url value="/css/css_writeOff.css"/>" rel="stylesheet"
	type="text/css"></link>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript">

	function queryDevVerification() {
			var cityId = $("select[@name='cityId']").val();
		document.getElementById("queryButton").disabled = true;
		var frm = document.getElementById("frm");
		frm.action = "<s:url value='/itms/resource/devVerificationDisList!queryDevVerification.action'/>";
		frm.submit();
	}
	
	function exportQueryDevVerification() {
		var form = document.getElementById("frm");
		form.action = "<s:url value='/itms/resource/devVerificationDisList!parseExcel.action'/>";
		form.submit();
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

	$(window).resize(function() {
		dyniframesize();
	});
</script>
</head>

<body>
	<div class="it_main">
		<h3 class="main-ttl">�ն˺�����һ���嵥ͳ��</h3>
		<div class="search-container">
			<form name="frm" id="frm" target="dataForm">
				<div class="search-item">
					<div class="search-label-blue">���أ�</div>
					<div class="search-con">
						<s:select list="cityList" name="cityId" listKey="city_id"
							listValue="city_name" value="1234" cssClass="select"
							style="width:100px;"></s:select>
					</div>
				</div>
				<div class="search-item">
					<div class="search-label-blue">�������ڣ�</div>
					<div class="search-con">
						<input type="text" name="starttime" readonly
							value="<s:property value='starttime'/>"/> 
							<img name="shortDateimg"
							onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
							src="../../images/dateButton.png" width="15" height="12"
							border="0" alt="ѡ��" /> &nbsp;-&nbsp; <input type="text"
							name="endtime" readonly value="<s:property value='endtime'/>"/>
								<img name="shortDateimg"
								onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
								src="../../images/dateButton.png" width="15" height="12"
								border="0" alt="ѡ��" />
					</div>
				</div>
				<div class="search-item">
					<div class="search-label-blue">������ʽ��</div>
					<div class="search-con">
						<select name = 'useType' class="select" style="width: 85px;">
							<option value="-1" selected>ȫ��</option>
							<option value="1">�Զ�����</option>
							<option value="2">�˹�����</option>
						</select>
					</div>
				</div>
				<div>
					<div class="search-item">
						<div class="search-label-blue">�Ƿ�һ�£�</div>
						<div class="search-con">
							<select name = 'isMatch' class="select" style="width: 65px;">
								<option value="-1" selected>ȫ��</option>
								<option value="1">��</option>
								<option value="0" >��</option>
							</select>
						</div>
						<!--��ť�������һ��search-item�У���֤��ť���ᵥ������-->
						<div class="searchbtn-box">
							<a href="javascript:void(0);" class="search-btn" onclick="queryDevVerification()" id="queryButton"> �� ѯ </a> 
							<a href="javascript:void(0);" class="search-btn" onclick="exportQueryDevVerification()"> �� �� </a>
						</div>
					</div>
				</div>
			</form>
			<div class="content">
			<iframe id="dataForm" name="dataForm" height="0" frameborder="0"
				scrolling="no" width="100%" src=""></iframe>
		</div>
		</div>
	</div>
</body>
<%@ include file="/foot.jsp"%>
</html>
