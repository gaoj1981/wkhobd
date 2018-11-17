/**
 * 
 */
package com.wkhmedical.test;

import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.taoxeo.repository.Paging;
import com.wkhmedical.dto.CarInfoPageParam;

/**
 * @author Administrator
 */
public class GGTest {

	/**
	 * @param args
	 */
	public void test() {
		RestTemplate rt = new RestTemplate();
		String url = "http://test.5dwo.com/api/car/get.list?access_token=b81c9f5c-82be-4c77-a115-1779471b5047";
		Paging<CarInfoPageParam> pgReq = new Paging<CarInfoPageParam>();
		ResponseEntity<String> rseResult = rt.postForEntity(url, pgReq, String.class);
		System.out.println(rseResult.getBody());
	}

	@Test
	public void test1() {
		RestTemplate template = new RestTemplate();
		String url = "http://www.xzw.com/fortune/aries/1.html";

//		HttpHeaders requestHeaders = new HttpHeaders();
//		requestHeaders.add("User-Agent",
//				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.8 Safari/537.36");
//		HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
//		ResponseEntity<String> response = template.exchange(url, HttpMethod.GET, requestEntity, String.class);
		
		template.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		ResponseEntity<String> response = template.getForEntity(url, String.class);
		String sttr = response.getBody();
		System.out.println(sttr);
	}

}
