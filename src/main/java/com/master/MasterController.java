package com.master;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MasterController {
	
	private final static Logger LOG = LoggerFactory.getLogger(MasterFilter.class);
	
	@GetMapping("/books")
	public ResponseEntity<String> getBooks(HttpServletRequest request) {
		 String httpUser = request.getHeader("Http-User");
		 String uri = "http://localhost:8888/books";
		 ResponseEntity<String> response = null;
		 RestTemplate restTemplate = new RestTemplate();
		 try {
	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Http-User","TEST");
	        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
	        response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
	        LOG.debug("Result - status ("+ response.getStatusCode() + ") has body: " + response.hasBody());
	    }
		    catch (Exception eek) {
		    	 LOG.debug("** Exception: "+ eek.getMessage());
		    }

		 return response;

	}
	
	@GetMapping("/toys")
	public String getToys(HttpServletRequest request) {
		 String httpUser = request.getHeader("Http-User");
		 RestTemplate restTemplate = new RestTemplate();
	     String quote = restTemplate.getForObject("http://localhost:8888/toys", String.class);
	     LOG.debug("Response = "+quote);
	     return quote;

	}

}
