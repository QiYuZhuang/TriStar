package org.dbiir.tristar.config;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DisabledListDelimiterHandler;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;
import org.dbiir.tristar.type.Operation;
import org.dbiir.tristar.type.Template;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.apache.commons.configuration2.ex.ConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WorkloadLoader {
//    private static String
    public Workload load(String filepath) {
        try {
            XMLConfiguration xmlConfig = buildConfiguration(filepath);
            Workload.WorkloadBuilder builder = Workload.newBuilder();
            builder.setName(xmlConfig.getString("workload"))
                    .setDataBaseName(xmlConfig.getString("databaseName"));

            int numTemplates = xmlConfig.getMaxIndex("transactionTemplates/transactionTemplate[*]") + 1;

            for (int i = 1; i <= numTemplates; i++) {
                String key = "transactionTemplates/transactionTemplate[" + i + "]";
                Template.TemplateBuilder tBuilder = Template.newBuilder();
                tBuilder.setName(xmlConfig.getString(key + "/name"));
                int numOperations = xmlConfig.getMaxIndex(key + "/operations/operation[*]") + 1;
                for (int j = 1; j <= numOperations; j++) {
                    String opKey = key + "/operations/operation[" + j + "]";
                    Operation.OperationBuilder opBuilder = Operation.newBuilder();
                    opBuilder.setOperationType(xmlConfig.getString(opKey + "/type"))
                            .setDatabaseName(xmlConfig.getString("databaseName"))
                            .setRelationName(xmlConfig.getString(opKey + "/relationName"))
                            .addAttributes(Arrays.stream(xmlConfig.getString(opKey + "/attributes")
                                    .split(","))
                                    .map(String::trim).toList());
                    if (xmlConfig.containsKey(opKey + "/updateAttributes")) {
                        opBuilder.addUpdateAttributes(List.of(xmlConfig.getStringArray(opKey + "/updateAttributes")));
                    }

                    tBuilder.addOperation(opBuilder.build());
                }
                builder.addTemplate(tBuilder.build());
            }
            return builder.build();
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
