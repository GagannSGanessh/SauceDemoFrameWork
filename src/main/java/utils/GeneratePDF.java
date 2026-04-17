package utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneratePDF {
    public static void main(String[] args) {
        String csvFile = "TestCaseSheet.csv";
        String pdfFile = "Standard_TestCase_Sheet.pdf";

        Document document = new Document(new com.itextpdf.text.Rectangle(1000, 600)); // Wide layout for standard test sheet

        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            // Add standard industry title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Standard Automation Test Case Sheet - SauceDemo", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Read CSV and build table
            List<String[]> rows = readCSV(csvFile);

            if (!rows.isEmpty()) {
                int numColumns = rows.get(0).length;
                PdfPTable table = new PdfPTable(numColumns);
                table.setWidthPercentage(100);

                Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

                // Add Headers
                for (String header : rows.get(0)) {
                    PdfPCell cell = new PdfPCell(new Phrase(header, boldFont));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setPadding(8);
                    table.addCell(cell);
                }

                // Add Rows
                for (int i = 1; i < rows.size(); i++) {
                    for (String data : rows.get(i)) {
                        PdfPCell cell = new PdfPCell(new Phrase(data.replace("\\n", "\n"), normalFont));
                        cell.setPadding(5);
                        table.addCell(cell);
                    }
                }

                // Explicit column widths to ensure description and steps have more visual area
                float[] columnWidths = {1f, 1.5f, 1.5f, 2f, 1.5f, 3f, 2f, 1f};
                table.setWidths(columnWidths);

                document.add(table);
                System.out.println("PDF generated successfully: " + pdfFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private static List<String[]> readCSV(String file) throws IOException {
        List<String[]> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            // Regex to ignore commas inside double quotes for correct CSV parsing
            String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].replace("\"", "").trim(); // clean quotes
            }
            lines.add(values);
        }
        br.close();
        return lines;
    }
}
