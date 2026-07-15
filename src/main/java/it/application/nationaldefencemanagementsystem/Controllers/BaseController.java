package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.BaseDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.BaseFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.OperatorFilterDto;
import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
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

    @GetMapping
    public List<BaseDto> index(
            @ModelAttribute BaseFilterDto filter
    ) {
        return service.index(filter);
    }
}