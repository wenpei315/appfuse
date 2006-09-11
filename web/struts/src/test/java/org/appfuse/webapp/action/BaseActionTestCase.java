package org.appfuse.webapp.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.appfuse.Constants;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.util.HashMap;

public abstract class BaseActionTestCase extends TestCase {
    protected transient final Log log = LogFactory.getLog(getClass());
    protected static XmlWebApplicationContext ctx;
    protected MockHttpServletRequest request = new MockHttpServletRequest();

    // This static block ensures that Spring's BeanFactory is only loaded
    // once for all tests
    static {
        String[] paths = {
                "classpath*:/applicationContext-dao.xml",
                "classpath*:/applicationContext-service.xml",
                "/WEB-INF/applicationContext-resources.xml",
                "/WEB-INF/action-servlet.xml"
            };
        
        ctx = new XmlWebApplicationContext();
        ctx.setConfigLocations(paths);
        ctx.setServletContext(new MockServletContext(""));
        ctx.refresh();
    }

    protected void setUp() throws Exception {
        LocalizedTextUtil.addDefaultResourceBundle(Constants.BUNDLE_KEY); 
        ActionContext.getContext().setSession(new HashMap());
        
        // change the port on the mailSender so it doesn't conflict with an 
        // existing SMTP server on localhost
        JavaMailSenderImpl mailSender = (JavaMailSenderImpl) ctx.getBean("mailSender");
        mailSender.setPort(2525);
        mailSender.setHost("localhost");

        // populate the request so getRequest().getSession() doesn't fail in BaseAction.java
        ServletActionContext.setRequest(request);
    }
    
    protected void tearDown() throws Exception {
        ActionContext.getContext().setSession(null);   
    }
}
