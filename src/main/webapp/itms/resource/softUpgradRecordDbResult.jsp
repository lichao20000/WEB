<%@ include file="/timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<lk:res />
<SCRIPT LANGUAGE="JavaScript">
	var result = "<s:property value='ajax' />";

	// �����
	var dbResult = result.split("|")[0];
	// �ļ��ϴ����
	var fileResult = result.split("|")[1];

	var str = "";
	if (dbResult == "3") {
		str += "���빫��ʧ��! ��������ʧ��!"
	}
	if (dbResult == "1") {
		str += "���ɹ���";
	} else {
		str += "���ʧ�ܣ�";
	}

	if (fileResult == "-1") {
		//����Ҫ�ϴ��ļ�
	} else if (fileResult == "1") {
		str += "�ļ��ϴ��ɹ���";
	} else {
		str += "�ļ��ϴ���ʧ�ܣ�";
	}
	alert(str);
</SCRIPT>
</html>