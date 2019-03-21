package com.ss.jcrm.integration.test.web

import org.springframework.test.web.reactive.server.WebTestClient

class WebSpecificationExtensions {

    static WebTestClient.RequestHeadersSpec url(WebTestClient.RequestHeadersUriSpec self, String uri) {
        return self.uri(uri)
    }

    static WebTestClient.RequestHeadersSpec headerValue(
        WebTestClient.RequestHeadersSpec self,
        String headerName,
        String headerValue
    ) {
        return self.header(headerName, headerValue)
    }
}