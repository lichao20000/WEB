<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>"
	type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>"
	type="text/css">
<script type="text/javascript" src="<s:url value="/Js/jquery.js"/>"></script>
<SCRIPT LANGUAGE="JavaScript" SRC="/Js/CheckFormForm.js"></SCRIPT>
<script type="text/javascript">
	function changeSelect() {
		$("tr[@id='vlanidDisplay']").css("display", "none");
		$("tr[@id='userDisplay']").css("display", "none");
		var servTypeId = $("select[@name='servTypeId']").val();
		$("input[name='sertype']").val(servTypeId);
		$("tr[@id='btn']").css("display", "");
		if ("10" == servTypeId) {
			$("tr[@id='inter']").css("display", "");
			$("tr[@id='itv']").css("display", "none");
			$("tr[@id='tianyi']").css("display", "none");
		}
		if ("11" == servTypeId) {
			$("tr[@id='itv']").css("display", "");
			$("tr[@id='inter']").css("display", "none");
			$("tr[@id='tianyi']").css("display", "none");
		}
		if ("25" == servTypeId) {
			$("tr[@id='tianyi']").css("display", "");
			$("tr[@id='inter']").css("display", "none");
			$("tr[@id='itv']").css("display", "none");
		}
	}

	function changeSelectType(num) {
		$("tr[@id='vlanidDisplay']").css("display", "none");
		$("tr[@id='userDisplay']").css("display", "none");
		var interservType = $("select[@name='interservType']").val();
		var itvservType = $("select[@name='itvservType']").val();
		var tianyiservType = $("select[@name='tianyiservType']").val();
		var leftnum = $("input[name='leftnum']").val();
		var rightnum = $("input[name='rightnum']").val();
		if ("1" == num) {
			if ("30" == interservType) {//工单
				if ("1" == leftnum) {
					$("td[@id='interData']").css("display", "");
					$("td[@id='interReal']").css("display", "none");
				} else {
					$("td[@id='choose']").css("display", "");
					$("td[@id='choose1']").css("display", "none");
					$("td[@id='interReal']").css("display", "none");
				}
			} else {//实际
				if ("1" == rightnum || "0" == rightnum) {
					$("td[@id='choose']").css("display", "none");
					$("td[@id='choose1']").css("display", "none");
					$("td[@id='interReal']").css("display", "");
					$("td[@id='interData']").css("display", "none");
				} else {
					$("td[@id='choose1']").css("display", "");
					$("td[@id='choose']").css("display", "none");
				}
			}
		}
		if ("2" == num) {
			if ("50" == itvservType) {
				$("td[@id='itvData']").css("display", "");
				$("td[@id='itvReal']").css("display", "none");
			} else {
				$("td[@id='itvReal']").css("display", "");
				$("td[@id='itvData']").css("display", "none");
			}
		}
		if ("3" == num) {
			if ("70" == tianyiservType) {
				$("td[@id='tianyiData']").css("display", "");
				$("td[@id='tianyiReal']").css("display", "none");
			} else {
				$("td[@id='tianyiReal']").css("display", "");
				$("td[@id='tianyiData']").css("display", "none");
			}
		}
	}

	function kuandaiChange(num) {
		if ("1" == num) {
			var choose_user = $("select[@name='choose_user']").val();
			if ("-1" != choose_user) {
				$("input[name='user_1']").val(choose_user);
				$("tr[@id='userDisplay']").css("display", "");
				$("tr[@id='vlanidDisplay']").css("display", "none");
				$("tr[@id='userDisplay']").html("");
				var user_val = $("input[name='" + choose_user + "']").val();
				$("tr[@id='userDisplay']").html(
						"<td colspan='3'>" + user_val + "</td>");
			}
		}
		if ("2" == num) {
			var choose_vlid = $("select[@name='choose_vlid']").val();
			if ("-1" != choose_vlid) {
				$("input[name='vlanid_1']").val(choose_vlid);
				$("tr[@id='vlanidDisplay']").css("display", "");
				$("tr[@id='userDisplay']").css("display", "none");
				$("tr[@id='vlanidDisplay']").html("");
				var user_val = $("input[name='" + choose_vlid + "']").val();
				$("tr[@id='vlanidDisplay']").html(
						"<td  colspan='3'>" + user_val + "</td>");
			}
		}
	}

	function update() {
		var tab = document.getElementById('tab');
		var num = tab.rows.length;

		var interfail = $("input[name='interfail']").val();
		var itvfail = $("input[name='itvfail']").val();
		var tianyifail = $("input[name='tianyifail']").val();
		var id = $("input[name='sertype']").val();
		var loid = $("input[name='loid']").val();
		var interservType = $("select[@name='interservType']").val();
		var itvservType = $("select[@name='itvservType']").val();
		var tianyiservType = $("select[@name='tianyiservType']").val();
		var targetvlan = $("input[name='vlanid_1']").val();
		var targetUser = $("input[name='user_1']").val();
		var interVal = "";
		var val = "";
		var itvVal = "";
		var tianyiVal = "";
		var oui = "";
		var devSn = "";
		var userid = "";
		var username = "";
		var deviceId = "";

		if (num > 1) {
			for ( var i = 0; i < num; i++) {
				$("input[name='interlanDis" + (i + 1) + "'][checked]").each(
						function() {
							interVal += this.value + "@";
						});
				$("input[name='interwlanDis" + (i + 1) + "'][checked]").each(
						function() {
							interVal += this.value + "@";
						});
				interVal+="#";
			}
		} else {
			$("input[name='interlanDis1'][checked]").each(function() {
				interVal += this.value + "@";
			});
			$("input[name='interwlanDis1'][checked]").each(function() {
				interVal += this.value + "@";
			});
		}

		$("input[name='itvlanDis'][checked]").each(function() {
			itvVal += this.value + "@";
		});
		$("input[name='itvwlanDis'][checked]").each(function() {
			itvVal += this.value + "@";
		});
		$("input[name='tianyilanDis'][checked]").each(function() {
			tianyiVal += this.value + "@";
		});
		$("input[name='tianyiwlanDis'][checked]").each(function() {
			tianyiVal += this.value + "@";
		});

		if ("10" == id) {
			//宽带
			if (num > 1) {
				if ("30" == interservType) {
					$("input[name=" + targetUser + "][checked]").each(
							function() {
								val += this.value + "@";
							});
				} else {
					//非工单
					if ("0" == interfail) {
						alert("宽带采集失败，请选择工单方式 ");
						return;
					}
					$("input[name=" + targetvlan + "][checked]").each(
							function() {
								val += this.value + "@";
							});
				}
				oui = $("input[name='interoui" + targetUser + "']").val();
				devSn = $("input[name='interDevsn" + targetUser + "']").val();
				userid = $("input[name='interuserid" + targetUser + "']").val();
				username = $("input[name='interusername" + targetUser + "']")
						.val();
				deviceId = $("input[name='interdeviceId" + targetUser + "']")
						.val();
			} else {
				if ("30" == interservType) {
					$("input[name='interlan'][checked]").each(function() {
						val += this.value + "@";
					});
					$("input[name='interwlan'][checked]").each(function() {
						val += this.value + "@";
					});
				} else {
					if ("0" == interfail) {
						alert("宽带采集失败，请选择工单方式 ");
						return;
					}
					$("input[name='laninterreal'][checked]").each(function() {
						val += this.value + "@";
					});
					$("input[name='wlaninterreal'][checked]").each(function() {
						val += this.value + "@";
					});
				}
				oui = $("input[name='interoui']").val();
				devSn = $("input[name='interDevsn']").val();
				userid = $("input[name='interuserid']").val();
				username = $("input[name='interusername']").val();
				deviceId = $("input[name='interdeviceId']").val();
			}
		}
		if ("11" == id) {
			if ("50" == itvservType) {
				$("input[name='itvlan'][checked]").each(function() {
					val += this.value + "@";
				});
				$("input[name='itvwlan'][checked]").each(function() {
					val += this.value + "@";
				});
			} else {
				if ("0" == itvfail) {
					alert("ITV采集失败，请选择工单方式");
					return;
				}
				$("input[name='lanitvreal'][checked]").each(function() {
					val += this.value + "@";
				});
				$("input[name='wlanitvreal'][checked]").each(function() {
					val += this.value + "@";
				});
			}
			oui = $("input[name='itvoui']").val();
			devSn = $("input[name='itvDevsn']").val();
			userid = $("input[name='itvuserid']").val();
			username = $("input[name='itvusername']").val();
			deviceId = $("input[name='itvdeviceId']").val();
		}
		if ("25" == id) {
			//TIANYI
			if ("70" == tianyiservType) {
				$("input[name='tianyilan'][checked]").each(function() {
					val += this.value + "@";
				});
				$("input[name='tianyiwlan'][checked]").each(function() {
					val += this.value + "@";
				});
			} else {
				if ("0" == tianyifail) {
					alert("天翼看店采集失败，请选择工单方式");
					return;
				}
				$("input[name='lantianyireal'][checked]").each(function() {
					val += this.value + "@";
				});
				$("input[name='wlantianyireal'][checked]").each(function() {
					val += this.value + "@";
				});
			}
			oui = $("input[name='tianyioui']").val();
			devSn = $("input[name='tianyiDevsn']").val();
			userid = $("input[name='tianyiuserid']").val();
			username = $("input[name='tianyiusername']").val();
			deviceId = $("input[name='tianyideviceId']").val();
		}
		var url = "<s:url value='/itms/service/manuallyconfigureportbusiness!update.action'/>";
		$.post(url, {
			id : id,
			loid : loid,
			val : val,
			userid : userid,
			username : username,
			interVal : interVal,
			itvVal : itvVal,
			tianyiVal : tianyiVal,
			oui : oui,
			deviceId : deviceId,
			devSn : devSn
		}, function(ajax) {
			alert(ajax);
		});
	}
</script>
<table class="listtable" id="listTable">
	<s:if test="userMap != null">
		<s:iterator value="userMap">
			<s:iterator>
				<input type="hidden" name="<s:property value="key"/>"
					value="<s:property value="value"/>">
			</s:iterator>
		</s:iterator>
	</s:if>
	<s:if test="vlanMap != null">
		<s:iterator value="vlanMap">
			<s:iterator>
				<input type="hidden" name="real<s:property value="key"/>"
					value="<s:property value="value"/>">
			</s:iterator>
		</s:iterator>
	</s:if>
	<caption>手工配置业务端口</caption>
	<thead>
		<tr>
			<th width="20%">全部业务列表</th>
			<th width="40%">按工单显示端口</th>
			<th width="40%">按实际终端配置显示端口</th>
		</tr>
	</thead>
	<tbody>
		<input type="hidden" name="user_1" value="">
		<input type="hidden" name="vlanid_1" value="">
		<input type="hidden" name="sertype" value="">
		<input type="hidden" name="loid" value="<s:property value="loid"/>">
		<input type="hidden" name="leftnum"
			value="<s:property value="interleftNum"/>">
		<input type="hidden" name="rightnum"
			value="<s:property value="interrightNum"/>">
		<s:if test="nointerentNameListHtml!= null">
			<tr>
				<td width='20%'>宽带</td>
				<td width='40%'>
					<table border="0" id="tab">
						<s:property value="nointerentNameListHtml" escapeHtml="false" />
					</table>
				</td>
				<td width='40%'>
					<table border="0" id="tab1">
						<s:property value="disrealinterentNameListHtml" escapeHtml="false" />
					</table>
				</td>
			</tr>
		</s:if>
		<s:if test="noitvNameListHtml!= null">
			<tr>
				<td width='20%'>ITV</td>
				<td width='40%'><table border="0">
						<s:property value="noitvNameListHtml" escapeHtml="false" />
					</table></td>
				<td width='40%'><table border="0">
						<s:property value="disrealitvNameListHtml" escapeHtml="false" />
					</table></td>
			</tr>
		</s:if>
		<s:if test="notianyiNameListHtml!= null">
			<tr>
				<td width='20%'>天翼看店</td>
				<td width='40%'><table border="0">
						<s:property value="notianyiNameListHtml" escapeHtml="false" />
					</table></td>
				<td width='40%'><table border="0">
						<s:property value="disrealtianyiNameListHtml" escapeHtml="false" />
					</table></td>
		</s:if>
		<tr>
			<td height="20"></td>
		</tr>
		<s:if
			test="%{nointerentNameListHtml != null || noitvNameListHtml!= null || notianyiNameListHtml!= null}">
			<tr>
				<td width="20%">业务类型</td>
				<td colspan="2" width="80%"><select name="servTypeId" class=bk
					onchange="changeSelect()">
						<option value="-1">==请选择业务类型==</option>
						<s:if test="nointerentNameListHtml!= null">
							<option value="10">==宽带==</option>
						</s:if>
						<s:if test="noitvNameListHtml!= null">
							<option value="11">==ITV==</option>
						</s:if>
						<s:if test="notianyiNameListHtml!= null">
							<option value="25">==天翼看店==</option>
						</s:if>
				</select></td>
			</tr>
		</s:if>
		<s:else>
			<tr>
				<td colspan="3" width="20%" style="text-align: center;">【<s:property
						value="loid" />】端口异常！
				</td>
			</tr>
		</s:else>
		<tr id="inter" style="display: none">
			<td width="20%">宽带</td>
			<td width="40%"><select name="interservType" class=bk
				onchange="changeSelectType(1)">
					<option value="-1">==请选择显示类型==</option>
					<s:if test="notianyiNameListHtml!= null">
						<option value="30">==按工单显示端口==</option>
					</s:if>
					<s:else>
						<option value="30">==按工单显示端口==</option>
						<option value="40">==按实际终端配置显示端口==</option>
					</s:else>
			</select></td>
			<td id="interData" style="display: none;" width="40%"><table>
					<s:property value="interentNameListHtml" escapeHtml="false" />
				</table></td>
			<td id="interReal" style="display: none" width="40%"><table>
					<s:property value="realinterentNameListHtml" escapeHtml="false" />
				</table></td>
			<td id="choose" style="display: none;" width="40%"><select
				name="choose_user" class=bk onchange="kuandaiChange(1)">
					<option value="-1">==请选择用户==</option>
					<s:if test="userList!=null">
						<s:if test="userList.size()>0">
							<s:iterator value="userList" status="st">
								<option value='<s:property value="userData"/>'>
									==
									<s:property value="userData" />
									==
								</option>
							</s:iterator>
						</s:if>
					</s:if>
			</select></td>
			<td id="choose1" style="display: none;" width="40%"><s:if
					test="vlanList!=null">
					<s:if test="vlanList.size()>0">
						<select name="choose_vlid" class=bk onchange="kuandaiChange(2)">
							<option value="-1">==请选择用户==</option>
							<s:iterator value="vlanList">
								<option value='real<s:property value="vlan_Map"/>'>
									==
									<s:property value="vlan_Map" />
									==
								</option>
							</s:iterator>
						</select>
					</s:if>
				</s:if> <s:else>
					宽带设备采集失败
				</s:else></td>
		</tr>
		<tr id="userDisplay" style="display: none;"></tr>
		<tr id="vlanidDisplay" style="display: none;"></tr>
		<tr id="itv" style="display: none">
			<td width="20%">ITV</td>
			<td width="40%"><select name="itvservType" class=bk
				onchange="changeSelectType(2)">
					<option value="-1">==请选择显示类型==</option>
					<option value="50">==按工单显示端口==</option>
					<option value="60">==按实际终端配置显示端口==</option>
			</select></td>
			<td id="itvData" style="display: none;" width="40%"><table>
					<s:property value="itvNameListHtml" escapeHtml="false" />
				</table></td>
			<td id="itvReal" style="display: none;" width='40%'><table>
					<s:property value="realitvNameListHtml" escapeHtml="false" />
				</table></td>
		</tr>
		<tr id="tianyi" style="display: none">
			<td width="20%">天翼看店</td>
			<td width="40%"><select name="tianyiservType" class=bk
				onchange="changeSelectType(3)">
					<option value="-1">==请选择显示类型==</option>
					<option value="70">==按工单显示端口==</option>
					<!-- <option value="80">==按实际终端配置显示端口==</option> -->
			</select></td>
			<td id="tianyiData" style="display: none;" width="40%"><table>
					<s:property value="tianyiNameListHtml" escapeHtml="false" />
				</table></td>
			<td id="tianyiReal" style="display: none;" width='40%'><table>
					<s:property value="realtianyiNameListHtml" escapeHtml="false" />
				</table></td>
		</tr>
		<tr id="btn" style="display: none">
			<td colspan="3" align="right" class=foot>
				<button onclick="update()" id="button" name="button">&nbsp;保存修改&nbsp;</button>
			</td>
		</tr>
	</tbody>
	<tr STYLE="display: none">
		<td colspan="3"><iframe id="childFrm" src=""></iframe></td>
	</tr>
</table>
