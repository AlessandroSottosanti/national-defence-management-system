package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper extends AbstractConverter<Vehicle,VehicleDto> {

    // converto entity veicolo in un veicolo dto per recuperare metodo dal db per poi darlo ai controller (o anche Frontend).
    @Override
    public VehicleDto toDTO(Vehicle vehicle) {

        //questo è un dto vuoto
        VehicleDto dto = new VehicleDto();

        //qua invece copio nel dto i campi dell'entity
        dto.setId(vehicle.getId());
        dto.setMatricola(vehicle.getMatricola());
        dto.setModello(vehicle.getModello());
        dto.setStato(vehicle.getStato());

        //qua evito un null; poi visto che nell'entity c'è tutto l'oggetto di categoria veicolo,
        //nel dto dò soltanto l'id
        if (vehicle.getCategory() != null) {
            dto.setVehicleCategoryId(
                    vehicle.getCategory().getId()
            );
        }

        if (vehicle.getBase() != null) {
            dto.setBaseId(
                    vehicle.getBase().getId()
            );
        }

        return dto;
    }

    @Override
    public Vehicle toEntity(VehicleDto dto) {

        Vehicle vehicle = new Vehicle();

        vehicle.setModello(dto.getModello());
        vehicle.setStato(dto.getStato());

        return vehicle;
    }

}