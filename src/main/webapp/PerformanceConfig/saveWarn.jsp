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
 * ���ø澯ҳ��
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-13 AM 10:13:19
 * 
 * @��Ȩ �Ͼ����� ���ܲ�Ʒ��;
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>���ø澯</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">	
<script type="text/javascript">
	var flg="<s:property value="ajax"/>";
	if(flg!=null && flg!=""){
		if(flg=="true"){
			alert("�༭�ɹ���");
			window.close();
		}else{
			alert("�༭ʧ�ܣ�");
		}
	}
	$(function(){
		$.init("show");
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
	
	//CheckForm
	function CheckForm(){
		if(!$.CheckWarn()){
			return false;
		}
		var url="<s:url value="/performance/configPmee!SaveWarn.action"/>";
		$("form[@name='frm']").attr("action",url);
		$("form[@name='frm']").submit();
		return true;
	}
</script>
</head>
<body>
	<form name="frm">
	<br>
	<table align="center" width="94%" class="querytable" id="tab_warn">
			<tr>
				<td colspan="5">
					<table width="450" class="tab" align="left">
						<tr>
							<td class="mouseon" name="td_fix" 
								onMouseOver="this.className='mouseon';" 
								onMouseOut="this.className='mouseon';"
                      		>�̶���ֵ����</td>
							<td class="mouseout" name="td_active"
								onMouseOver="this.className='mouseon';"
								onMouseOut="this.className='mouseout';" 
                      		>��̬��ֵ����</td>
							<td class="mouseout" name="td_sudden"
								onMouseOver="this.className='mouseon';"
								onMouseOut="this.className='mouseout';" 
                      		>ͻ�䷧ֵ����</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- *******************************************�����ǹ̶���ֵ������************************************ -->
			<tr name="tr_fix">
				<td class="title_2">�Ƚϲ�����һ:</td>
				<td>
					<select name="mintype">
						<option value="0" selected>��ʹ��</option>
						<option value="1">����</option>
						<option value="2">���ڵ���</option>
						<option value="3">С��</option>
						<option value="4">С�ڵ���</option>
						<option value="5">����</option>
						<option value="6">������</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">�̶���ֵһ:</td>
				<td>
					<input type="text" name="minthres">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">�̶���ֵһ����:</td>
				<td colspan="4">
					<input type="text" size="58" name="mindesc">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">����������ֵһ(��)</td>
				<td>
					<input type="text" name="mincount" value="1">
				</td>
				<td colspan="3">(�����澯)</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">�����澯����:</td>
				<td>
					<!--<select name="minwarninglevel">
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
				<td>
					<!--<select name="minreinstatelevel">
						<option value="0" selected>��ѡ��</option>
						<option value="1">������־</option>
						<option value="2">��ʾ�澯</option>
						<option value="3">һ��澯</option>
						<option value="4">���ظ澯</option>
						<option value="5">�����澯</option>
					</select>-->
					<s:select list="#request.warnMap" listKey="key" listValue="value" name="minreinstatelevel" theme="simple" headerKey="-1" headerValue="��ѡ��"/>
		
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">�Ƚϲ�������:</td>
				<td>
					<select name="maxtype">
						<option value="0" selected>��ʹ��</option>
						<option value="1">����</option>
						<option value="2">���ڵ���</option>
						<option value="3">С��</option>
						<option value="4">С�ڵ���</option>
						<option value="5">����</option>
						<option value="6">������</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">�̶���ֵ��:</td>
				<td>
					<input type="text" name="maxthres">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">�̶���ֵ������:</td>
				<td colspan="4">
					<input type="text" size="58" name="maxdesc">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">����������ֵ��(��)</td>
				<td>
					<input type="text" name="maxcount" value="1">
				</td>
				<td colspan="3">(�����澯)</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">�����澯����:</td>
				<td>
					<!--<select name="maxwarninglevel">
						<option value="0" selected>��ѡ��</option>
						<option value="1">������־</option>
						<option value="2">��ʾ�澯</option>
						<option value="3">һ��澯</option>
						<option value="4">���ظ澯</option>
						<option value="5">�����澯</option>
					</select>-->
					<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxwarninglevel" theme="simple" headerKey="-1" headerValue="��ѡ��"/>
		
				</td>
				<td class="title_2"></td>
				<td class="title_2">�ָ��澯����:</td>
				<td>
					<!--<select name="maxreinstatelevel">
						<option value="0" selected>��ѡ��</option>
						<option value="1">������־</option>
						<option value="2">��ʾ�澯</option>
						<option value="3">һ��澯</option>
						<option value="4">���ظ澯</option>
						<option value="5">�����澯</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxreinstatelevel" theme="simple" headerKey="-1" headerValue="��ѡ��"/>
		</td>
			</tr>
			<!-- *******************************************�����Ƕ�̬��ֵ������************************************ -->
			<tr name="tr_active">
				<td class="title_2">��̬��ֵ������:</td>
				<td>
					<select name="dynatype">
						<option value="0" selected>��ʹ��</option>
						<option value="1">�仯�ʴ���</option>
						<option value="2">�仯�ʴ��ڵ���</option>
						<option value="3">�仯��С��</option>
						<option value="4">�仯��С�ڵ���</option>
						<option value="5">�仯�ʵ���</option>
						<option value="6">�仯�ʲ�����</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">���ݵĻ�׼ֵ��ǰ���죩:</td>
				<td>
					<input type="text" name="beforeday" value="1">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">��ֵ�ٷֱȣ�����:</td>
				<td colspan="4">
					<input type="text" size="58" name="dynathres">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">�ﵽ��ֵ�ٷֱȣ��Σ�</td>
				<td>
					<input type="text" name="dynacount" value="1">
				</td>
				<td colspan="3">(�����澯)</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">��̬��ֵ����:</td>
				<td colspan="4">
					<input type="text" size="58" name="dynadesc">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">�����澯����:</td>
				<td>
					<!--<select name="dynawarninglevel">
						<option value="0" selected>��ѡ��</option>
						<option value="1">������־</option>
						<option value="2">��ʾ�澯</option>
						<option value="3">һ��澯</option>
						<option value="4">���ظ澯</option>
						<option value="5">�����澯</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynawarninglevel" theme="simple" headerKey="-1" headerValue="��ѡ��"/>
		        </td>
				<td class="title_2"></td>
				<td class="title_2">�ָ��澯����:</td>
				<td>
					<!--<select name="dynareinstatelevel">
						<option value="0" selected>��ѡ��</option>
						<option value="1">������־</option>
						<option value="2">��ʾ�澯</option>
						<option value="3">һ��澯</option>
						<option value="4">���ظ澯</option>
						<option value="5">�����澯</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynareinstatelevel" theme="simple" headerKey="-1" headerValue="��ѡ��"/>
		        </td>
			</tr>
			<tr name="tr_active">
				<td colspan="5" class="foot">��ע��  �仯�� �� �����ɵ���ֵ����׼ֵ��/��׼ֵ�� * 100</td>
			</tr>
			<!-- *******************************************������ͻ����ֵ������************************************ -->
			<tr name="tr_sudden">
				<td class="title_2">ͻ�䷧ֵ������:</td>
				<td>
					<select name="mutationtype">
						<option value="0" selected>��ʹ��</option>
						<option value="1">�仯�ʾ���ֵ����</option>
						<option value="2">�仯�ʾ���ֵ���ڵ���</option>
						<option value="3">�仯�ʾ���ֵС��</option>
						<option value="4">�仯�ʾ���ֵС�ڵ���</option>
						<option value="5">�仯�ʾ���ֵ����</option>
						<option value="6">�仯�ʾ���ֵ������</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">�����ٷֱȣ�����:</td>
				<td>
					<input type="text" name="mutationthres">
				</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">�ﵽ��ֵ�ٷֱȣ��Σ�</td>
				<td>
					<input type="text" name="mutationcount" value="1">
				</td>
				<td colspan="3">(�����澯)</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">ͻ�䷧ֵ����:</td>
				<td colspan="4">
					<input type="text" size="58" name="mutationdesc">
				</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">�����澯����:</td>
				<td colspan="4">
					<!--<select name="mutationwarninglevel">
						<option value="0" selected>��ѡ��</option>
						<option value="1">������־</option>
						<option value="2">��ʾ�澯</option>
						<option value="3">һ��澯</option>
						<option value="4">���ظ澯</option>
						<option value="5">�����澯</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="mutationwarninglevel" theme="simple" headerKey="-1" headerValue="��ѡ��"/>
		        </td>
			</tr>
			<tr name="tr_sudden">
				<td colspan="5" class="foot">��ע��  �仯�ʾ���ֵ �� |�����ɵ���ֵ���ϴβɼ�ֵ��/�ϴβɼ�ֵ�� * 100|</td>
			</tr>
			<!-- *************************************************END******************************************** -->
			<tr>
				<td colspan="5" class="foot" align="center">
					<input type="hidden" name="device_id" value="<s:property value="device_id"/>">
					<input type="hidden" name="expressionid" value="<s:property value="expressionid"/>">
					<button type="submit" onclick="return CheckForm();">&nbsp;��&nbsp;��&nbsp;</button>&nbsp;&nbsp;
					<button onclick="window.close();">&nbsp;��&nbsp;��&nbsp;</button>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>