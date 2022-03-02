import java.util.*;

class HuffmanNode implements Comparable<HuffmanNode> {
    private int val;
    private Byte data;
    private HuffmanNode left;
    private HuffmanNode right;

    public HuffmanNode(int val) {
        setVal(val);
    }

    public HuffmanNode(Byte data, int val) {
        setData(data);
        setVal(val);
    }

    public Byte getData() {
        return data;
    }

    public void setData(Byte data) {
        this.data = data;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public HuffmanNode getLeft() {
        return this.left;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    public HuffmanNode getRight() {
        return this.right;
    }

    public int compareTo(HuffmanNode o) { // 排序接口：正数为此节点大，负数为此节点小
        // 创建接口以兼容Collections.sort()方法
        return getVal() - o.getVal();
    }
}

class HuffmanTree {
    private HuffmanNode root;

    public HuffmanTree(){}
    public HuffmanTree(HuffmanNode root) {
        setRoot(root);
    }
    public HuffmanNode getRoot() {
        return root;
    }

    public void setRoot(HuffmanNode root) {
        this.root = root;
    }
    /**
     * @param contentByte 转换为Byte的待压缩数据
     */
    private void createHuffmanTree(byte[] contentByte) {
        List<HuffmanNode> nodes = new ArrayList<>();
        // 遍历统计，注意重复问题
        Map<Byte, Integer> charCounter = new HashMap<>();
        for (byte b : contentByte) {
            if (!charCounter.containsKey(b)) { // Map中还没有这个字符
                charCounter.put(b, 1);
            } else {
                charCounter.put(b, charCounter.get(b) + 1);
            }
        }
        // 将Map转为Arraylist<Node>
        for (Map.Entry<Byte, Integer> entry : charCounter.entrySet()) {
            nodes.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        while (nodes.size() > 1) {
            Collections.sort(nodes); // 从小到大排序
            HuffmanNode leftNode = nodes.get(0); //（1）取出权值最小的两个二叉树
            HuffmanNode rightNode = nodes.get(1);
            HuffmanNode parent = new HuffmanNode(leftNode.getVal() + rightNode.getVal()); // （2）构建新二叉树
            parent.setLeft(leftNode);
            parent.setRight(rightNode);
            nodes.remove(leftNode); // (3)删掉两个节点
            nodes.remove(rightNode);
            nodes.add(parent);
        }
        setRoot(nodes.get(0));
    }

    static Map<Byte, String> huffmanCodes = new HashMap<Byte, String>;

    // 主程序调用方法 getCodes(root); 最终结果在huffmanCodes中
    private Map<Byte, String> getCodes() {
        StringBuilder stringBuilder = new StringBuilder();
        if (getRoot() == null) {
            return null;
        } else {
            getCodes(getRoot().getLeft(), "0", stringBuilder);
            getCodes(getRoot().getRight(), "1", stringBuilder);
        }
        return huffmanCodes;
    }

    /**
     * @function:将传入node树所有node节点的赫夫曼编码求取并放入huffmanCodes集合
     * @param code 路径：左子节点为0，右子节点为1
     * @return
     */
    private static void getCodes(HuffmanNode node, String code, StringBuilder stringBuilder) {
        StringBuilder stringBuilder2 = new StringBuilder(); // 用于拼接
        stringBuilder2.append(code);
        if (node != null) { // 不处理空节点
            if (node == null) { // 处理非叶子节点
                getCodes(node.left, "0", stringBuilder2); // 向左递归
                getCodes(node.right, "1", stringBuilder2); // 向右递归
            } else { // 处理叶子节点
                huffmanCodes.put(node.data, stringBuilder2.toString());
            }
        }
    }

    private static byte[] compression(byte[] bytes, Map<Char, String> huffmanCodes) {
        /*
         * @param bytes 原始字符串对应的数组
         * @param huffmanCodes 赫夫曼编码表
         * @return 返回编码后的byte数组（补码方式存储）
         */
        // 1、将byte转成赫夫曼编码对应的字符串
        StringBuilder stringBuilder = new StringBuilder;
        for (byte b : bytes) {
            stringBuilder.append(huffmanCodes.get(b));
        }
        // 2、将字符串转为byte数组
        int len = (stringBuilder.length() + 7) / 8;
        byte[] huffmanCodeBytes = new byte[len];
        for (int i = 0; i < stringBuilder.length(); i += 8) { // 1byte = 8bit
            String strByte;
            if (i + 8 > stringBuilder.length()) {
                strByte = stringBuilder.sbuString(i);
            } else {
                strByte = stringBuilder.sbuString(i, i + 8);
            }
            huffmanCodeBytes[i / 8] = (byte) Integer.parseInt(strByte, 2);
        }
        return huffmanCodeBytes;
    }

    private static String byteToBitString(boolean flag, byte b) {
        int temp = b; // 将b转换为int
        // 问题：（1）正数位数太少不足8位，需要补位；（2）负数符号位在第32位，需要截取。
        if (flag) {
            temp |= 256; // 按位或补到九位
        }
        String str = Integer.toBinaryString(temp); // 返回的是32位二进制补码
        if (flag) {
            return str.substring(str.length() - 8);
        } else {
            return str;
        }
    }

    private static byte[] decompression(Map<Byte, String> huffmanCodes, byte[] huffmanCodeBytes) {
        /*
         * @param huffmanCodes编码表
         * @param huffmanCodeBytes
         */
        StringBuilder strBuilder1 = new StringBuilder; // 1、构成二进制字符串
        for (int i = 0; i < huffmanBytes.length; i++) {
            Byte b = huffmanBytes[i];
            boolean flag = (i == huffmanBytes.length - 1);
            strBuilder1.append(byteToBitString(!flag, b));
        }
        // 因为要反向查询，所以要调换Key与Value
        Map<String, Byte> map = new HashMap<String, Byte>();
        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        // 查询解码
        List<Byte> list = new ArrayList<>();
        int preIndex = 0, i = 0;
        while (i < strBuilder1.length()) {
            i++;
            String key = stringBuilder1.subString(preIndex, i);
            Byte b = map.get(key);
            if (b != null) {
                preIndex = i;
                list.add(b);
            }
        }
        byte[] output = new byte[list.size()];
        for (int i = 0; i < output.length; i++) {
            b[i] = list.get(i);
        }
    }


}


class HuffmanFile {
    byte[] contentOriginal = null;
    byte[] contentCoded = null;
    Map<Byte, String> huffmanCodes = null;

    // 构造器、输出器部分省略
    private contentOriginal(String str) {
        contentOriginal = str.getbyte();
    }

    private static void huffmanCodesFormation() {
        List<HuffmanNode> nodes = getNodes(contentOriginal); // 构建节点List
        HuffmanNode huffmanTreeRoot = createHuffmanTree(nodes); // 创建赫夫曼树
        huffmanCodes = getCodes(huffmanTreeRoot); // 生成赫夫曼编码表
    }

    private static void huffmanCompression() {
        if (huffmanCodes == null) {
            huffmanCodesFormation();
        }
        contentCoded = compression(contentOriginal, huffmanCodes);
    }

    private static void huffmanDecompression() {
        contentOriginal = decompression(huffmanCodes, contentCoded);
    }

    public static void zipFile(String srcFile, String dstFile) {
        /*
         * @param srcFile 源文件地址
         * @param dstFile 目标文件地址
         */
        FileInputStream is = null;
        OutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            is = new FileInputStream(srcFile);
            contentOriginal = new byte[is.available()];
            is.read(contentOriginal);
            huffmanCompression();
            os = new OutputStream(dstFile);
            oos = new ObjectOutputStream(os);
            oos.writeObject(contentCoded); // 写入文件编码结果
            oos.writeObject(huffmanCodes); // 写入编码表
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                is.close();
                oos.close();
                os.close();
catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void unZipFile(String srcFile, String dstFile) {
        /*
         * @param srcFile 源文件地址
         * @param dstFile 目标文件地址
         */
        InputStream is = null;
        ObjectInputStream ois = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(srcFile);
            ois = new ObjectOutputStream(os);
            contentCoded = (byte[]) ois.readObject();
            huffmanCodes = (Map<Byte, String>) ois.readObject();
            huffmanDecompression();
            os = new FileOutputStream(dstFile);
            os.write(huffmanOriginal);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                os.close();
                ois.close();
                is.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
