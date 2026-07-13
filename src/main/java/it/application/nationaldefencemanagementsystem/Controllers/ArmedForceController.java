package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.ArmedForceDto;
import it.application.nationaldefencemanagementsystem.Services.ArmedForceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/armed-forces")
public class ArmedForceController extends AbstractController<ArmedForceDto> {

    private final ArmedForceService service;

    public ArmedForceController(ArmedForceService service) {
        this.service = service;
    }

    @GetMapping("/name")
    public ArmedForceDto findByName(
            @RequestParam String name
    ) {
        return service.findByName(name);
    }
}