package co.kr.cocomu.auth.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class OAuthClientGenerator {

    private final SimpleClientHttpRequestFactory requestFactory;

    public RestClient generateGitHubApiClient() {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> converters.add(new MappingJackson2HttpMessageConverter()))
                .baseUrl("https://api.github.com")
                .build();
    }

    public RestClient generateGithubClient() {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> {
                    converters.add(new FormHttpMessageConverter());
                    converters.add(new MappingJackson2HttpMessageConverter());
                })
                .baseUrl("https://github.com")
                .build();
    }

    public RestClient generateKakaoAuthClient() {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> {
                    converters.add(new FormHttpMessageConverter());
                    converters.add(new MappingJackson2HttpMessageConverter());
                })
                .baseUrl("https://kauth.kakao.com")
                .build();
    }

    public RestClient generateKakaoApiClient() {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> converters.add(new MappingJackson2HttpMessageConverter()))
                .baseUrl("https://kapi.kakao.com")
                .build();
    }

    public RestClient generateGoogleOAuthClient() {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> {
                    converters.add(new FormHttpMessageConverter());
                    converters.add(new MappingJackson2HttpMessageConverter());
                })
                .baseUrl("https://oauth2.googleapis.com")
                .build();
    }

    public RestClient generateGoogleApiClient() {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .messageConverters(converters -> converters.add(new MappingJackson2HttpMessageConverter()))
                .baseUrl("https://www.googleapis.com")
                .build();
    }

}
