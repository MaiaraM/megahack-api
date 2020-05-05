package br.com.megahack.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpringDataPrimaryPostProcessor implements BeanFactoryPostProcessor {

    @Autowired
    private Logger log = LoggerFactory.getLogger(SpringDataPrimaryPostProcessor.class);


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        makeRepositoriesPrimary(getRepositoryBeans(beanFactory));
    }

    protected List<BeanDefinition> getRepositoryBeans(ConfigurableListableBeanFactory beanFactory) {
        List<BeanDefinition> springDataRepositoryDefinitions = new ArrayList<>();
        // For each defined bean...
        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Class<?> beanClass = Class.forName(beanClassName == null ? "" : beanClassName);
                // ...Filter the repositories
                if (isSpringDataJpaRepository(beanClass)) {
                    springDataRepositoryDefinitions.add(beanDefinition);
                }
            } catch (ClassNotFoundException e) {
                // The main application bean and 'null' beans will always throw this exception, anything else should be looked into
                if(beanClassName == null) { continue; }
                log.warn(String.format("Error when trying to create instance of %s", beanClassName));
            }
        }

        return springDataRepositoryDefinitions;
    }

    protected void makeRepositoriesPrimary(List<BeanDefinition> repositoryBeans) {
        // For each repository
        for (BeanDefinition repositoryBeanDefinition : repositoryBeans) {
            String repositoryInterface = (String) repositoryBeanDefinition.getAttribute("factoryBeanObjectType");
            // If it is marked with @Primary, set as primary (bc Spring Data doesn't)
            if (isPrimary(repositoryInterface)) {
                repositoryBeanDefinition.setPrimary(true);
            }
        }
    }

    private boolean isSpringDataJpaRepository(Class<?> beanClass) {
        return JpaRepositoryFactoryBean.class.isAssignableFrom(beanClass);
    }

    private boolean isPrimary(String repositoryInterface) {
        return AnnotationUtils.findAnnotation(getClassSafely(repositoryInterface), Primary.class) != null;
    }

    private Class<?> getClassSafely(String repositoryInterface) {
        try {
            return Class.forName(repositoryInterface);
        } catch (ClassNotFoundException e) {
            throw new ApplicationContextException(String.format("Error when trying to create instance of %s", repositoryInterface), e);
        }
    }
}