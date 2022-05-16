package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.model.SupportAnswer;
import lombok.Data;

import java.util.List;

@Data
public class GetAllHelpsResp {
    private List<SupportAnswer> answers;
    private String errors;
}
