class ArraySorts {
    /**
     * No.1 BubbleSort - O(n2)
     *
     * @param arr
     */
    public void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = arr.length - 1; j > i; j--) {
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                }
            }
        }
    }

    /**
     * No.2 InsertSort - O(n2)
     *
     * @param arr
     */
    public void insertSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int insertVal = arr[i]; //待插入数值
            int insertIndex = i - 1;
            while (insertIndex >= 0 && insertVal < arr[insertIndex]) { // 还没有找到要插入的地方
                arr[insertIndex + 1] = arr[insertIndex]; // 后移
                insertIndex--;
            }
            // 当退出while循环时，说明插入的地方已经找到了，位置在insertIndex + 1
            arr[insertIndex + 1] = insertVal;
        }
    }

    /**
     * No.3 ShellSort - O(nlogn ~ n2)
     *
     * @param arr
     */
    public static void shellSortMove(int[] arr) {
    // 移位法。
    // 核心与优化过的插入排序相似
        int gap = arr.length;
        while (gap != 1) { // 或for(int gap = arr.length/2; gap > 0; gap /= 2)
            gap /= 2;
            // 一轮排序：gap组，每组arr.length/gap单元
            // 从第gap个元素，逐个对其所在的组进行直接插入排序。
            for (int i = gap; i < arr.length; i++) {
                int j = i;
                int temp = arr[j];
                if (arr[j] < arr[j - gap]) {
                    while (j - gap >= 0 && temp < arr[j - gap]) {
                        arr[j] = arr[j - gap];
                        j -= gap;
                    }
                    // 退出循环时代表已经找到temp的位置
                    arr[j] = temp;
                }
            }
        }
    }

    public static void shellSortExchange(int[] arr) {
        // 交换法。由于交换的三句语段执行次数过多，故效率过低
        int gap = arr.length;
        while (gap != 1) { // 或for(int gap = arr.length/2; gap > 0; gap /= 2)
            gap /= 2;
            // 一轮排序：gap组，每组arr.length/gap单元
            for (int i = gap; i < arr.length; i++) {
                for (int j = i - gap; j >= 0; j -= gap) { // 每次j循环遍历了每组中的所有元素
                    // 步长为gap（组数）
                    // 如果当前这个元素大于加上gap之后的那个元素，证明需要交换
                    if (arr[j] > arr[j + gap]) {
                        int temp = arr[j];
                        arr[j] = arr[j + gap];
                        arr[j + gap] = temp;
                    }
                }
            }
        }
    }
}
