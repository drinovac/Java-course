package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class BgColorWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String bgcolor = context.getParameter("bgcolor");
        if(bgcolor.length() == 6) {
            context.setPersistentParameter("bgcolor", bgcolor);
            context.setTemporaryParameter("message", "Color changed");
            context.getDispatcher().dispatchRequest("/private/pages/bgcolor.smscr");
        } else {
            context.setTemporaryParameter("message", "Color not changed");
            context.getDispatcher().dispatchRequest("/private/pages/bgcolor.smscr");
        }
    }
}
