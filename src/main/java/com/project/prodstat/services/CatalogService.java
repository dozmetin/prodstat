package com.project.prodstat.services;


import com.google.gson.*;
import com.project.prodstat.DTOs.CatalogMetrics;
import com.project.prodstat.entities.Catalog;
import com.project.prodstat.entities.Producer;
import com.project.prodstat.entities.Track;
import com.project.prodstat.exceptions.SpotifyException;
import com.project.prodstat.exceptions.YoutubeException;
import com.project.prodstat.repositories.CatalogRepository;
import com.project.prodstat.repositories.ProducerRepository;
import com.project.prodstat.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Set;

@Service
public class CatalogService {


    private final CatalogRepository catalogRepository;
    private final TrackRepository trackRepository;

    private final ProducerRepository producerRepository;

    WebClient spotifyWebCl;
    WebClient youtubeWebCl;

    @Value("${youtube.apiKey}")
    private String youtubeApiKey;

    @Autowired
    public CatalogService(CatalogRepository catalogRepository, TrackRepository trackRepository,
                          ProducerRepository producerRepository, WebClient spotifyWebCl) {
        this.trackRepository = trackRepository;
        this.catalogRepository = catalogRepository;
        this.producerRepository = producerRepository;
        this.spotifyWebCl = spotifyWebCl;
        youtubeWebCl = WebClient.builder().baseUrl("https://www.googleapis.com/youtube/v3").build();
    }

    public Track addTrack(Long userId, String trackName, String artistName) throws SpotifyException, YoutubeException {
        Gson gson = new Gson();
        Track track = new Track();

        int spotifyPopularity = getSpotifyPopularity(trackName, artistName, gson);
        long youtubeViews = getYoutubeViews(trackName, artistName, gson);

        if((spotifyPopularity == -1) && (youtubeViews == -1)){
            throw new RuntimeException("The track you searched for does not exist on Spotify and Youtube");
        }
        else{
            trackRepository.save(track);
            if (producerRepository.findById(userId).isPresent()){
                Producer producer = producerRepository.findById(userId).get();
                Catalog catalog = catalogRepository.findById(producer.getCatalog_id()).get();
                track.setYoutubeViewCount(youtubeViews);
                track.setSpotifyPopularity(spotifyPopularity);
                track.setTrackName(trackName);
                track.setArtistName(artistName);
                catalog.getTracks().add(track);
                catalogRepository.save(catalog);
                producerRepository.save(producer);
            }
            else {
                throw new RuntimeException("Catalog Does Not Exist!");
            }
        }


        return track;

    }

    public int getSpotifyPopularity(String trackName, String artistName, Gson gson) throws SpotifyException {
        String jsonResponse = spotifyWebCl.get()
                .uri(uriBuilder -> uriBuilder.path("/search")
                        .queryParam("q", "{track} artist:{artist}")
                        .queryParam("type", "track")
                        .queryParam("limit", 1)
                        .build(trackName, artistName))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        int popularity = -1; // Default value if not found
        if (jsonResponse == null)
            throw new SpotifyException("Track not Found in Spotify");
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

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
        return popularity;
    }

    public long getYoutubeViews(String trackName, String artistName, Gson gson) throws YoutubeException {
        String jsonResponse = youtubeWebCl.get()
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
        long youtubeViews = -1; // Default value if not found
        if (jsonResponse == null)
            throw new YoutubeException("Track not Found in Youtube");
        String vidId = "empty";
        JsonObject jsonObject2 = JsonParser.parseString(jsonResponse).getAsJsonObject();
        JsonArray itemsArray = jsonObject2.getAsJsonArray("items");

        if (itemsArray != null && itemsArray.size() > 0) {
            JsonObject firstItem = itemsArray.get(0).getAsJsonObject();
            JsonObject idObject = firstItem.getAsJsonObject("id");
            if (idObject != null && idObject.has("videoId")) {
                vidId = idObject.get("videoId").getAsString();
                System.out.println(vidId);
            }
        }
        youtubeViews = searchYoutubeVideobyId(vidId);

        return youtubeViews;
    }


    private long searchYoutubeVideobyId(String videoId) throws YoutubeException {
        String jsonResponse = youtubeWebCl.get()
                .uri(uriBuilder -> uriBuilder.path("/videos")
                        .queryParam("id", videoId)
                        .queryParam("part", "statistics")
                        .queryParam("key", youtubeApiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (jsonResponse == null)
            throw new YoutubeException("Track not Found in Youtube");

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


    /**
     * Saves a catalog into the database.
     * @param catalog the producer to save.
     * @return the catalog.
     */
    public Catalog save(Catalog catalog) {
        return catalogRepository.save(catalog);
    }



    public CatalogMetrics calculateCatalogMetrics(Long catalogId) {
        Catalog catalog = catalogRepository.findById(catalogId)
                .orElseThrow(() -> new RuntimeException("Catalog not found"));
        Set<Track> tracks = catalog.getTracks();
        CatalogMetrics catalogMetrics = new CatalogMetrics();

        if (!tracks.isEmpty()) {
            double totalPopularity = 0;
            long totalViews = 0;
            int catalogSize = 0;

            for (Track track : tracks) {
                int spotiPopularity = track.getSpotifyPopularity();
                long youtubeViews = track.getYoutubeViewCount();
                if(spotiPopularity != -1){
                    totalPopularity += spotiPopularity;
                    catalogSize++;
                }
                if(youtubeViews != -1){
                    totalViews += youtubeViews;
                }
            }

            catalogMetrics.setAverageSpotifyPopularity(totalPopularity / catalogSize);
            catalogMetrics.setTotalYoutubeViews(totalViews);
        }

        return catalogMetrics;
    }


}
