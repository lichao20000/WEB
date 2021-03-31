/*
 * 
 * 创建日期 2006-2-10
 * Administrator suzr
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package com.linkage.litms.system;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.linkage.commons.db.PrepareSQL;
import com.linkage.litms.common.database.Cursor;
import com.linkage.litms.common.database.DataSetBean;
import com.linkage.litms.common.database.QueryPage;

public class CodeList {
    /**
     * 载入编码表中所有编码
     */
    private String LOAD_CODELIST_ALL = "select id,name,type,type_id FROM tab_code ORDER by type,type_id";
    private PrepareSQL pSQL = null;
    public CodeList() {
        pSQL = new PrepareSQL();
    }

    /**
     * 获取所有编码列表
     * @param request
     * @return ArrayList
     */
    public ArrayList getCodeList(HttpServletRequest request) {
        ArrayList list = new ArrayList();
        list.clear();

        String stroffset = request.getParameter("offset");
        int pagelen = 15;
        int offset;
        if (stroffset == null)
            offset = 1;
        else
            offset = Integer.parseInt(stroffset);
        QueryPage qryp = new QueryPage();
        qryp.initPage(LOAD_CODELIST_ALL, offset, pagelen);
        
        String strBar = qryp.getPageBar();
        list.add(strBar);
        PrepareSQL psql = new PrepareSQL(LOAD_CODELIST_ALL);
        psql.getSQL();
        Cursor cursor = DataSetBean.getCursor(LOAD_CODELIST_ALL, offset, pagelen);
        list.add(cursor);
        return list;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成方法存根

    }

}
