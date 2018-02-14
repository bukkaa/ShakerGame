package bukkaa.mediahouse.ShakerGameWS.webservice;

import bukkaa.mediahouse.ShakerGameWS.helpers.FileAccessHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);

        registry.addResourceHandler("/js/**").
                addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/sprite/**")
                .addResourceLocations("file:///" + FileAccessHelper.getSpritesFolderAbsPath());
        registry.addResourceHandler("/back_img/**")
                .addResourceLocations("file:///" + FileAccessHelper.getWebBackgroundFolderAbsPath());
    }
}
