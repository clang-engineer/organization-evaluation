package com.evaluation.function;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class AboutCSV {

    public static List<List<String>> readCsv(MultipartFile uploadFile) {
        List<List<String>> ret = new ArrayList<List<String>>();
        BufferedReader br = null;

        try {
            Charset.forName("UTF-8");
            String line = null;
            InputStream is = uploadFile.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                // csv 1행을 저장
                List<String> tmpList = new ArrayList<String>();
                String array[] = line.split(",");
                // 배열에서 리스트 반환
                tmpList = Arrays.asList(array);
                ret.add(tmpList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
}