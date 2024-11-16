package com.hospital.kafka.producers;

import com.hospital.kafka.events.BookingKafkaEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BookingKafkaEventProducer {
    @Autowired
    private KafkaTemplate<String, BookingKafkaEvent> bookingEventKafkaTemplate;
    public void pushBookingKafkaEvent(BookingKafkaEvent bookingEvent) {
        bookingEventKafkaTemplate.send("booking-event", bookingEvent);
    }
}
