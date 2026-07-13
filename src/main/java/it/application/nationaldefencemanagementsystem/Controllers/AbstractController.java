package it.application.nationaldefencemanagementsystem.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.application.nationaldefencemanagementsystem.Services.ServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


public abstract class AbstractController<DTO>{

    @Autowired
    private ServiceDto<DTO> service;

    @Operation(summary = "Recupera tutti gli elementi",
            description = "Restituisce la lista degli oggetti gestiti dal controller")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Oggetti restituiti correttamente"),
            @ApiResponse(responseCode = "500", description = "Errore interno del server" )

    })
    @GetMapping("/getall")
    public Iterable<DTO> getAll(){
        return service.getAll();
    }


    @Operation(summary = "cancella l' oggetto in memoria con id selezionato",
               description = "restituisce un oggetto cancellato")
    @DeleteMapping("/delete")
    public void delete(@RequestParam("id") Integer id) {
        service.delete(id);
    }


    @Operation(summary = "Modifica l' elemento del dto inserito",
            description = "Restituisce un oggetto aggiornato")
    @PutMapping("/update")
    public DTO update(@RequestBody DTO dto){
        service.update(dto);
        return dto;
    }

    @Operation(summary = "Inserisce un nuovo elemento",
            description = "Restituisce l'oggetto inserito")
    @PostMapping("/insert")
    public DTO insert (@RequestBody DTO dto) {
        service.insert(dto);
        return dto;
    }

    @Operation(summary = "Recupera un elemento specifico",
            description = "Restituisce l'oggetto con l'ID indicato")
    @GetMapping("/read")
    public DTO read(@RequestParam("id") Integer id) {
        return service.read(id);
    }

}
