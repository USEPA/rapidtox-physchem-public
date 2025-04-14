package gov.epa.ccte.api.physchem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo());
    }


    private Info apiInfo() {
        return new Info()
                .title("physchem")
                .version("1")
                .description("rapidtox physchem specs")
                .contact(apiContact());
    }

    private Contact apiContact() {
        return new Contact()
                .name("Asif Rashid, Joshua Powell")
                .email("rashid.asif@epa.gov")
                .url("https://confluence.epa.gov/display/AppRapidTox/App_RapidTox");
    }


}
