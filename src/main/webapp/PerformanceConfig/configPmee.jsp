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
 * ��������ҳ�桾�������ú�����������ͬһ�����̡�
 * �����豸����ǰ�ȼ������ܱ��ʽ�Ƿ��Ѿ����ù�������Ѿ����ù�����ʾ�û�
 * ����豸����ʱ����ʾ�û��Ƿ����ù���ֱ��ɾ����ǰ�����
 * REQ:XXXX-XXXX-XXXX
 * BUG:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-9-25 11:44:14
 *
 * ��Ȩ���Ͼ���������Ƽ� ���ܲ�Ʒ��;
 *******************************************�޸ļ�¼**********************************************
 * ���     ʱ��       �޸���          ����&BUG����     �޸�����                              ��ע
 *-----------------------------------------------------------------------------------------------
 *  1  2008-10-17   BENYP(5260)     ��          ��ʼ��ҳ�����Ӳ�ѯģ�����õ����ܱ��ʽ  �ͱ����Ƶ�����ģ���ں�
 *-----------------------------------------------------------------------------------------------
 *  2  2008-10-17   BENYP(5260)     ��          ���ý�������Ӳɼ�ʱ���������ܹ��޸�  ����Ҫ��
 *-----------------------------------------------------------------------------------------------
 *  3  2008-10-23   BENYP(5260)     ��          ��ԭʼ����Ĭ��Ϊ���                   ��˼�Ҫ��
 ************************************************************************* **********************
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��������</title>
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryExtend-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/Js/jQeuryCheckForm-linkage.js"/>"></script>
<script type="text/javascript"
	src="<s:url value="/PerformanceConfig/jquery.configPmee.js"/>"></script>
<link href="<s:url value="/css2/c_table.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/tab.css"/>" rel="stylesheet"
	type="text/css">
<link href="<s:url value="/css2/global.css"/>" rel="stylesheet"
	type="text/css">
<script language="javascript">
	$(function(){
		$.init("hide");//��ʼ��
		var flg="<s:property value="ajax"/>";
		if(flg!=null && flg!=""){
			if(flg=="true"){
				alert("���óɹ���");
				window.location="<s:url value="/performance/configPmee.action"/>?isbatch=<s:property value="isbatch"/>&device_id=<s:property value="device_id"/>";
			}else{
				alert("����ʧ�ܣ�");
			}
		}
		window.setInterval("getConfigExp();",1000*60*2);//ˢ�»�ȡ�Ѿ����õ����ܱ��ʽ
		//˫��ѡ�����ܱ��ʽ
		$("select[@name='ex_perssion']").dblclick(function(){
			$("select[@name='sel_expression']").append($("select[@name='ex_perssion'] option:selected"));
		});
		//˫���Ƴ����ܱ��ʽ
		$("select[@name='sel_expression']").dblclick(function(){
			$("select[@name='ex_perssion']").append($("select[@name='sel_expression'] option:selected"));
		});
		//��ѡѡ�����ܱ��ʽ
		$("button[@name='add']").click(function(){
			$("select[@name='sel_expression']").append($("select[@name='ex_perssion'] option:selected"));
		});
		//��ѡ�Ƴ����ܱ��ʽ
		$("button[@name='del']").click(function(){
			$("select[@name='ex_perssion']").append($("select[@name='sel_expression'] option:selected"));
		});
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
		//��ʾ�������ø澯��
		$("td[@name='td_show']").toggle(function(){
			$(this).html("�������� <img src='<s:url value="/images/ico_9_up.gif"/>' width='10' hight='12' align='center' border='0' alt='����������ø澯' >");
			$("tr[@name='tr_show']").show();
			$.showChage();
		},function(){
			$(this).html("���ø澯 <img src='<s:url value="/images/ico_9.gif"/>' width='10' hight='12' align='center' border='0' alt='������ø澯' >");
			$.init("hide");
		});
	});

	//���ø澯
	function ConfigWarn(expression_id){
		var url="<s:url value="/performance/configPmee!ConfigWarn.action"/>?device_id=<s:property value="device_id"/>&expressionid="+expression_id;
		window.open(url);
	}

	//ˢ�»�ȡ�Ѿ����õ����ܱ��ʽ
	function getConfigExp(){
		$.post(
			"<s:url value="/performance/configPmee!getConfigExp.action"/>",
			{
				device_id:"<s:property value="device_id"/>"
			},
			function(data){
				$("#tbody").html(data);
			}
		);
	}

	//��ʾ������ϸ��Ϣ
	function showDetail(device_id,name,ip,expressionid){
		var url="<s:url value="/performance/configPmee!showDetail.action"/>?expressionid="+expressionid+"&device_id="+device_id+"&t="+new Date();
		window.open(url);
	}
	//ˢ�����ܱ��ʽ
	function RefreshExp(expressionid){
		$("input[@name='expressionid']").val(expressionid);
		$("select[@name='keep']").attr("selectedIndex",0);
		if($.CheckWarn()){
			$("form").submit();
		}
		return false;
	}
	//ɾ�����ܱ��ʽ
	function Del(device_id,expressionid,target){
		$.post(
			"<s:url value="/performance/configPmee!delExp.action"/>",
			{
				device_id:device_id,
				expressionid:expressionid,
				id:"",
				type:"all"
			},
			function(data){
				if(data=="true"){
					alert("�ɹ�ɾ�����ã�");
					target.parent().parent().remove();
				}else{
					alert("ɾ������ʧ�ܣ������ԣ�");
				}
			}
		);
	}

	//CHECK FORM
	function CheckForm(){
		if($("select[@name='sel_expression']").html()==""){
			alert("������ѡ��һ�����ܱ��ʽ!");
			return false;
		}else if($("input[@name='interval']").val()==""){
			alert("������ɼ�ʱ������");
			$("input[@name='interval']").focus();
			return false;
		}else if(!$.checkNum($("input[@name='interval']").val(),'int')){
			alert("�ɼ�ʱ����ֻ��Ϊ���������������룡");
			$("input[@name='interval']").focus();
			$("input[@name='interval']").select();
			return false;
		}else if($.CheckWarn()){
			var expressionid="";
			$("select[@name='sel_expression'] option").each(function(){
				expressionid+=","+$(this).val();
			});
			$("input[@name='expressionid']").val(expressionid.substring(1));
			if($("input[@name='isbatch']").val()=="false"){
				$.post(
					"<s:url value="/performance/configPmee!CheckConfig.action"/>",
					{
						device_id:"'"+$("input[@name='device_id']").val()+"'",
						expressionid:expressionid.substring(1)
					},
					function(data){
						if(data!=null && data!=""){
							if(window.confirm("�������ܱ��ʽ:\n"+data+"\n�Ѿ����ã��Ƿ��������ã�")){
								$("form").submit();
							}
						}else{
							$("form").submit();
						}
					}
				);
			}else{
				$("form").submit();
			}
			return false;
		}
		return false;
	}
</script>
<style type="text/css">
.red {
	color: red;
}

.blue {
	color: blue;
}
</style>
</head>
<body>
<form action="<s:url value="/performance/configPmee!Config.action"/>">
<br>
<table width="94%" align="center" class="querytable">
	<tr>
		<s:if test="isbatch==false">
			<th colspan="5" class="title_1">��������(<s:property
				value="DevInfoMap.device_name" />��<s:property
				value="DevInfoMap.loopback_ip" />��)</th>
		</s:if>
		<s:else>
			<th colspan="5" class="title_1">��������</th>
		</s:else>
	</tr>
	<tr>
		<td colspan="2" class="title_1">��ѡ���豸����</td>
		<td class="title_1"></td>
		<td colspan="2" class="title_1">��ѡ���豸����</td>
	</tr>
	<tr>
		<td colspan="2"><select size="10" multiple="multiple" style="width: 100%;" name="ex_perssion">
			<s:iterator var="exp" value="ExpList">
				<option value="<s:property value="#exp.expressionid"/>"><s:property
					value="#exp.name" /></option>
			</s:iterator>
		</select></td>
		<td align="center" valign="middle" class="title_2">
		<button name="add">&nbsp;>>&nbsp;</button>
		<br>
		<br>
		<br>
		<br>
		<button name="del">&nbsp;<<&nbsp;</button>
		</td>
		<td colspan="2"><select size="10" multiple="multiple"
			style="width: 100%;" name="sel_expression">
			<s:iterator var="expu" value="ExpListUse">
				<option selected value="<s:property value="#expu.expressionid"/>"><s:property
					value="#expu.name" /></option>
			</s:iterator>
		</select></td>
	</tr>
	<tr>
		<td width="20%" class="title_2">�ɼ�ʱ������</td>
		<td width="25%"><input type="text" size="8" name="interval"
			value="300">(��)</td>
		<td width="10%" class="title_2"></td>
		<td width="20%" class="title_2">ԭʼ�����Ƿ����:</td>
		<td width="25%"><select name="intodb">
			<option value="1" selected>��</option>
			<option value="0">��</option>
		</select></td>
	</tr>
	<tr>
		<td class="title_2">�Ƿ���ԭ������</td>
		<td colspan="4"><select name="iskeep">
			<option value="true">����ԭ������</option>
			<option value="false">��������</option>
		</select></td>
	</tr>
	<tr>
		<td colspan="5" class="title_1" name="td_show">���ø澯 <img
			src="<s:url value="/images/ico_9.gif"/>" width="10" hight="12"
			align="center" border="0" alt="������ø澯"></td>
	</tr>
	<tr>
		<td colspan="5">&nbsp;</td>
	</tr>
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
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="minwarninglevel" theme="simple" headerKey="-1" headerValue="��ѡ��"  value="-1"/>
			</td>
		<td class="title_2"></td>
		<td class="title_2">�ָ��澯����:</td>
		<td><!--<select name="minreinstatelevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="minreinstatelevel" theme="simple" headerKey="-1" headerValue="��ѡ��" value="-1"/>
		</td>
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
		<td>
		<!--<select name="maxwarninglevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxwarninglevel" theme="simple" headerKey="-1" headerValue="��ѡ��" value="-1"/>
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
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxreinstatelevel" theme="simple" headerKey="-1" headerValue="��ѡ��" value="-1"/>
		</td>
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
		<td>
		<!--<select name="dynawarninglevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select>
		-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynawarninglevel" theme="simple" headerKey="-1" headerValue="��ѡ��" value="-1"/>
		</td>
		<td class="title_2"></td>
		<td class="title_2">�ָ��澯����:</td>
		<td><!--<select name="dynareinstatelevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynareinstatelevel" theme="simple" headerKey="-1" headerValue="��ѡ��" value="-1"/>

		</td>
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
		<td colspan="4"><!--<select name="mutationwarninglevel">
			<option value="0" selected>��ѡ��</option>
			<option value="1">������־</option>
			<option value="2">��ʾ�澯</option>
			<option value="3">һ��澯</option>
			<option value="4">���ظ澯</option>
			<option value="5">�����澯</option>
		</select>-->
		<s:select list="#request.warnMap" listKey="key" listValue="value" name="mutationwarninglevel" theme="simple" headerKey="-1" headerValue="��ѡ��" value="-1"/>
		</td>
	</tr>
	<tr name="tr_sudden">
		<td colspan="5" class="foot">��ע�� �仯�ʾ���ֵ �� |�����ɵ���ֵ���ϴβɼ�ֵ��/�ϴβɼ�ֵ�� *
		100|</td>
	</tr>
	<!-- *************************************************END******************************************** -->
	<tr>
		<td colspan="5" class="foot" align="center"><input type="hidden"
			name="device_id" value="<s:property value="device_id"/>"> <input
			type="hidden" name="expressionid"> <input type="hidden"
			name="isbatch" value="<s:property value="isbatch"/>">
		<button type="submit" onclick="return CheckForm();">&nbsp;��&nbsp;��&nbsp;</button>
		&nbsp;&nbsp; <s:if test="!ismodule">
			<button onclick="window.close();">&nbsp;��&nbsp;��&nbsp;</button>
		</s:if></td>
	</tr>
</table>
<br>
<!-- ���ý��չʾ�������������������ʾ��� --> <s:if
	test="ConfigResultList!=null && ConfigResultList.size()>0">
	<table width="94%" align="center" class="listtable">
		<thead>
			<tr>
				<th colspan="6">�豸(<s:property value="DevInfoMap.device_name" />��<s:property
					value="DevInfoMap.loopback_ip" />��)���ý��</th>
			</tr>
			<tr>
				<th width="25%" nowrap>���ʽ����</th>
				<th width="25%" nowrap>���ʽ����</th>
				<th width="10%" nowrap>�ɼ�ʱ����</th>
				<th width="8%" nowrap>����״̬</th>
				<th width="12%" nowrap>ʧ��ԭ��</th>
				<th width="20%" nowrap>����</th>
			</tr>
		</thead>
		<tbody id="tbody">
			<s:if test="ConfigResultList==null || ConfigResultList.size()==0">
				<tr>
					<td colspan="6">���豸��ʱδ�����κ����ܱ��ʽ</td>
				</tr>
			</s:if>
			<s:else>
				<s:iterator var="crlist" value="ConfigResultList">
					<tr style="color:<s:property value="#crlist.isok<1 ?'red':''"/>">
						<td><s:property value="#crlist.name" /></td>
						<td><s:property value="#crlist.descr" /></td>
						<td><label onclick="$.showT($(this));"><s:property
							value="#crlist.interval" /></label> <input type="text" size="8"
							style="display: none;"
							onmouseout="$.MO('<s:property value="#crlist.interval"/>',$(this))"
							onchange="$.changeT('<s:property value="#crlist.interval"/>',
									       					   '<s:property value="#crlist.expressionid"/>',
									       					   '<s:property value="#crlist.device_id"/>',
									                           $(this));"
							value="<s:property value="#crlist.interval"/>"></td>
						<td><s:property value="#crlist.result" /></td>
						<td><s:property value="#crlist.remark" /></td>
						<td><a href="#"
							onclick="showDetail('<s:property value="#crlist.device_id"/>',
																	'<s:property value="#crlist.device_name"/>',
									                                '<s:property value="#crlist.loopback_ip"/>',
									                                '<s:property value="#crlist.expressionid"/>');">��ϸ</a>&nbsp;
						<a href="#"
							onclick="RefreshExp('<s:property value="#crlist.expressionid"/>')">ˢ��</a>&nbsp;
						<a href="#"
							onclick="ConfigWarn('<s:property value="#crlist.expressionid"/>')">���ø澯</a>&nbsp;
						<a href="#"
							onclick="Del('<s:property value="device_id"/>',
									                         '<s:property value="#crlist.expressionid"/>',
									                         $(this))">ɾ��</a>&nbsp;
						</td>
					</tr>
				</s:iterator>
			</s:else>
		</tbody>
	</table>
</s:if> <br>
</form>
</body>
</html>
