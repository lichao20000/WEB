<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@ include file="../timelater.jsp" %>
<html>
<head>
<style type="text/css">
	.querytable{
		border-top: solid 1px #999;
		border-right: solid 1px #999;
	}
	.querytable th, .querytable td{
	 	border-bottom: solid 1px #999;
	 	border-left: solid 1px #999;
	}
</style>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript">

//����
function save(){
  	// У��
  	if(!checkForm()){
  		return;
  	}
  	$.ajax({
  		type:"Post",
  		url:"deviceModelInfo!StbVendorInfo.action",
  		data: $("#frm").serialize(),
  		dataType:'json',
  		success:function(data){
  			if(data.code == 1){
  				$("#add-div").hide();
  				queryFrm.submit();
  			}else{
  				alert(data.message);
  			}
  		},
  		error:function(e){
  			alert("�������쳣");
  			console.info("e",e);
  		}
   });
}
  
function add(){
  document.getElementById('add-div').style.display = "";
	document.frm.reset();
	document.frm.actionType.value="add";
	actLabel.innerHTML = '���';
	VendorLabel.innerHTML="";
}
  function doEdit(vendorId,vendor_name,vendor_add,telephone,staff_id,remark){
    document.getElementById('add-div').style.display = "";
    frm.vendorId.value = vendorId;
    frm.vendor_name.value = vendor_name;
    frm.vendor_add.value = vendor_add;
    frm.telephone.value = telephone;
    frm.staff_id.value = staff_id;
    frm.remark.value = remark;
    actLabel.innerHTML = '�༭';
    frm.actionType.value = "update";
  }

  function delDel(vendorId){
    if(confirm("���Ҫɾ���ó�����\n��������ɾ���Ĳ��ָܻ�������")){
        frm.vendorId.value = vendorId;
        frm.actionType.value = "delete";
		$.ajax({
	  		type:"Post",
	  		url:"deviceModelInfo!StbVendorInfo.action",
	  		data: $("#frm").serialize(),
	  		dataType:'json',
	  		success:function(data){
	  			if(data.code == 1){
	  				$("#add-div").hide();
	  				queryFrm.submit();
	  			}else{
	  				alert(data.message);
	  			}
	  		},
	  		error:function(e){
	  			alert("�������쳣");
	  			console.info("e",e);
	  		}
	    });
    }
    else{
      return false;
    }
  }

  function checkForm(){
    temp =document.all("vendor_name").value;
    if(temp=="")
    {
      alert("����д�豸�������ƣ�");
      return false;
    }
    temp =document.all("vendor_add").value;
    if(temp=="")
    {
      alert("����д�豸������ϵ��ַ��");
      return false;
    }
    return true;
  }

//��ҳ��������
function doQuerySubmit(){
    frm.actionType.value="";
    frm.submit();
}

</SCRIPT>
</head>
<%@ include file="../head.jsp" %>
<%@ include file="../toolbar.jsp"%>
<body>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<tr>
	<td>
	<table width="98%" height="30" border="0" cellspacing="0" cellpadding="0" class="green_gargtd" align="center">
		<tr>
			<td width="162" align="center" class="title_bigwhite">
				�豸����
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">
				��'<font color="#FF0000">*</font>'�ı�������д��ѡ��
			</td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td>
		<form name = "queryFrm" action="deviceModelInfo!StbVendorInfo.action">
			<table class="querytable"  width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
				<TR>
					<th colspan="4">���̲�ѯ</th>
				</TR>
				<TR>
					<TD class="column" width='15%' align="right">��������</TD>
					<TD width="225px">
						<input type="text" name="vendor_name" id="vendor_name" value="" maxlength=50 class=bk>
					</TD>
				 	<TD class=column width="15%" align='right'>���̱���</TD>
					<TD width="225px">
						<input type="text" name="vendor_add" id="vendor_add" value="" maxlength=50 class=bk>
					</TD>
				</TR>
				
				<TR>
					<td colspan="4" align="right" class=green_foot>
						<button type="button" onclick="add()" >&nbsp;��&nbsp;&nbsp;��&nbsp;</button>&nbsp;&nbsp;
						<button type="submit" >&nbsp;��&nbsp;&nbsp;ѯ&nbsp;</button>
					</td>
				</TR>
			</table>
		</form>
	</td>
</tr>
<TR><TD>
    <FORM NAME="frm" id="frm" METHOD="post" ACTION="deviceModelInfo!StbVendorInfo.action" onsubmit="return checkForm()">
	<TABLE width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
		<TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH bgcolor="#ffffff" colspan="5" >�豸����</TH>
					</TR>
					<tr CLASS=green_title2>
						<td width="20%">���̱��</td>
						<td width="20%">��������</td>
						<td width="20%">���̱���</td>
						<td width="20%">������ϵ�绰</td>
						<td width="20%">����</td>
					</tr>
					<s:iterator value="stbDeviceVendorList">
						<TR bgcolor="#FFFFFF">
							<td class=column1 nowrap><s:property value="vendor_id"/></td>
							<td class=column1 nowrap><s:property value="vendor_name"/></td>
							<td class=column1 nowrap><s:property value="vendor_add"/></td>
							<td class=column1 nowrap><s:property value="telephone"/></td>
							<td align="center" class=column1 nowrap>
								<a href="#" onclick="doEdit('<s:property value="vendor_id"/>',
										'<s:property value="vendor_name"/>',
										'<s:property value="vendor_add"/>',
										'<s:property value="telephone"/>',
										'<s:property value="staff_id"/>',
										'<s:property value="remark"/>')">�༭</a>&nbsp;&nbsp;
								<a href="#" onclick="delDel('<s:property value="vendor_id"/>')">ɾ��</a>&nbsp;&nbsp;
							</td>
						</TR>
					</s:iterator>
					<tr>
						<td align="right" CLASS=green_foot colspan="5">
							<%@ include file="/PageFoot.jsp" %>
						</td>
					</tr>
				</TABLE>
			</TD>
		</TR>

	</TABLE>
	<br>
	<TABLE  id = "add-div" width="98%" border=0 cellspacing=0 cellpadding=0 align="center" STYLE="display:none">
	  <TR>
			<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<TR>
						<TH colspan="4" align="center"><SPAN id="actLabel">���</SPAN><SPAN id="VendorLabel"></SPAN>�豸����</TH>
					</TR>
					<INPUT TYPE="hidden" NAME="vendorId" maxlength=6 class=bk size=10>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >��������</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="vendor_name" maxlength=32 class=bk size=40>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >���̱���</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="vendor_add" maxlength=25 class=bk size=30>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >������ϵ�绰</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="telephone" maxlength=10 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >Ա��ID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=15 class=bk size=30></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >��ע</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=60 class=bk size=80></TD>
					</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=green_foot>
							<INPUT TYPE="button" onclick="save()" value=" �� �� " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="hidden" NAME="actionType" value="">
							<INPUT TYPE="reset" value=" �� д " class=btn>
						</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>
	</TABLE>
	</FORM>
</TD></TR>
</TABLE>
<%@ include file="../foot.jsp"%>
</body>
</html>