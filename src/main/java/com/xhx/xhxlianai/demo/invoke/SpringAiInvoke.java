package com.xhx.xhxlianai.demo.invoke;

import com.xhx.xhxlianai.XhxLianaiApplication;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringAiInvoke {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(XhxLianaiApplication.class, args);

        ChatModel chatModel = context.getBean(ChatModel.class);

        String response = chatModel.call("介绍一下你自己");
        System.out.println(response);

        context.close();
    }
}

