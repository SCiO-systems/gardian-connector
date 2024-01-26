package services.scio.qvantum.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import services.scio.qvantum.models.resource.Resource;

import java.util.HashMap;

public class ExceptionHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        HashMap error = new HashMap();
        String errorMessage;
        Resource errorbody;

        Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Exception.class);
        errorMessage =  exception.getMessage();
        errorbody = exchange.getIn().getBody(Resource.class);

        error.put("Resource", errorbody);
        error.put("errorMessage", errorMessage);
         System.out.println("Exception Handler says:\n\t" + errorMessage);
        exchange.getIn().setHeader("CamelFileName", errorbody.getId());
        exchange.getIn().setBody(error, HashMap.class);
    }

}
