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

// 入库结果
var dbResult = result.split("|")[0];
// 文件上传结果
var fileResult = result.split("|")[1];

var str = "";
if(dbResult == "1")
{
	str += "修改成功！";
}
else
{
	str += "修改失败！";
}

if(fileResult == "-1")
{
	//不需要上传文件
}
else if(fileResult == "1")
{
	str += "文件上传成功！";
}
else
{
	str += "文件上传成失败！";
}
alert(str);

//查询页面重新查询一次
//alert(window.parent.opener.parent.queryDevice());// 模态窗口中window.opener()不好用
//window.parent.close();

window.parent.returnValue = "1";
window.parent.close();

</SCRIPT>
</html>