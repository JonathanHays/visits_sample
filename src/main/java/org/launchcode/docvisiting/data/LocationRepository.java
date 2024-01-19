package org.launchcode.docvisiting.data;

import org.launchcode.docvisiting.models.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends CrudRepository<Location, Integer> {

    List<Location> findByNameContainingOrAbbreviatedNameContainingIgnoreCaseOrderByNameAsc(String term, String term2);

    Page<Location> findByNameContainingOrAbbreviatedNameContainingIgnoreCaseOrderByNameAsc(String term, String term2, PageRequest pageRequest);

    List<Location> findByIsActiveOrderByNameAsc(boolean isActive);

    Page<Location> findAll(Pageable pageable);

}
