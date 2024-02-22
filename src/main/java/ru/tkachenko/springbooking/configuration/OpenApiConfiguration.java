package ru.tkachenko.springbooking.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI openApiDescription() {
        Server localhostServer = new Server();
        localhostServer.setUrl("http://localhost:8080");
        localhostServer.setDescription("Local env");

        Contact contact = new Contact();
        contact.setName("Rodion Tkachenko");
        contact.setEmail("someemail@example");
        contact.setUrl("http://some.url");

        License mitLicence = new License().name("GNU AGPLv3")
                .url("https://choosealicenese.com/licesnse/agpl-3.0/");

        Info info = new Info()
                .title("Booking API")
                .version("1.0")
                .contact(contact)
                .description("API for booking")
                .termsOfService("http://some.terms.url")
                .license(mitLicence);

        return new OpenAPI().info(info).servers(List.of(localhostServer));
    }
}
