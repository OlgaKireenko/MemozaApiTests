package memoza.data;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;

public class DataHelper {
    // todo: все поля сделать приватными, написать методы доступа к ним
    public static final String BASE_URI = Config.get("base.uri");
    private static final String LOGIN_ENDPOINT = Config.get("login.endpoint");
    private static final String VALIDATE_ENDPOINT = Config.get("validate.endpoint");
    public static final String username = Config.get("username");
    public static final String password = Config.get("password");
    public static final String STATIC_TOKEN = Config.get("static.token");


    static {
        RestAssured.baseURI = BASE_URI;
        RestAssured.useRelaxedHTTPSValidation();
    }


    public static Headers buildAuthHeaders(String ticket) {
        return new Headers(
                new Header("Content-Type", "application/json;charset=UTF-8"),
                new Header("kadme.security.token", STATIC_TOKEN),
                new Header("ticket", ticket)
        );
    }

    public static String loginAndGetTicket() {
        RestAssured.useRelaxedHTTPSValidation();
        return
                given()
                        .contentType(ContentType.JSON)
                        .body(Map.of("userName", username, "password", password))
                        .when()
                        .post(LOGIN_ENDPOINT)
                        .then()
                        .statusCode(200)
                        .extract().path("ticket");
    }


    public static long getValidUntilInLong() { // возвращает дату в лонг
        String ticket = loginAndGetTicket();
        Response response =
                given()
                        .queryParam("ticket", ticket)
                        .when()
                        .get(VALIDATE_ENDPOINT)
                        .then()
                        .statusCode(200)
                        .body(notNullValue())
                        .extract().response();

        String body = response.getBody().asString(); //получаем тело в String
        try {
            return Long.parseLong(body);
        } catch (NumberFormatException e) {
            fail("Response is not a valid number: " + body);
            throw new RuntimeException("Unreachable, but required by compiler"); // чтобы компилятор не ругался
        }
    }

    public static long parseEpochMillis(String body) {
        try {
            return Long.parseLong(body);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Not a valid epoch millis: " + body, e);
        }
    }

    public static LocalDateTime convertMillisToDateTime(long millis) {
        return convertMillisToDateTime(millis, ZoneId.systemDefault().getId());
    }

    public static LocalDateTime convertMillisToDateTime(long millis, String zoneId) {
        Instant instant = Instant.ofEpochMilli(millis);
        return LocalDateTime.ofInstant(instant, ZoneId.of(zoneId));
    }

    public static long millisToHours(long millis) {
        return millis / (1000 * 60 * 60);
    }


    public static Response createSchema(String ticket, String schemaJson) {
        Headers headers = DataHelper.buildAuthHeaders(ticket);

        return given()
                .baseUri(BASE_URI)
                .headers(headers)
                .body(schemaJson)
                .when()
                .post("/memoza-rest-server/schema/create/olki2.json");
    }

    public static String loadJsonFromResource(String filename) {
        try {
            return new String(Files.readAllBytes(Path.of("src/main/resources/" + filename)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить JSON-файл: " + filename, e);
        }
    }

    public static boolean schemaExists(String ticket, String namespace) {

        Response response = given()
                .baseUri(BASE_URI)
                .headers(DataHelper.buildAuthHeaders(ticket))
                .when()
                .get("memoza-rest-server/schema/nsp/" + namespace + ".json");

        int status = response.getStatusCode();
        System.out.println(status);
        return status == 200;
    }

    public static boolean deleteSchema(String ticket, String namespace) {
        Response response = given()
                .baseUri(BASE_URI)
                .headers(DataHelper.buildAuthHeaders(ticket))
                .when()
                .delete("/memoza-rest-server/schema/delete/" + namespace + ".json");

        int status = response.getStatusCode();
        return status == 200; // или 204, если сервер так настроен
    }

    public static Response getSchema(String ticket, String namespace) {
        return given()
                .headers(buildAuthHeaders(ticket))
                .when().get("/memoza-rest-server/schema/nsp/" + namespace + ".json");

    }

    public static Response getAndPrintResponse(String ticket, String url) {

        Response response = given()
                .headers(buildAuthHeaders(loginAndGetTicket()))
                .accept(ContentType.JSON)
                .when().get(url);
        System.out.println("=== GET " + url + " ===");
        response.prettyPrint();
        System.out.println("Status: " + response.getStatusCode());

        return response;
    }

    public static Response getResponse(String ticket, String url) {

        Response response = given()
                .headers(buildAuthHeaders(loginAndGetTicket()))
                .accept(ContentType.JSON)
                .when().get(url);
        System.out.println("=== GET " + url + " ===");
        //response.prettyPrint();
        //System.out.println("Status: " + response.getStatusCode());

        return response;
    }

    public static Response fetchAndSaveSchemaAll(String ticket, String targetPath) {
        Response response = getResponse(ticket, "/memoza-rest-server/schema/all.json");

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Schema request failed: " + response.statusLine());
        }

        try {
            Path out = Path.of(targetPath);
            Files.createDirectories(out.getParent());
            Files.writeString(out, response.asString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Can't save schema snapshot", e);
        }
        return response;
    }
}















