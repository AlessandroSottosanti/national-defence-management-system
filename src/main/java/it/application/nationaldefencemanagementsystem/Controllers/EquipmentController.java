package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.EquipmentDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.EquipmentFilterDto;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentCondition;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentStatus;
import it.application.nationaldefencemanagementsystem.Services.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController extends AbstractController<EquipmentDto>{

    private final EquipmentService service;
    public EquipmentController(EquipmentService service){this.service = service;}

    @GetMapping
    public List<EquipmentDto> index(
            @ModelAttribute EquipmentFilterDto filter
    ){
        return service.index(filter);
    }

}
