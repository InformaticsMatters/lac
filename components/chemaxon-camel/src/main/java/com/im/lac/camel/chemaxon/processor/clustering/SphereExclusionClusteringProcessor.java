package com.im.lac.camel.chemaxon.processor.clustering;

import chemaxon.struc.Molecule;
import com.chemaxon.descriptors.common.Descriptor;
import com.chemaxon.descriptors.common.DescriptorComparator;
import com.chemaxon.descriptors.common.DescriptorGenerator;
import com.im.lac.camel.chemaxon.processor.ProcessorUtils;
import com.im.lac.chemaxon.clustering.SphereExclusionClusterer;
import com.im.lac.types.MoleculeObject;
import com.im.lac.types.MoleculeObjectIterable;
import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 *
 * @author timbo
 */
public class SphereExclusionClusteringProcessor<T extends Descriptor> implements Processor {

    private static final Logger LOG = Logger.getLogger(SphereExclusionClusteringProcessor.class.getName());

    public static final String HEADER_MIN_CLUSTER_COUNT = "MinClusterCount";
    public static final String HEADER_MAX_CLUSTER_COUNT = "MaxClusterCount";

    private String clusterPropertyName;
    private int minClusterCount = SphereExclusionClusterer.DEFAULT_MIN_CLUSTER_COUNT;
    private int maxClusterCount = SphereExclusionClusterer.DEFAULT_MAX_CLUSTER_COUNT;
    private final DescriptorGenerator<T> generator;
    private final DescriptorComparator<T> comparator;

    public SphereExclusionClusteringProcessor(
            DescriptorGenerator<T> generator,
            DescriptorComparator<T> comparator) {
        this.generator = generator;
        this.comparator = comparator;
    }

    public SphereExclusionClusteringProcessor(
            DescriptorGenerator<T> generator,
            DescriptorComparator<T> comparator,
            int minClusterCount,
            int maxClusterCount) {
        this(generator, comparator);
        this.minClusterCount = minClusterCount;
        this.maxClusterCount = maxClusterCount;
    }

    public SphereExclusionClusteringProcessor clusterPropertyName(String propName) {
        this.clusterPropertyName = propName;
        return this;
    }

    public SphereExclusionClusteringProcessor minClusterCount(int count) {
        this.minClusterCount = count;
        return this;
    }

    public SphereExclusionClusteringProcessor maxClusterCount(int count) {
        this.maxClusterCount = count;
        return this;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Iterable<MoleculeObject> molIter = exchange.getIn().getBody(MoleculeObjectIterable.class);
        if (molIter == null) {
            molIter = exchange.getIn().getBody(Iterable.class);
        }
        SphereExclusionClusterer clusterer = createClusterer(exchange);
        Iterable<Molecule> results = clusterer.clusterMoleculeObjects(molIter);
        if (molIter instanceof Closeable) {
            try {
                ((Closeable) molIter).close();
            } catch (IOException ioe) {
                LOG.log(Level.WARNING, "Failed to close iterator", ioe);
            }
        }
        exchange.getIn().setBody(results);
    }

    SphereExclusionClusterer createClusterer(Exchange exchange) {
        SphereExclusionClusterer clusterer = new SphereExclusionClusterer(generator, comparator);
        if (clusterPropertyName != null) {
            clusterer.setClusterPropertyName(clusterPropertyName);
        }
        clusterer.setMinClusterCount(ProcessorUtils.determineIntProperty(exchange, minClusterCount, HEADER_MIN_CLUSTER_COUNT));
        clusterer.setMaxClusterCount(ProcessorUtils.determineIntProperty(exchange, maxClusterCount, HEADER_MAX_CLUSTER_COUNT));

        return clusterer;
    }

}