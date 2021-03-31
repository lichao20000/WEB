<%--
Author: 王森博
Date: 2010-01-06
--%>
<%@ page contentType="text/html;charset=gbk"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/jquery.blockUI.js"/>"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"
	SRC="<s:url value="/Js/CheckFormForm.js"/>"></SCRIPT>
<script type="text/javascript"
	src="<s:url value="/Js/jsDate/WdatePicker.js"/>"></script>
<link rel="stylesheet" href="<s:url value='/css/css_green.css'/>"
	type="text/css">
<SCRIPT LANGUAGE="JavaScript">

//配置VLAN
function configVlan(){
if(checkForm() == false)return false;
    var cityId = $.trim($("select[@name='cityId']").val());
    var minVlanid = $.trim($("input[@name='minVlanid']").val());
    var maxVlanid = $.trim($("input[@name='maxVlanid']").val());
    var basIp = $.trim($("input[@name='basIp']").val());
    var id = $.trim($("input[@name='id']").val());
    var type = $.trim($("input[@name='type']").val());
	var url = '<s:url value='/gwms/resource/iptvVlanManage!configVlan.action'/>';
	$.post(url,{
		cityId:cityId,
		minVlanid:minVlanid,
		maxVlanid:maxVlanid,
		basIp:basIp,
		id:id,
		type:type
	},function(ajax){
	    if(ajax=="11"){
	    	alert("新增成功!");
	    	window.location.reload();
	    }
	    if(ajax=="10"){
	    	alert("新增失败!");
	    }
	    if(ajax=="21"){
	    	alert("修改成功!");
	    	window.location.reload();
	    }
	    if(ajax=="20"){
	    	alert("修改失败!");
	    }
	    if(ajax=="00"){
	    	alert("处理类型出错！");
	    }
	});
}

function clearResult() {
	document.all("div_config").style.display = "none";
}

function showVlan(city_id,min_vlanid,max_vlanid,bas_ip,id){
    $("tr[@name='vlan']").show();
    $("input[@name='minVlanid']").val(min_vlanid);
    $("input[@name='maxVlanid']").val(max_vlanid);
    $("input[@name='basIp']").val(bas_ip);
    $("input[@name='id']").val(id);
    $("select[@name='cityId']").attr("value",city_id);
    $("input[@name='type']").val("2");
    $("select[@name='cityId']").attr("disabled",true);
}

function addVlan(){
    $("tr[@name='vlan']").show();
    $("input[@name='minVlanid']").val("");
    $("input[@name='maxVlanid']").val("");
    $("input[@name='basIp']").val("");
    $("select[@name='cityId']").attr("value","-1");
    $("input[@name='type']").val("1");
    $("input[@name='id']").val("");
    $("select[@name='cityId']").attr("disabled",false);
}

function delVlan(id,cityName){
    if (!confirm("请确认是否要删除"+cityName+"的VLAN值！")){
		return false;
	}
	var url = '<s:url value='/gwms/resource/iptvVlanManage!delVlan.action'/>';
	$.post(url,{
		id:id
	},function(ajax){
	    	alert(ajax);
	    	window.location.reload();
	});
}

function checkForm(){
     var cityId = $.trim($("select[@name='cityId']").val());
     var minVlanid = $.trim($("input[@name='minVlanid']").val());
     var maxVlanid = $.trim($("input[@name='maxVlanid']").val());
     var basIp = $.trim($("input[@name='basIp']").val());

     if(cityId == "-1"){
         alert("请选择属地");
         return false;
     }

     if(minVlanid == ""){
         alert("请输入最小VLAN");
         document.frm.minVlanid.focus();
         return false;
     }
     if(!IsNumber2(minVlanid,"最小VLAN")){
     	document.frm.minVlanid.focus();
        return false;
     }
     if(maxVlanid == ""){
         alert("请输入最大VLAN");
         document.frm.maxVlanid.focus();
         return false;
     }
     if(!IsNumber2(maxVlanid,"最大VLAN")){
     	document.frm.maxVlanid.focus();
        return false;
     }
     if(basIp != ""){
         var basIps = basIp.split(",");
         for(j = 0 ; j<basIps.length ; j++){
         	if(!IsIPAddr2(basIps[j],"BAS地址")){
                   document.frm.basIp.focus();
                   return false;
             }
         }
     }

           return true;
}

//-->
</SCRIPT>
<TABLE border=0 cellspacing=0 cellpadding=0 width="98%" align="center">
	<tr>
		<td>
			<table width="100%" height="30" border="0" cellspacing="0"
				cellpadding="0" class="green_gargtd">
				<tr>
					<td width="162" align="center" class="title_bigwhite"
						ondblclick="ShowHideLog()">
						IPTV考核VLAN管理
					</td>
					<td>
						<img src="<s:url value="/images/attention_2.gif"/>" width="15"
							height="12" />
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<TR>
		<TD>
			<FORM NAME="frm" METHOD="post" action="">
				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="text">
					<TR bgcolor="#FFFFFF" STYLE="display: ">
						<TH>
							IPTV考核标准VLAN管理
							<input type="hidden" name="type" value="">
						</TH>
					</TR>
					<tr align="left" id="trnet" STYLE="display: ">
						<td bgcolor=#999999>
							<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
								<tr align="center" bgcolor="#FFFFFF">
									<TD class=green_title align="center">
										属地
									</TD>
									<TD class=green_title align="center">
										最小VLAN
									</TD>
									<TD class=green_title align="center">
										最大VLAN
									</TD>
									<TD class=green_title align="center">
										BAS地址
									</TD>
									<TD class=green_title align="center">
										更新时间
									</TD>
									<TD class=green_title align="center">
										操作
									</TD>
								</tr>
								<s:if test="cityVlanList!=null">
									<s:iterator var="cityVlanList" value="cityVlanList">
										<tr align="center" bgcolor="#FFFFFF">
											<TD align="center">
												<s:property value="city_name" />
											</TD>
											<TD align="center">
												<s:if test="min_vlanid=='null'">
													-
												</s:if>
												<s:else>
													<s:property value="min_vlanid" />
												</s:else>
											</TD>
											<TD align="center">
												<s:if test="max_vlanid=='null'">
													-
												</s:if>
												<s:else>
													<s:property value="max_vlanid" />
												</s:else>
											</TD>
											<TD align="center">
												<s:if test="bas_ip=='null'">
													-
												</s:if>
												<s:else>
													<s:property value="bas_ip" />
												</s:else>
											</TD>
											<TD align="center">
												<s:if test="updatetime=='null'">
													-
												</s:if>
												<s:else>
													<s:property value="updatetime" />
												</s:else>
											</TD>
											<td align="center">

												<IMG SRC='../../images/edit.gif' BORDER='0' ALT='编辑'
													onclick="showVlan('<s:property value="city_id"/>','<s:property value="min_vlanid"/>',
                                                                            '<s:property value="max_vlanid"/>','<s:property value="bas_ip"/>',
                                                                            '<s:property value="id"/>')"
													style='cursor: hand'>
												<IMG SRC="<s:url value="/images/del.gif"/>" BORDER='0'
													ALT='删除'
													onclick="delVlan('<s:property value="id"/>','<s:property value="city_name" />')"
													style='cursor: hand'>
											</td>
										</tr>
									</s:iterator>
								</s:if>
								<s:else>
									<tr align="center" bgcolor="#FFFFFF">
										<TD colspan="6" align="center">
											<FONT color="red">没有标准VLAN值</FONT>
										</TD>
									</tr>
								</s:else>
								<TR bgcolor="#FFFFFF">
									<TD colspan="6" align="right" class="green_foot">
										<input type="button" value=" 增 加 " onclick="addVlan()"
											class="jianbian">
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
					<tr>
						<td height="20">
						</td>
					</tr>
					<tr name="vlan" style="display: none">
						<td>
							<TABLE width="100%" border=0 cellspacing=0 cellpadding=0
								align="center">
								<TR height=30px>
									<TH colspan="4" align="center">
										IPTV考核标准VLAN值设置
									</TH>
								</TR>
								<TR>
									<TD bgcolor=#999999>
										<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
											<TR bgcolor="#FFFFFF">
												<TD align="right" class="column" width="15%">
													属地
													<input type="hidden" name="id" value="">
												</TD>
												<TD colspan="3">
													<s:select list="cityList" name="cityId" headerKey="-1"
														headerValue="请选择属地" listKey="city_id"
														listValue="city_name" cssClass="bk"></s:select>
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD class=column align="right" width="15%">
													最小VLAN
												</TD>
												<TD width="35%">
													<input type="text" name="minVlanid" class='bk' value="">
												</TD>
												<TD class=column align="right" width="15%">
													最大VLAN
												</TD>
												<TD width="35%">
													<input type="text" name="maxVlanid" class='bk' value="">
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD class=column align="right" width="15%">
													BAS地址
												</TD>
												<TD colspan="3">
													<input type="text" name="basIp" class='bk' value=""
														size="60">
													没有可以为空，多个IP以“,”分隔
												</TD>
											</TR>
											<TR bgcolor="#FFFFFF">
												<TD colspan="4" align="right" class="green_foot">
													<input type="button" value=" 保 存 " onclick="configVlan();"
														class="jianbian">
												</TD>
											</TR>
										</TABLE>
									</TD>
								</TR>
							</TABLE>
						</td>
					</tr>
				</table>
			</FORM>
		</TD>
	</TR>
</TABLE>
