package com.wingdao.math.utils;

import com.wingdao.math.pojo.Constant;
import com.wingdao.math.pojo.Fraction;
import com.wingdao.math.pojo.MathNode;
import org.apache.commons.io.FileUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.zeroturnaround.zip.ZipUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DocxFileGenerator {
    //将公式写入word文档中，并返回word文档名称
    public String generateDocx(String headline,List<List<MathNode>> formulas) {
        //创建新的解压后的Docx文档
        String dirName = createNewDocx(Constant.TEMPLATEPATH);
        operateDocXml(Constant.TEMPLATEPATH+"/"+dirName,headline,formulas);
        transformDirToDocx(dirName);
        return dirName;
    }

    //从模板创建新的word文件解压后的文件夹
    public String createNewDocx(String templatePath) {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String dirName = "exercises"+uuid;

        try {
            FileUtils.copyDirectory(new File(templatePath+"/template"),new File(templatePath+"/"+dirName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dirName;
    }

    private void operateDocXml(String path, String headline, List<List<MathNode>> formulas) {
        try {
            SAXReader sax = new SAXReader();
            String xmlPath = path+"/word/document.xml";
            Document document = sax.read(xmlPath);
            HashMap<String, String> namespace = new HashMap<>();
            namespace.put("w","http://schemas.openxmlformats.org/wordprocessingml/2006/main");
            namespace.put("m","http://schemas.openxmlformats.org/officeDocument/2006/math");

            //修改文档标题
            XPath xPath = document.createXPath("//w:body/w:p/w:r/w:t");
            xPath.setNamespaceURIs(namespace);
            Node headlineNode = xPath.selectSingleNode(document);
            headlineNode.setText(headline);

            //添加表格与式子
            xPath = document.createXPath("//w:tbl");
            xPath.setNamespaceURIs(namespace);
            Element tblElement = (Element) xPath.selectSingleNode(document);
            xPath = document.createXPath("//w:tr");
            xPath.setNamespaceURIs(namespace);
            Element trElement = (Element) xPath.selectSingleNode(document);
            int row = formulas.size()%3 == 0 ? formulas.size()/3 - 1 : formulas.size()/3;
            for (int i = 0; i < row; i++) {
                Element clone = (Element) trElement.clone();
                tblElement.add(clone);
            }
            xPath = tblElement.createXPath("//w:tc/w:p");
            xPath.setNamespaceURIs(namespace);
            List<Element> sheets = xPath.selectNodes(tblElement);
            int i = 1;
            for (Element sheet : sheets) {
                if (i <= formulas.size()) {
                    Document doc = DocumentHelper.parseText(getFormulaXmlStr(i, formulas.get(i-1)));
                    i++;
                    sheet.add(doc.getRootElement());
                }
            }

            //在word文档最后面加上答案
            xPath = tblElement.createXPath("//w:body");
            xPath.setNamespaceURIs(namespace);
            Element body = (Element) xPath.selectSingleNode(document);
            addAnswerXmlStr(body,formulas);

            //将节点输出为文件
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("utf-8");
            File file = new File(xmlPath);
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.write(document);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAnswerXmlStr(Element body,List<List<MathNode>> formulas) {
        List<Element> elements = body.elements();
        Element wpElement = elements.get(elements.size()-2);
        String textBeginStr = "<w:r xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:eastAsia=\"微软雅黑\" w:hAnsi=\"Times New Roman\" w:cs=\"Times New Roman\" /><w:szCs w:val=\"21\" /></w:rPr><w:t xml:space=\"preserve\">";
        String textEndStr = "</w:t></w:r>";
        int row = formulas.size()%3 == 0 ? formulas.size()/3 : formulas.size()/3 + 1;
        for (int i = 0; i < row; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(textBeginStr);
            for (int x = 0; x < 3; x++) {
                if (i * 3 + x <= formulas.size() - 1) {
                    Fraction fra = formulas.get(i * 3 + x).get(formulas.get(i * 3 + x).size() - 1).getFraValue();
                    sb.append(i*3+x+1+".  ").append(generateAnswerStr(fra)).append("     ");
                }
            }
            sb.append(textEndStr);
            try {
                Element wrElement = DocumentHelper.parseText(sb.toString()).getRootElement();
                Element newWpElement = (Element) wpElement.clone();
                newWpElement.add(wrElement);
                elements.add(elements.size()-1,newWpElement);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    public String generateAnswerStr(Fraction fra) {
        StringBuilder sb = new StringBuilder();
        if (fra.getM() == 0) {
            if (fra.getN() == 0 || fra.getD()==1) {
                sb.append(fra.getN());
            } else {
                sb.append(fra.getN()).append("/").append(fra.getD());
            }
        } else {
            sb.append(fra.getM()).append("'").append(fra.getN()).append("/").append(fra.getD());
        }
        return sb.toString();
    }

    public String getFormulaXmlStr(int sequence, List<MathNode> formula) {
        String formulaBegin = "<m:oMathPara xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"><m:oMathParaPr><m:jc m:val=\"left\" /></m:oMathParaPr><m:oMath>";
        String formulaEnd = "</m:oMath></m:oMathPara>";
        StringBuilder sb = new StringBuilder();
        sb.append(generateSymbol((sequence)+".  "));
        //formula集合最后两个节点是等号与答案
        for (int i = 0; i < formula.size() -2; i++) {
            MathNode node = formula.get(i);
            String sign = node.getSign();
            Integer intValue = node.getIntValue();
            Fraction fra = node.getFraValue();
            if (sign != null) {
                //符号节点
                if (Constant.LEFT_BRACKET.equals(sign) || Constant.RIGHT_BRACKET.equals(sign)) {
                    sb.append(generateSymbol(sign));
                } else {
                    sb.append(generateSymbol(" "+sign+" "));
                }

            } else if (intValue != null) {
                //整数节点
                sb.append(generateNumber(intValue+""));
            } else {
                //分数节点
                sb.append(generateFraction(fra));
            }
        }
        String formulaBody = sb.toString();
        return generateUnit(formulaBegin,formulaEnd,formulaBody);
    }


    //解析字符串公式为word格式的公式
    public String parseFormulaStr(String formulaStr) {
        String formulaBegin = "<m:oMathPara xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\"><m:oMath>";
        String formulaEnd = "</m:oMath></m:oMathPara>";
        //生成word格式公式体
        StringBuilder stringBuilder = new StringBuilder();
        String[] elementStrs = formulaStr.split("\\s");
        for (String elementStr : elementStrs) {
            if (elementStr.contains("+")
                    || elementStr.contains("\u2212") || elementStr.contains("\u00d7") || elementStr.contains("\u00F7")) {
                //添加运算符号
                stringBuilder.append(generateSymbol(" " + elementStr+ " "));
            } else if (elementStr.contains("(") || elementStr.contains(")")) {
                //添加括号
                stringBuilder.append(generateNumber(elementStr));
            } else {
                //添加数字
                if (!elementStr.contains("/")) {
                    //添加整数
                    stringBuilder.append(generateNumber(elementStr));
                } else {
                    //添加分数
                    if (elementStr.contains("'")) {
                        //添加带分数
                        String[] splits = elementStr.split("'");
                        stringBuilder.append(generateNumber(splits[0]))
                                .append(generateFraction(splits[1]));
                    } else {
                        //添加真分数或假分数
                        stringBuilder.append(generateFraction(elementStr));
                    }
                }
            }
        }
        String fomulaBody = stringBuilder.toString();
        return generateUnit(formulaBegin,formulaEnd,fomulaBody);
    }

    private String generateUnit(String unitBegin, String unitEnd, String... unitBodys) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(unitBegin);
        for (String unitBody : unitBodys) {
            stringBuilder.append(unitBody);
        }
        return stringBuilder.append(unitEnd).toString();
    }

    private String generateNumber(String number) {
        String beginStr = "<m:r><m:rPr><m:nor/></m:rPr><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:cs=\"Times New Roman\"/></w:rPr><m:t>";
        String endStr = "</m:t></m:r>";
        return generateUnit(beginStr,endStr,number);
    }

    private String generateSymbol(String symbol) {
        String beginStr = "<m:r><m:rPr><m:nor/></m:rPr><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:cs=\"Times New Roman\"/></w:rPr><m:t xml:space=\"preserve\">";
        String endStr = "</m:t></m:r>";
        return generateUnit(beginStr,endStr,symbol);
    }

    public String generateFraction(Fraction fra) {
        String fraBeginStr = "<m:f><m:fPr><m:ctrlPr><w:rPr><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\" w:cs=\"Times New Roman\"/><w:i/></w:rPr></m:ctrlPr></m:fPr>";
        String fraEndStr = "</m:f>";
        String numeratorPart = generateUnit("<m:num>", "</m:num>", generateNumber(fra.getN() + ""));
        String denominatorPart = generateUnit("<m:den>", "</m:den>", generateNumber(fra.getD() + ""));
        if (fra.getM() == 0) {
            return generateUnit(fraBeginStr, fraEndStr, numeratorPart, denominatorPart);
        } else {
            return generateNumber(fra.getM()+"") + generateUnit(fraBeginStr, fraEndStr, numeratorPart, denominatorPart);
        }
    }

    public String generateFraction(String fraction) {
        String[] splits = fraction.split("/");
        String fraBeginStr = "<m:f><m:fPr><m:ctrlPr><w:rPr><w:rFonts w:ascii=\"Cambria Math\" w:hAnsi=\"Cambria Math\" w:cs=\"Times New Roman\"/><w:i/></w:rPr></m:ctrlPr></m:fPr>";
        String fraEndStr = "</m:f>";
        String numeratorPart = generateUnit("<m:num>","</m:num>",generateNumber(splits[0]));
        String denominatorPart = generateUnit("<m:den>","</m:den>",generateNumber(splits[1]));
        return generateUnit(fraBeginStr,fraEndStr,numeratorPart,denominatorPart);
    }

    public void transformDirToDocx(String srcDirName) {
        try {
            File srcDir = new File(Constant.TEMPLATEPATH+"/"+srcDirName);
            if (!srcDir.exists()) {
                srcDir.mkdirs();
            }
            File desDir = new File(Constant.DocxDir);
            if (!desDir.exists()) {
                desDir.mkdirs();
            }
            File exercises = new File(desDir, srcDirName+".docx");
            if (!exercises.exists()) {
                exercises.createNewFile();
            }
            ZipUtil.pack(srcDir, exercises);
            FileUtils.deleteDirectory(srcDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
