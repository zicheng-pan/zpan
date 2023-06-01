package com.minispring;

import com.minispring.beans.DefaultListableBeanFactory;
import com.minispring.beans.factory.config.BeanDefinition;
import com.minispring.beans.factory.config.BeanFactoryPostProcessor;
import com.minispring.beans.factory.config.ConfigurableListableBeanFactory;
import com.minispring.beans.factory.support.AutowiredAnnotationBeanFactoryPostProcessor;
import com.minispring.beans.factory.xml.XmlBeanDefinitionReader;
import com.minispring.beans.resource.ClassPathXmlResource;
import com.minispring.beans.resource.Resource;
import com.minispring.event.*;
import com.minispring.exception.BeansException;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {


    DefaultListableBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory beanFactory1 = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory1);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = beanFactory1;

        if (isRefresh) {
            try {
                refresh();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher applicationEventPublisher = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(applicationEventPublisher);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanFactoryPostProcessor(this.beanFactory));
    }

    @Override
    public void onRefresh() throws BeansException {
        this.beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }

    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {

    }

    @Override
    public void registerBean(String beanName, Object obj) {

    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactory.addBeanPostProcessor(postProcessor);
    }

}