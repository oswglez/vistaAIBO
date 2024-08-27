package com.expectra.roombooking.controller;

import com.expectra.roombooking.model.Hotel;
import com.expectra.roombooking.model.Room;
import com.expectra.roombooking.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class HomeController {
    private final HotelRepository hotelRepository;
    @Value("${spring.application.name}")
    String appName;

    public HomeController(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @RequestMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "esigned-html/index";
    }
}