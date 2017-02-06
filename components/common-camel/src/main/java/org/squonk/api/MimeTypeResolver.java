package org.squonk.api;

import org.squonk.io.IODescriptor;
import org.squonk.util.CommonMimeTypes;

/**
 * Created by timbo on 20/03/2016.
 */
public interface MimeTypeResolver extends CommonMimeTypes {

    Class resolvePrimaryType(String mimeType);

    Class resolveGenericType(String mimeType);

    IODescriptor createIODescriptor(String name, String mediaType);

    default HttpHandler createHttpHandler(String mimeType) {
        return createHttpHandler(resolvePrimaryType(mimeType), resolveGenericType(mimeType));
    }

    default HttpHandler createHttpHandler(Class primaryType) {
        return createHttpHandler(primaryType, null);
    }

    HttpHandler createHttpHandler(Class primaryType, Class genericType);

    default VariableHandler createVariableHandler(String mimeType) {
        return createVariableHandler(resolvePrimaryType(mimeType), resolveGenericType(mimeType));
    }

    default VariableHandler createVariableHandler(Class primaryType) {
        return createVariableHandler(primaryType, null);
    }

    VariableHandler createVariableHandler(Class primaryType, Class secondaryType);
}



