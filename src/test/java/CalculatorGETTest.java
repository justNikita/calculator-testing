import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.BaseSetUp;
import util.RestAssuredHelper;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static util.Responses.*;

@Feature("Тестирование калькулятора")
@Story("Проверка GET /resources")
class CalculatorGETTest extends BaseSetUp {

    @BeforeAll
    static void start() {
        RestAssured.basePath = "/resource";
    }

    private static Stream<Arguments> negativeRequestProvider() {
        return Stream.of(
                Arguments.of(new RequestSpecBuilder()
                                .addParam("a", 4)
                                .addParam("b", 5),
                        WITHOUT_METHOD),
                Arguments.of(new RequestSpecBuilder()
                                .addParam("a", 5)
                                .addParam("method", "+"),
                        WITHOUT_B),
                Arguments.of(new RequestSpecBuilder()
                                .addParam("b", 5)
                                .addParam("method", "+"),
                        WITHOUT_A),
                Arguments.of(new RequestSpecBuilder().setBody(""),
                        EMPTY_REQUEST)
        );
    }

    private static Stream<Arguments> invalidParameterRequestProvider() {
        return Stream.of(
                Arguments.of("s", 2, "+", INVALID_VALUE_A),
                Arguments.of(2, "s", "+", INVALID_VALUE_B),
                Arguments.of("%", 0, "+", INVALID_VALUE_A),
                Arguments.of(2, "%", "+", INVALID_VALUE_B),
                Arguments.of(2, 2, "?", INVALID_VALUE_METHOD),
                Arguments.of("2sad", 2, "+", INVALID_VALUE_A),
                Arguments.of(2, "2sad", "+", INVALID_VALUE_B)
        );
    }

    private static Stream<Arguments> plusRequestProvider() {
        return Stream.of(
                Arguments.of(2, 2, 4),
                Arguments.of(2, 0, 2),
                Arguments.of(0, 2, 0),
                Arguments.of(0, 0, 0),
                Arguments.of(-2, 5, 3),
                Arguments.of(-2, -2, -4),
                Arguments.of(2.5, 2.5, 5),
                Arguments.of(454564564, 945156456, (454564564 + 945156456))
        );
    }

    private static Stream<Arguments> minusRequestProvider() {
        return Stream.of(
                Arguments.of(2, 2, 0),
                Arguments.of(2, 0, 2),
                Arguments.of(0, 2, -2),
                Arguments.of(0, 0, 0),
                Arguments.of(-2, 5, -7),
                Arguments.of(-2, -2, 0),
                Arguments.of(4.8, 2.8, 2),
                Arguments.of(454564564, 945156456, (454564564 - 945156456))
        );
    }

    private static Stream<Arguments> divisionRequestProvider() {
        return Stream.of(
                Arguments.of(2, 2, 1),
                Arguments.of(0, 2, 0),
                Arguments.of(-4, 2, -2),
                Arguments.of(-2, -2, 1),
                Arguments.of(4.8, 4.2, 1.1),
                Arguments.of(454564564, 945156456, (454564564 / 945156456))
        );
    }

    private static Stream<Arguments> multiplicationRequestProvider() {
        return Stream.of(
                Arguments.of(2, 2, 4),
                Arguments.of(2, 0, 0),
                Arguments.of(0, 2, 0),
                Arguments.of(0, 0, 0),
                Arguments.of(-2, 5, -10),
                Arguments.of(-2, -2, 4),
                Arguments.of(2.5, 2.5, 6.25),
                Arguments.of(454564564, 945156456, (454564564 * 945156456))
        );
    }

    @DisplayName("Проверка оперции сложения")
    @ParameterizedTest(name = "{displayName}, запрос: {arguments}")
    @MethodSource("plusRequestProvider")
    @Tag("Positive")
    void plusTest(Number a, Number b, Number expectedResult) {
        RequestSpecBuilder request = new RequestSpecBuilder()
                .addParam("a", a)
                .addParam("b", b)
                .addParam("method", "+");

        RestAssuredHelper.newInstance()
                .execute(Method.GET, request)
                .checkResult(new ResponseSpecBuilder().expectStatusCode(200)
                        .expectBody("result", equalTo(expectedResult)));
    }

    @DisplayName("Проверка оперции вычетания")
    @ParameterizedTest(name = "{displayName}, запрос: {arguments}")
    @MethodSource("minusRequestProvider")
    @Tag("Positive")
    void minusTest(Number a, Number b, Number expectedResult) {
        RequestSpecBuilder request = new RequestSpecBuilder()
                .addParam("a", a)
                .addParam("b", b)
                .addParam("method", "+");

        RestAssuredHelper.newInstance()
                .execute(Method.GET, request)
                .checkResult(new ResponseSpecBuilder().expectStatusCode(200)
                        .expectBody("result", equalTo(expectedResult)));
    }

    @DisplayName("Проверка оперции деления")
    @ParameterizedTest(name = "{displayName}, запрос: {arguments}")
    @MethodSource("divisionRequestProvider")
    @Tag("Positive")
    void divisionTest(Number a, Number b, Number expectedResult) {
        RequestSpecBuilder request = new RequestSpecBuilder()
                .addParam("a", a)
                .addParam("b", b)
                .addParam("method", "/");

        RestAssuredHelper.newInstance()
                .execute(Method.GET, request)
                .checkResult(new ResponseSpecBuilder().expectStatusCode(200)
                        .expectBody("result", equalTo(expectedResult)));
    }

    @DisplayName("Проверка оперции умножения")
    @ParameterizedTest(name = "{displayName}, запрос: {arguments}")
    @MethodSource("multiplicationRequestProvider")
    @Tag("Positive")
    void multiplicationTest(Number a, Number b, Number expectedResult) {
        RequestSpecBuilder request = new RequestSpecBuilder()
                .addParam("a", a)
                .addParam("b", b)
                .addParam("method", "*");

        RestAssuredHelper.newInstance()
                .execute(Method.GET, request)
                .checkResult(new ResponseSpecBuilder().expectStatusCode(200)
                        .expectBody("result", equalTo(expectedResult)));
    }

    @DisplayName("Выполнение запроса без указания обязательных параметров")
    @ParameterizedTest(name = "{displayName}, запрос: {0}")
    @MethodSource("negativeRequestProvider")
    @Tag("Negative")
    void withoutRequiredParametersTest(RequestSpecBuilder request, ResponseSpecBuilder expectedResponse) {
        RestAssuredHelper.newInstance()
                .execute(Method.GET, request)
                .checkResult(expectedResponse);
    }

    @DisplayName("Выполнение запроса с указанием некорректных параметров")
    @ParameterizedTest(name = "{displayName}, запрос: {arguments}")
    @MethodSource("invalidParameterRequestProvider")
    @Tag("Negative")
    void invalidParameterTest(Object a, Object b, String method, ResponseSpecBuilder expectedResponse) {
        RequestSpecBuilder request = new RequestSpecBuilder()
                .addParam("a", a)
                .addParam("b", b)
                .addParam("method", method);

        RestAssuredHelper.newInstance()
                .execute(Method.GET, request)
                .checkResult(expectedResponse);

    }

    @Test
    @DisplayName("Проверка при делении на ноль")
    @Tag("Negative")
    void deleteByZeroTest() {
        RequestSpecBuilder request = new RequestSpecBuilder()
                .addParam("a", 2)
                .addParam("b", 0)
                .addParam("method", "/");

        RestAssuredHelper.newInstance()
                .execute(Method.GET, request)
                .checkResult(DIVISION_BY_ZERO);
    }
}