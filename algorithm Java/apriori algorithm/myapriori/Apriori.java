package myapriori;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Apriori {

	// private final static int SUPPORT = 806; // 支持度阈值(对应支持度20%）
	private final static int SUPPORT = 604; // 支持度阈值(对应支持度15%）
	//private final static int SUPPORT = 403; // 支持度阈值(对应支持度10%）
	private final static double CONFIDENCE = 0.65; // 置信度阈值
	private final static double totalnumber = 4028; // 数据数量

	private final static String ITEM_SPLIT = ";"; // 项之间的分隔符
	private final static String CON = "=>"; // 项之间的分隔符

	private final static List<String> transList = new ArrayList<String>(); // 所有交易(对应交易数据库D）

	static {// 初始化交易记录
		///////////////////////////////////////////////////////////////////////
		List<NData> datas = new ArrayList<NData>();// 从Excel表格中读取的数据
		// 从Excel表中读入作为输入的数据集（用ExcelReader类）
		try {
			// 读取Excel表格数据内容
			ExcelReader excelReader = new ExcelReader();
			InputStream is = new FileInputStream("F://associativeAnalysis//b5datanew.xls");
			datas = excelReader.readExcelContent(is);
			int n = datas.size();
			System.out.println(n + "条数据");
			System.out.println("已获得Excel表格的内容:");
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		// 该步骤将从Excel中读取的数据序列中的数据转换为特定的字符串形式依次存放在transList序列中
		for (int i = 0; i < datas.size(); i++) {
			String tran = datas.get(i).getA() + ";" + datas.get(i).getB() + ";" + datas.get(i).getC() + ";"
					+ datas.get(i).getD() + ";" + datas.get(i).getE() + ";" + datas.get(i).getF() + ";"
					+ datas.get(i).getG() + ";" + datas.get(i).getH() + ";" + datas.get(i).getI() + ";"
					+ datas.get(i).getJ() + ";" + datas.get(i).getK() + ";" + datas.get(i).getL() + ";"
					+ datas.get(i).getM() + ";" + datas.get(i).getN() + ";" + datas.get(i).getO() + ";"
					+ datas.get(i).getP() + ";" + datas.get(i).getQ() + ";";
			transList.add(tran);
		}

	}

	// 得到所有的频繁集
	public Map<String, Integer> getFC() {
		Map<String, Integer> frequentCollectionMap = new HashMap<String, Integer>();// 所有的频繁集

		frequentCollectionMap.putAll(getItem1FC());// 将 1-频繁集加入频繁集Map

		Map<String, Integer> itemkFcMap = new HashMap<String, Integer>();// 定义
																			// k-频繁集
		itemkFcMap.putAll(getItem1FC());// 将 1-频繁集加入 k-频繁集Map
		while (itemkFcMap != null && itemkFcMap.size() != 0) {
			Map<String, Integer> candidateCollection = getCandidateCollection(itemkFcMap);// 由k-1频繁集产生k-候选集
			Set<String> ccKeySet = candidateCollection.keySet();// 获得k-候选集的所有的key的序列

			// 对候选集项进行累加计数
			for (String trans : transList) {
				for (String candidate : ccKeySet) {
					boolean flag = true;// 用来判断交易中是否出现该候选项，如果出现，计数加1
					String[] candidateItems = candidate.split(ITEM_SPLIT);// 去掉分隔符ITEM_SPLIT，并得到字符串数组
					for (String candidateItem : candidateItems) {
						if (trans.indexOf(candidateItem + ITEM_SPLIT) == -1) {// 即判断candidateItem是否在trans交易中
							flag = false;
							break;
						}
					}
					if (flag) {
						Integer count = candidateCollection.get(candidate);
						candidateCollection.put(candidate, count + 1);// 表示有count条交易有candidate
					}
				}
			}

			// 从候选集中找到符合支持度的频繁集项
			itemkFcMap.clear();
			for (String candidate : ccKeySet) {
				Integer count = candidateCollection.get(candidate);
				if (count >= SUPPORT) {
					itemkFcMap.put(candidate, count);
				}
			}

			// 添加新生成的k频繁集到所有频繁集
			frequentCollectionMap.putAll(itemkFcMap);

		}

		return frequentCollectionMap;
	}

	// 从k-1频繁集得到k候选集
	private Map<String, Integer> getCandidateCollection(Map<String, Integer> itemkFcMap) {
		Map<String, Integer> candidateCollection = new HashMap<String, Integer>();// 定义k候选集
		Set<String> itemkSet1 = itemkFcMap.keySet();// 得到k-1频繁集的所有key
		Set<String> itemkSet2 = itemkFcMap.keySet();// 得到k-1频繁集的所有key

		for (String itemk1 : itemkSet1) {
			for (String itemk2 : itemkSet2) {
				// 进行连接
				String[] tmp1 = itemk1.split(ITEM_SPLIT);// 去掉分隔符ITEM_SPLIT，并得到字符串数组
				String[] tmp2 = itemk2.split(ITEM_SPLIT);// 去掉分隔符ITEM_SPLIT，并得到字符串数组

				String c = "";// 定义候选的 k频繁集
				if (tmp1.length == 1) {
					if (tmp1[0].compareTo(tmp2[0]) < 0) {
						c = tmp1[0] + ITEM_SPLIT + tmp2[0] + ITEM_SPLIT;
					}
				} else {
					boolean flag = true;
					for (int i = 0; i < tmp1.length - 1; i++) {
						if (!tmp1[i].equals(tmp2[i])) {
							flag = false;
							break;
						}
					}
					if (flag && (tmp1[tmp1.length - 1].compareTo(tmp2[tmp2.length - 1]) < 0)) {// 不等时返回负数
						c = itemk1 + tmp2[tmp2.length - 1] + ITEM_SPLIT;// 注意：得到的c的key形如"2;3;5;"
					}
				}

				// 进行剪枝:判断c是否为非频繁候选
				boolean hasInfrequentSubSet = false;// 定义非频繁候选指示
				if (!c.equals("")) {
					String[] tmpC = c.split(ITEM_SPLIT);// 字符串c去掉分隔符ITEM_SPLIT，并得到字符串数组tmpC
					for (int i = 0; i < tmpC.length; i++) {
						String subC = "";
						for (int j = 0; j < tmpC.length; j++) {
							if (i != j) {
								subC = subC + tmpC[j] + ITEM_SPLIT;
							}
						}
						if (itemkFcMap.get(subC) == null) {// 判断subC是否在k-1频繁集itemkFcMap中，如果没在，则c为非频繁集
							hasInfrequentSubSet = true;
							break;
						}
					}
				} else {
					hasInfrequentSubSet = true;
				}

				if (!hasInfrequentSubSet) {
					candidateCollection.put(c, 0);// 如果c为频繁集，则将其加入k频繁集中
				}
			}
		}

		return candidateCollection;
	}

	// 得到频繁1项集（map包括其对应的 1-频繁项，和其计数）
	private Map<String, Integer> getItem1FC() {
		Map<String, Integer> sItem1FcMap = new HashMap<String, Integer>();
		Map<String, Integer> rItem1FcMap = new HashMap<String, Integer>();// 频繁1项集

		for (String trans : transList) {
			String[] items = trans.split(ITEM_SPLIT);// 字符串trans去掉分隔符ITEM_SPLIT，并得到字符串数组
														// 如2;4;变成了2和4两个String类型数据
			for (String item : items) {
				Integer count = sItem1FcMap.get(item + ITEM_SPLIT);// 获得key为item
																	// +
																	// ITEM_SPLIT对应的值
				if (count == null) {
					sItem1FcMap.put(item + ITEM_SPLIT, 1);// 将key为item +
															// ITEM_SPLIT对应的值赋为1
				} else {
					sItem1FcMap.put(item + ITEM_SPLIT, count + 1);// 将key为item +
																	// ITEM_SPLIT赋值为count+1
				}
			}
		}

		Set<String> keySet = sItem1FcMap.keySet();
		for (String key : keySet) {
			Integer count = sItem1FcMap.get(key);
			if (count >= SUPPORT) {
				rItem1FcMap.put(key, count);
			}
		}
		return rItem1FcMap;
	}

	// 由频繁项集产生关联规则
	// 对于每个频繁项集l，产生其所有非空真子集；
	public Map<String, Double> getRelationRules(Map<String, Integer> frequentCollectionMap) {
		Map<String, Double> relationRules = new HashMap<String, Double>();
		Set<String> keySet = frequentCollectionMap.keySet();
		for (String key : keySet) {
			double countAll = frequentCollectionMap.get(key);// 获得键值为key的频繁项的个数
			String[] keyItems = key.split(ITEM_SPLIT);// 字符串key去掉分隔符ITEM_SPLIT，并得到字符串数组
			if (keyItems.length > 1) {
				List<String> source = new ArrayList<String>();
				Collections.addAll(source, keyItems);// 将keyItems里的字符串依次插入到source字符串序列中
				List<List<String>> result = new ArrayList<List<String>>();

				buildSubSet(source, result);// 获得source的所有非空子集放入result中
				// 例如{1；2；5；}的非空真子集有{1；2；}，{5；}，{1；5；}，{2；}，{2；5；}，{1；}共6个
				// List<String> source
				// 满足：source[0]="1;",source[1]="2;",source[2]="5;"

				for (List<String> itemList : result) {
					if (itemList.size() < source.size()) {// 只处理真子集
						List<String> otherList = new ArrayList<String>();
						for (String sourceItem : source) {
							if (!itemList.contains(sourceItem)) {
								otherList.add(sourceItem);
							}
						} // 举例来说，如果itemList是{1；2；}，那么otherList就是{5；}
						String reasonStr = "";// 前置
						String resultStr = "";// 结果
						for (String item : itemList) {
							reasonStr = reasonStr + item + ITEM_SPLIT;
						}
						for (String item : otherList) {
							resultStr = resultStr + item + ITEM_SPLIT;
						}

						double countReason = frequentCollectionMap.get(reasonStr);
						double itemConfidence = countAll / countReason;// 计算置信度

						if (itemConfidence >= CONFIDENCE ) {
							String rule = reasonStr + CON + resultStr;
							relationRules.put(rule, itemConfidence);
						}
					}
				}
			}
		}

		return relationRules;
	}

	// 获得source的所有非空子集放入result中
	private void buildSubSet(List<String> sourceSet, List<List<String>> result) {
		// 仅有一个元素时，递归终止。此时非空子集仅为其自身，所以直接添加到result中
		if (sourceSet.size() == 1) {
			List<String> set = new ArrayList<String>();
			set.add(sourceSet.get(0));// 此种情况下sourceSet只有一个元素
			result.add(set);
		} else if (sourceSet.size() > 1) {
			// 当有n个元素时，递归求出前n-1个子集，存于result中
			buildSubSet(sourceSet.subList(0, sourceSet.size() - 1), result);// 将sourceSet中除了最后一个位置的数据转换类型后放入result
			int size = result.size();// 求出此时result的长度，用于后面的追加第n个元素时计数
			// 把第n个元素加入到集合中
			List<String> single = new ArrayList<String>();
			single.add(sourceSet.get(sourceSet.size() - 1));// 将sourceSet的最后一个位置的数据放到single中
			result.add(single);// 将sourceSet中除了最后一个位置的数据转化为List<String>类型加入result序列中
			// 在保留前面的n-1子集的情况下，把第n个元素分别加到前n个子集中，并把新的集加入到result中;
			// 为保留原有n-1的子集，所以需要先对其进行复制
			List<String> clone;
			for (int i = 0; i < size; i++) {
				clone = new ArrayList<String>();
				for (String str : result.get(i)) {
					clone.add(str);
				}
				clone.add(sourceSet.get(sourceSet.size() - 1));

				result.add(clone);
			}
		}
	}

	// 将rule字符串 H3;E2;M3;=>B1; 转换为 B1;E2;M3;H3; 的形式
	// 这需要提取各字符串，然后将其按照字典顺序排序，再生成
	public static String changeStr(String rule) {
		String[] strs = rule.split(CON);
		String rulestr = "";
		for (String str : strs) {
			rulestr += str;
		}

		String[] items = rulestr.split(ITEM_SPLIT);
		Arrays.sort(items);
		String c = "";
		for (String item : items) {
			c += item + ";";
		}
		return c;
	}

	public static void main(String[] args) {
		System.out.println("///////////////////////////////////////////////////////");
		Apriori apriori = new Apriori();
		Map<String, Integer> frequentCollectionMap = apriori.getFC();
		System.out.println("----------------频繁集" + "----------------");
		Set<String> fcKeySet = frequentCollectionMap.keySet();
		for (String fcKey : fcKeySet) {
			System.out.println(fcKey + "  :  " + frequentCollectionMap.get(fcKey));
		}
		Map<String, Double> relationRulesMap = apriori.getRelationRules(frequentCollectionMap);
		System.out.println("----------------关联规则" + relationRulesMap.size() + "条" + "----------------");
		///////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////
		// 将得到的关联规则存入Excel表格中
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
		cell.setCellValue("关联规则");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("支持度数量");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("支持度");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("置信度");
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue("提升度");
		cell.setCellStyle(style);
		cell = row.createCell(5);
		cell.setCellValue("规则后件");
		cell.setCellStyle(style);
		////////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////////////////
		// 第五步，写入表的实体数据
		Set<String> rrKeySet = relationRulesMap.keySet();
		int i = 0;
		for (String rrKey : rrKeySet) {
			System.out.println(rrKey + "  :  " + relationRulesMap.get(rrKey));// 打印关联规则和其置信度
			row = sheet.createRow((int) i + 1);
			// 创建单元格，并设置值
			row.createCell(0).setCellValue(rrKey);
			// 将 H3;E2;M3;=>B1; 类型的字符串转换为 B1;E2;M3;H3;类型
			// 这需要提取各字符串，然后将其按照字典顺序排序，再生成
			row.createCell(1).setCellValue(frequentCollectionMap.get(changeStr(rrKey)));// 支持度数量表示
			row.createCell(2).setCellValue(1.0 * frequentCollectionMap.get(changeStr(rrKey)) / totalnumber);// 支持度
			row.createCell(3).setCellValue(relationRulesMap.get(rrKey));// 置信度
			// 计算提升度Lift
			String[] strs = rrKey.split(CON);
			String conseq = strs[1];
			row.createCell(4)
					.setCellValue(relationRulesMap.get(rrKey) * totalnumber / frequentCollectionMap.get(conseq));// 支持度
			row.createCell(5).setCellValue(conseq);// 规则后件
			i++;
		}
		////////////////////////////////////////////////////////////////////////////////////////////////
		// 第六步，将文件存到指定位置
		try {
			FileOutputStream fout = new FileOutputStream("F:/associativeAnalysis/ruleResults.xls");
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("检测结果已经存储到Excel表格F:/associativeAnalysis/ruleResults.xls中");
	}
}
