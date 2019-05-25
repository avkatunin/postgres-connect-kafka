package ru.andreykatunin.postgresconnect.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class PayloadQuery {

    private String operation;
    private String schema;
    private String table;
    @JsonProperty(value = "record_new")
    private Map<String, Object> recordNew;
    @JsonProperty(value = "record_old")
    private Map<String, Object> recordOld;
    private String query;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, Object> getRecordNew() {
        return recordNew;
    }

    public void setRecordNew(Map<String, Object> recordNew) {
        this.recordNew = recordNew;
    }

    public Map<String, Object> getRecordOld() {
        return recordOld;
    }

    public void setRecordOld(Map<String, Object> recordOld) {
        this.recordOld = recordOld;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
