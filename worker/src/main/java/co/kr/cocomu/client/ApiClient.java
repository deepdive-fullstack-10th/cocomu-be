//package co.kr.cocomu.client;
//
//import co.kr.cocomu.dto.result.EventMessage;
//import co.kr.cocomu.dto.result.ExecutionMessage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Component
//@Slf4j
//public class ApiClient {
//
//    private final WebClient webClient;
//    private final String username;
//    private final String password;
//
//    public ApiClient(
//        final WebClient webClient,
//        @Value("${admin.username}") final String username,
//        @Value("${admin.password}") final String password
//    ) {
//        this.webClient = webClient;
//        this.username = username;
//        this.password = password;
//    }
//
//    public void sendResultToMainServer(final EventMessage<ExecutionMessage> result) {
//        webClient.post()
//            .uri("/api/v1/executor/result")
//            .headers(headers -> headers.setBasicAuth(username, password))
//            .bodyValue(result)
//            .retrieve()
//            .bodyToMono(Void.class)
//            .doOnSuccess(success -> log.info("결과 전달 성공 : {}", result))
//            .doOnError(throwable -> log.error("왜 실패했지? - data: {}, error : {}", result.getData(), throwable.getMessage()))
//            .subscribe();
//    }
//
//}
