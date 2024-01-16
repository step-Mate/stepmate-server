package server.stepmate.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
@OpenAPIDefinition(
        info = @Info(title = "step-mate",
        description = "step-mate App Back-End 연동 문서",
        version = "v1"),
        servers = {
        @Server(url = "/", description = "Default Server url")
}
)
@Configuration
public class SwaggerConfig {
}
