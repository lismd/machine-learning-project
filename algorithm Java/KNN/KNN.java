package KNN;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

//KNN算法主体类 
public class KNN {
	/** * 设置优先级队列的比较函数，距离越大，优先级越高 */
	private Comparator<KNNNode> comparator = new Comparator<KNNNode>() {
		public int compare(KNNNode o1, KNNNode o2) {
			if (o1.getDistance() >= o2.getDistance()) {
				return -1;
			} else {
				return 1;
			}
		}
	};

	/** * 计算测试元组与训练元组之前的距离 * @param d1 测试元组* @param d2 训练元组 * @return 距离值 */

	public static double calDistance(List<Double> d1, List<Double> d2) {
		double distance = 0.00;
		for (int i = 0; i < d1.size(); i++) {
			distance += (d1.get(i) - d2.get(i)) * (d1.get(i) - d2.get(i));
		}
		return distance;
	}

	/**
	 * * 执行KNN算法，获取测试元组的类别 * @param datas 训练数据集 * @param testData 测试元组 * @param
	 * k 设定的K值 * @return 测试元组的类别
	 */

	public String knn(List<List<Double>> datas, List<Double> testData, int k) {
		PriorityQueue<KNNNode> pq = new PriorityQueue<KNNNode>(k, comparator);
		for (int i = 0; i < k; i++) {
			List<Double> currData = datas.get(i);//提取datas的第index条数据放在currData中
			String c = currData.get(currData.size() - 1).toString();//将该数据正常异常标识提取出来（String）类型
			KNNNode node = new KNNNode(i, calDistance(testData, currData), c);//第i条数据跟测试数据testData相比的距离
			pq.add(node);
		}
		for (int i = k; i < datas.size(); i++) {
			List<Double> t = datas.get(i);//提取datas的第i条数据放在currData中
			double distance = calDistance(testData, t);//计算第i条数据跟测试数据testData相比的距离
			//distance与k个距离中最大的比较
			KNNNode top = pq.peek();
			if (top.getDistance() > distance) {
				pq.remove();
				pq.add(new KNNNode(i, distance, t.get(t.size() - 1).toString()));
			}
		}
		return getMostClass(pq);
	}

	/** * 获取所得到的k个最近邻元组的多数类 * @param pq 存储k个最近近邻元组的优先级队列* @return 多数类的名称 */

	private String getMostClass(PriorityQueue<KNNNode> pq) {
		Map<String, Integer> classCount = new HashMap<String, Integer>();
		int pqsize = pq.size();
		for (int i = 0; i < pqsize; i++) {
			KNNNode node = pq.remove();
			String c = node.getC();//获得正常异常指示标识
			if (classCount.containsKey(c)) {
				classCount.put(c, classCount.get(c) + 1);//相应标示的计数加1
			} else {
				classCount.put(c, 1);
			}
		}
		int maxIndex = -1;
		int maxCount = 0;
		//比较正常和异常的计数，哪个大就返回相应指示
		Object[] classes = classCount.keySet().toArray();
		for (int i = 0; i < classes.length; i++) {
			if (classCount.get(classes[i]) > maxCount) {
				maxIndex = i;
				maxCount = classCount.get(classes[i]);
			}
		}
		return classes[maxIndex].toString();
	}
}
