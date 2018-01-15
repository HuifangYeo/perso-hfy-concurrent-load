package com.test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

public class WorkerThread implements Runnable {
  
    private String command;
    private Map<Object, Long> map = null;
    private PojoVO pojo = null;
    private AtomicLong atomicLong = new AtomicLong();
    private int vol = 100000;
    private int opt = 1;
    
    public WorkerThread(Map<Object, Long> assignedMap, AtomicLong atomicLong, int vol, int opt) {
        this.map = assignedMap;
        this.atomicLong = atomicLong;
        this.vol = vol;
        this.opt = opt; 
    }
    
    public void run() {
    	System.out.println("Run vol: " + vol);
    	if(opt == 1) {
	    	for(int i=0; i<vol; i++) {
		    	pojo = getPojo();
		    	if(map.containsKey(pojo)) {
		    		System.out.println("pojo duplicated: " + pojo);
		    	}else {
		    		map.put(pojo, atomicLong.getAndIncrement());
		    	}
	    	}
    	}else {
    		for(int i=0; i<vol; i++) {
		    	pojo = getPojo();
		    	
		    	String key = DigestUtils.md5Hex(pojo.toString());
				if(!map.containsKey(key)) {
					map.put(key, atomicLong.getAndIncrement());
				}else {
					System.out.println("String Key duplicated - " + key);
				}
	    	}
    	}
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