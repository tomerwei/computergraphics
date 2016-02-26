/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.rendering.core;

/**
 * A pait consisting of a type and a coresponding render object factory.
 * 
 * @author Philipp Jenke
 * 
 */
public class TypeFactoryPair<T> {

    private final Class<?> type;

    private final IRenderObjectsFactory<T> factory;

    public TypeFactoryPair(Class<?> type, IRenderObjectsFactory<T> factory) {
        this.type = type;
        this.factory = factory;
    }

    public Class<?> getType() {
        return type;
    }

    public IRenderObjectsFactory<T> getFactory() {
        return factory;
    }

}
