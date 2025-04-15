package com.expectra.roombooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@CrossOrigin(origins = "*")
public class HomeController {
    @Value("${spring.application.name}")
    String appName;


    @RequestMapping
    @Operation(summary = "Home Page", description = "Posible pagina de inicio")
    public String homePage(final Model model) {
        model.addAttribute("appName", appName);
        return "designed-html/index";
    }
}