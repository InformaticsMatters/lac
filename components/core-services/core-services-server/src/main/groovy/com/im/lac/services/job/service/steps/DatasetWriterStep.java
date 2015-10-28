package com.im.lac.services.job.service.steps;

import com.im.lac.services.job.variable.Variable;
import com.im.lac.services.job.variable.VariableManager;
import com.squonk.dataset.Dataset;
import com.squonk.dataset.DatasetMetadata;
import com.squonk.dataset.DatasetProvider;
import java.io.InputStream;
import java.util.stream.Stream;
import org.apache.camel.CamelContext;

/**
 *
 * @author timbo
 */
public class DatasetWriterStep extends AbstractStep {

    public static final String FIELD_INPUT_DATASET = "SourceDataset";
    public static final String FIELD_OUTPUT_DATA = "OutputData";
    public static final String FIELD_OUTPUT_METADATA = "OutputMetadata";

    @Override
    public String[] getInputVariableNames() {
        return new String[]{FIELD_INPUT_DATASET};
    }

    @Override
    public String[] getOutputVariableNames() {
        return new String[]{FIELD_OUTPUT_DATA, FIELD_OUTPUT_METADATA};
    }

    @Override
    public void execute(VariableManager varman, CamelContext context) throws Exception {
        DatasetProvider p = fetchMappedValue(FIELD_INPUT_DATASET, DatasetProvider.class, varman);
        Dataset ds = p.getDataset();

        Stream s = ds.createMetadataGeneratingStream(ds.getStream());
        ds.replaceStream(s);
        try (InputStream is = ds.getInputStream(false)) {
            Variable d = varman.createVariable(FIELD_OUTPUT_DATA, InputStream.class, is, Variable.PersistenceType.BYTES);
            System.out.println("JSON DA: " + varman.getValue(d));
        }

        DatasetMetadata md = ds.getMetadata();
        createMappedVariable(FIELD_OUTPUT_METADATA, DatasetMetadata.class, md, Variable.PersistenceType.JSON, varman);
    }

}
