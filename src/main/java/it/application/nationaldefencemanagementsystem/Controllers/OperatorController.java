package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.application.nationaldefencemanagementsystem.DTOs.OperatorDto;
import it.application.nationaldefencemanagementsystem.DTOs.FilterDTOs.OperatorFilterDto;
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

    @Operation(summary = "Recupera tutti gli elementi",
            description = "Restituisce la lista degli oggetti gestiti dal controller")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Oggetti restituiti correttamente"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    @GetMapping
    public List<OperatorDto> index(
            @ModelAttribute OperatorFilterDto filter
    ) {
        return service.index(filter);
    }
}