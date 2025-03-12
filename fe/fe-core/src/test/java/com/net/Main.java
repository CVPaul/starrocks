package com.net;

import java.net.InetAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        // 测试正向解析
        InetAddress[] addresses = InetAddress.getAllByName("FAKE_HOSTNAME");
        for (InetAddress addr : addresses) {
            System.out.println(addr.getHostName() + " -> " + addr.getHostAddress());
        }

        // 测试反向解析
        InetAddress ip = InetAddress.getByName("1.2.3.4");
        System.out.println(ip.getHostAddress() + " -> " + ip.getHostName());
    }
}