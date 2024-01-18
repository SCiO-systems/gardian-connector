package services.scio.qvantum.process;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ExceptionHandler implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {

        String errorMessage;

        Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Exception.class);
        errorMessage =  exception.getMessage();
        System.out.println("Exception Handler says:\n\t" + errorMessage);

    }

}
