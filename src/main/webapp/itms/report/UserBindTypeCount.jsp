<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>

<script language="JavaScript">


</script>

<br>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align=center>
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite"
						ondblclick="ShowHideLog()">
						�û�����Ϣͳ��
					</td>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<form name=frm>
				<table width="100%" border="0" cellspacing="1" cellpadding="2"
					bgcolor="#999999">
					<tr>
						<th colspan=4>
							�û�����Ϣͳ��
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							��ʼʱ��
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img
								onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">
						</td>
						<td class=column align=center width="15%">
							����ʱ��
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="ѡ��">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							�� ��
						</td>
						<td colspan="3">
							<select name="city_id">
								<option value="00">
								ʡ����
								</option>
							</select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column colspan=4 align=right>
							<input type="button" value=" ͳ �� " onclick="#"
								class="jianbian">
						</td>
					</tr>
				</table>
			</form>
		</td>
	</tr>

	<tr id="trData" style="display:">
		<td class="colum">
			<div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
<table width="100%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td>
			<table width="100%" border=0 cellspacing=1 cellpadding=2
				bgcolor=#999999 id=userTable>
				<tr>
					<th colspan="9">
						��ѯ���
					</th>
				</tr>
				<tr>
					<td class="green_title" align='center'>
						����
					</td>
					<td class="green_title" align='center'>
						�ܿ�����
					</td>
					<td class="green_title" align='center'>
						MAC�ȶ��½��û�
					</td>
					<td class="green_title" align='center'">
						�ֹ���
					</td>
					<td class="green_title" align='center'>
						������
					</td>
					<td class="green_title" align='center'>
						MAC�ȶ԰�
					</td>
					<td class="green_title" align='center'>
						��Ч����
					</td>
					<td class="green_title" align='center'>
						�Զ���MAC��֤
					</td>
					<td class="green_title" align='center'>
						�ֹ���MAC��֤
					</td>
				</tr>
				<tr class="column" bgcolor=#ffffff>
					<td class="" align='center'>
						ʡ����
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
				</tr>
				<tr class="column" bgcolor=#ffffff>
					<td class="" align='center'>
						<a href="#">�Ͼ�</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
					<td class="" align='center'>
						<a href="#">12</a>
					</td>
				</tr>
				<tr class="column" bgcolor=#ffffff>
					<td class="" align='center'>
						...
					</td>
					<td class="" align='center'>
						...
					</td>
					<td class="" align='center'>
						...
					</td>
					<td class="" align='center'>
						...
					</td>
					<td class="" align='center'>
						...
					</td>
					<td class="" align='center'>
						...
					</td>
					<td class="" align='center'>
						...
					</td>
					<td class="" align='center'>
						...
					</td>
					<td class="" align='center'>
						...
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr bgcolor=#999999>
		<td>
			<table border=0 cellspacing=1 cellpadding=2 width="100%"
				align="center">
				<tr bgcolor="#FFFFFF">
					<td class=column align="center" width="40">
						<IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0'
							ALT='�����б�' style='cursor: hand' onclick="ToExcel()">
					</td>
					<td class=column align="right">

					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr STYLE="display: none">
		<td>
			<iframe id="childFrm" src=""></iframe>
		</td>
	</tr>
</table>
			</div>
		</td>
	</tr>
	<tr>
		<td height="20">
		</td>
	</tr>
</TABLE>

<%@ include file="/foot.jsp"%>
