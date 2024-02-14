package eu.senlainc.course.foodpricing;

import eu.senlainc.course.foodpricing.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FoodpricingApplication extends SpringBootServletInitializer {

    @Autowired
    private EmailSenderService senderService;
    private static final String STARTUP_MESSAGE = "Application started. Performing startup operations...";
    private static final Logger logger = LoggerFactory.getLogger(FoodpricingApplication.class);

    public static void main(String[] args) {
        logger.info(STARTUP_MESSAGE);
        SpringApplication.run(FoodpricingApplication.class, args);
    }
}