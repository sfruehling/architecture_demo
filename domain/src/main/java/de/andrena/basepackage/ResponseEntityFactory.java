package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Component
public class ResponseEntityFactory {

    private final HttpHeaderFactory httpHeaderFactory;

    @Autowired
    public ResponseEntityFactory(HttpHeaderFactory httpHeaderFactory) {
        this.httpHeaderFactory = httpHeaderFactory;
    }

    public <T> ResponseEntity<?> createOkResponseForList(List<T> dtos, String path) {
        return new ResponseEntity<>(dtos,
                                    httpHeaderFactory.createHeader(path,
                                                                   HttpStatus.OK),
                                    HttpStatus.OK);
    }

    public <T> ResponseEntity<?> createCreatedResponse(T dto, String path) {
        try {
            return new ResponseEntity<>(dto,
                                        httpHeaderFactory.createHeader(URLEncoder.encode(path,
                                                                                         "UTF-8"),
                                                                       HttpStatus.CREATED),
                                        HttpStatus.CREATED);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public <T> ResponseEntity<?> createOkResponse(T dto, String path) {
        try {
            return new ResponseEntity<>(dto,
                                        httpHeaderFactory.createHeader(URLEncoder.encode(path,
                                                                                         "UTF-8"),
                                                                       HttpStatus.OK),
                                        HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
