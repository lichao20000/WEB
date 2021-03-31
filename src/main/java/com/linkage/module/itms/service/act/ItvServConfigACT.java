package com.linkage.module.itms.service.act;

import action.splitpage.splitPageAction;
import com.linkage.module.itms.service.bio.ItvServConfigBIO;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItvServConfigACT extends splitPageAction implements SessionAware,
        ServletRequestAware {
    private static Logger  logger = LoggerFactory.getLogger(ItvServConfigACT.class);
    private Map session;
    private HttpServletRequest request;

    private String loid;

    private String ajax = null;

    private ItvServConfigBIO bio;

    private ArrayList<HashMap<String, String>> data;

    public String query()
    {
        logger.warn("ItvServConfigACT=>query([{}]) start",loid);
        data=bio.query(loid);
        logger.warn("ItvServConfigACT=>query([{}]) end",loid);
        return "list";
    }

    public String config()
    {
        logger.warn("ItvServConfigACT=>query([{}]) start",loid);
        ajax=bio.config(loid);
        logger.warn("ItvServConfigACT=>query([{}]) end",loid);
        return "ajax";
    }



    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    @Override
    public void setServletRequest(HttpServletRequest request) {

    }

    public String getLoid() {
        return loid;
    }

    public void setLoid(String loid) {
        this.loid = loid;
    }

    public String getAjax() {
        return ajax;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setAjax(String ajax) {
        this.ajax = ajax;
    }

    public ItvServConfigBIO getBio() {
        return bio;
    }

    public void setBio(ItvServConfigBIO bio) {
        this.bio = bio;
    }

    public Map getSession() {
        return session;
    }

    public ArrayList<HashMap<String, String>> getData() {
        return data;
    }

    public void setData(ArrayList<HashMap<String, String>> data) {
        this.data = data;
    }
}
