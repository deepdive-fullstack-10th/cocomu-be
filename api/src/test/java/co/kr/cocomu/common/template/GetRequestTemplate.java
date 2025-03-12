package co.kr.cocomu.common.template;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.ValidatableMockMvcResponse;
import java.util.Map;
import org.springframework.http.MediaType;

public class GetRequestTemplate {

    public static ValidatableMockMvcResponse execute(String path) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .cookie("refreshToken", "test-refresh-token")
            .contentType(MediaType.APPLICATION_JSON)
            .when().get(path)
            .then().log().all();
    }

    public static ValidatableMockMvcResponse executeNoAuth(String path) {
        return RestAssuredMockMvc
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON)
            .when().get(path)
            .then().log().all();
    }

    public static ValidatableMockMvcResponse executeWithParams(String path, Map<String, Object> params) {
        return RestAssuredMockMvc
            .given().log().all()
            .header("Authorization", "Bearer token")
            .contentType(MediaType.APPLICATION_JSON)
            .params(params)
            .when().get(path)
            .then().log().all();
    }

}
