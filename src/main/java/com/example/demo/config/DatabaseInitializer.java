package com.example.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("开始初始化数据库，连接URL: {}", datasourceUrl);
        
        try {
            // 读取init.sql文件
            ClassPathResource resource = new ClassPathResource("init.sql");
            String sqlScript = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()), StandardCharsets.UTF_8);
            
            // 按分号分割SQL语句并执行
            String[] sqlStatements = sqlScript.split(";\\s*\\n");
            
            for (String sql : sqlStatements) {
                sql = sql.trim();
                if (!sql.isEmpty() && !sql.startsWith("--")) {
                    try {
                        log.info("执行SQL: {}", sql);
                        jdbcTemplate.execute(sql);
                        log.info("SQL执行成功");
                    } catch (Exception e) {
                        log.warn("SQL执行失败，继续执行下一条: {}", e.getMessage());
                        // 继续执行下一条SQL
                    }
                }
            }
            
            log.info("数据库初始化完成");
            
        } catch (Exception e) {
            log.error("数据库初始化失败: {}", e.getMessage(), e);
        }
    }
}