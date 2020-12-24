package com.atguigu.springcloud.controller;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Auther: hzp
 * @Date: 2020/12/15 10:18
 * @Description:
 */
@RestController
@Slf4j
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.application.name}")
    private String instanceName;
    private final DiscoveryClient discoveryClient;
    @Autowired
    public PaymentController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }
    @GetMapping
    public String hello() {
        return "Hello,Zookeeper.";
    }
    @GetMapping("/services")
    public List<String> serviceUrl() {
        List<ServiceInstance> list = discoveryClient.getInstances(instanceName);
        List<String> services = new ArrayList<>();
        if (list != null && list.size() > 0 ) {
            list.forEach(serviceInstance -> {
                services.add(serviceInstance.getUri().toString());
            });
        }
        return services;
    }

    @GetMapping(value = "/payment/zk")
    public String paymentzk(){
//        listTest();
        return "springcloud with zookeeper:"+serverPort+"\t"+ UUID.randomUUID().toString();
    }



    int version = 0;
    /**
     * 迭代获取父节点的子节点
     */
    public ArrayList<String> iterChildNodeList(String parentNodeName, ZooKeeper zooKeeper) {
        ArrayList<String> nodes = new ArrayList<String>();
        if (parentNodeName != null && !parentNodeName.equals("")) {
            try {
                ArrayList<String> childNodeList = (ArrayList<String>) zooKeeper.getChildren(parentNodeName, null);
                if (childNodeList.size() > 0) {
                    System.out.println("父结点:" + parentNodeName);
                    for (String childNode : childNodeList) {
                        String childNodePath = "";
                        if (!parentNodeName.equals("/")) {
                            childNodePath = parentNodeName + "/" + childNode;
                        } else {
                            childNodePath = parentNodeName + childNode;
                        }
                        System.out.println(parentNodeName + "的子节点：" + childNodePath);
                        nodes.addAll(iterChildNodeList(childNodePath, zooKeeper));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nodes;
    }

    /**
     * 获取服务器节点列表
     */
    public void listTest() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.220.131:2181", 10000, null);//192.168.10.201:2181
            String root = "/";
            ArrayList<String> nodes = iterChildNodeList(root, zooKeeper);
            for(String node : nodes){
                log.info(node);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定节点的节点数据
     */
    public void getDataTest() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.44.128:2181", 10000, null);
            String path = "/myZnode";
            String data = new String(zooKeeper.getData(path, null, new Stat()));
            System.out.println(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 节点创建测试
     */
    public void createTest() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.44.128:2181", 10000, null);
            String path = "/my";
            String dataStr = "100";
            byte[] data = dataStr.getBytes();
            //String res;
            //CreateMode:（1）PERSISTENT：持久；（2）PERSISTENT_SEQUENTIAL：持久顺序；（3）EPHEMERAL：临时；（4）EPHEMERAL_SEQUENTIAL：临时顺序。
            if (null==zooKeeper.exists(path, false)) {
                //不存在节点，则新建
                zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            else{
                //存在节点，则删除后新建
                //zooKeeper.delete(path, zooKeeper.exists(path, null).getVersion());
                //zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                //存在节点，则更新数据
                setTest();
                //zooKeeper.setData(path, data, version);
            }
            String newData = new String(zooKeeper.getData(path, null, new Stat()));
            if (newData != null && !newData.equals("")) {
                System.out.println("插入节点为：" + path);
                System.out.println("新插入数据为:" + newData);
            } else {
                System.out.println("创建失败！");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 节点数据更新测试
     */
    public void setTest() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.44.128:2181", 10000, null);
            // 修改的节点
            String path = "/my";
            // 修改的新数据
            byte[] data = new String("man").getBytes();
            // 未修改过版本号为0，修改后版本号自动递增1
            version = zooKeeper.exists(path,true).getVersion();
            System.out.println("更新前新版本号为:" + version);
            // 修改之前的节点数据
            String beforeData = new String(zooKeeper.getData(path, null, new Stat()));
            System.out.println("更新之前数据为:" + beforeData);
            //更新
            Stat stat = zooKeeper.setData(path, data, version);
            // 修改之后的版本号
            version = stat.getVersion();
            System.out.println("更新数据后新版本号为:" + version);
            // 修改之后的节点数据
            String afterData = new String(zooKeeper.getData(path, null, new Stat()));
            System.out.println("更新之后数据为:" + afterData);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * 节点删除测试
     */
    public void deleteTest() {
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.44.128:2181", 10000, null);
            // 删除的节点
            String path = "/my";
            Stat stat = zooKeeper.exists(path, null);
            // 删除的节点的版本
            int version = stat.getVersion();
            // 执行删除
            zooKeeper.delete(path, version);
            //zooKeeper.delete("/my", zooKeeper.exists(path, null).getVersion());
            System.out.println("该节点已删除，当前节点如下：");
            // 删除后列出最新的zk节点结构
            listTest();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
