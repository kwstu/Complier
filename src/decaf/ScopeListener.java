package decaf;
import java.util.Stack;

import org.antlr.v4.runtime.tree.TerminalNode;

import decaf.DecafParser.Callout_argContext;
import decaf.DecafParser.ExprContext;
import decaf.DecafParser.Field_nameContext;
import decaf.DecafParser.StatementContext;
import decaf.DecafParser.TypeContext;

import java.io.Console;
import java.util.List;
public class ScopeListener extends DecafParserBaseListener {

	private Stack<Scope> scopes;
	private Scope currentScope;

	public ScopeListener() {
		scopes = new Stack<Scope>();
		scopes.push(new Scope(null));
	}

	public void enterProgram(DecafParser.ProgramContext ctx) {
		scopes.push(new Scope(scopes.peek()));
		this.currentScope = scopes.peek();


		//Check if any methods are defined after main
		List<DecafParser.Method_declContext> methods = ctx.method_decl();

		if (! methods.isEmpty()) {
			if (methods.get(0).ID().toString().equals("main")) {
				
			}else{
				System.out.println("Warning: Methods defined after Main will not be executed.");
			}
		}
	}

	@Override
	public void enterField_decl(DecafParser.Field_declContext ctx) {

		scopes.push(new Scope(scopes.peek()));
		this.currentScope = scopes.peek();

		List<DecafParser.Field_nameContext> fields = ctx.field_name();
		for (DecafParser.Field_nameContext field_name : fields) {
			String varName = field_name.ID().getText();
			
			//If field is contained within current scope
			
			if(this.currentScope.containsKey(varName)){
				System.err.println("Error line " + field_name.getStart().getLine() 
						+ " " +varName + "already defined.");
			} else {
				currentScope.put(varName, new ScopeData());
			}

			//Check array size is greater than or equal to 1
			
			if (field_name.SQBRACKETLEFT() != null && field_name.SQBRACKETRIGHT() != null ) {
				int size = Integer.parseInt(field_name.INT_LITERAL().getText());
				if (size <= 0){
					System.err.println("Error line " + field_name.getStart().getLine() 
							+ " " +varName + "array is" + size +"must be greater than 0");
				}else{
					currentScope.put(varName, new ScopeData());
				}
		}	
		}
		//checkVarName(varName);
	}
	


	public void enterVar_decl(DecafParser.Var_declContext ctx) {
		List<TerminalNode> ids = ctx.ID();

		for(TerminalNode ID:ids){
			if(ID != null && checkVarName(ID.getText())){
				System.out.println("Variable" + ID.getText() + "already in use.");
			}
		}

	}

	public void enterLocation (DecafParser.LocationContext ctx){
		TerminalNode id = ctx.ID();


	}

	public void enterStatment(DecafParser.StatementContext ctx) {
		DecafParser.LocationContext location = ctx.location();
		if (location != null) {
			String varName = location.ID().getText();
			checkVarName(varName);
		}

	

	}



	public void enterMethod_decl (DecafParser.Method_declContext ctx) {
		scopes.push(new Scope(scopes.peek()));
		this.currentScope = scopes.peek();
		//ScopeData sData = currentScope.get(method_name);
		
		String name = ctx.ID().toString();
		
		//If method already exists
		if (this.currentScope.containsKey(name)) {
		System.out.println("Method " + name + " already exists");
	    
		} else {
			currentScope.put(name, new ScopeData(ctx.type().toString()));
		}

			
		
		
		//Check to see if there is a main method at no.1 spot
		List<TerminalNode> methodName = ctx.ID();
		// Method arguments
		
	

		// Checking method return types.
		
		TypeContext methodType = ctx.type(0);
		TerminalNode void_type = ctx.VOID();
		
        for (DecafParser.StatementContext statementContext : ctx.block().statement()) {
        	TerminalNode returnType = statementContext.RETURN();
        	
        	if(void_type != null){
        		currentScope.put(name, new ScopeData(ctx.type().toString()));
        	}
        	
        	if(methodType.equals(returnType) && methodType != null){
        		// Return type OK.
        		currentScope.put(name, new ScopeData(ctx.type().toString()));
        	}else{
        		System.err.println("Error line " + ctx.getStart().getLine() 
						+ " method should return" + returnType + "but returns" + returnType.getText());
        	}
        	
        	if(!statementContext.IF().equals(null)){
        		System.err.println("For");
        	}
        	
        	if(!statementContext.FOR().equals(null) && !statementContext.expr(0).literal().INT_LITERAL().equals(null)){
        		System.err.println("Error line " + ctx.getStart().getLine() 
						+ " ");
        	}
        	
        }
     
		}

		
		public void enterLocation_decl(DecafParser.LocationContext ctx){
			
			scopes.push(new Scope(scopes.peek()));
			this.currentScope = scopes.peek();
			TerminalNode x = ctx.ID();
			if(! checkVarName(x.toString())){
				currentScope.put(x.toString(), new ScopeData());	
			}else{
				System.err.println("Error at line" + ctx.getStart().getLine() 
						+ " " + x + " already defined.");
			}
		}

	public void enterMethod_call(DecafParser.Method_callContext ctx){
		
		scopes.push(new Scope(scopes.peek()));
		this.currentScope = scopes.peek();
		
		//Store method name and search for said method name in current scope
		String method_name = ctx.method_name().getText();
		ScopeData sData = currentScope.get(method_name);
		
		// If method is found in current scope then method exists.
		if(currentScope.containsKey(method_name)){
			System.err.println("Error line " + ctx.getStart().getLine() 
					+ " " + method_name + "does not exist");
		}else{
			//method exists 
		}
		
		
		
		List<ExprContext> params = ctx.expr();
		
		//If method call parameters are the same as method signature.
		if(sData.rType.equals(params)){
			// Arguments match
		}else{
			System.err.println("Error line " + ctx.getStart().getLine() 
					+ " arguments inputted for " + method_name + " do not match method signature");
		}
		
		//If parameter size is indifferent.
		if(sData.arguments.size() != ctx.expr().size()){
			System.err.println("Error line " + ctx.getStart().getLine() 
					+ " " + method_name + "does not contain the same amount of arguments.");
		}

	}


	public void exitMethod_decl(DecafParser.Method_declContext ctx) {
		scopes.pop();
		this.currentScope = scopes.peek();
	}


	private boolean checkVarName(String varName) {
		Scope scope = scopes.peek();
		if(scope.isDeclared(varName)) {
			System.out.println("Oops variable already used : " + varName);
			return true;
			
		}
		else {
			return false;
			
		}
	}

	

}

