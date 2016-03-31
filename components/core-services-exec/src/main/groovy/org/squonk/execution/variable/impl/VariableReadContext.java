package org.squonk.execution.variable.impl;

import org.squonk.api.VariableHandler;
import org.squonk.client.VariableClient;
import org.squonk.core.client.NotebookRestClient;
import org.squonk.util.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by timbo on 13/03/16.
 */
public class VariableReadContext implements VariableHandler.ReadContext{

    private final VariableClient client;
    private final Long notebookId;
    private final Long sourceId;
    private final String variableName;

    public VariableReadContext(VariableClient client, Long notebookId, Long sourceId, String variableName) {
        this.client = client;
        this.notebookId = notebookId;
        this.sourceId = sourceId;
        this.variableName = variableName;
    }

    @Override
    public String readTextValue(String key) throws Exception {
        return client.readTextValue(notebookId, sourceId, variableName, key);
    }

    @Override
    public InputStream readStreamValue(String key) throws Exception {
        InputStream is = client.readStreamValue(notebookId, sourceId, variableName, key);
        return (is == null ? null : IOUtils.getGunzippedInputStream(is));
    }

}
