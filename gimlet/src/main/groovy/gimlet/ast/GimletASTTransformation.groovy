package gimlet.ast

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
public class GimletASTTransformation implements ASTTransformation {
    
    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        sourceUnit.getAST()?.getClasses().findAll({ 
            it.superClass.typeClass == groovy.lang.Script 
        }).each({
            it.superClass = new ClassNode(gimlet.GimletBaseScript)
            def methods = it.getDeclaredMethods('run')

            if (!methods.isEmpty()) {
                def method = methods[0]
                def statements = method.code.statements
            
                // add our serve call
                statements.add(createServeStatement())
            }
            
        })
    }

    private Statement createServeStatement() {
        return new ExpressionStatement(
            new MethodCallExpression(
                new VariableExpression("this"),
                new ConstantExpression("serve"),
                ArgumentListExpression.EMPTY_ARGUMENTS
            )
        )
    }
}