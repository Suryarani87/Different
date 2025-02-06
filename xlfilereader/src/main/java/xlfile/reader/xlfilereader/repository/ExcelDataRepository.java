package xlfile.reader.xlfilereader.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import xlfile.reader.xlfilereader.model.ExcelData;

@Repository
public interface ExcelDataRepository extends JpaRepository<ExcelData, Long> {

}
