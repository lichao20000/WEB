<%@ page import="com.linkage.commons.db.DBUtil" %>
<%@ page import="com.linkage.commons.db.PrepareSQL" %>
<%@ include file="../../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>

<%
request.setCharacterEncoding("GBK");
String rp_id = request.getParameter("id");

Map field = null;
Cursor cursor = null;
Map fields = null;

String rp_name = null;
String strSQL = null;
String tmp = null;
int layer = -1;

out.println("<script language=javascript>");

strSQL = "select * from tab_report_manager where id = "+ rp_id;
// teledb
if (DBUtil.GetDB() == 3) {
    strSQL = "select report_name, layer from tab_report_manager where id = "+ rp_id;
}
PrepareSQL psql = new PrepareSQL(strSQL);
psql.getSQL();
field = DataSetBean.getRecord(strSQL);
rp_name = (field != null)?(String.valueOf(field.get("report_name"))):"";
layer = Integer.parseInt(String.valueOf(field.get("layer")));

//
if(request.getParameter("confirmChg") != null){
	tmp = request.getParameter("MenuTextChg");
	tmp = Encoder.toGB(tmp);
	strSQL = "update tab_report_manager set report_name = '"+ tmp +"' where id = "+ rp_id;
	int iCode = DataSetBean.executeUpdate(strSQL);
	
	if(iCode > 0){
		out.println("window.alert(\"目录名称修改成："+ tmp +" 成功\");");
		out.println("top.leftPage.treeView.setMenuText(\""+ tmp +"\");");
		rp_name = tmp;
	}
}

if(request.getParameter("confirmDel") != null){
	tmp = request.getParameter("MenuTextDel");
	
	strSQL = "select count(1) as num from tab_report_manager where pid = "+ rp_id;
    // teledb
    if (DBUtil.GetDB() == 3) {
        strSQL = "select count(*) as num from tab_report_manager where pid = "+ rp_id;
    }
	int iCode = Integer.parseInt(String.valueOf(DataSetBean.getRecord(strSQL).get("num")));
	//int ss = iCode;
	if(iCode > 0){
		out.println("window.alert(\"目录："+ tmp +" 下面存在子节点；不能删除此目录\");history.go(-1);");
	}else{
		strSQL = "delete from tab_report_manager where id = "+ rp_id;
		iCode = DataSetBean.executeUpdate(strSQL);
		
		if(iCode > 0){
			out.println("window.alert(\"目录："+ tmp +" 删除成功\");");
			out.println("top.leftPage.treeView.delMenuText();");
			//rp_name = tmp;
		}	
	}
}


if(request.getParameter("createChild") != null){
	String rootid = request.getParameter("rootid");
	strSQL = "select * from tab_report_manager where id="+ rp_id;
    // teledb
    if (DBUtil.GetDB() == 3) {
        strSQL = "select layer from tab_report_manager where id="+ rp_id;
    }
	field = DataSetBean.getRecord(strSQL);
	layer = Integer.parseInt(String.valueOf(field.get("layer")));
	tmp = request.getParameter("MenuTextCrtChild");
	
	long maxId = DataSetBean.getMaxId("tab_report_manager","id");	
	
	String url = "adminTreeView.jsp?id="+ maxId;
	
	strSQL = "insert into tab_report_manager (id,user_id,report_name,sqlvalue,modulename,report_type,remark,pid,layer,rootid,ishas,perform_type,public_stat,report_stat,report_path,reg_time) values (";
	strSQL += maxId+ ",'"+ session.getAttribute("username") +"','"+ tmp +"',null,'','','',";
	strSQL += rp_id +","+ (layer + 1) +","+ rootid +",1,'',null,1,'','')";
	
	int iCode = DataSetBean.executeUpdate(strSQL);
	
	if(iCode > 0){
		out.println("window.alert(\" 子目录："+ tmp +" 创建成功\");");
		out.println("top.leftPage.treeView.addSub(\""+ tmp +"\",\""+ url +"\",\"top.rightPage.viewPage\");");
		
		//rp_name = tmp;
	}	
}


field = null;
strSQL = null;

out.println("</script>");

//得到页节点链表
strSQL = "select * from tab_report_manager where ishas = 0 and pid = "+ rp_id;
// teledb
if (DBUtil.GetDB() == 3) {
    strSQL = "select report_name, id, layer from tab_report_manager where ishas = 0 and pid = "+ rp_id;
}
cursor = DataSetBean.getCursor(strSQL); 
fields = cursor.getNext();
%>
<HTML>
<HEAD>
<TITLE> <%=LipossGlobals.getLipossName()%> </TITLE>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=gb2312">
<link rel="stylesheet" href="../../../css/css_blue.css" type="text/css">
<style type="text/css">
<!--
.style1 {font-family: Arial, Helvetica, sans-serif}
-->
</style>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #FFFFFF;
}
-->
</style>
<SCRIPT LANGUAGE="JavaScript">
<!--


//-->
</SCRIPT>
</HEAD>
<BODY scrolling="no" onload = setRp_path()>
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
<TR><TD>
	<!-- 主程序 start -->
	<form name=frm action=""  method="post" onsubmit="return checkform();">
        <table width="98%"  border="0" align="center" cellpadding="0" cellspacing="5">
          <tr> 
            <td></td>
          </tr>
          <tr> 
            <td height="44" valign="top"> 
              <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                <tr> 
                  <td width="13%"><img src="images/shenhe_tu1.gif" width="85" height="78"></td>
                  <td width="2%"><img src="images/shenhe_tu2.gif" width="3" height="69"></td>
                  <td width="85%" valign="top"> 
                    <table width="100%"  border="0" cellspacing="0" cellpadding="0">
                      <tr> 
                        <td class="html_title"><strong>说明</strong></td>
                      </tr>
                      <tr> 
                        <td height="55" bgcolor="#EFEBFF" class="text">您当前选择的目录&gt;&gt;<font color=blue>
                          <script>document.write(top.leftPage.treeView.getCurrentPath());</script>
                          </font></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr> 
            <td height="1" background="images/dian.gif"></td>
          </tr>
          <tr> 
            <td height="13"> 
              <table width="750"  border="0" cellpadding="6" cellspacing="1" bgcolor="#999999">
                <tr> 
                  <td width="25%" bgcolor="#FFFFFF"> 
                    <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="text">
                      <tr> 
                        <td width="155"> 
                          <table width="143" height="29" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td background="images/shenhe_tu3.gif"> 
                                <div align="center">修改目录名称</div>
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td width="210"> 
                          <input name="MenuTextChg" type="text" class="form_yellowkuang" size="30" value="<%=((request.getParameter("confirmDel") != null)?"":(rp_name))%>">
                        </td>
                        <td> 
                          <input type="submit" name="confirmChg" value=" 修 改 "  class=btn>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr> 
                  <td bgcolor="#FFFFFF"> 
                    <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="text">
                      <tr> 
                        <td width="155"> 
                          <table width="143" height="29" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td background="images/shenhe_tu3.gif"> 
                                <div align="center">删除当前目录</div>
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td width="210"> 
                          <input name="MenuTextDel" type="text" class="form_yellowkuang" size="30" value="<%=((request.getParameter("confirmDel") != null)?"":(rp_name))%>">
                        </td>
                        <td> 
                          <input type="submit" name="confirmDel" value=" 删 除 " class=btn>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
                <tr> 
                  <td bgcolor="#FFFFFF"> 
                    <table width="95%"  border="0" align="center" cellpadding="0" cellspacing="0" class="text">
                      <tr> 
                        <td width="155"> 
                          <table width="143" height="29" border="0" cellpadding="0" cellspacing="0">
                            <tr> 
                              <td background="images/shenhe_tu3.gif"> 
                                <div align="center">创建子目录</div>
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td width="210"> 
                          <input name="MenuTextCrtChild" type="text" class="form_yellowkuang" size="30">
                        </td>
                        <td> 
                          <input type="submit" name="createChild" value=" 新 增 "  class=btn>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <table width="750"  border="0" cellpadding="3" cellspacing="1" bordercolor="#000000" bgcolor="#999999" class="text">
                <tr bgcolor="#EFEBFF"> 
                  <td colspan="3" background="images/table_title.jpg"> 
                    <div align="center"><a href="#" onClick=openAddNode("<%=rp_id%>","<%=layer%>")><strong>新增静态-报表节点</strong></a></div>
                  </td>
                </tr>
                <tr bgcolor="#EFEBFF"> 
                  <td width="34%"> 
                    <div align="center">对应节点列表</div>
                  </td>
                  <td width="37%"> 
                    <div align="center">报表名称 </div>
                  </td>
                  <td width="29%"> 
                    <div align="center">操作</div>
                  </td>
                </tr>
				<%int num = 0;while(fields != null){%>
                <tr bgcolor="#EFEBFF"> 
                  <td> 
                    <div align="center"><%=++num%></div>
                  </td>
                  <td align="center"><%=fields.get("report_name")%></td>
                  <td><div align="center"><a href="#" onClick=openNodeChg("<%=fields.get("id")%>","<%=fields.get("layer")%>")>修改</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onClick=NodeDel(<%=fields.get("id")%>)>删除</a></div></td>
                </tr>
				<%fields = cursor.getNext();}
				  fields = null;
				%>
              </table>
            </td>
          </tr>
        </table>
        <input type=hidden name=id value="<%=rp_id%>">
        <input type=hidden name=layer value="<%=layer%>">
        <input type=hidden name=rp_path value="">
		<input type=hidden name=rootid value="">
      </form>
	<!-- 主程序 end -->
</TD></TR>
<TR><TD HEIGHT=20>&nbsp;</TD></TR>
</TABLE>
<iframe id="childFrm" width="0" height="0"></iframe>
</body></html>
<script language=javascript>
function setRp_path(){
	document.all("rp_path").value = top.leftPage.treeView.getCurrentPath();
}
function addSub(text,hint,status,script,link,target){
	top.leftPage.treeView.addSub(text,hint,status,script,link,target);
}

function setMenuText(){
	var text = document.getElementById("MenuTextChg").value;
	top.leftPage.treeView.setMenuText(text);
}


function checkform(){
	var tmp = top.leftPage.treeView.getCurrentPathID();
	var arr = tmp.split(",");
	rootid = arr[arr.length-1];
	document.all("rootid").value = rootid;
	return true;
}

function openNodeChg(id,layer){
	var page = "chgTreeNode.jsp?tt="+ new Date().getTime() +"&id="+ id +"&layer="+ layer;
	var height = 320;
	var width = 400;
	var w = window.open(page,"ss","width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
	w.moveTo((screen.width-width)/2,(screen.height-height)/2);
}

function openAddNode(id,layer){
	var page = "addTreeNode.jsp?tt="+ new Date().getTime() +"&id="+ id +"&layer="+ layer;
	var tmp = top.leftPage.treeView.getCurrentPathID();
	var arr = tmp.split(",");
	rootid = arr[arr.length-1];
	page += "&rootid="+rootid;	
	var height = 320;
	var width = 400;
	var w = window.open(page,"ss","width="+width+",height="+height+",resizable=yes,scrollbars=no,toolbar=no");
	w.moveTo((screen.width-width)/2,(screen.height-height)/2);
}

function NodeDel(id){
	var page = "../../../Js/warning.htm";
	var vReturnValue = window.showModalDialog(page,"","dialogHeight: 217px; dialogWidth: 400px; dialogTop: px; dialogLeft: px; edge: Raised; center: Yes; help: No; resizable: No; status: No;scroll:No;");
	if(vReturnValue){
		//To Add your code
		document.all("childFrm").src = "rp_nodesave.jsp?report_stat=-2&tt="+ new Date().getTime() +"&id="+id;
	}
}

function doString(flag){
	if(flag == -1){
		alert("删除节点操作失败");
	}else{
		alert("删除节点成功");
	}
	this.location.reload();
}
</script>
