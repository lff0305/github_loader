package org.lff;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;

public class Utility {

    public static final String MASTER = "master";

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

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

    public static void writeFile(String file, byte[] content) {
        logger.info("Writting " + content.length + " bytes to " + file);
        try {
            OutputStream bw = (new FileOutputStream(file));
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean mkdir(String dir) {
        logger.info("Making dir " + dir);
        try {
            File f = new File(dir);
            if (f.exists()) {
                logger.info(dir + " already exists and it is a directory ? " + f.isDirectory());
                return f.isDirectory();
            }
            boolean result = f.mkdir();
            logger.info("Create dir " + dir + " = " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFileMeta(Config config) {

        String tenant = config.getTenant();
        logger.info("Tenant = " + tenant);
        String path = config.getPath();
        logger.info("Path = " + path);
        String file = config.getFile();
        logger.info("File = " + file);

        String url = config.getEndpoint() + "/repos/" + tenant + "/contents/" + path + "/" + file + "?ref=" + config.getBranch();
        logger.info("url = " + url);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("Accept", "application/vnd.github.v3+json")
                .setHeader("User-Agent", "Java 11 HttpClient Bot");
        if (config.isAuth()) {
            builder.header("Authorization", Utility.basicAuth("user", config.getToken()));
        }
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            logger.info("Response is " + response.statusCode() + " " + response.body());
            String body = response.body();
            return body;
        } catch (IOException e) {
            logger.error("Failed to load config", e);
            if (response != null) {
                logger.error("Response is " + response.statusCode() + " / " + response.body());
            } else {
                logger.error("Response is null");
            }
            return null;
        } catch (InterruptedException e) {
            return null;
        } finally {

        }
    }

    public static byte[] download(String url, String token) {
        logger.info("Downloading " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Authorization", Utility.basicAuth("user", token))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .build();
        HttpResponse<byte[]> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            logger.info("Response is " + response.statusCode());
            return response.body();
        } catch (IOException e) {
            logger.error("Failed to load config", e);
            if (response != null) {
                logger.error("Response is " + response.statusCode() + " / " + response.body());
            } else {
                logger.error("Response is null");
            }
        } catch (InterruptedException e) {
            return null;
        } finally {
        }
        return null;
    }
}
