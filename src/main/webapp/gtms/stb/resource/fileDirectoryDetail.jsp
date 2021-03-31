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


   //合并单元格
   function fixRowspan() {
       var tb = document.getElementById("fileServerDetailTable");
       var row_span_num = 1;
       var first_row_title = "";
       var first_row_obj = null;
       for ( var i = 1; i < tb.rows.length; i++) {
           var first_new_row_title = tb.rows[i].cells[1].innerHTML;
           if (first_row_title != "" && first_row_title == first_new_row_title) {
               tb.rows[i].deleteCell(1);
               row_span_num++;
               first_row_obj.setAttribute("rowSpan", row_span_num);
           } else {
               if (first_row_title != "") {
                   first_row_obj.setAttribute("rowSpan", row_span_num);
                   row_span_num = 1;
               }
               first_row_obj = tb.rows[i].cells[1];
               first_row_title = first_new_row_title;
           }
       }
   }

</script>
</head>
<body>
<table   class="listtable"  id="fileServerDetailTable">
	<thead>
       <tr>
          <th>名称</th>
          <th>地址</th>
          <th colspan="<s:property value="colspanNum"/>">路径</th>
       </tr>
	</thead>
	<tbody>
		<s:if test="fileDirectoryList != null ">
			<s:if test="fileDirectoryList.size() > 0">
				<s:iterator value="fileDirectoryList" status="status" var="list" >
					<tr>
						<s:if test='#status.first'>
							<td align="center" rowspan="<s:property value="rowspanNum"/>">
								<nobr><s:property value="server_name"/></nobr>
							</td>
							<td align="center" rowspan="<s:property value="rowspanNum"/>">
								<s:property value="host"/>
							</td>
						</s:if>
						<s:iterator value="fileDirectoryList[#status.index]" >
                			<td align="center"><s:property value="value"/></td>
            			</s:iterator>
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
