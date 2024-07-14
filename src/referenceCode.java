import java.util.*;

public class referenceCode {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(), m = sc.nextInt(), k = sc.nextInt();
        long[][] pow = new long[n][n]; // 用于存储节点之间的权重，表达无向图
        long[][] dp = new long[n][1 << k]; // 存储状态压缩DP的结果
        @SuppressWarnings("unchecked")
        HashSet<Integer>[][] routes = new HashSet[n][1 << k];// 创建一个二维数组，每个元素都是一个 HashSet<Integer>,用来存储路径上的结点
        // 初始化routes数组，为每个元素创建一个新的 HashSet
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 1 << k; j++) {
                routes[i][j] = new HashSet<>();
            }
        }
        // 初始化pow和dp数组
        for (int i = 0; i < n; i++) {
            Arrays.fill(pow[i], Integer.MAX_VALUE); // 初始化距离为最大值
            pow[i][i] = 0; // 节点到自身的距离为0
            Arrays.fill(dp[i], Integer.MAX_VALUE); // 初始化状态DP为最大值
        }

        // 读取边的信息并记录节点之间的最小距离
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt() - 1, v = sc.nextInt() - 1, w = sc.nextInt();
            // 采用邻接矩阵存储结果
            pow[u][v] = Math.min(pow[u][v], w);
            pow[v][u] = Math.min(pow[v][u], w);
        }
        // 读取所有关键节点
        int y = -1;// 用来记录最后一个关键点
        // 标记关键节点，并初始化状态关键节点的DP值和routes为结点本身
        for (int i = 0; i < k; i++) {
            int x = sc.nextInt() - 1;
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
                    // dp[i][s] = Math.min(dp[i][s], dp[i][t] + dp[i][s ^ t]);
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
        System.out.print("斯坦纳树的路径为：");
        System.out.println(routes[y][(1 << k) - 1]);
        System.out.println("路径矩阵为：");
        for (int j = 0; j < 1 << k; j++) {
            System.out.println("第" + j + "列路径");
            for (int i = 0; i < n; i++) {
                System.out.print(routes[i][j] + "  ");
            }
            System.out.println();
        }
    }

    // 处理状态s下的最短路径情况
    private static void deal(int s, int n, long[][] dp, long[][] pow, HashSet<Integer>[][] routes) {
        // 建立优先队列，按照dp值升序排列
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(o -> o[1]));
        boolean[] vis = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (dp[i][s] != Integer.MAX_VALUE) {
                pq.add(new long[] { i, dp[i][s] });
            }
        }
        // 使用Dijkstra算法求解最短路径，即求解以每个节点为顶点，满足状态s（包含对应的点）的权重和的最小值
        while (!pq.isEmpty()) {
            long[] tmp = pq.poll();// 删除队列首元素并保存在tmp中
            // 如果该行表示的点已经访问，则跳过
            if (vis[(int) tmp[0]]) {
                continue;
            }
            // 否则进行访问并设置为已访问
            vis[(int) tmp[0]] = true;
            // 遍历所有与tmp对应点直接连接的点
            for (int i = 0; i < n; i++) {
                // 跳过所有不与tmp对应点相连的点和该点本身
                if (i == tmp[0] || pow[(int) tmp[0]][i] == Integer.MAX_VALUE) {
                    continue;
                }
                if (tmp[1] + pow[(int) tmp[0]][i] < dp[i][s]) {
                    // 如果遍历到的点的dp值可以更新为更小的由tmp的dp值+两点间权重就更新其dp和routes
                    dp[i][s] = (tmp[1] + pow[(int) tmp[0]][i]);
                    routes[i][s].clear();
                    routes[i][s].addAll(routes[(int) tmp[0]][s]);
                    routes[i][s].add(i + 1);
                    pq.add(new long[] { i, dp[i][s] });
                }
            }
        }
    }
}
