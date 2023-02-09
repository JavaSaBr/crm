package integration.test.db.model

class DbTestPrefix {
  String propertyPrefix
  String schemaPrefix
  DbTestSchema dbTestSchema
  
  DbTestPrefix(String propertyPrefix, String schemaPrefix, DbTestSchema dbTestSchema) {
    this.propertyPrefix = propertyPrefix
    this.schemaPrefix = schemaPrefix
    this.dbTestSchema = dbTestSchema
  }
}
