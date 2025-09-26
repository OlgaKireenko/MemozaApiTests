package example;

import memoza.data.DataHelper;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Map;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//    String body = DataHelper.loginAndGetTicket();
//    System.out.println(body);

        //long time = DataHelper.getValidUntilAsDate();

//        String ticket = DataHelper.loginAndGetTicket();
     //long time = DataHelper.getValidUntilInLong();

//        System.out.println(DataHelper.convertMillisToDateTime(time));
//        System.out.println(DataHelper.convertMillisToDateTime(time, "Europe/Moscow"));
//        System.out.println(ticket);
//        System.out.println(time);

        // Получаем тикет
       String ticket = DataHelper.loginAndGetTicket();
//
//        // Получаем пространства имён
//        Map<String, Integer> namespaces = DataHelper.getNamespaces(ticket);
//
//        // Печатаем результат
//        System.out.println("Список пространств имён:" );
//        namespaces.forEach((key, value) ->
//                System.out.println(" - " + key + " : " + value));

//String ticket = DataHelper.loginAndGetTicketDev05();
//Boolean existst = DataHelper.schemaExists(ticket, "llmquestions08");

        System.out.println(ticket);

        // DataHelper.getAndPrintResponse(DataHelper.loginAndGetTicket(), "/memoza-rest-server/schema/all.json");
        DataHelper.fetchAndSaveSchemaAll(ticket, "src/test/resources/schema/all_schema_snapshot.json");


    }}
