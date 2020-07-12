package com.ttt.core.bizchain.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author : tutingting
 * @description: bizchain自动配置类
 * @date : 2020/4/22 下午4:27
 */
@Configuration
@EnableConfigurationProperties({BizChainProperties.class})
@ConditionalOnBean({BizChainProperties.class})
public class BizChainAutoConfiguration {
    @Configuration
    @Import({BizChainAutoConfiguration.AutoConfiguredBizChainRegistrar.class})
    public static class MapperScannerRegistrarNotFoundConfiguration {
        public MapperScannerRegistrarNotFoundConfiguration() {
        }

        @PostConstruct
        public void afterPropertiesSet() {
            System.out.println("BizChainAutoConfiguration======bizchain auto configuration finish!");
        }
    }

    public static class AutoConfiguredBizChainRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {
        private BeanFactory beanFactory;
        private ResourceLoader resourceLoader;
        private BizChainProperties bizChainProperties;
        private Environment environment;

        public AutoConfiguredBizChainRegistrar() {
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if(bizChainProperties == null){
                System.out.println("BizChainAutoConfiguration======registerBeanDefinitions not found bizChainProperties");
                return;
            }
            for(ChainConfig chainConfig : bizChainProperties.getChains()){
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(chainConfig.getClazz());
                List<BeanReference> handleBeanReferences = new ManagedList<>(chainConfig.getHandles().size());
                for(String handle : chainConfig.getHandles()){
                    handleBeanReferences.add(new RuntimeBeanReference(handle));
                }
                beanDefinitionBuilder.addPropertyValue("handles", handleBeanReferences);
                registry.registerBeanDefinition(chainConfig.getId(), beanDefinitionBuilder.getBeanDefinition());
            }
        }

        private static <T> T getConfigurationProperties(Environment env, Class<T> cls){
            Binder binder = new Binder(ConfigurationPropertySources.get(env));
            BindResult<T> bindResult = binder.bind(cls.getAnnotation(ConfigurationProperties.class).prefix(), cls);
            return bindResult.orElse(null);
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
            this.environment = beanFactory.getBean(Environment.class);
            this.bizChainProperties = getConfigurationProperties(this.environment, BizChainProperties.class);
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }
}
