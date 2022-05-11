package com.diplom.black_fox_ex.response;

import com.diplom.black_fox_ex.model.SupportAnswer;

import java.util.List;

public class HelpGetAllAnswersResponse {
    List<SupportAnswer> answers;
    String errors;

    public List<SupportAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SupportAnswer> answers) {
        this.answers = answers;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
