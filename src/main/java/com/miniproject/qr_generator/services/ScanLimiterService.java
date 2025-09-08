package com.miniproject.qr_generator.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScanLimiterService {

    private final Map<String, ScanRecord> scanMap = new ConcurrentHashMap<>();

    public boolean canSend(String userId) {
        ScanRecord record = scanMap.get(userId);
        LocalDate today = LocalDate.now();

        if (record == null || !record.date.equals(today)) {
            scanMap.put(userId, new ScanRecord(today, 1));
            return true;
        }

        if (record.count < 2) {
            record.count++;
            return true;
        }

        return false;
    }

    private static class ScanRecord {
        LocalDate date;
        int count;

        ScanRecord(LocalDate date, int count) {
            this.date = date;
            this.count = count;
        }
    }
}
