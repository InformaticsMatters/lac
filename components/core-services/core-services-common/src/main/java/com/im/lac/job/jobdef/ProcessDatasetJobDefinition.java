package com.im.lac.job.jobdef;

/** Interface for a job that processes a dataset and updates it or creates a new dataset with the results. 
 * The following options are possible:
 * <ol>
 * <li>Read a dataset, process it and update it with the results 
 * (datasetId will be defined, mode will be UPDATE, datasetName will be ignored)</li>
 * <li>Read a dataset, process it and create a new dataset with the results 
 * (datasetId will be defined, mode will be CREATE, datasetName will be specified)</li>
 * </ol>
 *
 * @author timbo
 */
public interface ProcessDatasetJobDefinition extends JobDefinition {

    public enum DatasetMode {

        UPDATE, CREATE
    }

    Long getDatasetId();

    DatasetMode getDatasetMode();

    String getDatasetName();
    
    void configureDataset(Long datasetId, DatasetMode datasetMode, String newDatasetName);

}