package org.vincentyeh.IMG2PDF.framework.handler;

public abstract class Handler<RETURN, DATA> {

    protected Handler<RETURN, DATA> next;

    public Handler(Handler<RETURN, DATA> next) {
        this.next = next;
    }

    protected RETURN doNext(DATA data) throws CantHandleException {
        if (next != null) {
            return next.handle(data);
        }
        throw new CantHandleException("Can't handle.");
    }

    public abstract RETURN handle(DATA data) throws CantHandleException;

}
