package com.project.prodstat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {

    @Autowired
    transient TempService tempService;

    @GetMapping("/search-track")
    public String searchTrack(@RequestParam String trackName, @RequestParam String artistName) {
        return tempService.searchTrack(trackName, artistName);
    }
}
