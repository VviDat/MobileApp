package com.example.bt4.Model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Civic {
    private JsonObject normalizedInput;
    private JsonArray offices;
    private JsonArray officials;

    public Civic(JsonObject normalizedInput, JsonArray offices, JsonArray officials) {
        this.normalizedInput = normalizedInput;
        this.offices = offices;
        this.officials = officials;
    }

    public JsonObject getNormalizedInput() {
        return normalizedInput;
    }

    public void setNormalizedInput(JsonObject normalizedInput) {
        this.normalizedInput = normalizedInput;
    }

    public JsonArray getOffices() {
        return offices;
    }

    public void setOffices(JsonArray offices) {
        this.offices = offices;
    }

    public JsonArray getOfficials() {
        return officials;
    }

    public void setOfficials(JsonArray officials) {
        this.officials = officials;
    }

    @Override
    public String toString() {
        return "Civic{" +
                "normalizedInput=" + normalizedInput +
                ", offices=" + offices +
                ", officials=" + officials +
                '}';
    }
}
