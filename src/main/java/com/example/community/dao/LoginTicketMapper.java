package com.example.community.dao;

import com.example.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

@Mapper
@Deprecated //不再推荐使用
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket(user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true,keyProperty = "id")//自动生成主键，返回给id属性
    int insertLoginTicket(LoginTicket loginTicket);


    //整张表围绕ticket设计进行的,前端只有ticket，其他数据服务端保存
    @Select({
            "select id,user_id,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);


    //修改状态
    @Update({
            "<script>",
            "update login_ticket set status=#{status} ",
            "where ticket=#{ticket}",
            "<if test=\"ticket!=null\">",//仅做动态sql的展示，无实际意义
            "and 1=1",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket, int status);
}
