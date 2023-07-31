package com.project.prodstat;

import com.google.gson.*;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class TempService {
    WebClient spotifyWebCl;
    WebClient youtubeWebCl;

    @Value("${youtube.apiKey}")
    private String youtubeApiKey;

    public TempService(WebClient spotifyWebCl) {
        this.spotifyWebCl = spotifyWebCl;
        youtubeWebCl = WebClient.builder().baseUrl("https://www.googleapis.com/youtube/v3").build();
    }

    public String searchTrack(String trackName, String artistName) {

        String jsonResponse = spotifyWebCl.get()
                .uri(uriBuilder -> uriBuilder.path("/search")
                        .queryParam("q", "{track} artist:{artist}")
                        .queryParam("type", "track")
                        .queryParam("limit", 1)
                        .build(trackName, artistName))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        Gson gson = new Gson();
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        // Extract the popularity metric
        int popularity = -1; // Default value if not found
        if (jsonObject.has("tracks")) {
            JsonObject tracks = jsonObject.getAsJsonObject("tracks");
            if (tracks.has("items") && tracks.get("items").isJsonArray()) {
                JsonArray items = tracks.getAsJsonArray("items");
                if (items.size() > 0) {
                    JsonObject firstTrack = items.get(0).getAsJsonObject();
                    if (firstTrack.has("popularity")) {
                        popularity = firstTrack.get("popularity").getAsInt();
                        System.out.println("Popularity: " + popularity);
                    }
                }
            }
        }

        String jsonResponse2 = youtubeWebCl.get()
                .uri(uriBuilder -> uriBuilder.path("/search")
                        .queryParam("q", "{track} {artist}")
                        .queryParam("type", "video")
                        .queryParam("maxResults", 1)
                        .queryParam("part", "snippet")
                        .queryParam("key", youtubeApiKey) // Replace with your YouTube API key
                        .build(trackName, artistName))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        String vidId = "empty";
        JsonObject jsonObject2 = JsonParser.parseString(jsonResponse2).getAsJsonObject();
        JsonArray itemsArray = jsonObject2.getAsJsonArray("items");

        if (itemsArray != null && itemsArray.size() > 0) {
            JsonObject firstItem = itemsArray.get(0).getAsJsonObject();
            JsonObject idObject = firstItem.getAsJsonObject("id");
            if (idObject != null && idObject.has("videoId")) {
                vidId = idObject.get("videoId").getAsString();
                System.out.println(vidId);
            }
        }

        System.out.println(getVideoViewCountFromYouTube(vidId));

        return jsonResponse2;
        //return "ok";
        //.map(this::mapToTrack);
    }

    private long getVideoViewCountFromYouTube(String videoId) {
        String jsonResponse = youtubeWebCl.get()
                .uri(uriBuilder -> uriBuilder.path("/videos")
                        .queryParam("id", videoId)
                        .queryParam("part", "statistics")
                        .queryParam("key", youtubeApiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray itemsArray = jsonObject.getAsJsonArray("items");

            if (itemsArray != null && itemsArray.size() > 0) {
                JsonObject firstItem = itemsArray.get(0).getAsJsonObject();
                JsonObject statisticsObject = firstItem.getAsJsonObject("statistics");
                if (statisticsObject != null && statisticsObject.has("viewCount")) {
                    return statisticsObject.get("viewCount").getAsLong();
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return -1; // View count not found in the response or an error occurred during parsing
    }

}
