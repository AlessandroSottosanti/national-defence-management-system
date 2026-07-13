package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.BaseDto;
import it.application.nationaldefencemanagementsystem.Services.BaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bases")
public class BaseController extends AbstractController<BaseDto> {

    private final BaseService service;

    public BaseController(BaseService service) {
        this.service = service;
    }

    @GetMapping("/city")
    public List<BaseDto> findByCity(
            @RequestParam String city
    ) {
        return service.findByCity(city);
    }

    @GetMapping("/armed-force")
    public List<BaseDto> findByArmedForce(
            @RequestParam Integer armedForceId
    ) {
        return service.findByArmedForce(armedForceId);
    }

    @GetMapping("/search")
    public List<BaseDto> searchByName(
            @RequestParam String name
    ) {
        return service.searchByName(name);
    }
}