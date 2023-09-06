package com.luv2code.springbootlibrary.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJWT {

    public static String payloadJWTExtraction(String token, String extraction){

        token.replace("Bearer ", "");

        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String payload = new String(decoder.decode(chunks[1]));//decode之后的结果类型是字节数组；String的构造函数可以接受字节数组然后将其转成string

        String[] entries = payload.split(",");
        Map<String, String> map = new HashMap<String, String>();

        for(String entry : entries){
            String[] keyValue = entry.split(":");
            if(keyValue[0].equals(extraction)) {

                int remove = 1;
                //判断"sub"的值是否以}结尾；如果是就设置remove为2
                if (keyValue[1].endsWith("}")) {
                    remove = 2;
                }
                //移除所提取值结尾的引号或者右括号；substring（beginindex-inclusive，endindex-exclusive）
                keyValue[1] = keyValue[1].substring(0, keyValue[1].length()-remove);
                //移除前面的引号
                keyValue[1] = keyValue[1].substring(1);

                map.put(keyValue[0],keyValue[1]);
            }
        }

        if(map.containsKey(extraction)){
            return map.get(extraction);
        }
        return null;
    }
}
