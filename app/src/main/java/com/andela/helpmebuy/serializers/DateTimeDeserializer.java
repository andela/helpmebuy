package com.andela.helpmebuy.serializers;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import org.joda.time.DateTime;

import java.io.IOException;

public class DateTimeDeserializer extends JsonDeserializer<DateTime> {

    @Override
    public DateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonParseException {
        return DateTime.parse(parser.getText());
    }
}
