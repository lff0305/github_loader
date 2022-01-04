package org.lff;

import java.util.Base64;

public class Utility {
    public static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
