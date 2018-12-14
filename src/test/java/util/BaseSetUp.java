package util;

import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class BaseSetUp {
    protected static Config config = ConfigFactory.create(Config.class);

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = config.host();
        RestAssured.port = config.port();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterAll
    public static void tearDown() {
        RestAssured.reset();
    }
}