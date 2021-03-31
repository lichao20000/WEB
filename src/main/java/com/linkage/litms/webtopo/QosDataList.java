package com.linkage.litms.webtopo;

import org.omg.CORBA.StringHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import RemoteDB.QOSManager;
import RemoteDB.QosData;
import RemoteDB.QosQueueData;
import RemoteDB.QueueingDeviceConf;

public class QosDataList {
	/** log */
	private static Logger logger = LoggerFactory.getLogger(QosDataList.class);
    private static QOSManager qosManager = null;
    
    private static QueueingDeviceConf qosQueueManager = null;

    public QosData[] getDeviceQosData(String device_id, String account,
            String passwd) {
        if(qosManager == null){
            qosManager = new VpnScheduler().getQosManager();
        }
        
        StringHolder collecttime = new StringHolder();
        logger.debug("开始获取设备" + device_id + "的流量");
        
        QosData datas[] = null;
        try{
            datas = qosManager.getPortData(device_id, collecttime);
            logger.debug("获得采集时间为" + collecttime.value);
        }catch(Exception e){
            e.printStackTrace();
            qosManager = new VpnScheduler().getQosManager();
            datas = qosManager.getPortData(device_id, collecttime);
        }
        if (datas != null) {
        	logger.debug(""+datas.length);
        }else{
            datas = new QosData[0];
        }
        return datas;
    }
    public QosQueueData[] getDeviceQosQueueData(String device_id, String account,
            String passwd) {
        if(qosQueueManager == null){
            qosQueueManager = new VpnScheduler().getQosQueueManager();
        }
        
        StringHolder collecttime = new StringHolder();
        logger.debug("开始获取设备" + device_id + "的流量");
        
        QosQueueData datas[] = null;
        try{
            datas = qosQueueManager.getPortData(device_id, collecttime);
            logger.debug("获得采集时间为" + collecttime.value);
        }catch(Exception e){
            e.printStackTrace();
            qosQueueManager = new VpnScheduler().getQosQueueManager();
            datas = qosQueueManager.getPortData(device_id, collecttime);
        }
        if (datas != null) {
        	logger.debug(""+datas.length);
        }else{
            datas = new QosQueueData[0];
        }
        return datas;
    }
}
