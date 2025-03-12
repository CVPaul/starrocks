package com.net.resolver;

import java.net.spi.InetAddressResolver;
import java.net.spi.InetAddressResolverProvider;

public class DynamicHostsResolverProvider extends InetAddressResolverProvider {
    @Override
    public InetAddressResolver get(Configuration config) {
        String filePath = System.getenv().getOrDefault("HOSTS_FILE", "etc/hosts");
        System.out.println("Loaded HOSTS_FILE from env: " + filePath); // 添加日志
        try {
            return new DynamicHostsResolver(new HostsFileResolver(filePath), config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load resolver", e);
        }
    }

    @Override
    public String name() {
        return "DynamicHostsResolver";
    }
}