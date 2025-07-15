import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class MainTest {

    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    @Test
    public void testLiteralBinary(){
        // check literals work correctly
        assertEquals(new MathProblemSolver("1").solve(), "1=1");
        // check addition works with literals
        assertEquals(new MathProblemSolver("1+1").solve(), "1+1=2");
        // check subtraction works with literals
        assertEquals(new MathProblemSolver("1-1").solve(), "1-1=0");
        // check multiplication works with literals
        assertEquals(new MathProblemSolver("2*3").solve(), "2*3=6");
        // check division works with literals
        assertEquals(new MathProblemSolver("100/10").solve(), "100/10=10");
        // check exponents work
        assertEquals(new MathProblemSolver("2^5").solve(), "2^5=32");
        assertEquals(new MathProblemSolver("121^0.5").solve(), "121^0.5=11");
        assertEquals(new MathProblemSolver("121+4^0.5-4").solve(), "121+4^0.5-4=119");

        assertEquals(new MathProblemSolver("2^3^2").solve(), "2^3^2=512");
        assertEquals(new MathProblemSolver("(2^3)^2").solve(), "(2^3)^2=64");

    }
    @Test
    public void testLiteralFactorial(){
        // check factorial works
        assertEquals(new MathProblemSolver("1!").solve(), "1!=1");
        assertEquals(new MathProblemSolver("2!").solve(), "2!=2");
        assertEquals(new MathProblemSolver("3!").solve(), "3!=6");
        assertEquals(new MathProblemSolver("7!").solve(), "7!=5040");
    }
    @Test
    public void testLiteralNegation(){
        // check single literal
        assertEquals(new MathProblemSolver("-5").solve(), "-5=-5");
        // check with binary operations
        assertEquals(new MathProblemSolver("-5+5").solve(), "-5+5=0");
        assertEquals(new MathProblemSolver("-5+-5").solve(), "-5+-5=-10");
        assertEquals(new MathProblemSolver("-10-5").solve(), "-10-5=-15");
        assertEquals(new MathProblemSolver("-11*3").solve(), "-11*3=-33");
        assertEquals(new MathProblemSolver("-12*-3").solve(), "-12*-3=36");
        assertEquals(new MathProblemSolver("100/-10").solve(), "100/-10=-10");
        assertEquals(new MathProblemSolver("-5/-0.5").solve(), "-5/-0.5=10");
    }
    @Test
    public void testLiteralFactorialNegation(){
        // check factorial works
        assertEquals(new MathProblemSolver("-5!").solve(), "-5!=-120");
        assertEquals(new MathProblemSolver("-1!").solve(), "-1!=-1");
    }
    @Test
    public void testGrouping(){
        // check single literal
        assertEquals(new MathProblemSolver("(5)").solve(), "(5)=5");
        assertEquals(new MathProblemSolver("(5)+1").solve(), "(5)+1=6");
        // check single parentheses
        assertEquals(new MathProblemSolver("(5*3)").solve(), "(5*3)=15");
        assertEquals(new MathProblemSolver("(5-4+3/1)").solve(), "(5-4+3/1)=4");
        // check multiple parentheses
        assertEquals(new MathProblemSolver("(9-0+1)*(1+3)").solve(), "(9-0+1)*(1+3)=40");
        assertEquals(new MathProblemSolver("8+(1/2)+(0.5)-9+(5^2)").solve(), "8+(1/2)+(0.5)-9+(5^2)=25");
        assertEquals(new MathProblemSolver("(1+2+3*3)+(7/4)*(1--3)").solve(), "(1+2+3*3)+(7/4)*(1--3)=19");
        // check negated parentheses
        assertEquals(new MathProblemSolver("-(7+4)").solve(), "-(7+4)=-11");
        assertEquals(new MathProblemSolver("5*-(11*2)").solve(), "5*-(11*2)=-110");
        // check factored parentheses
        assertEquals(new MathProblemSolver("(1+2)!+3").solve(), "(1+2)!+3=9");
    }
    @Test
    public void testLiteralFunctions(){
        // check functions work
        assertEquals(new MathProblemSolver("MAX(1,2,3,4,5)").solve(), "max(1,2,3,4,5)=5");
        assertEquals(new MathProblemSolver("Min(100,50)").solve(), "min(100,50)=50");

        assertEquals(new MathProblemSolver("log(10,100)").solve(), "log(10,100)=2");
        assertEquals(new MathProblemSolver("sqrt(1024)").solve(), "sqrt(1024)=32");

        assertEquals(new MathProblemSolver("ceil(10.100)").solve(), "ceil(10.1)=11");
        assertEquals(new MathProblemSolver("floor(1.9024)").solve(), "floor(1.9024)=1");

        assertEquals(new MathProblemSolver("round(1.5001)").solve(), "round(1.5001)=2");
        assertEquals(new MathProblemSolver("abs(---6)").solve(), "abs(---6)=6");

        // check function stacking
        assertEquals(new MathProblemSolver("round(max(1.49,1.51))").solve(), "round(max(1.49,1.51))=2");
        assertEquals(new MathProblemSolver("min(4,3,2,1,abs(-5), log(10,100))").solve(), "min(4,3,2,1,abs(-5),log(10,100))=1");
        assertEquals(new MathProblemSolver("min(ceil(min(ceil(10.79),10.99)),12)").solve(), "min(ceil(min(ceil(10.79),10.99)),12)=11");

        // check factorial on functions
        assertEquals(new MathProblemSolver("max(5,root(36,2))!").solve(), "max(5,root(36,2))!=720");
        assertEquals(new MathProblemSolver("log(abs(-2),abs(8))!!").solve(), "log(abs(-2),abs(8))!!=720");

        //check negation
        assertEquals(new MathProblemSolver("-max(2,3)+-abs(-3)").solve(), "-max(2,3)+-abs(-3)=-6");
        assertEquals(new MathProblemSolver("-max(800,log(4,12))/-sqrt(64)").solve(), "-max(800,log(4,12))/-sqrt(64)=100");
    }
    @Test
    public void testVectorBinary(){
        // check vectors work correctly
        assertEquals(new MathProblemSolver("(1,1)").solve(), "(1,1)=(1,1)");
        // check addition works with vectors
        assertEquals(new MathProblemSolver("(1,1)+(1,1)").solve(), "(1,1)+(1,1)=(2,2)");
        // check subtraction works with vectors
        assertEquals(new MathProblemSolver("(3,2)-(3,3)").solve(), "(3,2)-(3,3)=(0,-1)");
        // check multiplication works with vectors
        assertEquals(new MathProblemSolver("(3,2,1)*7").solve(), "(3,2,1)*7=(21,14,7)");
        // check division works with vectors
        assertEquals(new MathProblemSolver("(121,99,11,1100)/11").solve(), "(121,99,11,1100)/11=(11,9,1,100)");
        // additional tests
        assertEquals(new MathProblemSolver("(2,7,5,4,8)+5*(1,1,1,1,1)").solve(), "(2,7,5,4,8)+5*(1,1,1,1,1)=(7,12,10,9,13)");
    }

    @Test
    public void testVectorNegation(){
        // check vectors negation
        assertEquals(new MathProblemSolver("-(1,1)").solve(), "-(1,1)=(-1,-1)");
        assertEquals(new MathProblemSolver("(5,3)+-(2,3)").solve(), "(5,3)+-(2,3)=(3,0)");
    }
}
