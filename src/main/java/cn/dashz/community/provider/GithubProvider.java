package cn.dashz.community.provider;

import cn.dashz.community.DTO.AccessTokenDTO;
import cn.dashz.community.DTO.GithubUser;
import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

//泛指组件，当组件不好归类的时候，使用此注解进行标注
@Component
public class GithubProvider {
    //向Github获取accesstoken值
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        //通过okhttp向Github发送请求
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        //创建一个okhttp客户端
        OkHttpClient client = new OkHttpClient();
        //指定发送请求的正文
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        //通过json的形式向github发送数据，请求返回token
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        //尝试接收返回的请求
        try (Response response = client.newCall(request).execute()) {
            //将返回的数据转换成string形式
            String string = response.body().string();
            //获取的数据形式：
            //access_token=74c32923afe90938b7ca478fd7dbbbc059d7e997&scope=user&token_type=bearer
            //将获取的string切割得到token值
            String[] split = string.split("&");
            String string_token = split[0];
            String token = string_token.split("=")[1];
            return token;
        } catch (IOException e) {
            //在命令行打印异常信息在程序中出错的位置及原因
            e.printStackTrace();
        }
        return null;
    }

    //向Github获取User信息
    public GithubUser getUser(String accessToken){
        //新建一个okhttp客户端
        OkHttpClient client = new OkHttpClient();
        //新建一个GET请求
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        //获取github返回的数据
        try (Response response = client.newCall(request).execute();){
            //将返回的数据转换成string类型
            String string = response.body().string();
            //将一个string对象自动转换为一个类对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
