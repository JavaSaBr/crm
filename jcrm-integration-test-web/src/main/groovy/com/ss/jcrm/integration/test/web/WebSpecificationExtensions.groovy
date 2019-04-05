package com.ss.jcrm.integration.test.web

import com.ss.jcrm.web.exception.BadRequestWebException
import org.springframework.test.web.reactive.server.WebTestClient

class WebSpecificationExtensions {

    static WebTestClient.RequestHeadersSpec url(WebTestClient.RequestHeadersUriSpec self, String uri) {
        return self.uri(uri)
    }
    
    static WebTestClient.RequestHeadersUriSpec body(WebTestClient.RequestBodyUriSpec self, Object body) {
        return self.syncBody(body) as WebTestClient.RequestHeadersUriSpec
    }

    static WebTestClient.RequestHeadersSpec headerValue(
        WebTestClient.RequestHeadersSpec self,
        String headerName,
        String headerValue
    ) {
        return self.header(headerName, headerValue)
    }
    
    static WebTestClient.BodyContentSpec verifyBadRequest(
        WebTestClient.ResponseSpec spec,
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
