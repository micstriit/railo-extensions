package uk.co.pixl8.smtp.gateway;

import java.io.IOException;
import java.util.Map;

import lucee.runtime.exp.PageException;
import lucee.runtime.gateway.Gateway;
import lucee.runtime.gateway.GatewayEngine;


public abstract class GatewaySupport implements Gateway {
	
	private String        id;
	private int           state=STOPPED;
	private GatewayEngine engine;
	

	protected abstract void _doStart() throws PageException;
	protected abstract void _doStop()  throws PageException;
	
	
	@Override
	public void init(GatewayEngine engine, String id, String cfcPath, Map config) throws IOException {
		this.engine=engine;
		this.id=id;
	}
	
	
	@Override
	public void doRestart() {
		doStop();
		doStart();
	}
	
	
	@Override
	public final void doStart() {
		state = STARTING;
		try {
			_doStart();
		    state = RUNNING;
		} 
		catch (Throwable pe) {
			pe.printStackTrace();
			state=FAILED;
		    error(pe.getMessage());
		}
	}
	
	
	@Override
	public final void doStop() {
		state = STOPPING;
		
		try {
			
		    _doStop();
		    state = STOPPED;
		} 
		catch (Throwable pe) {
			
			state=FAILED;
		    error(pe.getMessage());
		}
	}
	

	@Override
	public String getId() {
		return id;
	}
	
	
	@Override
	public int getState() {
		return state;
	}
	
	
	@Override
    public Object getHelper() {
    	return null;
    }


	public void info(String msg) {
		System.out.println( msg );
	}
	
	
	public void error(String msg) {
		System.err.println( msg );
	}

}
