//var xmlCodeDoc=new ActiveXObject("Microsoft.XMLDOM");
var xmlCodeDoc = null;


try{  
	  if (window.ActiveXObject){  
		  xmlCodeDoc= new ActiveXObject("Microsoft.XMLDOM");  
		  xmlCodeDoc.async = false;    
		  xmlCodeDoc.load("../Js/code.xml");   
	  }   
	  else if  
	     (document.implementation&& document.implementation.createDocument){  
	        try{    
	            xmlCodeDoc = document.implementation.createDocument('', '', null);    
	            xmlCodeDoc.async = false;    
	            xmlCodeDoc.load("../Js/code.xml");    
	        } catch(e){    
	            var xmlhttp = new window.XMLHttpRequest();    
	            xmlhttp.open("GET","../Js/code.xml",false);    
	            xmlhttp.send(null);    
	            xmlCodeDoc = xmlhttp.responseXML;    
	        }
	        
	           //firefox并不支持selectSingleNode和selectNodes方法;下面两段是用XPath来解决firefox模拟selectSingleNode和selectNodes方法,正确性有待解决  
            XMLDocument.prototype.selectSingleNode = Element.prototype.selectSingleNode = function (xpath){  
                 var  x = this .selectNodes(xpath)  
                 if ( ! x || x.length < 1 ) return   null ;  
                 return  x[ 0 ];  
            }  
            XMLDocument.prototype.selectNodes = Element.prototype.selectNodes = function (xpath){  
                 var  xpe  =   new  XPathEvaluator();  
                 var  nsResolver  =  xpe.createNSResolver( this .ownerDocument  ==   null   ?  
                     this .documentElement :  this .ownerDocument.documentElement);  
                 var  result  =  xpe.evaluate(xpath,  this , nsResolver,  0 ,  null );  
                 var  found  =  [];  
                 var  res;  
                 while  (res  =  result.iterateNext())  
                    found.push(res);  
                 return  found;  
            }  
              
	  }  
	  else{  
	      alert("load data error");  
	  }  
  }  
  catch(e){  
    alert(e.message);  
  } 

//xmlCodeDoc.async=false;

//xmlCodeDoc.load("../Js/code.xml");



function c_select(name,type,istitle,value){

	var sHtml = "<select name='"+name+"' class=bk>";

	if(istitle) sHtml += "<option value='0'>==请选择==</option>";

	var oNodeXml = xmlCodeDoc.selectNodes("//"+type);

	if(oNodeXml != null){		

		for(var i=0;i<oNodeXml.length;i++){
			
			try{    
				v = oNodeXml.item(i).getAttribute("sid");
			}catch(e){  
				v = oNodeXml[i].getAttribute("sid");
			} 
			

			if(v==value){

				sHtml += "<option value='"+v+"' selected>";

			}

			else{

				sHtml += "<option value='"+v+"'>";

			}

			try{
				sHtml += oNodeXml.item(i).value + "</option>";
			}catch(e){  
				sHtml += oNodeXml[i].getAttribute("value") + "</option>";
			} 

		}

	}

	sHtml += "</select>";

	document.write(sHtml);

}



function c_select_no(name,type,istitle,value){

	var sHtml = "<select name='"+name+"' class=bk>";

	if(istitle) sHtml += "<option value='0'>==请选择==</option>";

	var oNodeXml = xmlCodeDoc.selectNodes("//"+type);

	if(oNodeXml != null){		

		for(var i=0;i<oNodeXml.length;i++){

			v = oNodeXml.item(i).getAttribute("tid");

			if(v==value){

				sHtml += "<option value='"+v+"' selected>";

			}

			else{

				sHtml += "<option value='"+v+"'>";

			}

			sHtml += oNodeXml.item(i).getAttribute("value") + "</option>";

		}

	}

	sHtml += "</select>";

	//alert(sHtml);

	document.write(sHtml);

}



function c_select_hascng(name,type,istitle,value){

	var sHtml = "<select name='"+name+"' class=bk onchange=\"change()\">";

	if(istitle) sHtml += "<option value='0'>==请选择==</option>";

	var oNodeXml = xmlCodeDoc.selectNodes("//"+type);

	if(oNodeXml != null){		

		for(var i=0;i<oNodeXml.length;i++){

			v = oNodeXml.item(i).getAttribute("sid");

			if(v==value){

				sHtml += "<option value='"+v+"' selected>";

			}

			else{

				sHtml += "<option value='"+v+"'>";

			}

			sHtml += oNodeXml.item(i).getAttribute("value") + "</option>";

		}

	}

	sHtml += "</select>";

	document.write(sHtml);

}



function c_radio(name,type,value){

	var oNodeXml = xmlCodeDoc.selectNodes("//"+type);

	var sHtml = "";

	if(oNodeXml != null){

		for(var i=0;i<oNodeXml.length;i++){

			v = oNodeXml.item(i).getAttribute("sid");

			if(v==value)

				sHtml += "<input type='radio' name='"+ name +"' value='"+ v +"' checked>";

			else

				sHtml += "<input type='radio' name='"+ name +"' value='"+ v +"'>";



			sHtml += "&nbsp;"+ oNodeXml.item(i).getAttribute("value");

		}

	}

	else{

		sHtml = "<input type='hidden' name='"+name+"' value='0'>";

	}



	document.write(sHtml);

}



function getCodeName(type,value,isNo){

	var oNodeXml = xmlCodeDoc.selectNodes("//"+type);

	if(oNodeXml == null) return "";

	for(var i=0;i<oNodeXml.length;i++){

		
		try{    
			if(isNo != null)
				v = parseInt(oNodeXml.item(i).getAttribute("tid"));
			else
				v = parseInt(oNodeXml.item(i).getAttribute("sid"));
			if(v == parseInt(value)) return oNodeXml.item(i).getAttribute("value");
		}catch(e){  
			if(isNo != null)
				v = parseInt(oNodeXml[i].getAttribute("tid"));
			else
				v = parseInt(oNodeXml[i].getAttribute("sid"));
			if(v == parseInt(value)) return oNodeXml[i].getAttribute("value");
		} 

	}

	return "未知";

}

