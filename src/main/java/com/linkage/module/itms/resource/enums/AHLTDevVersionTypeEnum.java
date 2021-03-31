package com.linkage.module.itms.resource.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 安徽联通设备版本类型枚举类
 * @Author lingmin
 * @Date 2020/5/29
 **/
public enum  AHLTDevVersionTypeEnum {

    SFU("1","SFU"),
    HGU("2","HGU"),
    SMART("3","智能网关"),
    CONVERGED("4","融合网关");

    private String code;
    private String text;

    AHLTDevVersionTypeEnum(String code,String text){
        this.code = code;
        this.text = text;
    }

    /**
     * 根据code获取text
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        for (AHLTDevVersionTypeEnum e : values()) {
            if (code.equals(e.getCode())) {
                return e.getText();
            }
        }
        return null;
    }

    /**
     * 获取枚举的列表
     * @return
     */
    public static List<Map<String, String>> getAll() {
        List<Map<String, String>> allList = new ArrayList<Map<String, String>>();
        Map<String, String> typeEnum = new HashMap<String, String>();
        typeEnum.put("text", "请选择");
        typeEnum.put("value", "-1");
        allList.add(typeEnum);
        for (AHLTDevVersionTypeEnum e : values()) {
            typeEnum = new HashMap<String, String>();
            typeEnum.put("text", e.getText());
            typeEnum.put("value", e.getCode());
            allList.add(typeEnum);
        }
        return allList;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setText(String text) {
        this.text = text;
    }
}
