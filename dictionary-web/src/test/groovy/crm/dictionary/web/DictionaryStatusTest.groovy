package crm.dictionary.web

class DictionaryStatusTest extends DictionarySpecification {
  
  def "should return status ok"() {
    when:
        def response = webClient.get()
            .url("$contextPath/status")
            .exchange()
    then:
        response.expectStatus().isOk()
  }
}
