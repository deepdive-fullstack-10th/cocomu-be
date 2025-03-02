package co.kr.cocomu.common.template;

import co.kr.cocomu.common.fake.FakeOAuthClientGenerator;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;

public class OAuthClientTemplate {

    protected MockWebServer mockWebServer;
    protected FakeOAuthClientGenerator fakeOAuthClientGenerator;

    @BeforeEach
    protected void init() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        fakeOAuthClientGenerator = new FakeOAuthClientGenerator(mockWebServer);
    }

    @AfterEach
    protected void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    protected void enqueue(String message) {
        mockWebServer.enqueue(new MockResponse()
            .setBody(message)
            .addHeader("Content-Type", "application/json"));
    }

    protected void enqueue4XXError() {
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.BAD_REQUEST.value())
            .addHeader("Content-Type", "application/json"));
    }

    protected void enqueue5XXError() {
        mockWebServer.enqueue(new MockResponse()
            .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .addHeader("Content-Type", "application/json"));
    }

    protected RecordedRequest takeRequest() throws InterruptedException {
        return mockWebServer.takeRequest();
    }

}
