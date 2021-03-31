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
				strlist += "�豸���кţ�" + device_serialnumber + "<br>";
				strlist += "(��������&nbsp;&nbsp;����ʱ��&nbsp;&nbsp;����ʱ��)<br>";
				
				String[] arr1 = policy.split("\\|");
				String[] weekArr = new String[] {"","һ","��","��","��","��","��","��"};
				String[] customizeArr = new String[] {"�ն�","�û�","�ͻ�","������Ϣ","WAN&LAN","�澯","����","����"};
				if (null != arr1) {
					for (int i=0; i<arr1.length; i++) {
						String s2 = arr1[i];
						if (null == s2) {
							continue;
						}
						String[] arr2 = s2.split(",");
						/**
						if (i == 0) {
							strlist += "�ձ���";
							if (null != arr2 && arr2.length == 2) {
								strlist += arr2[0] + "ʱ";
								strlist += "��" + arr2[1] + "ʱ";
							}
							strlist += "<br>";
						} else if (i == 1) {
							strlist += "�ܱ���";
							if (null != arr2 && arr2.length == 2) {
								String s31 = arr2[0];
								String s32 = arr2[1];
								if (null != s31) {
									String[] arr31 = s31.split("_");
									if (null != arr31 && arr31.length == 2) {
										strlist += "��" + weekArr[Integer.parseInt(arr31[0])];
										strlist += arr31[1] + "ʱ";
									}
								}
								if (null != s32) {						
									String[] arr32 = s32.split("_");
									if (null != arr32 && arr32.length == 2) {
										strlist += "��";
										strlist += "��" + weekArr[Integer.parseInt(arr32[0])];
										strlist += arr32[1] + "ʱ";
									}
								}
							}
							strlist += "<br>";
						} else 
							**/
							if (i == 2) {
							strlist += "�±���";
							if (null != arr2 && arr2.length == 2) {
								String s31 = arr2[0];
								String s32 = arr2[1];
								if (null != s31) {
									String[] arr31 = s31.split("_");
									if (null != arr31 && arr31.length == 2) {
										strlist += arr31[0] + "��";
										strlist += arr31[1] + "ʱ";
									}
								}
								if (null != s32) {						
									String[] arr32 = s32.split("_");
									if (null != arr32 && arr32.length == 2) {
										strlist += "��";
										strlist += arr32[0] + "��";
										strlist += arr32[1] + "ʱ";
									}
								}
							}
							strlist += "<br>";
						} 
						/**	else {
							strlist += "�걨��";
							if (null != arr2 && arr2.length == 2) {
								String s31 = arr2[0];
								String s32 = arr2[1];
								if (null != s31) {
									String[] arr31 = s31.split("_");
									if (null != arr31 && arr31.length == 3) {
										strlist += arr31[0] + "��";
										strlist += arr31[1] + "��";
										strlist += arr31[2] + "ʱ";
									}
								}
								if (null != s32) {						
									String[] arr32 = s32.split("_");
									if (null != arr32 && arr32.length == 3) {
										strlist += "��";
										strlist += arr32[0] + "��";
										strlist += arr32[1] + "��";
										strlist += arr32[2] + "ʱ";
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
							custStr += "��";
						}
						custStr += customizeArr[j];
					}
				}
				strlist += "���������ݣ�" + custStr + "<br>";
				strlist += "�������ͣ�" + ("0".equals(send_type) ? "�Զ�����" : "ȷ�Ϻ���") + "<br>";
				strlist += "���ձ������䣺" + email + "<br>";
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
		parent.alert("���óɹ�");
	} else {
		parent.alert("����ʧ��");
	}
} else if (action_type == 2) {
	parent.document.getElementById("queryResultTr").style.display = "";
	parent.document.getElementById("queryResultSpan").innerHTML = "<%="".equals(strlist) ? "���豸û�����ñ������" : strlist%>";
}
</SCRIPT>
</body>
</html>
