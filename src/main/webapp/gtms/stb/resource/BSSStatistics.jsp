<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<title>零配置BSS工单统计</title>
<lk:res />
<script type="text/javascript">
	<!--
   $(function(){

        //查寻零配置BSS工单
        $("#query").click(function(){

            if(!validateTime()){
                  return;
            }

            $("#resultFrame").hide("");
            $("#tip_loading").show();

            var url="<s:url value='/gtms/stb/resource/stbBssQuery!query.action'/>";

            $("#queryForm").attr("action",url);
            $("#queryForm").attr("target","resultFrame");
            $("#queryForm").submit();

        });

        //导出bss工单
        $("#export").click(function(){

        	if(!validateTime()){
                return;
            }

           //导出工单
           $("#queryForm").attr("action",
                   "<s:url value='/gtms/stb/resource/stbBssQuery!exportBss.action'/>");
           $("#queryForm").submit();

        });

   })

   /**
   *验证开始和结束时间
   *开始时间大于结束时间返回false
   */
   function validateTime(){
     var beginTime=$("#fromTime").val();
     var endTime=$("#toTime").val();

     if(beginTime>endTime){
          alert("开始时间不能在结束时间之后");
          return false;
     }
     return true;
   }


   function setDataSize(dataHeight){
		$("#resultFrame").height(dataHeight);
	}

   function showIframe(){
		$("#tip_loading").hide();
		$("#resultFrame").show();
	}
 //-->
</script>


</head>

<body>
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD >
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
	            	您当前的位置：BSS工单查询
			</TD>
		</TR>
	</TABLE>

	<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<form id="queryForm" name="queryForm">
		<table id="tblQuery" width="98%" class="querytable">
		  <thead>
		   <tr>
				<td colspan="4" class="title_1">零配置BSS工单统计</td>
			</tr>
			</thead>
			<tr>
				<td width="15%"  class="title_2">属地： </td>
				<td width="35%" ><select name="dto.cityId" id="cityId">
				    <option value='<s:property value="defaultCityId" />'  >请选择</option>
					<s:iterator value="cityList" var="city">
						<option value="<s:property value="#city.city_id"/>"><s:property value="#city.space" escapeHtml="false" /><s:property value="#city.city_name" /></option>
					</s:iterator>
				</select></td>
				<td  class="title_2">是否包含下级属地：</td>
				<td> <input type="radio"
					name="dto.subordinate" value="1" checked="checked"/>是 <input type="radio"
					name="dto.subordinate" value="2"   />否</td>
			</tr>
			<tr>

				<td  class="title_2">开始时间：</td>
				<td><lk:date id="fromTime" name="dto.fromTime" type="day" /></td>
				<td  class="title_2">结束时间：</td>
				<td><lk:date id="toTime" name="dto.toTime" type="day" /></td>
			</tr><tr>
			<td  class="title_2">业务帐号：</td>
				<td> <input type="text" style="width:150px;"
					name="dto.servAccount" id="servAccount" /></td>

			<td  class="title_2">产品ID</td>
				<td> <input type="text" style="width:150px;"
					name="dto.prodId" id="prodId" /></td>

			</tr>
			<tr>
				<td   class="title_2">操作类型：</td>
				<td colspan="3" >
				<input type="checkbox"	value="0" name="dto.opera" />开户
				<input type="checkbox" value="1"  name="dto.opera" />销户
				<input type="checkbox" value="2"  name="dto.opera" />移机
				<input type="checkbox" value="3"  name="dto.opera" />修正
				</td>
			</tr>
			<tr>
				<td class="foot" colspan="4" >
					<div style="float:right">
						<button id="query">查询</button>
						<button id="export">导出</button>
					</div>
				</td>
			</tr>
		</table>


		  <iframe id="resultFrame" name="resultFrame"  width="100%" frameborder="0" scrolling="no" align="center"></iframe>
		  <!-- 数据加载提示 -->
		   	<div style="width:100%;display:none;text-align: center;" id="tip_loading">
				正在加载数据,请耐心等待......
			</div>
		</form>
		</td>
	</tr>
	</table>
</body>
</html>
