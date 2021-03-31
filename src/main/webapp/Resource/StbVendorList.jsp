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

//保存
function save(){
  	// 校验
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
  			alert("服务器异常");
  			console.info("e",e);
  		}
   });
}
  
function add(){
  document.getElementById('add-div').style.display = "";
	document.frm.reset();
	document.frm.actionType.value="add";
	actLabel.innerHTML = '添加';
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
    actLabel.innerHTML = '编辑';
    frm.actionType.value = "update";
  }

  function delDel(vendorId){
    if(confirm("真的要删除该厂商吗？\n本操作所删除的不能恢复！！！")){
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
	  			alert("服务器异常");
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
      alert("请填写设备厂商名称！");
      return false;
    }
    temp =document.all("vendor_add").value;
    if(temp=="")
    {
      alert("请填写设备厂商联系地址！");
      return false;
    }
    return true;
  }

//翻页方法调用
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
				设备厂商
			</td>
			<td>
				<img src="../images/attention_2.gif" width="15" height="12">
				带'<font color="#FF0000">*</font>'的表单必须填写或选择
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
					<th colspan="4">厂商查询</th>
				</TR>
				<TR>
					<TD class="column" width='15%' align="right">厂商名称</TD>
					<TD width="225px">
						<input type="text" name="vendor_name" id="vendor_name" value="" maxlength=50 class=bk>
					</TD>
				 	<TD class=column width="15%" align='right'>厂商别名</TD>
					<TD width="225px">
						<input type="text" name="vendor_add" id="vendor_add" value="" maxlength=50 class=bk>
					</TD>
				</TR>
				
				<TR>
					<td colspan="4" align="right" class=green_foot>
						<button type="button" onclick="add()" >&nbsp;新&nbsp;&nbsp;增&nbsp;</button>&nbsp;&nbsp;
						<button type="submit" >&nbsp;查&nbsp;&nbsp;询&nbsp;</button>
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
						<TH bgcolor="#ffffff" colspan="5" >设备厂商</TH>
					</TR>
					<tr CLASS=green_title2>
						<td width="20%">厂商编号</td>
						<td width="20%">厂商名称</td>
						<td width="20%">厂商别名</td>
						<td width="20%">厂商联系电话</td>
						<td width="20%">操作</td>
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
										'<s:property value="remark"/>')">编辑</a>&nbsp;&nbsp;
								<a href="#" onclick="delDel('<s:property value="vendor_id"/>')">删除</a>&nbsp;&nbsp;
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
						<TH colspan="4" align="center"><SPAN id="actLabel">添加</SPAN><SPAN id="VendorLabel"></SPAN>设备厂商</TH>
					</TR>
					<INPUT TYPE="hidden" NAME="vendorId" maxlength=6 class=bk size=10>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >厂商名称</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="vendor_name" maxlength=32 class=bk size=40>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >厂商别名</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="vendor_add" maxlength=25 class=bk size=30>&nbsp;<font color="#FF0000">*</font></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >厂商联系电话</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="telephone" maxlength=10 class=bk size=20></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >员工ID</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="staff_id" maxlength=15 class=bk size=30></TD>
					</TR>
					<TR bgcolor="#FFFFFF">
						<TD class=column align="right" >备注</TD>
						<TD colspan=3><INPUT TYPE="text" NAME="remark" maxlength=60 class=bk size=80></TD>
					</TR>
					<TR >
						<TD colspan="4" align="right" CLASS=green_foot>
							<INPUT TYPE="button" onclick="save()" value=" 保 存 " class=btn>&nbsp;&nbsp;
							<INPUT TYPE="hidden" NAME="actionType" value="">
							<INPUT TYPE="reset" value=" 重 写 " class=btn>
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