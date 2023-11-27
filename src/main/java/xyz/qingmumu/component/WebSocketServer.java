package xyz.qingmumu.component;


import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.qingmumu.dao.MessageDao;
import xyz.qingmumu.dao.UserDao;
import xyz.qingmumu.entity.Message;
import xyz.qingmumu.entity.User;
import xyz.qingmumu.util.TimeUtil;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date:2023/11/25 13:36
 * @Created by Muqing
 */
@Component
@ServerEndpoint("/socket/{userid}/{username}/{token}")
@Slf4j
public class WebSocketServer {

    //    用户Session
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();
    //直接使用自动装配无法生效
    private static MessageDao messageDao;
    private static UserDao userDao;

    @Autowired
    public void setMessageDao(MessageDao messageDao) {
        WebSocketServer.messageDao = messageDao;
    }

    @Autowired
    private void setUserDao(UserDao userDao) {
        WebSocketServer.userDao = userDao;
    }

    /***
     * WebSocket 建立连接事件
     * 1.把登录的用户存到 sessionMap 中
     * 2.发送给所有人当前登录人员信息
     * 3.查询100条消息记录并返回
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username, @PathParam("token") String token) throws IOException {
        // 获取指定 token 对应的账号id，如果未登录，则返回 null
        if (StpUtil.getLoginIdByToken(token) != null) {
            // 搜索用户名是否已经存在sessionMap中
            boolean isExist = sessionMap.containsKey(username);
            if (!isExist) {
                sessionMap.put(username, session);
                //返回用户列表及其最新的50条消息记录
                HashMap<String, Object> firstInfo = new HashMap<>();
                LambdaQueryWrapper<Message> messageLambdaQueryWrapper = Wrappers.lambdaQuery();
                //查询时需要最新消息 所以时间降序排列
                messageLambdaQueryWrapper.orderBy(true, false, Message::getSendingTime).last("limit 0,50");
                List<Message> messages = messageDao.selectList(messageLambdaQueryWrapper);
                //返回信息显示时需要时间升序显示 所有需要将list反转
                Collections.reverse(messages);
                firstInfo.put("userList", setUserList());
                firstInfo.put("messages", messages);
                firstInfo.put("joinUser", username);
                //最新50条消息记录返回给当前最新连接用户 而不需要返回给所有用户
                sendMessageById(JSON.toJSONString(firstInfo), username);

                HashMap<String, Object> info = new HashMap<>();
                info.put("userList", setUserList());
                info.put("joinUser", username);
                sendAllMessage(JSON.toJSONString(info));
            }
        } else {
            //未登录
            session.close(new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, "请先登录！"));
        }
    }

    /**
     * WebSocket 关闭连接事件
     * 1.把登出的用户从 sessionMap 中剃除
     * 2.发送给所有人当前登录人员信息
     */
    @OnClose
    public void onClose(@PathParam("username") String username) throws IOException {
        if (username != null) {
            //移除登出用户
            sessionMap.remove(username);
            HashMap<String, Object> userInfo = new HashMap<>();
            //显示离开用户
            userInfo.put("closeUser", username);
            //更新用户列表
            userInfo.put("userList", setUserList());
            sendAllMessage(JSON.toJSONString(userInfo));
        }
    }

    /**
     * WebSocket 接受信息事件
     * 接收处理客户端发来的数据
     *
     * @param msg 信息实体
     */
    @OnMessage
    public void onMessage(String msg, @PathParam("userid") Integer userId) throws IOException {
        User user = userDao.selectById(userId);
        LocalDateTime localDateTime = TimeUtil.getLocalDateTime();
        Message message = new Message(userId, user.getUsername(), user.getAvatar(), localDateTime, msg);
        messageDao.insert(message);
        //发送消息给所有用户
        sendAllMessage(JSON.toJSONString(message));
    }

    /**
     * WebSocket 错误事件
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info(error.toString());
    }

    /**
     * 设置接收消息的用户列表
     *
     * @return 用户列表
     */
    private ArrayList<String> setUserList() {
        return  new ArrayList<>(sessionMap.keySet());
    }

    /**
     * 发送消息到所有用户
     *
     * @param message 消息
     */
    private void sendAllMessage(String message) throws IOException {
        for (Session session : sessionMap.values()) {
            session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 将消息发送给指定用户
     *
     * @param message  消息
     * @param username 指定用户名
     * @throws IOException
     */
    private void sendMessageById(String message, String username) throws IOException {
        for (String name : sessionMap.keySet()) {
            if (name.equals(username)) {
                sessionMap.get(name).getBasicRemote().sendText(message);
            }
        }
    }
}
