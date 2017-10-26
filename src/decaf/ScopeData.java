package decaf;

import java.util.List;

import decaf.DecafParser.ExprContext;
import decaf.DecafParser.TypeContext;

public class ScopeData {
	
	String rType;
	List<ExprContext> arguments;
	
	public ScopeData(){
		
	}
	
	public ScopeData(String iType){
		this.rType = iType;
	}
	
	public ScopeData(String iType, List<ExprContext> iArguments){
		this.rType = iType;
		this.arguments = iArguments;
	}
	
}
