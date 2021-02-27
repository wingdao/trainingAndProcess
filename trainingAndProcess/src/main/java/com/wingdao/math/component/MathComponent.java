package com.wingdao.math.component;

import com.wingdao.math.pojo.Condition;
import com.wingdao.math.pojo.MathNode;
import com.wingdao.math.utils.DocxFileGenerator;
import com.wingdao.math.utils.FormulaGenerator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MathComponent {
    public String getDocxFile(Condition condition) {
        FormulaGenerator formulaGenerator = new FormulaGenerator();
        List<List<MathNode>> formulas = new ArrayList<>();
        int formulaNum = condition.getFormulaNum();
        for (int i = 0; i < formulaNum; i++) {
            formulas.add(formulaGenerator.generateFormula(condition));
        }
        DocxFileGenerator docxFileGenerator = new DocxFileGenerator();
        String fileName = docxFileGenerator.generateDocx(condition.getHeadline(),formulas);
        return fileName;
    }
}
