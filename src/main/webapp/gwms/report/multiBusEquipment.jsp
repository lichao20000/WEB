<%@ include file="../../timelater.jsp"%>
<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<link rel="stylesheet" href="<s:url value="/css/css_blue.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css/css_green.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/My97DatePicker/WdatePicker.js"/>"></script>
<script type="text/javascript" src="<s:url value="/Js/edittable.js"/>"></script>
<%
String isJs= LipossGlobals.getLipossProperty("InstArea.ShortName");
%>
<script type="text/javascript">
	$(function() {
        var starttime = '<s:property value='starttime'/>';
        var endtime = '<s:property value='endtime'/>';

        var servTypeIdString = '<s:property value='servTypeIdString'/>';

        if(servTypeIdString != "undefined" && servTypeIdString != null && servTypeIdString != ""){
            var servTypeIdList = servTypeIdString.split(",");
            for (var i=0; i<servTypeIdList.length; i++) {
                $("input[name='servTypeId']").each(function(){
                    if($(this).val()==servTypeIdList[i]){
                        $(this).attr("checked","checked");
                    }
                })
            }
        }


    });

    function checkForm(){
        var obj = document.frm.servTypeId;

        var servTypeIdString="";
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked) {
                servTypeIdString = servTypeIdString + "," + obj[i].value;
            }
        }

        if (servTypeIdString == "") {
            alert("请选择业务类型！");
            return false;
        }

        // 截取掉第一个逗号
        servTypeIdString = servTypeIdString.substring(1);
        $("input[@name='servTypeIdString']").val(servTypeIdString);
        return true;
    }

	function ToExcel(servTypeIdString) {
		var page="<s:url value='/gwms/report/multiBusEquipment!getMultiBusEquipmentExcel.action'/>?"+ "servTypeIdString=" + servTypeIdString;
		document.all("childFrm").src=page;
	}

	function do_test() {
        if (!checkForm()) {
            return false;
		}
		$("input[@name='button']").attr("disabled", true);
		frm.submit();
	}

	function getMultiBusEquipmentDetail(city_id, servTypeIdString) {
		var page="<s:url value='/gwms/report/multiBusEquipment!queryMultiBusEquipmentDetailList.action'/>?" + "cityId=" + city_id + "&servTypeIdString=" + servTypeIdString;
		window.open(page,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");
	}
</script>

<form name="frm"
	action="<s:url value='/gwms/report/multiBusEquipment!getMultiBusEquipmentList.action"'/>"
	method="POST">
<table width="98%" border=0 cellspacing=0 cellpadding=0 align="center">
	<tr>
		<td height="20"></td>
	</tr>
	<TR>
		<TD>
		<TABLE width="100%" height="30" border="0" cellspacing="0"
			cellpadding="0" class="green_gargtd">
			<TR>
				<TD width="164" align="center" class="title_bigwhite">多个业务统计设备资源报表查询</TD>
				<td>&nbsp; <img src="<s:url value="/images/attention_2.gif"/>"
					width="15" height="12"> &nbsp; 时间范围确定所要查询的设备。</td>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#999999>
		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
			<tr>
				<th colspan="4">多个业务统计设备资源报表查询统计</th>
			</tr>
		</TABLE>


		<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">

			<TR bgcolor=#ffffff>
				<%--<TD class=column width="15%" align='right'>属地</TD>
				<TD width="35%" colspan="3"><s:select list="cityList" name="city_id"
													  headerKey="00" headerValue="请选择属地" listKey="city_id"
													  listValue="city_name" cssClass="bk"></s:select></TD>--%>

				<TD class=column width="15%" align='right'>
					业务类型
				</TD>
				<TD width="35%">
					<INPUT TYPE="checkbox" NAME="servTypeId" value="10">宽带上网&nbsp;
					<INPUT TYPE="checkbox" NAME="servTypeId" value="11">IPTV&nbsp;
					<INPUT TYPE="checkbox" NAME="servTypeId" value="14">VoIP&nbsp;

					<input type="hidden" name="servTypeIdString" value=""/>
				</TD>


				<%--<td class="column" width='25%' align="left">开通时间</td>
				<td width='25%' align="left"><input type="text"
													name="starttime" class='bk' readonly
													value="<s:property value='starttime'/>"> <img
						name="shortDateimg"
						onClick="WdatePicker({el:document.frm.starttime, dateFmt:'yyyy-MM-dd', skin:'whyGreen'})"
						src="../../images/dateButton.png" width="15" height="12" border="0"
						alt="选择"></td>
				<td class="column" width='25%' align="left"> —— </td>
				<td width='25%' align="left"><input type="text" name="endtime"
													class='bk' readonly value="<s:property value='endtime'/>">
					<img name="shortDateimg"
						 onClick="WdatePicker({el:document.frm.endtime, dateFmt:'yyyy-MM-dd', skin:'whyGreen'})"
						 src="../../images/dateButton.png" width="15" height="12" border="0"
						 alt="选择"></td>--%>

			</TR>


			<%--<TR bgcolor=#ffffff id="timeSearch">

			</TR>--%>

			<TR>
				<td class="green_foot" colspan="4" align="right">
					<input class=jianbian name="button" type="button" onclick="do_test();" value=" 查 询 ">
					<%--<INPUT TYPE="button" value=" 导 出 " class=jianbian onclick="ToExcel()">--%>
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	<tr>
		<td bgcolor=#ffffff>&nbsp;</td>
	</tr>

	<s:if test="data==null">
		<!-- 第一次入采集，不做任何查询 -->
	</s:if>
	<s:else>
			<tr>
				<td>
				<table width="100%" border=0 cellspacing=1 cellpadding=2
					bgcolor=#999999 id=userTable>
					<tr>
						<td class="green_title" align='center' style="display: none" >city_id</td>
						<td class="green_title" align='center' width="5%">属地</td>
						<td class="green_title" align='center' width="5%">设备数量</td>
					</tr>

					<s:if test="data.size()>0">
						<s:iterator value="data">
							<tr bgcolor="#ffffff">
								<td class=column nowrap align="center" style="display: none"><s:property value="cityId" /></td>
								<td class=column nowrap align="center"><s:property value="cityName" /></td>

								<td class=column nowrap align="center">
									<a href="javascript:getMultiBusEquipmentDetail('<s:property value="cityId" />', '<s:property value="servTypeIdString"/>');">
										<s:property value="deviceNum" />
									</a>
								</td>
							</tr>
						</s:iterator>
					</s:if>

					<s:else>
						<tr>
							<td colspan=19 align=left class=column>系统没有相关的设备资源信息!</td>
						</tr>
					</s:else>

				</table>
				</td>
			</tr>

	</s:else>

    <tfoot>
        <tr>
            <td colspan="10">
                <IMG SRC="<s:url value="/images/excel.gif"/>" BORDER='0' ALT='导出列表'
                     style='cursor: hand'
                     onclick="ToExcel('<s:property value="servTypeIdString"/>')">
            </td>
        </tr>

    </tfoot>

	<TR>
		<TD HEIGHT=20 align="center">
		<div id="_process"></div>
		</TD>
	</TR>

    <tr STYLE="display: none">
        <td colspan="5">
            <iframe id="childFrm" src=""></iframe>
        </td>
    </tr>
</table>
</form>

<%@ include file="../foot.jsp"%>
