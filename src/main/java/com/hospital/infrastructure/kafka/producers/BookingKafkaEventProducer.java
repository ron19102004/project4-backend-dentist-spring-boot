package com.hospital.infrastructure.kafka.producers;

import com.hospital.infrastructure.kafka.events.BookingKafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingKafkaEventProducer {
    private final KafkaTemplate<String, BookingKafkaEvent> bookingEventKafkaTemplate;
    @Autowired
    public BookingKafkaEventProducer(KafkaTemplate<String, BookingKafkaEvent> bookingEventKafkaTemplate) {
        this.bookingEventKafkaTemplate = bookingEventKafkaTemplate;
    }

    public void pushBookingKafkaEvent(BookingKafkaEvent bookingEvent) {
        bookingEventKafkaTemplate.send("booking-event", bookingEvent);
    }
}
