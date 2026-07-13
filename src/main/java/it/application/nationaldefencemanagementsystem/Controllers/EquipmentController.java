package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.EquipmentDto;
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

    @GetMapping("/status")
    public List<EquipmentDto> findByStatus(@RequestParam EquipmentStatus status){
        return service.findByStatus(status);
    }

    @GetMapping("/condition")
    public List<EquipmentDto> findByCondition(@RequestParam EquipmentCondition condition) {
        return service.findByCondition(condition);
    }

    @GetMapping("/search")
    public List<EquipmentDto> findByStatusAndCondition(
            @RequestParam EquipmentStatus status,
            @RequestParam EquipmentCondition condition) {
        return service.findByStatusAndCondition(status, condition);
    }

    @GetMapping("/firearms")
    public List<EquipmentDto> findFireArms() {
        return service.findFireArms();
    }

    @GetMapping("/firearms/low-ammo")
    public List<EquipmentDto> findFireArmsNeedingAmmunition(@RequestParam Integer threshold) {
        return service.findFireArmsNeedingAmmunition(threshold);
    }

    @GetMapping("/operator/{operatorId}")
    public List<EquipmentDto> findByOperatorId(@PathVariable Integer operatorId) {
        return service.findByOperatorId(operatorId);
    }

    @GetMapping("/unassigned")
    public List<EquipmentDto> findByOperatorIsNull() {
        return service.findByOperatorIsNull();
    }

    @GetMapping("/assigned")
    public List<EquipmentDto> findByOperatorIdIsNotNull() {
        return service.findByOperatorIdIsNotNull();
    }


    @GetMapping("/count/status")
    public long countByStatus(@RequestParam EquipmentStatus status) {
        return service.countByStatus(status);
    }


    @GetMapping("/count/condition")
    public long countByCondition(@RequestParam EquipmentCondition condition) {
        return service.countByCondition(condition);
    }

}
