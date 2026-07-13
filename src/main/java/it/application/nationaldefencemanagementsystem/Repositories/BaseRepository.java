package it.application.nationaldefencemanagementsystem.Repositories;

import it.application.nationaldefencemanagementsystem.Entities.Base;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseRepository extends JpaRepository<Base, Integer> {

    List<Base> findByCity(String city);

    List<Base> findByArmedForceId(Integer armedForceId);

    List<Base> findByNameContainingIgnoreCase(String name);
}