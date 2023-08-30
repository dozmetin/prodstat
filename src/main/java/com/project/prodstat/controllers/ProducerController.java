package com.project.prodstat.controllers;


import com.project.prodstat.entities.Producer;
import com.project.prodstat.services.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/producers")
public class ProducerController {
    private final ProducerService producerService;


    @Autowired
    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping (path="/home2")
    public String home2(){
        return "Hello, Home!";
    }


    /**
     * Receives a POST mapping to save a producer instance into the database.
     * @param producer the lottery to save.
     * @return ResponseEntity with ok message and producer as its body if the producer was successfully saved.
     * Otherwise, return ResponseEntity with badRequest message and the error message as its body.
     */
    @PostMapping(path = "/producer")
    public @ResponseBody
    ResponseEntity<Producer> addNewProducer(
            @RequestBody Producer producer) {
        return ResponseEntity.ok(producerService.createProducer(producer));
    }
}
