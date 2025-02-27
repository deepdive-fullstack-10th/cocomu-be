package co.kr.cocomu.auth.client;

import co.kr.cocomu.auth.exception.AuthExceptionCode;
import co.kr.cocomu.common.exception.domain.BadGatewayException;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestClientExecutor {

    public static <T> T execute(final Supplier<T> apiCall) {
        try {
            return apiCall.get();
        } catch (final HttpClientErrorException e) {
            log.error("OAuth Exception: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new BadGatewayException(AuthExceptionCode.OAUTH_EXCEPTION);
        } catch (final Exception e) {
            log.error("OAuth Error: {} - {}", e.getClass(), e.getMessage());
            throw new BadGatewayException(AuthExceptionCode.OAUTH_ERROR);
        }
    }

}
