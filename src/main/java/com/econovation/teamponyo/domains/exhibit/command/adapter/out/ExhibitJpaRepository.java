package com.econovation.teamponyo.domains.exhibit.command.adapter.out;

import com.econovation.teamponyo.domains.exhibit.domain.model.Exhibit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitJpaRepository extends JpaRepository<Exhibit, Long> {

}
