<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.module.gwms.dao.tabquery.CityDAO" %>
<jsp:useBean id="sheetManage" scope="request" class="com.linkage.litms.netcutover.SheetManage"/>
<%
	request.setCharacterEncoding("GBK");
	String flag = request.getParameter("flag");
	String stroffset = request.getParameter("offset");
	int offset;
	int pagelen = 15;
	if(stroffset == null) offset = 1;
	else{
		offset = Integer.parseInt(stroffset);
	}
	List list = sheetManage.getSearchList(request);
	String strBar = String.valueOf(list.get(0));
	Cursor cursor = (Cursor)list.get(1);
	Map fields = cursor.getNext();

	String[] arrStyle = new String[11];
	arrStyle[0] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
	arrStyle[1] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
	arrStyle[2] = "class=trOut  onmouseover='this.className=\"trOver\"' onmouseout='this.className=\"trOut\"'";
	arrStyle[3] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
	arrStyle[4] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
	arrStyle[5] = "class=trOutblue onmouseover='this.className=\"trOverblue\"' onmouseout='this.className=\"trOverblue\"'";
	arrStyle[6] = "class=trOutred onmouseover='this.className=\"trOutred\"' onmouseout='this.className=\"trOutred\"'";
	arrStyle[7] = "class=trOutchense onmouseover='this.className=\"trOverchense\"' onmouseout='this.className=\"trOutchense\"'";
	arrStyle[8] = "class=trOutyellow onmouseover='this.className=\"trOutyellow\"' onmouseout='this.className=\"trOutyellow\"'";
	arrStyle[9] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";
	arrStyle[10] = "class=trOutshougong onmouseover='this.className=\"trOvershougong\"' onmouseout='this.className=\"trOutshougong\"'";

/**
	String city_id = curUser.getCityId();
	SelectCityFilter City = new SelectCityFilter(request);
	String city_name = City.getNameByCity_id(city_id);
	String strCityList = City.getSelfAndNextLayer(true, city_id, "");
*/
%>
<SCRIPT LANGUAGE="JavaScript">
var flag = "<%=flag%>";
function GoList(page){
	this.location = page + "?flag=" + flag;
}

</SCRIPT>
<%@ include file="../toolbar.jsp"%>
<TABLE width="100%" border=0 cellspacing=0 cellpadding=0 align="center" id="myTable">
			<TR>
				<TD HEIGHT=40>&nbsp;</TD>
			</TR>			
			<TR>
				<TD bgcolor=#999999>
				<TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
					<tr>
						<th width="" nowrap>�������</th>
						<th width="" nowrap>ҵ������</th>
						<th width="" nowrap>����</th>
						<!-- <th width="" nowrap>������Դ</th> -->
						<th width="" nowrap>�û��ʻ�</th>
						<th width="" nowrap>ִ�н��</th>
						<th width="" nowrap>��ʼʱ��</th>
						<th nowrap>����ʱ��</th>
						<th nowrap width="120">ʧ��ԭ������</th>
					</tr>
				<%
					if (fields != null) {
	                	String tmp,tmp_batch;
	                	int iStatus = 0;
	                	while (fields != null) {
                    		tmp = fields.get("sheet_id") + "," + fields.get("receive_time")+"," +  fields.get("gather_id");

		                    tmp_batch = fields.get("sheet_id") + "," + fields.get("receive_time")+ ","+fields.get("gather_id");

		                    out.println("<tr "
		                                    + arrStyle[iStatus]
		                                    + " ondblclick=doDbClick(this) title='˫������ʾ������ϸ��Ϣ'  parames='"
		                                    + tmp + "' value='"
		                                    + fields.get("") + "'>");
		                    out.println("<td class=column1><nobr>"
		                            + fields.get("sheet_id") + "</nobr></td>");
		                    //ҵ������
		                    tmp = (String)fields.get("serv_type_id");
		                    out.println("<td class=column1><nobr>"
		                            + com.linkage.litms.Global.Serv_type_Map.get(tmp) + "</nobr></td>");
		                            
		                            
		                    tmp = (String) fields.get("city_id");
		                    out.println("<td class=column1><nobr>" + CityDAO.getCityIdCityNameMap().get(tmp)
		                            + "</nobr></td>");
							//������Դ
							/* tmp = (String) fields.get("sheet_source");
							if(tmp.equals("0")){
								tmp = "�������";
							}else{
								tmp = "BSS";
							}
		                    out.println("<td class=column1><nobr>"
		                            + tmp + "</nobr></td>"); */
		
		                    out.println("<td class=column1><nobr>"
		                            + fields.get("username") + "</nobr></td>");
		                            
		                            
		                    tmp = (String)fields.get("exec_status");
		                    if(tmp.equals("1")){
			                    tmp = (String) fields.get("fault_code");                           
			                    if(tmp.equals("1")){
			                    	tmp = "ִ�гɹ�";
			                    }else if(tmp.equals("-1")){
			                    	tmp = "���Ӳ���";
			                    }else if(tmp.equals("-2")){
			                    	tmp = "���ӳ�ʱ";
			                    }else if(tmp.equals("-3")){
			                    	tmp = "δ�ҵ���ع���";
			                    }else if(tmp.equals("-4")){
			                     	tmp = "δ�ҵ�����豸";                   
			                    }else if(tmp.equals("-5")){
			                     	tmp = "δ�ҵ����RPC����";                                         
			                    }else if(tmp.equals("-6")){
			                     	tmp = "�豸��������";                       
			                    } 
			                }else{
			                	tmp = "����ִ��";
			                }        
							out.println("<td class=column1><nobr>"
		                            + tmp
		                            + "</nobr></td>");                                    
		                    tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong((String)fields.get("start_time")));                          
							out.println("<td class=column1><nobr>"
		                            +  tmp
		                            + "</nobr></td>"); 
		                    tmp = (String) fields.get("end_time");
		                    if(tmp!=null && !tmp.equals("")){            
		                    	tmp = StringUtils.formatDate("yyyy-MM-dd HH:mm:ss",Long.parseLong((String)fields.get("end_time")));         
							}
							out.println("<td class=column1><nobr>"
		                            +  tmp
		                            + "</nobr></td>");                        
		
		                    tmp = (String) fields.get("fault_desc");
		                    if (tmp == null || tmp.equals("null"))
		                        tmp = "";
		                    out.println("<td class=column1><nobr>" + tmp + "</nobr></td>"); 
		                                                        
		                    out.println("</tr>");
		
		                    fields = cursor.getNext();
		                }
		            } else {
		                out.println("<tr bgcolor='#ffffff' ><td align=center colspan=10>û�й�����¼</td></tr>");
		            }

            %>
            	<TR><TD class=column COLSPAN=9 align=right><%=strBar %></TD></TR>
				</TABLE>
		</TD>
	</TR>

</TABLE>
<SCRIPT LANGUAGE="JavaScript">
	parent.document.all("showList").innerHTML = document.body.innerHTML;
</SCRIPT>