<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>���������ÿ�ͨͳ��</title>
<lk:res />
<script type="text/javascript">
   $(function(){
        //��Ѱ���������ÿ�ͨͳ��
        $("#query").click(function(){
            if(!validateTime()){
                  return;
            }
            $("#resultFrame").hide("");
            $("#tip_loading").show();
            var url="<s:url value='/gtms/stb/zeroconf/zeroConfStatisticsReportQuery!getZeroConfStatisticsReportByCityid.action'/>";
            $("#queryForm").attr("action",url);
            $("#queryForm").attr("target","resultFrame");
            $("#queryForm").submit();
        });

   })
   /**
   *��֤��ʼ�ͽ���ʱ��
   *��ʼʱ����ڽ���ʱ�䷵��false
   */
   function validateTime(){
     var beginTime=$("#fromTime").val();
     var endTime=$("#toTime").val();

     if(beginTime>endTime){
          alert("��ʼʱ�䲻���ڽ���ʱ��֮��");
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
</script>
</head>
<body>
	<form id="queryForm" name="queryForm">
		<table id="tblQuery" width="98%" class="querytable">
			<thead>
				<tr>
					<td colspan="4" class="title_1">���������ÿ�ͨͳ��</td>
				</tr>
			</thead>
			<tr style="display: none;">
				<td width="15%" class="title_2">���أ�</td>
				<td width="35%"><select name="dto.cityId" id="cityId">
						<option value='<s:property value="defaultCityId" />'>��ѡ��</option>
						<s:iterator value="cityList" var="city">
							<option value="<s:property value="#city.city_id"/>">
								<s:property value="#city.space" escapeHtml="false" />
								<s:property value="#city.city_name" />
							</option>
						</s:iterator>
				</select></td>
				<td></td>
				<td></td>
			</tr>
			<tr>
				<td class="title_2">��ʼʱ�䣺</td>
				<td><lk:date id="fromTime" name="dto.fromTime" type="day" /></td>
				<td class="title_2">����ʱ�䣺</td>
				<td><lk:date id="toTime" name="dto.toTime" type="day" /></td>
			</tr>
			<tr>
				<td class="title_3" colspan="4">
					<div style="float: right">
						<button id="query">��ѯ</button>
					</div>
				</td>
			</tr>
		</table>
		<iframe id="resultFrame" name="resultFrame" width="98%"
			frameborder="0" scrolling="no" align="center"></iframe>
		<!-- ���ݼ�����ʾ -->
		<div style="width: 96%; display: none; text-align: center;"
			id="tip_loading">
			<img src='/lims/images/loading.gif ' />���ڼ�������,�����ĵȴ�......
		</div>
	</form>
</body>
</html>
