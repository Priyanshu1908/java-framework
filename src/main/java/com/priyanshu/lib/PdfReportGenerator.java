package com.priyanshu.lib;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.text.DocumentException;
import com.priyanshu.model.TestData;
import com.priyanshu.model.TestEvidence;
import com.priyanshu.model.TestStatus;
import com.priyanshu.model.TestType;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class PdfReportGenerator implements IReportGenerator {

    //private final String TorchbearerLogo = "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";
    private final String PdfPath = "/PDF_Reports";
    private Image _torchbearerImage;
    private Document _textDocument;
    private PdfDocument _pdfDocument;
    private PdfWriter _pdfWriter;
    private TestReport _testReport;
    private Style _fontTableHeading;
    private Style _fontTableRowHeading;
    private Style _fontTableRowDetails;
    private Style _fontStepNote;
    private Style _fontProjectTitle;
    private Style _fontStatusPassed;
    private Style _fontStatusFailed;
    private Style _baseFont;
    private String _filename;

    /**
     * Generate fonts each time the new pdf document is created
     *
     * @throws IOException If problems with files
     */
    private void RegenerateFonts() throws IOException, java.io.IOException {
        _fontTableHeading = new Style()
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                .setFontSize(12f)
                .simulateBold()
                .setUnderline()
                .setFontColor(DeviceRgb.BLUE);


        _fontTableRowHeading = new Style()
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                .setFontSize(11f)
                .simulateBold()
                .setFontColor(DeviceRgb.BLACK);


        _fontTableRowDetails = new Style()
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                .setFontSize(11f)
                .setFontColor(DeviceRgb.BLACK);


        _fontStepNote = new Style()
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                .setFontSize(11f)
                .simulateItalic()
                .setFontColor(DeviceRgb.BLACK);


        _fontProjectTitle = new Style()
                .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                .setFontSize(21.0f)
                .setFontColor(DeviceRgb.BLACK).simulateBold().setUnderline();


        _fontStatusPassed = new Style()
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                .setFontSize(11.0f).simulateBold().setFontColor(DeviceRgb.GREEN);


        _fontStatusFailed = new Style()
                .setFont(PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN))
                .setFontSize(11.0f).simulateBold().setFontColor(DeviceRgb.RED);


        _baseFont =
                new Style().setFont(PdfFontFactory.createFont(StandardFonts.TIMES_BOLD)).setFontSize(12);
    }

    @Override
    public IReportGenerator Prepare(TestReport testReport) throws Exception {
        _testReport = testReport;
        if (StringUtils.isBlank(this._filename))
            Utilities.createFolder(_testReport.GetOutputPath() + PdfPath);
        return this;
    }

    @Override
    /**
     * Create finalized PDF report
     */
    public void RenderAndSave() throws Exception {
        try {
            RegenerateFonts();
            RenderHeader();
            for (TestEvidence evidence : _testReport.TestData.TestEvidences) {
                RenderEvidence(evidence);
                if (StringUtils.isBlank(this._filename)) AttachDocuments(evidence);
            }
            RenderFooter();
            _textDocument.flush();
            _pdfWriter.flush();
            _pdfDocument.close();
        } catch (IOException | DocumentException e) {
            throw new Exception(e);
        }
    }

    /**
     * TestReport the screenshots in PDF file
     *
     * @param evidence Test evidence
     * @throws DocumentException     Document generation exception
     * @throws MalformedURLException Incorrect url
     */
    private void RenderEvidence(TestEvidence evidence) throws DocumentException, MalformedURLException {
        _textDocument.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        _textDocument.add(_torchbearerImage);


        _textDocument.add(new Paragraph());
        _textDocument.add(new Paragraph());
        _textDocument.add(
                new Paragraph().add(new Text("Step Description: " + evidence.Details).addStyle(_fontTableHeading)));
        _textDocument.add(new Paragraph());
        var table = new Table(UnitValue.createPercentArray(new float[]{30f, 70f})).useAllAvailableWidth();
        table.addCell(new Cell().add(new Paragraph().add(new Text("Proving Step").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(
                new Paragraph().add(new Text(StringUtils.isBlank(evidence.Screenshot) ? "No" : "Yes").addStyle(_fontTableRowDetails))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text("Expected Result").addStyle(_fontTableRowHeading))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text(evidence.Expected).addStyle(_fontTableRowDetails))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text("Actual Result").addStyle(_fontTableRowHeading))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text(evidence.Actual).addStyle(_fontTableRowDetails))));
        table.addCell(new Cell().add(new Paragraph().add(new Text("Step Status").addStyle(_fontTableRowHeading))));


        var status = evidence.StepStatus == TestStatus.Failed ? _fontStatusFailed : _fontStatusPassed;


        table.addCell(
                new Cell().add(
                        new Paragraph().add(new Text(evidence.StepStatus.toString().toUpperCase()).addStyle(status))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text("Execution Timestamp").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(new Paragraph().add(new Text(new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(new Date()))
                .addStyle(_fontTableRowDetails))));


        for (Map.Entry<String, String> entry : evidence.CustomRows.entrySet()) {
            table.addCell(new Cell().add(new Paragraph().add(new Text(entry.getKey()).addStyle(_fontTableRowHeading))));
            table.addCell(new Cell().add(new Paragraph().add(new Text(entry.getValue()).addStyle(_fontTableRowDetails))));
        }


        _textDocument.add(table);
        switch (evidence.TestType) {
            case Api:
                _textDocument.add(new Paragraph().add(new Text(
                        "Note : Request and response details are captured and embedded with this report.")
                        .addStyle(_fontStepNote)));
                break;
            case Xls:
            case Dbs:
                _textDocument.add(new Paragraph().add(new Text(evidence.Details).addStyle(_fontStepNote)));
                break;
            case Web:
            case WebApi:
            case App:
                var imageBytes = Base64.getDecoder().decode(evidence.Screenshot);
                var imageData = ImageDataFactory.create(imageBytes);
                var image = new Image(imageData).setMargins(10, 10, 10, 10).setAutoScale(true);
                _textDocument.add(image);
                break;
        }
    }

    private void RenderHeader() throws DocumentException, Exception {
        var fileName = StringUtils.isBlank(_filename) ? _testReport.GetOutputPath() + PdfPath + "/" + _testReport.TestData.Name + _testReport.TestData.TestParam + ".pdf" : _filename;
        _pdfWriter = new PdfWriter(fileName);
        _pdfDocument = new PdfDocument(_pdfWriter);
        _textDocument = new Document(_pdfDocument, PageSize.A4, false);


//        _torchbearerImage = new Image(ImageDataFactory.create(Base64.getDecoder().decode(TorchbearerLogo)))
//                .scaleToFit(78, 107).setFixedPosition(18f, 799f);
        _textDocument.add(_torchbearerImage);
        _textDocument.add(new Paragraph().setTextAlignment(TextAlignment.CENTER)
                .add(new Text("Test Case Execution Report").addStyle(_fontProjectTitle)));
        _textDocument.add(new Paragraph());
        _textDocument.add(new Paragraph().add(new Text("Test Properties").addStyle(_fontTableHeading)));
        _textDocument.add(new Paragraph());


        var table = new Table(UnitValue.createPercentArray(new float[]{30f, 70f})).useAllAvailableWidth();


        table.addCell(
                new Cell().add(new Paragraph().add(new Text("Automation Framework").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(new Paragraph().add(
                new Text("Torchbearer Java Framework (" + _testReport.TestData.TestToolVersion + ")")
                        .addStyle(_fontTableRowDetails))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text("Technology Used").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(new Paragraph().add(
                new Text("Selenium " + _testReport.TestData.SeleniumVersion + " (UI), "
                        + "RestAssured " + _testReport.TestData.RestSharpVersion + " (API)")
                        .addStyle(_fontTableRowDetails))));
        table.addCell(new Cell().add(new Paragraph().add(new Text("Browser").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(new Paragraph().add(
                new Text(IsWebTest(_testReport.TestData) ? _testReport.TestData.Browser.toString() : "-").addStyle(_fontTableRowDetails))));
        table.addCell(new Cell().add(new Paragraph().add(new Text("URL").addStyle(_fontTableRowHeading))));
        table.addCell(
                new Cell().add(
                        new Paragraph().add(new Text(_testReport.TestData.Url != null ? _testReport.TestData.Url : "-").addStyle(_fontTableRowDetails))))
        ;
        table.addCell(
                new Cell().add(new Paragraph().add(new Text("Operating System").addStyle(_fontTableRowHeading))));


        table.addCell(new Cell().addStyle(_fontTableRowDetails).add(new Paragraph(_testReport.TestData.Os)));


        _textDocument.add(table);
        _textDocument.add(new Paragraph());
        _textDocument.add(new Paragraph().add(new Text("Test Case Details").addStyle(_fontTableHeading)));
        _textDocument.add(new Paragraph());
        table = new Table(UnitValue.createPercentArray(new float[]{30f, 70f})).useAllAvailableWidth();
        if (_testReport.TestData.TestManagementData != null) {
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Test Case Name").addStyle(_fontTableRowHeading))));
            table.addCell(new Cell().add(
                    new Paragraph().add(
                            new Text(_testReport.TestData.TestManagementData.TestCaseName).addStyle(_fontTableRowDetails))));
            table.addCell(new Cell().add(
                    new Paragraph().add(new Text("Test Case Description").addStyle(_fontTableRowHeading))));
            if (!_testReport.TestData.TestManagementData.TestCaseDescription.equals(""))
                table.addCell(new Cell().add(new Paragraph().add(
                        new Text(_testReport.TestData.TestManagementData.TestCaseDescription.replace("<br />", "\n"))
                                .addStyle(_fontTableRowDetails))));
            else
                table.addCell(
                        new Cell().add(new Paragraph().add(new Text("Not Available").addStyle(_fontTableRowDetails))));
        } else {
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Test Case Name").addStyle(_fontTableRowHeading))));
            table.addCell(
                    new Cell().add(new Paragraph(new Text(_testReport.TestData.Name).addStyle(_fontTableRowDetails))));
            table.addCell(new Cell().add(
                    new Paragraph().add(new Text("Test Case Description").addStyle(_fontTableRowHeading))));
            if (StringUtils.isBlank(_testReport.TestData.Description))
                _testReport.TestData.Description = "Not Available";
            table.addCell(
                    new Cell().add(
                            new Paragraph().add(
                                    new Text(_testReport.TestData.Description).addStyle(_fontTableRowDetails))));
        }


        table.addCell(new Cell().add(new Paragraph().add(new Text("Time Zone").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(
                new Paragraph().add(TimeZone.getDefault().getDisplayName()).addStyle(_fontTableRowDetails)));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text("Start Time of the Test").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(new Paragraph().add(new Text(_testReport.TestData.StartTime).addStyle(_fontTableRowDetails))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text("End Time of the Test").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(
                new Paragraph().add(
                        new Text(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())))).addStyle(_fontTableRowDetails));
        table.addCell(new Cell().add(new Paragraph().add(new Text("Executed by").addStyle(_fontTableRowHeading))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text(System.getProperty("user.name")).addStyle(_fontTableRowDetails))));
        table.addCell(new Cell().add(new Paragraph().add(new Text("Executed on").addStyle(_fontTableRowHeading))));
        table.addCell(new Cell().add(new Paragraph().add(new Text(_testReport.TestData.ExecutedOn != null ? _testReport.
                TestData.ExecutedOn : "").addStyle(_fontTableRowDetails))));
        table.addCell(
                new Cell().add(new Paragraph().add(new Text("Overall Status").addStyle(_fontTableRowHeading))));


        var status = _testReport.TestData.TestCaseStatus == TestStatus.Failed ? _fontStatusFailed : _fontStatusPassed;
        table.addCell(new Cell().add(
                new Paragraph().add(new Text(_testReport.TestData.TestCaseStatus.toString().toUpperCase())
                        .addStyle(status))));


        for (Map.Entry<String, String> entry : _testReport.TestData.CustomRows.entrySet()) {
            table.addCell(new Cell().add(new Paragraph().add(new Text(entry.getKey()).addStyle(_fontTableRowHeading))));
            table.addCell(new Cell().add(new Paragraph().add(new Text(entry.getValue()).addStyle(_fontTableRowDetails))));
        }
        _textDocument.add(table);


        if (_testReport.TestData.TestManagementData != null) {
            _textDocument.add(new Paragraph("\n"));
            _textDocument.add(new Paragraph().add(new Text("Test Management Details\n").addStyle(_fontTableHeading)));
            table = new Table(UnitValue.createPercentArray(new float[]{30f, 70f})).useAllAvailableWidth();
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Test Management Tool").addStyle(_fontTableRowHeading))));
            table.addCell(new Cell().add(new Paragraph().add(new Text(_testReport.TestData.TestManagementData.System.toUpperCase()))
                    .addStyle(_fontTableRowDetails)));
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Test Management Server").addStyle(_fontTableRowHeading))));
            table.addCell(new Cell().add(new Paragraph().add(_testReport.TestData.TestManagementData.Server)
                    .addStyle(_fontTableRowDetails)));
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Project Name").addStyle(_fontTableRowHeading))));
            table.addCell(new Cell().add(
                    new Paragraph().add(
                            new Text(_testReport.TestData.TestManagementData.ProjectName).addStyle(_fontTableRowDetails))));
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Test Case ID").addStyle(_fontTableRowHeading))));
            table.addCell(
                    new Cell().add(
                            new Paragraph().add(
                                    new Text(_testReport.TestData.TestManagementData.TestCaseId).addStyle(_fontTableRowDetails))));
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Test Case Version").addStyle(_fontTableRowHeading))));
            table.addCell(new Cell().add(
                    new Paragraph().add(
                            new Text(_testReport.TestData.TestManagementData.TestCaseVersion).addStyle(_fontTableRowDetails))));
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Test Set ID").addStyle(_fontTableRowHeading))));
            table.addCell(
                    new Cell().add(
                            new Paragraph().add(
                                    new Text(_testReport.TestData.TestManagementData.TestSetId).addStyle(_fontTableRowDetails))));
            table.addCell(
                    new Cell().add(new Paragraph().add(new Text("Test Set Name").addStyle(_fontTableRowHeading))));
            table.addCell(
                    new Cell().add(
                            new Paragraph().add(
                                    new Text(_testReport.TestData.TestManagementData.TestSetName).addStyle(_fontTableRowDetails))));
            table.addCell(new Cell().add(new Paragraph().add(new Text("Run ID").addStyle(_fontTableRowHeading))));
            table.addCell(
                    new Cell().add(
                            new Paragraph().add(
                                    new Text(_testReport.TestData.TestManagementData.CurrentRunId).addStyle(_fontTableRowDetails))));
            table.addCell(new Cell().add(new Paragraph().add(new Text("Run Name").addStyle(_fontTableRowHeading))));
            table.addCell(new Cell().add(
                    new Paragraph().add(
                            new Text(_testReport.TestData.TestManagementData.CurrentRunName).addStyle(_fontTableRowDetails))));
            if (_testReport.TestData.AlmDefectId != null) {
                table.addCell(
                        new Cell().add(new Paragraph().add(new Text("Defect Id").addStyle(_fontTableRowHeading))));
                table.addCell(
                        new Cell().add(
                                new Paragraph().add(new Text(_testReport.TestData.AlmDefectId).addStyle(_fontTableRowDetails))));
            }
            _textDocument.add(table);


        }
    }


    /**
     * Render report's footer
     */
    private void RenderFooter() {
        _textDocument.add(new Paragraph().setTextAlignment(TextAlignment.CENTER)
                .add(new Text("-".repeat(45) + "End of TestReport" + "-".repeat(45)).addStyle(_baseFont)));


        var xAxis = _pdfDocument.getDefaultPageSize().getWidth();
        var yAxis = _pdfDocument.getDefaultPageSize().getBottom();


        var numberOfPages = _pdfDocument.getNumberOfPages();
        for (var i = 1; i <= numberOfPages; i++) {
            _textDocument.showTextAligned(new Paragraph().add(new Text(String.format("Page %s of %s", i, numberOfPages))),
                    xAxis / 2, yAxis + 32, i, TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
        }
    }


    /**
     * Attach external documents to PDF report from evidence
     *
     * @param evidence Test evidence
     */
    private void AttachDocuments(TestEvidence evidence) throws Exception {
        for (var filePath : evidence.EmbeddedFiles) {
            var file = Paths.get(filePath).getFileName();
            PdfFileSpec fs;
            try {
                fs = PdfFileSpec.createEmbeddedFileSpec(_pdfDocument, filePath, file.toString(), new PdfName(file.toString()));
            } catch (IOException e) {
                throw new Exception(e);
            }
            _pdfDocument.addFileAttachment(file.toString(), fs);
            var array = new PdfArray(Collections.singletonList(fs.getPdfObject().getIndirectReference()));
            _pdfDocument.getCatalog().put(new PdfName(filePath), array);
        }
    }


    /**
     * Is Web Test
     *
     * @param testData Test Data
     * @return Test Type
     */
    private Boolean IsWebTest(TestData testData) {
        return testData.TestType.equals(TestType.Web) || testData.TestType.equals(TestType.WebApi);
    }


    /**
     * Set filename directly
     *
     * @param fileName File name
     * @return this
     */
    public IReportGenerator SetFileName(String fileName) {
        _filename = fileName;
        return this;
    }
}