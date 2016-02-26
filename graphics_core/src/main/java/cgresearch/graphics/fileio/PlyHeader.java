/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.fileio;

import java.util.ArrayList;
import java.util.List;

import cgresearch.graphics.fileio.PlyProperty.ProperyName;

/**
 * Representation of a PLY header.
 * 
 * @author Philipp Jenke
 * 
 */
public class PlyHeader {

    /**
     * Possible PLY formats.
     */
    public enum Format {
        ASCII, BINARY_BIG_ENDIAN, UNKNOWN
    };

    /**
     * Format of the PLY file.
     */
    private Format format;

    /**
     * Number of vertices.
     */
    private int numberOfVertices;

    /**
     * Properties of the vertices.
     */
    private List<PlyProperty> vertexProperties = new ArrayList<PlyProperty>();

    /**
     * Number of facets
     */
    private int numberOfFacets;

    /**
     * Properties of the facets.
     */
    private List<PlyProperty> facetProperties = new ArrayList<PlyProperty>();

    /**
     * Constructor.
     */
    public PlyHeader() {
    }

    /**
     * Setter
     */
    public void setNumberOfVertices(int n) {
        numberOfVertices = n;
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    /**
     * Add additional vertex property
     * 
     * @param property
     */
    public void addVertexProperty(PlyProperty property) {
        vertexProperties.add(property);
    }

    /**
     * Check if vertices have the property with a given name and type
     */
    public boolean hasVertexProperty(String name, PlyProperty.DataType type) {
        for (int i = 0; i < vertexProperties.size(); i++) {
            if (vertexProperties.get(i).getName().equals(name)
                    && vertexProperties.get(i).getNumberOfTypes() == 1
                    && vertexProperties.get(i).getType(0) == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * Setter
     */
    public void setNumberOfFacets(int n) {
        numberOfFacets = n;
    }

    public int getNumberOfFacets() {
        return numberOfFacets;
    }

    /**
     * Add additional vertex property
     * 
     * @param property
     */
    public void addFacetProperty(PlyProperty property) {
        facetProperties.add(property);
    }

    /**
     * Check if vertices have the property with a given name and type
     */
    public boolean hasFacetProperty(String name, PlyProperty.DataType type) {
        for (int i = 0; i < facetProperties.size(); i++) {
            if (facetProperties.get(i).getName().equals(name)
                    && facetProperties.get(i).getNumberOfTypes() == 1
                    && facetProperties.get(i).getType(0) == type) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clear all contained information.
     */
    public void clear() {
        numberOfVertices = 0;
        vertexProperties.clear();
        numberOfFacets = 0;
        facetProperties.clear();
        format = Format.UNKNOWN;
    }

    /**
     * Setter.
     */
    public void setFormat(Format format) {
        this.format = format;
    }

    /**
     * Getter.
     */
    public Format getFormat() {
        return format;
    }

    /**
     * @param tokens
     */
    public void addVertexPropery(String[] tokens) {
        PlyProperty property = new PlyProperty();
        property.setName(tokens[tokens.length - 1]);
        for (int i = 1; i < tokens.length - 1; i++) {
            property.addType(tokens[i]);
        }
        addVertexProperty(property);
    }

    /**
     * @param tokens
     */
    public void addFacetProperty(String[] tokens) {
        PlyProperty property = new PlyProperty();
        property.setName(tokens[tokens.length - 1]);
        for (int i = 1; i < tokens.length - 1; i++) {
            property.addType(tokens[i]);
        }
        addFacetProperty(property);
    }

    /**
     * Get the position of the property with the given name
     */
    public int getVertexPropertyPosition(ProperyName name) {
        for (int i = 0; i < vertexProperties.size(); i++) {
            if (vertexProperties.get(i).getName() == name) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the PLY file contains vertex normals.
     */
    public boolean hasVertexNormal() {
        return hasVertexPropery(PlyProperty.ProperyName.NX)
                && hasVertexPropery(PlyProperty.ProperyName.NY)
                && hasVertexPropery(PlyProperty.ProperyName.NZ);
    }

    /**
     * Check of the PLY file has a certain vertex property.
     */
    private boolean hasVertexPropery(ProperyName propertyName) {
        for (PlyProperty property : vertexProperties) {
            if (property.getName() == propertyName) {
                return true;
            }
        }
        return false;
    }

    /**
     * Getter
     */
    public int getNumberOfVertexProperties() {
        return vertexProperties.size();
    }

    /**
     * Getter.
     */
    public PlyProperty getVertexProperty(int i) {
        return vertexProperties.get(i);
    }

    /**
     * Getter.
     */
    public int getNumberOfFacetProperties() {
        return facetProperties.size();
    }

    /**
     * Getter.
     */
    public PlyProperty getFacetProperty(int index) {
        return facetProperties.get(index);
    }
}
