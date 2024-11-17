package com.titans.ahs.formatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(LocalDateTime date, JsonGenerator gen, SerializerProvider provider) throws IOException {

        if (date.isBefore(LocalDateTime.now())) {
            // Apply special formatting for past dates
            String formattedDate = date.format(formatter);
            gen.writeString(formattedDate);
        } else {
            // Default formatting
            String formattedDate = date.format(formatter);
            gen.writeString(formattedDate);
        }
    }

}
