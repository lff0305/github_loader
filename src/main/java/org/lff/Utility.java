package org.lff;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Base64;

public class Utility {

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public static String removeLeading(String str) {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if (c == '/' && first) {
                continue;
            }
            first = false;
            sb.append(c);
        }
        return sb.toString();
    }

    public static String removeTailing(String str) {
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        int i = str.length() - 1;
        for (; i >= 0; i--) {
            char c = str.charAt(i);
            if (c == '/' && first) {
                continue;
            }
            break;
        }
        for (int j=0; j<=i; j++) {
            sb.append(str.charAt(j));
        }
        return sb.toString();
    }

    public static String removeLeadingTailing(String str) {
        return removeLeading(removeTailing(str));
    }

    public static boolean isJsonObject(String body) {
        if (isEmpty(body)) {
            return false;
        }
        try {
            JSONObject o = new JSONObject(body);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isJsonArray(String body) {
        if (isEmpty(body)) {
            return false;
        }
        try {
            JSONArray o = new JSONArray(body);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void writeFile(String file, String content) {
        logger.info("Writting " + content.length() + " chars to " + file);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
