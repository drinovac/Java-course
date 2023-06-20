package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import java.util.Set;
import java.util.SplittableRandom;

public class SumWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {

        int a = context.getParameter("a") == null ? 1 : Integer.parseInt(context.getParameter("a"));
        int b = context.getParameter("b") == null ? 1 : Integer.parseInt(context.getParameter("b"));

        context.setTemporaryParameter("varA", String.valueOf(a));
        context.setTemporaryParameter("varB", String.valueOf(b));

        int zbroj = a + b;

        context.setTemporaryParameter("zbroj", String.valueOf(zbroj));

        String imgName = zbroj % 2 == 0 ? "images/sunset.jpg" : "images/tree.jpg";

        context.setTemporaryParameter("imgName", imgName);

        context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
    }
}
