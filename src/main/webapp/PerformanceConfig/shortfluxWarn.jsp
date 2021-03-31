<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%> 
<%--
/**
 * 流量配置告警页面【通用告警页面，告警字段没有江苏的多】
 * 
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-24 PM 04:43:01
 * 
 * @版权 南京联创 网管产品部;
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
							onMouseOut="this.className='mouseon';">固定阀值告警配置</td>
						<td class="mouseout" name="td_active"
							onclick="$.showHide(2)"
							onMouseOver="this.className='mouseon';"
							onMouseOut="this.className='mouseout';">动态阀值告警配置</td>
						<td class="mouseout" name="td_sudden"
							onclick="$.showHide(3)"
							onMouseOver="this.className='mouseon';"
							onMouseOut="this.className='mouseout';">突变阀值告警配置</td>
					</tr>
				</table>
			</td>
		</tr>
		<!-- ****************************固定阀值设置*********************************** -->
		<tr name="tr_fix">
			<td class="title_2" width="20%"><input type="checkbox" id="c_inmax"><label for="c_inmax">端口流入带宽利用率阈值(%)</label></td>
			<td width="30%">
				<input type="text" name="ifinoctetsbps_max" readonly class="onread">
			</td>
			<td class="title_2" width="20%"><input type="checkbox" id="c_outmax"><label for="c_outmax">端口流出带宽利用率阈值(%)</label></td>
			<td width="30%">
				<input type="text" name="ifoutoctetsbps_max" readonly class="onread">
			</td>
		</tr>
		<tr name="tr_fix">
			<td class="title_2"><input type="checkbox" id="c_disinmax"><label for="c_disinmax">端口流入丢包率阈值(%)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			<td>
				<input type="text" name="ifindiscardspps_max" readonly class="onread">
			</td>
			<td class="title_2"><input type="checkbox" id="c_disoutmax"><label for="c_disoutmax">端口流出丢包率阈值(%)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			<td>
				<input type="text" name="ifoutdiscardspps_max" readonly class="onread">
			</td>
		</tr>
		<tr name="tr_fix">
			<td class="title_2"><input type="checkbox" id="c_errinmax"><label for="c_errinmax">端口流入错包率阈值(%)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			<td>
				<input type="text" name="ifinerrorspps_max" readonly class="onread">
			</td>
			<td class="title_2"><input type="checkbox" id="c_erroutmax"><label for="c_erroutmax">端口流出错包率阈值(%)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label></td>
			<td>
				<input type="text" name="ifouterrorspps_max" readonly class="onread">
			</td>
		</tr>
		<tr name="tr_fix">
			<td class="title_2">超出阈值的次数（发告警）</td>
			<td colspan="3">
				<input type="text" name="warningnum" value="3">
			</td>
		</tr>
		<tr name="tr_fix">
			<td class="title_2">发出阈值告警时的告警级别</td>
			<td>
				<select name="warninglevel">
					<option value="0">请选择</option>
					<option value="1">正常日志</option>
					<option value="2">提示告警</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
				</select>
			</td>
			<td class="title_2">恢复告警级别</td>
			<td>
				<select name="reinstatelevel">
					<option value="0">请选择</option>
					<option value="1">正常日志</option>
					<option value="2">提示告警</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
				</select>
			</td>
		</tr>
		<!-- *********************************************动态阀值设置********************************** -->
		<tr name="tr_active" style="display:none;">
			<td class="title_1" colspan="4"><input type="checkbox" id="c_usedynamic"><label for="c_usedynamic">启用动态阈值告警</label></td>
		</tr>
		<tr name="tr_active" style="display:none;">
			<td class="title_2">超出动态阈值的百分比(%)</td>
			<td>
				<input type="text" name="overper" readonly class="onread"><label class="l_red">(0-100%)</label>
			</td>
			<td class="title_2">超出百分比次数(发告警)</td>
			<td>
				<input type="text" name="overnum" readonly class="onread">
			</td>
		</tr>
		<tr name="tr_active" style="display:none;">
			<td class="title_2">生成动态阈值一的天数(天)</td>
			<td colspan="3">
				<input type="text" name="com_day" readonly class="onread"><label class="l_red">(用来做为比较标准阈值的天数，最大为3天)</label>
			</td>
		</tr>
		<tr name="tr_active" style="display:none;">
			<td class="title_2">发出动态阈值告警时的告警级别</td>
			<td>
				<select name="overlevel" disabled="disabled">
					<option value="0">请选择</option>
					<option value="1">正常日志</option>
					<option value="2">提示告警</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
				</select>
			</td>
			<td class="title_2">发出恢复告警时的级别</td>
			<td>
				<select name="reinoverlevel" disabled="disabled">
					<option value="0">请选择</option>
					<option value="1">正常日志</option>
					<option value="2">提示告警</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
				</select>
			</td>
		</tr>
		<!-- ************************************突变阀值设置****************************** -->
		<tr name="tr_sudden" style="display:none;">
			<td colspan="4" class="title_1">
				<input type="checkbox" name="intbflag_bak" id="c_intbflag"><label for="c_intbflag">启用流入流量突变告警配置</label>
				<input type="hidden" name="intbflag" value="0">
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td class="title_2">流入速率变化率阈值(%)</td>
			<td>
				<input type="text" name="ifinoctets" readonly class="onread">
			</td>
			<td class="title_2">流入速率突变告警操作符</td>
			<td>
				<select name="inoperation" disabled="disabled">
					<option value="1">></option>
					<option value="2"><</option>
					<option value="3">=</option>
				</select>
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td class="title_2">流入速率突变告警级别</td>
			<td>
				<select name="inwarninglevel" disabled="disabled">
					<option value="0">请选择</option>
					<option value="1">正常日志</option>
					<option value="2">提示告警</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
				</select>
			</td>
			<td class="title_2">流入速率恢复突变告警级别</td>
			<td>
				<select name="inreinstatelevel" disabled="disabled">
					<option value="0">请选择</option>
					<option value="1">正常日志</option>
					<option value="2">提示告警</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
				</select>
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td colspan="4" class="title_1">
				<input type="checkbox" name="outtbflag_bak" id="c_outtbflag"><label for="c_outtbflag">启用流出流量突变告警配置</label>
				<input type="hidden" name="outtbflag" value="0">
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td class="title_2">流出速率变化率阈值(%)</td>
			<td>
				<input type="text" name="ifoutoctets" readonly="readonly" class="onread">
			</td>
			<td class="title_2">流出速率突变告警操作符</td>
			<td>
				<select name="outoperation" disabled="disabled">
					<option value="1">></option>
					<option value="2"><</option>
					<option value="3">=</option>
				</select>
			</td>
		</tr>
		<tr name="tr_sudden" style="display:none;">
			<td class="title_2">流出速率突变告警级别</td>
			<td>
				<select name="outwarninglevel" disabled="disabled">
					<option value="0">请选择</option>
					<option value="1">正常日志</option>
					<option value="2">提示告警</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
				</select>
			</td>
			<td class="title_2">流出速率恢复突变告警级别</td>
			<td>
				<select name="outreinstatelevel" disabled="disabled">
					<option value="0">请选择</option>
					<option value="1">正常日志</option>
					<option value="2">提示告警</option>
					<option value="3">一般告警</option>
					<option value="4">严重告警</option>
					<option value="5">紧急告警</option>
				</select>
			</td>
		</tr>
	</table>
</body>
</html>

