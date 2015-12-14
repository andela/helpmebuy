package com.andela.helpmebuy.serializers;

import com.andela.helpmebuy.models.City;
import com.andela.helpmebuy.models.Country;
import com.andela.helpmebuy.models.Location;
import com.andela.helpmebuy.models.Region;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Created by Oluwatosin on 11/25/2015.
 */
public class LocationDeserializer extends JsonDeserializer<Location> {
    @Override
    public Location deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String str = jp.getText();
        String[] parts = str.split(";");
        Country country = stringToCountry(parts[0]);
        Region region = stringToRegion(parts[1]);
        City city = cityToCity(parts[2]);
        Location location = new Location(country, region, city);

        return location;
    }


    private Country stringToCountry(String countryString) {
        String[] countryParts = countryString.split(":");
        Country country = new Country();
        country.setId(countryParts[0]);
        country.setName(countryParts[1]);
        return country;
    }

    private Region stringToRegion(String regionString) {
        String[] regionParts = regionString.split(":");
        Region region = new Region();
        region.setId(regionParts[0]);
        region.setName(regionParts[1]);
        return region;
    }

    private City cityToCity(String cityString) {
        String[] cityParts = cityString.split(":");
        City city = new City();
        city.setId(cityParts[0]);
        city.setName(cityParts[1]);
        return city;
    }
}
