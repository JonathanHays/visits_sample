package org.launchcode.docvisiting;
// <!-- 
// created by: Jonathan Hays
//  -->
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "org.launchcode.docvisiting")
public class DOCVisitingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DOCVisitingApplication.class, args);
    }

}
