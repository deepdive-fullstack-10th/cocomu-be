package co.kr.cocomu.auth.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormDataGenerator {

    public static MultiValueMap<String, String> generateGoogleFormData(
        final String clientId,
        final String clientSecret,
        final String code,
        final String redirectUri
    ) {
        final MultiValueMap<String, String> formData = generateKakaoFormData(clientId, clientSecret, code);
        formData.add("redirect_uri", redirectUri);
        return formData;
    }

    public static MultiValueMap<String, String> generateKakaoFormData(
        final String clientId,
        final String clientSecret,
        final String code
    ) {
        final MultiValueMap<String, String> formData = generateGithubFormData(clientId, clientSecret, code);
        formData.add("grant_type", "authorization_code");
        return formData;
    }

    public static MultiValueMap<String, String> generateGithubFormData(
        final String clientId,
        final String clientSecret,
        final String code
    ) {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);
        return formData;
    }

}
