package com.wkhmedical.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.taoxeo.lang.exception.BizRuntimeException;
import com.wkhmedical.exception.FilelException;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExcelUtil {
	private final static String xls = "xls";
	private final static String xlsx = "xlsx";
	// sheet分隔标记
	public final static String sheetSplit = "$$SHEETSPLIT$$";

	/**
	 * 读入excel文件，解析后返回
	 * 
	 * @param file
	 * @throws IOException
	 */
	public static List<String[]> readExcel(File file, MultipartFile multiFile) throws IOException {
		if ((file == null && multiFile == null) || (file != null && multiFile != null)) {
			throw new IOException("file对象参数有误");
		}
		// 获得Workbook工作薄对象
		Workbook workbook = null;
		// 检查文件
		if (file != null) {
			checkFile(file);
			workbook = getWorkbook(file);
		}
		else if (multiFile != null) {
			checkFile(multiFile);
			workbook = getWorkbook(multiFile);
		}
		// 创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
		List<String[]> list = new ArrayList<String[]>();
		if (workbook != null) {
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				// 获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if (sheet == null) {
					continue;
				}
				// 获得当前sheet的开始行
				int firstRowNum = sheet.getFirstRowNum();
				// 获得当前sheet的结束行
				int lastRowNum = sheet.getLastRowNum();
				// 循环除了第一行的所有行
				for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
					// 获得当前行
					Row row = sheet.getRow(rowNum);
					if (row == null) {
						continue;
					}
					// 获得当前行的开始列
					int firstCellNum = row.getFirstCellNum();
					// 获得当前行的列数
					int lastCellNum = row.getPhysicalNumberOfCells();
					String[] cells = new String[row.getPhysicalNumberOfCells()];
					// 循环当前行
					for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
						Cell cell = row.getCell(cellNum);
						cells[cellNum] = getCellValue(cell);
					}
					list.add(cells);
				}
				// 写入Sheet分隔标记
				String[] sheetArr = new String[] { sheetSplit };
				list.add(sheetArr);
			}
			workbook.close();
		}
		if (list.size() > 0) {
			list.remove(list.size() - 1);
		}
		return list;
	}

	// =========================================针对MultipartFile=START
	public static void checkFile(MultipartFile file) throws IOException {
		// 判断文件是否存在
		if (null == file) {
			log.error("文件不存在！");
			throw new FileNotFoundException("文件不存在！");
		}
		// 获得文件名
		String fileName = file.getOriginalFilename();
		// 判断文件是否是excel文件
		if (!fileName.endsWith(xls) && !fileName.endsWith(xlsx)) {
			log.error(fileName + "不是excel文件");
			throw new IOException(fileName + "不是excel文件");
		}
	}

	public static Workbook getWorkbook(MultipartFile file) {
		// 获得文件名
		String fileName = file.getOriginalFilename();
		// 创建Workbook工作薄对象，表示整个excel
		Workbook workbook = null;
		try {
			// 获取excel文件的io流
			InputStream is = file.getInputStream();
			// 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
			if (fileName.endsWith(xls)) {
				// 2003
				workbook = new HSSFWorkbook(is);
			}
			else if (fileName.endsWith(xlsx)) {
				// 2007
				workbook = new XSSFWorkbook(is);
			}
		}
		catch (IOException e) {
			log.info(e.getMessage());
		}
		return workbook;
	}
	// =========================================针对MultipartFile=END

	// =========================================针对File=START
	/**
	 * 判断文件是否是excel
	 * 
	 * @throws Exception
	 */
	public static void checkFile(File file) {
		// 判断文件是否存在
		if (null == file || !file.exists()) {
			log.error("文件不存在！");
			throw new FilelException("file_excel_not_exists");
		}
		// 获得文件名
		String fileName = file.getName();
		if (!(file.isFile() && (fileName.endsWith(xls) || fileName.endsWith(xlsx)))) {
			throw new FilelException("file_not_excel", fileName);
		}
	}

	/**
	 * 判断Excel的版本,获取Workbook
	 * 
	 * @param in
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbook(File file) throws IOException {
		Workbook wb = null;
		FileInputStream in = new FileInputStream(file);
		if (file.getName().endsWith(xls)) { // Excel 2003
			wb = new HSSFWorkbook(in);
		}
		else if (file.getName().endsWith(xlsx)) { // Excel 2007/2010
			wb = new XSSFWorkbook(in);
		}
		return wb;
	}
	// =========================================针对File=END

	public static String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		// 把数字当成String来读，避免出现1读成1.0的情况
		if (cell.getCellTypeEnum() == CellType.NUMERIC) {
			cell.setCellType(CellType.STRING);
		}
		// 判断数据的类型
		switch (cell.getCellTypeEnum()) {
		case NUMERIC: // 数字
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		case STRING: // 字符串
			cellValue = String.valueOf(cell.getStringCellValue());
			break;
		case BOOLEAN: // Boolean
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case FORMULA: // 公式
			cellValue = String.valueOf(cell.getCellFormula());
			break;
		case BLANK: // 空值
			cellValue = "";
			break;
		case ERROR: // 故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "未知类型";
			break;
		}
		return cellValue;
	}

	public static boolean checkFileValid(File file, int sheetNum, int[] rowNumArr, int[] colNumArr) {
		checkFile(file);
		if (sheetNum < 0 || rowNumArr.length != sheetNum || colNumArr.length != sheetNum) {
			throw new FilelException("file_analysis_param_error");
		}
		Workbook wb = null;
		try {
			wb = getWorkbook(file);
		}
		catch (Exception e) {
			throw new BizRuntimeException("file_analysis_error");
		}
		int stNum = wb.getNumberOfSheets();
		if (stNum != sheetNum) {
			throw new FilelException("file_excel_sheet_num_error");
		}
		for (int stNumTmp = 0; stNumTmp < stNum; stNumTmp++) {
			// 获得当前sheet工作表
			Sheet sheet = wb.getSheetAt(stNumTmp);
			if (sheet == null) {
				throw new FilelException("file_analysis_error");
			}
			else {
				// 总行数校验
				int rowNum = sheet.getLastRowNum();
				if (rowNum > rowNumArr[stNumTmp]) {
					throw new FilelException("file_excel_sheet_rownum_error", stNumTmp + 1, rowNumArr[stNumTmp]);
				}
				// 总列数校验
				int colNum = sheet.getRow(0).getPhysicalNumberOfCells();
				if (colNum != colNumArr[stNumTmp]) {
					throw new FilelException("file_excel_sheet_colnum_error", stNumTmp + 1);
				}
			}
		}
		return true;
	}

}
