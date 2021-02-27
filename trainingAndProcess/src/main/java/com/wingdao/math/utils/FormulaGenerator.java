package com.wingdao.math.utils;

import com.wingdao.math.pojo.*;

import java.util.ArrayList;
import java.util.List;

public class FormulaGenerator {
    public List<MathNode> generateFormula(Condition condition) {
        int signNum = RandomMathUtils.nextInt(condition.getMaxSignNum()-condition.getMinSignNum()+1) + condition.getMinSignNum();
        MathNode node = build(signNum,condition);
        List<MathNode> formula = new ArrayList<>();
        addNodeToFormula(node,formula);
        formula.add(new MathNode("="));
        formula.add(node);
        return handleFormula(condition,formula);
    }

    //根据条件将式子处理成需要的形式
    private List<MathNode> handleFormula(Condition condition,List<MathNode> formula) {
        for (int i = 0; i < formula.size()-1; i++) {
            MathNode node = formula.get(i);
            Fraction fra = node.getFraValue();
            if (fra != null && node.getIntValue() == null) {
                if (condition.getMixedFraFlag()) {
                    Fraction newFra = Fraction.simplifyFra(fra);
                    fra.setN(newFra.getN());
                    fra.setD(newFra.getD());
                    if (fra.getN() % fra.getD() != 0) {
                        fra.setM(fra.getN() / fra.getD());
                        fra.setN(fra.getN() % fra.getD());
                    } else {
                        node.setIntValue(fra.getN() / fra.getD());
                    }
                }
                if (condition.getSimplifiedFraFlag()) {
                    if (fra.getN() % fra.getD() == 0) {
                        node.setIntValue(fra.getN() / fra.getD());
                    } else {
                        Fraction newFra = Fraction.simplifyFra(fra);
                        fra.setN(newFra.getN());
                        fra.setD(newFra.getD());
                    }
                }
            }
        }
        //将答案约分成最简形式
        if (!condition.getMixedFraFlag()) {
            MathNode node = formula.get(formula.size() - 1);
            Fraction newFra = node.getFraValue();
            node.getFraValue().setN(newFra.getN());
            node.getFraValue().setD(newFra.getD());
        }
        return formula;
    }

    //中序遍历二叉树并将二叉树的值存入formula中
    public void addNodeToFormula(MathNode node,List<MathNode> formula) {
        if (node == null) {
            return;
        }
        if (isLeftNeedBrackets(node)) {
            formula.add(new MathNode(Constant.LEFT_BRACKET));
        }
        addNodeToFormula(node.getLeft(),formula);
        if (isLeftNeedBrackets(node)) {
            formula.add(new MathNode(Constant.RIGHT_BRACKET));
        }
        formula.add(node);
        if (isRightNeedBrackets(node)) {
            formula.add(new MathNode(Constant.LEFT_BRACKET));
        }
        addNodeToFormula(node.getRight(),formula);
        if (isRightNeedBrackets(node)) {
            formula.add(new MathNode(Constant.RIGHT_BRACKET));
        }
    }

    private boolean isLeftNeedBrackets(MathNode node) {
        return (Constant.MULTIPLICATION.equals(node.getSign()) || Constant.DIVISION.equals(node.getSign()))
                && (Constant.ADDITION.equals(node.getLeft().getSign()) || Constant.SUBTRACTION.equals(node.getLeft().getSign()));
    }

    private boolean isRightNeedBrackets(MathNode node) {
        return Constant.DIVISION.equals(node.getSign()) && (node.getRight().getSign()!=null) || (Constant.MULTIPLICATION.equals(node.getSign()) || Constant.SUBTRACTION.equals(node.getSign()))
                && (Constant.ADDITION.equals(node.getRight().getSign()) || Constant.SUBTRACTION.equals(node.getRight().getSign()));
    }

    //递归生成formula二叉树,返回的根节点是一个符号节点
    public MathNode build(int signNum,Condition condition) {
        //如果signNum==0已经到了叶子节点，这是边界条件
        if (signNum == 0) {
            MathNode numNode = RandomMathUtils.nextNum(condition);
            return numNode;
            /*return RandomMathUtils.nextNum(condition);*/
        }
        //从根节点开始创建
        MathNode node = RandomMathUtils.nextSign(condition);
        //创建左节点
        //左节点运算符数量
        int leftSignNum = RandomMathUtils.nextInt(signNum);
        node.setLeft(build(leftSignNum,condition));
        //创建右节点
        //右节点运算符数量
        int rightSignNum = signNum - leftSignNum -1;
        node.setRight(build(rightSignNum,condition));

        //除数为0，重新生成右子树
        if (node.getSign().equals(Constant.DIVISION) && (node.getRight().getFraValue().getN() == 0)) {
            node.setRight(build(rightSignNum,condition));
        }
        //计算左右子树的和
        Fraction value = calculate(node.getSign(), node.getLeft().getFraValue(), node.getRight().getFraValue());
        //出现负数交换运算符号两边的式子
        if (value.isNegative()) {
            swapNode(node);
            value = calculate(node.getSign(), node.getLeft().getFraValue(), node.getRight().getFraValue());
        }
        node.setFraValue(value);
        return node;
    }

    /**
     * 单步计算
     */
    private Fraction calculate(String sign, Fraction left, Fraction right) {
        switch (sign) {
            case Constant.ADDITION:
                return left.add(right);
            case Constant.MULTIPLICATION:
                return left.multiply(right);
            case Constant.SUBTRACTION:
                return left.subtract(right);
            default:
                return left.divide(right);
        }
    }

    /**
     * 交换左右子树
     */
    private void swapNode(MathNode node) {
        if (node != null) {
            MathNode t = node.getLeft();
            node.setLeft(node.getRight());
            node.setRight(t);
        }
    }
}
