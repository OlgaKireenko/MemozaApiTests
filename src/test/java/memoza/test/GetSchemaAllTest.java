package memoza.test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import memoza.data.DataHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;


import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetSchemaAllTest {


    public static Response fullSchemaResponse;

       @Test
    public void shouldReturnFullSchema() {
        fullSchemaResponse =  DataHelper.fetchAndSaveSchemaAll(DataHelper.loginAndGetTicket(), "src/test/resources/schema/all_schema_snapshot.json");

        assertEquals(200, fullSchemaResponse.statusCode(), "Ожидаем 200 OK");
        assertTrue(fullSchemaResponse.contentType().startsWith("application/json"));


    // Десериализация всего массива схем
        List<Map<String, Object>> schemas = fullSchemaResponse.jsonPath().getList("$");
        assertFalse(schemas.isEmpty(), "Список схем не должен быть пустым");

        for (Map<String, Object> schema : schemas) {
            assertNotNull(schema.get("name"), "Schema.name обязателен");
            assertTrue(
                    schema.keySet().stream().anyMatch(k -> k.toLowerCase().startsWith("label")),
                    "Должно быть поле, начинающееся с 'label'"
            );
            // classes — это массив, поэтому приводим к списку карт
            // metaProperties — объект (map) с ключами = именам свойств
                Object metaObj = schema.get("metaProperties");
                if (metaObj instanceof Map) {
                    Map<String, Object> metaProps = (Map<String, Object>) metaObj;
                    metaProps.forEach((key, value) -> {
                        assertTrue(value instanceof Map, "metaProperties должно содержать Map");
                        Map<String, Object> mp = (Map<String, Object>) value;
                        assertEquals(key, mp.get("name"),
                                "Ключ metaProperties должен совпадать с name");
                        assertNotNull(mp.get("type"), "metaProperties.type обязателен");
                    });
                }
            }
        }
    }

