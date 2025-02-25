package co.kr.cocomu.template;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class GetRequestTemplate {

    public static <T> T executeSuccess(final String path, final TypeRef<T> typeRef) {
        return RestAssuredMockMvc
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON)
            .when().get(path)
            .then().log().all()
            .status(HttpStatus.OK)
            .extract()
            .as(typeRef);
    }

    public static <T> T executeSuccessWithParams(
        final String path,
        final Map<String, Object> queryParams,
        final TypeRef<T> typeRef
    ) {
        return RestAssuredMockMvc
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON)
            .params(queryParams)
            .when().get(path)
            .then().log().all()
            .status(HttpStatus.OK)
            .extract()
            .as(typeRef);
    }

    public static void executeBadRequest(final String path) {
        RestAssuredMockMvc
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON)
            .when().get(path)
            .then().log().all()
            .status(HttpStatus.BAD_REQUEST);
    }

    public static void executeBadRequestWithParams(final String path, final Map<String, Object> queryParams) {
        RestAssuredMockMvc
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON)
            .params(queryParams)
            .when().get(path)
            .then().log().all()
            .status(HttpStatus.BAD_REQUEST);
    }

    public static void executeNotFound(final String path) {
        RestAssuredMockMvc
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON)
            .when().get(path)
            .then().log().all()
            .status(HttpStatus.NOT_FOUND);
    }

    public static void executeNotFoundWithParams(final String path, final Map<String, Object> queryParams) {
        RestAssuredMockMvc
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON)
            .params(queryParams)
            .when().get(path)
            .then().log().all()
            .status(HttpStatus.NOT_FOUND);
    }

}
