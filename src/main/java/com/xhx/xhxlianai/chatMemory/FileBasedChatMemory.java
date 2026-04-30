package com.xhx.xhxlianai.chatMemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileBasedChatMemory implements ChatMemory {
    private final String BASE_DIR;
    private static final Kryo kryo = new Kryo();

    static {
        // 配置 Kryo
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

    }

    public FileBasedChatMemory(String dir) {
        this.BASE_DIR = dir;
        File baseDir = new File(dir);
        if (!baseDir.exists() && !baseDir.mkdirs()) {
            System.err.println("Failed to create directory: " + dir);
        }
    }

    @Override
    public void add(String conversationId, Message message) {
        add(conversationId, List.of(message));
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> conversationMessages = getOrCreateConversation(conversationId);
        conversationMessages.addAll(messages);
        saveConversation(conversationId, conversationMessages);
    }

    @Override
    public List<Message> get(String conversationId) {
        return getOrCreateConversation(conversationId);
    }

    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists() && !file.delete()) {
            System.err.println("Failed to delete: " + file.getAbsolutePath());
        }
    }

    @SuppressWarnings("unchecked")
    private List<Message> getOrCreateConversation(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();

        if (file.exists()) {
            try (Input input = new Input(new FileInputStream(file))) {
                messages = kryo.readObject(input, ArrayList.class);
                System.out.println("Loaded " + messages.size() + " messages from " + conversationId);
            } catch (Exception e) {
                System.err.println("Failed to load conversation: " + conversationId);
                e.printStackTrace();
                // 删除损坏的文件
                if (file.exists() && !file.delete()) {
                    System.err.println("Failed to delete corrupted file: " + file.getAbsolutePath());
                }
            }
        }
        return messages;
    }

    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        try (Output output = new Output(new FileOutputStream(file))) {
            kryo.writeObject(output, messages);
            System.out.println("Saved " + messages.size() + " messages to " + conversationId);
        } catch (IOException e) {
            System.err.println("Failed to save conversation: " + conversationId);
            e.printStackTrace();
        }
    }

    private File getConversationFile(String conversationId) {
        // 清理文件名中的非法字符
//        String safeId = conversationId.replaceAll("[^a-zA-Z0-9-]", "_");
        return new File(BASE_DIR, conversationId + ".kryo");
    }
}