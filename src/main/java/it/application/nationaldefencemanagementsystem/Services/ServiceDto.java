package it.application.nationaldefencemanagementsystem.Services;

public interface ServiceDto<DTO> {

    public DTO read(Integer id);

    public DTO insert (DTO dto);

    public DTO update (DTO dto);

    public void delete (Integer id);
}