<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>业务平台类型管理</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
	<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<script type="text/javascript" src="/Js/jsDate/WdatePicker.js"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">

function save()
{
	var platformid = $("input[@name='platformid']").val().trim();
	 if(platformid.trim()==""||platformid==null)
	{
		 alert("请填写业务平台类型id");
		 return false;
	} 
	 if(isNaN(platformid)){
		 alert("业务平台类型id只能为数字!");
		 return false;
	 }
	 var platformname = $("input[@name='platformname']").val();
	 var remark = $("input[@name='remark']").val(); 
	 if(platformname.trim()==""||platformname==null)
		{
			 alert("请填写业务平台类型名称");
			 return false;
		}
	 var url="<s:url value='/gtms/stb/resource/stbservplatform!queryplatformid.action'/>";
	 $.post(url,{
		 platformid:platformid
		},function(ajax){
			if(ajax==-1)
			{
				alert("id已存在!");
			}else
				{
				 var url="<s:url value='/gtms/stb/resource/stbservplatform!addservPlatform.action'/>";
				 $.post(url,{
					 platformid:platformid,
					 platformname:platformname,
					 remark:remark
					},function(ajax){
						if(ajax==1)
						{
							alert("添加成功!");
						}else
						{
							alert("添加失败!");
						}
					});
				 
				}
		});
}

</SCRIPT>
<style>
span {
	position: static;
	border: 0;
}
</style>
</head>
<body>
	<form NAME="frm"  action="" target="dataForm">
		<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
			<tr>
				<TH align="center" >业务平台类型新增</TH>
			</tr>
			<tr>
				<td>
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
						<TR>
							<TD bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="30%" class="column">业务平台类型ID</TD>
										<TD colspan="3"><input type="text" name="platformid"
											id="platformid" class="bk" value=""> <font
											color="red">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="30%" class="column">业务平台类型名称</TD>
										<TD  colspan="3"><input type="text" name="platformname"
											id="platformname" class="bk" value=""> <font
											color="red">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="30%" class="column">备注</TD>
										<TD  colspan="3"><input type="text" name="remark"
											id="remark" class="bk" value=""> </TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD  align="right" class="green_foot" colspan="4">
											<button onclick="save()" class="jianbian">保存</button>
											<button onclick="javascript:window.close();" class="jianbian" >取消</button>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>