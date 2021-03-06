package demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.LinkedMultiValueMap;

import sparklr.common.AbstractIntegrationTests;

/**
 * @author Dave Syer
 */
public class CustomProviderTests extends AbstractIntegrationTests {

	@Test
	public void customGrant() throws Exception {
		LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("grant_type", "custom");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic " + new String(Base64.encode(("my-trusted-client:").getBytes())));
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = http.postForMap("/oauth/token", headers, form);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getHeaders().getFirst("Cache-Control").contains("no-store"));
		assertTrue(response.getHeaders().getFirst("Pragma").contains("no-cache"));
	}

	@Test
	public void invalidGrant() throws Exception {
		LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
		form.set("grant_type", "foo");
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic " + new String(Base64.encode(("my-trusted-client:").getBytes())));
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response = http.postForMap("/oauth/token", headers, form);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getHeaders().getFirst("Cache-Control").contains("no-store"));
		assertTrue(response.getHeaders().getFirst("Pragma").contains("no-cache"));
	}

}
