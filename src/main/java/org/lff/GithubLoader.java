package org.lff;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.apache.commons.cli.*;
import org.json.JSONObject;

public class GithubLoader {


    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        Options options = new Options();
        Option opt = new Option(null, "url", true, "url of github api");
        opt.setRequired(true);
        options.addOption(opt);

        Option token = new Option(null, "token", true, "token of github api");
        token.setRequired(false);
        options.addOption(token);

        Option dir = new Option("d", "dir", true, "dir to load");
        dir.setRequired(false);
        options.addOption(dir);

        Option branch = new Option("b", "branch", true, "branch");
        branch.setRequired(false);
        options.addOption(branch);

        Option file = new Option("f", "file", true, "single file to load");
        file.setRequired(false);
        options.addOption(file);

        Option output = new Option(null, "output", true, "output directory");
        output.setRequired(true);
        options.addOption(output);

        Option create = new Option(null, "create", false, "create output directory if not exists");
        create.setRequired(false);
        options.addOption(create);

        CommandLine commandLine = null;
        DefaultParser parser = new DefaultParser();
        try {
            commandLine = parser.parse(options, args);
            Config config = Config.build(commandLine);
            process(config);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void process(Config config) {
        String body = processFile(config);
        if (Utility.isJsonArray(body)) {

        }
        if (Utility.isJsonObject(body)) {
            JSONObject o = new JSONObject(body);
            String download_url = o.getString("download_url");
            String content = download(download_url, config.getToken());
            if (content == null) {
                logger.error("Failed to download from " + download_url);
                System.exit(1);
            }
            Utility.writeFile(config.getOutput() + "/" + config.getFile(), content);
        }
    }

    private static String download(String url, String token) {
        logger.info("Downloading " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .header("Authorization", Utility.basicAuth("user", token))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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

    private static String processFile(Config config) {

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
                .setHeader("User-Agent", "Java 11 HttpClient Bot");
        if (config.isAuth()) {
            builder.header("Authorization", Utility.basicAuth("user", config.getToken()));
        }
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            logger.info("Response is " + response.statusCode());
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



    public String loadDir(String repo, String owner, String path) {
        String url = String.format("", repo, owner, path);
        return null;
    }

}
