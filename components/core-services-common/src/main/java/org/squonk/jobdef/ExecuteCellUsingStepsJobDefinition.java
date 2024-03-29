package org.squonk.jobdef;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.squonk.execution.steps.StepDefinition;
import org.squonk.io.IODescriptor;

/**
 * Created by timbo on 31/12/15.
 */
public class ExecuteCellUsingStepsJobDefinition implements StepsCellExecutorJobDefinition {

    private Long notebookId;
    private Long editableId;
    private Long cellId;
    private IODescriptor[] inputs;
    private IODescriptor[] outputs;
    private StepDefinition[] steps;

    public ExecuteCellUsingStepsJobDefinition() {}

    public ExecuteCellUsingStepsJobDefinition(
            @JsonProperty("notebookId") Long notebookId,
            @JsonProperty("editableId") Long editableId,
            @JsonProperty("cellId") Long cellId,
            @JsonProperty("inputs") IODescriptor[] inputs,
            @JsonProperty("outputs") IODescriptor[] outputs,
            @JsonProperty("steps") StepDefinition[] steps) {
        this.notebookId = notebookId;
        this.editableId = editableId;
        this.cellId = cellId;
        this.inputs = inputs;
        this.outputs = outputs;
        this.steps = steps;
    }

    public ExecuteCellUsingStepsJobDefinition(
            Long notebookId,
            Long editableId,
            Long cellId,
            IODescriptor[] inputs,
            IODescriptor[] outputs,
           StepDefinition step) {
        this.notebookId = notebookId;
        this.editableId = editableId;
        this.cellId = cellId;
        this.inputs = inputs;
        this.outputs = outputs;
        this.steps = new StepDefinition[] { step };
    }

    @Override
    public Long getNotebookId() {
        return notebookId;
    }

    @Override
    public Long getEditableId() {
        return editableId;
    }

    @Override
    public Long getCellId() {
        return cellId;
    }

    @Override
    public IODescriptor[] getInputs() {
        return inputs;
    }

    @Override
    public IODescriptor[] getOutputs() {
        return outputs;
    }

    public StepDefinition[] getSteps() {
        return steps;
    }

    public void configureCellAndSteps(Long notebookId, Long editableId, Long cellId, IODescriptor[] inputs, IODescriptor[] outputs, StepDefinition... steps) {
        this.notebookId = notebookId;
        this.editableId = editableId;
        this.cellId = cellId;
        this.inputs = inputs;
        this.outputs = outputs;
        this.steps = steps;
    }

}
