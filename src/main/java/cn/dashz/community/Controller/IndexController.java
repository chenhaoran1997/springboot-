package cn.dashz.community.Controller;

import cn.dashz.community.mapper.UserMapper;
import cn.dashz.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;
    //指定路由
    @GetMapping("/")
    public String Index(HttpServletRequest request){
        //通过request获取cookies
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            //找到名字为token的cookie
            if (cookie.getName().equals("token")){
                //获取token的值
                String token = cookie.getValue();
                //通过token值在本地数据库找到该用户
                User user = userMapper.findByToken(token);
                if (user != null){
                    //如果找到该用户，将其加入到session当中
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }

        //返回templetes中的页面
        return "index";
    }
}
