package com.linkage.module.ids.bio;

import java.util.Map;

import org.jdom.Document;

import com.linkage.commons.util.StringUtil;


public class test
{
	public static void main(String[] args) {  
        StringBuilder soap=new StringBuilder(); //构造请求报文  
        /*soap.append(" <soapenv:Envelope  xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");  
        soap.append(" xmlns:mo=\"http://mo.nbi.hdm.alcatel.com\">");
        soap.append(" <soapenv:Header/>");  
        soap.append(" <soapenv:Body>");  
        soap.append(" <mo:startGetUserInfoDiag>");
        soap.append(" <mo:op_id>111111</mo:op_id>");
        soap.append(" <mo:logic_id></mo:logic_id>");
        soap.append(" <mo:ppp_usename>02357586887</mo:ppp_usename>");  
        soap.append(" <mo:customer_id>1</mo:customer_id>");
        soap.append(" <mo:serial_number>433005CB395CB9D39</mo:serial_number>");
        soap.append(" <mo:auth_username>1</mo:auth_username>");
        soap.append(" </mo:startGetUserInfoDiag>");  
        soap.append(" </soapenv:Body>");
        soap.append(" </soapenv:Envelope>"); */
        soap.append(" <soapenv:Envelope  xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ");  
        soap.append(" xmlns:mo=\"http://mo.main.dispatch.itms.linkage.com\">");
        soap.append(" <soapenv:Header/>");  
        soap.append(" <soapenv:Body>");  
        soap.append(" <mo:startGetUserInfoDiag>");
        soap.append(" <mo:op_id>111111</mo:op_id>");
        soap.append(" <mo:logic_id></mo:logic_id>");
        soap.append(" <mo:ppp_usename>02357586887</mo:ppp_usename>");  
        soap.append(" <mo:customer_id>1</mo:customer_id>");
        soap.append(" <mo:serial_number>433005CB395CB9D39</mo:serial_number>");
        soap.append(" <mo:auth_username>1</mo:auth_username>");
        soap.append(" </mo:startGetUserInfoDiag>");  
        soap.append(" </soapenv:Body>");
        soap.append(" </soapenv:Envelope>"); 
        String requestSoap=soap.toString();  
          //http://136.3.218.66:8180/nbitktws
        String serviceAddress="http://136.3.218.66:8180/nbitktws/services/MobileOperationInterfaceService?wsdl";   //服务地址(将XXXX替换成自己项目的地址)  
        String charSet="utf-8";  
        String contentType="text/xml; charset=utf-8";  
  
               //第一步：调用方法getResponseSoap。返回响应报文和状态码</span>  
  
              Map<String,Object> responseSoapMap=SoapUtil.responseSoap(requestSoap, serviceAddress, charSet, contentType);  
              Integer statusCode=(Integer)responseSoapMap.get("statusCode");  
              if(statusCode==200){  
              	String responseSoap=(String)responseSoapMap.get("responseSoap");  
              	String targetNodeName="isSuccess";  
                //第二步：调用strXmlToDocument方法。  将字符串类型的XML的响应报文 转化成Docunent结构文档
                Document doc=XMLUtil.strXmlToDocument(responseSoap);    
                //第三步：调用getValueByElementName方法。递归获得目标节点的值</span>  
                String result= XMLUtil.getValueByElementName(doc,targetNodeName);  
                if(!StringUtil.IsEmpty(result)){                              
                	System.out.println(result);  
                }    
              else{                            
                   System.out.println("没有此节点或者没有值！");}  
                }  
              else{  
                 System.out.println("请求失败！");  
               }                   
        }  
}
