package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.OperatorFilterDto;
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

    @GetMapping
    public List<OperatorDto> index(
            @RequestParam(required = false) String serviceNumber,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String rank,
            @RequestParam(required = false) Integer baseId,
            @RequestParam(required = false) OperatorStatus status
    ) {

        OperatorFilterDto filter = new OperatorFilterDto();

        filter.setServiceNumber(serviceNumber);
        filter.setRank(rank);
        filter.setFirstName(firstName);
        filter.setLastName(lastName);
        filter.setBaseId(baseId);
        filter.setStatus(status);

        return service.index(filter);
    }
}