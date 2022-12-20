package com.diploma.black_fox_ex.response;

import com.diploma.black_fox_ex.model.Support;
import lombok.Data;

import java.util.List;

@Data
@Deprecated
public class GetAllHelpsResp {

    private List<Support> answers;
    private String errors;
}
