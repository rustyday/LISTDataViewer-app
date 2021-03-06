package exnihilum.com.au.listdataviewer;

/**
 * Created by david on 29/07/2017.
 *
 */

public class LayerType {

    private String server;
    private String classification;
    private String layerName;
    private String geometryType;
    private String layerID;
    private String param1;
    private String param2;
    private String param3;
    private String param4;

    public LayerType(String server, String classification, String layerName,
                     String geometryType, String layerID, String param1, String param2) {
        this.server = server;
        this.classification = classification;
        this.layerName = layerName;
        this.geometryType = geometryType;
        this.layerID = layerID;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = null;
        this.param4 = null;
    }

    public LayerType(String server, String classification, String layerName,
                     String geometryType, String layerID, String param1, String param2, String param3) {
        this.server = server;
        this.classification = classification;
        this.layerName = layerName;
        this.geometryType = geometryType;
        this.layerID = layerID;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = null;
    }

    public LayerType(String server, String classification, String layerName,
                     String geometryType, String layerID, String param1, String param2, String param3, String param4) {
        this.server = server;
        this.classification = classification;
        this.layerName = layerName;
        this.geometryType = geometryType;
        this.layerID = layerID;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
    }

    String getClassification() {
        return classification;
    }

    String getLayerName() {
        return layerName;
    }

    String getGeometryType() {
        return geometryType;
    }

    String getLayerID() {
        return layerID;
    }

    String getParam1() {
        return param1;
    }

    String getParam2() {
        return param2;
    }

    String getParam3() {
        return param3;
    }

    String getParam4() {
        return param4;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "LayerType{" +
                "classification='" + classification + '\'' +
                ", layerName='" + layerName + '\'' +
                ", geometryType='" + geometryType + '\'' +
                ", layerID='" + layerID + '\'' +
                ", param1='" + param1 + '\'' +
                ", param2='" + param2 + '\'' +
                ", param2='" + param3 + '\'' +
                ", param2='" + param4 + '\'' +
                '}';
    }

    boolean isNameEqualTo(String checkName) {
        return this.layerName.equals(checkName);
    }
}
