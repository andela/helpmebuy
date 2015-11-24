package com.andela.helpmebuy.utilities;


public class Utils {

    public static int getHourIn12HoursFormat(int hourOfDay) {
        return hourOfDay > 12 ? hourOfDay % 12 : hourOfDay;
    }
}
