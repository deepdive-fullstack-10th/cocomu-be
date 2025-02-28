package co.kr.cocomu.template;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.Map;
import org.springframework.http.MediaType;

public class PostRequestTemplate {

    public static ValidatableMockMvcResponse execute(String path) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .when().post(path)
            .then().log().all();
    }

    public static ValidatableMockMvcResponse executeWithBody(String path, Object requestBody) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .when().post(path)
            .then().log().all();
    }

    public static ValidatableMockMvcResponse executeWithParams(String path, Map<String, Object> queryParams) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .contentType(MediaType.APPLICATION_JSON)
            .params(queryParams)
            .when().post(path)
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
            .when().post(path)
            .then().log().all();
    }

}