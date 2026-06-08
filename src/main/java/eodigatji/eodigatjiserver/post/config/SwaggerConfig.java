package eodigatji.eodigatjiserver.post.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("어디갔지(eodigatji) Post API 명세서")
                        .description("분실물/습득물 서비스 백엔드 API 테스트입니다.")
                        .version("v1.0.0"));
    }
}