<%@page contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="lk" uri="/linkage"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />

<title>������BSS����ͳ��</title>
<lk:res />
<script type="text/javascript">
	<!--
   $(function(){

        //��Ѱ������BSS����
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

        //����bss����
        $("#export").click(function(){

        	if(!validateTime()){
                return;
            }

           //��������
           $("#queryForm").attr("action",
                   "<s:url value='/gtms/stb/resource/stbBssQuery!exportBss.action'/>");
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
 //-->
</script>


</head>

<body>
	<TABLE width="98%" align="center" class="querytable">
		<TR>
			<TD >
				<IMG height=20 src="<s:url value='/images/bite_2.gif'/>" width=24>
	            	����ǰ��λ�ã�BSS������ѯ
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
				<td colspan="4" class="title_1">������BSS����ͳ��</td>
			</tr>
			</thead>
			<tr>
				<td width="15%"  class="title_2">���أ� </td>
				<td width="35%" ><select name="dto.cityId" id="cityId">
				    <option value='<s:property value="defaultCityId" />'  >��ѡ��</option>
					<s:iterator value="cityList" var="city">
						<option value="<s:property value="#city.city_id"/>"><s:property value="#city.space" escapeHtml="false" /><s:property value="#city.city_name" /></option>
					</s:iterator>
				</select></td>
				<td  class="title_2">�Ƿ�����¼����أ�</td>
				<td> <input type="radio"
					name="dto.subordinate" value="1" checked="checked"/>�� <input type="radio"
					name="dto.subordinate" value="2"   />��</td>
			</tr>
			<tr>

				<td  class="title_2">��ʼʱ�䣺</td>
				<td><lk:date id="fromTime" name="dto.fromTime" type="day" /></td>
				<td  class="title_2">����ʱ�䣺</td>
				<td><lk:date id="toTime" name="dto.toTime" type="day" /></td>
			</tr><tr>
			<td  class="title_2">ҵ���ʺţ�</td>
				<td> <input type="text" style="width:150px;"
					name="dto.servAccount" id="servAccount" /></td>

			<td  class="title_2">��ƷID</td>
				<td> <input type="text" style="width:150px;"
					name="dto.prodId" id="prodId" /></td>

			</tr>
			<tr>
				<td   class="title_2">�������ͣ�</td>
				<td colspan="3" >
				<input type="checkbox"	value="0" name="dto.opera" />����
				<input type="checkbox" value="1"  name="dto.opera" />����
				<input type="checkbox" value="2"  name="dto.opera" />�ƻ�
				<input type="checkbox" value="3"  name="dto.opera" />����
				</td>
			</tr>
			<tr>
				<td class="foot" colspan="4" >
					<div style="float:right">
						<button id="query">��ѯ</button>
						<button id="export">����</button>
					</div>
				</td>
			</tr>
		</table>


		  <iframe id="resultFrame" name="resultFrame"  width="100%" frameborder="0" scrolling="no" align="center"></iframe>
		  <!-- ���ݼ�����ʾ -->
		   	<div style="width:100%;display:none;text-align: center;" id="tip_loading">
				���ڼ�������,�����ĵȴ�......
			</div>
		</form>
		</td>
	</tr>
	</table>
</body>
</html>
