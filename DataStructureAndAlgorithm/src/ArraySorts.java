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
    public void shellSortMove(int[] arr) {
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

    public void shellSortExchange(int[] arr) {
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

    public void quickSort(int[] arr) {
        quickSortMethod(arr, 0, arr.length - 1);
    }

    private void quickSortMethod(int[] arr, int left, int right) { // 需要获取左右索引的范围
        int l = left, r = right, temp = 0;
        int pivot = arr[(left + right) / 2];
        // 循环目的：为把pivot小的值放到左边，大的值放到右边
        while (l < r) {
            while (arr[l] < pivot) { // 循环目的：在左边找一个比pivot大的
                l++;
            }
            while (arr[r] > pivot) { // 循环目的：在左边找一个比pivot大的
                r--;
            } // 上两个循环不可以判断等号
            if (l >= r) { // 按照要求找不到了一对可以交换的数字了
                break;
            }
            temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            if (arr[l] == pivot) {
                r--;
            }
            if (arr[r] == pivot) {
                l++;
            }
        }
        // 递归部分
        if (l == r) {
            l++;
            r--;
        }
        if (left < r) {
            quickSortMethod(arr, left, r);
        }
        if (right > l) {
            quickSortMethod(arr, l, right);
        }
    }

    public void mergeSort(int[] arr) {
        mergeSortMethod(arr, 0, arr.length - 1, new int[arr.length]);
    }

    /*
     * @param left 左边有序序列的初始索引
     * @param mid 右边有序数列的初始索引
     * @param right 右索引
     * @param temp 中转数组
     */
    private void mergeSortMethod(int[] arr, int left, int right, int[] temp) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSortMethod(arr, left, mid, temp);
            mergeSortMethod(arr, mid + 1, right, temp);
            // 2 合并部分
            int i = left; // 左边有序序列的初始索引
            int j = mid + 1; // 右边有序序列的初始索引
            int tmp = 0;
            // 2.1 左右两边（有序）的数据按规则合并直到有一方剩余
            while (i <= mid && j <= right) {
                if (arr[i] <= arr[j]) {
                    temp[tmp] = arr[i];
                    i++;
                } else {
                    temp[tmp] = arr[j];
                    j++;
                }
                tmp++;
            }
            // 2.2 把剩余的数填充进去
            while (i <= mid) {
                temp[tmp] = arr[i];
                i++;
                tmp++;
            }
            while (j <= right) {
                temp[tmp] = arr[j];
                j++;
                tmp++;
            }
            // 2.3 拷贝
            for (i = left; i <= right; i++) {
                arr[i] = temp[i - left];
            }
        }
    }

    /**
     * 基数排序（桶排序）
     *
     * @param arr
     */
    public void radixSort(int[] arr) {
        // 第一轮
        int[][] bucket = new int[10][arr.length]; // 十个桶
        int[] bucketIndex = new int[10]; // 每个桶中实际放了多少数据
        // 得到最大的位数
        int max = arr[0];
        for (int i : arr) {
            max = Math.max(max, i);
        }
        int maxlength = (max + "").length();
        for (int i = 0; i < maxlength; i++) {
            // 放数据
            for (int j : arr) {
                int div = i * 10;
                int temp = j / div % 10; // 取出对应位数
                bucket[temp][bucketIndex[temp]] = j;
                bucketIndex[temp]++;
            }
            int index = 0;
            for (int k = 0; k < 10; k++) { // 遍历10个桶
                for (int l = 0; l < bucketIndex[k]; l++) {
                    arr[index] = bucket[k][l];
                    index++;
                }
                bucketIndex[k] = 0;
            }
        }
    }

    public void heapSort(int[] arr) { // 升序排列构建大顶堆
        int temp = 0;
        for (int i = arr.length / 2 - 1; i >= 0; i--) { // 调整生成大顶堆
            adjustHeap(arr, i, arr.length);
        }
        for (int i = arr.length - 1; i > 0; i--) {
            temp = arr[i];
            arr[i] = arr[0];
            arr[0] = temp;
            adjustHeap(arr, 0, i);
        }
    }

    /**
     * @param arr    待操着数组
     * @param length 数组有效长度（去除了已经堆排序好的部分）
     * @param i      表示调整目标的非叶子节点在数组中的索引
     * @return 返回调整了i号非叶子节点及其所属的整个二叉树的结果
     */
    private void adjustHeap(int[] arr, int i, int length) {
        int temp = arr[i]; // 保存当前元素的值
        for (int k = i * 2 + 1; k < length; k = k * 2 + 1) { // 处理左子节点
            if (k + 1 < length) {
                if (arr[k] < arr[k + 1]) {
                    k++; // 指向右子节点
                }
            }
            if (arr[k] > temp) { // 把较大的值赋给当前节点，并调整指针以备下一步循环
                arr[i] = arr[k];
                i = k;
            } else { // 因为调用adjustHeap时下面已经在之前的调用中处理好了
                break;
            }
        } // 退出for循环时，这一棵树的最大值处于初始参数i处
        arr[i] = temp;
    }
}
