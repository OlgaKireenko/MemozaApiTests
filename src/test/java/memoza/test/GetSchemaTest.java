package memoza.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import memoza.data.DataHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static memoza.data.DataHelper.BASE_URI;
import static org.hamcrest.Matchers.*;

public class GetSchemaTest {
    public String ticket;


    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URI;
        //RestAssured.baseURI = "https://memoza.search-centric.ru";
        RestAssured.useRelaxedHTTPSValidation(); // Отключаем проверку SSL-сертификатов глобально
    }

    @BeforeEach
    void setUpTicket() {
        ticket = DataHelper.loginAndGetTicket();
    }


    @Test
    public void shouldReturnSchemaJson() {
        Response response = DataHelper.getSchema(ticket, "kbox_volve");
        response.then()
                .statusCode(200)
                .contentType("application/json");
    }

    @Test
    public void schemaShouldContainFields() {
        Response response = DataHelper.getSchema(ticket, "kbox_volve");
        response.then()
                .statusCode(200)
                .body("$", hasKey("classes"))
                .body("$", hasKey("version"))
//                .body("classes", not(empty()))// корневой объект содержит "classes"
//                .body("version", not(empty()))
        ;
    }
    @Test
    public void schemaNameShouldBeKboxVolve() {
        Response response = DataHelper.getSchema(ticket, "kbox_volve");
        response.then()
                .statusCode(200)
                .body("name", equalTo("kbox_volve"));
    }

    @Test
    public void fieldsListShouldNotBeEmpty() {
        Response response = DataHelper.getSchema(ticket, "kbox_volve");
        response.then()
                .statusCode(200)
                .body("classes", not(empty())); // убеждаемся, что есть классы
    }
}



