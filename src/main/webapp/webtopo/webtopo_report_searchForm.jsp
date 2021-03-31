<%@ include file="../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ page import="com.linkage.litms.flux.FluxComm,
				 com.linkage.litms.common.database.Cursor,
				 java.util.*,
                 com.linkage.litms.common.util.*"%>
<%
request.setCharacterEncoding("GBK");
String device_id = request.getParameter("dev_id");

FluxComm flux = new FluxComm(request);
Cursor cursor = flux.getDeviceType();

SimpleDateFormat  sdf=new SimpleDateFormat("yyyy-MM-dd");
String dateValue=sdf.format(new Date((new Date()).getTime()-86400000));
long longValue=(new Date().getTime()-86400000)/1000;


%>
<% out.println("<div id='idLayerTable'>");%>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
    <TR>
    <TD align="center" bgcolor="#FFFFFF"> 
        <FORM method=POST action="" NAME="frm" onsubmit="return checkForm()" target="childFrm">
            <TABLE width="95%" border=0 align="center" cellpadding=0 cellspacing=0 bgcolor="#999999" class="text">
                <TR><TD ><input type="hidden" name="dev_id" value="<%=device_id%>">
                    <TABLE border=0 cellspacing=1 cellpadding=2 width="100%">
                <TR bgcolor="#FFFFFF"> 
                  <td colspan=2 class="blue_title"> <div align="center" >设备端口流量统计--<span id="title">〖设备端口流量均值〗</span></div></td>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD width="180" >&nbsp;请选择要查看报表的类型：</TD>
                  <TD > <input name="SearchType" type="radio" value="1" checked  onClick="javascript:searchType();">
                    日报表 
                    <input type="radio" name="SearchType" value="2" onClick="javascript:searchType();">
                    周报表 
                    <input type="radio" name="SearchType" value="3" onClick="javascript:searchType();">
                    月报表 
                    <input type="radio" name="SearchType" value="4" onClick="javascript:searchType();">
                    年报表 
                    <input type="radio" name="SearchType" value="5" onClick="javascript:searchType();"  disabled>
                    阶段报表</TD>
                </TR>
                <TR bgcolor="#FFFFFF" id="r1"  style="display:none"> 
                  <TD >&nbsp;请选择阶段报表的查询方式：</TD>
                  <TD><select name="dayType" onchange="javascript:typeChange();">
                      <option value="0">按区间段查询</option>
                      <option value="1">按日间隔查询</option>
                    </select></TD>
                </TR>
                <TR bgcolor="#FFFFFF" id="r2"  style="display:none"> 
                  <TD >&nbsp;请选择时间段：</TD>
                  <TD >开始日期： 
                    <input type="text" name="s_day" class=bk> <input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button"> 
                    &nbsp;结束日期： 
                    <input type="text" name="e_day" class=bk> <input type="button" value="▼" class=btn onClick="showCalendar('day',event)" name="button"> 
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" id="r3"  style="display:none"> 
                  <TD >&nbsp;请选择时间：</TD>
                  <TD ><input type="text" name="allday" class=bk size="70"> <input type="button" value="增加" class=btn onClick="showCalendar('addday');" name="button"> 
                    <input type="button" value="清空" class=btn onclick="javascript:frm.allday.value='';" name="clear"> 
                  </TD>
                </TR>
                <TR bgcolor="#FFFFFF" id="r4" style="display:"> 
                  <TD >&nbsp;请选择您要查看报表的<FONT COLOR="red"><span id="strdata">日：</span></FONT></TD>
                  <TD > <span id="changeStart"> <INPUT TYPE="text" NAME="day" class=bk value=<%=dateValue%>> <INPUT TYPE="button" class=btn onclick="showCalendar('day',event)" value="▼"> 
                    <INPUT TYPE="hidden" name="hidday"> </span></TD>
                </TR>
                
                <TR bgcolor="#FFFFFF" > 
                  <TD >&nbsp;选择流量类型：<br> <INPUT name="checkbox" TYPE="checkbox"  id="checkbox" onclick="selectAll('kind')"> 
                    &nbsp;全选</TD>
                  <TD > <table width="100%" border="0" cellpadding="0" cellspacing="0">
                      <tr> 
                        <td><span> 
                          <input name="kind" type="checkbox"  id="kind" value="ifinoctetsbps" checked>
                          流入速率(bps)&nbsp;</span> </td>
                        <td> <input name="kind" type="checkbox" id="kind" value="ifindiscardspps">
                          流入丢包率&nbsp;</td>
                        <td> <input name="kind" type="checkbox"  id="kind" value="ifinerrors">
                          流入错误包数&nbsp; </td>
                        <td> <input name="kind" type="checkbox"  id="kind" value="ifinoctetsbpsmax">
                          流入峰值</td>
                        <td> <input name="kind" type="checkbox" id="kind" value="ifinucastpktspps">
                          每秒流入单播包数 </td>
                        <td> <input name="kind" type="checkbox" id="kind" value="u1">
                          流入带宽利用率</td>
                      </tr>
                      <tr> 
                        <td><input name="kind" type="checkbox" id="kind" value="ifoutoctetsbps" checked>
                          流出速率(bps)&nbsp;</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutdiscardspps">
                          流出丢包率&nbsp;</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifouterrors">
                          流出错误包数</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutoctetsbpsmax">
                          流出峰值</td>
                        <td><input name="kind" type="checkbox" id="kind" value="ifinnucastpktspps">
                          每秒流入非单播包数</td>
                        <td><input type="checkbox" name="kind" value="u2" >
                          流出带宽利用率</td>
                      </tr>
                      <tr> 
                        <td><input name="kind" type="checkbox" id="kind" value="ifinerrorspps">
                          流入错包率</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifinoctets">
                          流入字节数</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifindiscards">
                          流入丢弃包数&nbsp;</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="u3">
                          最大流入利用率</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutucastpktspps">
                          每秒流出单播包数&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr> 
                        <td><input name="kind" type="checkbox"  id="kind" value="ifouterrorspps">
                          流出错包率</td>
                        <td><input name="kind" type="checkbox"id="kind" value="ifoutoctets">
                          流出字节数&nbsp;</td>
                        <td><input name="kind" type="checkbox" id="kind" value="ifoutdiscards">
                          流出丢弃包数&nbsp;</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="u4">
                          最大流出利用率</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutnucastpktspps">
                          每秒流出非单播包数</td>
                        <td>&nbsp;</td>
                      </tr>
                      <tr>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifinunknownprotospps">
                          每秒流入未知协议包数</td>
                        <td><input name="kind" type="checkbox"  id="kind" value="ifoutqlenpps">
                          每秒流出队列大小</td>
                        <td>&nbsp;</td>
                        <td>&nbsp;</td>
                      </tr>
                    </table></TD>
                </TR>
                <TR bgcolor="#FFFFFF"> 
                  <TD bgcolor="#FFFFFF" >&nbsp;请选择设备端口：<br> <INPUT TYPE="checkbox" onclick="selectAll('port')"> 
                    &nbsp;全选</TD>
                  <TD><SPAN id=device_port>请先选择设备</SPAN></TD>
                </TR>
                <TR bgcolor="#FFFFFF"  onmouseout="className='blue_trOut'"> 
                  <TD colspan=2 align=right class="blue_foot"> <div align="right"> 
                      <input type="hidden" name="queryTime" >
                      <input type="hidden" name="queryMonth">
					  
                      <INPUT TYPE="submit" name="cmdOK" value=" 统 计 "  class="jianbian">
                      &nbsp;&nbsp;&nbsp;&nbsp; </div></TD>
                </TR>
              </TABLE>
                </TD></TR>
            </TABLE>
        </FORM>
        <DIV id="idLayerView1" style="overflow:auto;width:1200px;display:none"></div>
    </TD></TR>
    <TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<%out.println("</div>");%>
<SCRIPT LANGUAGE="JavaScript">
    <!--
	if(typeof(parent.maxspeed) == "object"){
		parent.maxspeed.innerHTML = idLayerTable.innerHTML;	
		parent.showport();
		parent.frm.hidday.value=<%=longValue%>;
		//alert(parent.frm.hidday.value);
    }
    //-->
</SCRIPT>