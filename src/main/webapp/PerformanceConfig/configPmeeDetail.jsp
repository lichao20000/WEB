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
 * ����������ϸ��Ϣ
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-6 PM 03:40:48
 *
 * @��Ȩ �Ͼ����� ���ܲ�Ʒ��
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>����������ϸ��Ϣ</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript" src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet" type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet" type="text/css">
<script type="text/javascript">
	$(function(){
		$.init("show");
		var flg="<s:property value="ajax"/>";
		if(flg!=null && flg!=""){
			if(flg=="true"){
				alert("�༭�ɹ���");
				window.location="<s:url value="/performance/configPmee!showDetail.action"/>?expressionid=<s:property value="expressionid"/>&device_id=<s:property value="device_id"/>";
			}else{
				alert("�༭ʧ�ܣ�");
			}
		}
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
	//��ʾ�޸�ʵ����
	function Modify(id,intodb,mintype,minthres,mindesc,mincount,minwarninglevel,minreinstatelevel,
					maxtype,maxthres,maxdesc,maxcount,maxwarninglevel,maxreinstatelevel,
					dynatype,beforeday,dynathres,dynacount,dynadesc,dynawarninglevel,dynareinstatelevel,
					mutationtype,mutationthres,mutationcount,mutationdesc,mutationwarninglevel){
		$("input[@name='id']").val(id);
		$("#tab_warn").show();
		//****************************�Ƿ����***************************//
		$("select[@name='intodb'] option").each(function(){
			if($(this).val()==intodb){
				$(this).attr("selected",true);
			}
		});
		//****************************�̶���ֵ����***********************************
		$("select[@name='mintype']").attr("selectedIndex",mintype);//�Ƚϲ�����һ:
		$("input[@name='minthres']").val(minthres);//�̶���ֵһ
		$("input[@name='mindesc']").val(mindesc);//�̶���ֵһ����
		$("input[@name='mincount']").val(mincount);//����������ֵһ(��)
		$("select[@name='minwarninglevel']").attr("selectedIndex",minwarninglevel);//�����澯����:
		$("select[@name='minreinstatelevel']").attr("selectedIndex",minreinstatelevel);//�ָ��澯����:

		$("select[@name='maxtype']").attr("selectedIndex",maxtype);//�Ƚϲ�������:
		$("input[@name='maxthres']").val(maxthres);//�̶���ֵ��:
		$("input[@name='maxdesc']").val(maxdesc);//�̶���ֵ������:
		$("input[@name='maxcount']").val(maxcount);//����������ֵ��(��)
		$("select[@name='maxwarninglevel']").attr("selectedIndex",maxwarninglevel);//�����澯����:
		$("select[@name='maxreinstatelevel']").attr("selectedIndex",maxreinstatelevel);//�ָ��澯����:

		//*****************************��̬��ֵ����************************************
		$("select[@name='dynatype']").attr("selectedIndex",dynatype);
		$("input[@name='beforeday']").val(beforeday);//���ݵĻ�׼ֵ��ǰ���죩
		$("input[@name='dynathres']").val(dynathres);//��ֵ�ٷֱȣ�����
		$("input[@name='dynacount']").val(dynacount);//�ﵽ��ֵ�ٷֱȣ��Σ�
		$("input[@name='dynadesc']").val(dynadesc);//��̬��ֵ����
		$("select[@name='dynawarninglevel']").attr("selectedIndex",dynawarninglevel);//�����澯����:
		$("select[@name='dynareinstatelevel']").attr("selectedIndex",dynareinstatelevel);//�ָ��澯����

		//******************************ͻ�䷧ֵ����**********************************
		$("select[@name='mutationtype']").attr("selectedIndex",mutationtype);//ͻ�䷧ֵ������
		$("input[@name='mutationthres']").val(mutationthres);//�����ٷֱȣ�����
		$("input[@name='mutationcount']").val(mutationcount);//�ﵽ��ֵ�ٷֱȣ��Σ�
		$("input[@name='mutationdesc']").val(mutationdesc);//ͻ�䷧ֵ����
		$("select[@name='mutationwarninglevel']").attr("selectedIndex",mutationwarninglevel);//�����澯����
	}
	//ɾ��ʵ��
	function Del(id,device_id,expressionid,target){
		$.post(
			"<s:url value="/performance/configPmee!delExp.action"/>",
			{
				device_id:device_id,
				expressionid:expressionid,
				id:id,
				type:"one"
			},
			function(data){
				if(data=="true"){
					alert("ɾ���ɹ���");
					target.parent().parent().remove();
				}else{
					alert("ɾ��ʧ�ܣ�������!");
				}
				return;
			}
		);
	}
	//Check Form
	function CheckForm(){
		if(!$.CheckWarn()){
			return false;
		}else{
			return true;
		}
	}
</script>
</head>
<body>
	<form action="configPmee!editPxp.action">
		<br>
		<table width="94%" class="listtable" align="center">
			<thead>
				<tr>
					<th colspan="4">����������ϸ��Ϣ</th>
				</tr>
				<tr>
					<th nowrap>���ʽ����</th>
					<th nowrap>ʵ������</th>
					<th nowrap>����</th>
					<th nowrap>����</th>
				</tr>
			</thead>
			<tbody>
				<s:iterator var="data" value="ConfigData" status="rowstatus">
					<tr class="<s:property value="#rowstatus.odd==true?'odd':'even'"/>"
						onmouseover="className='odd'"
						onmouseout="className='<s:property value="#rowstatus.odd==true?'odd':'even'"/>'"
					>
						<td><s:property value="#data.name"/></td>
						<td><s:property value="#data.descr"/></td>
						<td><s:property value="#data.indexid"/></td>
						<td>
							<a href="#"
							onclick="Modify(
							'<s:property value="#data.id"/>',
							'<s:property value="#data.intodb"/>',
							'<s:property value="#data.mintype"/>',
							'<s:property value="#data.minthres"/>',
							'<s:property value="#data.mindesc"/>',
							'<s:property value="#data.mincount"/>',
							'<s:property value="#data.minwarninglevel"/>',
							'<s:property value="#data.minreinstatelevel"/>',
							'<s:property value="#data.maxtype"/>',
							'<s:property value="#data.maxthres"/>',
							'<s:property value="#data.maxdesc"/>',
							'<s:property value="#data.maxcount"/>',
							'<s:property value="#data.maxwarninglevel"/>',
							'<s:property value="#data.maxreinstatelevel"/>',
							'<s:property value="#data.dynatype"/>',
							'<s:property value="#data.beforeday"/>',
							'<s:property value="#data.dynathres"/>',
							'<s:property value="#data.dynacount"/>',
							'<s:property value="#data.dynadesc"/>',
							'<s:property value="#data.dynawarninglevel"/>',
							'<s:property value="#data.dynareinstatelevel"/>',
							'<s:property value="#data.mutationtype"/>',
							'<s:property value="#data.mutationthres"/>',
							'<s:property value="#data.mutationcount"/>',
							'<s:property value="#data.mutationdesc"/>',
							'<s:property value="#data.mutationwarninglevel"/>'
							);">&nbsp;�޸�&nbsp;</a>&nbsp;|&nbsp;
							<a href="#" onclick="Del('<s:property value="#data.id"/>',
							                         '<s:property value="device_id"/>',
							                         '<s:property value="expressionid"/>',$(this));">&nbsp;ɾ��&nbsp;</a>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<br>
		<table align="center" width="94%" class="querytable" id="tab_warn" style="display:none;">
			<tr>
				<td width="20%" class="title_2">ԭʼ�����Ƿ����:</td>
				<td colspan="4">
					<select name="intodb">
						<option value="1">��</option>
						<option value="0" selected>��</option>
					</select>
				</td>
			</tr>
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
					<select name="minreinstatelevel">
						<option value="0" selected>��ѡ��</option>
						<option value="1">������־</option>
						<option value="2">��ʾ�澯</option>
						<option value="3">һ��澯</option>
						<option value="4">���ظ澯</option>
						<option value="5">�����澯</option>
					</select>
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
					<input type="hidden" name="id">
					<input type="hidden" name="device_id" value="<s:property value="device_id"/>">
					<input type="hidden" name="expressionid" value="<s:property value="expressionid"/>">
					<button  type="submit" onclick="return CheckForm();">&nbsp;��&nbsp;��&nbsp;</button>&nbsp;&nbsp;
					<button onclick="window.close();">&nbsp;��&nbsp;��&nbsp;</button>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
