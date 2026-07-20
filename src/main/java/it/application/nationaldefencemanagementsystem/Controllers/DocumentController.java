package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Documents", description = "Document management: search, archive, and retrieve official files")
public class DocumentController extends AbstractController<DocumentsDto> {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Filtered document search", description = "Returns the list of documents. Uses query parameters to filter by title, operator name, vehicle ID, etc." +
            "Use at least one parameter.")
    public List<DocumentsDto> index(
            @ModelAttribute DocumentFilterDto filter
    ) {
        return service.index(filter);
    }

}
