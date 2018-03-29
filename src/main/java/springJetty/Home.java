package springJetty;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;

@RestController
public class Home implements ApplicationContextAware {

	ApplicationContext applicationContext;
	String value = "Default value";


	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}


	public void setValue(String value) {
		this.value = value;
	}

	@RequestMapping("/")
	public String home()
	{
        return applicationContext.getBean(InjectingValue.class).getValue();
	}

}
