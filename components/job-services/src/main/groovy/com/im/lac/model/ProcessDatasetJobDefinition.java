package com.im.lac.model;

import com.im.lac.jobs.Job;

/**
 *
 * @author timbo
 */
public class ProcessDatasetJobDefinition implements JobDefinition {

    private final Long datasetId;

    private final String destination;

    private final Job.DatasetMode mode;
    
    private final Class resultType;
    
    private final String datasetName;

    public ProcessDatasetJobDefinition(
            Long datasetId, 
            String destination, 
            Job.DatasetMode mode, 
            Class resultType, 
            String datasetName) {
        this.datasetId = datasetId;
        this.destination = destination;
        this.mode = mode;
        this.resultType = resultType;
        this.datasetName = datasetName;
    }
    
    public ProcessDatasetJobDefinition(
            Long datasetId, 
            String destination, 
            Job.DatasetMode mode, 
            Class resultType) {
        this.datasetId = datasetId;
        this.destination = destination;
        this.mode = mode;
        this.resultType = resultType;
        this.datasetName = null;
    }
    
    public ExecutionMode getExecutionMode() {
        return ExecutionMode.ASYNC_SIMPLE;
    }

    public Long getDatasetId() {
        return datasetId;
    }

    public String getDestination() {
        return destination;
    }

    public Job.DatasetMode getMode() {
        return mode;
    }

    public Class getResultType() {
        return resultType;
    }

    public String getDatasetName() {
        return datasetName;
    }

}
