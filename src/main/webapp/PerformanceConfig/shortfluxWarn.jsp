<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%--
/**
 * �������ø澯ҳ�桾ͨ�ø澯ҳ�棬�澯�ֶ�û�н��յĶࡿ
 * 
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-24 PM 04:43:01
 * 
 * @��Ȩ �Ͼ����� ���ܲ�Ʒ��;
 * 
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<script type="text/javascript">
	$(function(){
		$.initWarn();
	});
</script>
<style type="text/css">
	label.l_red{
		color:red;
		margin-left: 10px;
	}	
</style>
</head>
<body>
	<table width="100%" class="querytable">
		<tr>
			<td colspan="4">
				<table width="450" class="tab" align="left" >
					<tr>
						<td class="mouseon" name="td_fix" 
							onclick="$.showHide(1)"
							onMouseOver="this.className='mouseon';" 
							onMouseOut="this.className='mouseon';">�̶���ֵ�澯����</td>
						<td class="mouseout" name="td_active"
							onclick="$.showHide(2)"
							onMouseOver="this.className='mouseon';"
							onMouseOut="this.className='mouseout';">��̬��ֵ�澯����</td>
						<td class="mouseout" name="td_sudden"
							onclick="$.showHide(3)"
							onMouseOver="this.className='mouseon';"
							onMouseOut="this.className='mouseout';">ͻ�䷧ֵ�澯����</td>
					</tr>
				</table>
			</td>
		</tr>
		<!-- ****************************�̶���ֵ����*********************************** -->
		<tr name="tr_fix">
			<td class="title_2" width="20%"><input type="checkbox" id="c_inmax"><label for="c_inmax">�˿����������������ֵ(%)</label></td>
			<td width="30%">
				<input type="text" name="ifinoctetsbps_max" readonly class="onread">
			</td>
			<td class="title_2" width="20%"><input type="checkbox" id="c_outmax"><label for="c_outmax">�˿�����������������ֵ(%)</label></td>
			<td width="30%">
				<input type="text" name="ifoutoctetsbps_max" readonly class="onread">
			</td>
		</tr>
		<tr name="tr_fix">
			<td class="title_2"><input type="checkbox" id="c_disinmax"><label for="c_disinmax">�˿����붪������ֵ(%)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			<td>
				<input type="text" name="ifindiscardspps_max" readonly class="onread">
			</td>
			<td class="title_2"><input type="checkbox" id="c_disoutmax"><label for="c_disoutmax">�˿�������������ֵ(%)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			<td>
				<input type="text" name="ifoutdiscardspps_max" readonly class="onread">
			</td>
		</tr>
		<tr name="tr_fix">
			<td class="title_2"><input type="checkbox" id="c_errinmax"><label for="c_errinmax">�˿�����������ֵ(%)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			<td>
				<input type="text" name="ifinerrorspps_max" readonly class="onread">
			</td>
			<td class="title_2"><input type="checkbox" id="c_erroutmax"><label for="c_erroutmax">�˿������������ֵ(%)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			<td>
				<input type="text" name="ifouterrorspps_max" readonly class="onread">
			</td>
		</tr>
		<tr name="tr_fix">
			<td class="title_2">������ֵ�Ĵ��������澯��</td>
			<td colspan="3">
				<input type="text" name="warningnum" value="3">
			</td>
		</tr>
		<tr name="tr_fix">
			<td class="title_2">������ֵ�澯ʱ�ĸ澯����</td>
			<td>
				<select name="warninglevel">
					<option value="0">��ѡ��</option>
					<option value="1">������־</option>
					<option value="2">��ʾ�澯</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
				</select>
			</td>
			<td class="title_2">�ָ��澯����</td>
			<td>
				<select name="reinstatelevel">
					<option value="0">��ѡ��</option>
					<option value="1">������־</option>
					<option value="2">��ʾ�澯</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
				</select>
			</td>
		</tr>
		<!-- *********************************************��̬��ֵ����********************************** -->
		<tr name="tr_active" style="display:none;">
			<td class="title_1" colspan="4"><input type="checkbox" id="c_usedynamic"><label for="c_usedynamic">���ö�̬��ֵ�澯</label></td>
		</tr>
		<tr name="tr_active" style="display:none;">
			<td class="title_2">������̬��ֵ�İٷֱ�(%)</td>
			<td>
				<input type="text" name="overper" readonly class="onread"><label class="l_red">(0-100%)</label>
			</td>
			<td class="title_2">�����ٷֱȴ���(���澯)</td>
			<td>
				<input type="text" name="overnum" readonly class="onread">
			</td>
		</tr>
		<tr name="tr_active" style="display:none;">
			<td class="title_2">���ɶ�̬��ֵһ������(��)</td>
			<td colspan="3">
				<input type="text" name="com_day" readonly class="onread"><label class="l_red">(������Ϊ�Ƚϱ�׼��ֵ�����������Ϊ3��)</label>
			</td>
		</tr>
		<tr name="tr_active" style="display:none;">
			<td class="title_2">������̬��ֵ�澯ʱ�ĸ澯����</td>
			<td>
				<select name="overlevel" disabled="disabled">
					<option value="0">��ѡ��</option>
					<option value="1">������־</option>
					<option value="2">��ʾ�澯</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
				</select>
			</td>
			<td class="title_2">�����ָ��澯ʱ�ļ���</td>
			<td>
				<select name="reinoverlevel" disabled="disabled">
					<option value="0">��ѡ��</option>
					<option value="1">������־</option>
					<option value="2">��ʾ�澯</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
				</select>
			</td>
		</tr>
		<!-- ************************************ͻ�䷧ֵ����****************************** -->
		<tr name="tr_sudden" style="display:none;">
			<td colspan="4" class="title_1">
				<input type="checkbox" name="intbflag_bak" id="c_intbflag"><label for="c_intbflag">������������ͻ��澯����</label>
				<input type="hidden" name="intbflag" value="0">
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td class="title_2">�������ʱ仯����ֵ(%)</td>
			<td>
				<input type="text" name="ifinoctets" readonly class="onread">
			</td>
			<td class="title_2">��������ͻ��澯������</td>
			<td>
				<select name="inoperation" disabled="disabled">
					<option value="1">></option>
					<option value="2"><</option>
					<option value="3">=</option>
				</select>
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td class="title_2">��������ͻ��澯����</td>
			<td>
				<select name="inwarninglevel" disabled="disabled">
					<option value="0">��ѡ��</option>
					<option value="1">������־</option>
					<option value="2">��ʾ�澯</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
				</select>
			</td>
			<td class="title_2">�������ʻָ�ͻ��澯����</td>
			<td>
				<select name="inreinstatelevel" disabled="disabled">
					<option value="0">��ѡ��</option>
					<option value="1">������־</option>
					<option value="2">��ʾ�澯</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
				</select>
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td colspan="4" class="title_1">
				<input type="checkbox" name="outtbflag_bak" id="c_outtbflag"><label for="c_outtbflag">������������ͻ��澯����</label>
				<input type="hidden" name="outtbflag" value="0">
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td class="title_2">�������ʱ仯����ֵ(%)</td>
			<td>
				<input type="text" name="ifoutoctets" readonly="readonly" class="onread">
			</td>
			<td class="title_2">��������ͻ��澯������</td>
			<td>
				<select name="outoperation" disabled="disabled">
					<option value="1">></option>
					<option value="2"><</option>
					<option value="3">=</option>
				</select>
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td class="title_2">��������ͻ��澯����</td>
			<td>
				<select name="outwarninglevel" disabled="disabled">
					<option value="0">��ѡ��</option>
					<option value="1">������־</option>
					<option value="2">��ʾ�澯</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
				</select>
			</td>
			<td class="title_2">�������ʻָ�ͻ��澯����</td>
			<td>
				<select name="outreinstatelevel" disabled="disabled">
					<option value="0">��ѡ��</option>
					<option value="1">������־</option>
					<option value="2">��ʾ�澯</option>
					<option value="3">һ��澯</option>
					<option value="4">���ظ澯</option>
					<option value="5">�����澯</option>
				</select>
			</td>
		</tr>
	</table>
</body>
</html>

