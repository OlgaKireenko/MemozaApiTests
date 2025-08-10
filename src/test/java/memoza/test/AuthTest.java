package memoza.test;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import memoza.data.Config;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import memoza.data.DataHelper;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static memoza.data.DataHelper.*;
import static org.hamcrest.Matchers.*;
public class AuthTest {
    @BeforeAll

    static void setup() {
        RestAssured.baseURI = BASE_URI;
    }
    @Test
    public void loginShouldReturnTicket() {
        given()
                .contentType("application/json")
                .body(Map.of("userName", username, "password", password))
                .when()
                .post("/whereoil-rest-server/security/auth/login.json")
                .then()
                .statusCode(200)
                .body("username", equalTo(username))  //проверяет username в response, позже уберу в параметры
                .body("ticket", notNullValue()) //проверяет, что тикет не null (возможно, избыточная проверка)
                .body("ticket", hasLength(32)) //проверяет длину тикета, 32 - фиксированная длина?
        ;
    }
    @Test
    void shouldReturnErrorForInvalidCredentials() {
        given()
                .contentType("application/json")
                .body(Map.of(
                        "userName", Config.get("username"),
                        "password", "wrongPass"
                ))
                .when()
                .post("/whereoil-rest-server/security/auth/login.json")
                .then()
                .statusCode(401)
                .body("error.code", equalTo("AUTH_1")) //
                .body("error.message", containsString("Authentication service not configured")); //
    }

}
