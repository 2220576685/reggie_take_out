package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description:test
 * @Creatr by : jinyu
 * @Date: 2021/12/7 0007 9:59
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}",code);


            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);
            //session.setAttribute(phone, code);

            redisTemplate.opsForValue().set(phone,code,120, TimeUnit.SECONDS);
            return R.success("手机短信验证码发送成功");
        }
        return R.error("验证码发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
//        1). 获取前端传递的手机号和验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
//        2). 从Session中获取到手机号对应的正确的验证码

       // Object codeInSession = session.getAttribute(phone);
       // 2). 从Redis中获取到手机号对应的正确的验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);
//        3). 进行验证码的比对 , 如果比对失败, 直接返回错误信息
        if (codeInSession != null && codeInSession.equals(code)) {

            //删除手机号
            redisTemplate.delete(phone);
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            User user = userService.getOne(queryWrapper);


//        4). 如果比对成功, 需要根据手机号查询当前用户, 如果用户不存在, 则自动注册一个新用户
            if (user==null){
                 user = new User();
                 user.setPhone(phone);
                 user.setStatus(1);
                  userService.save(user);
            }
            session.setAttribute("user",user.getId());

            return R.success(user);
        }

//        5). 将登录用户的ID存储Session中
        return R.error("登录失败");
    }


    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String>loginout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");

        return R.success("退出成功");
    }
}
