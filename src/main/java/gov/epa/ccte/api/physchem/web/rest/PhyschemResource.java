package gov.epa.ccte.api.physchem.web.rest;

/**
 * REST controller for getting the {@link gov.epa.ccte.api.physchem.domain}s.
 */
import gov.epa.ccte.api.physchem.domain.PhyschemExperimental;
import gov.epa.ccte.api.physchem.domain.PhyschemPredicted;
import gov.epa.ccte.api.physchem.domain.PhyschemSummary;
import gov.epa.ccte.api.physchem.domain.PhyschemSummaryDetails;
import gov.epa.ccte.api.physchem.repository.PhyschemExperimentalRepository;
import gov.epa.ccte.api.physchem.repository.PhyschemPredictedRepository;
import gov.epa.ccte.api.physchem.repository.PhyschemSummaryRepository;
import gov.epa.ccte.api.physchem.service.PhyschemService;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PhyschemResource {
  
    private final PhyschemService physchemService;

    @GetMapping("test")
    public String greeting() {
        log.debug("test ");

        return "physchem";
    }
    
    @GetMapping("/summary/{dtxsid}")
    public List<PhyschemSummary> getPhyschemSummaryWithDetails(@PathVariable("dtxsid") String dtxsid) {
        log.debug("dtxsid = {}", dtxsid);
        
        List<PhyschemSummary> details = physchemService.fetchPhyschemSummary(dtxsid);
        
        log.debug("{} records found for summary", details.size());
        
        return details;
    }

    @PostMapping("/summary")
    public List<PhyschemSummary> getPhyschemSummaryWithDetails(@RequestBody List<String> dtxsids) {
        log.debug("dtxsids = {}", dtxsids);
        
        List<PhyschemSummary> details = physchemService.fetchPhyschemSummaries(dtxsids);
        
        log.debug("{} records found for summary", details.size());
        
        return details;
    }
    @PostMapping("/physiochemical/summary/with-details")
    public List<PhyschemSummaryDetails> getPhysiochemicalSummaryWithDetails(@RequestBody List<String> dtxsids) {
        log.debug("dtxsids = {}", dtxsids);

        List<PhyschemSummaryDetails> physchemSummaryDetails = physchemService.fetchPhyschemSummaryDetails(dtxsids);


		    log.debug("{} records found for summary", physchemSummaryDetails.size());

		    return physchemSummaryDetails;
	  }
    @PostMapping("/envfatetransport/summary/with-details")
    public List<PhyschemSummaryDetails> getEnvFateTransportWithDetails(@RequestBody List<String> dtxsids) {
        log.debug("dtxsids = {}", dtxsids);

        List<PhyschemSummaryDetails> physchemSummaryDetails = physchemService.fetchEnvFateSummaryDetails(dtxsids);

		    log.debug("{} records found for summary", physchemSummaryDetails.size());

		    return physchemSummaryDetails;
	  }

}
