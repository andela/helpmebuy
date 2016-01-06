package com.andela.helpmebuy.serializers;

import com.andela.helpmebuy.models.Location;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by Oluwatosin on 11/25/2015.
 */
public class LocationSerializer extends JsonSerializer<Location> {
    @Override
    public void serialize(Location value, JsonGenerator generator, SerializerProvider provider) throws IOException, JsonProcessingException {
        generator.writeString(LocationToString(value));
    }

    private String LocationToString(Location location){
       return  location.getCountry().getId() + ":" + location.getCountry().getName() +
                ";" + location.getRegion().getId() + ":" + location.getRegion().getName() +
                ";" + location.getCity().getId() + ":" + location.getCity().getName();

    }
}
