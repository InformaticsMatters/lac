package org.squonk.camel.rdkit.processor;

import org.squonk.types.MoleculeObject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.squonk.camel.CamelCommonConstants;
import org.squonk.dataset.Dataset;
import org.squonk.dataset.DatasetMetadata;
import org.squonk.dataset.MoleculeObjectDataset;
import org.squonk.rdkit.io.RDKitMoleculeIOUtils;

import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author timbo
 */
public class RDKitCanonicalSmilesGeneratorProcessor implements Processor {

    private static final Logger LOG = Logger.getLogger(RDKitCanonicalSmilesGeneratorProcessor.class.getName());

    private final String propertyName;

    public RDKitCanonicalSmilesGeneratorProcessor(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public void process(Exchange exch) throws Exception {
        Dataset<MoleculeObject> dataset = exch.getIn().getBody(Dataset.class);
        if (dataset == null || dataset.getType() != MoleculeObject.class) {
            throw new IllegalStateException("Input must be a Dataset of MoleculeObjects");
        }

        String modeS = exch.getIn().getHeader("mode", String.class);
        final RDKitMoleculeIOUtils.FragmentMode mode = (modeS == null ? RDKitMoleculeIOUtils.FragmentMode.WHOLE_MOLECULE : RDKitMoleculeIOUtils.FragmentMode.valueOf(modeS.toUpperCase()));

        Stream<MoleculeObject> results = dataset.getStream().peek((mo) -> {
            String smiles = RDKitMoleculeIOUtils.generateCanonicalSmiles(mo, mode);
            if (smiles != null) {
                mo.putValue(propertyName, smiles);
            }
        });

        DatasetMetadata<MoleculeObject> meta = dataset.getMetadata();
        if (meta == null) {
            meta = new DatasetMetadata(MoleculeObject.class);
        }
        meta.getValueClassMappings().put(propertyName, String.class);
        exch.getIn().setHeader(CamelCommonConstants.HEADER_METADATA, meta);
        exch.getIn().setBody(new MoleculeObjectDataset(results));
    }

}
