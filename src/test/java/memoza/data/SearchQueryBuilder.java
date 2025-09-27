package memoza.data;

import org.json.JSONObject;

public class SearchQueryBuilder {
    public static String searchByClassName(String className) {
        return "{\n" +
                "  \"query\": {\n" +
                "    \"classname\": \"" + className + "\"\n" +
                "  }\n" +
                "}";
    }

    public static String buildQueryStringSearch(String queryString) {


        JSONObject root = new JSONObject();
        JSONObject query = new JSONObject();

        query.put("queryString", queryString);
        root.put("query", query);

        return root.toString(2);
    }

}








