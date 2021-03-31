<%@ include file="../../timelater.jsp"%>
<%@ page contentType="text/html;charset=GBK"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="<s:url value="/css3/c_table.css"/>" type="text/css">
<link rel="stylesheet" href="<s:url value="/css3/global.css"/>" type="text/css">
<SCRIPT LANGUAGE="JavaScript" SRC="<s:url value="/Js/jquery.js"/>"></SCRIPT>
<script type="text/javascript" src="../../Js/My97DatePicker/WdatePicker.js"></script>

<TABLE>
    <tr>
        <td>
            <table class="green_gargtd">
                <tr>
                    <th>
                        ���������޸�IP�·�ͳ��
                    </th>
                    <td>
                        <img src="<s:url value="/images/attention_2.gif"/>" width="15" height="12">
                        ʱ��Ϊ����ʱ��
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <form name=frm>
                <table class="querytable">
                    <tr>
                        <th colspan=4>
                            VOIP�޸�IP�����·�ͳ��
                        </th>

                    </tr>
                    <tr bgcolor=#ffffff>
                        <td class=column align="center" width="15%">
                            ��ʼʱ��
                        </td>
                        <td width="35%">
                            <input type="text" name="starttime" class='bk' readonly value="<s:property value='starttime'/>">
                            <img name="shortDateimg"
                                 onClick="WdatePicker({el:document.frm.starttime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                 src="../../images/dateButton.png" width="15" height="12"
                                 border="0" alt="ѡ��" />
                        </td>
                        <td class=column align="center" width="15%">
                            ����ʱ��
                        </td>
                        <td width="35%">
                            <input type="text" name="endtime" class='bk' readonly value="<s:property value='endtime'/>">
                            <img name="shortDateimg"
                                 onClick="WdatePicker({el:document.frm.endtime,dateFmt:'yyyy-MM-dd HH:mm:ss',skin:'whyGreen'})"
                                 src="../../images/dateButton.png" width="15" height="12"
                                 border="0" alt="ѡ��" />
                        </td>
                    </tr>
                    <TR bgcolor="#FFFFFF" id="gwShare_tr21" STYLE="">
                        <td class="column" align="right" width="15%">
                            �豸����
                        </td>
                        <td width="35%" colspan="3">
                            <select name="cityId" class="bk">
                                <option value="-1">==��ѡ��==</option>
                            </select>
                        </td>

                    </TR>
                    <tr bgcolor=#ffffff>
                        <td class=foot colspan=4 align="right" padding="4px">
                            <input type="button" onclick="doQuery()" name="button" value="�� ѯ"/>
                        </td>
                    </tr>
                </table>
            </form>
        </td>
    </tr>

    <tr id="trData" style="display: none">
        <td class="colum">
            <div id="QueryData" style="width: 100%; z-index: 1; top: 100px">
                ����ͳ�ƣ����Ե�....
            </div>
        </td>
    </tr>
    <tr>
        <td height="20">
        </td>
    </tr>
</TABLE>

<%@ include file="/foot.jsp"%>
<script language="JavaScript">
    var flag = "";
    function init(){
        // ��ʼ������
        gwShare_change_select("city","00");
        initDate();
    }
    //��ʼ��ʱ��
    function initDate()
    {
        theday=new Date();
        day=theday.getDate();
        month=theday.getMonth()+1;
        year=theday.getFullYear();
        hour = theday.getHours();
        mimute = theday.getMinutes();
        second = theday.getSeconds();

        flag = '<s:property value="starttime"/>' ;
        if(null!=flag &&""!=flag){
            $("input[@name='starttime']").val('<s:property value="starttime"/>');
            $("input[@name='endtime']").val('<s:property value="endtime"/>');
        }else{
            //��ʼʱ��Ĭ��Ϊ����ĵ�һ��
            $("input[@name='starttime']").val(year+"-1-1 00:00:00");
            $("input[@name='endtime']").val(year+"-" + month + "-" + day + " " + hour +":"+ mimute +":" + second);
        }
    }
    function doQuery(){
        var cityId = $.trim($("select[@name='cityId']").val());                // �豸����
        var startTime = $.trim($("input[@name='starttime']").val());           // ��ʼʱ��(����ʱ��)
        var endTime = $.trim($("input[@name='endtime']").val());              // ����ʱ��(����ʱ��)

        $("tr[@id='trData']").show();
        $("button[@name='button']").attr("disabled", true);
        $("div[@id='QueryData']").html("����ͳ�ƣ����Ե�....");
        var url = '<s:url value='/itms/report/VoipXIPBSSReportJL!getVoipBSSDownStatistic.action'/>';
        $.post(url,{
            cityId : cityId,
            startTime : startTime,
            endTime : endTime
        },function(ajax){
            $("div[@id='QueryData']").html("");
            $("div[@id='QueryData']").append(ajax);
            $("button[@name='button']").attr("disabled", false);
        });
    }

    function viewDetail(cityId,startTimeSec,endTimeSec,type,totalNum) {
        var page="<s:url value='/itms/report/VoipXIPBSSReportJL!getVoipBSSDownInfo.action'/>?";
        var url = page
            + "cityId=" + cityId
            + "&startTimeSec=" + startTimeSec
            + "&endTimeSec=" + endTimeSec
            + "&type=" + type
            + "&totalNum=" + totalNum;
        window.open(url,"","left=50,top=50,height=550,width=900,toolbar=no,menubar=no,location=no,resizable=yes scrollbars=yes");

    }

    function ToExcel(cityId,startTimeSec,endTimeSec,type) {
        var page="<s:url value='/itms/report/VoipXIPBSSReportJL!getVoipBSSDownInfoExcel.action'/>?"
            + "cityId=" + cityId
            + "&startTimeSec=" + startTimeSec
            + "&endTimeSec=" + endTimeSec
            + "&type=" + type;
        document.all("childFrm").src=page;
    }

    /*------------------------------------------------------------------------------
    //������:		deviceSelect_change_select
    //����  :	type
                    vendor      �����豸����
                    deviceModel �����豸�ͺ�
                    devicetype  �����豸�汾
    //����  :	����ҳ������̡��ͺż�����
    //����ֵ:
    //˵��  :
    //����  :	Create 2009-12-25 of By qxq
    ------------------------------------------------------------------------------*/
    function gwShare_change_select(type,selectvalue){
        switch (type){
            case "city":
                var url = "<s:url value="/gwms/share/gwDeviceQuery!getCityNextChild.action"/>";
                $.post(url,{
                },function(ajax){
                    gwShare_parseMessage(ajax,$("select[@name='cityId']"),selectvalue);
                });
                break;
            default:
                alert("δ֪��ѯѡ�");
                break;
        }
    }

    function gwShare_parseMessage(ajax,field,selectvalue){
        var flag = true;
        if(""==ajax){
            return;
        }
        var lineData = ajax.split("#");
        if(!typeof(lineData) || !typeof(lineData.length)){
            return false;
        }
        field.html("");
        option = "<option value='-1' selected>==��ѡ��==</option>";
        field.append(option);
        for(var i=0;i<lineData.length;i++){
            var oneElement = lineData[i].split("$");
            var xValue = oneElement[0];
            var xText = oneElement[1];
            if(selectvalue==xValue){
                flag = false;
                //����ÿ��value��text��ǵ�ֵ����һ��option����
                option = "<option value='"+xValue+"' selected>=="+xText+"==</option>";
            }else{
                //����ÿ��value��text��ǵ�ֵ����һ��option����
                option = "<option value='"+xValue+"'>=="+xText+"==</option>";
            }
            try{
                field.append(option);
            }catch(e){
                alert("�豸�ͺż���ʧ�ܣ�");
            }
        }
        if(flag){
            field.attr("value","-1");
        }
    }


    init();

</script>

