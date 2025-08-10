package memoza.test;

import io.restassured.response.Response;
import memoza.data.DataHelper;
import org.junit.jupiter.api.Test;

import static java.util.concurrent.CompletableFuture.anyOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateSchemaTest {

    @Test
    public void shouldCreateSchema() {

        String ticket = DataHelper.loginAndGetTicket();
        String schemaJson = DataHelper.loadJsonFromResource("schema.json");
        Response response = DataHelper.createSchema(ticket, schemaJson);
        response.then().statusCode(200);
        //TODO: убедиться, что ответ сервера true
        System.out.println("Ответ сервера: " + response.getBody().asString());
    }

    @Test
    public void shouldGetSchema() {
        String ticket = DataHelper.loginAndGetTicket();
        Boolean exists = DataHelper.schemaExists(ticket, "llmquestions09");
        assertTrue(exists, "Ожидалась созданная схема 'llmquestions09', но она не найдена");
    }


    @Test
    public void shouldDeleteSchema() {
        String ticket = DataHelper.loginAndGetTicket();
        Boolean deleted = DataHelper.deleteSchema(ticket, "llmquestions09");
        assertTrue(deleted, "Схему не удалось удалить");
    }
    //TODO: убедиться, что ответ сервера true

    }












