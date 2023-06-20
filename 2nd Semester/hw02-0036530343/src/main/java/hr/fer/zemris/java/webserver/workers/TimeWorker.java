package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.time.LocalDateTime;

public class TimeWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {

        String urlImage = context.getParameter("bgimage");
        if(urlImage == null) {
            urlImage = Math.random() < 0.5 ? "images/sunset.jpg" : "images/tree.jpg";
        }
        context.setPersistentParameter("bgimage", urlImage);
        context.setTemporaryParameter("time", String.valueOf(LocalDateTime.now()));
        context.getDispatcher().dispatchRequest("/private/pages/time.smscr");
    }
}
