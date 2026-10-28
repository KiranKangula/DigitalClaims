package utils;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;
import org.testng.reporters.XMLReporterConfig;
import org.testng.reporters.XMLReporter;

import java.util.List;

public class CustomReportListener extends XMLReporter implements IReporter {
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        XMLReporterConfig config = getConfig();
        config.setOutputDirectory(outputDirectory);
        config.setGenerateTestResultAttributes(true);
        config.setGenerateGroupsAttribute(true);
        config.setGenerateDependsOnMethods(true);
        config.setGenerateDependsOnGroups(true);
        super.generateReport(xmlSuites, suites, outputDirectory);
    }
}