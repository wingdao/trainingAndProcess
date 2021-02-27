package com.wingdao.math.pojo;

import com.wingdao.math.utils.RandomMathUtils;

import java.util.ArrayList;
import java.util.List;

public class MathNode {
    //如果是符号节点，这里有值
    private String sign;
    //数字节点，这里表示数字的值，符号节点，这里表示左右两边式子的值
    private Fraction fraValue;
    //整数节点
    private Integer intValue;

    private MathNode left;
    private MathNode right;

    public MathNode() {
    }

    public MathNode(String sign) {
        this.sign = sign;
    }

    public MathNode(Fraction fraValue) {
        this.fraValue = fraValue;
    }

    public MathNode(Fraction fraValue, int intValue) {
        this.fraValue = fraValue;
        this.intValue = intValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Fraction getFraValue() {
        return fraValue;
    }

    public void setFraValue(Fraction fraValue) {
        this.fraValue = fraValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public MathNode getLeft() {
        return left;
    }

    public void setLeft(MathNode left) {
        this.left = left;
    }

    public MathNode getRight() {
        return right;
    }

    public void setRight(MathNode right) {
        this.right = right;
    }
}
