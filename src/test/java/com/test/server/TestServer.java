package com.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
			ConcurrentHashMap cmap = (ConcurrentHashMap) generatePojoMap(Integer.parseInt(i), qty);
			System.out.println("Map size: " + cmap.size());
			
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
		Date startDate = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		final AtomicLong atomicLong = new AtomicLong(1);
		final int keyOpt = opt;
		System.out.println(("Start generating pojo map").concat(dateFormat.format(startDate)));
		
		final ExecutorService pool = Executors.newFixedThreadPool(NB_THREADS);
		final ExecutorCompletionService<String> completionService = new ExecutorCompletionService<String>(pool);
		
		for(int i=0; i< vol; i++) {
			completionService.submit(new Callable<String>() {
				public String call() throws Exception {
			    	return new WorkerThread(assignedPojoMapForTest, atomicLong.getAndIncrement(), keyOpt).run();
			    }
			});
		}
		
		for(int i=0; i< vol; i++) {
			try{
				final Future<String> future = completionService.take();
			    try {
			        final String content = future.get();
			        if(i==(vol-1)) {
			        	Date endDate = new Date();
			        	Long timeTaken = (endDate.getTime() - startDate.getTime())/1000;
			        	System.out.println("End loading of Pojo in map: ".concat(dateFormat.format(endDate)) );
			    		System.out.println("Final cPojoMap size: " + cPojoMap.size());
			    		System.out.println("Total processing time: " + timeTaken + "s");
			        }
			    } catch (ExecutionException e) {
			        System.out.println("Error while executing: ".concat(e.getMessage()));
			    }
			} catch (InterruptedException e) {
				 System.out.println("Error while processing: ".concat(e.getMessage()));
		    }
        }
		
		pool.shutdownNow();
		/*
		for (int j = 0; j < NB_THREADS; j++) {
			if(j>0) {
				atomicLong = new AtomicLong((vol/NB_THREADS)*j);
			} 
			
			int threadVol = (j==(NB_THREADS-1))?((vol/NB_THREADS)+(vol%NB_THREADS)):(vol/NB_THREADS);
			
            Runnable worker = new WorkerThread(assignedPojoMapForTest, atomicLong, threadVol, opt);
            executor.execute(worker);              
		}
		*/
		
		// This will make the executor accept no new threads
        // and finish all existing threads in the queue
        //executor.shutdown();
        // Wait until all threads are finish
        //while (!executor.isTerminated()) {
        //}
        
		return assignedPojoMapForTest;
	}
}
