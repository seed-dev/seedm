package com.github.seedm.web.controller;

import com.github.seedm.entities.result.FlagResult;
import com.github.toolkit.core.CodecKit;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录相关处理控制器
 * @author Eugene
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private CodecKit codecKit;

    @RequestMapping(value = "/submit")
    @ResponseBody
    public FlagResult submit(HttpServletRequest request) {
        FlagResult result = new FlagResult();
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(codecKit.encodeBase64(mobile), codecKit.hex(password, CodecKit.ALGORITHMS_MD5));
        try {
            subject.login(usernamePasswordToken);
            result.setFlag(FlagResult.SUCCESS);
            result.setMsg("登录成功");
            return result;
        } catch (UnknownAccountException e) {//错误的账号
            result.setFlag(FlagResult.ERROR);
            result.setMsg("用户登录账号错误");
            logger.error("用户登录账号错误", e);
        } catch (IncorrectCredentialsException e) {//错误的凭证
            result.setFlag(FlagResult.ERROR);
            result.setMsg("用户登录密码错误");
            logger.error("用户登录密码错误", e);
        }
        return result;
    }
}
