package com.wingdao.math.pojo;

import java.util.Arrays;
import java.util.List;

public class Condition {
    //生成的文档标题
    private String headline;
    //式子数量
    private Integer formulaNum;
    //出现哪些运算符号
    private Integer[] signs;
    //运算符号数量范围
    private Integer minSignNum;
    private Integer maxSignNum;
    //整数数量比重 为1-10
    private Integer intWeight;
    //整数值范围
    private Integer minIntNum;
    private Integer maxIntNum;
    //分数值范围
    private Integer minFraNum;
    private Integer maxFraNum;
    //分母范围
    private Integer minDenomNum;
    private Integer maxDenomNum;
    //是否使用带分数
    private boolean mixedFraFlag;
    //式子中的分数是否是最简分数
    private boolean simplifiedFraFlag;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public Integer getFormulaNum() {
        return formulaNum;
    }

    public void setFormulaNum(Integer formulaNum) {
        this.formulaNum = formulaNum;
    }

    public Integer[] getSigns() {
        return signs;
    }

    public void setSigns(Integer[] signs) {
        this.signs = signs;
    }

    public Integer getMinSignNum() {
        return minSignNum;
    }

    public void setMinSignNum(Integer minSignNum) {
        this.minSignNum = minSignNum;
    }

    public Integer getMaxSignNum() {
        return maxSignNum;
    }

    public void setMaxSignNum(Integer maxSignNum) {
        this.maxSignNum = maxSignNum;
    }

    public Integer getIntWeight() {
        return intWeight;
    }

    public void setIntWeight(Integer intWeight) {
        this.intWeight = intWeight;
    }

    public Integer getMinIntNum() {
        return minIntNum;
    }

    public void setMinIntNum(Integer minIntNum) {
        this.minIntNum = minIntNum;
    }

    public Integer getMaxIntNum() {
        return maxIntNum;
    }

    public void setMaxIntNum(Integer maxIntNum) {
        this.maxIntNum = maxIntNum;
    }

    public Integer getMinFraNum() {
        return minFraNum;
    }

    public void setMinFraNum(Integer minFraNum) {
        this.minFraNum = minFraNum;
    }

    public Integer getMaxFraNum() {
        return maxFraNum;
    }

    public void setMaxFraNum(Integer maxFraNum) {
        this.maxFraNum = maxFraNum;
    }

    public Integer getMinDenomNum() {
        return minDenomNum;
    }

    public void setMinDenomNum(Integer minDenomNum) {
        this.minDenomNum = minDenomNum;
    }

    public Integer getMaxDenomNum() {
        return maxDenomNum;
    }

    public void setMaxDenomNum(Integer maxDenomNum) {
        this.maxDenomNum = maxDenomNum;
    }

    public boolean getMixedFraFlag() {
        return mixedFraFlag;
    }

    public void setMixedFraFlag(boolean mixedFraFlag) {
        this.mixedFraFlag = mixedFraFlag;
    }

    public boolean getSimplifiedFraFlag() {
        return simplifiedFraFlag;
    }

    public void setSimplifiedFraFlag(boolean simplifiedFraFlag) {
        this.simplifiedFraFlag = simplifiedFraFlag;
    }

    @Override
    public String toString() {
        return "Condition{" +
                "headline='" + headline + '\'' +
                ", formulaNum=" + formulaNum +
                ", signs=" + Arrays.toString(signs) +
                ", minSignNum=" + minSignNum +
                ", maxSignNum=" + maxSignNum +
                ", intWeight=" + intWeight +
                ", minIntNum=" + minIntNum +
                ", maxIntNum=" + maxIntNum +
                ", minFraNum=" + minFraNum +
                ", maxFraNum=" + maxFraNum +
                ", minDenomNum=" + minDenomNum +
                ", maxDenomNum=" + maxDenomNum +
                ", mixedFraFlag=" + mixedFraFlag +
                ", simplifiedFraFlag=" + simplifiedFraFlag +
                '}';
    }
}
