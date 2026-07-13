package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.Entities.OperatorStatus;
import it.application.nationaldefencemanagementsystem.Services.OperatorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operators")
public class OperatorController extends AbstractController<OperatorDto> {

    private final OperatorService service;

    public OperatorController(OperatorService service) {
        this.service = service;
    }

    @GetMapping("/service-number")
    public OperatorDto findByServiceNumber(
            @RequestParam String serviceNumber
    ) {
        return service.findByServiceNumber(serviceNumber);
    }

    @GetMapping("/status")
    public List<OperatorDto> findByStatus(
            @RequestParam OperatorStatus status
    ) {
        return service.findByStatus(status);
    }

    @GetMapping("/base")
    public List<OperatorDto> findByBase(
            @RequestParam Integer baseId
    ) {
        return service.findByBase(baseId);
    }

    @GetMapping("/search")
    public List<OperatorDto> searchByLastName(
            @RequestParam String lastName
    ) {
        return service.searchByLastName(lastName);
    }
}