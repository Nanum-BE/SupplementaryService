package com.nanum.intro.presemtation;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class IntroController {

    private final MessageSource messageSource;
    @GetMapping
    public String helloInternationalized(@RequestHeader(name = "Accept-Language", required = false)Locale locale){
        return messageSource.getMessage("greeting.message",null,locale);
    }
}
