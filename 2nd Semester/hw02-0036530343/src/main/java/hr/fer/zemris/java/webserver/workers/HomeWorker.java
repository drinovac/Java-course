package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class HomeWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {

        if(context.getPersistentParameterNames().contains("bgcolor")) {
            String bgcolor = context.getPersistentParameter("bgcolor");
            context.setTemporaryParameter("background", bgcolor);
        } else {
            context.setTemporaryParameter("background", "7F7F7F");
        }

        context.getDispatcher().dispatchRequest("/private/pages/home.smscr");

    }
}
