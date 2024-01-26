package com.example.finalproject.Classes;

import android.widget.EditText;

public class ValidationData {
    private boolean isValid;
    private String error;

    public ValidationData(boolean isValid, String error) {
        this.isValid = isValid;
        this.error = error;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
