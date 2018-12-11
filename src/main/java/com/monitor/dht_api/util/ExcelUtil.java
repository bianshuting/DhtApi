package com.monitor.dht_api.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {	
	
	/**
	 * 获取文件
	 */
	public static File createFile(String filePath) {
        File file = null;
        if (!filePath.equals("")) {
            file = new File(filePath);
        } else {            
             System.out.println("文件不存在！");
            System.exit(-1);
        }
        return file;
    }
	
	/**
     * 
     * @param file
     * @return true: 文件未被操作; false: 文件正在被操作.
     */
    public static boolean isFile(File file) {
        return file.renameTo(file);
    }
	 /**
     * 检查传入的文件后缀是否为 xlsx
     * 
     * @param filePacht
     * @return
     */
    public static boolean checkXlsx(File file) {
        boolean flag = false;

        //if (file.exists()) {
            String str = file.getName();
            if (str.substring(str.lastIndexOf(".") + 1).equals("xlsx"))
                flag = true;
//        } else {            
//            System.out.println("文件不存在！");
//        }

        return flag;
    }
	
	/**
	 * 获取excel工作簿
	 */
	public static XSSFWorkbook createExcelWorkBook(String filePath) {
        File file = createFile(filePath);

        BufferedInputStream in = null;
        XSSFWorkbook book = null;

        //if (checkXlsx(file)) {
            try {
                in = new BufferedInputStream(new FileInputStream(file));
                book = new XSSFWorkbook(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
        return book;
    }
	
	/**
     * 创建 XSSFSheet
     * 
     * @param sheetName
     * @return
     */
    public static XSSFSheet createExcelSheet(String filePath,String sheetName) {
        XSSFSheet sheet = null;
        if (!sheetName.equals("")) {
            XSSFWorkbook book = createExcelWorkBook(filePath);
            if (book != null) {
                int sheetCount = book.getNumberOfSheets();
                for (int i = 0; i < sheetCount; i++) {
                    if (sheetName.equals(book.getSheetName(i))) {
                        sheet = book.getSheet(sheetName);
                        break;
                    }
                }
            }
        } else {            
            System.out.println("工作表名称不正确！");
            System.exit(-1);
        }

        return sheet;
    }
    
    /**
     * 获取指定 sheet 中最大行数
     * 
     * @param sheetName
     * @return
     */
    public static int getSheetMaxRow(String filePath,String sheetName) {
        int maxRow = -1;
        if (createExcelSheet(filePath,sheetName) != null)
            maxRow = createExcelSheet(filePath,sheetName).getLastRowNum();
        return maxRow;
    }

    
    /**
     * 获取指定行
     * 
     * @param sheetName
     * @param line
     *            行索引, 从 0 开始
     * @return
     */
    public static XSSFRow getExcelRow(String filePath,String sheetName,int line) {
        XSSFRow row = null;
        if (line <= getSheetMaxRow(filePath,sheetName))
            row = createExcelSheet(filePath,sheetName).getRow(line);
        return row;
    }
    
    /**
     * 获取 Excel 指定行数据
     * 
     * @param sheetName
     * @param line
     * @return 返回一个一维数组
     */
    public static String[] readExcelRows(String filePath,String sheetName,int line) {
        String[] result = null;
        XSSFRow row = getExcelRow(filePath,sheetName,line);
        int maxRow = getSheetMaxRow(filePath,sheetName);
        if (row != null && maxRow > -1) {
            int columnNum = row.getLastCellNum();
            result = new String[columnNum];
            for (int i = 0; i < columnNum; i++) {
                // 判断单元格是否为空, 不进行判断时, 如遇到空白单元格时, 抛出空指针异常
                if (null != row.getCell(i))
                    result[i] = row.getCell(i).toString().trim();
            }
        }
        return result;
    }
    
    /**
     * 获取指定单元格中内容
     * 
     * @param sheetName
     * @param line
     *            行
     * @param column
     *            列
     * @return
     */
    public static String readExcelCell(String filePath,String sheetName,int line, int column) {
        String[] value = null;
        String result = "";
        value = readExcelRows(filePath,sheetName,line);
        if (value != null) {
            result = value[column];
        }
        return result;
    }
    
    /**
     * 将数据写入指定单元格
     * 
     * @param sheetName
     *            sheet 名称
     * @param line
     *            需写入的行
     * @param column
     *            需写入的列
     * @param content
     *            需写入的内容
     * @throws IOException
     */
    public static void writeExcelCell(String filePath,String sheetName,int line, int column, String content){
        XSSFWorkbook book = createExcelWorkBook(filePath);
        XSSFSheet sheet = book.getSheet(sheetName);
        try {
        	if (line <= 8 && line >= 0) {
                XSSFRow row = sheet.getRow(line);              
                if (column < 10 && column >= 0) {
                    if (isFile(createFile(filePath))) {
                        FileOutputStream out = new FileOutputStream(createFile(filePath));
                        XSSFCell cell = row.getCell(column);
                        if (null == cell) {
                            cell = row.createCell(column);
                        }
                        cell.setCellValue(content);
                        book.write(out);
                        out.close();
                    } else {                    
                        System.out.println("文件: " + filePath + ", 正被使用, 请关闭! 程序执行终止...");
                        System.exit(-1);
                    }
                } else {
                	System.out.println("列索引越界...");	
                }                
            } else {            
            System.out.println("行索引越界...");
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
    }
    
    /**
     * 获取列的索引
     * 
     * @param sheetName
     *            sheet 名称
     * @param line
     *            需要获取的行
     * @param columnName
     *            列名
     * @return
     */
    public static int getColumnIndex(String filePath,String sheetName,int line, String columnName) {
        int index = -1;
        String[] title = readExcelRows(filePath,sheetName,line);

        if (null != title) {
            for (int i = 0; i < title.length; i++) {
                if (columnName.equals(title[i]))
                    index = i;
            }
        }
        return index;
    }
    
    /**
     * 设置单元格背景色
     * 
     * @param sheetName
     *            sheet 名称
     * @param titleRow
     *            列标题所在行号, 索引 0 开始
     * @param columnName
     *            需要获取列索引的列名称
     * @param line
     *            需要设置颜色单元格所在行
     * @param color
     *            需设置的颜色; 0: 红色; 1: 绿色; 2: 灰色; 3: 黄色; 4: 白色.
     * @throws IOException
     */
    public static void setCellBackgroundColor(String filePath,String sheetName,int line,int column,int color){
        XSSFWorkbook book = createExcelWorkBook(filePath);
        XSSFSheet sheet = book.getSheet(sheetName);

        
        XSSFRow row = sheet.getRow(line);
        XSSFCell cell = row.getCell(column);
        XSSFCellStyle old = cell.getCellStyle();
        XSSFCellStyle temp = book.createCellStyle();
        temp.cloneStyleFrom(old); // 拷贝旧的样式

        switch (color) {
        case 0:
            // 红色
            temp.setFillForegroundColor(IndexedColors.RED.getIndex());
            break;
        case 1:
            // 绿色
            temp.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            break;
        case 2:
            // 灰色
            temp.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
            break;
        case 3:
            // 黄色
            temp.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            break;
        case 4:
            // 白色
            temp.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            break;
        default:        	
            System.out.println("设定颜色参数 (color) 错误...");
            break;
        }

        temp.setFillPattern(CellStyle.SOLID_FOREGROUND);        
        cell.setCellStyle(temp);
        try {
        	if (isFile(createFile(filePath))) {
                FileOutputStream out = new FileOutputStream(createFile(filePath));
                book.write(out);
                out.close();
            } else {            
                System.out.println("文件: " + filePath + ", 正被使用, 请关闭! 程序执行终止...");
                System.exit(-1);
            }
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
    }


}
