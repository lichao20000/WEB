//<!--
//����Error�����toString����
Error.prototype.toString = function() {
	return "Error/Code: 0x"
		 + this.number.toString(16)
		 + "\nMessage: "
		 + this.description;
};
Error.prototype.print = function() {alert(this);}
//this.xmlHttp Object
//var this.xmlHttp = null;
function XmlHRequest(_url){
	this._HttpURL = _url;
	try{
		if(window.ActiveXObject){
			this.xmlHttp=new ActiveXObject("Microsoft.XMLHttp");
		}
		else{
			this.xmlHttp=new this.xmlHttpRequest();
		}
	}catch(ex){
		ex.print();
	}
};
var _p = XmlHRequest.prototype;
_p.setURL = function(_url){
	this._HttpURL = _url;
}
//����doService����
_p.doService = function(_state,_function){
	try{
		if(this.xmlHttp != null){
			this.xmlHttp.open(_state,this._HttpURL,true);
			this.xmlHttp.setRequestHeader("content-type","text/html");
			this.xmlHttp.onreadystatechange = _function;
			this.xmlHttp.send();
		}
	}catch(ex){
		ex.print()
	}
};
//�豸������Ϻ󴥷������¼�
_p.setReadyChange = function(_param){
	if(this.xmlHttp != null)
		this.xmlHttp.onreadystatechange = _param;
}
//�õ�������
_p.getResult = function(){
  try{
		if(this.xmlHttp!= null && this.xmlHttp.readyState == 4 && this.xmlHttp.status == 200){
			return new String(this.xmlHttp.responseText);
		}
	}catch(ex){
		ex.print();
	}
	return "";
};
//�õ�������XML
_p.getXML = function(){
  try{
		if(this.xmlHttp!= null && this.xmlHttp.readyState == 4 && this.xmlHttp.status == 200){
			return new String(this.xmlHttp.responseXML);
		}
	}catch(ex){
		ex.print();
	}
	return "";
};
//����get
_p.doGet = function(){
	if(arguments.length == 0)
		this.doService("GET",null);
	else
		this.doService("GET",arguments[0]);
};
//����post
_p.doPost = function(_function){
	if(arguments.length == 0)
		this.doService("POST",null);
	else
		this.doService("POST",arguments[0]);
};
_p.getReady = function(){
	try{
		if(this.xmlHttp!= null && this.xmlHttp.readyState == 4 && this.xmlHttp.status == 200){
			return true;
		}
	}catch(ex){
		ex.print();
		return false;
	}
}
//-->