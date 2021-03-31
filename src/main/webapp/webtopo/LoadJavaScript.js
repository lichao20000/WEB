//include脚本文件
function IncludeJS(sId, fileUrl, source) 
{ 
    if ((source != null) && (!document.getElementById( sId ))){ 
        var oHead = document.getElementsByTagName('HEAD').item(0); 
        var oScript = document.createElement( "script" ); 
        oScript.language = "javascript"; 
        oScript.type = "text/javascript"; 
        oScript.id = sId; 
        oScript.defer = true; 
        oScript.text = source; 
        oHead.appendChild( oScript ); 
    } 
}
function GetHttpRequest() 
{ 
    if ( window.XMLHttpRequest ) // Gecko 
        return new XMLHttpRequest() ; 
    else if ( window.ActiveXObject ) // IE 
        return new ActiveXObject("MsXml2.XmlHttp") ; 

}
//得到xmlHttpRequest对象
function GetHttpRequestH(){
	try{
		if(window.ActiveXObject){
			return new ActiveXObject("Microsoft.XMLHttp");
		}
		else{
			return new this.xmlHttpRequest();
		}
	}catch(e){
		return null;
	}
}
//动态加载脚本文件并eval
function OnLoadJS(sId, url){ 
    var oXmlHttp = GetHttpRequestH();
	if(oXmlHttp == null){
		alert("Error XMLHttp!");
		return ;
	}
	try{
		oXmlHttp.OnReadyStateChange = function() 
		{ 
			if ( oXmlHttp.readyState == 4 ) 
			{
				if ( oXmlHttp.status == 200 || oXmlHttp.status == 304 ) 
				{
					IncludeJS( sId, url, oXmlHttp.responseText);
				} 
				else 
				{ 
					alert( 'XML request error: ' + oXmlHttp.statusText + ' (' + oXmlHttp.status + ')' ) ; 
				} 
			} 
		} 
		oXmlHttp.open('GET', url, true); 
		oXmlHttp.send(null); 
	}catch(e){
		alert(e.message);
	}
} 