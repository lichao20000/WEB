<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=gbk"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%--
	/**
	 * ��jsp��������Ϣ
	 *
	 * @author wuchao(����) tel��
	 * @version 1.0
	 * @since 2011-9-21 ����02:42:14
	 * 
	 * <br>��Ȩ���Ͼ������Ƽ� ���ܿƼ���
	 * 
	 */
 --%>
<html>
<head>
<title>��ͼ���������·�</title>
<link href="../../css/css_green.css" rel="stylesheet" type="text/css">
<%@ include file="../../head.jsp"%>
<%@ include file="../../toolbar.jsp"%>
<SCRIPT LANGUAGE="JavaScript" SRC="../../Js/jquery.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript">
$(function(){
	var isfirst = "<s:property value="isFword" />";
	if(isfirst=='1'){
		$("tr[@name='tr_result']").show();
	}else{
		$("tr[@name='tr_result']").hide();
	}
});





function check(){

	
	var task_name =document.all("task_name").value;
	var map_id=	$("select[@name='map_id']").val();
	 if(map_id=="-1")
	   {
	     alert("��ͼ���Ʋ���Ϊ��");
	     return false;
	   }
	   
	
	   if(task_name=="-1" || task_name=="")
	   {
	     alert("����д�������ƣ�");
	     return false;
	   }
	   return true;
	   
}

function forward(){
	if(!check()){
		return
	}
	var map_id=	$("select[@name='map_id']").val();
	var device_idNormalStr=document.getElementById("device_idNormalStr").value;
	var name=document.getElementById("task_name").value;
	var task_name=encodeURIComponent(name);
	var url = "<s:url value="/gwms/resource/importLoidACT!forward.action"/>";
	$.post(url,{
			map_id:map_id,
			device_idNormalStr:device_idNormalStr,
			task_name:task_name
	},function(ajax){
	  
		alert("�·��ɹ�");	
		
		});
$("input[@name='task_name']").val("");

}

function doUpload(){
	
	var username=document.importFrm.file.value;
	if(username.replace(/(^\s*)|(\s*$)/g, "") == ""){
		alert("����ѡ���ļ���");
		return false;
	}
	//��ť���µ�ʱ��Ѱ�ť�û� add by zhangcong 2011-06-07
	document.importFrm.upload.disabled = true;
	//parent.idWait.innerHTML = "�����ϴ�����......";
	//idWait.innerHTML = "�����ϴ�����......";
	document.importFrm.submit();
	
}

</SCRIPT>
</head>
<body>

<!-- <form name="frm" action="itvConfig!doStatery.action" method="post"
	onsubmit="return checkForm();"> -->
<!--  	<input type="text" size=30 value="<s:property value="normalLoid"/>"></input>-->
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
						��ͼ���������·�</td>
						<td nowrap><img src="../../images/attention_2.gif" width="15"
							height="12"> &nbsp;����LOID�����·�</td>
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
								<TH colspan="4" align="center">��ͼ���������·�</TH>
							</TR>
							<tr id="importUserQuery" bgcolor="#FFFFFF" style="display: ">
								<td align="right">�ύ�ļ���</td>
								<td colspan="3">

								<form name="importFrm"
									action="importLoidACT!getDeviceByImportLoid.action"
									method="POST" enctype="multipart/form-data">
								<table>
									<tr>
										<td><input type="file" size="60" name="file" /> <!-- <input type="button" value="˵ ��" name="filedesc" onclick="parent.showDesc()" /> -->
										<input type="button" value=" �� ��  " class=jianbian
											name="upload" onclick="doUpload()" /></td>
									</tr>
								</table>
								</form>
                              </td>
							</tr>
							<%
							if ("nx_dx".equals(LipossGlobals.getLipossProperty("InstArea.ShortName")))
							{
							%>
                            <tr id="importUserQuery2" style="display: " bgcolor="#FFFFFF">
								<td align="right">ע�����
								<td colspan="3"><font color="#7f9db9">
								1����Ҫ������ļ���ʽΪExcel�� <br>
								2���ļ��ĵ�һ�е�һ��ΪLoid�� <br>
								3���ļ�������������100��,�糬��100�У�ֻ����ǰ100�����ݡ� <br>
								4����ȷ���ļ����豸�·�����ͼ������ͬ </font></td>
							</tr>
							<%
							}
							else
							{
							%>
							<tr id="importUserQuery2" style="display: " bgcolor="#FFFFFF">
								<td align="right">ע�����
								<td colspan="3"><font color="#7f9db9">
								1����Ҫ������ļ���ʽΪExcel�� <br>
								2���ļ��ĵ�һ��Ϊ�����У���Loid�� <br>
								3���ļ�ֻ��һ�С� <br>
								4���ļ�������������100��,�糬��100�У�ֻ����ǰ100�����ݡ� <br>
								5����ȷ���ļ����豸�·�����ͼ������ͬ </font></td>
							</tr>
							<%
							}
							%>
							<!-- ���һ�����ص�ռλ�������ֽ�����һ�� -->
							<tr id="space" style="display: " bgcolor="#FFFFFF">
								<td colspan="4" align="right" CLASS="green_foot">&nbsp;</td>
							</tr>
							<tr style="display: " bgcolor="#FFFFFF">
								<td colspan="4" height="20">&nbsp;</td>
							</tr>
							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<th colspan="4" align="center" id="1">�ļ��������</th>
							</tr>

							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">�����Loid����</td>
                                <td colspan="3"><s:property value="allLoid" /></td>
							</tr>
                           	<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">�Ѳ�ѯ�����豸����</td>
								<!--  		<td colspan="3"><input type="text" size=50
									value="<s:property value="normalLoid"/>"></input></td>-->
								<td colspan="3"><s:property value="normalLoid" /></td>
							</tr>
                         	<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">δ�鵽��LOID</td>
								<td colspan="3"><s:property value="deviceExceptionStr" /></td>
							</tr>
                            <tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">��ͼģ������</td>

								<td><s:select list="digitMapList" name="map_id"
									headerKey="-1" headerValue="��ѡ����ͼ����" listKey="map_id"
									listValue="map_name" value="map_id" cssClass="bk"></s:select></td>
							</tr>

							<tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td align="right">��������</td>
								<td colspan="3"><input id="task_name" type="text"
									name="task_name" size=50></input> <font color="#FF0000">*</font>
								�����·�ʱ����Ҫ��</td>

							</tr>
                           <tr bgcolor="#FFFFFF" name="tr_result" style="display: ">
								<td colspan="4" align="right" CLASS="green_foot"><input
									type="button" class=jianbian name=queryBtn value=" �����·� "
									onclick="forward();"> &nbsp;</td>
							</tr>
                           	<tr bgcolor="#FFFFFF" style="display: none">
								<td align="right">�豸id����</td>
								<td colspan="3"><input id="device_idNormalStr" type="text"
									name="device_idNormalStr" size=50
									value="<s:property value="device_idNormalStr"/>"></input></td>
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
<!--  </form>-->
</body>
<%@ include file="../../foot.jsp"%>
</html>