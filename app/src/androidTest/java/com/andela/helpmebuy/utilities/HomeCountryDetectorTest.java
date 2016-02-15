package com.andela.helpmebuy.utilities;

import static org.junit.Assert.*;
import com.andela.helpmebuy.activities.SigninActivity;

/**
 * Created by emkarumbi on 15/02/2016.
 */
public class HomeCountryDetectorTest {

    @org.junit.Test
    public void testGetCountry() throws Exception {
        String samplestr = "Kindaruma Rd, Nairobi, Kenya";
        String country = new HomeCountryDetector(new SigninActivity()).getCountry(samplestr);
        assertTrue(country == "Kenya");
    }
}