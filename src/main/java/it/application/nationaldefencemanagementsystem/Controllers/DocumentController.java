package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.DocumentsDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.DocumentFilterDto;
import it.application.nationaldefencemanagementsystem.Services.DocumentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController extends AbstractController<DocumentsDto> {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping
    public List<DocumentsDto> index(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String filePath,
            @RequestParam(required = false) Integer operatorId,
            @RequestParam(required = false) Integer vehicleId
    ){

        DocumentFilterDto filter = new DocumentFilterDto();
        filter.setId(id);
        filter.setTitle(title);
        filter.setFilePath(filePath);
        filter.setOperatorId(operatorId);
        filter.setVehicleId(vehicleId);

        return service.index(filter);
    }
}