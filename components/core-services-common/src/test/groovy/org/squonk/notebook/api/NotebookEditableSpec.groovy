package org.squonk.notebook.api

import org.squonk.types.io.JsonHandler
import spock.lang.Specification

/**
 * Created by timbo on 11/03/16.
 */
class NotebookEditableSpec extends Specification {

    void "to and from json"() {

        when:
        NotebookEditable e1 = new NotebookEditable(1, 2, 3, "owner", new Date(), new Date(), new NotebookInstance())
        String json = JsonHandler.getInstance().objectToJson(e1)
        NotebookEditable e2 = JsonHandler.getInstance().objectFromJson(json, NotebookEditable.class)

        then:
        json != null
        json.length() > 0
        e2 != null
        e2.id == 1
        e2.notebookId == 2
        e2.parentId == 3
        e2.owner == "owner"
        e2.createdDate != null
        e2.lastUpdatedDate != null
        e2.notebookInstance != null
    }

}