package com.linkage.module.ids.bio;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class XMLUtil
{
	public static Document strXmlToDocument(String parseStrXml){  
        StringReader read = new StringReader(parseStrXml);    
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入    
        InputSource source = new InputSource(read);    
        //创建一个新的SAXBuilder    
        SAXBuilder sb = new SAXBuilder();   // 新建立构造器    
        Document doc = null;  
        try {  
            doc = sb.build(source);  
        } catch (JDOMException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }   
        return doc;  
          
    }  
	
	public static String getValueByElementName(Document doc,String finalNodeName){  
	       Element root = doc.getRootElement();  
	        HashMap<String,Object> map=new HashMap<String,Object>();  
	     //调用getChildAllText方法。获取目标子节点的值 
	       Map<String,Object> resultmap=getChildAllText(doc, root,map);     
	       String result=(String)resultmap.get(finalNodeName);  
	       return result;  
	    }  
	
	public static Map<String ,Object> getChildAllText(Document doc, Element e,HashMap<String,Object> resultmap)    
    {    
        if (e != null)    
        {    
            if (e.getChildren() != null)   //如果存在子节点  
            {    
                List<Element> list = e.getChildren();     
                for (Element el : list)    //循环输出  
                {    
                    if(el.getChildren().size() > 0)   //如果子节点还存在子节点，则递归获取  
                    {    
                        getChildAllText(doc, el,resultmap);    
                    }    
                    else    
                    {    
                        resultmap.put(el.getName(), el.getTextTrim());  //将叶子节点值压入map  
                    }    
                }    
            }    
        }    
        return resultmap;    
    }  
}
