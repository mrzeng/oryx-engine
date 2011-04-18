package de.hpi.oryxengine.util.io;

import java.io.InputStream;

/**
 * This is a Utility Interface for the StreamSource that can be used to access any kind of resource.
 */
public interface StreamSource {
    
    /**
     * Retrieves the type of the {@link StreamSource}.
     * 
     * @return a {@link String} representing the type of the {@link StreamSource}
     */
    String getType();
    
    /**
     * Makes the source available via {@link InputStream}.
     * 
     * @return an {@link InputStream} containing the content of the {@link StreamSource}
     */
    InputStream getInputStream();
}