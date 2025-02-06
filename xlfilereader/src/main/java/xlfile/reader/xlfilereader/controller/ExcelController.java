package xlfile.reader.xlfilereader.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import xlfile.reader.xlfilereader.model.ExcelData;
import xlfile.reader.xlfilereader.service.ExcelService;

@RestController
@RequestMapping("/api/excel")
//@Tag(name="Example", description ="This is an my example API")
public class ExcelController {

	@Autowired
	private ExcelService excelservice;
	
	
	@PostMapping("/upload")
	//@Operation(summary="This api used to read and upload the xl data in local system")
	public String uploadExcelFile(@RequestParam(required = true) String filePath) {

		try {

			File file = new File(filePath);

			if (!file.exists()) {

				return "File not found";
			}

			excelservice.saveExcelData(file);

			return "File uploaded and data saved to database";

		} catch (Exception e) {

			return "Error uploading : " + e.getMessage();
		}

	}
	
	@GetMapping("/data")
	//@Operation(summary = "this is used to retraive the data")
	public List<ExcelData> getAllExcelData(){
		
		return excelservice.getallExcelData();
		
	}
}
