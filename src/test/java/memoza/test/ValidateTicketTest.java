package memoza.test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import memoza.data.DataHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.restassured.http.ContentType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static memoza.data.DataHelper.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValidateTicketTest {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    void shouldValidateTicket() {  //todo: Название не нравится
        // Шаг 1. Логин → получаем тикет
        String ticket = DataHelper.loginAndGetTicket();
        // Шаг 2. Передаём тикет в GET-запросе как query-параметр
        given()
                .queryParam("ticket", ticket)
                .when()
                .get("/whereoil-rest-server/security/auth/validateticket.json")
                .then()
                .statusCode(200)
                .body(notNullValue());

    }

    @Test
    //Проверяет, что дата тикета в будущем, уточнить у Дмитрия, какой срок службы у тикета в часах

    public void shouldCheckValidUntilDate() {

        //String ticket = DataHelper.loginAndGetTicket();
        long millis = DataHelper.getValidUntilInLong();
        LocalDateTime validUntilForTicket = DataHelper.convertMillisToDateTime(millis);
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        assertTrue(validUntilForTicket.isAfter(now), "validUntil is not in the future: " + validUntilForTicket);
    }

    @Test

        // Проверяет срок службы тикета не больше скольки-то часов
    void ticketLifetimeShouldNotExceed8ours() {

        long validUntilMillis = getValidUntilInLong();
        long nowMillis = System.currentTimeMillis();

        long diffMillis = validUntilMillis - nowMillis;

        System.out.println("Valid for " + DataHelper.millisToHours(diffMillis) + " hours");

        long maxAllowedMillis = 9 * 60 * 60 * 1000L; // 8 или 9 часов в миллисекундах

        assertTrue(diffMillis > 0, "Ticket is already expired");
        assertTrue(diffMillis <= maxAllowedMillis, "Ticket lifetime exceeds 8 hours: " + (diffMillis / 3600000.0) + "h");

    }

    @Test
    public void shouldFailIfTicketIsExpired() {
        String expiredTicket = "15192a43dacefb34dc34c1cc74944e9f"; // to do: просроченный тикет найти

        // Временный метод — подменим вызов внутри DataHelper
        Response response = given()
                .queryParam("ticket", expiredTicket)
                .when()
                .get("/whereoil-rest-server/security/auth/validateticket.json")
                .then()
                .statusCode(401)
                .body("error.code", equalTo("AUTH_3")) //
                .body("error.message", containsString("not valid"))
                .extract().response();
        System.out.println(response);
    }
}





