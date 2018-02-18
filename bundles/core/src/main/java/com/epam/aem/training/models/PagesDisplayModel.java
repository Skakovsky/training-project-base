package com.epam.aem.training.models;

import com.day.cq.wcm.api.Page;
import com.epam.aem.training.utils.Converter;
import com.epam.aem.training.beans.Article;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.*;
import java.util.ArrayList;
import java.util.List;

import static com.epam.aem.training.beans.Constants.*;


@Model(adaptables = Resource.class)
public class PagesDisplayModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagesDisplayModel.class);

    @Inject
    private String behaviour;

    @Inject
    private String number;

    @Inject
    @Optional
    private String dynamicPath;

    @Inject
    @Optional
    private String[] staticPath;

    @Inject
    private ResourceResolver resourceResolver;

    private List<Article> articles;

    private String test;

    public String getBehaviour() {
        return behaviour;
    }

    public String getNumber() {
        return number;
    }

    public String getDynamicPath() {
        return dynamicPath;
    }

    public String[] getStaticPath() {
        return staticPath;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public String getTest() {
        return test;
    }

    @PostConstruct
    public void init() throws RepositoryException {
        LOGGER.info("========== AEM PagesDisplayModel @PostConstruct init() ==========");
        String articleTitle;
        String articleImage;
        String articleText;
        articles = new ArrayList<>();
        if (STATIC_BEHAVIOUR.equals(behaviour) && staticPath != null) {
            LOGGER.info("Model configured to static behaviour.");

            try {
                for (String nodePath : staticPath) {
                    if (resourceResolver.getResource(nodePath).getChild(NODE_CONTENT) != null) {
                        test = resourceResolver.getResource(nodePath).getChild(NODE_CONTENT).getPath();
                        Node node = resourceResolver.getResource(nodePath).getChild(NODE_CONTENT).adaptTo(Node.class);
                        articleTitle = node.getProperty(ARTICLE_TITLE_PROPERTY).getString();
                        articleText = node.getProperty(ARTICLE_TEXT_PROPERTY).getString();
                        articleImage = node.getNode(ARTICLE_IMAGE_PROPERTY).getPath();
                        articles.add(new Article(articleTitle, articleText, articleImage, Converter.convertToEndPath(nodePath)));
                    }
                }
            } catch (RepositoryException e) {
                LOGGER.error("Repository exception : " + e.getMessage());
            }

        }
        if (DYNAMIC_BEHAVIOUR.equals(behaviour) && dynamicPath != null) {
            LOGGER.info("Model configured to dynamic behaviour.");
            try {
                Resource rootResource = resourceResolver.getResource(dynamicPath);
                List<Resource> resources = getChildCQPages(rootResource);
                for (Resource resource : resources) {
                    Page page = resource.adaptTo(Page.class);
                    articleTitle = page.getTitle();
                    articleText = page.getDescription();
                    Resource jcrContent = resource.getChild(NODE_CONTENT);
                    articleImage = jcrContent.getChild(ARTICLE_IMAGE_PROPERTY).getPath();
                    articles.add(new Article(articleTitle, articleText, articleImage, Converter.convertToEndPath(resource.getPath())));
                }
            } catch (NullPointerException e) {
                LOGGER.error("Null pointer exception : " + e.getMessage());
            }
        }
        int i = Integer.parseInt(number);
        if (articles.size() >= i) {
            articles = articles.subList(0, i);
        }
    }

    private List<Resource> getChildCQPages(Resource resource) {
        List<Resource> resources = new ArrayList<>();
        resources.add(resource);
        if (resource.hasChildren()) {
            Iterable<Resource> children = resource.getChildren();
            for (Resource child : children) {
                if (child.isResourceType(TYPE_CQ_PAGE)) {
                    resources.addAll(getChildCQPages(child));
                }
            }
        }
        return resources;
    }
}
