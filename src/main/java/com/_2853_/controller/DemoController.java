package com._2853_.controller;

import com._2853_.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private DemoService service;

    @GetMapping("/es")
    public String es(String name) {
        service.es(name);
        /**这里返回值默认返回成功，不做任务处理，是否提交和回滚直接查看数据*/
        return "success";
    }

}
