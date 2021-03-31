<html>
<body>
<!--作者： 王志猛  版权：联创科技 日期： 2006/12/7 -->
<%
String filename = (String)request.getParameter("filename");//告警声音文件的位置
String state = (String)request.getParameter("state");//告警声音的状态， on为开启。off为关闭
//告警声音的状态，是否循环播放 0为是，1为播放1次
int loop =Integer.parseInt((String)request.getParameter("loop"));
loop = loop==0?1:0;//数据库中规定0为发声一次，1为一直发生
%>
<object id="MediaPlayer1" width="460" height="68" classid="CLSID:22d6f312-b0f6-11d0-94ab-0080c74c7e95" 
codebase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.cab#Version=6,4,5,715" 
align="baseline" border="0" standby="Loading Microsoft Windows Media Player components..." 
type="application/x-oleobject" style="display:none">
    <param name="invokeURLs" value="0">
    <param name="FileName" value="../webtopo/sound/<%=filename%>">
    <param name="ShowControls" value="1">
    <param name="ShowPositionControls" value="0">
    <param name="ShowAudioControls" value="1">
    <param name="ShowTracker" value="1">
    <param name="ShowDisplay" value="0">
    <param name="ShowStatusBar" value="1">
    <param name="AutoSize" value="0">
    <param name="ShowGotoBar" value="0">
    <param name="ShowCaptioning" value="0">
    <param name="AutoStart" value="1">
    <param name="PlayCount" value="<%=loop%>">
    <param name="AnimationAtStart" value="0">
    <param name="TransparentAtStart" value="0">
    <param name="AllowScan" value="0">
    <param name="EnableContextMenu" value="1">
    <param name="ClickToPlay" value="0">
    <param name="DefaultFrame" value="datawindow">
    <embed   type="application/x-mplayer2" src="<%=filename%>"  pluginspage="http://www.microsoft.com/isapi/redir.dll?prd=windows&amp;sbp=mediaplayer&amp;ar=media&amp;sba=plugin&amp;"
        name="MediaPlayer" showcontrols="1" showpositioncontrols="0"
        showaudiocontrols="1" showtracker="1" showdisplay="0"
        showstatusbar="1"
        autosize="0"
        showgotobar="0" showcaptioning="0" autostart="1" autorewind="0"
        animationatstart="0" transparentatstart="0" allowscan="1"
        enablecontextmenu="1" clicktoplay="0" 
        defaultframe="datawindow" invokeurls="0" style="display:none">
    </embed>
</object>
<script language="javascript" type="text/javascript">
var state  = "<%=state%>";
if(state=="off")
{
	setTimeout("document.getElementById(\"MediaPlayer1\").stop();",1000);
}

</script>
</body>
</html>