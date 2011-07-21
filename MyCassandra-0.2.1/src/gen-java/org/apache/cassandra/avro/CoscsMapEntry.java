/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.apache.cassandra.avro;

@SuppressWarnings("all")
public class CoscsMapEntry extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"CoscsMapEntry\",\"namespace\":\"org.apache.cassandra.avro\",\"fields\":[{\"name\":\"key\",\"type\":\"bytes\"},{\"name\":\"columns\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ColumnOrSuperColumn\",\"fields\":[{\"name\":\"column\",\"type\":[{\"type\":\"record\",\"name\":\"Column\",\"fields\":[{\"name\":\"name\",\"type\":\"bytes\"},{\"name\":\"value\",\"type\":\"bytes\"},{\"name\":\"timestamp\",\"type\":\"long\"},{\"name\":\"ttl\",\"type\":[\"int\",\"null\"]}]},\"null\"]},{\"name\":\"super_column\",\"type\":[{\"type\":\"record\",\"name\":\"SuperColumn\",\"fields\":[{\"name\":\"name\",\"type\":\"bytes\"},{\"name\":\"columns\",\"type\":{\"type\":\"array\",\"items\":\"Column\"}}]},\"null\"]}]}}}]}");
  public java.nio.ByteBuffer key;
  public java.util.List<org.apache.cassandra.avro.ColumnOrSuperColumn> columns;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return key;
    case 1: return columns;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: key = (java.nio.ByteBuffer)value$; break;
    case 1: columns = (java.util.List<org.apache.cassandra.avro.ColumnOrSuperColumn>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
}