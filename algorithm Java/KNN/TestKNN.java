package KNN;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


// KNN算法测试类 


public class TestKNN {

	/** * 程序执行入口 * @param args */

	public static void main(String[] args) {
		int k=30;//参数k
		///////////////////////////////////////////////////////////////////////
		List<List<Double>> datas = new ArrayList<List<Double>>();//定义训练数据集
		//从Excel表中读入作为输入的数据集（用ExcelReader类）
		MyExcelReader excelReader = new MyExcelReader();
        try {           
            //读取Excel表格数据内容    
            InputStream is = new FileInputStream("F://KNN//xunlian.xls");
            datas= excelReader.readExcelContent(is);
            int n=datas.size();
            System.out.println("已获得Excel表格的内容:"+n+"条训练数据");           
        } catch (FileNotFoundException e) {      
            System.out.println("未找到指定路径的训练数据文件!");      
            e.printStackTrace();      
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////
        List<List<Double>> testDatas = new ArrayList<List<Double>>();//定义测试数据集
        try {           
            //读取Excel表格数据内容    
            InputStream istest = new FileInputStream("F://KNN//ceshi.xls");
            testDatas= excelReader.readExcelContentTest(istest);
            int ntest=testDatas.size();
            System.out.println("已获得Excel表格的内容:"+ntest+"条测试数据");           
        } catch (FileNotFoundException e) {      
            System.out.println("未找到指定路径的测试数据文件!");      
            e.printStackTrace();      
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////
        List<Integer> normalIndicats = new ArrayList<Integer>();
        try {           
            //读取Excel表格数据内容    
            InputStream istest = new FileInputStream("F://KNN//ceshi.xls");
            normalIndicats= excelReader.readExcelIndicat(istest);
            int ntest=testDatas.size();
            System.out.println("已获得Excel表格的内容:"+ntest+"条数据的正常指示标识");           
        } catch (FileNotFoundException e) {      
            System.out.println("未找到指定路径的测试数据文件!");      
            e.printStackTrace();      
        }
        ///////////////////////////////////////////////////////////////////////////////////////////
		//对模型评价时需要计算A,B,C,D四个模块
		int A,B,C,D;
		A=0;
		B=0;
		C=0;
		D=0;
        ///////////////////////////////////////////////////////////////////////////////////////////
        //将模型对检测数据的检测结果存入Excel表格中
            // 第一步，创建一个webbook，对应一个Excel文件
     		HSSFWorkbook wb = new HSSFWorkbook();
     		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
     		HSSFSheet sheet = wb.createSheet("sheet1");
     		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
     		HSSFRow row = sheet.createRow((int) 0);
     		// 第四步，创建单元格，并设置值表头 设置表头居中
     		HSSFCellStyle style = wb.createCellStyle();
     		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

     		HSSFCell cell = row.createCell(0);
     		cell.setCellValue("Index");
     		cell.setCellStyle(style);
     		cell = row.createCell(1);
     		cell.setCellValue("实际正常异常标识");
     		cell.setCellStyle(style);
     		cell = row.createCell(2);
     		cell.setCellValue("检测正常异常标识");
     		cell.setCellStyle(style);

        ////////////////////////////////////////////////////////////////////////////////////////////////
        KNN knn = new KNN();
        int index=606;//第一条测试数据的index
		for (int i = 0; i < testDatas.size(); i++) {
			List<Double> test = testDatas.get(i);//获得第i个测试数据
			System.out.println("测试元组: ");
			for (int j = 0; j < test.size(); j++) {
				System.out.print(test.get(j) + " ");
			}
			System.out.print("类别为: ");
			int detectIndicat=Math.round(Float.parseFloat((knn.knn(datas, test, k))));//该测试数据的检测正常异常标识
			System.out.println(detectIndicat);
			////////////////////////////////////////////////////////////////////////////////////////////
			// 第五步，写入表每行的数据
			row = sheet.createRow((int) i + 1);
			//创建单元格，并设置值
			row.createCell(0).setCellValue(index);
			int normalIndicat=normalIndicats.get(i);
			row.createCell(1).setCellValue(normalIndicat);
			row.createCell(2).setCellValue(detectIndicat);
			index++;
			//////////////////////////////////////////////////////////////////////////////////////////////
    		///////////////////////////////////////////////////////////////////////////////////
    		//对模型进行评价各模块数量计算
    			if (normalIndicat==1&&detectIndicat==1)
    				A++;
    			else if(normalIndicat==1&&detectIndicat==0)
    				B++;
    			else if(normalIndicat==0&&detectIndicat==1)
    				C++;
    			else
    				D++;
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////
		// 第六步，将文件存到指定位置
		try {
			FileOutputStream fout = new FileOutputStream("F://KNN//KNNDetectResult.xls");
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("检测结果已经存储到Excel表格F://KNN//KNNDetectResult.xls中");
		/////////////////////////////////////////////////////////////////////////////////////////////////
		//模型评价指标计算
		System.out.println("实际正常检测正常"+A+"个");
		System.out.println("实际正常检测异常"+B+"个");
		System.out.println("实际异常检测正常"+C+"个");
		System.out.println("实际异常检测异常"+D+"个");
		double mingzhong=D/(1.0*(B+D));
		System.out.println("命中率为"+mingzhong);
		double fugai=D/(1.0*(C+D));
		System.out.println("覆盖率为"+fugai);
		double accury=(A+D)/(1.0*(A+B+C+D));
		System.out.println("总体准确率为"+accury);
	}
}
