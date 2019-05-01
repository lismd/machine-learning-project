package myapriori;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dealString {
	public static void main(String[] args) {
		String tran="A1"+";"+"B1"+";";
		String t="A1;B1;";
		System.out.println(tran);
		System.out.println(t);
		System.out.println(tran==t);
		///////////////////////////////////////////////////////////////////////
		List<NData> datas = new ArrayList<NData>();// 从Excel表格中读取的数据
		// 从Excel表中读入作为输入的数据集（用ExcelReader类）
		try {
			// 读取Excel表格数据内容
			ExcelReader excelReader = new ExcelReader();
			InputStream is = new FileInputStream("F://associativeAnalysis//b5datas.xls");
			datas = excelReader.readExcelContent(is);
			int n = datas.size();
			System.out.println(n + "条数据");
			System.out.println("已获得Excel表格的内容:");
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		}
		//////////////////////////////////////////////////////////////////////////////
		List<String> S = new ArrayList<String>();
		////////////////////////////////////////////////////////////////////////////////////////////////
		// 该步骤将从Excel中读取的数据序列中的数据转换为特定的字符串形式依次存放在transList序列中
		for (int i = 0; i < 10; i++) {
			String temp = datas.get(i).getA() + ";" + datas.get(i).getB() + ";" + datas.get(i).getC() + ";"
					+ datas.get(i).getD() + ";" + datas.get(i).getE() + ";" + datas.get(i).getF() + ";"
					+ datas.get(i).getG() + ";" + datas.get(i).getH() + ";" + datas.get(i).getI() + ";"
					+ datas.get(i).getJ() + ";" + datas.get(i).getK() + ";" + datas.get(i).getL() + ";"
					+ datas.get(i).getM() + ";" + datas.get(i).getN() + ";" + datas.get(i).getO() + ";"
					+ datas.get(i).getP() + ";" + datas.get(i).getQ() + ";";
			
			System.out.println(temp);
			S.add(temp);
		}
		System.out.println("////////////////////////////////////////////////////////");
		for (int i = 0; i < 10; i++) {
			System.out.println(S.get(i));
		}
		//////////////////////////////////////////////////////////////////////////////////////////////
		String b="M1;A1;B1;P2;";
		//b.indexOf(ch)
		System.out.println(b.contains("A"));
		String[] items = b.split(";");
		for (String item : items) {
			System.out.println(item);
		}
		Arrays.sort(items);
		System.out.println("排序后:");
		for (String item : items) {
			System.out.println(item);
		}
		String c="";
		for (String item : items) {
			c+=item+";";
		}
		System.out.println(c);
		System.out.println(c.length());
		if(!(c=="A1;B1;M1;P2;")){
			System.out.println("good:true");
		}
		////////////////////////////////////////////////////////////////////////
		StringBuffer r=new StringBuffer();
		for (String item : items) {
			r.append(item+";");
		}
		System.out.println(r);
		if(r.toString()=="A1;B1;M1;P2;"){
			System.out.println("r:true");
		}
		///////////////////////////////////////////////////////////////////
		String rule="M1;A1;B1;=>P2;";
		String str="";
		String[] myitems = rule.split("=>");
		for (String item : myitems) {
			str+=item;
			System.out.println(str);
		}
		System.out.println(str);
	}
}
