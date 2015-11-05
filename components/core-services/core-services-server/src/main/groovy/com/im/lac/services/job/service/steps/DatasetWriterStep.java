package com.im.lac.services.job.service.steps;

import com.im.lac.services.job.variable.Variable;
import com.im.lac.services.job.variable.VariableManager;
import com.squonk.dataset.Dataset;
import com.squonk.dataset.DatasetProvider;
import org.apache.camel.CamelContext;

/** Reads a dataset and writes it. The only real purpose of this is to take a temporary
 * dataset (PersistenceType.NONE) and make it persistent (PersistenceType.DATASET).
 * Generally you should not need to do this, but its available should you need to.
 *
 * @author timbo
 */
public class DatasetWriterStep extends AbstractStep {

    public static final String FIELD_INPUT_DATASET = "_DatasetWriterSourceDataset";
    public static final String FIELD_OUTPUT_DATASET = "_DatasetWriterOutputDataset";

    @Override
    public String[] getInputVariableNames() {
        return new String[]{FIELD_INPUT_DATASET};
    }

    @Override
    public String[] getOutputVariableNames() {
        return new String[]{FIELD_OUTPUT_DATASET};
    }

    @Override
    public void execute(VariableManager varman, CamelContext context) throws Exception {
        // the assumption is that the dataset has PersistenceType.NONE
        DatasetProvider p = fetchMappedValue(FIELD_INPUT_DATASET, DatasetProvider.class, varman);
        Dataset ds = p.getDataset();
        createMappedVariable(FIELD_OUTPUT_DATASET, Dataset.class, ds, Variable.PersistenceType.DATASET, varman);
    }

}
