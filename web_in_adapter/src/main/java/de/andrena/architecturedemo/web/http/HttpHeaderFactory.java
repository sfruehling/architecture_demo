package de.andrena.architecturedemo.web.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class HttpHeaderFactory {

	public HttpHeaders createHeader(String location, HttpStatus httpStatus) {
		HttpHeaders responseHeaders = new HttpHeaders();
		URI uri;
		try {
			uri = new URI(location);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		responseHeaders.set("HTTP status", httpStatus.getReasonPhrase());
		responseHeaders.setLocation(uri);
		return responseHeaders;
	}

}
