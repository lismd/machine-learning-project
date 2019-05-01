//能够读取xunlian.xls和ceshi.xls
package KNN;
     
import java.io.IOException;      
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;      
     
import org.apache.poi.hssf.usermodel.HSSFCell;      
import org.apache.poi.hssf.usermodel.HSSFRow;      
import org.apache.poi.hssf.usermodel.HSSFSheet;      
import org.apache.poi.hssf.usermodel.HSSFWorkbook;      
import org.apache.poi.poifs.filesystem.POIFSFileSystem;      
     
/**    
 * 读取Excel表格的功能类        
 */     
public class MyExcelReader {      
    private POIFSFileSystem fs;      
    private HSSFWorkbook wb;      
    private HSSFSheet sheet;      
    private HSSFRow row;      
    /**    
     * 读取Excel表格表头的内容    
     * @param InputStream    
     * @return String[] 表头内容的数组    
     *     
     */     
    public String[] readExcelTitle(InputStream is) {      
        try {      
            fs = new POIFSFileSystem(is);      
            wb = new HSSFWorkbook(fs);      
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
        sheet = wb.getSheetAt(0);    
        row = sheet.getRow(0);      
        //标题总列数colNum，这里的表colNum=9(即总共有9列）      
        int colNum = row.getPhysicalNumberOfCells();      
        String[] title = new String[colNum];      
        for (int i=0; i<colNum; i++) {      
            title[i] = getStringCellValue(row.getCell(i)); 
        }      
        return title;      
    }      
          
    /**    
     * 读取训练数据内容    
     * @param InputStream    
     * @return List<List<Double>> 包含单元格数据内容的java对象序列    
     */     
    public List<List<Double>>  readExcelContent(InputStream is) {  
    	List<List<Double>> datas = new ArrayList<List<Double>>();      
        try {      
            fs = new POIFSFileSystem(is);      
            wb = new HSSFWorkbook(fs);      
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
        sheet = wb.getSheetAt(0);      
        //得到总行数rowNum      
        int rowNum = sheet.getLastRowNum();      
        row = sheet.getRow(0);      
        //int colNum = row.getPhysicalNumberOfCells();    这里的表colNum=9(即总共有9列）  
        //正文内容应该从第二行开始,第一行为表头的标题      
        for (int i = 1; i <=rowNum; i++) {      
            row = sheet.getRow(i);
            List<Double> d = new ArrayList<Double>(); 
            d.add(row.getCell(1).getNumericCellValue());
            d.add(row.getCell(2).getNumericCellValue());
            d.add(row.getCell(3).getNumericCellValue());
            d.add(row.getCell(4).getNumericCellValue());
            d.add(row.getCell(5).getNumericCellValue());
            d.add(row.getCell(6).getNumericCellValue());
            d.add(row.getCell(7).getNumericCellValue());
            d.add(row.getCell(8).getNumericCellValue());//
            datas.add(d);
        }      
        return datas;      
    }  
    
    /**    
     * 读取测试数据内容    
     * @param InputStream    
     * @return List<List<Double>> 包含单元格数据内容的java对象序列    
     */ 
    public List<List<Double>>  readExcelContentTest(InputStream is) {  
    	List<List<Double>> testdatas = new ArrayList<List<Double>>();      
        try {      
            fs = new POIFSFileSystem(is);      
            wb = new HSSFWorkbook(fs);      
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
        sheet = wb.getSheetAt(0);      
        //得到总行数rowNum      
        int rowNum = sheet.getLastRowNum();      
        row = sheet.getRow(0);      
        //int colNum = row.getPhysicalNumberOfCells();    这里的表colNum=9(即总共有9列）  
        //正文内容应该从第二行开始,第一行为表头的标题      
        for (int i = 1; i <=rowNum; i++) {      
            row = sheet.getRow(i);
            List<Double> d = new ArrayList<Double>(); 
            d.add(row.getCell(1).getNumericCellValue());
            d.add(row.getCell(2).getNumericCellValue());
            d.add(row.getCell(3).getNumericCellValue());
            d.add(row.getCell(4).getNumericCellValue());
            d.add(row.getCell(5).getNumericCellValue());
            d.add(row.getCell(6).getNumericCellValue());
            d.add(row.getCell(7).getNumericCellValue());
            testdatas.add(d);
        }      
        return testdatas;      
    }
    
    /**    
     * 读取Excel数据内容    
     * @param InputStream    
     * @return List<Integer> 包含其中一列单元格数据内容的java对象序列    
     */     
    public List<Integer>  readExcelIndicat(InputStream is) {  
    	List<Integer> normalIndicat = new ArrayList<Integer>();      
        try {      
            fs = new POIFSFileSystem(is);      
            wb = new HSSFWorkbook(fs);      
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
        sheet = wb.getSheetAt(0);      
        //得到总行数rowNum      
        int rowNum = sheet.getLastRowNum();      
        row = sheet.getRow(0);      
        //int colNum = row.getPhysicalNumberOfCells();    这里的表colNum=9(即总共有9列）  
        //正文内容应该从第二行开始,第一行为表头的标题      
        for (int i = 1; i <=rowNum; i++) {      
            row = sheet.getRow(i);
            normalIndicat.add((int) (row.getCell(8).getNumericCellValue()));
        }      
        return normalIndicat;      
    }      
    
    /**    
     * 获取单元格数据内容为字符串类型的数据    
     * @param cell Excel单元格    
     * @return String 单元格数据内容    
     */     
    private String getStringCellValue(HSSFCell cell) {      
        String strCell = "";      
        switch (cell.getCellType()) {      
        case HSSFCell.CELL_TYPE_STRING:      
            strCell = cell.getStringCellValue();      
            break;      
        case HSSFCell.CELL_TYPE_NUMERIC:      
            strCell = String.valueOf(cell.getNumericCellValue());  
            break;      
        case HSSFCell.CELL_TYPE_BOOLEAN:      
            strCell = String.valueOf(cell.getBooleanCellValue());      
            break;      
        case HSSFCell.CELL_TYPE_BLANK:      
            strCell = "";      
            break;      
        default:      
            strCell = "";      
            break;      
        }      
        if (strCell.equals("") || strCell == null) {      
            return "";      
        }        
        return strCell;      
    }      
          
  
}   
