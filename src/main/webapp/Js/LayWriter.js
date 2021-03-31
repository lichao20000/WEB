var ns4 = (document.layers)? true:false;

var ie4 = (document.all)? true:false;



function layerWrite(id,nestref,text) 

{

	if (ns4) {

		var lyr = (nestref)? eval('document.'+nestref+'.document.'+id+'.document') : document.layers[id].document

		lyr.open()

		lyr.write(text)

		lyr.close()

	}

	else if (ie4) document.all[id].innerHTML = text

}

function InitXmlParse(idGroupList)

{

	var strXSLSrc = "../Js/myviewdata.xsl";

	objXSLDoc = new ActiveXObject("Microsoft.XMLDOM");

	objXSLDoc.async = false;

	objXSLDoc.load( strXSLSrc );

	objXMLDoc = new ActiveXObject("Microsoft.XMLDOM");

	objXMLDoc.async = false;

	var strXMLSrc = idGroupList.XMLSrc;

	objXMLDoc.load( strXMLSrc );

	var sHtml = objXMLDoc.transformNode( objXSLDoc );

	if(sHtml==null) alert("parse xml error!");

	idGroupList.innerHTML = sHtml;

}

//行选种判断

function getSelectValue(Title)

{

	var o = document.all(Title);

	if(o==null) return null;

	if(typeof(o[0]) != "object")

	{

		if(o.checked)

			return o.value;

		else

			return  -1;

	}



	for(var i=0; i<o.length; i++)

	{

		if(o[i].checked)

			return o[i].value;

	}

	return -1;

}

//隐藏相应的对象

function EC(leaf,obj)

{



	pobj = obj.offsetParent;

	oTRs = pobj.getElementsByTagName("TR");



	var m_bShow;



	for(var i=0; i<oTRs.length; i++){



		if(oTRs[i].leaf == leaf){

			m_bShow = (oTRs[i].style.display=="none");

			oTRs[i].style.display = m_bShow?"":"none";

		}

	}

	sobj = obj.getElementsByTagName("IMG");

	//window.alert(sobj[0].src);

	//window.alert(oTRs.length);

	if(m_bShow) {

		sobj[1].src = "images/up_enabled.gif";

		obj.style.backgroundColor = "#A0C6E5";

	}

	else{

		sobj[1].src = "images/down_enabled.gif";

		obj.style.backgroundColor = "#cccccc";

	}

}