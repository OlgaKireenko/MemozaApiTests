package memoza.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import memoza.data.SearchQueryBuilder;
import memoza.data.DataHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static memoza.data.DataHelper.BASE_URI;
import static memoza.data.DataHelper.SEARCH_ENDPOINT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchDataRequestsTest {

    //public static String namespace = "kbox_volve"; //TO DO: убрать в датакласс
    public String ticket;

    @BeforeAll
    static void setup() {
        //RestAssured.baseURI = BASE_URI;
        //RestAssured.baseURI = "https://memoza.search-centric.ru";
        RestAssured.useRelaxedHTTPSValidation(); // Отключаем проверку SSL-сертификатов глобально
    }

    @BeforeEach
    void setUpTicket() {
        ticket = DataHelper.loginAndGetTicket();
    }

    @Test
        //Подготовка request body, внутренний тест
    void testByClassName() {
        String requestBody = SearchQueryBuilder.searchByClassName("");
        System.out.println(requestBody);
        assertTrue(requestBody.contains("\"classname\": \"\""));
    }

    @Test
    void testSearchByClassName() {
        // 1. Готовим JSON через наш билдер
        String requestBody = SearchQueryBuilder.searchByClassName("");
        // 2. Отправляем POST запрос
        Response response = given()
                .baseUri(baseURI)
                .headers(DataHelper.buildAuthHeaders(ticket))
                //.contentType("application/json")
                .body(requestBody)
                .when()
                .post("/memoza-rest-server/data/kbox_volve/_search.json")
                .then()
                .extract().response();

        // 3. Проверяем статус код
        assertEquals(200, response.getStatusCode());

        // 4. (опционально) печатаем ответ для наглядности
        //System.out.println(response);
    }

    @Test
    void testSearchByQueryText () {

        String requestBody = SearchQueryBuilder.buildQueryStringSearch("Лисянское");
        Response response =
                given()
                        .baseUri(BASE_URI)
                        .headers(DataHelper.buildAuthHeaders(ticket))
                        .body(requestBody)
                        .post("/memoza-rest-server/data/llmquestions/_search.json")
                        .then()
                        .extract()
                        .response();

        // 3. Проверяем статус код

        assertEquals(200, response.statusCode());
        assertTrue(response.asString().contains("Лисянское"));

        // 4. (опционально) печатаем ответ для наглядности
        //System.out.println(response);


    }



}





