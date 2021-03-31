<%--
Author		: liuli
Date		: 2006-10-13
Desc		: 手工录入公告信息
--%>
<%@ include file="../timelater.jsp"%>
<%@page import="com.linkage.module.gwms.dao.tabquery.CityDAO"%>
<%@ page contentType="text/html;charset=GBK"%>
<%request.setCharacterEncoding("GBK");
List tem_List = CityDAO.getNextCityIdsByCityPid(user.getCityId());
tem_List.add(user.getCityId());
%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.linkage.litms.common.database.Cursor"%>
<%@page import="com.linkage.litms.common.database.DataSetBean"%>
<%@ page import="com.linkage.commons.db.DBUtil" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>无标题文档</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;

}
-->
</style>
<link href="./css/css_blue.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--
.style1 {
	color: #FFFFFF;
	font-weight: bold;
}
body,td,th {
	font-size: 12px;
}
-->
</style>

<SCRIPT LANGUAGE="JavaScript">
<!--
	parent.showImg.style.display="none";
	function GoSameDevice1(pages)
{
	var page;
	page=pages;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}

function GoSameDevice2(pages)
{
	var page;
	page=pages;
	window.open(page,"","left=20,top=20,width=600,height=450,resizable=yes,scrollbars=yes");
}
//-->
</SCRIPT>

</head>

<body>

<table width="100%" height="97%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td width="183" valign="top" background="images/left_back.jpg"><img src="images/index_left_top.jpg" width="183" height="19">
      
	  <table width="160"  border="0" align="center" cellpadding="0" cellspacing="0" background="images/left_back.jpg">
        <tr>
          <td height="30" valign="top" background="images/index_left_title.gif">
		  <table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td width="23%"><div align="center"><img src="images/index_user_icon_1.gif" width="17" height="20"></div></td>
              <td width="77%" height="22"><span class="style1">用户信息</span></td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td background="images/index_left_back.gif" bgcolor="#FFFFFF"><table width="92%"  border="0" cellspacing="1" cellpadding="5">
            <tr>
              <td class="text"> <div align="center"><strong><img src="images/index_user_icon_2.gif" width="13" height="12"> 姓名</strong></div></td>
              <td class="bottom_line"> <%=user.getUserInfo().get("per_name")%> </td>
            </tr>
            <tr>
              <td class="text"> <div align="center"><strong><img src="images/index_user_icon_2.gif" width="13" height="12"> 账号</strong></div></td>
              <td class="bottom_line"><%=user.getAccount()%></td>
            </tr>
            <tr>
            	<%String cityName = CityDAO.getCityIdCityNameMap(user.getCityId());%>
              <td class="text"> <div align="center"><strong><img src="images/index_user_icon_2.gif" width="13" height="12"> 属地</strong></div></td>
              <td class="bottom_line"><%=cityName%></td>
            </tr>
            <tr>
              <td class="text"> <div align="center"><strong><img src="images/index_user_icon_2.gif" width="13" height="12"> 部门</strong></div></td>
              <td class="bottom_line"><%=user.getUserInfo().get("per_dep_oid")%></td>
            </tr>
            <tr>
              <td class="text"> <div align="center"><strong><img src="images/index_user_icon_2.gif" width="13" height="12"> 职务</strong></div></td>
              <td class="bottom_line"><%=user.getUserInfo().get("per_jobtitle")%></td>
            </tr>
          </table></td>
        </tr>
        <tr>
          <td><img src="images/index_left_down.gif" width="160" height="16"></td>
        </tr>
      </table>
      <br>
      <table width="160"  border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
          <td height="30" valign="top" background="images/index_left_title.gif"><table width="90%"  border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td width="23%" height="20"><div align="center"><img src="images/index_left_icon_1.gif" width="14" height="12"></div></td>
                <td width="77%" height="22"><span class="style1">客服热线</span></td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td background="images/index_left_back.gif" bgcolor="#FFFFFF"><table width="92%"  border="0" cellspacing="1" cellpadding="5">
              <tr>
                <td class="text">
                  <div align="right"><strong><img src="images/index_left_icon_2.gif" width="12" height="12"> </strong></div></td>
                <td class="bottom_line"> <span class="input"><strong><em>0531-86033070</em></strong></span></td>
              </tr>
              <tr>
                <td class="text">
                  <div align="right"><strong><img src="images/index_left_icon_3.gif" width="12" height="12"></strong></div></td>
                <td class="bottom_line"><strong class="input"><em>025-83753909</em></strong></td>
              </tr>
              <tr>
                <td class="text">
                  <div align="center"></div></td>
                <td class="text">工作时间<br>
                  星期一到星期五<br>
                8:00-17:30</td>
              </tr>
          </table></td>
        </tr>
        <tr>
          <td><img src="images/index_left_down.gif" width="160" height="16"></td>
        </tr>
      </table>  
	</td>

    <td valign="top" width="100%">		
		<table width="97%"  border="0" align="center" cellpadding="0" cellspacing="0">
		  <tr>
			<td height="19" colspan="3">
			<span class="text">&nbsp;您的当前位置：首页 
			<%                   
				if(user.getAccount().equals("admin")){
			 %>
                <a href="javascript:GoSameDevice2('inside_gonggaoxinxi1broad.jsp');">添加手工公告信息</a><br>
            <%
				}
		     %>		
			</span>
			</td>
		  </tr>
		  <tr>
			<td height="5" valign="top" colspan="3"> </td> 
		  </tr>
		  <tr>
			<td background="images/index_main_k_1.gif" width="5" height="5"></td>
			<td background="images/index_main_k_2.gif"></td>
			<td background="images/index_main_k_3.gif" width="5" height="5"></td>
		  </tr>
		  <tr>
			<td background="images/index_main_k_7.gif"></td>
			<td valign="top">
				<table width="789" height="129"  border="0" align="center" cellpadding="0" cellspacing="0" background="images/index_back1.jpg">
				  <tr>
					<td width="141">&nbsp;</td>
					<td width="648"><table width="98%"  border="0" cellspacing="0" cellpadding="5">
					  <tr>
						<td> <img src="images/index_main_ico_1.gif" width="22" height="7">
						 <span class="title">新增功能</span> 
						 </td>
					  </tr>
					  <tr>
						<td height="67" valign="top" class="kuang_blue">
                    	   <marquee height="40" behavior="scroll"  direction="up"  loop="-1" scrollamount="1"  onmouseover=this.stop() onmouseout=this.start()>
	                    <%
	                        String sql1 = "select * from tab_broad_info where titletype='新增功能' ";
                            // teledb
                            if (DBUtil.GetDB() == 3) {
                                sql1 = "select city_id, id, title from tab_broad_info where titletype='新增功能' ";
                            }
                            com.linkage.commons.db.PrepareSQL psql1 = new com.linkage.commons.db.PrepareSQL(sql1);
                            psql1.getSQL();
	                        Cursor cursor1 = DataSetBean.getCursor(sql1);
	                        Map fields1 = cursor1.getNext();
	                  	    while(fields1 != null){ 
	                  	  	String[] tem_city_id = ((String)fields1.get("city_id")).split(","); 
	                  		String Str_kinds = "";
	                  		for(int i=0;i<tem_city_id.length;i++){
	                  			if(Str_kinds.equals(""))
	                  				Str_kinds = tem_city_id[i];
	                  			else
	                  				Str_kinds += "," + tem_city_id[i];
	                  		}
	                  	    //String tem_city_id = (String)fields1.get("city_id"); 	
	                  		if(tem_List.contains(Str_kinds)){          					
	          					out.print("<a target='_blank' href='viewNews2.jsp?id="+ fields1.get("id") +"'>"+"<br>"+"<img src='images/index_main_ico_1.gif'>" + fields1.get("title") + "</a> ");	            				         					
	                  		}	                    		          				
	          				fields1 = cursor1.getNext();
	          			}              	                    	                    	                  	               
	                    %>
                    	</marquee>          
						</td>
					  </tr>
					</table></td>
				  </tr>
				</table>			  
			  <br>
			  <br>
			  <table width="775" height="129"  border="0" align="center" cellpadding="0" cellspacing="0" background="images/index_back2.jpg">
                <tr>
                  <td width="632"><table width="98%"  border="0" align="right" cellpadding="5" cellspacing="0">
                    <tr>
                      <td> <span class="bottom_line"><img src="images/index_main_ico_2.gif" width="13" height="11"></span> 
                      <span class="title">公告</span> 
                      </td>
                    </tr>
                    <tr>
                      <td height="67" valign="top" class="kuang_blue"> 
                    	<marquee height="40" behavior="scroll"  direction="up"  loop="-1" scrollamount="1"  onmouseover=this.stop() onmouseout=this.start()>
	                    <%
	                    String sql2 = "select * from tab_broad_info where titletype='公告' ";
                        // teledb
                        if (DBUtil.GetDB() == 3) {
                            sql2 = "select city_id, id, title from tab_broad_info where titletype='公告'  ";
                        }
                        com.linkage.commons.db.PrepareSQL psql2 = new com.linkage.commons.db.PrepareSQL(sql2);
                        psql2.getSQL();
                        Cursor cursor2 = DataSetBean.getCursor(sql2);
                        Map fields2 = cursor2.getNext();
                  	       while(fields2 != null){
                  	    	 //String[] tem_city_id = ((String)fields2.get("city_id")).split(",");
 	                  	  	String[] tem_city_id1 = ((String)fields2.get("city_id")).split(","); 
	                  		String Str_kinds1 = "";
	                  		for(int k=0;k<tem_city_id1.length;k++){
	                  			if(Str_kinds1.equals(""))
	                  				Str_kinds1 = tem_city_id1[k];
	                  			else
	                  				Str_kinds1 += "," + tem_city_id1[k];
	                  		}
                  	    	 //String tem_city_id = (String)fields2.get("city_id");    
	                       if(tem_List.contains(Str_kinds1)){  	
	            	       out.print("<a target='_blank' href='viewNews1.jsp?id="+ fields2.get("id") +"'>"+"<br>"+"<img src='images/index_main_ico_1.gif'>" + fields2.get("title") +"</a> ");	            				
	                    }	                    			            				
	            			fields2 = cursor2.getNext();
                  	    }
	                    %>
                    	</marquee>   
                     </td>
                    </tr>
                  </table>
                  </td>
                  <td width="143" valign="top">&nbsp;</td>
                </tr>
              </table>
	          <br>
	          <br>
	          <table width="775" height="129"  border="0" align="center" cellpadding="0" cellspacing="0" background="images/index_back3.jpg">
                <tr>
                  <td width="141">&nbsp;</td>
                  <td width="634"><table width="98%"  border="0" cellspacing="0" cellpadding="5">
                    <tr>
                      <td> <span class="bottom_line"><img src="images/index_main_ico_2.gif" width="13" height="11"></span> <span class="title">提示</span> </td>
                    </tr>
                    <tr>
                      <td height="67" valign="top" class="kuang_blue">                        
                      <span class="text">
                      <marquee height="40" behavior="scroll"  direction="up"  loop="-1" scrollamount="1"  onmouseover=this.stop() onmouseout=this.start()>
                      <%        
                      String sql3 = "select * from tab_broad_info where titletype='提示' ";
                      // teledb
                      if (DBUtil.GetDB() == 3) {
                          sql3 = "select city_id, id, title from tab_broad_info where titletype='提示'  ";
                      }
                      com.linkage.commons.db.PrepareSQL psql3 = new com.linkage.commons.db.PrepareSQL(sql3);
                      psql3.getSQL();
                      Cursor cursor3 = DataSetBean.getCursor(sql3);
                      Map fields3 = cursor3.getNext();
                  	    while(fields3 != null){ 
                  	   // String[] tem_city_id = ((String)fields3.get("city_id")).split(",");
                  	  	String[] tem_city_id2 = ((String)fields3.get("city_id")).split(","); 
                  		String Str_kinds2 = "";
                  		for(int j=0;j<tem_city_id2.length;j++){
                  			if(Str_kinds2.equals(""))
                  				Str_kinds2 = tem_city_id2[j];
                  			else
                  				Str_kinds2 += "," + tem_city_id2[j];
                  		}
                  	    //String tem_city_id = (String)fields3.get("city_id"); 	
                  		if(tem_List.contains(Str_kinds2)){          					
          					out.print("<a target='_blank' href='viewNews.jsp?id="+ fields3.get("id") +"'>"+"<br>"+"<img src='images/index_main_ico_1.gif'>" + fields3.get("title") + "</a> ");	            				         					
                  		}	                    		          				
          				fields3 = cursor3.getNext();
          			}                              
                      %>         
                       </marquee> 
                       </span>
                        </td>
                    </tr>
                  </table>
                  </td>
                </tr>
              </table></td>
			<td width="5" background="images/index_main_k_8.gif"></td>
		  </tr>
		  <tr>
			<td><img src="images/index_main_k_4.gif" width="5" height="5"></td>
			<td background="images/index_main_k_5.gif"></td>
			<td><img src="images/index_main_k_6.gif" width="5" height="5"></td>
		  </tr>
		</table>
		</td>
  </tr>
</table>
</body>
</html>
