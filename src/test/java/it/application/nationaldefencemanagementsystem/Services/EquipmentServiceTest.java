package it.application.nationaldefencemanagementsystem.Services;


import it.application.nationaldefencemanagementsystem.Mappers.EquipmentMapper;
import it.application.nationaldefencemanagementsystem.Repositories.EquipmentRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentService service;


}
