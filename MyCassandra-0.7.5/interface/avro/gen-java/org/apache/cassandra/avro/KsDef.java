/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package org.apache.cassandra.avro;

@SuppressWarnings("all")
public class KsDef extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = org.apache.avro.Schema.parse("{\"type\":\"record\",\"name\":\"KsDef\",\"namespace\":\"org.apache.cassandra.avro\",\"fields\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"strategy_class\",\"type\":\"string\"},{\"name\":\"strategy_options\",\"type\":[{\"type\":\"map\",\"values\":\"string\"},\"null\"]},{\"name\":\"replication_factor\",\"type\":\"int\"},{\"name\":\"cf_defs\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"CfDef\",\"fields\":[{\"name\":\"keyspace\",\"type\":\"string\"},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"column_type\",\"type\":[\"string\",\"null\"]},{\"name\":\"comparator_type\",\"type\":[\"string\",\"null\"]},{\"name\":\"subcomparator_type\",\"type\":[\"string\",\"null\"]},{\"name\":\"comment\",\"type\":[\"string\",\"null\"]},{\"name\":\"row_cache_size\",\"type\":[\"double\",\"null\"]},{\"name\":\"key_cache_size\",\"type\":[\"double\",\"null\"]},{\"name\":\"read_repair_chance\",\"type\":[\"double\",\"null\"]},{\"name\":\"gc_grace_seconds\",\"type\":[\"int\",\"null\"]},{\"name\":\"default_validation_class\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"min_compaction_threshold\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"max_compaction_threshold\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"row_cache_save_period_in_seconds\",\"type\":[\"int\",\"null\"],\"default\":0},{\"name\":\"key_cache_save_period_in_seconds\",\"type\":[\"int\",\"null\"],\"default\":3600},{\"name\":\"memtable_flush_after_mins\",\"type\":[\"int\",\"null\"],\"default\":60},{\"name\":\"memtable_throughput_in_mb\",\"type\":[\"null\",\"int\"],\"default\":null},{\"name\":\"memtable_operations_in_millions\",\"type\":[\"null\",\"double\"],\"default\":null},{\"name\":\"id\",\"type\":[\"int\",\"null\"]},{\"name\":\"column_metadata\",\"type\":[{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"ColumnDef\",\"fields\":[{\"name\":\"name\",\"type\":\"bytes\"},{\"name\":\"validation_class\",\"type\":\"string\"},{\"name\":\"index_type\",\"type\":[{\"type\":\"enum\",\"name\":\"IndexType\",\"symbols\":[\"KEYS\"],\"aliases\":[\"org.apache.cassandra.config.avro.IndexType\"]},\"null\"]},{\"name\":\"index_name\",\"type\":[\"string\",\"null\"]}],\"aliases\":[\"org.apache.cassandra.config.avro.ColumnDef\"]}},\"null\"]}],\"aliases\":[\"org.apache.cassandra.config.avro.CfDef\"]}}}],\"aliases\":[\"org.apache.cassandra.config.avro.KsDef\"]}");
  public java.lang.CharSequence name;
  public java.lang.CharSequence strategy_class;
  public java.util.Map<java.lang.CharSequence,java.lang.CharSequence> strategy_options;
  public int replication_factor;
  public java.util.List<org.apache.cassandra.avro.CfDef> cf_defs;
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return strategy_class;
    case 2: return strategy_options;
    case 3: return replication_factor;
    case 4: return cf_defs;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: name = (java.lang.CharSequence)value$; break;
    case 1: strategy_class = (java.lang.CharSequence)value$; break;
    case 2: strategy_options = (java.util.Map<java.lang.CharSequence,java.lang.CharSequence>)value$; break;
    case 3: replication_factor = (java.lang.Integer)value$; break;
    case 4: cf_defs = (java.util.List<org.apache.cassandra.avro.CfDef>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
}
