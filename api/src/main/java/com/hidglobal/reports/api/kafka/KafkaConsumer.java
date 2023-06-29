package com.hidglobal.reports.api.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.hidglobal.reports.api.util.JsonNodeParser;
import com.hidglobal.reports.persistence.entity.EventType;
import com.hidglobal.reports.persistence.entity.RoundTripEntity;
import com.hidglobal.reports.persistence.repository.RoundTripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

import static com.hidglobal.reports.api.util.JsonNodeParser.extractValue;
import static com.hidglobal.reports.common.ApiTestConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final JsonNodeParser jsonNodeParser;
    private final RoundTripRepository roundTripRepository;

    @KafkaListener(topics = "${spring.kafka.consumer.properties.notification-topic}",
            groupId = "${spring.kafka.consumer.properties.notification-group}")
    public void listen(String message, Acknowledgment acknowledgment) {
            log.info("Received Message : {} ", message);
        try {
            JsonNode attributesNode = jsonNodeParser.findValueAtNode(ATTRIBUTES, message);
            String credHolderId = jsonNodeParser.findValueAtNode(KEY, message)
                    .get(CRED_HOLDER_ID)
                    .asText();
            long timestamp = extractTimestamp(attributesNode);
            String credHolderName = extractValue(attributesNode, CRED_HOLDER_NAME);
            String eventName = attributesNode.get(EVENT_NAME).asText();
            RoundTripEntity roundTripEntity = new RoundTripEntity(credHolderId, timestamp,
                    credHolderName, EventType.valueOf(eventName));
            roundTripRepository.save(roundTripEntity);
        } catch (Exception e) {
            log.error("Error retrieving kafka event", e);
        } finally {
            acknowledgment.acknowledge(); //We still acknowledge as this was a duplicate message
        }
    }

    private long extractTimestamp(JsonNode attributesNode) {
        String timestamp = extractValue(attributesNode, DATE_TIME);
        return ZonedDateTime.parse(timestamp, ISO_FORMATTER).toEpochSecond();
    }

}
