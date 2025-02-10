package com.priyanshu.lib;

import com.priyanshu.model.TestType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class XmlReportGenerator implements IReportGenerator {

    private static final String XmlPath = "/XmlReports";
    private TestReport _testReport;
    private static final String Sep = File.separator;

    @Override
    public IReportGenerator Prepare(TestReport testReport) throws Exception {
        _testReport = testReport;
        Utilities.createFolder(_testReport.GetOutputPath() + XmlPath);
        return this;
    }

    @Override
    public void RenderAndSave() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();


            Element root = document.createElement("testreport");
            document.appendChild(root);


            AddElement(document, root, "AutomationFramework", _testReport.TestData.TestToolVersion);
            AddElement(document, root, "TechnologyUsed", _testReport.TestData.SeleniumVersion);
            AddElement(document, root, "JavaVersion", System.getProperty("java.version"));
            AddElement(document, root, "Browser", _testReport.TestData.TestType.equals(TestType.Web) || _testReport.TestData.TestType.equals(TestType.WebApi) ? String.valueOf(_testReport.TestData.Browser) : "-");
            AddElement(document, root, "URL", _testReport.TestData.Url);
            AddElement(document, root, "OperatingSystem", System.getProperty("os.name"));
            AddElement(document, root, "TestCaseName", _testReport.TestData.Name);
            AddElement(document, root, "TestCaseDescription", _testReport.TestData.Description);
            AddElement(document, root, "TimeZone", TimeZone.getDefault().getDisplayName());
            AddElement(document, root, "StartTime", _testReport.TestData.StartTime);
            AddElement(document, root, "EndTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            AddElement(document, root, "ExecutedBy", System.getProperty("user.name"));
            AddElement(document, root, "ExecutedOn", _testReport.TestData.Os);
            AddElement(document, root, "HostName", _testReport.TestData.ExecutedOn != null ? _testReport.TestData.ExecutedOn : "");
            AddElement(document, root, "OverallStatus", _testReport.TestData.TestCaseStatus.toString());


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(GetPath(_testReport)));
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate XML report", e);
        }
    }

    public void AddElement(Document document, Element parent, String name, String value) {
        Element element = document.createElement(name);
        element.appendChild(document.createTextNode(value != null ? value : ""));
        parent.appendChild(element);
    }

    public static String GetPath(TestReport report) throws Exception {
        var outputPath = report.GetOutputPath() + XmlPath;
        var fileName = report.TestData.ClassName + report.TestData.TestParam + ".xml";
        return outputPath + Sep + fileName;
    }
}
