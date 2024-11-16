package com.hospital.kafka.conf.consumers;

import com.hospital.kafka.events.BookingKafkaEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaBookingConsumerConf {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, BookingKafkaEvent> bookingConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "booking-consumer");
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                bookingEventJsonDeserializer());
    }

    @Bean
    public JsonDeserializer<BookingKafkaEvent> bookingEventJsonDeserializer() {
        JsonDeserializer<BookingKafkaEvent> deserializer = new JsonDeserializer<>(BookingKafkaEvent.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("com.history.kafka.events");
        return deserializer;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingKafkaEvent> bookingEventConcurrentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BookingKafkaEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingConsumerFactory());
        factory.setCommonErrorHandler(new CommonErrorHandler() {
            @Override
            public void handleOtherException(Exception thrownException,
                                             Consumer<?, ?> consumer,
                                             MessageListenerContainer container,
                                             boolean batchListener) {
                System.out.println(thrownException.getMessage());
            }
        });
        return factory;
    }
}