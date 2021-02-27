import com.wingdao.math.pojo.Condition;
import com.wingdao.math.pojo.Fraction;
import com.wingdao.math.pojo.MathNode;
import com.wingdao.math.utils.FormulaGenerator;
import org.junit.Before;

import java.util.List;

public class Test {
    private Condition condition = new Condition();

    @Before
    public void before() {
        condition.setHeadline("小学生四则运算");
        condition.setFormulaNum(100);
        condition.setSigns(new Integer[] {1,2,3,4});
        condition.setIntWeight(0);
        condition.setMixedFraFlag(true);
        condition.setSimplifiedFraFlag(true);
        condition.setMinIntNum(0);
        condition.setMaxIntNum(5);
        condition.setMinFraNum(2);
        condition.setMaxFraNum(3);
        condition.setMinSignNum(1);
        condition.setMaxSignNum(1);
        condition.setMinDenomNum(1);
        condition.setMaxDenomNum(5);
    }

    @org.junit.Test
    public void test1() {
        for (int i = 0; i < 20; i++) {
            testGenerateFormula();
        }
    }

    @org.junit.Test
    public void testGenerateFormula() {
        FormulaGenerator formulaGenerator = new FormulaGenerator();
        List<MathNode> formula = formulaGenerator.generateFormula(condition);
        for (int i =0; i < formula.size()-1;i++) {
            MathNode node = formula.get(i);
            if (node.getSign() != null) {
                System.out.print(node.getSign());
            } else if (node.getIntValue() != null) {
                System.out.print(node.getIntValue());
            } else {
                if (node.getFraValue().getM() == 0) {
                    System.out.print(node.getFraValue().getN()+"/"+node.getFraValue().getD());
                } else {
                    System.out.print(node.getFraValue().getM()+"'"+node.getFraValue().getN()+"/"+node.getFraValue().getD());
                }
            }
        }
        Fraction answer = formula.get(formula.size()-1).getFraValue();
        if (answer.getM() == 0) {
            System.out.print(answer.getN()+"/"+answer.getD());
        } else {
            System.out.print(answer.getM()+"'"+answer.getN()+"/"+answer.getD());
        }
        System.out.println();
    }
}
