package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.nodes.*;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Stack;

public class SmartScriptEngine {

    private DocumentNode documentNode;
    private RequestContext requestContext;
    private ObjectMultistack multistack = new ObjectMultistack();

    private INodeVisitor visitor = new INodeVisitor() {
        @Override
        public void visitTextNode(TextNode node) {
            try {
                requestContext.write(node.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void visitForLoopNode(ForLoopNode node) {
              String variable = node.getVariable().asText();
              ValueWrapper endExpression = new ValueWrapper(node.getEndExpression().asText());
              ValueWrapper stepExpression = new ValueWrapper(node.getStepExpression().asText());

              multistack.push(variable, new ValueWrapper(node.getStartExpression().asText()));

              while (multistack.peek(variable).numCompare(endExpression.doubleValue()) <= 0) {
                  for (int i = 0; i < node.numberOfChildren(); i++) {
                      node.getChild(i).accept(this);
                  }
                  multistack.peek(variable).add(stepExpression.doubleValue());
              }

              multistack.pop(variable);
        }

        @Override
        public void visitEchoNode(EchoNode node) {
            Stack<ValueWrapper> stack = new Stack<>();

            for(Element element: node.getElements()) {
                if(element instanceof ElementConstantDouble) {
                    stack.push(new ValueWrapper(((ElementConstantDouble) element).getValue()));
                } else if(element instanceof ElementConstantInteger) {
                    stack.push(new ValueWrapper(((ElementConstantInteger) element).getValue()));
                } else if(element instanceof ElementString) {
                    stack.push(new ValueWrapper(element.asText()));
                } else if(element instanceof ElementVariable) {
                    stack.push(new ValueWrapper(multistack.peek(element.asText()).getValue()));
                } else if(element instanceof ElementOperator) {
                    ValueWrapper first = stack.pop();
                    ValueWrapper second = stack.pop();

                    if ("+".equals(element.asText())) {
                        first.add(second.doubleValue());
                        stack.push(first);
                    } else if ("-".equals(element.asText())) {
                        first.subtract(second.doubleValue());
                        stack.push(first);
                    } else if ("*".equals(element.asText())) {
                        first.multiply(second.doubleValue());
                        stack.push(first);
                    } else if ("/".equals(element.asText())) {
                        first.divide(second.doubleValue());
                        stack.push(first);
                    }
                } else if (element instanceof ElementFunction) {

                    if ("@sin".equals(element.asText())) {
                        ValueWrapper x = stack.pop();
                        stack.push(new ValueWrapper(Math.sin(x.doubleValue() * Math.PI / 180)));
                    } else if ("@decfmt".equals(element.asText())) {
                        ValueWrapper f = stack.pop();
                        ValueWrapper x = stack.pop();

                        DecimalFormat decimalFormat = new DecimalFormat(f.toString());
                        stack.push(new ValueWrapper(decimalFormat.format(x.doubleValue())));
                    } else if ("@dup".equals(element.asText())) {
                        ValueWrapper x = stack.pop();
                        stack.push(x);
                        stack.push(x);
                    } else if ("@swap".equals(element.asText())) {
                        ValueWrapper a = stack.pop();
                        ValueWrapper b = stack.pop();
                        stack.push(a);
                        stack.push(b);
                    } else if ("@setMimeType".equals(element.asText())) {
                        ValueWrapper x = stack.pop();
                        requestContext.setMimeType(x.toString());
                    } else if ("@paramGet".equals(element.asText())) {
                        ValueWrapper dv = stack.pop();
                        ValueWrapper name = stack.pop();

                        String value = requestContext.getParameter(name.toString());

                        stack.push(value == null ? dv : new ValueWrapper(value));
                    } else if ("@pparamGet".equals(element.asText())) {
                        ValueWrapper dv = stack.pop();
                        ValueWrapper name = stack.pop();

                        String value = requestContext.getPersistentParameter(name.toString());

                        stack.push(value == null ? dv : new ValueWrapper(value));
                    } else if ("@pparamSet".equals(element.asText())) {
                        ValueWrapper name = stack.pop();
                        ValueWrapper value = stack.pop();

                        requestContext.setPersistentParameter(name.toString(), value.toString());
                    } else if ("@pparamDel".equals(element.asText())) {
                        ValueWrapper name = stack.pop();

                        requestContext.removePersistentParameter(name.toString());
                    } else if ("@tparamGet".equals(element.asText())) {
                        ValueWrapper dv = stack.pop();
                        ValueWrapper name = stack.pop();

                        String value = requestContext.getTemporaryParameter(name.toString());

                        stack.push(value == null ? dv : new ValueWrapper(value));
                    } else if ("@tparamSet".equals(element.asText())) {
                        ValueWrapper name = stack.pop();
                        ValueWrapper value = stack.pop();

                        requestContext.setTemporaryParameter(name.toString(), value.toString());
                    } else if ("@tparamDel".equals(element.asText())) {
                        ValueWrapper name = stack.pop();

                        requestContext.removeTemporaryParameter(name.toString());
                    }
                }
            }

            StringBuilder sb = new StringBuilder();

            while(!stack.isEmpty()) {
                sb.insert(0, stack.pop().toString());
            }

            try {
                requestContext.write(sb.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void visitDocumentNode(DocumentNode node) {
            for(int i = 0; i < node.numberOfChildren(); i++) {
                node.getChild(i).accept(visitor);
            }
        }
    };

    public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
        this.documentNode = documentNode;
        this.requestContext = requestContext;
    }

    public void execute() {
        documentNode.accept(visitor);
    }



}
