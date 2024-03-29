package org.squonk.notebook.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by timbo on 29/02/16.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class NotebookSavepointDTO extends AbstractNotebookVersionDTO {

    private String creator;
    private String description;
    private String label;

    public NotebookSavepointDTO() {

    }

    public NotebookSavepointDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("notebookId") Long notebookId,
            @JsonProperty("parentId") Long parentId,
            @JsonProperty("creator") String creator,
            @JsonProperty("createdDate") Date createdDate,
            @JsonProperty("updatedDate") Date updatedDate,
            @JsonProperty("description") String description,
            @JsonProperty("label") String label,
            @JsonProperty("canvasDTO") NotebookCanvasDTO canvasDTO) {
        super(id, notebookId, parentId, createdDate, updatedDate, canvasDTO);
        this.creator = creator;
        this.description = description;
        this.label = label;
    }

    public String getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

}
