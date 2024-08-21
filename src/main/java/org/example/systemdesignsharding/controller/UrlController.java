package org.example.systemdesignsharding.controller;

import lombok.extern.log4j.Log4j2;
import org.example.systemdesignsharding.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class UrlController {

    @Autowired
    UrlService urlService;

    @GetMapping("/test")
    public void test(@RequestParam("number") int number) {
        urlService.testSharding(number);
        log.info("in controller");
    }
}

