import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TenCommonlyUsedAlgorithms {

    /**
     * No.1 二分查找（非递归）
     * 对*升序*数列进行二分查找
     *
     * @param arr    待查找的数组
     * @param target 需要查找的数
     * @return 返回对应下标值，-1代表未找到
     */
    public static int noRecursionBinarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (arr[mid] == target) {
                return target;
            }
            if (arr[mid] > target) {
                right = mid - 1; // 向左查找
            } else if (arr[mid] < target) {
                left = mid + 1; // 向右查找
            }
        }
        return -1;
    }

    /**
     * No.2 分治算法——汉诺塔问题
     *
     * @param num 需要移动的塔的层数
     * @param a   源地址
     * @param b   中间借助地址
     * @param c   目标地址
     */
    public static void hanoiTower(int num, char a, char b, char c) {
        if (num == 1) {
            System.out.println("No.1 " + a + "->" + c);
        } else {
            hanoiTower(num - 1, a, c, b);
            System.out.println("No." + num + " " + a + "->" + c);
            hanoiTower(num - 1, b, a, c);
        }
    }

    /**
     * No.3 动态规划——01背包问题
     *
     * @param weightLimit
     * @param weight
     * @param value
     */
    public static void dynamicPlanning01(int weightLimit, int[] weight, int[] value) {
        int[] maxValue = new int[weightLimit + 1];
        int[][] itemRecord = new int[weight.length + 1][weightLimit + 1];
        for (int i = 1; i <= weight.length; i++) {
            for (int k = weight[i - 1]; k <= weightLimit; k += weight[i - 1]) {
                int[] tempMaxValue = new int[weightLimit + 1];
                for (int j = 1; j < weight[i - 1]; j++) {
                    tempMaxValue[j] = maxValue[j];
                }
                for (int j = weight[i - 1]; j <= weightLimit; j++) {
                    tempMaxValue[j] = Math.max(maxValue[j], maxValue[j - weight[i - 1]] + value[i - 1]);
                    if (maxValue[j] >= maxValue[j - weight[i - 1]] + value[i - 1]) {
                        tempMaxValue[j] = maxValue[j];
                    } else {
                        tempMaxValue[j] = maxValue[j - weight[i - 1]] + value[i - 1];
                        itemRecord[i][j] += 1;
                    }
                }
                maxValue = tempMaxValue;
            }
        }
        System.out.println("maxValue = " + maxValue[weight.length]);
        int row = weight.length, column = weightLimit;
        while (row > 0 && column > 0) {
            if (itemRecord[row][column] == 0) {
                row -= 1;
            } else {
                System.out.println("放" + itemRecord[row][column] + "件" + row + "号商品");
                column -= itemRecord[row][column] * weight[row - 1];
            }
        }
    }

    /**
     * No.4 Part.1 KMP算法（字符串比对）
     *
     * @param str
     * @return
     */
    public static int[] kmpNext(String str) { // 用于获得部分匹配值的方法
        int[] next = new int[str.length()];
        next[0] = 0;
        for (int i = 1, j = 0; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(j)) {
                j++;
            } else { // 当 str.charAt(i) ！= str.charAt(j) 时需要一直向前找，直到满足str.charAt(i) == str.charAt(j)
                while (j > 0 && str.charAt(i) != str.charAt(j)) {
                    j = next[j - 1];
                }
            }
            next[i] = j;
        }
        return next;
    }

    /**
     * No.4 Part.1 KMP算法的最长公共前缀数组
     *
     * @param str1
     * @param str2
     * @return
     */
    public static int kmpAlgorithm(String str1, String str2) {
        int[] next = kmpNext(str2);
        for (int p1 = 0, p2 = 0; p1 < str1.length(); p1++) {
            if (str1.charAt(p1) == str2.charAt(p2)) {
                if (p2 == str2.length() - 1) { // 全长都匹配成功了 return p1 - p2; } else { // 没匹配完
                    p2++;
                }
            } else {
                while (p2 != 0 && str1.charAt(p1) != str2.charAt(p2)) {
                    p2 = next[p2 - 1];
                }
            }
        }
        return -1;
    }

    /**
     * * @param cityTable 存放电台对应信息的集合，结构为<电台名，HashSet<能覆盖的城市>> * @return 选取的电台集合
     */
    public static List<String> greedyAlgorithm(HashMap<String, HashSet<String>> cityTable) {
        HashSet<String> allAreas = new HashSet<>(); // 需要覆盖的所有地区
        for (String key : cityTable.keySet()) { // 放入所有城市
            allAreas.addAll(cityTable.get(key));
        }
        List<String> selectedCity = new ArrayList<>(); // 存放选择的电台组合
        HashSet<String> newCovered = new HashSet<>(); // 存放能新覆盖的地区
        String maxKey = null; // 保存一次循环中最大覆盖数的电台，若不为空则置入selectedCity
        while (allAreas.size() != 0) {
            maxKey = null;
            for (String key : cityTable.keySet()) { // 取出了当前广播站可以覆盖的所有地区
                HashSet<String> areas = cityTable.get(key);
                newCovered.clear();
                newCovered.addAll(areas);
                newCovered.retainAll(allAreas); // 取交集
                if (newCovered.size() > 0 && (maxKey == null || newCovered.size() > cityTable.get(maxKey).size())) {
                    maxKey = key; // 贪婪算法的核心：每次选择最优的。
                }
            }
            if (maxKey != null) {
                selectedCity.add(maxKey);
                allAreas.removeAll(cityTable.get(maxKey));
            }
        }
        return selectedCity;
    }
}
