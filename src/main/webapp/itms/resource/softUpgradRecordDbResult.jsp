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
	if (dbResult == "3") {
		str += "插入公告失败! 新增操作失败!"
	}
	if (dbResult == "1") {
		str += "入库成功！";
	} else {
		str += "入库失败！";
	}

	if (fileResult == "-1") {
		//不需要上传文件
	} else if (fileResult == "1") {
		str += "文件上传成功！";
	} else {
		str += "文件上传成失败！";
	}
	alert(str);
</SCRIPT>
</html>