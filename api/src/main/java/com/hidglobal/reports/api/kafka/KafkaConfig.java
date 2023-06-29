package com.hidglobal.reports.api.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.hidglobal.reports.api.util.JsonNodeParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hidglobal.reports.common.ApiTestConstant.ATTRIBUTES;
import static com.hidglobal.reports.common.ApiTestConstant.EVENT_NAME;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private static final int CONCURRENCY = 3;
    private static final int POLL_TIMEOUT = 3000;
    private final KafkaProperties kafkaProperties;
    private final JsonNodeParser jsonNodeParser;
    private static final List<String> VALID_EVENTS = List.of("ALLOWED_NORMAL_IN", "ALLOWED_NORMAL_OUT");

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(CONCURRENCY);
        factory.getContainerProperties().setPollTimeout(POLL_TIMEOUT);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setAckDiscarded(true);
        factory.setRecordFilterStrategy(recordFilterStrategy());

        return factory;
    }

    @Bean
    public ConsumerFactory<Integer, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configureProtocol(props);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configureProtocol(props);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private void configureProtocol(Map<String, Object> props) {
        KafkaProperties.Ssl protocol = kafkaProperties.getSsl();
        props.put(SslConfigs.SSL_PROTOCOL_CONFIG, protocol.getProtocol());
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, protocol.getProtocol());
    }

    private RecordFilterStrategy<Integer, String> recordFilterStrategy() {
        return consumerRecord -> {
            String message = consumerRecord.value();
            boolean isValidEvent;
            try {
                JsonNode attributesNode = jsonNodeParser.findValueAtNode(ATTRIBUTES, message);
                String eventName = attributesNode.get(EVENT_NAME).asText();
                isValidEvent = VALID_EVENTS.contains(eventName);
            } catch (JsonProcessingException e) {
                log.error("Unable to parse the incoming message {}", message);
                return true;
            }

            return !isValidEvent;
        };
    }
}
