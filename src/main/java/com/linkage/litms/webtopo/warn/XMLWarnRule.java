package com.linkage.litms.webtopo.warn;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class XMLWarnRule {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(XMLWarnRule.class);
  public ArrayList RuleList = new ArrayList();
  public ArrayList RuleNameList = new ArrayList();
  public HashMap Name_ContentORMap = new HashMap();
  public HashMap Name_ContentANDMap = new HashMap();
  public XMLWarnRule() {

  }

  public void ReadXMLFile(String inFile) {
//为解析XML作准备，创建DocumentBuilderFactory实例,指定DocumentBuilder
    Document doc = null;

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
    }
    catch (ParserConfigurationException pce) {
        logger.warn(pce.getMessage());
        return ;

    }

    try {
      java.io.Reader reader = new FileReader(inFile);
      org.xml.sax.InputSource source = new InputSource(reader);
      doc = db.parse(source);
    }
    catch (Exception e) {
      logger.warn(e.getMessage());
      return ;
    }

    try {
      Element root = doc.getDocumentElement();
      if (root == null) {
        return;
      }
      //取该用户的所有的告警列表
      NodeList rules1 = root.getElementsByTagName("Rule");
      if (rules1 != null) {
        for (int m = 0; m < rules1.getLength(); m++) {
          Element ruleWarn = (Element) rules1.item(m);

          NodeList rules = ruleWarn.getElementsByTagName("content");
          //首先清除所有的规则
          RuleList.clear();
          RuleNameList.clear();
          Name_ContentORMap.clear();
          Name_ContentANDMap.clear();

          for (int i = 0; i < rules.getLength(); i++) {
            //依次取每个规则元素
            Element ruleItem = (Element) rules.item(i);
            //创建一个用户规则的实例
            WarnRuleBean rule = new WarnRuleBean();
            String ruleName = "";
            int priority = -1;
            //取"规则名称"元素，下面类同
            NodeList names = ruleItem.getElementsByTagName("name");
            if (names.getLength() == 1) {
              Element e = (Element) names.item(0);
              Text t = (Text) e.getFirstChild();
              rule.setName(t.getNodeValue());
              ruleName = t.getNodeValue();
            }

            NodeList prioritys = ruleItem.getElementsByTagName("priority");
            if (prioritys.getLength() == 1) {
              Element e = (Element) prioritys.item(0);
              Text t = (Text) e.getFirstChild();
              rule.setPriority(Integer.parseInt(t.getNodeValue()));
              priority = Integer.parseInt(t.getNodeValue());
            }

            NodeList descs = ruleItem.getElementsByTagName("desc");
            if (descs.getLength() == 1) {
              Element e = (Element) descs.item(0);
              Text t = (Text) e.getFirstChild();
              if (t != null) {
                rule.setDesc(t.getNodeValue());
              }
              else {
                rule.setDesc(" ");
              }
            }

            String content = "";
            NodeList texts = ruleItem.getElementsByTagName("text");
            if (texts.getLength() == 1) {
              Element e = (Element) texts.item(0);
              Text t = (Text) e.getFirstChild();
              content = t.getNodeValue();
              content = content.replaceAll("小于","<");
              rule.setContent(content);
              
            }
            //logger.debug(ruleName + ":" + content);
            //对规则内容的处理,将or条件和and条件分开,在过滤的时候先过滤or哈西表中的，再过滤and哈西表中的
            if (content != null && !content.equals("")) {
              //首先对或条件进行优化
              String[] mystr = content.split("or");
              if (mystr != null && mystr.length > 0) {
                ArrayList andStrList = new ArrayList();
                ArrayList orList = new ArrayList();
                ArrayList andList = new ArrayList();
                for (int j = 0; j < mystr.length; j++) {
                  String logic = mystr[j];
                  logger.debug("logic:" + logic);
                  if (logic.indexOf("and") >= 0) {
                    andStrList.add(logic);
                  }
                  else {
                    orList.add(logic);
                  }
                }

                //其次对每一组与条件进行加入
                for (int j = 0; j < andStrList.size(); j++) {
                  String andStr = (String) andStrList.get(j);
                  String[] andStrs = andStr.split("and");
                  for (int s = 0; s < andStrs.length; s++) {
                    andList.add(andStrs[s]);
                  }
                }

                andStrList.clear();
                andStrList = null;
                Name_ContentORMap.put(ruleName, orList);
                Name_ContentANDMap.put(ruleName, andList);
              }
            }

            NodeList results = ruleItem.getElementsByTagName("result");
            if (results.getLength() == 1) {
              Element e = (Element) results.item(0);
              Text t = (Text) e.getFirstChild();
              rule.setResult(t.getNodeValue());
            }

            if (!ruleName.equals("")) {
              RuleNameList.add(ruleName);
            }
            RuleList.add(priority, rule);
          }
          //logger.debug("规则的长度是:" + RuleList.size());
          //logger.debug("规则名称的长度是:" + RuleNameList.size());
          //logger.debug("或的长度是:" + Name_ContentORMap.size());
          //logger.debug("并的长度是:" + Name_ContentANDMap.size());

        }

      }
      if (doc != null) {
        doc = null;
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void GenerateXMLFile(String outFile, ArrayList rulesList) {
    //创建文件
    MyFile.clearFile(outFile);
    //创建写文件
    BufferedWriter xmlWriter = MyFile.openAppendWriter(outFile);
    MyFile.WriteXml(xmlWriter, "<?xml version=\"1.0\" encoding=\"GB2312\"?> ");
    MyFile.WriteXml(xmlWriter, "<WarnRule>");
    MyFile.WriteXml(xmlWriter, "<Rule>");
    for (int i = 0; i < rulesList.size(); i++) {
      WarnRuleBean rule = (WarnRuleBean) rulesList.get(i);
      if (rule != null) {
        String content=rule.getContent();
        content=content.replaceAll("<","小于");
        MyFile.WriteXml(xmlWriter, "<content>");
        MyFile.WriteXml(xmlWriter, "<name>" + rule.getName() + "</name>");
        MyFile.WriteXml(xmlWriter,
                        "<priority>" + rule.getPriority() + "</priority>");
        MyFile.WriteXml(xmlWriter, "<desc>" + rule.getDesc() + "</desc>");
        MyFile.WriteXml(xmlWriter, "<text>" + content + "</text>");
        MyFile.WriteXml(xmlWriter, "<result>" + rule.getResult() + "</result>");
        MyFile.WriteXml(xmlWriter, "</content>");
      }
    }
    MyFile.WriteXml(xmlWriter, "</Rule>");
    MyFile.WriteXml(xmlWriter, "</WarnRule>");
    MyFile.closeWriter(xmlWriter);
  }

  public boolean WriteXMLFile(String outFile, ArrayList rulesList) {
//为解析XML作准备，创建DocumentBuilderFactory实例,指定DocumentBuilder
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
    }
    catch (ParserConfigurationException pce) {
      pce.printStackTrace();
      return false;
    }

    Document doc = null;
    doc = db.newDocument();

    Element root = doc.createElement("WarnRule");
    //根元素添加上文档
    doc.appendChild(root);

    //判断该用户是否已经有了规则定义，如果没有的话，添加
    Element FirstElement = null;
    FirstElement = doc.createElement("Rule");
    //FirstElement.setAttribute("account", "sss");
    root.appendChild(FirstElement);

    //logger.debug("0000000000");
    for (int i = 0; i < rulesList.size(); i++) {
      logger.debug("11111111111111111111");
      //增加content节点
      Element contontElement = doc.createElement("content");
      FirstElement.appendChild(contontElement);

      WarnRuleBean rule = (WarnRuleBean) rulesList.get(i);
      if (rule != null) {
        //logger.debug("111111111");
        //依次添加该规则的每一项
        Element name = doc.createElement("name");
        contontElement.appendChild(name);
        Text tName = doc.createTextNode(rule.getName());
        name.appendChild(tName);

        Element priority = doc.createElement("priority");
        contontElement.appendChild(priority);
        Text tPriority = doc.createTextNode(String.valueOf(rule.getPriority()));
        priority.appendChild(tPriority);

        Element desc = doc.createElement("desc");
        contontElement.appendChild(desc);
        Text tDesc = doc.createTextNode(rule.getDesc());
        desc.appendChild(tDesc);

        String content=rule.getContent();
        content=content.replaceAll("<","小于");
        Element text = doc.createElement("text");
        contontElement.appendChild(text);
        Text tText = doc.createTextNode(content);
        text.appendChild(tText);

        Element result = doc.createElement("result");
        contontElement.appendChild(result);
        Text tResult = doc.createTextNode(rule.getResult());
        result.appendChild(tResult);
      }
    }

    FileOutputStream outStream=null;
    OutputStreamWriter outWriter=null;
    try {
      outStream = new FileOutputStream(outFile);
      outWriter = new OutputStreamWriter(outStream);
      //XmlDocument xmldoc=(XmlDocument) doc;
      //xmldoc.write(outWriter, "GB2312");
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }finally{
    	try {
    		if(outStream!=null){
    			outStream.close();
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	try {
    		if(outWriter!=null){
    			outWriter.close();
    		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    return true;
  }

  public static void main(String[] args) {
    //建立测试实例
    XMLWarnRule xmlTest = new XMLWarnRule();
    //初始化向量列表
    try {
      logger.debug("开始读Input.xml文件");
      //xmlTest.readXMLFile("./data/warnrule.xml");

      logger.debug("读入完毕,开始写Output.xml文件");
      xmlTest.WriteXMLFile("./data/warnrule.xml", null);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    logger.debug("写入完成");

  }

}