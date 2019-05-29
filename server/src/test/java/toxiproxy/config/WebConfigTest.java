package toxiproxy.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;

@ExtendWith(MockitoExtension.class)
public class WebConfigTest {

  @InjectMocks private WebConfig webConfig;

  @Test
  void containerCustomizer() {
    WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> customizer = webConfig.containerCustomizer();

  }
}
