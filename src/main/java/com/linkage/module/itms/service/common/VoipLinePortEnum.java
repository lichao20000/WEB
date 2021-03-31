package com.linkage.module.itms.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内蒙古语音终端物理标识枚举
 *
 * @Author lingmin
 * @Date 2020/8/31
 **/
public enum  VoipLinePortEnum {

    A0("A0","A0"),
    A1("A1","A1"),
    A2("A2","A2"),
    A3("A3","A3"),
    A4("A4","A4"),
    A5("A5","A5"),
    A6("A6","A6"),
    A7("A7","A7"),
    A8("A8","A8"),
    A9("A9","A9"),
    A10("A10","A10"),
    A11("A11","A11"),
    A12("A12","A12"),
    A13("A13","A13"),
    A14("A14","A14"),
    A15("A15","A15"),
    A16("A16","A16"),
    A17("A17","A17"),
    A18("A18","A18"),
    A19("A19","A19"),
    A20("A20","A20"),
    A21("A21","A21"),
    A22("A22","A22"),
    A23("A23","A23"),
    A24("A24","A24"),
    A25("A25","A25"),
    A26("A26","A26"),
    A27("A27","A27"),
    A28("A28","A28"),
    A29("A29","A29"),
    A30("A30","A30"),
    A31("A31","A31");

    private String value;
    private String name;

    VoipLinePortEnum(String value,String name){
        this.value = value;
        this.name = name;
    }

    public static List<Map<String,String>> getAllLinePorts(){
        List<Map<String,String>> allMapList = new ArrayList<Map<String, String>>();
        for(VoipLinePortEnum e : values()){
            Map<String,String> map = new HashMap<String, String>();
            map.put("text", e.name);
            map.put("value", e.value);
            allMapList.add(map);
        }
        return allMapList;
    }
}
