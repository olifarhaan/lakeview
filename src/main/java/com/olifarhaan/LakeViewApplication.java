package com.olifarhaan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*

Things to note:
1. Ids are auto generated for all entities, we can set custom ids for entities by using super(id) in the constructor for that entity
2. The Ids of all enums are their names.

*/

@SpringBootApplication
public class LakeViewApplication {

	public static void main(String[] args) {
		SpringApplication.run(LakeViewApplication.class, args);
	}

}
