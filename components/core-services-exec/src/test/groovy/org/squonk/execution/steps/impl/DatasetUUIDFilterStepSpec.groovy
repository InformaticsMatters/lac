package org.squonk.execution.steps.impl

import org.squonk.dataset.Dataset
import org.squonk.execution.variable.VariableManager
import org.squonk.io.IODescriptor
import org.squonk.io.IODescriptors
import org.squonk.notebook.api.VariableKey
import org.squonk.types.BasicObject
import spock.lang.Specification

/**
 * Created by timbo on 16/05/17.
 */
class DatasetUUIDFilterStepSpec extends Specification {


    def input = [
            new BasicObject([i: 1, f: 1.1f, s: 'one']),
            new BasicObject([i: 2, f: 2.2f, s: 'two']),
            new BasicObject([i: 3, f: 3.3f, s: 'three']),
            new BasicObject([i: 4, f: 4.4f, s: 'four']),
            new BasicObject([i: 5, f: 5.5f, s: 'five']),
            new BasicObject([i: 6, f: 6.6f, s: 'six']),
    ]
    Dataset ds = new Dataset(BasicObject.class, input)
    Long producer = 1

    void "simple filter step"() {

        VariableManager varman = new VariableManager(null, 1, 1);

        def uuids = input[1].UUID.toString() + "," + input[3].UUID.toString() + " , \n" + input[5].UUID.toString()

        varman.putValue(
                new VariableKey(producer, "input"),
                Dataset.class,
                ds)

        DatasetUUIDFilterStep step = new DatasetUUIDFilterStep()
        step.configure(producer, "job1",
                [(DatasetUUIDFilterStep.OPTION_UUIDS): uuids],
                [IODescriptors.createMoleculeObjectDataset("input")] as IODescriptor[],
                [IODescriptors.createMoleculeObjectDataset("output")] as IODescriptor[],
                ["input": new VariableKey(producer, "input")],
                [:]
        )

        when:
        step.execute(varman, null)
        Dataset output = varman.getValue(new VariableKey(producer, "output"), Dataset.class)

        then:

        output != null
        output.items.size() == 3
        output.items.find { it.UUID == input[1].UUID} != null
        output.items.find { it.UUID == input[3].UUID} != null
        output.items.find { it.UUID == input[5].UUID} != null
    }

    void "test parse"() {

        when:
        DatasetUUIDFilterStep step = new DatasetUUIDFilterStep()

        then:
        step.parseUUIDs("""${UUID.randomUUID().toString()} ${UUID.randomUUID().toString()} , ${UUID.randomUUID().toString()},${UUID.randomUUID().toString()}
 ${UUID.randomUUID().toString()}\n\n${UUID.randomUUID().toString()} \n${UUID.randomUUID().toString()}, ${UUID.randomUUID().toString()}""").size() == 8



    }


}
