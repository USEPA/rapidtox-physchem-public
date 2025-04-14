package gov.epa.ccte.api.physchem.web.rest;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataRequest {
    private List<String> dtxsids;
    private String property;
}
