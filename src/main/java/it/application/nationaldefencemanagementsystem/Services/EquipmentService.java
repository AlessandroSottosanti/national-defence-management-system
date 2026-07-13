package it.application.nationaldefencemanagementsystem.Services;

import it.application.nationaldefencemanagementsystem.DTOs.EquipmentDto;
import it.application.nationaldefencemanagementsystem.Entities.Equipment;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentCondition;
import it.application.nationaldefencemanagementsystem.Entities.EquipmentStatus;
import it.application.nationaldefencemanagementsystem.Mappers.EquipmentMapper;
import it.application.nationaldefencemanagementsystem.Repositories.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService extends AbstractService<Equipment, EquipmentDto>{

    private final EquipmentRepository repository;

    public EquipmentService(EquipmentRepository repository, EquipmentMapper mapper){
        super(repository,mapper);
        this.repository = repository;
    }

    public List<EquipmentDto> findByStatus(EquipmentStatus status) {
        return converter.toDTOList(repository.findByStatus(status));
    }

    public List<EquipmentDto> findByCondition(EquipmentCondition condition) {
        return converter.toDTOList(repository.findByCondition(condition));
    }

    public List<EquipmentDto> findByStatusAndCondition(EquipmentStatus status, EquipmentCondition condition) {
        return converter.toDTOList(repository.findByStatusAndCondition(status, condition));
    }

    public List<EquipmentDto> findFireArms() {
        return converter.toDTOList(repository.findByFireArmTrue());
    }

    public List<EquipmentDto> findFireArmsNeedingAmmunition(Integer threshold) {
        return converter.toDTOList(repository.findByFireArmTrueAndAmmunitionCountLessThan(threshold));
    }

    public List<EquipmentDto> findByOperatorId(Integer operatorId) {
        return converter.toDTOList(repository.findByOperatorId(operatorId));
    }

    public long countByStatus(EquipmentStatus status) {
        return repository.countByStatus(status);
    }

    public long countByCondition(EquipmentCondition condition) {
        return repository.countByCondition(condition);
    }

    public List<EquipmentDto> findByOperatorIsNull(){
        return converter.toDTOList(repository.findByOperatorIdIsNull());
    }

    public List<EquipmentDto> findByOperatorIdIsNotNull(){
        return converter.toDTOList(repository.findByOperatorIdIsNotNull());
    }

}
