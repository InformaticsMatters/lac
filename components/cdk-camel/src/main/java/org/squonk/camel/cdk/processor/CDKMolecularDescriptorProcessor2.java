package org.squonk.camel.cdk.processor;

import com.im.lac.types.MoleculeObject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.squonk.camel.CamelCommonConstants;
import org.squonk.cdk.molecule.DescriptorCalculator;
import org.squonk.cdk.molecule.MolecularDescriptors;
import org.squonk.dataset.Dataset;
import org.squonk.dataset.DatasetMetadata;
import org.squonk.dataset.MoleculeObjectDataset;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author timbo
 */
public class CDKMolecularDescriptorProcessor2 implements Processor {

    private static final Logger LOG = Logger.getLogger(CDKMolecularDescriptorProcessor2.class.getName());

    private final List<DescriptorDefinition> calculatorDefinitions = new ArrayList<>();


    public CDKMolecularDescriptorProcessor2 calculate(MolecularDescriptors.Descriptor descriptor, String[] propNames) {
        calculatorDefinitions.add(new DescriptorDefinition(descriptor, propNames));

        return this;
    }

    public CDKMolecularDescriptorProcessor2 calculate(MolecularDescriptors.Descriptor descriptor) {
        calculatorDefinitions.add(new DescriptorDefinition(descriptor, descriptor.defaultPropNames));
        return this;
    }

    protected List<DescriptorCalculator> getCalculators(Exchange exchange) throws InstantiationException, IllegalAccessException {
        List<DescriptorCalculator> calculators = new ArrayList<>();
        for (DescriptorDefinition def : calculatorDefinitions) {
            calculators.add(def.descriptor.create(def.propNames));
        }
        return calculators;
    }

    @Override
    public void process(Exchange exch) throws Exception {
        Dataset<MoleculeObject> dataset = exch.getIn().getBody(Dataset.class);
        if (dataset == null || dataset.getType() != MoleculeObject.class) {
            throw new IllegalStateException("Input must be a Dataset of MoleculeObjects");
        }
        Stream<MoleculeObject> mols = dataset.getStream();
        List<DescriptorCalculator> calculators = getCalculators(exch);
        for (DescriptorCalculator calculator : calculators) {
            mols = calculateMultiple(mols, calculator);
        }
        handleMetadata(exch, dataset.getMetadata());
        exch.getIn().setBody(new MoleculeObjectDataset(mols));
    }

    protected void handleMetadata(Exchange exch, DatasetMetadata meta) throws IllegalAccessException, InstantiationException {
        if (meta == null) {
            meta = new DatasetMetadata(MoleculeObject.class);
        }
        List<DescriptorCalculator> calculators = getCalculators(exch);
        for (DescriptorCalculator calc : calculators) {
            String[] names = calc.getPropertyNames();
            Class[] types = calc.getPropertyTypes();
            if (names.length != types.length) {
                throw new IllegalStateException("Names and types differ in length");
            }
            for (int i=0; i<names.length; i++) {
                meta.getValueClassMappings().put(names[i], types[i]);
            }
        }
        exch.getIn().setHeader(CamelCommonConstants.HEADER_METADATA, meta);
    }

    protected Stream<MoleculeObject> calculateMultiple(Stream<MoleculeObject> input, DescriptorCalculator calculator) {
        input = input.peek((mo) -> {
            try {
                calculator.calculate(mo);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Failed to evaluate molecule", ex);
            }
        });
        return input;
    }

    protected class DescriptorDefinition {

        MolecularDescriptors.Descriptor descriptor;
        String[] propNames;

        DescriptorDefinition(MolecularDescriptors.Descriptor descriptor, String[] propNames) {
            this.descriptor = descriptor;
            this.propNames = propNames;
        }
    }
}