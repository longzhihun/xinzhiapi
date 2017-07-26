package com.xinzhi.controller;

import com.xinzhi.callnettaxi.SendFileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jlguo on 25/07/2017.
 */
@RestController
@RequestMapping("/xinzhi-apis/")
public class TaxiApiController {
    @Autowired
    SendFileData sendFileData;

    final static String url = "https://wycs.bjjtw.gov.cn/receive/app/common/binapi/bjjjkjyxgs";


    @GetMapping(params = {"param_file_path"},value = "/callTaxiApi")
    @ResponseStatus(HttpStatus.OK)
    public String callTaxiApi(@RequestParam("param_file_path") String param_file_path){
        Map<String, String> textMap = new HashMap<String, String>();
        textMap.put("fileName", "luxin.json");
        Map<String, String> fileMap = new HashMap<String, String>();
        fileMap.put("binFile", param_file_path);
        return sendFileData.post(url, textMap, fileMap);
    }


}
