<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@page import="com.linkage.litms.common.database.Cursor" %>
<%@page import="com.linkage.litms.common.database.DataSetBean" %>
<%@page import="java.util.Map"%>
<%//�����б�
	String sql="select vendor_id,vendor_name from tab_vendor";
	Cursor cursor=DataSetBean.getCursor(sql);
	Map field=cursor.getNext();
	String data="";
	while(field!=null){
		//data+="<option value='"+field.get("vendor_id")+"'>=="+field.get("vendor_name")+"("+field.get("vendor_id")+")"+"==</option>";
		data+="<option value='"+field.get("vendor_id")+"'>=="+field.get("vendor_name")+"==</option>";
		field=cursor.getNext();
	}
%>
<html>
<head>
<link href="../css/css_green.css" rel="stylesheet" type="text/css">
<script type="text/javascript"  src="../Js/jquery.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!--
	$(function(){
		$("select[@name='vendor']").change(function(){
			if($(this).val()==""){
				alert("��ѡ����!");
				$(this).focus();
				return;
			}else{
				$("#custom").html("<img src='../images/loading.gif'>�����������ݣ���ȴ�......");
				$.post(
					"pmee_custom_dual_v3.jsp",
					{
						type:"1",
						vendor:$(this).val()
					},
					function(data){
						$("#custom").html(data);
					}
				);
			}
		});
		//
		$("#chk_all").click(function(){
			var chk=$(this).attr("checked");
			chk=typeof(chk)=="undefined"?false:chk;
			$("input[@name='chk']").attr("checked",chk);
			$("input[@name='chk'][@disabled]").attr("checked",true);
		});
	});
	//check form
	function CheckForm(){
		if($("input[@name='chk'][@checked]").length==0){
			alert("��ѡ����������!");
			return false;
		}
		$.post(
			"pmee_custom_dual_v3.jsp",
			{
				type:"0",
				vendor:$("select[@name='vendor']").val(),
				chk:encodeURIComponent(sel_chk())
			},
			function(data){
				if(eval(data)==true){
					alert("���Ƴɹ�!");
				}else{
					alert("����ʧ�ܣ�������!");
				}
			}
		);
		
	}
	function sel_chk(){
		var chk="";
		$("input[@name='chk'][@checked]").each(function(){
			chk+=","+$(this).val();
		});
		return chk.substring(1);
	}
	
//-->
</SCRIPT>
</head>
<body>
	<form>
		<table width="98%" border=0 align="center" cellpadding="1" cellspacing="1" bgcolor="#999999" class="text">
			<tr>
				<th colspan=2>���ܱ�����</th>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td align="right" width="20%">
					����:
				</td>
				<td>
					<select name="vendor">
						<option value="">
							==��ѡ��==
						</option>
						<%=data%>
					</select>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF" id="">
				<td align="right">��������:
					<br><input type="checkbox" name="chkall" id="chk_all"><label for="chk_all">ȫѡ:</label>
				</td>
				<td>
					<div id="custom"></div>
				</td>
			</tr>
			<tr bgcolor="#FFFFFF">
				<td align="center" colspan=2>
					<input type="button" value="����" onclick="CheckForm()">
					<input type="button" value="�ر�" onclick="window.close();">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>