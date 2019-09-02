package com.ss.jcrm.integration.test.web

import com.ss.jcrm.web.exception.BadRequestWebException
import org.springframework.web.util.UriBuilder

import java.util.function.Function

import static org.springframework.test.web.reactive.server.WebTestClient.*

class WebSpecificationExtensions {
    
    static RequestHeadersSpec url(RequestHeadersUriSpec self, String uri) {
        return self.uri(uri)
    }
    
    static RequestHeadersSpec url(RequestHeadersSpec self, String uri) {
        
        if (self instanceof UriSpec) {
            return self.uri(uri)
        } else {
            throw new IllegalStateException("The " + self + " isn't UriSpec")
        }
    }
    
    static RequestHeadersSpec url(RequestHeadersUriSpec self, Function<UriBuilder, URI> uriFunction) {
        return self.uri(uriFunction)
    }
    
    static RequestHeadersSpec param(RequestHeadersSpec self, String name, String value) {
        return self.attribute(name, value)
    }
    
    static RequestHeadersUriSpec body(RequestBodyUriSpec self, Object body) {
        return self.syncBody(body) as RequestHeadersUriSpec
    }
    
    static RequestHeadersUriSpec body(RequestHeadersSpec self, Object body) {
        return self.body(body) as RequestHeadersUriSpec
    }

    static RequestHeadersSpec headerValue(
        RequestHeadersSpec self,
        String headerName,
        String headerValue
    ) {
        return self.header(headerName, headerValue)
    }
    
    static BodyContentSpec verifyErrorResponse(
        ResponseSpec spec,
        int errorCode,
        String errorMessage
    ) {
        
        return spec.expectHeader().valueEquals(BadRequestWebException.HEADER_ERROR_CODE, String.valueOf(errorCode))
            .expectHeader().valueEquals(BadRequestWebException.HEADER_ERROR_MESSAGE, errorMessage)
            .expectBody()
            .jsonPath('$.errorCode').isEqualTo(errorCode)
            .jsonPath('$.errorMessage').isEqualTo(errorMessage)
    }
}
