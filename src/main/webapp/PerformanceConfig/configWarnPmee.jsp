<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.Map,com.linkage.module.gwms.dao.gw.EventLevelLefDAO"%>
<%
EventLevelLefDAO eventLevelLefDAO=new EventLevelLefDAO();
Map warnMap = eventLevelLefDAO.getWarnLevel();
request.setAttribute("warnMap",warnMap);
%>
<%--
/**
 * ���ø澯ѡ����
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-6 PM 03:42:45
 * 
 * @��Ȩ����.�Ͼ����� ���ܲ�Ʒ��;
 * 
 */
 --%>
<html>
<head>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/tab.css"/>"     rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>"  rel="stylesheet" type="text/css">
<script type="text/javascript">
	$(function(){
		$("tr[@name='tr_fix']").show();
		$("tr[@name='tr_active']").hide();
		$("tr[@name='tr_sudden']").hide();
		//����̶���ֵ����
		$("td[@name='td_fix']").click(function(){
			$.showHide(1);
		});
		//�����̬��ֵ����
		$("td[@name='td_active']").click(function(){
			$.showHide(2);
		});
		//���ͻ����ֵ����
		$("td[@name='td_sudden']").click(function(){
			$.showHide(3);
		});
	});

	
	//Check Form
	function CheckForm(){
		if($("select[@name='mintype']").val()>0 && $("input[@name='minthres']").val()==""){
			alert("�����˹̶���ֵһ���̶���ֵһ����Ϊ�գ�������!");
			$("input[@name='minthres']").focus();
			return false;
		}else if($("input[@name='minthres']").val()!="" && !$.checkNum($("input[@name='minthres']").val(),'int')){
			alert("�̶���ֵֻ��Ϊ���֣����������룡");
			$("input[@name='minthres']").focus();
			$("input[@name='minthres']").select();
			return false;
		}else if($("input[@name='mincount']").val()==""){
			alert("����������ֵ�Ĵ�������Ϊ�գ����������룡");
			$("input[@name='mincount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='mincount']").val(),'int')){
			alert("����������ֵ�Ĵ���ֻ��Ϊ���ָ�ʽ�����������룡");
			$("input[@name='mincount']").focus();
			$("input[@name='mincount']").select();
			return false;
		}else if($("select[@name='maxtype']").val()>0 && $("input[@name='maxthres']").val()==""){
			alert("�̶���ֵ����Ϊ�գ������룡");
			$("input[@name='maxthres']").focus();
			return false;
		}else if($("input[@name='maxthres']").val()!="" && !$.checkNum($("input[@name='maxthres']").val(),'int')){
			alert("�̶���ֵֻ��Ϊ���ָ�ʽ�����������룡");
			$("input[@name='maxthres']").focus();
			$("input[@name='maxthres']").select();
			return false;
		}else if($("input[@name='maxcount']").val()==""){
			alert("����������ֵ�Ĵ�������Ϊ�գ������룡");
			$("input[@name='maxcount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='maxcount']").val(),'int')){
			alert("����������ֵ�Ĵ���ֻ��Ϊ���ָ�ʽ�����������룡");
			$("input[@name='maxcount']").focus();
			$("input[@name='maxcount']").select();
			return false;
		}else if($("input[@name='beforeday']").val()==""){
			alert("���������ݻ�׼ֵ��");
			$("input[@name='beforeday']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='beforeday']").val(),'int')){
			alert("���ݻ�׼ֵֻ��Ϊ���ָ�ʽ�����������룡");
			$("input[@name='beforeday']").focus();
			$("input[@name='beforeday']").select();
			return false;
		}else if($("select[@name='dynatype']").val()>0 && $("input[@name='dynathres']").val()==""){
			alert("��ֵ�ٷֱȲ���Ϊ�գ������룡");
			$("input[@name='dynathres']").focus();
			return false;
		}else if($("input[@name='dynathres']").val()!="" && !$.checkNum($("input[@name='dynathres']").val(),'int')){
			alert("��ֵ�ٷֱ�ֻ��Ϊ���ָ�ʽ������������");
			$("input[@name='dynathres']").focus();
			$("input[@name='dynathres']").select();
			return false;
		}else if($("select[@name='mutationtype']").val()>0 && $("input[@name='mutationthres']").val()==""){
			alert("ͻ�䷧ֵ�����ٷֱȲ���Ϊ�գ�������!");
			$("input[@name='mutationthres']").focus();
			return false;
		}else if($("input[@name='mutationthres']").val()!="" && !$.checkNum($("input[@name='mutationthres']").val(),'int')){
			alert("�ٷֱ�ֻ��Ϊ���ָ�ʽ������������!");
			$("input[@name='mutationthres']").focus();
			$("input[@name='mutationthres']").select();
			return false;
		}else if($("input[@name='mutationcount']").val()==""){
			alert("�ﵽ��ֵ�ٷֱȴ�������Ϊ�գ�������!");
			$("input[@name='mutationcount']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='mutationcount']").val(),'int')){
			alert("����ֻ��Ϊ���ָ�ʽ������������");
			$("input[@name='mutationcount']").focus();
			$("input[@name='mutationcount']").select();
			return false;
		}else{
			return true;
		}
	}
</script>
</head>
<body>
<br>
<table width="100%" class="querytable">
	<tr name="tr_show">
		<td colspan="5">
		<table width="450" class="tab" align="left">
			<tr>
				<td class="mouseon" name="td_fix"
					onMouseOver="this.className='mouseon';"
					onMouseOut="this.className='mouseon';">�̶���ֵ����</td>
				<td class="mouseout" name="td_active"
					onMouseOver="this.className='mouseon';"
					onMouseOut="this.className='mouseout';">��̬��ֵ����</td>
				<td class="mouseout" name="td_sudden"
					onMouseOver="this.className='mouseon';"
					onMouseOut="this.className='mouseout';">ͻ�䷧ֵ����</td>
			</tr>
		</table>
		</td>
	</tr>
	<!-- *******************************************�����ǹ̶���ֵ������************************************ -->
	<tr name="tr_fix">
		<td class="title_2">�Ƚϲ�����һ:</td>
		<td><select name="mintype">
			<option value="0" selected>��ʹ��</option>
			<option value="1">����</option>
			<option value="2">���ڵ���</option>
			<option value="3">С��</option>
			<option value="4">С�ڵ���</option>
			<option value="5">����</option>
			<option value="6">������</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">�̶���ֵһ:</td>
		<td><input type="text" name="minthres"></td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">�̶���ֵһ����:</td>
		<td colspan="4"><input type="text" size="58" name="mindesc">
		</td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">����������ֵһ(��)</td>
		<td><input type="text" name="mincount" value="1"></td>
		<td colspan="3">(�����澯)</td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">�����澯����:</td>
		<td><!--<select name="minwarninglevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="minwarninglevel" theme="simple" headerKey="-1" headerValue="��ѡ��"/>
			    </td>
		<td class="title_2"></td>
		<td class="title_2">�ָ��澯����:</td>
		<td><select name="minreinstatelevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select></td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">�Ƚϲ�������:</td>
		<td><select name="maxtype">
			<option value="0" selected>��ʹ��</option>
			<option value="1">����</option>
			<option value="2">���ڵ���</option>
			<option value="3">С��</option>
			<option value="4">С�ڵ���</option>
			<option value="5">����</option>
			<option value="6">������</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">�̶���ֵ��:</td>
		<td><input type="text" name="maxthres"></td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">�̶���ֵ������:</td>
		<td colspan="4"><input type="text" size="58" name="maxdesc">
		</td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">����������ֵ��(��)</td>
		<td><input type="text" name="maxcount" value="1"></td>
		<td colspan="3">(�����澯)</td>
	</tr>
	<tr name="tr_fix">
		<td class="title_2">�����澯����:</td>
		<td><select name="maxwarninglevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">�ָ��澯����:</td>
		<td><select name="maxreinstatelevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select></td>
	</tr>
	<!-- *******************************************�����Ƕ�̬��ֵ������************************************ -->
	<tr name="tr_active">
		<td class="title_2">��̬��ֵ������:</td>
		<td><select name="dynatype">
			<option value="0" selected>��ʹ��</option>
			<option value="1">�仯�ʴ���</option>
			<option value="2">�仯�ʴ��ڵ���</option>
			<option value="3">�仯��С��</option>
			<option value="4">�仯��С�ڵ���</option>
			<option value="5">�仯�ʵ���</option>
			<option value="6">�仯�ʲ�����</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">���ݵĻ�׼ֵ��ǰ���죩:</td>
		<td><input type="text" name="beforeday" value="1"></td>
	</tr>
	<tr name="tr_active">
		<td class="title_2">��ֵ�ٷֱȣ�����:</td>
		<td colspan="4"><input type="text" size="58" name="dynathres">
		</td>
	</tr>
	<tr name="tr_active">
		<td class="title_2">�ﵽ��ֵ�ٷֱȣ��Σ�</td>
		<td><input type="text" name="dynacount" value="1"></td>
		<td colspan="3">(�����澯)</td>
	</tr>
	<tr name="tr_active">
		<td class="title_2">��̬��ֵ����:</td>
		<td colspan="4"><input type="text" size="58" name="dynadesc">
		</td>
	</tr>
	<tr name="tr_active">
		<td class="title_2">�����澯����:</td>
		<td><select name="dynawarninglevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">�ָ��澯����:</td>
		<td><select name="dynareinstatelevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select></td>
	</tr>
	<tr name="tr_active">
		<td colspan="5" class="foot">��ע�� �仯�� �� �����ɵ���ֵ����׼ֵ��/��׼ֵ�� * 100</td>
	</tr>
	<!-- *******************************************������ͻ����ֵ������************************************ -->
	<tr name="tr_sudden">
		<td class="title_2">ͻ�䷧ֵ������:</td>
		<td><select name="mutationtype">
			<option value="0" selected>��ʹ��</option>
			<option value="1">�仯�ʾ���ֵ����</option>
			<option value="2">�仯�ʾ���ֵ���ڵ���</option>
			<option value="3">�仯�ʾ���ֵС��</option>
			<option value="4">�仯�ʾ���ֵС�ڵ���</option>
			<option value="5">�仯�ʾ���ֵ����</option>
			<option value="6">�仯�ʾ���ֵ������</option>
		</select></td>
		<td class="title_2"></td>
		<td class="title_2">�����ٷֱȣ�����:</td>
		<td><input type="text" name="mutationthres"></td>
	</tr>
	<tr name="tr_sudden">
		<td class="title_2">�ﵽ��ֵ�ٷֱȣ��Σ�</td>
		<td><input type="text" name="mutationcount" value="1"></td>
		<td colspan="3">(�����澯)</td>
	</tr>
	<tr name="tr_sudden">
		<td class="title_2">ͻ�䷧ֵ����:</td>
		<td colspan="4"><input type="text" size="58" name="mutationdesc">
		</td>
	</tr>
	<tr name="tr_sudden">
		<td class="title_2">�����澯����:</td>
		<td colspan="4"><select name="mutationwarninglevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select></td>
	</tr>
	<tr name="tr_sudden">
		<td colspan="5" class="foot">��ע�� �仯�ʾ���ֵ �� |�����ɵ���ֵ���ϴβɼ�ֵ��/�ϴβɼ�ֵ�� * 100|</td>
	</tr>
	<tr>
		<td colspan="5" class="foot" align="center">
			<button type="submit" onclick="return CheckForm();">&nbsp;��&nbsp;��&nbsp;</button>&nbsp;&nbsp;
			<button onclick="window.close();">&nbsp;��&nbsp;��&nbsp;</button>
		</td>
	</tr>
</table>
</body>
</html>