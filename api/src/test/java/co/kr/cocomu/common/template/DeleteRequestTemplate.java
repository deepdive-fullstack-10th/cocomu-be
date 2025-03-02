package co.kr.cocomu.common.template;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.Map;
import org.springframework.http.MediaType;

public class DeleteRequestTemplate {

    public static ValidatableMockMvcResponse execute(String path) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .when().delete(path)
            .then().log().all();
    }

    public static <T> ValidatableMockMvcResponse executeWithBody(String path, T requestBody) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .when().delete(path)
            .then().log().all();
    }

    public static ValidatableMockMvcResponse executeWithParams(String path, Map<String, Object> queryParams) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .contentType(MediaType.APPLICATION_JSON)
            .params(queryParams)
            .when().delete(path)
            .then().log().all();
    }

    public static ValidatableMockMvcResponse executeWithBodyAndParams(
        String path,
        Object requestBody,
        Map<String, Object> queryParams
    ) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .params(queryParams)
            .when().delete(path)
            .then().log().all();
    }

}