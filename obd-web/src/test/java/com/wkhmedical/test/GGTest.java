/**
 * 
 */
package com.wkhmedical.test;

import org.junit.Test;
import org.springframework.http.ResponseEntity;
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
	@Test
	public void main() {
		RestTemplate rt = new RestTemplate();
		String url = "http://test.5dwo.com/api/car/get.list?access_token=b81c9f5c-82be-4c77-a115-1779471b5047";
		Paging<CarInfoPageParam> pgReq = new Paging<CarInfoPageParam>();
		while (true) {
			ResponseEntity<String> rseResult = rt.postForEntity(url, pgReq, String.class);
			System.out.println(rseResult.getBody());
		}

	}

}
