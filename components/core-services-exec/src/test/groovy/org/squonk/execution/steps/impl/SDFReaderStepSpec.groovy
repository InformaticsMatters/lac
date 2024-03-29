package org.squonk.execution.steps.impl

import org.squonk.dataset.Dataset

import org.squonk.execution.variable.VariableManager
import org.squonk.io.IODescriptor
import org.squonk.io.IODescriptors
import org.squonk.io.IORoute
import org.squonk.notebook.api.VariableKey
import org.squonk.types.io.JsonHandler
import spock.lang.Specification

/**
 *
 * @author timbo
 */
class SDFReaderStepSpec extends Specification {
    
    void "test read sdf"() {
        VariableManager varman = new VariableManager(null, 1, 1);
        SDFReaderStep step = new SDFReaderStep()
        FileInputStream is = new FileInputStream("../../data/testfiles/Kinase_inhibs.sdf.gz")
        //FileInputStream is = new FileInputStream("../../data/testfiles/dhfr_standardized.sdf.gz")
        Long producer = 1
        step.configure(producer, "job1", [:],
                [IODescriptors.createMoleculeObjectDataset("input")] as IODescriptor[],
                [IODescriptors.createMoleculeObjectDataset("output")] as IODescriptor[],
                [(SDFReaderStep.VAR_SDF_INPUT): new VariableKey(producer, "input")],
                [:]
        )
        varman.putValue(
            new VariableKey(producer,"input"),
            InputStream.class, 
            is)
        varman.putValue(new VariableKey(producer,"input"),
                String.class,
                "some filename")
        
        when:
        step.execute(varman, null)
        Dataset ds = varman.getValue(new VariableKey(producer, SDFReaderStep.VAR_DATASET_OUTPUT), Dataset.class)

        then:
        ds != null
        ds.items.size() == 36
        ds.metadata.getProperties()['source'].contains("some filename")


        //InputStream json = JsonHandler.getInstance().marshalStreamToJsonArray(ds.getStream(), false)
        //println json.text
        //File f = new File("/tmp/dhfr.json")
        //f << json.text

        cleanup:
        is.close()
        
    }
    
	
}

