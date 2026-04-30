package com.xhx.xhxlianai.demo.invoke;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * http请求
 */
public class CurlAivoke {

    private static final String URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

    public static void main(String[] args) {
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant.");

        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", "解释什么是微服务架构");

        Map<String, Object> input = new HashMap<>();
        input.put("messages", List.of(systemMessage, userMessage));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("result_format", "message");

        Map<String, Object> body = new HashMap<>();
        body.put("model", "qwen-plus");
        body.put("input", input);
        body.put("parameters", parameters);

        try (HttpResponse response = HttpRequest.post(URL)
                .header("Authorization", "Bearer " + TestApiKey.API_KEY)
                .header("Content-Type", ContentType.JSON.getValue())
                .body(JSONUtil.toJsonStr(body))
                .execute()) {
            JSONObject jsonObject = JSONUtil.parseObj(response.body());
            String answer = jsonObject
                    .getJSONObject("output")
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getStr("content");
            System.out.println(answer);
        }
    }
}
