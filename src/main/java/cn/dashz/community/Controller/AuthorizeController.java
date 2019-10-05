package cn.dashz.community.Controller;

import cn.dashz.community.DTO.AccessTokenDTO;
import cn.dashz.community.DTO.GithubUser;
import cn.dashz.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    //自动装配
    @Autowired
    private GithubProvider githubProvider;

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
                           @RequestParam(name = "state") String state){
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
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
