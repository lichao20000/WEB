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
 * 性能配置详细信息
 * REQ:XXXX-XXXX-XXXX
 * @author BENYP(5260) E-Mail:benyp@lianchuang.com
 * @version 1.0
 * @since 2008-10-6 PM 03:40:48
 *
 * @版权 南京联创 网管产品部
 *
 */
 --%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能配置详细信息</title>
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
				alert("编辑成功！");
				window.location="<s:url value="/performance/configPmee!showDetail.action"/>?expressionid=<s:property value="expressionid"/>&device_id=<s:property value="device_id"/>";
			}else{
				alert("编辑失败！");
			}
		}
		//点击固定阈值配置
		$("td[@name='td_fix']").click(function(){
			$.showHide(1);
		});
		//点击动态阈值配置
		$("td[@name='td_active']").click(function(){
			$.showHide(2);
		});
		//点击突变阈值配置
		$("td[@name='td_sudden']").click(function(){
			$.showHide(3);
		});
	});
	//显示修改实例项
	function Modify(id,intodb,mintype,minthres,mindesc,mincount,minwarninglevel,minreinstatelevel,
					maxtype,maxthres,maxdesc,maxcount,maxwarninglevel,maxreinstatelevel,
					dynatype,beforeday,dynathres,dynacount,dynadesc,dynawarninglevel,dynareinstatelevel,
					mutationtype,mutationthres,mutationcount,mutationdesc,mutationwarninglevel){
		$("input[@name='id']").val(id);
		$("#tab_warn").show();
		//****************************是否入库***************************//
		$("select[@name='intodb'] option").each(function(){
			if($(this).val()==intodb){
				$(this).attr("selected",true);
			}
		});
		//****************************固定阀值配置***********************************
		$("select[@name='mintype']").attr("selectedIndex",mintype);//比较操作符一:
		$("input[@name='minthres']").val(minthres);//固定阈值一
		$("input[@name='mindesc']").val(mindesc);//固定阈值一描述
		$("input[@name='mincount']").val(mincount);//连续超出阀值一(次)
		$("select[@name='minwarninglevel']").attr("selectedIndex",minwarninglevel);//发出告警级别:
		$("select[@name='minreinstatelevel']").attr("selectedIndex",minreinstatelevel);//恢复告警级别:

		$("select[@name='maxtype']").attr("selectedIndex",maxtype);//比较操作符二:
		$("input[@name='maxthres']").val(maxthres);//固定阀值二:
		$("input[@name='maxdesc']").val(maxdesc);//固定阀值二描述:
		$("input[@name='maxcount']").val(maxcount);//连续超出阀值二(次)
		$("select[@name='maxwarninglevel']").attr("selectedIndex",maxwarninglevel);//发出告警级别:
		$("select[@name='maxreinstatelevel']").attr("selectedIndex",maxreinstatelevel);//恢复告警级别:

		//*****************************动态阀值配置************************************
		$("select[@name='dynatype']").attr("selectedIndex",dynatype);
		$("input[@name='beforeday']").val(beforeday);//数据的基准值（前几天）
		$("input[@name='dynathres']").val(dynathres);//阀值百分比（％）
		$("input[@name='dynacount']").val(dynacount);//达到阀值百分比（次）
		$("input[@name='dynadesc']").val(dynadesc);//动态阀值描述
		$("select[@name='dynawarninglevel']").attr("selectedIndex",dynawarninglevel);//发出告警级别:
		$("select[@name='dynareinstatelevel']").attr("selectedIndex",dynareinstatelevel);//恢复告警级别

		//******************************突变阀值配置**********************************
		$("select[@name='mutationtype']").attr("selectedIndex",mutationtype);//突变阀值操作符
		$("input[@name='mutationthres']").val(mutationthres);//超出百分比（％）
		$("input[@name='mutationcount']").val(mutationcount);//达到阀值百分比（次）
		$("input[@name='mutationdesc']").val(mutationdesc);//突变阀值描述
		$("select[@name='mutationwarninglevel']").attr("selectedIndex",mutationwarninglevel);//发出告警级别
	}
	//删除实例
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
					alert("删除成功！");
					target.parent().parent().remove();
				}else{
					alert("删除失败，请重试!");
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
					<th colspan="4">性能配置详细信息</th>
				</tr>
				<tr>
					<th nowrap>表达式名称</th>
					<th nowrap>实例描述</th>
					<th nowrap>索引</th>
					<th nowrap>操作</th>
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
							);">&nbsp;修改&nbsp;</a>&nbsp;|&nbsp;
							<a href="#" onclick="Del('<s:property value="#data.id"/>',
							                         '<s:property value="device_id"/>',
							                         '<s:property value="expressionid"/>',$(this));">&nbsp;删除&nbsp;</a>
						</td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
		<br>
		<table align="center" width="94%" class="querytable" id="tab_warn" style="display:none;">
			<tr>
				<td width="20%" class="title_2">原始数据是否入库:</td>
				<td colspan="4">
					<select name="intodb">
						<option value="1">是</option>
						<option value="0" selected>否</option>
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
                      		>固定阀值配置</td>
							<td class="mouseout" name="td_active"
								onMouseOver="this.className='mouseon';"
								onMouseOut="this.className='mouseout';"
                      		>动态阀值配置</td>
							<td class="mouseout" name="td_sudden"
								onMouseOver="this.className='mouseon';"
								onMouseOut="this.className='mouseout';"
                      		>突变阀值配置</td>
						</tr>
					</table>
				</td>
			</tr>
			<!-- *******************************************以下是固定阈值配置项************************************ -->
			<tr name="tr_fix">
				<td class="title_2">比较操作符一:</td>
				<td>
					<select name="mintype">
						<option value="0" selected>不使用</option>
						<option value="1">大于</option>
						<option value="2">大于等于</option>
						<option value="3">小于</option>
						<option value="4">小于等于</option>
						<option value="5">等于</option>
						<option value="6">不等于</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">固定阀值一:</td>
				<td>
					<input type="text" name="minthres">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">固定阀值一描述:</td>
				<td colspan="4">
					<input type="text" size="58" name="mindesc">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">连续超出阀值一(次)</td>
				<td>
					<input type="text" name="mincount" value="1">
				</td>
				<td colspan="3">(发出告警)</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">发出告警级别:</td>
				<td>
					<!--<select name="minwarninglevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="minwarninglevel" theme="simple" headerKey="-1" headerValue="请选择"/>
			       </td>
				<td class="title_2"></td>
				<td class="title_2">恢复告警级别:</td>
				<td>
					<select name="minreinstatelevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">比较操作符二:</td>
				<td>
					<select name="maxtype">
						<option value="0" selected>不使用</option>
						<option value="1">大于</option>
						<option value="2">大于等于</option>
						<option value="3">小于</option>
						<option value="4">小于等于</option>
						<option value="5">等于</option>
						<option value="6">不等于</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">固定阀值二:</td>
				<td>
					<input type="text" name="maxthres">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">固定阀值二描述:</td>
				<td colspan="4">
					<input type="text" size="58" name="maxdesc">
				</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">连续超出阀值二(次)</td>
				<td>
					<input type="text" name="maxcount" value="1">
				</td>
				<td colspan="3">(发出告警)</td>
			</tr>
			<tr name="tr_fix">
				<td class="title_2">发出告警级别:</td>
				<td>
					<!--<select name="maxwarninglevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxwarninglevel" theme="simple" headerKey="-1" headerValue="请选择"/>
			    </td>
				<td class="title_2"></td>
				<td class="title_2">恢复告警级别:</td>
				<td>
					<!--<select name="maxreinstatelevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
					<s:select list="#request.warnMap" listKey="key" listValue="value" name="maxreinstatelevel" theme="simple" headerKey="-1" headerValue="请选择"/>

				</td>
			</tr>
			<!-- *******************************************以下是动态阈值配置项************************************ -->
			<tr name="tr_active">
				<td class="title_2">动态阀值操作符:</td>
				<td>
					<select name="dynatype">
						<option value="0" selected>不使用</option>
						<option value="1">变化率大于</option>
						<option value="2">变化率大于等于</option>
						<option value="3">变化率小于</option>
						<option value="4">变化率小于等于</option>
						<option value="5">变化率等于</option>
						<option value="6">变化率不等于</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">数据的基准值（前几天）:</td>
				<td>
					<input type="text" name="beforeday" value="1">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">阀值百分比（％）:</td>
				<td colspan="4">
					<input type="text" size="58" name="dynathres">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">达到阀值百分比（次）</td>
				<td>
					<input type="text" name="dynacount" value="1">
				</td>
				<td colspan="3">(发出告警)</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">动态阀值描述:</td>
				<td colspan="4">
					<input type="text" size="58" name="dynadesc">
				</td>
			</tr>
			<tr name="tr_active">
				<td class="title_2">发出告警级别:</td>
				<td>
					<!--<select name="dynawarninglevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynawarninglevel" theme="simple" headerKey="-1" headerValue="请选择"/>
			   </td>
				<td class="title_2"></td>
				<td class="title_2">恢复告警级别:</td>
				<td>
					<!--<select name="dynareinstatelevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="dynareinstatelevel" theme="simple" headerKey="-1" headerValue="请选择"/>
			   </td>
			</tr>
			<tr name="tr_active">
				<td colspan="5" class="foot">备注：  变化率 ＝ （（采到的值－基准值）/基准值） * 100</td>
			</tr>
			<!-- *******************************************以下是突变阈值配置项************************************ -->
			<tr name="tr_sudden">
				<td class="title_2">突变阀值操作符:</td>
				<td>
					<select name="mutationtype">
						<option value="0" selected>不使用</option>
						<option value="1">变化率绝对值大于</option>
						<option value="2">变化率绝对值大于等于</option>
						<option value="3">变化率绝对值小于</option>
						<option value="4">变化率绝对值小于等于</option>
						<option value="5">变化率绝对值等于</option>
						<option value="6">变化率绝对值不等于</option>
					</select>
				</td>
				<td class="title_2"></td>
				<td class="title_2">超出百分比（％）:</td>
				<td>
					<input type="text" name="mutationthres">
				</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">达到阀值百分比（次）</td>
				<td>
					<input type="text" name="mutationcount" value="1">
				</td>
				<td colspan="3">(发出告警)</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">突变阀值描述:</td>
				<td colspan="4">
					<input type="text" size="58" name="mutationdesc">
				</td>
			</tr>
			<tr name="tr_sudden">
				<td class="title_2">发出告警级别:</td>
				<td colspan="4">
					<!--<select name="mutationwarninglevel">
						<option value="0" selected>请选择</option>
						<option value="1">正常日志</option>
						<option value="2">提示告警</option>
						<option value="3">一般告警</option>
						<option value="4">严重告警</option>
						<option value="5">紧急告警</option>
					</select>-->
				<s:select list="#request.warnMap" listKey="key" listValue="value" name="mutationwarninglevel" theme="simple" headerKey="-1" headerValue="请选择"/>
			    </td>
			</tr>
			<tr name="tr_sudden">
				<td colspan="5" class="foot">备注：  变化率绝对值 ＝ |（（采到的值－上次采集值）/上次采集值） * 100|</td>
			</tr>
			<!-- *************************************************END******************************************** -->
			<tr>
				<td colspan="5" class="foot" align="center">
					<input type="hidden" name="id">
					<input type="hidden" name="device_id" value="<s:property value="device_id"/>">
					<input type="hidden" name="expressionid" value="<s:property value="expressionid"/>">
					<button  type="submit" onclick="return CheckForm();">&nbsp;保&nbsp;存&nbsp;</button>&nbsp;&nbsp;
					<button onclick="window.close();">&nbsp;关&nbsp;闭&nbsp;</button>
				</td>
			</tr>
		</table>
		<br>
	</form>
</body>
</html>
