<%--
Author		: Alex.Yan (yanhj@lianchuang.com)
Date		: 2007-11-28
Desc		: TR069: devicetype_list, add;
			  SNMP:	 Ddevicetype list.
--%>

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
if(dbResult == "1")
{
	str += "�޸ĳɹ���";
}
else
{
	str += "�޸�ʧ�ܣ�";
}

if(fileResult == "-1")
{
	//����Ҫ�ϴ��ļ�
}
else if(fileResult == "1")
{
	str += "�ļ��ϴ��ɹ���";
}
else
{
	str += "�ļ��ϴ���ʧ�ܣ�";
}
alert(str);

//��ѯҳ�����²�ѯһ��
//alert(window.parent.opener.parent.queryDevice());// ģ̬������window.opener()������
//window.parent.close();

window.parent.returnValue = "1";
window.parent.close();

</SCRIPT>
</html>