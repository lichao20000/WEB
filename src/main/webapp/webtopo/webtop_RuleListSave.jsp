<%@ page contentType="text/html;charset=GBK"%>
<%@ include file="../timelater.jsp"%>
<%@ page import="com.linkage.litms.webtopo.warn.*"%>
<%
	request.setCharacterEncoding("GBK");
	String script = null;
	String rule_Priority = request.getParameter("rule_Priority");
	String rule_Name = request.getParameter("rule_Name");
	String rule_Desc = request.getParameter("rule_Desc");
	String rule_Text = request.getParameter("rule_Text");
	String rule_Result = request.getParameter("rule_Result");
	//��������
	String tmpLen = request.getParameter("arrLength");
	Map map = new HashMap();
	map.put("0",rule_Priority);
	map.put("1",rule_Name);
	map.put("2",rule_Desc);
	map.put("3",rule_Text);
	map.put("4",rule_Result);
	
	Object[] arrValue = null;
	String[][] m_strDatas = null;
	
	tmpLen = (tmpLen == null || tmpLen.equals("")) ? "0" : tmpLen;
	int length = Integer.parseInt(tmpLen);
	//0:setPriority 1:setName 2:setContent 3:setResult 4:setDesc
	m_strDatas = new String[length][5];
	//�ȱ�����(index,name,desc,text,result)
	for(int i=0;i<5;i++){
		//�ָ� �õ�object[]
		arrValue = StringUtils.partOff((String)map.get("" + i),"$");
		// �ڱ�����
		for(int j=0;j<arrValue.length;j++){
			m_strDatas[j][i] = (String)arrValue[j];
		}
	}

	String account = user.getAccount();
    String file = request.getRealPath("webtopo/" + account + "_warnrule.xml");
	DeviceWarnFilter filter = DeviceWarnFilter.getInstance(file);//new DeviceWarnFilter();
	if(filter.SaveXML(m_strDatas,file)){
		session.setAttribute("webtopo_warn", null);
	    script = "alert(\"�澯���򱣴�ɹ�!\");window.close();";
	}else{
	    script = "alert(\"�澯���򱣴�ʧ��,������!\");window.close();";
	}
%>
<script language="javascript">
	<%=script%>
</script>