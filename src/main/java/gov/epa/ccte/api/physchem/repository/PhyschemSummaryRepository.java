package gov.epa.ccte.api.physchem.repository;

import gov.epa.ccte.api.physchem.domain.PhyschemSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface PhyschemSummaryRepository extends JpaRepository<PhyschemSummary, String> {

	List<PhyschemSummary> findByDtxsidOrderByPreferredNameAscPropertyAsc(@Param("dtxsid") String dtxsid);

	List<PhyschemSummary> findByDtxsidInAndPropertyInIgnoreCaseOrderByPreferredNameAscPropertyAsc(List<String> dtxsid, List<String> properties);

	List<PhyschemSummary> findByDtxsidInOrderByPreferredNameAscPropertyAsc(List<String> dtxsid);

	List<PhyschemSummary> findByDtxsidAndProperty(@Param("dtxsid") String dtxsid, @Param("property") String property);

}
