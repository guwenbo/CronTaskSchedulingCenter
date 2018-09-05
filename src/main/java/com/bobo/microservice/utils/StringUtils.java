package com.bobo.microservice.utils;

public class StringUtils {

    /**
     * Checks if a CharSequence is empty ("") or null
     *
     * @param cs
     * @return
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }


    /**
     * Checks if a CharSequence is not empty ("") and not null
     *
     * @param cs
     * @return
     */
    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

}
