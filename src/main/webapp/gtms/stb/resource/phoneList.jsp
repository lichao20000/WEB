<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head lang="en">
<meta charset="UTF-8">
<!-- <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE9" /> -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes">
<title>详细页面</title> <link rel="stylesheet"
	href="../../../iconfont/iconfont.css">
<link href="../../../css1/detail.css" type="text/css" rel="stylesheet">
<script src="../../../Js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="<s:url value='/Js/jquery.js'/>"></script>
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
</head>
<body>
<div class="box">
    <div class="contentAll">
        <div class="title">用户画像数据查询</div>
        <div class="detail user">
            <table width="100%">
                <thead>
                <td colspan="6">手机号码</td>
                </thead>
                <s:if test="phonenumber!=null&&phonenumber.size()>0">
								<s:iterator value="phonenumber">
                <tr style="text-align: center;">
                    <td><s:property value="phone_number" /></td>
                </tr>
                	</s:iterator>
				</s:if>
							<s:else>
							 <tr>
                    <td>没有查询到电话号码</td>
                		</tr>
							</s:else>
            </table>
            <div class="toolbar">
                <a onclick="javascript:window.close();"><button type="button" class="btn-get mt40">返回</button></a>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
function test()
{
	$("#ftpenable").val("1");	
}
function test1()
{
	$("#ftpenable").val("0");	
}
function config(netaccount)
{
	var url = "<s:url value='/gtms/stb/resource/UserImage!phonenumber.action'/>?netaccount="
		+ netaccount ;
	window.open(url, "","left=80,top=80,width=800,height=400,resizable=yes,scrollbars=yes");
	}
function query()
{
var ftpenable = $("#ftpenable").val();
if(ftpenable==""||ftpenable==null)
{
	alert("请选择查询方式");
 	return false;
}
var UserName = $("#UserName").val();
if(UserName==""||UserName==null)
 {
 alert("请填写LOID或者宽带主账号");
 return false;
 }
 if(ftpenable==1&&UserName.length<6)
	 {
	 alert("设备序列号不可以小于6位!");
	 return false;
	 }
document.frm.submit();
}
	$(function () {
    setWidth();
    function setWidth(){
        var size = $("#row .shebei").length;
        var width = size*225;
        $("#row").width(width);
    }
});
	$(".queryType li").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
    });
	//文档加载好之后，取第一个给他添加start属性。取最后一个给他附加end属性
	$(document).ready(function(){
		if($("#row").children("div").length>1)
		{
		$("#row").children("div").eq(0).addClass("start");
		$("#row").children("div").eq($("#row").children("div").length-1).addClass("end");
		}else
			{
				$("#row").children("div").eq(0).addClass("only");
			}
	})
</script>
</html>
