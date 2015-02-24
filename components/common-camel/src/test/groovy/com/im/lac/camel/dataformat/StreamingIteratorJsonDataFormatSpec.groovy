package com.im.lac.camel.dataformat

import com.fasterxml.jackson.databind.ObjectMapper
import com.im.lac.types.MoleculeObject
import com.im.lac.types.MoleculeObjectIterable
import spock.lang.Specification

/**
 *
 * @author timbo
 */
class StreamingIteratorJsonDataFormatSpec extends Specification {
    
    
    void "binary data"() {
        
        setup:
        String txt = "Hello World!"
        def b = new BinaryClass()
        b.bytes = txt.getBytes()
        def dataFormat = new StreamingIteratorJsonDataFormat<SimpleClass>(BinaryClass.class)
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        
        
        when:
        dataFormat.marshal(null, [b], out)
        def json = new String(out.toByteArray())
        println "JSON binary: " + json
        def xxx = dataFormat.unmarshal(null, new ByteArrayInputStream(json.getBytes()))
        println "reconverted: $xxx"
        def cols = xxx.collect()
        println "magic: ${cols[0].bytes}"
        String s = new String(cols[0].bytes)
        println "string: $s"
        
        then: 
        json != null
        xxx != null
        cols.size() == 1
        s == txt
        
        
    }
    
    void "unmarshal simple"() {
        
        setup:
        String input = '''[
{"name" : "tim", "age": 18},
{"name" : "tom", "age": 99}
]'''
        def dataFormat = new StreamingIteratorJsonDataFormat<SimpleClass>(SimpleClass.class)
        
        when:
        def result = dataFormat.unmarshal(null, new ByteArrayInputStream(input.getBytes()))
        int size = 0
        result.each { size++ }
        
        then:
        size == 2
    }
    
    void "marshal simple"() {
        
        setup:
        def input = [new SimpleClass("tim", 18),new SimpleClass("tom", 99)]
            
        def dataFormat = new StreamingIteratorJsonDataFormat<SimpleClass>(SimpleClass.class)
        ByteArrayOutputStream out = new ByteArrayOutputStream()
        
        when:
        dataFormat.marshal(null, input, out)
        def result = new String(out.toByteArray())
        println "JSON: " + result
        
        then:
        result.split('name').length == 3
    }
    
    void "marshal moleculeobjects"() {
        
        setup:
        def input = [new MoleculeObject('CCC'),new MoleculeObject('c1ccccc')]
            
        ObjectMapper mapper = new ObjectMapper()
        
        when:
        String json = mapper.writeValueAsString(input[0])
        println "JSON: " + json
        
        then:
        json.indexOf('representations') == -1
    }
    
    void "unmarshal moleculeobjects"() {
            
        setup:
        String input = '''[
    {"format":"smiles","source":"c1ccccc1","values":{"field_0":"1"}},
    {"format":"smiles","source":"CCC","values":{"field_0":"2"}}
    ]'''
        def dataFormat = new StreamingIteratorJsonDataFormat<MoleculeObject>(MoleculeObject.class)
            
        when:
        def result = dataFormat.unmarshal(null, new ByteArrayInputStream(input.getBytes()))
        def mols = result.collect()
            
        then:
        mols.size() == 2
        mols[0].source == 'c1ccccc1'
    }
    
    	
}
