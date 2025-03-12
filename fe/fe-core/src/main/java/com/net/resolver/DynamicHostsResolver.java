package com.net.resolver;

import java.net.*;
import java.net.spi.InetAddressResolver;
import java.net.spi.InetAddressResolverProvider;

import java.util.List;
import java.util.stream.Stream;

public class DynamicHostsResolver implements InetAddressResolver {
    private final HostsFileResolver hostsResolver;
    private final InetAddressResolver builtinResolver; // 引用默认解析器

    // 修改构造函数，接收 Configuration 参数
    public DynamicHostsResolver(HostsFileResolver hostsResolver, InetAddressResolverProvider.Configuration configuration) {
        this.hostsResolver = hostsResolver;
        this.builtinResolver = configuration.builtinResolver(); // 获取内置解析器
    }

    @Override
    public Stream<InetAddress> lookupByName(String host, LookupPolicy policy) throws UnknownHostException {
        System.out.println("Custom resolver triggered for host: " + host);
        List<String> ips = hostsResolver.getIpsByHost(host);
        
        if (ips.isEmpty()) {
            // 回退到默认解析器
            System.out.println("Fallback to builtin resolver for: " + host);
            return builtinResolver.lookupByName(host, policy);
        }
        
        return ips.stream().map(ip -> {
            try {
                return InetAddress.getByAddress(host, parseIp(ip));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String lookupByAddress(byte[] addr) throws UnknownHostException {
        String ip = InetAddress.getByAddress(addr).getHostAddress();
        String host = hostsResolver.getHostByIp(ip);
        if (host == null) throw new UnknownHostException();
        return host;
    }

    private static byte[] parseIp(String ip) {
        String[] parts = ip.split("\\.");
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) Integer.parseInt(parts[i]);
        }
        return bytes;
    }
}