package com.wingdao.math.controller;

import com.wingdao.math.component.MathComponent;
import com.wingdao.math.pojo.Condition;
import com.wingdao.math.pojo.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/math")
@CrossOrigin
public class MathController {
    @Autowired
    private MathComponent mathComponent;

    @RequestMapping(value = "/formulaFile", method = RequestMethod.POST)
    public Map generateFormulas(@RequestBody Condition condition) {
        String fileName = mathComponent.getDocxFile(condition);
        Map<String,String> map = new HashMap<>();
        map.put("fileName",fileName);
        return map;
    }

    @RequestMapping(value = "/formulaFile", method = RequestMethod.GET)
    public void downloadFormulaFile(String fileName, HttpServletRequest request, HttpServletResponse response) {
        File file = new File(Constant.DocxDir+"/"+fileName+".docx");
        if (file.exists()) {
            BufferedInputStream bis = null;
            try {
                response.setHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName+".docx", "UTF-8"));
                //实现文件下载
                byte[] buffer = new byte[1024];
                bis = new BufferedInputStream(new FileInputStream(file));
                ServletOutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
