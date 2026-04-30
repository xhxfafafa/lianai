package com.xhx.xhxlianai.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoveAppTest {


    @Resource
    private LoveApp loveApp;

    @Test
    void chat() {
    }

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员鱼皮";
        String answer = loveApp.chat(chatId, message);
        // 第二轮
        message = "我想让另一半（编程导航）更爱我";
        answer = loveApp.chat(chatId, message);
        Assertions.assertNotNull(answer);
        // 第三轮
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = loveApp.chat(chatId, message);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWith() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员鱼皮,但是我不知道怎么让另一半更加爱我,我该怎么做,直接给我建议";
        LoveApp.LoveReport loveReport = loveApp.doChatWith(message, chatId);
        Assertions.assertNotNull(loveReport);

        message = "我想让另一半（编程导航）更爱我";
        loveReport = loveApp.doChatWith(message, chatId);
        Assertions.assertNotNull(loveReport);
    }
}