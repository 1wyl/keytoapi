文档说明：

src文件夹包含2个jar包，5个csv文件，7个java文件
1.java文件说明：
referenceCode.java是参考代码的改进版，可以实现显示路径。
method1.java是可以自动读取csv文件构建无向图和初始化dp，routes矩阵的文件，但还需要手动输入关键词组。
method1ForTest.java在method1.java的基础上实现了自动读取mashup2.csv文件进行测试，得到命中率和时间开销的数据。
KeyToAPIConverter是将api到key的映射转换为key到api映射的文件。
ApiCorporation是统计合作次数的文件。
MashupConverter是将mashup文件中的api和key转成对应的整数编号方便测试。
Main文件无具体作用。

2.csv文件说明：
mashup.csv是原始mashup文件。
filtered_apitable2.csv是过滤没出现过的api的新文件。
singleKeyToAPIs.csv是表示key到api映射的文件。
corporation.csv是表示不同api之间合作次数的文件。
mashup2.csv是将mashup.csv中api和key用其在filtered_apitable2.csv，singleKeyToAPIs.csv编号表示的文件。

3.jar包说明：
opencsv-5.9.jar是用于在Java中读取和写入CSV文件的库。它提供了一种简单而高效的方式来处理CSV数据，包括解析CSV内容和将数据写入CSV文件。
commons-lang3-3.14.0.jar是一个库，提供了一组用于常见编程任务的实用类和方法，这些任务未在核心Java API中提供。它增强了核心Java类的功能，提供了额外的实用工具。