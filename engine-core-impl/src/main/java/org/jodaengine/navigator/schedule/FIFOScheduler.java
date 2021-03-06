package org.jodaengine.navigator.schedule;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.jodaengine.ext.AbstractListenable;
import org.jodaengine.ext.listener.AbstractSchedulerListener;
import org.jodaengine.process.token.Token;


/**
 * The Class FIFOScheduler. It is a simple FIFO Scheduler so nothing too interesting going on around here.
 */
public class FIFOScheduler extends AbstractListenable<AbstractSchedulerListener> implements Scheduler {

    /** The process instances we would like to schedule. */
    private List<Token> processtokens;

    /**
     * Instantiates a new simple scheduler queue. Thereby instantiating a synchronized linked list.
     */
    public FIFOScheduler() {

        processtokens = new LinkedList<Token>();
        processtokens = Collections.synchronizedList(processtokens);
    }

    @Override
    public void submit(Token p) {

        changed(new SchedulerEvent(SchedulerAction.SUBMIT, p, processtokens.size()));
        processtokens.add(p);
    }

    @Override
    public Token retrieve() {
        Token removedToken;
        synchronized (this.processtokens) {
            if (this.processtokens.isEmpty()) {
                return null;
            }
            removedToken = processtokens.remove(0);
            
        }
        changed(new SchedulerEvent(SchedulerAction.RETRIEVE, removedToken, processtokens.size()));
        return removedToken;
    }
    
    @Override
    public boolean isEmpty() {
        return this.processtokens.isEmpty();
    }

    @Override
    public void submitAll(List<Token> listOfTokens) {
        this.processtokens.addAll(listOfTokens);  
    }
    
    /**
     * We changed, tell everybody now!.
     *
     * @param event the event
     */
    private void changed(SchedulerEvent event) {
        setChanged();
        notifyObservers(event);
    }

    @Override
    public int size() {

        return processtokens.size();
    }

    @Override
    public boolean remove(Token token) {

        return processtokens.remove(token);
    }

    @Override
    public void releaseLock(Token token) {

        // TODO Auto-generated method stub
        
    }
    
    
}
