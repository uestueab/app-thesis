package com.test.viewpagerfun.model.dataconverter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String fromSynonymList(List<String> synonyms) {
        if (synonyms == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        return gson.toJson(synonyms, type);
    }

    @TypeConverter
    public List<String> toSynonymList(String json) {
        if (json == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> synonyms = gson.fromJson(json, type);
        return synonyms;
    }
}
