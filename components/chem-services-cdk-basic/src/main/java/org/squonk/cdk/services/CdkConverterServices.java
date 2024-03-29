package org.squonk.cdk.services;

import org.squonk.camel.cdk.processor.CDKDatasetConvertProcessor;
import org.squonk.core.HttpServiceDescriptor;
import org.squonk.execution.steps.StepDefinitionConstants;
import org.squonk.io.IODescriptor;
import org.squonk.io.IODescriptors;
import org.squonk.options.OptionDescriptor;

/**
 * Created by timbo on 14/11/16.
 */
public class CdkConverterServices {


    public static final HttpServiceDescriptor SERVICE_DESCRIPTOR_CONVERT_TO_SDF = createServiceDescriptor(
            "cdk.export.sdf", "SDF Export (CDK)", "Convert to SD file format using CDK",
            new String[]{"export", "dataset", "sdf", "sdfile", "cdk"},
            null,
            "default_icon.png", "dataset_to_sdf", null);

    public static final HttpServiceDescriptor SERVICE_DESCRIPTOR_CONVERT_DATASET = createServiceDescriptor(
            "cdk.dataset.convert.molecule.format", "Convert molecule format", "Convert molecule format for a Dataset using CDK",
            new String[]{"convert", "dataset", "format", "cdk"},
            null,
            "default_icon.png", "dataset_convert_format",
            new OptionDescriptor[]{
                    new OptionDescriptor<>(String.class, "query." + CDKDatasetConvertProcessor.HEADER_MOLECULE_FORMAT, "Molecule format", "Format to convert molecules to",
                            OptionDescriptor.Mode.User)
                            .withDefaultValue("mol")
                            .withValues(new String[] {"mol", "mol:v2", "mol:v3", "smiles"})
                            .withMinMaxValues(1, 1)
            }
    );

    static final HttpServiceDescriptor[] ALL = new HttpServiceDescriptor[]{
            SERVICE_DESCRIPTOR_CONVERT_TO_SDF, SERVICE_DESCRIPTOR_CONVERT_DATASET
    };


    private static HttpServiceDescriptor createServiceDescriptor(String id, String name, String description, String[] tags, String resourceUrl, String icon, String endpoint, OptionDescriptor[] options) {

        return new HttpServiceDescriptor(
                id,
                name,
                description,
                tags,
                resourceUrl,
                icon,
                new IODescriptor[]{IODescriptors.createMoleculeObjectDataset("input")},
                new IODescriptor[]{IODescriptors.createSDF("output")},
                options,
                StepDefinitionConstants.MoleculeServiceThinExecutor.CLASSNAME,
                endpoint
        );
    }

}
