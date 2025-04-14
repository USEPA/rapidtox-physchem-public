package gov.epa.ccte.api.physchem.repository;
import gov.epa.ccte.api.physchem.domain.PhyschemExperimental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public interface PhyschemExperimentalRepository extends JpaRepository<PhyschemExperimental, Integer> {

    List<PhyschemExperimental> findByDtxsidAndProperty(@Param("dtxsid") String dtxsid, @Param("property") String property);

    List<PhyschemExperimental> findByDtxsidInAndPropertyEquals(List<String> dtxsids, String property);

    List<PhyschemExperimental> findByDtxsidInOrderByDtxsidAscPropertyAsc(List<String> dtxsids);

}