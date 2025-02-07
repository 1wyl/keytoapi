## 一、问题描述

使用PW数据集中的mashup.csv文件构建一个无向图，该无向图中的点表示API，边表示两个API之间曾经有过合作构建mashup，边的权重根据两个API之间的合作次数进行函数映射表示兼容性。api.csv文件表明每个API有一到多个关键词可以描述。

目标是使用算法，对于输入特定的关键词，可以返回n组API，每组API表示的点在无向图中是连通的，n组API按照权重的降序排列。

同时，还要使用命中率指标和时间开销指标对算法的有效性和效率进行评价。接着在原先方法的基础上进行改进，再次使用两个指标进行评价，观察其有效性和效率是否改善。

## 二、设计思路

### 数据处理

1. 根据mashup表和api表生成新的apiTable，里面包含mashup中出现过的API并给它们整数编号。
2. 根据apiTable中API到关键词的映射关系得到新的表keyToAPIs，这是关键词到API的映射，同时也对关键词进行编号。
3. 根据mashup得到一张corporation表，里面反映了不同API之间的合作次数。
4. 最后将mashup中的API和关键词全都对应成其编号以便进行测试。

### 代码实现部分

1. 输入一组编号表示的关键词（数字和关键词的对应关系见singleKeyToAPIs.csv）。
2. 对每个关键词从singleKeyToAPIs里找到一组API。
3. 递归执行k层循环，遍历API的组合：每次循环需要重新初始化dp和routes，pow不变。
4. 将每个API组合作为输入的k个关键节点，执行算法体。
5. 将每个组合运算的结果：权值和路径按照权值大小保存在优先队列中。
6. 设置topS数量s=10，然后输出前十条路径，也就是推荐的API。
7. 命中率检测：需要将mashup中添加一列numAPIs用数字表示的API。
8. 随机从每行的numAPIs抽出几个关键词作为输入，进行total=100次输入，统计mashup中实际的API在前十条数字路径中被包括的次数count，count/total就是命中率。

## 三、更多内容

1. 更多详细内容请参考 [我的github仓库](https://github.com/1wyl/keytoapi/blob/main/%E8%AF%BE%E7%A8%8B%E6%8A%A5%E5%91%8A.pdf)
