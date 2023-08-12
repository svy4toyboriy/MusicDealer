package musicDealer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class YouTube {

    private static final String API_KEY = "API_KEY";
    private static final String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";

    public static String[] audioId;
    public static String[] audioTitle;
    public static String[] audioUrl;

    public static void search(String query, int maxResults) throws IOException {

        audioId = new String[maxResults];
        audioTitle = new String[maxResults];
        audioUrl = new String[maxResults];

        URL url = new URL(SEARCH_URL + "?part=snippet&maxResults=" + maxResults
                + "&q=" + query + "&key=" + API_KEY);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine = in.lines().collect(Collectors.joining());

            StringBuilder title = new StringBuilder();
            int index = 0;
            for(int i = 0; i < maxResults; i++) {
                index = inputLine.indexOf("videoId", index) + 11;
                audioId[i] = inputLine.substring(index, index + 11);

                title.setLength(0);
                index = inputLine.indexOf("title", index) + 9;
                while (inputLine.charAt(index)!='"') {
                    title.append(inputLine.charAt(index));
                    index++;
                }
                audioTitle[i] = title.toString();
                audioUrl[i] = "https://youtu.be/" + audioId[i];
            }
            in.close();
        } else {
            System.err.println("Error searching videos, response code : " + responseCode);
        }
    }
}
