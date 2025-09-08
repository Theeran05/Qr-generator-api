package com.miniproject.qr_generator.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.miniproject.qr_generator.dto.QrRequest;

@Service
public class QrLinkService {

    private final Map<String, QrRequest> linkMap = new ConcurrentHashMap<>();

    public String register(QrRequest request) {
        String id = generateUniqueId(); // e.g., UUID or custom logic
        linkMap.put(id, request);
        return id;
    }

    public QrRequest getRequest(String id) {
        return linkMap.get(id);
    }

    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString().substring(0, 8); // short ID
    }
}
