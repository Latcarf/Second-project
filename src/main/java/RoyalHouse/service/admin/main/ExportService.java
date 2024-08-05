package RoyalHouse.service.admin.main;

import RoyalHouse.model.Request;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportService {

    public byte[] exportRequestsToExcel(List<Request> requests) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Requests");
            createHeaderRow(sheet);
            int rowCount = 1;

            for (Request request : requests) {
                Row row = sheet.createRow(rowCount++);
                writeRequest(request, row);
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Phone");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Date");
        headerRow.createCell(5).setCellValue("Status");
    }

    private void writeRequest(Request request, Row row) {
        row.createCell(0).setCellValue(request.getId());
        row.createCell(1).setCellValue(request.getUserName());
        row.createCell(2).setCellValue(request.getPhone());
        row.createCell(3).setCellValue(request.getEmail());
        row.createCell(4).setCellValue(request.getDate().toString());
        row.createCell(5).setCellValue(request.getStatus());
    }
}
