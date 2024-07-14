import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;

public class method1 {
    public static void main(String[] args) {
//下面是初始化的内容：
        String csvFilePath = "C:\\JavaProjects\\KeysToAPIs\\src\\singleKeyToAPIs.csv";
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入关键词个数：");
        int k = sc.nextInt();
        int [] keys = new int[k];//关键词数组
        LinkedHashSet<Integer>[] apiGroups = new LinkedHashSet[k];//与关键词数组对应的Hashset
        for (int i = 0; i < k; i++) {
            apiGroups[i] = new LinkedHashSet<>();
        }
        System.out.println("请输入用整数表示的关键词：");
        for(int i=0;i<k;i++) {
            keys[i] = sc.nextInt();//读取keys[i]
            //初始化与keys[i]关键词对应的Hashset
            int rowsToSkip = keys[i];//跳过keys[i]行
            try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFilePath)).build()) {
                // 跳过keys[i]行
                for (int j = 0; j < rowsToSkip; j++) {
                    reader.readNext();
                }
                // 读取后续行的数据
                String[] row = reader.readNext();
                // 添加元素到apiGroups
                for (int ix = 2; ix < row.length; ix++) {
                    int value = Integer.parseInt(row[ix]);
                    apiGroups[i].add(value);
                }
            } catch (IOException | CsvValidationException e) {
                throw new RuntimeException(e);
            }
        }

        //输出apiGroups
        System.out.println("apiGroups打印查看");
        for(int p=0;p<k;p++){
            System.out.println("关键词"+keys[p]+"对应的api组为：");
            Iterator<Integer> iterator1 = apiGroups[p].iterator();
            while(iterator1.hasNext()){
                System.out.println(iterator1.next());
            }
        }

        //数量定义
        //Scanner sc = new Scanner(System.in);
        int n = 1398;//结点个数
        double[][] pow = new double[n][n]; // 用于存储节点之间的权重，表达无向图
        // 初始化pow数组
        for (int i = 0; i < n; i++) {
            Arrays.fill(pow[i], Integer.MAX_VALUE); // 初始化距离为最大值
            pow[i][i] = 0; // 节点到自身的距离为0
        }

        String csvFilePath2 = "C:\\JavaProjects\\KeysToAPIs\\src\\corporation.csv";
        //合作次数的条数
        int m = 1096;//边的个数
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFilePath2)).build()) {
            reader.readNext();//跳过表头
            // 初始化pow数组
            for (int i = 0; i < m; i++) {
                String[] row = reader.readNext();
                int u = Integer.parseInt(row[0])-1,v=Integer.parseInt(row[1])-1;
                double w= weight(Integer.parseInt(row[2]));
                // 采用邻接矩阵存储结果
                pow[u][v] = Math.min(pow[u][v], w);
                pow[v][u] = Math.min(pow[v][u], w);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }


        //创建dp数组
        double[][] dp = new double[n][1 << k]; // 存储状态压缩DP的结果
        //创建routes数组
        @SuppressWarnings("unchecked")
        HashSet<Integer>[][] routes = new HashSet[n][1 << k];// 创建一个二维数组，每个元素都是一个 HashSet<Integer>,用来存储路径上的结点
//上面是初始化的内容：

//下面是调用循环体的内容：
        //定义优先队列，存放路径方案和对应权重，按照权重升序排列
        PriorityQueue<Pair> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(Pair::getSecond));
        // 用于存储组合的结果
        LinkedHashSet<Integer> currentCombination = new LinkedHashSet<>();
        // 开始递归遍历
        recursiveLoop(apiGroups, 0,currentCombination,n,k,pow,dp,routes,priorityQueue);
//上面是调用循环体的内容：

//下面是输出结果的内容：
        //定义输出的topn数量S
        int S = 10;
        int count = 0;
        System.out.println("权重最小的前"+S+"组api推荐");
        System.out.println("格式为[api1,api2,...,apin]：权重");
        while (!priorityQueue.isEmpty() && count < S) {
            Pair pair = priorityQueue.poll();
            System.out.println(pair.getFirst() + " : " + pair.getSecond());
            count++;
        }
//上面是输出结果的内容：
    }


//下面是循环体的内容：
    private static void recursiveLoop(LinkedHashSet<Integer>[] apiGroups, int currentIndex,LinkedHashSet<Integer> currentCombination,int n,int k,double[][] pow,double[][] dp,HashSet<Integer>[][] routes,PriorityQueue<Pair> priorityQueue) {
        // 如果当前索引已经超过数组的长度，说明已经完成了一种组合，输出结果
        if (currentIndex == apiGroups.length) {
            //输出此时的组合情况：
            System.out.println("此时的关键结点为：");
            Iterator<Integer> iterator0 = currentCombination.iterator();
            while(iterator0.hasNext()){
                System.out.println(iterator0.next());
            }

            // 初始化dp数组
            for (int i = 0; i < n; i++) {
                Arrays.fill(dp[i], Integer.MAX_VALUE); // 初始化状态DP为最大值
            }
            // 初始化routes数组，为每个元素创建一个新的 HashSet
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 1 << k; j++) {
                    routes[i][j] = new HashSet<>();
                }
            }
            //读取所有关键节点，初始化dp数组和routes数组
            int y = -1;// 用来记录最后一个关键点
            // 标记关键节点，并初始化状态关键节点的DP值和routes为结点本身
            Iterator<Integer> iterator = currentCombination.iterator();
            for (int i = 0; i < k; i++) {
                int x = iterator.next() - 1;
                dp[x][1 << i] = 0; // 以输入的关键点为根，二进制中对应关键节点位置为1的dp总权重为0
                routes[x][1 << i].add(x + 1);
                y = x; // 记录最后一个关键节点
            }

            // 使用状态压缩DP求解最小路径和
            // 遍历从单个节点开始，直到满足所有关键节点都连同的所有可能状态
            for (int s = 1; s < 1 << k; s++) {
                // 遍历以节点i为根节点，状态为s时的情况
                for (int i = 0; i < n; i++) {
                    // 遍历s二进制的所有子集，执行i的度数大于1时的操作
                    // 更新routes里的路径
                    for (int t = s & (s - 1); t > 0; t = (t - 1) & s) {
                        //dp[i][s] = Math.min(dp[i][s], dp[i][t] + dp[i][s ^ t]);
                        if (dp[i][t] + dp[i][s ^ t] < dp[i][s]) {
                            dp[i][s] = dp[i][t] + dp[i][s ^ t];
                            routes[i][s].addAll(routes[i][t]);
                            routes[i][s].addAll(routes[i][s ^ t]);
                        }
                    }
                }
                // 处理状态为s的情况下最短路径
                deal(s, n, dp, pow, routes);
            }
            System.out.print("斯坦纳树的权重为：");
            System.out.println(dp[y][(1 << k) - 1]); // 输出结果
            System.out.println("路径矩阵为：");
            for (int j = 0; j < 1<<k ; j++) {
                System.out.println("第" + j + "列路径");
                for (int i = 0; i < n; i++) {
                    System.out.print(routes[i][j] + "  ");
                }
                System.out.println();
            }
            System.out.print("斯坦纳树的路径为：");
            System.out.println(routes[y][(1 << k) - 1]);
            priorityQueue.add(new Pair(routes[y][(1 << k) - 1], dp[y][(1 << k) - 1]));
            return;
        }
        // 遍历当前 LinkedHashSet 中的元素，递归地处理下一个 LinkedHashSet
        for (Integer number : apiGroups[currentIndex]) {
            // 添加当前元素到当前组合
            currentCombination.add(number);
            // 递归处理下一个 LinkedHashSet
            recursiveLoop(apiGroups, currentIndex + 1, currentCombination, n, k, pow, dp, routes, priorityQueue);
            // 回溯，移除当前元素，以便尝试其他组合方式
            currentCombination.remove(number);
        }
    }
//上面是循环体的内容：



//下面是deal函数：
    private static void deal(int s, int n, double[][] dp, double[][] pow, HashSet<Integer>[][] routes) {
        // 建立优先队列，按照dp值升序排列
        PriorityQueue<double[]> pq = new PriorityQueue<>(Comparator.comparingDouble(o -> o[1]));
        boolean[] vis = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (dp[i][s] != Integer.MAX_VALUE) {
                pq.add(new double[]{i, dp[i][s]});
            }
        }
        // 使用Dijkstra算法求解最短路径，即求解以每个节点为顶点，满足状态s（包含对应的点）的权重和的最小值
        while (!pq.isEmpty()) {
            double[] tmp = pq.poll();//删除队列首元素并保存在tmp中
            // 如果该行表示的点已经访问，则跳过
            if (vis[(int) tmp[0]]) {
                continue;
            }
            //否则进行访问并设置为已访问
            vis[(int) tmp[0]] = true;
            //遍历所有与tmp对应点直接连接的点
            for (int i = 0; i < n; i++) {
                // 跳过所有不与tmp对应点相连的点和该点本身
                if (i == tmp[0] || pow[(int) tmp[0]][i] == Integer.MAX_VALUE) {
                    continue;
                }
                if (tmp[1] + pow[(int) tmp[0]][i] < dp[i][s]) {
                    //如果遍历到的点的dp值可以更新为更小的由tmp的dp值+两点间权重就更新其dp和routes
                    dp[i][s] = (tmp[1] + pow[(int) tmp[0]][i]);
                    routes[i][s].clear();
                    routes[i][s].addAll(routes[(int)tmp[0]][s]);
                    routes[i][s].add(i+1);
                    pq.add(new double[]{i, dp[i][s]});
                }
            }
        }
    }
//上面是deal函数：

//下面是weight函数：
    private static double weight(int x){
        return  100/(double)x;
    }
//上面是weight函数：
}


//下面是Pair类
//class Pair {
//    private HashSet<Integer> first;
//    private double second;
//
//    public Pair(HashSet<Integer> first, double second) {
//        this.first = new HashSet<>(first);  // 使用HashSet的拷贝以确保独立性
//        this.second = second;
//    }
//
//    public HashSet<Integer> getFirst() {
//        return new HashSet<>(first);  // 返回HashSet的拷贝以确保独立性
//    }
//
//    public double getSecond() {
//        return second;
//    }
//}
//上面是Pair类
