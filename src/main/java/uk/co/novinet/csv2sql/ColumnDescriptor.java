package uk.co.novinet.csv2sql;

public class ColumnDescriptor {
    private String csvColumnName;
    private String databaseColumnName;
    private DataType dataType;
    private int length;

    public String getDatabaseColumnName() {
        return databaseColumnName;
    }

    public void setDatabaseColumnName(String databaseColumnName) {
        this.databaseColumnName = databaseColumnName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getCsvColumnName() {
        return csvColumnName;
    }

    public void setCsvColumnName(String csvColumnName) {
        this.csvColumnName = csvColumnName;
    }
}
