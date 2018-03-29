package springJetty.web;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;

@RestController
public class Home implements ApplicationContextAware {


	String value = "Default value";
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@RequestMapping("/")
	public String home()
	{
		return applicationContext.getBean(test.class).getValue();
	}

}
