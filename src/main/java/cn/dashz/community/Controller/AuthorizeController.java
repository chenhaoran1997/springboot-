package cn.dashz.community.Controller;

import cn.dashz.community.DTO.AccessTokenDTO;
import cn.dashz.community.DTO.GithubUser;
import cn.dashz.community.mapper.UserMapper;
import cn.dashz.community.model.User;
import cn.dashz.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    //自动装配
    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    //将application.properties中定义的变量引入
    @Value("${github.client_id}")
    private String clientID;
    @Value("${github.Client_secret}")
    private String clientSecret;
    @Value("${github.Redirect_uri}")
    private String redirectUri;

    //指定路由
    @GetMapping("/callback")
    //获取URL中返回的值
    public String callback(@RequestParam(name = "code" ) String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        //创建AccessToken对象并为其指定需要的参数
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientID);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        //通过code值向Github获取access_token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //通过accessToken值向github获取user信息
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null){
            User user = new User();
            //UUID用于生成唯一的ID
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            //String.valueOf用于将其参数强制转换为字符串形式
            user.setAccount_id(String.valueOf(githubUser.getId()));
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            //将获取的用户信息插入数据库中
            userMapper.insert(user);
            //用生成的token设置一个cookie
            response.addCookie(new Cookie("token",token));

            /*//登陆成功，写cookie和session
            request.getSession().setAttribute("user",githubUser);*/
            return "redirect:/";
        }else {
            //登陆失败，重新登录
            return "redirect:/";
        }
    }
}
