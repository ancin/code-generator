package io.code;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/****
 * @author songkejun
 */
@SpringBootApplication
@MapperScan("io.code.dao")
public class CoderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoderApplication.class, args);
	}
}
