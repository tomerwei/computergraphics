/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package cgresearch.graphics.fileio;

import java.util.ArrayList;
import java.util.List;

import cgresearch.core.logging.Logger;

/**
 * Representation of a property in a PLY file.
 * 
 * @author Philipp Jenke
 * 
 */
public class PlyProperty {

    /**
     * Possible property types.
     * 
     */
    public enum DataType {
        FLOAT32, LIST, UINT8, INT32, INT, FLOAT, UCHAR, SHORT, UNKNOWN
    };

    /**
     * Possible vertex/facet property names.
     */
    public enum ProperyName {
        X, Y, Z, NX, NY, NZ, INTENSITY, VERTEX_INDICES, UNKNOWN
    }

    /**
     * Name
     */
    private ProperyName name;

    /**
     * List of data types
     */
    private List<DataType> types = new ArrayList<DataType>();

    /**
     * Constructor
     */
    public PlyProperty() {
    }

    /**
     * Setter
     */
    public void setName(String name) {
        if (name.toUpperCase().equals("X")) {
            this.name = ProperyName.X;
        } else if (name.toUpperCase().equals("Y")) {
            this.name = ProperyName.Y;
        } else if (name.toUpperCase().equals("Z")) {
            this.name = ProperyName.Z;
        } else if (name.toUpperCase().equals("NX")) {
            this.name = ProperyName.NX;
        } else if (name.toUpperCase().equals("NY")) {
            this.name = ProperyName.NY;
        } else if (name.toUpperCase().equals("NZ")) {
            this.name = ProperyName.NZ;
        } else if (name.toUpperCase().equals("INTENSITY")) {
            this.name = ProperyName.INTENSITY;
        } else if (name.toUpperCase().equals("VERTEX_INDICES")) {
            this.name = ProperyName.VERTEX_INDICES;
        } else {
            this.name = ProperyName.UNKNOWN;
        }
    }

    /**
     * Getter
     */
    public ProperyName getName() {
        return name;
    }

    /**
     * Add additional type.
     */
    public void addType(DataType type) {
        types.add(type);
    }

    /**
     * Return the number of data types in this property
     */
    public int getNumberOfTypes() {
        return types.size();
    }

    /**
     * Get the type at the given index
     */
    public DataType getType(int index) {
        return types.get(index);
    }

    /**
     * Add a type-string as a type.
     */
    public void addType(String typeString) {
        DataType type = convertStringToType(typeString);
        addType(type);
    }

    /**
     * @param typeString
     * @return
     */
    private DataType convertStringToType(String typeString) {
        if (typeString.equals("float32")) {
            return DataType.FLOAT32;
        } else if (typeString.equals("float")) {
            return DataType.FLOAT;
        } else if (typeString.equals("list")) {
            return DataType.LIST;
        } else if (typeString.equals("uint8")) {
            return DataType.UINT8;
        } else if (typeString.equals("int32")) {
            return DataType.INT32;
        } else if (typeString.equals("int")) {
            return DataType.INT;
        } else if (typeString.equals("uchar")) {
            return DataType.UCHAR;
        } else if (typeString.equals("short")) {
            return DataType.SHORT;
        }else {
            return DataType.UNKNOWN;
        }
    }

    /**
     * Return the size of the property in bytes.
     */
    public int getSizeInBytes() {
        int size = 0;
        for (DataType type : types) {
            size += sizeof(type);
        }
        return size;
    }

    /**
     * Return the size of the data type in bytes.
     */
    private int sizeof(DataType type) {
        switch (type) {
        case FLOAT32:
            return 4;
        case FLOAT:
            return 4;
        case INT32:
            return 4;
        case INT:
            return 4;
        case UINT8:
            return 1;
        case UCHAR:
            return 1;
        case SHORT:
            return 2;
        default:
            Logger.getInstance().error(
                    "PlyProperty.sizeof(): size cannot be determined!");
            return 0;
        }
    }

}
