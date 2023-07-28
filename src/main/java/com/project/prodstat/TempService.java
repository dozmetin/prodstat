package com.project.prodstat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
public class TempService {
    WebClient spotifyWebCl;

    public TempService(WebClient spotifyWebCl) {
        this.spotifyWebCl = spotifyWebCl;
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
        return jsonResponse;
        //return "ok";
        //.map(this::mapToTrack);
    }

}
