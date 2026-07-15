package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.VehicleDto;
import it.application.nationaldefencemanagementsystem.Entities.Base;
import it.application.nationaldefencemanagementsystem.Entities.Vehicle;
import it.application.nationaldefencemanagementsystem.Entities.VehicleCategory;
import it.application.nationaldefencemanagementsystem.Mappers.VehicleMapper;
import it.application.nationaldefencemanagementsystem.Repositories.BaseRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleCategoryRepository;
import it.application.nationaldefencemanagementsystem.Repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleCategoryRepository vehicleCategoryRepository;
    private final BaseRepository baseRepository;
    private final VehicleMapper vehicleMapper;

    public List<VehicleDto> findAll() {

        return vehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::toDto)
                .toList();
    }

    public VehicleDto findById(Integer id) {

        Vehicle vehicle = getVehicleById(id);

        return vehicleMapper.toDto(vehicle);
    }

    public VehicleDto create(VehicleDto dto) {

        VehicleCategory vehicleCategory =
                getVehicleCategoryById(dto.getVehicleCategoryId());

        Base base = getBaseById(dto.getBaseId());

        Vehicle vehicle = vehicleMapper.toEntity(dto);

        vehicle.setMatricola(UUID.randomUUID());
        vehicle.setCategory(vehicleCategory);
        vehicle.setBase(base);

        Vehicle savedVehicle =
                vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(savedVehicle);
    }

    public VehicleDto update(
            Integer id,
            VehicleDto dto
    ) {

        Vehicle vehicle = getVehicleById(id);

        VehicleCategory vehicleCategory =
                getVehicleCategoryById(dto.getVehicleCategoryId());

        Base base = getBaseById(dto.getBaseId());

        vehicle.setModello(dto.getModello());
        vehicle.setStato(dto.getStato());
        vehicle.setCategory(vehicleCategory);
        vehicle.setBase(base);

        Vehicle updatedVehicle =
                vehicleRepository.save(vehicle);

        return vehicleMapper.toDto(updatedVehicle);
    }

    public void delete(Integer id) {

        Vehicle vehicle = getVehicleById(id);

        vehicleRepository.delete(vehicle);
    }

    private Vehicle getVehicleById(Integer id) {

        return vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Veicolo non trovato"
                        )
                );
    }

    private VehicleCategory getVehicleCategoryById(
            Integer id
    ) {

        return vehicleCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria del mezzo non trovata"
                        )
                );
    }

    private Base getBaseById(Integer id) {

        return baseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Base non trovata"
                        )
                );
    }
}