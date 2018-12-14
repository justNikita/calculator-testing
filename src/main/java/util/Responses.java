package util;

import io.restassured.builder.ResponseSpecBuilder;
import lombok.experimental.UtilityClass;

import static org.hamcrest.Matchers.equalTo;

@UtilityClass
public class Responses {
    //FAILED
    public static final ResponseSpecBuilder WITHOUT_METHOD = new ResponseSpecBuilder().expectStatusCode(400)
            .expectBody("message", equalTo("Не указан метод"));
    public static final ResponseSpecBuilder WITHOUT_B = new ResponseSpecBuilder().expectStatusCode(400)
            .expectBody("message", equalTo("Не указан паарметр b"));
    public static final ResponseSpecBuilder WITHOUT_A = new ResponseSpecBuilder().expectStatusCode(400)
            .expectBody("message", equalTo("Не указан паарметр a"));
    public static final ResponseSpecBuilder EMPTY_REQUEST = new ResponseSpecBuilder().expectStatusCode(400)
            .expectBody("message", equalTo("Некорректный запрос"));
    public static final ResponseSpecBuilder DIVISION_BY_ZERO = new ResponseSpecBuilder().expectStatusCode(400)
            .expectBody("message", equalTo("Деление на ноль невозможно"));
    public static final ResponseSpecBuilder INVALID_VALUE_A = new ResponseSpecBuilder().expectStatusCode(400)
            .expectBody("message", equalTo("Указано некорректное значение для параметра a"));
    public static final ResponseSpecBuilder INVALID_VALUE_B = new ResponseSpecBuilder().expectStatusCode(400)
            .expectBody("message", equalTo("Указано некорректное значение для параметра b"));
    public static final ResponseSpecBuilder INVALID_VALUE_METHOD = new ResponseSpecBuilder().expectStatusCode(400)
            .expectBody("message", equalTo("Указано некорректное значение для параметра method"));
}