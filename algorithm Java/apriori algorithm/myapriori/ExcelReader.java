package myapriori;
      
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
public class ExcelReader {      
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
        //标题总列数colNum，这里的表colNum=18(即总共有18列）      
        int colNum = row.getPhysicalNumberOfCells();      
        String[] title = new String[colNum];      
        for (int i=0; i<colNum; i++) {      
            title[i] = getStringCellValue(row.getCell(i)); 
        }      
        return title;      
    }      
          
    /**    
     * 读取Excel数据内容    
     * @param InputStream    
     * @return List<NData> 包含单元格数据内容的java对象序列    
     */     
    public List<NData>  readExcelContent(InputStream is) {  
    	ArrayList<NData> datas = new ArrayList<NData>();// 定义函数输出         
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
        //int colNum = row.getPhysicalNumberOfCells();    这里的表colNum=18(即总共有18列）  
        //正文内容应该从第二行开始,第一行为表头的标题      
        for (int i = 1; i <=rowNum; i++) {      
            row = sheet.getRow(i);
            NData d=new NData();
            d.setA(row.getCell(0).getStringCellValue());
            d.setB(row.getCell(1).getStringCellValue());
            d.setC(row.getCell(2).getStringCellValue());
            d.setD(row.getCell(3).getStringCellValue());
            d.setE(row.getCell(4).getStringCellValue());
            d.setF(row.getCell(5).getStringCellValue());
            d.setG(row.getCell(6).getStringCellValue());
            d.setH(row.getCell(7).getStringCellValue());
            d.setI(row.getCell(8).getStringCellValue());
            d.setJ(row.getCell(9).getStringCellValue());
            d.setK(row.getCell(10).getStringCellValue());
            d.setL(row.getCell(11).getStringCellValue());
            d.setM(row.getCell(12).getStringCellValue());
            d.setN(row.getCell(13).getStringCellValue());
            d.setO(row.getCell(14).getStringCellValue());
            d.setP(row.getCell(15).getStringCellValue());
            d.setQ(row.getCell(16).getStringCellValue());
            d.setIndex((int) row.getCell(17).getNumericCellValue());
            datas.add(d);
        }      
        return datas;      
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
