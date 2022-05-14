package com.diplom.black_fox_ex.config;
/*-----------------------------------------------------------------------------------**/
import com.diplom.black_fox_ex.io.FileDirectories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**-----------------------------------------------------------------------------------**/
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}")
    private String uploadPath;

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler(uploadPath + "**")
//                .addResourceLocations("file://" + uploadPath);
//        registry.addResourceHandler("/**").addResourceLocations(
//                "file://" + FileDirectories.USER_IMG);

        registry.addResourceHandler(FileDirectories.USER_IMG.getPath() + "**").addResourceLocations("file://" + FileDirectories.USER_IMG.getPath());
        registry.addResourceHandler(FileDirectories.HISTORY_IMG.getPath() + "**").addResourceLocations("file://" + FileDirectories.HISTORY_IMG.getPath());

    }
}