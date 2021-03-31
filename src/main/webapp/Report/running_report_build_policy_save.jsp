<html>
<%@ page import="java.util.Map,java.util.TreeMap,java.util.List,java.util.Iterator,java.text.SimpleDateFormat,java.util.Date,java.util.Set,com.linkage.litms.common.database.Cursor,com.linkage.litms.common.database.DataSetBean"%>
<jsp:useBean id="rrctMgr" scope="request" class="com.linkage.litms.report.RRcTMgr"/>
<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%
	request.setCharacterEncoding("GBK");
	String action_type = request.getParameter("action_type");
	boolean updatePolicy = true;
	String strlist = "";

	if ("1".equals(action_type)) {
		updatePolicy = rrctMgr.updatePolicy(request);
	} else if ("2".equals(action_type)){
		Cursor cursor = rrctMgr.queryPolicy(request);
		if (null != cursor) {
			Map map = cursor.getNext();
			if (null != map) {
				String device_serialnumber = (String) map.get("device_serialnumber");
				String send_type = (String) map.get("send_type");
				String retry = (String) map.get("retry");
				String policy = (String) map.get("policy");
				String customize = (String) map.get("customize");
				String email = (String) map.get("email");
				if (!"".equals(strlist)) {
					strlist += "<br>";
				}
				strlist += "设备序列号：" + device_serialnumber + "<br>";
				strlist += "(报表类型&nbsp;&nbsp;生成时间&nbsp;&nbsp;发送时间)<br>";
				
				String[] arr1 = policy.split("\\|");
				String[] weekArr = new String[] {"","一","二","三","四","五","六","七"};
				String[] customizeArr = new String[] {"终端","用户","客户","在线信息","WAN&LAN","告警","流量","病毒"};
				if (null != arr1) {
					for (int i=0; i<arr1.length; i++) {
						String s2 = arr1[i];
						if (null == s2) {
							continue;
						}
						String[] arr2 = s2.split(",");
						/**
						if (i == 0) {
							strlist += "日报表：";
							if (null != arr2 && arr2.length == 2) {
								strlist += arr2[0] + "时";
								strlist += "，" + arr2[1] + "时";
							}
							strlist += "<br>";
						} else if (i == 1) {
							strlist += "周报表：";
							if (null != arr2 && arr2.length == 2) {
								String s31 = arr2[0];
								String s32 = arr2[1];
								if (null != s31) {
									String[] arr31 = s31.split("_");
									if (null != arr31 && arr31.length == 2) {
										strlist += "周" + weekArr[Integer.parseInt(arr31[0])];
										strlist += arr31[1] + "时";
									}
								}
								if (null != s32) {						
									String[] arr32 = s32.split("_");
									if (null != arr32 && arr32.length == 2) {
										strlist += "，";
										strlist += "周" + weekArr[Integer.parseInt(arr32[0])];
										strlist += arr32[1] + "时";
									}
								}
							}
							strlist += "<br>";
						} else 
							**/
							if (i == 2) {
							strlist += "月报表：";
							if (null != arr2 && arr2.length == 2) {
								String s31 = arr2[0];
								String s32 = arr2[1];
								if (null != s31) {
									String[] arr31 = s31.split("_");
									if (null != arr31 && arr31.length == 2) {
										strlist += arr31[0] + "号";
										strlist += arr31[1] + "时";
									}
								}
								if (null != s32) {						
									String[] arr32 = s32.split("_");
									if (null != arr32 && arr32.length == 2) {
										strlist += "，";
										strlist += arr32[0] + "号";
										strlist += arr32[1] + "时";
									}
								}
							}
							strlist += "<br>";
						} 
						/**	else {
							strlist += "年报表：";
							if (null != arr2 && arr2.length == 2) {
								String s31 = arr2[0];
								String s32 = arr2[1];
								if (null != s31) {
									String[] arr31 = s31.split("_");
									if (null != arr31 && arr31.length == 3) {
										strlist += arr31[0] + "月";
										strlist += arr31[1] + "号";
										strlist += arr31[2] + "时";
									}
								}
								if (null != s32) {						
									String[] arr32 = s32.split("_");
									if (null != arr32 && arr32.length == 3) {
										strlist += "，";
										strlist += arr32[0] + "月";
										strlist += arr32[1] + "号";
										strlist += arr32[2] + "时";
									}
								}
							}
							strlist += "<br>";
						}
						**/
					}
				}

				String custStr = "";
				for (int j=0; j < customize.length(); j++) {
					if ('1' == customize.charAt(j)) {
						if (!"".equals(custStr)) {
							custStr += "，";
						}
						custStr += customizeArr[j];
					}
				}
				strlist += "报表定制内容：" + custStr + "<br>";
				strlist += "发送类型：" + ("0".equals(send_type) ? "自动发送" : "确认后发送") + "<br>";
				strlist += "接收报表邮箱：" + email + "<br>";
			}
		}
	}

%>

<body>
<SPAN ID="child"><%=strlist%></SPAN>
<SCRIPT LANGUAGE="JavaScript">
var action_type = "<%=action_type%>";

if (action_type == 1) {
	var updatePolicy = "<%=updatePolicy%>";
	if ("true" == updatePolicy) {
		parent.alert("设置成功");
	} else {
		parent.alert("设置失败");
	}
} else if (action_type == 2) {
	parent.document.getElementById("queryResultTr").style.display = "";
	parent.document.getElementById("queryResultSpan").innerHTML = "<%="".equals(strlist) ? "该设备没有配置报表策略" : strlist%>";
}
</SCRIPT>
</body>
</html>
