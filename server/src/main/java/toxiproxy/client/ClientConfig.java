package toxiproxy.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientConfig {

  @Bean
  public RestTemplate toxiproxyClientRestTemplate() {
    return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
  }

}
