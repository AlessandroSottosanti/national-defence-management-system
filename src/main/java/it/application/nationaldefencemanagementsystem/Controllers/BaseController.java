package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.BaseDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.BaseFilterDto;
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

            @RequestParam(required = false) String name,

            @RequestParam(required = false) String city,

            @RequestParam(required = false) String address,

            @RequestParam(required = false) Integer armedForceId

    ) {

        BaseFilterDto filter = new BaseFilterDto();

        filter.setName(name);
        filter.setCity(city);
        filter.setAddress(address);
        filter.setArmedForceId(armedForceId);

        return service.index(filter);
    }
}