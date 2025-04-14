package gov.epa.ccte.api.physchem.service;

import gov.epa.ccte.api.physchem.domain.PhyschemExperimental;
import gov.epa.ccte.api.physchem.domain.PhyschemPredicted;
import gov.epa.ccte.api.physchem.domain.PhyschemSummary;
import gov.epa.ccte.api.physchem.domain.PhyschemSummaryDetails;
import gov.epa.ccte.api.physchem.repository.PhyschemExperimentalRepository;
import gov.epa.ccte.api.physchem.repository.PhyschemPredictedRepository;
import gov.epa.ccte.api.physchem.repository.PhyschemSummaryRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public final class PhyschemService {

    // Constants for use in filtering physchem and env fate queries
    public static final List<String> PHYSCHEM_PROPS = List.of("Henry's Law", "Boiling Point", "Melting Point", "Density", "Vapor Pressure", "Water Solubility", "LogKoa: Octanol-Air", "LogKow: Octanol-Water");
    public static final List<String> ENV_FATE_TRANSPORT_PROPS = List.of("Biodeg. Half-Life", "Soil Adsorp. Coeff. (Koc)");

    private final PhyschemSummaryRepository summaryRepository;
    private final PhyschemExperimentalRepository experimentalRepository;
    private final PhyschemPredictedRepository predictedRepository;

    private List<PhyschemSummaryDetails> assemble(List<PhyschemSummary> summaries,
            List<PhyschemExperimental> experimentals,
            List<PhyschemPredicted> predicteds) {

        log.debug("building PhyschemSummaryDetails list");

        Map<String, List<PhyschemExperimental>> expLookup = getExpLookup(experimentals);
        Map<String, List<PhyschemPredicted>> predLookup = getPredLookup(predicteds);

        List<PhyschemSummaryDetails> physchemSummaryDetails = new ArrayList<>();
        List<PhyschemPredicted> predictedList;
        List<PhyschemExperimental> experimentalList;

        for (PhyschemSummary summary : summaries) {
            PhyschemSummaryDetails details = convert(summary);

            String key = summary.getDtxsid() + "-" + summary.getProperty();

            if (expLookup.containsKey(key)) {
                experimentalList = expLookup.get(key);
            } else {
                experimentalList = new ArrayList<>();
            }

            if (predLookup.containsKey(key)) {
                predictedList = predLookup.get(key);
            } else {
                predictedList = new ArrayList<>();
            }

            details.setPredictedData(predictedList);
            details.setExperimentalData(experimentalList);

            physchemSummaryDetails.add(details);
        }

        log.debug("physchemSummaryDetails count = {}", physchemSummaryDetails.size());

        return physchemSummaryDetails;
    }

    private Map<String, List<PhyschemPredicted>> getPredLookup(List<PhyschemPredicted> predicteds) {

        Map<String, List<PhyschemPredicted>> preds = new HashMap<>();

        String oldKey = "";
        List<PhyschemPredicted> duplicates = new ArrayList<>();

        for (PhyschemPredicted pred : predicteds) {
            String key = pred.getDtxsid() + "-" + pred.getProperty();
            if (oldKey.equals("")) {
                // first record
                duplicates.add(pred);
                oldKey = key;
            } else if (oldKey.equalsIgnoreCase(key)) {
                duplicates.add(pred);
            } else {
                preds.put(oldKey, duplicates);
                oldKey = key;
                duplicates = new ArrayList<>();
                duplicates.add(pred);
            }
        }
        // last record
        preds.put(oldKey, duplicates);

        return preds;
    }

    private Map<String, List<PhyschemExperimental>> getExpLookup(List<PhyschemExperimental> experimentals) {

        Map<String, List<PhyschemExperimental>> exps = new HashMap<>();

        String oldKey = "";
        List<PhyschemExperimental> duplicates = new ArrayList<>();

        for (PhyschemExperimental exp : experimentals) {
            String key = exp.getDtxsid() + "-" + exp.getProperty();
            if (oldKey.equals("")) {
                // first record
                duplicates.add(exp);
                oldKey = key;
            } else if (oldKey.equalsIgnoreCase(key)) {
                duplicates.add(exp);
            } else {
                exps.put(oldKey, duplicates);
                oldKey = key;
                duplicates = new ArrayList<>();
                duplicates.add(exp);
            }
        }
        // last record
        exps.put(oldKey, duplicates);

        return exps;
    }

    private PhyschemSummaryDetails convert(PhyschemSummary summary) {
        PhyschemSummaryDetails details = new PhyschemSummaryDetails();

        details.setId(summary.getId());
        details.setDtxsid(summary.getDtxsid());
        details.setPreferredName(summary.getPreferredName());
        details.setProperty(summary.getProperty());
        details.setUnit(summary.getUnit());
        details.setEnvFateInd(summary.getEnvFateInd());
        details.setExpMin(summary.getExpMin());
        details.setExpMax(summary.getExpMax());
        details.setExpMean(summary.getExpMean());
        details.setExpMedian(summary.getExpMedian());
        details.setPredMin(summary.getPredMin());
        details.setPredMax(summary.getPredMax());
        details.setPredMean(summary.getPredMean());
        details.setPredMedian(summary.getPredMedian());

        return details;
    }

    public List<PhyschemSummary> fetchPhyschemSummary(String dtxsid) {
        return summaryRepository.findByDtxsidOrderByPreferredNameAscPropertyAsc(dtxsid);
    }
    
    public List<PhyschemSummary> fetchPhyschemSummaries(List<String> dtxsids) {
        return summaryRepository.findByDtxsidInOrderByPreferredNameAscPropertyAsc(dtxsids);
    }
    
    public List<PhyschemSummaryDetails> fetchEnvFateSummaryDetails(List<String> dtxsids) {
        return fetchPhyschemSummaryDetails(dtxsids, ENV_FATE_TRANSPORT_PROPS);
    }

    public List<PhyschemSummaryDetails> fetchPhyschemSummaryDetails(List<String> dtxsids) {
        return fetchPhyschemSummaryDetails(dtxsids, PHYSCHEM_PROPS);
    }

    public List<PhyschemSummaryDetails> fetchPhyschemSummaryDetails(List<String> dtxsids, final List<String> properties) {

        List<PhyschemSummary> summary = summaryRepository.findByDtxsidInAndPropertyInIgnoreCaseOrderByPreferredNameAscPropertyAsc(dtxsids, properties);
        List<PhyschemExperimental> experimentals = experimentalRepository.findByDtxsidInOrderByDtxsidAscPropertyAsc(dtxsids);
        List<PhyschemPredicted> predicteds = predictedRepository.findByDtxsidInOrderByDtxsidAscPropertyAsc(dtxsids);

        log.debug("summary count = {}, experimentals count = {}, predicteds count = {}", summary.size(), experimentals.size(), predicteds.size());

        return assemble(summary, experimentals, predicteds);
    }
}
