/**
 * 处理Cursor工具类
 */
package com.linkage.litms.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linkage.litms.common.database.Cursor;

/**
 * @date   2007-3-26
 * @author Administrator
 *
 */
public class UtilCursor {
	private static Logger m_logger = LoggerFactory.getLogger(StringUtils.class);

    public static UtilCursor instance = null;
    
    /**
     * 私有够找函数
     *
     */
    private UtilCursor(){
    }
    /**
     * 获取instance实例
     * @return
     */
    public static UtilCursor getInstance(){
        if(instance == null){
            instance = new UtilCursor();
        }
        
        return instance;
    }
    /**
     * 合并Cursor数组
     * @param cursors
     * @return
     */
    public Cursor mergeCursorArray(Cursor[] cursors){
        Cursor result = new Cursor();
        for(int i=0,length = cursors.length;i<length;i++){
            result.addCursor(cursors[i]);
        }
        return result;
    }
    /**
     * 合并Cursor
     * @param listCursor
     * @return
     */
    public Cursor mergeCursorList(List listCursor){
        if(listCursor != null && listCursor.size() > 0){
            try{
                return mergeCursorArray((Cursor[])listCursor.toArray());
            }catch(Exception ex){
                m_logger.debug(ex.getMessage());
            }
        }
        return new Cursor();
    }
    /**
     * 统计Cursor中值：平均值、最大值等。
     * @param cursor       查询数据库的结果集
     * @param columnName   对应数据库中numeric字段
     * @return  结果返回MAP
     */
    public Map getIntValueCursor(Cursor cursor, String valueName,String timeName) {
        Map mapValue = new HashMap();
        if(cursor != null){
            cursor.Reset();
        }else{
            return mapValue;
        }
            
        mapValue.clear();
        Map fields = cursor.getNext();
        //存放当fields
        Map curFileds = null;
        //存放当前值
        double curValue = 0;
        //存放总和值
        double sumValue = 0;
        //存放平均值
        double maxValue = 0;
        long curTime = 0l;
        long maxTime = 0l;
        while (fields != null) {
            curValue    = Double.parseDouble((String) fields.get(valueName));
            sumValue    += curValue;
            maxValue    = Math.max(curValue,maxValue);//curValue > maxValue ? curValue : maxValue;
            curTime     = Long.parseLong((String)fields.get(timeName));
            if(curTime > maxTime){
                maxTime     = curTime;
                curFileds   = fields;
            }
            fields          = cursor.getNext();
        }
        if(curFileds != null){
            curValue = Double.parseDouble((String)curFileds.get(valueName));
        }
        mapValue.put("avg", String.valueOf(sumValue / cursor.getRecordSize()));
        mapValue.put("max", String.valueOf(maxValue));
        mapValue.put("cur", String.valueOf(curValue));
        
        curFileds = null;
        fields = null;
        cursor = null;
        return mapValue;
    }
}
