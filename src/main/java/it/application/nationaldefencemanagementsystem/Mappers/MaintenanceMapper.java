package it.application.nationaldefencemanagementsystem.Mappers;

import it.application.nationaldefencemanagementsystem.DTOs.MaintenanceDto;
import it.application.nationaldefencemanagementsystem.Entities.Maintenance;
import org.springframework.stereotype.Component;

@Component
public class MaintenanceMapper {

     //Converte l'entity Maintenance nel DTO restituito dal service e dal controller.

    public MaintenanceDto toDto(Maintenance maintenance) {

        MaintenanceDto dto = new MaintenanceDto();

        dto.setId(maintenance.getId());
        dto.setDescription(maintenance.getDescription());
        dto.setStartDate(maintenance.getStartDate());
        dto.setEndDate(maintenance.getEndDate());
        dto.setEstimatedMaintenanceDays(
                maintenance.getEstimatedMaintenanceDays()
        );
        dto.setCost(maintenance.getCost());

        //Nell'entity è presente l'intero oggetto Vehicle.Nel DTO restituiamo soltanto il suo ID.

        if (maintenance.getVehicle() != null) {
            dto.setVehicleId(
                    maintenance.getVehicle().getId()
            );
        }

        //aggiungo equipaggiamento
        if (maintenance.getEquipment() != null) {
            dto.setEquipmentId(
                    maintenance.getEquipment().getId()
            );
        }

        return dto;
    }

    /*
    Converte il DTO in una nuova entity. Il veicolo non viene impostato qui: verrà recuperato tramite repository nel service.
     */
    public Maintenance toEntity(MaintenanceDto dto) {

        Maintenance maintenance = new Maintenance();

        maintenance.setDescription(dto.getDescription());
        maintenance.setStartDate(dto.getStartDate());
        maintenance.setEndDate(dto.getEndDate());
        maintenance.setEstimatedMaintenanceDays(
                dto.getEstimatedMaintenanceDays()
        );
        maintenance.setCost(dto.getCost());

        return maintenance;
    }
}