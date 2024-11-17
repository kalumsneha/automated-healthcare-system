package com.titans.ahs.formatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateSerializer extends JsonSerializer<LocalDate> {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void serialize(LocalDate date, JsonGenerator gen, SerializerProvider provider) throws IOException {

        if (date.isBefore(LocalDate.now())) {
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
