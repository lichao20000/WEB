<%@ page language="java" contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<script type="text/javascript" src="../../../Js/jquery.js"></script>


<script type="text/javascript">


   var request = false;
   try {
     request = new XMLHttpRequest();
   } catch (trymicrosoft) {
     try {
       request = new ActiveXObject("Msxml2.XMLHTTP");

     } catch (othermicrosoft) {
       try {
         request = new ActiveXObject("Microsoft.XMLHTTP");
       } catch (failed) {
         request = false;
       }
     }
   }
   if (!request){
     alert("Error initializing XMLHttpRequest!");
   }

   function fileDetail(id){
	   var url="<s:url value='/gtms/stb/resource/FileServerManage!fileQueryList.action'/>";
	   url=url+"?id="+id+"";
	   window.open(url);
	}

   //合并单元格
   function fixRowspan() {
       var tb = document.getElementById("fileServerTable");
       var row_span_num = 1;
       var first_row_title = "";
       var first_row_obj = null;
       for ( var i = 1; i < tb.rows.length; i++) {
           var first_new_row_title = tb.rows[i].cells[0].innerHTML;
           if (first_row_title != "" && first_row_title == first_new_row_title) {
               tb.rows[i].deleteCell(0);
               row_span_num++;
               first_row_obj.setAttribute("rowSpan", row_span_num);
               first_row_obj.innerHTML = first_row_title.replace("/", "<br/>");
           } else {
               if (first_row_title != "") {
                   first_row_obj.setAttribute("rowSpan", row_span_num);
                   first_row_obj.innerHTML = first_row_title.replace("/", "<br/>");
                   row_span_num = 1;
               }
               first_row_obj = tb.rows[i].cells[0];
               first_row_title = first_new_row_title;
           }
       }
   }
</script>
</head>
<body onload="fixRowspan()">
<table width="100%" class="listtable" id="fileServerTable">
	<thead>
       <tr>
          <th width="30%">文件服务器</th>
          <th width="30%">地址</th>
          <th width="30%">状态</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="serverList != null ">
			<s:if test="serverList.size() > 0">
				<s:iterator value="serverList">
					<s:set var="isonline" value="#serverList.isonline"/>
					<tr>
						<td align="center">
							<s:property value="server_name" />
						</td>
						<td align="center">
						 	<s:if test='isonline=="1"'>
							 	<a href="javascript:fileDetail('<s:property value='id' />')"><s:property value="host" /></a>
							</s:if>
							<s:else>
								<s:property value="host" />
							</s:else>

						</td>
						<td align="center">
							 <s:if test='isonline=="1"'>
							 	<IMG SRC="<s:url value='/images/greenPoint.png'/>" WIDTH="20" HEIGHT="20" BORDER="0" >
							</s:if>
							<s:else>
								<IMG SRC="<s:url value='/images/grayPoint.png'/>" WIDTH="20" HEIGHT="20" BORDER="0" >
							</s:else>
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan=11 align=left> 没有查询到相关数据！ </td>
				</tr>
			</s:else>
		</s:if>
		<s:else>
			<tr>
				<td colspan=11 align=left> 没有查询到相关数据！ </td>
			</tr>
		</s:else>
	</tbody>
</table>
</body>
</html>
