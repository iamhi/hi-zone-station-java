package com.github.iamhi.hizone.station.gateway.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class UiWelcomeController {

    @GetMapping
    public String index(Model model) {
        return "index";
    }
}
