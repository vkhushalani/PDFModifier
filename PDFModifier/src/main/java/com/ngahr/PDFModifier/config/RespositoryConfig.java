package com.ngahr.PDFModifier.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.ngahr.PDFModifier.model.WritePDF;


@Configuration
public class RespositoryConfig extends RepositoryRestConfigurerAdapter {
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(WritePDF.class);
        
    }
}
