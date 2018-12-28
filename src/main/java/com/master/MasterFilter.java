package com.master;



import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)

public class MasterFilter implements Filter {



	private final static Logger LOG = LoggerFactory.getLogger(MasterFilter.class);
	Map<String,String> userMap = new HashMap<String,String>();



	@Override

	public void init(final FilterConfig filterConfig) throws ServletException {

		LOG.info("Initializing filter :{}", this);
		
		TimerTask timerTask = new MyTimerTask();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 10*1000);
        System.out.println("TimerTask started");
        //cancel after sometime
        /*try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();*/
       // System.out.println("TimerTask cancelled");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

	}



	@Override

	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)

			throws IOException, ServletException {
		
		

		HttpServletRequest req = (HttpServletRequest) request;

		HttpServletResponse res = (HttpServletResponse) response;

		LOG.info("Logging Request  {} : {}", req.getMethod(), req.getRequestURI());
		createFWContext(req.getHeader("Http-User"));

		chain.doFilter(request, response);

		LOG.info("Logging Response :{}", res.getContentType());

	}

	public String createFWContext(String userid) {
		
		if(!userMap.containsKey(userid)) {
			LOG.info("Creating FW Context");
			String universalSessionId=UUID.randomUUID().toString().toUpperCase().replace("-", "");
			userMap.put(userid,universalSessionId );
			System.out.println("UserMap = "+userMap);
			return universalSessionId;
		}else {
			LOG.info("FW Context already Exists");
			return userMap.get(userid);
		}
			
	}

	
	@Override

	public void destroy() {

		System.out.println("Destructing filter :{}"+this);
		userMap.clear();
		if(userMap.isEmpty()) {
			System.out.println("The user Map has been cleared");
		}

	}
	
	public class MyTimerTask extends TimerTask {

	    @Override
	    public void run() {
	        System.out.println("Timer task started at:"+new Date());
	        completeTask();
	        System.out.println("Timer task finished at:"+new Date());
	    }

	    private void completeTask() {
	        try {
	            //assuming it takes 20 secs to complete the task
	        	if(!userMap.isEmpty())
	        	System.out.println("List Of Usesrs = "+userMap.keySet());
	            Thread.sleep(20000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}

}