package util;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;

import static io.restassured.RestAssured.given;

public class RestAssuredHelper {
    private RequestSpecification requestSpecification = given()
            .contentType(ContentType.JSON)
            .log().everything()
            .filter(new AllureRestAssured());
    private Response response;

    public static RestAssuredHelper newInstance() {
        return new RestAssuredHelper();
    }

    @Step("Выполняю {0} запрос")
    public RestAssuredHelper execute(Method method, RequestSpecBuilder request) {
        response = requestSpecification.spec(request.build()).request(method);
        return this;
    }

    @Step("Проверяю отвте")
    public RestAssuredHelper checkResult(ResponseSpecBuilder expectedResult) {
        response.then().spec(expectedResult.build());
        return this;
    }

    @Step("Проверяю отвте")
    public RestAssuredHelper checkResult(Object expectedResponse, int expectedStatusCode) {
        checkResult(expectedResponse, response.as(expectedResponse.getClass()), expectedStatusCode, response.statusCode());
        return this;
    }

    private void checkResult(Object expectedResponse, Object actualResponse, int expectedStatusCode,
                             int actualStatusCode) {

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualResponse).describedAs("Проверяем эквивалентно ли тело ответа ожидаемому")
                    .isEqualTo(expectedResponse);
            softly.assertThat(actualStatusCode).describedAs("Проверяем равен ли статус код в ответе ожидаемому")
                    .isEqualTo(expectedStatusCode);
        });
    }
}