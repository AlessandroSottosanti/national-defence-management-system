package it.application.nationaldefencemanagementsystem.Controllers;

import it.application.nationaldefencemanagementsystem.DTOs.DocumentsDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.DocumentFilterDto;
import it.application.nationaldefencemanagementsystem.Services.DocumentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController extends AbstractController<DocumentsDto>{

    private final DocumentService service;
    public DocumentController(DocumentService service){this.service = service;}

    @GetMapping
    public List<DocumentsDto> index(
            @ModelAttribute DocumentFilterDto filter
    ){
        return service.index(filter);
    }

}
