import java.awt.*;
import java.util.*;
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
     * No.4 Part.2 KMP算法的最长公共前缀数组
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
     * No.5 贪心算法
     *
     * @param cityTable 存放电台对应信息的集合，结构为<电台名，HashSet<能覆盖的城市>>
     * @return 选取的电台集合
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

    /**
     * No.6 普利姆算法（边缘配套方法省略）
     *
     * @param graph
     * @param v
     */
    public static final int INF = Integer.MAX_VALUE;

    /* 图类的定义
     * class MapGraph { // 数据库/图
     *     int verxs; // 节点个数
     *     char[] data; // 存放顶点名/值
     *     int[][] weight; // 存放权值邻接矩阵，无连接的两点或自身可用INF表示 // 注意！！！在kruskal算法中，自身与自身的权值在邻接矩阵中记为0
     * }
     */
    public void prim(MapGraph graph, int v) { // 创建最小生成树,v为起始顶点的编号
        MapGraph mapGraph = new MapGraph();
        mapGraph = graph; // 不改变输入对象
        int[] visited = new int[mapGraph.verxs]; // 记录已访问过的节点
        visited[v] = 1;
        for (int k = 1; k < mapGraph.verxs; k++) { // 必定循环verxs-1次
            int h1 = -1, h2 = -1; //用于记录两个目标顶点的下标
            int minWeight = 10000; // 遍历中找到的最小权值
            for (int i = 0; i < mapGraph.verxs; i++) {
                for (int j = 0; j < mapGraph.verxs; j++) {
                    if (visited[i] == 1 && visited[j] != 1 && minWeight > mapGraph.weight[i][j]) {
                        h1 = i; // 访问过的值 h2 = j; // 未访问过的值
                        minWeight = mapGraph.weight[i][j];
                    }
                }
            }
            visited[h2] = 1; // 找到h1到h2之间的通路，新连接点为j，新加入权值为mapGraph.weight[h1][h2]; // 根据需求进行结果处理
        }
    }

    /**
     * No.7 克鲁斯卡尔算法（边缘配套方法省略）
     *
     * @param graph
     * @param v
     */
    public void kruskal(MapGraph graph, int v) {
        MapGraph mapGraph = new MapGraph();
        mapGraph = graph; // 不改变输入对象
        int index = 0; // 用于索引结果数组
        int[] ends = new int[mapGraph.verxs]; // 用于保存顶点的终点，0表示游离点
        EdgeData[] output = new EdgeData[mapGraph.verxs - 1]; // 用于保存最终选取的边
        EdgeData[] edges = mapGraph.getEdges(); // 提取出所有路径放到数组里
        sortEdges(edges); // 按权值从小到大排序
        int i = 0;
        while (i < edges.length && index < mapGraph.verxs - 1) {
            int p1 = getPosition(edges[i].start, mapGraph.data);
            int p2 = getPosition(edges[i].end, mapGraph.data); // 获取顶点
            int end1 = getEnd(ends, p1);
            int end2 = getEnd(ends, p2); // 获取终点
            if (end1 != end2) { // 不构成回路
                output[index++] = edges[i];
                ends[end1] = end2;
            }
            i++;
        }
    }

    private void sortEdges(EdgeData[] edges) { // 冒泡排序
        for (int i = 0; i < edges.length - 1; i++) {
            for (int j = 0; j < edges.length - 1 - i; j++) {
                if (edges[j].weight > edges[j + 1].weight) {
                    EdgeData tmp = edges[j];
                    edges[j] = edges[j + 1];
                    edges[j + 1] = tmp;
                }
            }
        }
    }

    private int getPosition(char ch, char[] chs) { // 返回顶点的下标
        for (int i = 0; i < chs.length; i++) {
            if (ch == chs[i]) {
                return i;
            }
        }
        return -1;
    }

    private EdgeData[] getEdges(char[] data, int[][] weight) { // 获取邻接矩阵的所有边
        int edgeNum = 0;
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = i + 1; j < data.length; j++) {
                if (weight[i][j] != INF) {
                    edgeNum++;
                }
            }
        }
        EdgeData[] edges = new EdgeData[edgeNum];
        int index = 0;
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = i + 1; j < data.length; j++) {
                if (weight[i][j] != INF) {
                    edges[index++] = new EdgeData(data[i], data[j], weight[i][j]);
                }
            }
        }
        return edges;
    }

    /**
     * 获取下标i顶点的终点
     *
     * @param ends 一个在遍历中逐步形成的终点表格。游离点终点为0
     * @param i    需要寻找的下标
     * @return 下标对应的终点
     */
    private int getEnd(int[] ends, int i) {
        while (ends[i] != 0) {
            i = ends[i];
        } // 这里用到重要算法：并查集
        return i;
    }

    /**
     * No.9 弗洛伊德算法
     */
    class Graph { // 图
        private char[] vertex; // 存放顶点的数组
        private int[][] distance; // 存放距离表
        private int[][] preVertex; // 存放前驱顶点表
        int Nan = 65535;    // 构造函数、显示函数 省略    // 初始化前驱顶点表
        for(
        int i = 0;
        i<length;i++) // error

        {    //
            Arrays.fill(preVertex[i], i);
        }

        public void floyd() {
            int len = 0; // 用于保存距离
            for (int k = 0; k < distance.length; k++) { // 一层：遍历中间顶点
                for (int i = 0; i < distance.length; i++) { // 二层：遍历出发顶点
                    for (int j = 0; j < distance.length; j++) { // 三层：遍历结束顶点
                        len = distance[i][k] + distance[k][j];
                        if (len < distance[i][j]) {
                            distance[i][j] = len;
                            preVertex[i][j] = preVertex[k][j];
                        }
                    }
                }
            }
        }
    }

    /**
     * No.10 马踏棋盘核心算法
     *
     * @param chessboard 棋盘
     * @param row        当前位置索引
     * @param column
     * @param step       当前是第几步，初始就是第一步，最大为滴64步
     */
    public static void traversalChessboard(int[][] chessboard, int row, int column, int step) {
        chessboard[row][column] = step;
        visited[row * X + column] = true;
        ArrayList<Point> points = next(new Point(column, row));
        for (Point p : points) {
            if (!visited[p.y * X + p.x]) {
                traversalChessboard(chessboard, p.y, p.x, step + 1);
            }
        }
        if (step < X * Y && !finished) { // step<X*Y的情况1：棋盘没有走完卡死了；情况2：棋盘走完在回溯
            visited[row * X + column] = false;
            chessboard[row][column] = 0;
        } else {
            finished = true;
        }
    }

    private static int X = 8; // 棋盘的列
    private static int Y = 8;
    private static boolean visited[] = new boolean[X * Y]; // 标记棋盘的各个位置有没有被访问
    private static boolean finished = false; // 标记是否成功遍历
    private int[][] chessboard = new int[Y][X];
    private Point startPoint = new Point(1, 1); // 调用方法

    public static ArrayList<Point> next(Point curPoint) {    // 方法用于返回下一步可行的地址
        ArrayList<Point> points = new ArrayList<Point>();
        Point p = new Point();
        if ((p.x = curPoint.x - 2) >= 0 && (p.y = curPoint.y - 1) >= 0) {
            points.add(new Point(p));
        }
        if ((p.x = curPoint.x - 2) >= 0 && (p.y = curPoint.y + 1) < Y) {
            points.add(new Point(p));
        }
        if ((p.x = curPoint.x + 2) < X && (p.y = curPoint.y - 1) >= 0) {
            points.add(new Point(p));
        }
        if ((p.x = curPoint.x + 2) < X && (p.y = curPoint.y + 1) < Y) {
            points.add(new Point(p));
        }
        if ((p.x = curPoint.x - 1) >= 0 && (p.y = curPoint.y - 2) >= 0) {
            points.add(new Point(p));
        }
        if ((p.x = curPoint.x - 1) >= 0 && (p.y = curPoint.y + 2) < Y) {
            points.add(new Point(p));
        }
        if ((p.x = curPoint.x + 1) < X && (p.y = curPoint.y - 2) >= 0) {
            points.add(new Point(p));
        }
        if ((p.x = curPoint.x + 1) < X && (p.y = curPoint.y + 2) < Y) {
            points.add(new Point(p));
        }
        return points;
    }
//

    /**
     * 此方法有一个问题：无法获得所有可行解
     * 贪心优化算法：对next内顶点下一步的数目进行非递减排序
     *
     * @param points
     * @return
     */
    public static void nextSort(ArrayList<Point> points) {
        points.sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {            // 获取o1的下一步的数量
                int size1 = next(o1).size();
                int size2 = next(o2).size();
                return size1 - size2;
            }
        });
    }

}

class MapGraph {
    int verxs;
    char[] data;
    int[][] weight;

    public EdgeData[] getEdges() {
        return new EdgeData[10]; // 省略
    }
}

class EdgeData { // 定义一条边
    char start;
    char end;
    int weight;

    public EdgeData(char start, char end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }
}

class Graph {
    private char[] vertex;
    private int[][] weight; // 邻接矩阵，用65535表示没有连接或自身    // 构造略
    VisitedVertex visitedVertex;

    public void dijkstra(int start) {
        visitedVertex = new VisitedVertex(vertex.length, start);
        int nextVertex = start;
        while (nextVertex != -1) {
            update(nextVertex);
            nextVertex = visitedVertex.updateTarget();
        }
    }

    private void update(int index) { // 更新下标为index的点与所有周围点的关系
        int len = 0;
        visitedVertex.alreadyVisited[index] = 1;
        for (int j = 0; j < weight[index].length; j++) {
            len = visitedVertex.distance[index] + weight[index][j]; // 新的可能距离
            if (visitedVertex.alreadyVisited[j] == 0 && visitedVertex.distance[j] > len) {
                visitedVertex.distance[j] = len;
                visitedVertex.preVertex[j] = index;
            }
        }
    }
}

class VisitedVertex { // 迪杰斯特拉
    public int[] alreadyVisited; // 1为已访问过，0为未访问
    public int[] preVertex; // 存储前一顶点下标
    public int[] distance; // 存储最小距离

    public VisitedVertex(int length, int start) {
        alreadyVisited = new int[length];
        preVertex = new int[length];
        distance = new int[length];
        Arrays.fill(distance, 65535); // 用于表达还未遍历到
        distance[start] = 0;
    }

    public int updateTarget() {
        int min = 65535;
        int minaddr = -1;
        for (int i = 0; i < distance.length; i++) {
            if (distance[i] < min && alreadyVisited[i] == 0) {
                min = distance[i];
                minaddr = i;
            }
        }
        return minaddr;
    }
}





