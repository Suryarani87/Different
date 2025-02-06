package xlfile.reader.xlfilereader.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xlfile.reader.xlfilereader.model.ExcelData;
import xlfile.reader.xlfilereader.repository.ExcelDataRepository;

@Service
public class ExcelService {

	@Autowired
	private ExcelDataRepository exceldatarepository;

	public void saveExcelData(File file) throws IOException {
		
		System.out.println("Processing File Name :"+file.getName());
		System.out.println("File Path :"+file.getAbsolutePath());
		System.out.println("File Size :"+file.length()+"Byte");

		List<ExcelData> excelDataList = new ArrayList<>();

		try (FileInputStream inputStream = new FileInputStream(file)) {

			Workbook workbook;

			if (file.getName().endsWith(".xlsx")) {

				workbook = new XSSFWorkbook();
			} else if (file.getName().endsWith(".xls")) {

				workbook = new HSSFWorkbook();

			} else {

				throw new IllegalArgumentException("Unsupported file format. Only .xls and .xlsx are supported");
			}

			if (workbook.getNumberOfSheets() == 0) {

				throw new IllegalArgumentException("The excel file doest not contain any sheet");
			}

			Sheet sheet = workbook.getSheetAt(0);
			
			if(sheet.getPhysicalNumberOfRows() == 0) {
				
				throw new IllegalArgumentException("The sheet is empty");
			}

			// Read headers (first row)
			Iterator<Row> rowIteretor = sheet.iterator();

			Row headerRow = rowIteretor.next();

			List<String> headers = new ArrayList<>();

			for (Cell cell : headerRow) {

				headers.add(cell.getStringCellValue());
			}

			// Read data rows

			while (rowIteretor.hasNext()) {

				Row row = rowIteretor.next();
				ExcelData exceldata = new ExcelData();

				Map<String, String> dataMap = new HashMap<>();

				for (int i = 0; i < headers.size(); i++) {

					Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

					String cellvalue = "";

					switch (cell.getCellType()) {

					case STRING:
						cellvalue = cell.getStringCellValue();
						break;

					case NUMERIC:
						cellvalue = String.valueOf(cell.getNumericCellValue());
						break;

					case BOOLEAN:

						cellvalue = String.valueOf(cell.getBooleanCellValue());
						break;

					default:

						cellvalue = "";
					}

					dataMap.put(headers.get(i), cellvalue);
				}

				exceldata.setData(dataMap);

				excelDataList.add(exceldata);

			}

		}

		exceldatarepository.saveAll(excelDataList);

	}

	public List<ExcelData> getallExcelData() {

		return exceldatarepository.findAll();
	}

}
