import java.io.*;
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
        this.data = data;
        this.val = val;
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

    /**
     * 将传入node树所有node节点的赫夫曼编码求取并放入huffmanCodes集合
     *
     * @param code 路径：左子节点为0，右子节点为1
     */
    private void getCodesPackeged(String code, StringBuilder stringBuilder, Map<Byte, String> huffmanCodes) {
        StringBuilder stringBuilder2 = new StringBuilder(); // 用于拼接
        stringBuilder2.append(code);
        if (left != null || right != null) { // 处理非叶子节点
            if (left != null) {
                left.getCodesPackeged("0", stringBuilder2, huffmanCodes); // 向左递归
            }
            if (right != null) {
                right.getCodesPackeged("1", stringBuilder2, huffmanCodes); // 向右递归
            }
        } else { // 处理叶子节点
            huffmanCodes.put(getData(), stringBuilder2.toString());
        }
    }

    public void getCodes(Map<Byte, String> huffmanCodes) { // 根节点调用，且已经判空
        StringBuilder stringBuilder = new StringBuilder();
        getCodesPackeged("0", stringBuilder, huffmanCodes);
        getCodesPackeged("1", stringBuilder, huffmanCodes);
    }
}

class HuffmanTree {
    /**
     * @param contentByte 转换为Byte的待压缩数据
     */
    public static void createHuffmanTree(byte[] contentByte, Map<Byte, String> huffmanCodes) {
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
        nodes.get(0).getCodes(huffmanCodes);
    }
}


class HuffmanFile {
    private static byte[] contentOriginal = null;
    private static byte[] contentCoded = null;
    private static byte[] contentDecoded = null;
    private static Map<Byte, String> huffmanCodes = new HashMap<>();

    // 构造器、输出器部分省略
    public void setContentOriginal(String str) {
        contentOriginal = str.getBytes();
    }

    public static void zipFilePackeged(String srcFile, String dstFile) {
        /*
         * @param srcFile 源文件地址
         * @param dstFile 目标文件地址
         */
        FileInputStream is = null;
        FileOutputStream os = null;
        ObjectOutputStream oos = null;
        try {
            is = new FileInputStream(srcFile);
            contentOriginal = new byte[is.available()];
            is.read(contentOriginal);
            huffmanCompression();
            os = new FileOutputStream(dstFile);
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
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void unZipFilePackeged(String srcFile, String dstFile) {
        /*
         * @param srcFile 源文件地址
         * @param dstFile 目标文件地址
         */
        InputStream is = null;
        ObjectInputStream ois = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(srcFile);
            ois = new ObjectInputStream(is);
            contentCoded = (byte[]) ois.readObject();
            huffmanCodes = (Map<Byte, String>) ois.readObject();
            huffmanDecompression();
            os = new FileOutputStream(dstFile);
            os.write(contentOriginal);
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

    private static void huffmanCodesFormation() {
        HuffmanTree.createHuffmanTree(contentOriginal, huffmanCodes); // 生成赫夫曼编码表
    }

    private static void huffmanCompression() {
        if (huffmanCodes == null) {
            huffmanCodesFormation();
        }
        contentCoded = compressionPackeged(contentOriginal);
    }

    private static void huffmanDecompression() {
        contentDecoded = decompressionPackeged(contentCoded);
    }

    private static byte[] compressionPackeged(byte[] bytes) {
        /*
         * @param bytes 原始字符串对应的数组
         * @param huffmanCodes 赫夫曼编码表
         * @return 返回编码后的byte数组（补码方式存储）
         */
        // 1、将byte转成赫夫曼编码对应的字符串
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(huffmanCodes.get(b));
        }
        // 2、将字符串转为byte数组
        int len = (stringBuilder.length() + 7) / 8;
        byte[] huffmanCodeFile = new byte[len];
        for (int i = 0; i < stringBuilder.length(); i += 8) { // 1byte = 8bit
            String strByte;
            if (i + 8 > stringBuilder.length()) {
                strByte = stringBuilder.substring(i);
            } else {
                strByte = stringBuilder.substring(i, i + 8);
            }
            huffmanCodeFile[i / 8] = (byte) Integer.parseInt(strByte, 2);
        }
        return huffmanCodeFile;
    }

    private static byte[] decompressionPackeged(byte[] huffmanCodeFile) {
        /*
         * @param huffmanCodes编码表
         * @param huffmanCodeBytes
         */
        StringBuilder strBuilder1 = new StringBuilder(); // 1、构成二进制字符串
        for (int i = 0; i < huffmanCodeFile.length; i++) {
            Byte b = huffmanCodeFile[i];
            boolean flag = (i == huffmanCodeFile.length - 1);
            strBuilder1.append(byteToBitString(!flag, b));
        }
        // 因为要反向查询，所以要调换Key与Value
        Map<String, Byte> map = new HashMap<>();
        for (Map.Entry<Byte, String> entry : huffmanCodes.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        // 查询解码
        List<Byte> list = new ArrayList<>();
        int preIndex = 0, i = 0;
        while (i < strBuilder1.length()) {
            i++;
            String key = strBuilder1.substring(preIndex, i);
            Byte b = map.get(key);
            if (b != null) {
                preIndex = i;
                list.add(b);
            }
        }
        byte[] output = new byte[list.size()];
        for (i = 0; i < output.length; i++) {
            output[i] = list.get(i);
        }
        return output;
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
}
