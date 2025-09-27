package memoza.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import memoza.data.DataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


//Получение списка пространств имен


public class GetAllAvailableNamespacesTest {
    public String ticket;

    @BeforeEach
    void setUpTicket() {
        ticket = DataHelper.loginAndGetTicket();
    }

    @Test
    public void shouldReturnNSPList (){
        Response response= DataHelper.getAndPrintResponse(DataHelper.loginAndGetTicket(), "/memoza-rest-server/schema/nsp.json");
        // Базовые проверки
        assertEquals(200, response.getStatusCode(), "Ожидаем HTTP 200");
        // TODO: содержит определённые  namespaces
        assertEquals("application/json", response.contentType().split(";")[0], "Ответ должен быть JSON");
        // Десериализуем в Map<String,Integer>, потому что ключи динамические
        Map<String, Integer> namespaces = response.as(Map.class);
        assertFalse(namespaces.isEmpty(), "Список namespace не должен быть пустым");
        // Проверим, что все ревизии – положительные числа
        namespaces.forEach((k, v) -> {
            assertNotNull(v, "Revision для " + k + " не должен быть null");
            assertTrue(v instanceof Number, "Revision должен быть числом");
            assertTrue(((Number) v).longValue() >= 0, "Revision для " + k + " должен быть > 0"); //вообще не должен быть 0, уточнить у Дмитрия
        });
    }
}

//TODO:: С истёкшим токеном //401/403

//TODO:: без авторизации




