package org.dbiir.tristar.config;

import lombok.Getter;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DisabledListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;
import org.dbiir.tristar.type.Attribute;
import org.dbiir.tristar.type.Relation;

public class SchemaLoader {
    public Schema load(String filepath) {
        try {
            XMLConfiguration xmlConfig = buildConfiguration(filepath);
            Schema.SchemaBuilder builder = Schema.newBuilder();
            builder.setName(xmlConfig.getString("databaseName"));
            int numRelations = xmlConfig.getMaxIndex("relations/relation") + 1;
            for (int i = 0; i < numRelations; i++) {
                String key = "relations/relation[" + i + "]";
                Relation rel = new Relation(xmlConfig.getString(key + "/name"));
                for (String str: xmlConfig.getString(key + "/attributes").split(",")) {
                    rel.addAttribute(new Attribute(str));
                }
                for (String str: xmlConfig.getString(key + "/primary").split(",")) {
                    rel.addPrimary(new Attribute(str));
                }
                builder.addRelation(rel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private XMLConfiguration buildConfiguration(String filename) throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
                        .configure(
                                params
                                        .xml()
                                        .setFileName(filename)
                                        .setListDelimiterHandler(new DisabledListDelimiterHandler())
                                        .setExpressionEngine(new XPathExpressionEngine()));
        return builder.getConfiguration();
    }
}
