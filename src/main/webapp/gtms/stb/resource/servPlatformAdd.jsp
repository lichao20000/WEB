<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ҵ��ƽ̨���͹���</title>
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
		 alert("����дҵ��ƽ̨����id");
		 return false;
	} 
	 if(isNaN(platformid)){
		 alert("ҵ��ƽ̨����idֻ��Ϊ����!");
		 return false;
	 }
	 var platformname = $("input[@name='platformname']").val();
	 var remark = $("input[@name='remark']").val(); 
	 if(platformname.trim()==""||platformname==null)
		{
			 alert("����дҵ��ƽ̨��������");
			 return false;
		}
	 var url="<s:url value='/gtms/stb/resource/stbservplatform!queryplatformid.action'/>";
	 $.post(url,{
		 platformid:platformid
		},function(ajax){
			if(ajax==-1)
			{
				alert("id�Ѵ���!");
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
							alert("��ӳɹ�!");
						}else
						{
							alert("���ʧ��!");
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
				<TH align="center" >ҵ��ƽ̨��������</TH>
			</tr>
			<tr>
				<td>
					<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
						<TR>
							<TD bgcolor=#999999>
								<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="30%" class="column">ҵ��ƽ̨����ID</TD>
										<TD colspan="3"><input type="text" name="platformid"
											id="platformid" class="bk" value=""> <font
											color="red">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="30%" class="column">ҵ��ƽ̨��������</TD>
										<TD  colspan="3"><input type="text" name="platformname"
											id="platformname" class="bk" value=""> <font
											color="red">*</font></TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD align="right" width="30%" class="column">��ע</TD>
										<TD  colspan="3"><input type="text" name="remark"
											id="remark" class="bk" value=""> </TD>
									</TR>
									<TR bgcolor="#FFFFFF">
										<TD  align="right" class="green_foot" colspan="4">
											<button onclick="save()" class="jianbian">����</button>
											<button onclick="javascript:window.close();" class="jianbian" >ȡ��</button>
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