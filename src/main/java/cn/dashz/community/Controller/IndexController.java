package cn.dashz.community.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    //指定路由
    @GetMapping("/")
    public String Index(){
        //返回templetes中的页面
        return "index";
    }
}
