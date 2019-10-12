package com.wjq.gmall.manage;


import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws Exception {
        while(true){
            /*
             * 选择测试文件序号
             */

            System.out.println("请输入居民点的个数...输入0结束");

            Scanner sc = new Scanner(System.in);


             /*
             * 读取数据
             */

            int size = sc.nextInt();

            if(size==0) {
                break;
            }

            /*
             * 1 定义居民点坐标以及每个点对应的权重
             */
            int[] x = new int[size];
            int[] y = new int[size];
            double[] xweight = new double[size];
            double[] yweight = new double[size];
            for (int i = 0; i < size; i++) {


                xweight[i]=1;
                yweight[i]=1;
            }

            for (int i = 0; i <size ; i++) {
                System.out.println("第"+(i+1)+"个居民点...");
                x[i] = sc.nextInt();
                y[i] = sc.nextInt();
            }

            initarea(x, y, xweight, yweight);
            System.out.println();
        }
    }

    /**
     * 快速排序(同时将每个点对应的权值依次做了调整)
     *
     * @param attr  待排序数组（轴上点坐标）
     * @param weight  权值
     * @param low  待排序数组最低点
     * @param height  待排序数组最高点
     */
    public static void pxres(int[] attr, double[] weight, int low, int height){
        int temp = 0;
        double temp1 =0;
        int i = low;
        int j = height;

        if(low < height){
            temp = attr[low];
            temp1 = weight[low];

            while(i != j){
                while(j > i && attr[j] >= temp){
                    --j;
                }
                if(i < j){
                    attr[i] = attr[j];
                    weight[i] = weight[j];
                    ++i;
                }
                while(i < j && attr[i] < temp){
                    ++i;
                }
                if(i < j){
                    attr[j] = attr[i];
                    weight[j] = weight[i];
                    --j;
                }
            }

            attr[i] = temp;
            weight[i] = temp1;
            pxres(attr, weight, low, i - 1);
            pxres(attr, weight, i + 1, height);
        }
    }

    /**
     * 对每个轴求带权中位数对应的坐标
     * @param addr  轴上坐标
     * @param Weights  权重
     * @param zhou  X或Y轴标识
     * @return 带权中位数
     */
    public static int axis(int [] addr, double [] Weights, String zhou){

        /*
         * 对每个轴坐标进行快速排序，同时调整对应的权重
         */

        pxres(addr, Weights, 0, addr.length - 1);
        System.out.println("排序后的" + zhou + "轴坐标为：");
        for(int i = 0; i < addr.length; i++){
            System.out.print(addr[i] + " ");
        }
        System.out.println("\n排序后的" + zhou + "轴坐标对应的权值为：");
        for(int i = 0; i < Weights.length; i++){
            System.out.print(Weights[i] + " ");
        }

        /*
         * 3 所有居民点权值之和
         */

        double sumweight = 0;
        for(int i = 0; i < Weights.length; i++){
            sumweight += Weights[i];
        }
        System.out.println("\n所有居民点权值之和：" + sumweight);

        /*
         * 4 求x轴方向的带权中位数
         */

        double sum = 0;
        for(int i = 0; i < Weights.length; i++){
            sum += Weights[i];
            if(sum >= sumweight / 2){
                return addr[i];
            }
        }

        return 0;
    }

    /**
     * 求邮局坐标方法
     * @param Xaxis  x轴坐标点集
     * @param Yaxis  y轴坐标点集
     * @param XWeights  权重
     * @param YWeights  权重
     */
    public static void initarea(int[] Xaxis, int[] Yaxis, double [] XWeights, double[] YWeights){

        /*
         * 2 对x轴，y轴分别处理 (注意：对应轴排完序相对应的权值随着做对应调整，使其保持一一对应)
         * 2.1 对x轴坐标点进行快速排序
         */
        int px = axis(Xaxis, XWeights, "X");
        /*
         * 2.2 对y轴坐标点进行快速排序
         */
        int py = axis(Yaxis, YWeights, "Y");

        /*
         * 打印邮局位置
         */
        System.out.println("邮局位置为：（" + px + "," + py + ")");
    }
}
