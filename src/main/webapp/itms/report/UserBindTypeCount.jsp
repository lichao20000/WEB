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
						用户绑定信息统计
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
							用户绑定信息统计
						</th>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							起始时间
						</td>
						<td>
							<input type="text" name="starttime" class='bk' readonly
								value="<s:property value='starttime'/>">
							<img
								onclick="new WdatePicker(document.frm.starttime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">
						</td>
						<td class=column align=center width="15%">
							结束时间
						</td>
						<td>
							<input type="text" name="endtime" class='bk' readonly
								value="<s:property value='endtime'/>">
							<img
								onclick="new WdatePicker(document.frm.endtime,'%Y-%M-%D %h:%m:%s',true,'whyGreen')"
								src="<s:url value='/images/search.gif'/>" width="15" height="12"
								border="0" alt="选择">
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column align=center width="15%">
							属 地
						</td>
						<td colspan="3">
							<select name="city_id">
								<option value="00">
								省中心
								</option>
							</select>
						</td>
					</tr>
					<tr bgcolor=#ffffff>
						<td class=column colspan=4 align=right>
							<input type="button" value=" 统 计 " onclick="#"
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
						查询结果
					</th>
				</tr>
				<tr>
					<td class="green_title" align='center'>
						属地
					</td>
					<td class="green_title" align='center'>
						总开户数
					</td>
					<td class="green_title" align='center'>
						MAC比对新建用户
					</td>
					<td class="green_title" align='center'">
						手工绑定
					</td>
					<td class="green_title" align='center'>
						自助绑定
					</td>
					<td class="green_title" align='center'>
						MAC比对绑定
					</td>
					<td class="green_title" align='center'>
						有效绑定数
					</td>
					<td class="green_title" align='center'>
						自动绑定MAC认证
					</td>
					<td class="green_title" align='center'>
						手工绑定MAC认证
					</td>
				</tr>
				<tr class="column" bgcolor=#ffffff>
					<td class="" align='center'>
						省中心
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
						<a href="#">南京</a>
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
							ALT='导出列表' style='cursor: hand' onclick="ToExcel()">
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
