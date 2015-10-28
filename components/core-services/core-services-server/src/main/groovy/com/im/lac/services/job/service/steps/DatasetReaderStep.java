package com.im.lac.services.job.service.steps;

import com.im.lac.services.job.variable.Variable;
import com.im.lac.services.job.variable.VariableManager;
import com.squonk.dataset.Dataset;
import com.squonk.dataset.DatasetMetadata;
import com.squonk.types.io.JsonHandler;
import java.io.InputStream;
import org.apache.camel.CamelContext;

/**
 *
 * @author timbo
 */
public class DatasetReaderStep extends AbstractStep {

    public static final String FIELD_OUTPUT_DATASET = "OutputDataset";
    public static final String FIELD_INPUT_DATA = "InputData";
    public static final String FIELD_INPUT_METADATA = "InputMetadata";

    @Override
    public void execute(VariableManager varman, CamelContext context) throws Exception {
        InputStream is = fetchMappedValue(FIELD_INPUT_DATA, InputStream.class, varman);
        DatasetMetadata md = fetchMappedValue(FIELD_INPUT_METADATA, DatasetMetadata.class, varman);
        Dataset ds = JsonHandler.getInstance().unmarshalDataset(md, is);
        createMappedVariable(FIELD_OUTPUT_DATASET, Dataset.class, ds, Variable.PersistenceType.NONE, varman);
    }

}
