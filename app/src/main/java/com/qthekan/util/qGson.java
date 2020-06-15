package com.qthekan.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class qGson {

    /**
     * getter 가 설정된 class object 를 전달하면 json 포멧 문자열을 반환한다.
     */
    public static String prettyPrint(Object o)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(o);
        
        return jsonString;
    }
    
    
    public static String prettyPrint(String json)
    {
        JsonParser parser = new JsonParser();
        //return prettyPrint(JsonParser.parseString(json));
        return prettyPrint(parser.parse(json));
    }
    
}
