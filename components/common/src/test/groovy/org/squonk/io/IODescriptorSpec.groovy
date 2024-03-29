package org.squonk.io

import org.squonk.types.io.JsonHandler
import spock.lang.Specification

/**
 * Created by timbo on 02/01/17.
 */
class IODescriptorSpec extends Specification {

    void "to/from json"() {
        when:
        def iod1 = new IODescriptor("in", "text/plain", String.class, null)
        def json = JsonHandler.instance.objectToJson(iod1)
        println json
        def iod2 = JsonHandler.instance.objectFromJson(json, IODescriptor.class)

        then:
        iod2 != null

    }


    void "equals  single"() {
        when:
        def iod1 = new IODescriptor("in", "text/plain", String.class, null)
        def iod2 = new IODescriptor("in", "text/plain", String.class, null)

        then:
        iod1.equals(iod2)

    }

    void "equals  double"() {
        when:
        def iod1 = new IODescriptor("in", "text/plain", List.class, String.class)
        def iod2 = new IODescriptor("in", "text/plain", List.class, String.class)

        then:
        iod1.equals(iod2)

    }


}
