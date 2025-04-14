package gov.epa.ccte.api.physchem.repository;

import gov.epa.ccte.api.physchem.domain.PhyschemPredicted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PhyschemPredictedRepository extends JpaRepository<PhyschemPredicted, Integer>{

    List<PhyschemPredicted> findByDtxsidAndProperty(@Param("dtxsid") String dtxsid, @Param("property") String property);

    List<PhyschemPredicted> findByDtxsidInOrderByDtxsidAscPropertyAsc(List<String> dtxsids);

}