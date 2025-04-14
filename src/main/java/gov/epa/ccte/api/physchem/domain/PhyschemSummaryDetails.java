package gov.epa.ccte.api.physchem.domain;

import lombok.Data;

import java.util.List;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class PhyschemSummaryDetails extends PhyschemSummary{

    List<PhyschemExperimental> experimentalData;

    List<PhyschemPredicted> predictedData;
}
