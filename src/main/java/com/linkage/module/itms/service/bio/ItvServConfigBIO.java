package com.linkage.module.itms.service.bio;

import com.linkage.commons.util.StringUtil;
import com.linkage.litms.LipossGlobals;
import com.linkage.litms.common.util.DateTimeUtil;
import com.linkage.module.ids.util.WSClientUtil;
import com.linkage.module.itms.service.dao.ItvServConfigDAO;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ItvServConfigBIO {
    private static Logger logger = LoggerFactory.getLogger(ItvServConfigBIO.class);

    private ItvServConfigDAO dao;

    public ArrayList<HashMap<String, String>> query(String loid)
    {
        return dao.query(loid);
    }

    public String config(String loid) {
        Map<String,String> map = new HashMap<String, String>();
        DateTimeUtil dt = new DateTimeUtil();
        String cmdId = StringUtil.getStringValue(dt.getLongTime());
        StringBuffer inParam = new StringBuffer();
        inParam.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>	\n");
        inParam.append("<root>										\n");
        inParam.append("	<CmdID>").append(cmdId).append("</CmdID>						\n");
        inParam.append("	<CmdType>CX_01</CmdType>					\n");
        inParam.append("	<ClientType>3</ClientType>					\n");
        inParam.append("	<Param>									\n");
        inParam.append("		<UserInfoType>1</UserInfoType>		\n");
        inParam.append("        <UserInfo>").append(loid).append("</UserInfo>      \n");
        inParam.append("	</Param>								\n");
        inParam.append("</root>										\n");
        logger.warn("http:"+inParam.toString());
        final String url = LipossGlobals.getLipossProperty("ItmsServiceUri");
        String methodName = "hbdxIptvSetValue";
        logger.warn("测速url=============="+url);
        String callBack =  WSClientUtil.callItmsService(url, inParam.toString(), methodName);
        logger.warn("hbdxIptvSetValue回参："+callBack);

        //解析回参
        SAXReader reader = new SAXReader();
        Document document = null;
        String  rstCode = "";
        String rstMsg = "";
        try {
            document = reader.read(new StringReader(callBack));
            Element root = document.getRootElement();
            rstCode = root.elementTextTrim("RstCode");
            rstMsg = root.elementTextTrim("RstMsg");
        } catch (DocumentException e) {
            logger.warn("解析hbdxIptvSetValue回参有误!");
        }

        return rstMsg;
    }

    public ItvServConfigDAO getDao() {
        return dao;
    }

    public void setDao(ItvServConfigDAO dao) {
        this.dao = dao;
    }
}
