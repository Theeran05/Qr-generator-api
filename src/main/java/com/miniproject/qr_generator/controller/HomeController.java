package com.miniproject.qr_generator.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class HomeController {
	
	@GetMapping
	public String getHome()
	{
		return "Home controller for QR-generator";
	}

}
