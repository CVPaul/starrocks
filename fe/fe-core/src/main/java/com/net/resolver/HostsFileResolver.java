package com.net.resolver;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class HostsFileResolver {
    private final Map<String, List<String>> hostToIps = new HashMap<>();
    private final Map<String, String> ipToHost = new HashMap<>();

    public HostsFileResolver(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) throw new IOException("File not found: " + filePath);
        parseHostsFile(path);
    }

    private void parseHostsFile(Path path) throws IOException {
        Files.readAllLines(path).stream()
            .filter(line -> !line.startsWith("#") && !line.trim().isEmpty())
            .forEach(line -> {
                String[] parts = line.split("\\s+");
                if (parts.length < 2) return;
                String ip = parts[0];
                Arrays.stream(parts).skip(1).forEach(host -> {
                    hostToIps.computeIfAbsent(host, k -> new ArrayList<>()).add(ip);
                    ipToHost.putIfAbsent(ip, host);
                });
            });
    }

    public List<String> getIpsByHost(String host) {
        return hostToIps.getOrDefault(host, Collections.emptyList());
    }

    public String getHostByIp(String ip) {
        return ipToHost.get(ip);
    }
}