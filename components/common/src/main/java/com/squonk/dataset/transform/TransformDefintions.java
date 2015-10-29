package com.squonk.dataset.transform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author timbo
 */
public class TransformDefintions {

    private final List<AbstractTransform> transforms;

    public TransformDefintions(@JsonProperty("transforms") List<AbstractTransform> transforms) {
        this.transforms = transforms;
    }

    public TransformDefintions() {
        transforms = new ArrayList<>();
    }

    public TransformDefintions deleteField(String fieldName) {
        transforms.add(new DeleteFieldTransform(fieldName));
        return this;
    }
    
    public TransformDefintions renameField(String fieldName, String newName) {
        transforms.add(new RenameFieldTransform(fieldName, newName));
        return this;
    }
    
    public TransformDefintions convertField(String fieldName, Class type) {
        transforms.add(new ConvertFieldTransform(fieldName, type));
        return this;
    }

    public List<AbstractTransform> getTransforms() {
        return transforms;
    }

    public static void main(String[] args) throws Exception {
        TransformDefintions o = new TransformDefintions();
        o.deleteField("foo").renameField("bar", "baz");
        ObjectMapper mapper = new ObjectMapper();

        String json = mapper.writeValueAsString(o);
        System.out.println("JSON: " + json);

        TransformDefintions b = mapper.readValue(json, TransformDefintions.class);
        System.out.println("OBJ:  " + b);
        System.out.println("SIZE: " + b.getTransforms().size());
        
        for (AbstractTransform t: b.getTransforms()) {
            System.out.println("T: " + t);
        }   
    }

}
