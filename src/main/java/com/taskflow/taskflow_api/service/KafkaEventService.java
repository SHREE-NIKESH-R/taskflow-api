package com.taskflow.taskflow_api.service;

import org.springframework.stereotype.Service;

@Service
public class KafkaEventService {

    public void publishTaskEvent(String eventType, Long taskId, String email) {
        String message = String.format(
                "{\"event\":\"%s\",\"taskId\":%d,\"user\":\"%s\"}",
                eventType, taskId, email);

        System.out.println("[EVENT] " + message);
    }
}
