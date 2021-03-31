package com.linkage.module.gwms.obj.gw;

import java.util.Date;

public class HealthWanDslOBJ {

	private String deviceId;
	
	private String wanId;
	
	private String status;
	
	private String modType;
	
	private float upAtten;
	
	private float upAttenMax;
	
	private float upAttenMin;
	
	private float downAtten;
	
	private float downAttenMax;
	
	private float downAttenMin;
	
	private long upMaxRate;
	
	private long upMaxRateMax;
	
	private long upMaxRateMin;
	
	private long downMaxRate;
	
	private long downMaxRateMax;
	
	private long downMaxRateMin;
	
	private String datePath;
	
	private long interDepth;
	
	private long interDepthMax;
	
	private long interDepthMin;
	
	private String updateTime;
	
	private float upNoise;
	
	private float upNoiseMax;
	
	private float upNoiseMin;
	
	private float downNoise;
	
	private float downNoiseMax;
	
	private float downNoiseMin;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getWanId() {
		return wanId;
	}

	public void setWanId(String wanId) {
		this.wanId = wanId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getModType() {
		return modType;
	}

	public void setModType(String modType) {
		this.modType = modType;
	}

	

	public String getUpdateTime() {
		if (updateTime == null || updateTime.length() == 0) {
			updateTime = "" + (new Date()).getTime() / 1000;
		}
		
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public float getUpAtten() {
		return upAtten;
	}

	public void setUpAtten(float upAtten) {
		this.upAtten = upAtten;
	}

	public float getUpAttenMax() {
		return upAttenMax;
	}

	public void setUpAttenMax(float upAttenMax) {
		this.upAttenMax = upAttenMax;
	}

	public float getUpAttenMin() {
		return upAttenMin;
	}

	public void setUpAttenMin(float upAttenMin) {
		this.upAttenMin = upAttenMin;
	}

	public float getDownAtten() {
		return downAtten;
	}

	public void setDownAtten(float downAtten) {
		this.downAtten = downAtten;
	}

	public float getDownAttenMax() {
		return downAttenMax;
	}

	public void setDownAttenMax(float downAttenMax) {
		this.downAttenMax = downAttenMax;
	}

	public float getDownAttenMin() {
		return downAttenMin;
	}

	public void setDownAttenMin(float downAttenMin) {
		this.downAttenMin = downAttenMin;
	}

	public long getUpMaxRate() {
		return upMaxRate;
	}

	public void setUpMaxRate(long upMaxRate) {
		this.upMaxRate = upMaxRate;
	}

	public long getUpMaxRateMax() {
		return upMaxRateMax;
	}

	public void setUpMaxRateMax(long upMaxRateMax) {
		this.upMaxRateMax = upMaxRateMax;
	}

	public long getUpMaxRateMin() {
		return upMaxRateMin;
	}

	public void setUpMaxRateMin(long upMaxRateMin) {
		this.upMaxRateMin = upMaxRateMin;
	}

	public long getDownMaxRate() {
		return downMaxRate;
	}

	public void setDownMaxRate(long downMaxRate) {
		this.downMaxRate = downMaxRate;
	}

	public long getDownMaxRateMax() {
		return downMaxRateMax;
	}

	public void setDownMaxRateMax(long downMaxRateMax) {
		this.downMaxRateMax = downMaxRateMax;
	}

	public long getDownMaxRateMin() {
		return downMaxRateMin;
	}

	public void setDownMaxRateMin(long downMaxRateMin) {
		this.downMaxRateMin = downMaxRateMin;
	}

	public String getDatePath() {
		return datePath;
	}

	public void setDatePath(String datePath) {
		this.datePath = datePath;
	}

	public long getInterDepth() {
		return interDepth;
	}

	public void setInterDepth(long interDepth) {
		this.interDepth = interDepth;
	}

	public long getInterDepthMax() {
		return interDepthMax;
	}

	public void setInterDepthMax(long interDepthMax) {
		this.interDepthMax = interDepthMax;
	}

	public long getInterDepthMin() {
		return interDepthMin;
	}

	public void setInterDepthMin(long interDepthMin) {
		this.interDepthMin = interDepthMin;
	}

	public float getUpNoise() {
		return upNoise;
	}

	public void setUpNoise(float upNoise) {
		this.upNoise = upNoise;
	}

	public float getUpNoiseMax() {
		return upNoiseMax;
	}

	public void setUpNoiseMax(float upNoiseMax) {
		this.upNoiseMax = upNoiseMax;
	}

	public float getUpNoiseMin() {
		return upNoiseMin;
	}

	public void setUpNoiseMin(float upNoiseMin) {
		this.upNoiseMin = upNoiseMin;
	}

	public float getDownNoise() {
		return downNoise;
	}

	public void setDownNoise(float downNoise) {
		this.downNoise = downNoise;
	}

	public float getDownNoiseMax() {
		return downNoiseMax;
	}

	public void setDownNoiseMax(float downNoiseMax) {
		this.downNoiseMax = downNoiseMax;
	}

	public float getDownNoiseMin() {
		return downNoiseMin;
	}

	public void setDownNoiseMin(float downNoiseMin) {
		this.downNoiseMin = downNoiseMin;
	}


	
	
}















