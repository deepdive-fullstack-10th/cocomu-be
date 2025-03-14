//package co.kr.cocomu.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Configuration
//public class WebClientConfig {
//
//    private final String apiServerUri;
//
//    public WebClientConfig(@Value("${api.server.uri}") final String apiServerUri) {
//        this.apiServerUri = apiServerUri;
//    }
//
//    @Bean
//    public WebClient webClient() {
//        return WebClient.builder()
//            .baseUrl(apiServerUri)
//            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//            .build();
//    }
//
//}