package org.squonk.execution.steps.impl;

import org.apache.camel.CamelContext;
import org.squonk.dataset.Dataset;
import org.squonk.dataset.DatasetMetadata;
import org.squonk.execution.steps.AbstractStep;
import org.squonk.execution.steps.StepDefinitionConstants;
import org.squonk.execution.variable.VariableManager;
import org.squonk.types.MoleculeObject;
import org.squonk.types.PDBFile;
import org.squonk.util.IOUtils;

import java.io.InputStream;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Reads a PDB format file.
 * The structure is passed as an {@link InputStream} (can be gzipped).
 *
 * @author timbo
 */
public class PdbReaderStep extends AbstractStep {

    private static final Logger LOG = Logger.getLogger(PdbReaderStep.class.getName());

    /**
     * Expected variable name for the input
     */
    private static final String VAR_FILE_INPUT = StepDefinitionConstants.VARIABLE_FILE_INPUT;

    @Override
    public void execute(VariableManager varman, CamelContext context) throws Exception {
        LOG.info("execute PdbReaderStep");
        statusMessage = "Reading file";
        String filename = fetchMappedInput(VAR_FILE_INPUT, String.class, varman);
        statusMessage = "Read PDB file " + filename;
    }

}