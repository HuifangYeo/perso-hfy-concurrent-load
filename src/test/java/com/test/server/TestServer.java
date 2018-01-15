package com.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.test.*;

public class TestServer {
	private static Map cPojoMap = null;
	private static Map assignedPojoMapForTest = null;
	//Number of worker threads
	private static final int NB_THREADS = Runtime.getRuntime().availableProcessors();
	
	public static void main(final String[] args) throws Exception {
		BufferedReader br = null;
	try {
		br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter input : ");
		
		while (true) {
			String i = br.readLine();
			
			if ("q".equals(i)) {
                System.out.println("Exit!");
                System.exit(0);
            }
			
			System.out.print("Enter Qty : ");
			int qty = Integer.parseInt(br.readLine());
			generatePojoMap(Integer.parseInt(i), qty);
			
			System.out.print("Enter input : ");
		}
	} catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	}
	
	public static Map generatePojoMap(int opt, int vol) {
		cPojoMap = new ConcurrentHashMap();
		assignedPojoMapForTest = cPojoMap;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		AtomicLong atomicLong = new AtomicLong();
		System.out.println(("Start generating pojo map").concat(dateFormat.format(new Date())));
		ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
		for (int j = 0; j < NB_THREADS; j++) {
			if(j>0) {
				atomicLong = new AtomicLong((vol/NB_THREADS)*j);
			} 
			
			int threadVol = (j==(NB_THREADS-1))?((vol/NB_THREADS)+(vol%NB_THREADS)):(vol/NB_THREADS);
			
            Runnable worker = new WorkerThread(assignedPojoMapForTest, atomicLong, threadVol, opt);
            executor.execute(worker);              
		}
		
		// This will make the executor accept no new threads
        // and finish all existing threads in the queue
        executor.shutdown();
        // Wait until all threads are finish
        while (!executor.isTerminated()) {
        }
        
		System.out.println("End loading of Pojo in map: ".concat(dateFormat.format(new Date())) );
		System.out.println("cPojoMap size: " + cPojoMap.size());
		
		
		return cPojoMap;
	}
}
