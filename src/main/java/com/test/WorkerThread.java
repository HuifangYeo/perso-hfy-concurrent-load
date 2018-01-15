package com.test;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class WorkerThread{
  
    private Map<Object, Long> map = null;
    private PojoVO pojo = null;
    private int opt = 1;
    private long val = 0;
    
    public WorkerThread(Map<Object, Long> assignedMap, long val, int opt) {
        this.map = assignedMap;
        this.val = val;
        
        /*
        this.atomicLong = atomicLong;
        this.vol = vol;
        */
        this.opt = opt;         
    }
    
    public String run() {
    	if(opt == 1) {
			pojo = getPojo();
	    	if(!map.containsKey(pojo)) {
	    		map.put(pojo, val);
	    	}else {
	    		System.out.println("pojo duplicated: " + pojo);
	    	}
    	}else {
			pojo = getPojo();
	    	String key = DigestUtils.md5Hex(pojo.toString());
			if(!map.containsKey(key)) {
				map.put(key, val);
			}else {
				System.out.println("String Key duplicated - " + key);
			}
    	}
    	return pojo.toString();
    }
    
    private PojoVO getPojo(){
    
    	return new PojoVO(
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				RandomStringUtils.randomAlphanumeric(24),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random()),
				Double.doubleToLongBits(Math.random())
		);
	}
}