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

public class knncompareK {

	public static void main(String[] args) {
		/**
		 * 针对不同的参数k 根据训练数据用KNN模型检测测试数据集中异常数据，并将校验结果的各评价指标存到Excel表格中输出 参数如下：
		 * 
		 * @param int
		 *            k 人为设定的参数k，很明显设定的k值小于200(设的过大检测出来的数据会都是正常的）
		 * @return 将结果存到Excel表格中输出
		 */
		///////////////////////////////////////////////////////////////////////
		// 人为设定参数k
		// 可以循环改变k值从而达到更好的检测效果
		///////////////////////////////////////////////////////////////////////
		List<List<Double>> datas = new ArrayList<List<Double>>();// 定义训练数据集
		// 从Excel表中读入作为输入的数据集（用ExcelReader类）
		MyExcelReader excelReader = new MyExcelReader();
		try {
			// 读取Excel表格数据内容
			InputStream is = new FileInputStream("F://5folder//KNN//xunlian05.xls");
			datas = excelReader.readExcelContent(is);
			int n = datas.size();
			System.out.println("已获得Excel表格的内容:" + n + "条训练数据");
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的训练数据文件!");
			e.printStackTrace();
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		List<List<Double>> testDatas = new ArrayList<List<Double>>();// 定义测试数据集
		try {
			// 读取Excel表格数据内容
			InputStream istest = new FileInputStream("F://5folder////KNN//ceshi05.xls");
			testDatas = excelReader.readExcelContentTest(istest);
			int ntest = testDatas.size();
			System.out.println("已获得Excel表格的内容:" + ntest + "条测试数据");
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的测试数据文件!");
			e.printStackTrace();
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		List<Integer> normalIndicats = new ArrayList<Integer>();
		try {
			// 读取Excel表格数据内容
			InputStream istest = new FileInputStream("F://5folder////KNN//ceshi05.xls");
			normalIndicats = excelReader.readExcelIndicat(istest);
			int ntest = testDatas.size();
			System.out.println("已获得Excel表格的内容:" + ntest + "条数据的正常指示标识");
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的测试数据文件!");
			e.printStackTrace();
		}
		//////////////////////////////////////////////////////////////////////////////////////////////////
		// 存入分析结果的Excel表格的表头
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
		cell.setCellValue("k");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("实际正检测正");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("实际正检测异");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("实际异检测正");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("实际异检测异");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("命中率");
		cell.setCellStyle(style);
		cell = row.createCell(6);
		cell.setCellValue("覆盖率");
		cell.setCellStyle(style);
		cell = row.createCell(7);
		cell.setCellValue("准确率");
		cell.setCellStyle(style);
		cell = row.createCell(8);
		cell.setCellValue("自定义指标");
		cell.setCellStyle(style);
		cell = row.createCell(9);
		cell.setCellValue("指标f1");
		cell.setCellStyle(style);
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// 设置参数k的范围为k=10:5:160,k的取值共计30=(155-10)/5+1个
		int ksize = 30;
		double[] A = new double[ksize];
		// 定义一个float类型的2维数组，实际正常，检测正常
		double[] B = new double[ksize];
		// 定义一个float类型的2维数组，实际正常，检测异常
		double[] C = new double[ksize];
		// 定义一个float类型的2维数组，实际异常，检测正常
		double[] D = new double[ksize];
		// 定义一个float类型的2维数组，实际异常，检测异常
		double[] mingzhong = new double[ksize];
		// 定义一个float类型的2维数组,命中率
		double[] fugai = new double[ksize];
		// 定义一个float类型的2维数组，覆盖率
		double[] accury = new double[ksize];
		// 定义一个float类型的2维数组，总体准确率
		double[] zonghe = new double[ksize];
		// 定义一个float类型的2维数组，我定义的一个指标
		double[] f1 = new double[ksize];
		// 定义一个float类型的2维数组，总体准确率
		//////////////////////////////////////////////////////////////////////////////////////////
		KNN knn = new KNN();
		int k = 10;// 参数k的值是人为设定的，认为k的值小于等于500
		while (k <= 155) {
			int p = k / 5 - 2;
			for (int i = 0; i < testDatas.size(); i++) {
				List<Double> test = testDatas.get(i);//获得第i个测试数据
				int detectIndicat=Math.round(Float.parseFloat((knn.knn(datas, test, k))));//该测试数据的检测正常异常标识
				int normalIndicat=normalIndicats.get(i);//该测试数据的实际正常异常标识
				//////////////////////////////////////////////////////////////////////////////////////////////
	    		///////////////////////////////////////////////////////////////////////////////////
	    		//对模型进行评价各模块数量计算
	    			if (normalIndicat==1&&detectIndicat==1)
	    				A[p]++;
	    			else if(normalIndicat==1&&detectIndicat==0)
	    				B[p]++;
	    			else if(normalIndicat==0&&detectIndicat==1)
	    				C[p]++;
	    			else
	    				D[p]++;
			}
			//////////////////////////////////////////////////////////////////////////////////////////////
				mingzhong[p] = D[p] / (1.0 * (B[p] + D[p]));
				fugai[p] = D[p] / (1.0 * (C[p] + D[p]));
				accury[p]= (A[p] + D[p]) / (1.0 * (A[p] + B[p] + C[p] + D[p]));
				zonghe[p] = 0.4 * mingzhong[p]+ 0.6 * fugai[p];
				f1[p]=2*mingzhong[p]*fugai[p]/(mingzhong[p]+fugai[p]);
				////////////////////////////////////////////////////////////////////////////////////
				// 第五步，不断写入表的实体数据
				row = sheet.createRow((int) (p ) + 1);
				// 创建单元格，并设置值
				row.createCell(0).setCellValue(k);
				row.createCell(1).setCellValue(A[p]);
				row.createCell(2).setCellValue(B[p]);
				row.createCell(3).setCellValue(C[p]);
				row.createCell(4).setCellValue(D[p]);
				row.createCell(5).setCellValue(mingzhong[p]);
				row.createCell(6).setCellValue(fugai[p]);
				row.createCell(7).setCellValue(accury[p]);
				row.createCell(8).setCellValue(zonghe[p]);
				row.createCell(9).setCellValue(f1[p]);
			k += 5;
		}

		// 第六步，将文件存到指定位置
		try {
			FileOutputStream fout = new FileOutputStream("F://5folder//KNN//KNNAssessResult05.xls");
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("不同参数k对应评价结果已经存储到Excel表格F://5folder//KNN//KNNAssessResult05.xls中");
	}
}
