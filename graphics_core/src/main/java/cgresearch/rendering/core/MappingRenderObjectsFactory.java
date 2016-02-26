/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a mapping between CG node content types and registered render object
 * factories.
 * 
 * @author Philipp Jenke
 * 
 */
public class MappingRenderObjectsFactory<T> {

    /**
     * List of pairs between type and factory.
     */
    private List<IRenderObjectsFactory<T>> mapping = new ArrayList<IRenderObjectsFactory<T>>();

    /**
     * Insert a new pair into the data structure.
     * 
     * @param type
     *            Type in the pair.
     * @param factory
     *            Corresponding factory.
     */
    public void put(IRenderObjectsFactory<T> factory) {
        mapping.add(factory);
    }

    /**
     * Find the corresponding factory for an object (determined by the type).
     * 
     * @param object
     *            Object for which the factory is sought.
     * @return Corresponding factory. Return null, if no factory fits.
     */
    public IRenderObjectsFactory<T> get(Object object) {
        for (IRenderObjectsFactory<T> factory : mapping) {
            if (sameType(object.getClass(), factory.getType())) {
                return factory;
            }
        }
        return null;
    }

    /**
     * Return true, if type1 fits into type2, e.g. type1 implements type2.
     * 
     * @param type1
     *            Type for which a match is sought.
     * @param type2
     *            Type for which a factory exists.
     * @return True, if type1 fits to type2, false otherwise.
     */
    private boolean sameType(Class<?> type1, Class<?> type2) {
        boolean isInstance = type2.isAssignableFrom(type1);
        return isInstance;
    }
}