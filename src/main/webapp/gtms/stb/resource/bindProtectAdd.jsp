<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../../Js/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript">
<%
	String opr = (String)request.getParameter("opr");
%>
function doQuery(){
    var mac = $.trim($("input[@name='mac']").val());           
    var userName = $.trim($("input[@name='userName']").val()); 
    var remark = $.trim($("input[@name='remark']").val());
    var opr = '<%=opr%>' ;
    
  	//2����������һ��
	if(mac.length == 0 || userName.length == 0)
	{
		alert("�豸MAC,ҵ���˺ž�����Ϊ��");
		document.frm.mac.focus();
		return false;
	}
	
	var re = /^[0-9A-F][0-9A-F]*$/;
	if(!re.test(mac))
	{
		alert("mac���Ϸ�");
		return false;
	}
	
    var url = '<s:url value='/gtms/stb/resource/stbBindProtect!add.action'/>'; 
	$.post(url,{
		mac:mac,
		userName:userName,
		opr:opr,
		remark:encodeURI(remark)
	},function(ajax){
		if(1 == ajax)
		{
			alert("��Ӽ�¼�ɹ�");
		}
		else
		{
			alert("ҵ���˺Ż������MAC�Ѿ����ڼ�¼����˶Ժ��ٽ���¼�룡");
		}
		window.location.reload();
	});
}



</script>

<br>
<TABLE>
	<tr>
		<td>
			<table class="green_gargtd">
				<tr>
					<th>
						�󶨱�����ϵ����
					</th>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table class="querytable">
					<tr>
						<th colspan=4>
							�󶨱�����ϵ����
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" width='15%' align="right">ҵ���˺ţ�</td>
						<td width='35%' align="left">
						<input name="userName" type="text" class='bk' value="">
						<font color="red">*</font>
						</td>
						<td class="column" width='5%' align="right">�豸MAC��</td>
						<td width='45%' align="left">
						<input name="mac" type="text" class='bk' value=""> 
						<font color="red">* ֻ��A-F��ĸ�����������,��ʽ��112233AABBCC</font>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class="column" width='15%' align="right">��ע��</td>
						<td width='35%' align="left">
						<input name="remark" type="text"  class='bk' value="">
						</td>
						<td class="column" width='15%' align="right"></td>
						<td width='35%' align="left">
							<input name="acc_oid" type="hidden" class='bk' value=""> 
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=foot colspan=4 align=right>
							<button onclick="doQuery()">
								&nbsp;�ύ&nbsp;
							</button>
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display: none">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
				����ͳ�ƣ����Ե�....
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>

